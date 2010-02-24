/*
 * Copyright 2010 Torkjel Hongve (torkjelh@conduct.no)
 *
 * This file is part of Upside.
 *
 * Upside is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Upside is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Upside.  If not, see <http://www.gnu.org/licenses/>.
 */
package upside.federation;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import upside.utils.AbstractParser;
import upside.utils.Exceptions;

public class ConfigParser extends AbstractParser {

    public ConfigParser(InputStream in) {
        super(in);
    }

    public Config parse() {
        String baseUrl = root().getAttribute("base-url");
        Map<String, URL> originSites = parseOriginSites();
        Set<FederatedSite> federatedSites = parseFederatedSites();
        return new Config(baseUrl, originSites, federatedSites);
    }

    private Map<String, URL> parseOriginSites() {
        Map<String, URL> originSites = new HashMap<String, URL>();
        List<Element> siteElems = getChildElements(root(), "site");
        for (Element se : siteElems) {
            try {
                originSites.put(se.getAttribute("name"), new URL(se.getAttribute("url")));
            } catch (MalformedURLException e) {
                throw Exceptions.re(e);
            }
        }
        return originSites;
    }

    private Set<FederatedSite> parseFederatedSites() {
        Set<FederatedSite> federatedSites = new HashSet<FederatedSite>();
        for (Element fsElem : getChildElements(root(), "federated-site"))
            federatedSites.add(parseFederatedSite(fsElem));
        return federatedSites;
    }

    private FederatedSite parseFederatedSite(Element fsElem) {
        String name = fsElem.getAttribute("name");
        Element descElem = getChildElement(fsElem, "description");
        String description = descElem != null ? descElem.getTextContent() : null;
        Set<FederatedCategory> categories = parseFederatedCategories(fsElem);
        Set<Include> includes = parseIncludes(fsElem);
        return new FederatedSite(name, description, categories, includes);
    }

    private Set<FederatedCategory> parseFederatedCategories(Element fsElem) {
        Set<FederatedCategory> categories = new HashSet<FederatedCategory>();
        for (Element fcElem : getChildElements(fsElem, "category"))
            categories.add(parseFederatedCategory(fcElem));
        return categories;
    }

    private FederatedCategory parseFederatedCategory(Element fcElem) {
        String catName = fcElem.getAttribute("name");
        Element descElem = getChildElement(fcElem, "description");
        String catDescription = descElem != null ? descElem.getTextContent() : null;
        Set<Include> includes = parseIncludes(fcElem);
        return new FederatedCategory(catName, catDescription, includes);
    }

    private Set<Include> parseIncludes(Element fcElem) {
        Set<Include> includes = new HashSet<Include>();
        for (Element incElem : getChildElements(fcElem, "include"))
            includes.add(parseInclude(incElem));
        return includes;
    }

    private Include parseInclude(Element incElem) {
        String site = incElem.getAttribute("site");
        boolean keepCategories = "true".equals(incElem.getAttribute("keep-categories"));
        if (incElem.hasAttribute("feature")) {
            String version = incElem.hasAttribute("version")
                ? incElem.getAttribute("version")
                : null;
            return Include.newInclude(site, keepCategories).feature(incElem.getAttribute("feature"), version);
        } else if (incElem.hasAttribute("category"))
            return Include.newInclude(site, keepCategories).category(incElem.getAttribute("category"));
        else
            return Include.newInclude(site, keepCategories).site();
    }
}
