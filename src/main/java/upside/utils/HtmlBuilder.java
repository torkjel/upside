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
