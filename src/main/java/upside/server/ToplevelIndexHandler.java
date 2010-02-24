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

import java.io.OutputStream;

import upside.federation.Config;
import upside.federation.FederatedSite;
import upside.utils.HtmlBuilder;

public class ToplevelIndexHandler extends PageHandler {

    public ToplevelIndexHandler(OutputStream out) {
        super(out);
    }

    @Override
    public void handle() {
        HtmlBuilder html = new HtmlBuilder();

        html.html().head().title("Upside - The update site federator").close();
        html.body();
        html.text(
            "Welcome to <b>Upside - The update site federator</b><p>" +
            "Upside is a tool for creating and publishing Eclipse update sites which dynamically " +
            "merges other update sites.<br>" +
            "See <a href=\"http://github.com/torkjel/upside\">http://github.com/torkjel/upside</a> " +
            "for more information" +
            "<p>" +
            "This Upside installation contains the following update sites:");
        html.ul();
        Config config = fm().getConfig();
        for (FederatedSite fs : config.getFederatedSites()) {
            html.li(
                "<a href=\"" + config.getBaseUrl() + fs.getName() + "/\">" +
                fs.getName() + "</a><br>" +
                "<i>" + fs.getDescription() + "</i>");
        }
        html.close().close().close();

        write(html.toString());
    }

}
