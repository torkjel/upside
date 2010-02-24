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

public class Description extends AbstractSiteElement {
    private String url;
    private String description;

    public Description(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        return maybeEquals(o) && deepEquals((SiteElement)o);
    }

    @Override
    public int hashCode() {
        return getUrl() != null ? getUrl().hashCode() : super.hashCode();
    }

    @Override
    public boolean deepEquals(SiteElement desc) {
        if (maybeEquals(desc)) {
            Description d = (Description)desc;
            return eq(getUrl(), d.getUrl()) && eq(getDescription(), d.getDescription());
        }
        return false;
    }
}
