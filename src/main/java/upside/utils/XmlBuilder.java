package upside.utils;

import java.util.Stack;

public class XmlBuilder {
    private StringBuilder sb = new StringBuilder();

    private State state = new State(null, -2);
    private Stack<State> stack = new Stack<State>();

    public XmlBuilder() {
    }

    public XmlBuilder open(String name) {
        if (state.tag != null && state.attr)
            sb.append(">");
        state.content = true;
        state.attr = false;
        stack.push(state);

        state = new State(name, state.indent + 2);
        if (state.tag != null)
            sb.append("\n");
        sb.append(indent()).append("<").append(name);

        return this;
    }

    public XmlBuilder attr(String name, Object value) {
        sb.append(" ").append(name).append("=\"").append(value).append("\"");
        return this;
    }

    public XmlBuilder text(String text) {
        if (state.attr)
            sb.append(">");
        sb.append("\n").append(indent()).append(text);
        state.content = true;
        state.attr = false;
        return this;
    }

    public XmlBuilder close() {
        if (state.content)
            sb.append("\n").append(indent()).append("</").append(state.tag).append(">");
        else sb.append("/>");
        state = stack.pop();
        return this;
    }

    private String indent() {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < state.indent; n++)
            sb.append(' ');
        return sb.toString();
    }

    private static class State {
        String tag;
        int indent;
        boolean content;
        boolean attr = true;

        State(String tag, int indent) {
            this.tag = tag;
            this.indent = indent;
        }
    }

    public String toString() {
        return sb.toString();
    }


}
