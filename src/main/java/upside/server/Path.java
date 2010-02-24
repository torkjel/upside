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
package upside.server;

import upside.site.Site;

class Path {

    private static final String INDEX = "index.html";
    private static final String APP_NAME = "upside";

    private String[] path;

    private String siteName;

    private boolean isSite;
    private boolean isSiteIndex;
    private boolean isToplevelIndex;

    public Path(String path) {
        this.path = path.split("/");
        parse();
    }

    public boolean isSite() {
        return isSite;
    }

    public boolean isSiteIndex() {
        return isSiteIndex;
    }

    public boolean isToplevelIndex() {
        return isToplevelIndex;
    }

    public String getSiteName() {
        return siteName;
    }

    public boolean isValid() {
        return isSite || isSiteIndex || isToplevelIndex;
    }

    private void parse() {
        for (int n = 0; n < path.length; n++)
            if (path[n].equals(APP_NAME)) {
                if (n == path.length - 1 || (n == path.length - 2 && path[n + 1].equals(INDEX))) {
                    isToplevelIndex = true;
                    return;
                } else if (n == path.length - 2) {
                    siteName = path[n + 1];
                    isSiteIndex = true;
                    return;
                } else if (n == path.length - 3) {
                    if (path[n + 2].equals(Site.SITEXML))
                        isSite = true;
                    else if (path[n + 2].equals(INDEX))
                        isSiteIndex = true;
                    else
                        continue;
                    siteName = path[n + 1];
                }
            }
    }
}
