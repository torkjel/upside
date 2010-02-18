package upside.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import upside.site.SiteParser;

public abstract class AbstractParser {

    private Document doc;

    protected AbstractParser(InputStream in) {
        try {
            this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        } catch (Exception e) {
            throw Exceptions.re(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected Document doc() {
        return doc;
    }

    protected Element root() {
        return doc.getDocumentElement();
    }

    protected Element getChildElement(Element elem, String name) {
        List<Element> children = getChildElements(elem, name);
        return children.size() > 0 ? children.get(0) : null;
    }

    protected List<Element> getChildElements(Element elem, String name) {
        List<Element> children = new ArrayList<Element>();
        NodeList nl = elem.getChildNodes();
        for (int n = 0; n < nl.getLength(); n++) {
            Node node = nl.item(n);
            if (node instanceof Element) {
                Element e = (Element)node;
                if (e.getTagName().equals(name))
                    children.add(e);
            }
        }
        return children;
    }

    protected Element getSingletonElement(String name) {
        NodeList nl = doc.getElementsByTagName(name);
        switch (nl.getLength()) {
        case 1:
            return (Element)nl.item(0);
        case 0:
            return null;
        default:
            throw new IllegalStateException("Not a singleton element: " + name);
        }
    }

}
