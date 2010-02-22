package upside.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class AbstractParser {

    private Document doc;

    protected AbstractParser(InputStream in) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            builder.setEntityResolver(new DTDResolver());
            this.doc = builder.parse(in);
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

    private class DTDResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
            String id = systemId != null ? systemId : publicId;
            if (id == null) return null;
            if (id.contains("/") && !id.endsWith("/"))
                id = id.substring(id.lastIndexOf("/"));
            System.out.println(id);
            return new InputSource(getClass().getResourceAsStream(id));
        }

    }
}
