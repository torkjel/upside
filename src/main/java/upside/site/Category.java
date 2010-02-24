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

public class Category extends AbstractSiteElement {

    private String name;
    private String label;
    private String description;

    public Category(String name, String label, String description) {
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        return maybeEquals(o) && ((Category)o).getName().equals(getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + ":[" +
            "name:" + name + ", " +
            "label:" + label + ", " +
            "description:" + description + "]";
    }

    @Override
    public boolean deepEquals(SiteElement se) {
        if (maybeEquals(se)) {
            Category c = (Category)se;
            return eq(getName(), c.getName())
                && eq(getLabel(), c.getLabel())
                && eq(getDescription(), c.getDescription());
        }
        return false;
    }

}
