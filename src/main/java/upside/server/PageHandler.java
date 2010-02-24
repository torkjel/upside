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
