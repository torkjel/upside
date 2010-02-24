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

import org.xml.sax.SAXParseException;

public final class Exceptions {
    private Exceptions() { }

    public static RuntimeException re(Exception e) {
        if (e instanceof RuntimeException)
            return (RuntimeException)e;
        else if (e instanceof SAXParseException) {
            SAXParseException sax = (SAXParseException)e;
            return new RuntimeException(sax.getMessage() + "(" +
                "System:" + sax.getSystemId() + ", " +
                "Public:" + sax.getPublicId() + ", " +
                "L:" + sax.getLineNumber() + " C:" + sax.getColumnNumber() + ")",
                e);
        } else
            return new RuntimeException(e);
    }
}
