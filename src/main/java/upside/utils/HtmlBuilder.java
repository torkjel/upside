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

public class HtmlBuilder extends XmlBuilder {

    private HtmlBuilder tag(String name) {
        return (HtmlBuilder)open(name);
    }

    public HtmlBuilder html() {
        return tag("html");
    }

    public HtmlBuilder head() {
        return tag("head");
    }

    public HtmlBuilder body() {
        return tag("body");
    }

    public HtmlBuilder ul() {
        return tag("ul");
    }

    public HtmlBuilder li(String data) {
        return (HtmlBuilder)tag("li").text(data).close();
    }

    public HtmlBuilder title(String title) {
        return (HtmlBuilder)open("title").text(title).close();
    }

}
