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
