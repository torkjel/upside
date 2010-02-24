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
            return new InputSource(getClass().getResourceAsStream(id));
        }

    }
}
