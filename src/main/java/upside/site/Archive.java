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

import java.net.URL;

import upside.utils.Utils;

public class Archive extends AbstractSiteElement {

    private String path;
    private String url;

    public Archive(String path, String url) {
        this.path = path;
        this.url = url;
    }

    public Archive withAbsoluteUrl(URL base) {
        if (hasAbsoluteUrl())
            throw new IllegalStateException("URL is already absolute");
        return new Archive(path, Utils.absoluteUrl(base, url));
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasAbsoluteUrl() {
        return Utils.isAbsolute(url);
    }

    @Override
    public boolean equals(Object o) {
        // eclipse uses the path as a key, so that defines identity.
        // XXX: it's possible that the same path is used by different plug-ins. This can
        // only be worked around by taking control over features.xml and re-mapping the
        // plugin paths.
        return maybeEquals(o) && ((Archive)o).getPath().equals(getPath());
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

    @Override
    public boolean deepEquals(SiteElement se) {
        if (maybeEquals(se)) {
            Archive a = (Archive)se;
            return eq(getUrl(), a.getUrl())
                && eq(getPath(), a.getPath());
        }
        return false;
    }
}
