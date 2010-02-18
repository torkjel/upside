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
        String description = getChildElement(fsElem, "description").getTextContent();
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
        String catDescription = getChildElement(fcElem, "description").getTextContent();
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
