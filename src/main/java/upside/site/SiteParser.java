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
package upside.site;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import upside.utils.AbstractParser;

public class SiteParser extends AbstractParser {

    public SiteParser(InputStream in) {
        super(in);
    }

    private Map<String, Category> categoryMap;

    public Site parse() {
        Set<Category> categories = parseCategories();
        categoryMap = map(categories);
        return new Site(parseDescription(), categories, parseFeatures(), parseArchives());
    }

    private Description parseDescription() {
        Element descElem = getChildElement(root(), "description");
        return new Description(descElem.getAttribute("url"), descElem.getTextContent().trim());
    }

    private Set<Feature> parseFeatures() {
        Set<Feature> features = new HashSet<Feature>();
        for (Element e : getChildElements(root(), "feature"))
            features.add(parseFeature(e));
        return features;
    }

    private Feature parseFeature(Element elem) {
        return new Feature(
            elem.getAttribute("url"),
            elem.getAttribute("id"),
            elem.getAttribute("version"),
            elem.hasAttribute("patch") ? Boolean.valueOf(elem.getAttribute("patch")) : false,
            parseFeatureCategories(elem));
    }

    private Set<Category> parseFeatureCategories(Element elem) {
        Collection<Element> catElemes = getChildElements(elem, "category");
        Set<Category> categories = new HashSet<Category>();
        for (Element e : catElemes) {
            Category category = categoryMap.get(e.getAttribute("name"));
            if (category != null)
                categories.add(category);
        }
        return categories;
    }

    private Set<Category> parseCategories() {
        Set<Category> categories = new HashSet<Category>();
        for (Element e : getChildElements(root(), "category-def"))
            categories.add(parseCategory(e));
        return categories;
    }

    private Category parseCategory(Element elem) {
        Element descElem = getChildElement(elem, "description");
        String description = descElem != null ? descElem.getTextContent().trim() : null;
        return new Category(
            elem.getAttribute("name"),
            elem.getAttribute("label"),
            description);
    }

    private Set<Archive> parseArchives() {
        Set<Archive> archives = new HashSet<Archive>();
        for (Element e : getChildElements(root(), "archive"))
            archives.add(parseArchive(e));
        return archives;
    }

    private Archive parseArchive(Element elem) {
        return new Archive(elem.getAttribute("path"), elem.getAttribute("url"));
    }

    private Map<String, Category> map(Set<Category> categories) {
        Map<String, Category> cm = new HashMap<String, Category>();
        for (Category c : categories)
            cm.put(c.getName(), c);
        return cm;
    }
}
