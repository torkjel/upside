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
                "<a href=\"" + config.getBaseUrl() + fs.getName() + "\">" +
                fs.getName() + "</a><br>" +
                "<i>" + fs.getDescription() + "</i>");
        }
        html.close().close().close();

        write(html.toString());
    }

}
