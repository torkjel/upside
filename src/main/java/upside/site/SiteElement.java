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

import upside.utils.Utils;

public interface SiteElement {
    boolean equals(Object o);
    int hashCode();
    boolean deepEquals(SiteElement element);
}

abstract class AbstractSiteElement implements SiteElement {

    final boolean maybeEquals(Object o) {
        return o != null && o.getClass().equals(getClass());
    }

    boolean eq(Object o1, Object o2) {
        return Utils.eq(o1, o2);
    }

}