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

import upside.federation.FederationManager;
import upside.federation.FederationManagerFactory;
import upside.site.Site;
import upside.utils.Exceptions;

abstract class PageHandler {

    private OutputStream out;
    private FederationManager federationManager;

    protected PageHandler(OutputStream out) {
        this.out = out;
    }

    protected OutputStream out() {
        return out;
    }

    protected Site getSite(String siteName) {
        Site merged = fm().getFederatedSite(siteName);
        if (merged == null)
            throw new IllegalArgumentException("Site not found: " + siteName);
        return merged;
    }

    protected FederationManager fm() {
        return federationManager != null
            ? federationManager
            : FederationManagerFactory.getFederationManager();
    }

    protected void setFederationManager(FederationManager fm) {
        this.federationManager = fm;
    }

    protected void write(String data) {
        try {
            out().write(data.getBytes("UTF-8"));
        } catch (Exception e) {
            throw Exceptions.re(e);
        }
    }

    public abstract void handle();
}
