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
        Map<String, URL> originSites = new HashMap<String, URL>();
        Set<FederatedSite> federatedSites = new HashSet<FederatedSite>();

        List<Element> siteElems = getChildElements(root(), "site");
        for (Element se : siteElems) {
            try {
                originSites.put(se.getAttribute("name"), new URL(se.getAttribute("url")));
            } catch (MalformedURLException e) {
                throw Exceptions.re(e);
            }
        }

        for (Element fsElem : getChildElements(root(), "federated-site")) {
            String name = fsElem.getAttribute("name");
            String description = getChildElement(fsElem, "description").getTextContent();
            Set<FederatedCategory> categories = new HashSet<FederatedCategory>();

            for (Element fcElem : getChildElements(fsElem, "category")) {
                String catName = fcElem.getAttribute("name");
                String catDescription = getChildElement(fcElem, "description").getTextContent();

                Set<Include> includes = new HashSet<Include>();
                for (Element incElem : getChildElements(fcElem, "include")) {
                    String site = incElem.getAttribute("site");
                    if (incElem.hasAttribute("feature")) {
                        String version = incElem.hasAttribute("version")
                            ? incElem.getAttribute("version")
                            : null;
                        includes.add(
                            Include.createFeatureInclude(
                                site, incElem.getAttribute("feature"), version));
                    } else if (incElem.hasAttribute("category")) {
                        includes.add(
                            Include.createCategoryInclude(
                                site, incElem.getAttribute("category")));
                    } else
                        includes.add(Include.createSiteInclude(site));
                }
                categories.add(new FederatedCategory(catName, catDescription, includes));
            }

            federatedSites.add(new FederatedSite(name, description, categories));
        }

        return new Config(baseUrl, originSites, federatedSites);
    }
}
