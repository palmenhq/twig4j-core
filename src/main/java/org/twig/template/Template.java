package org.twig.template;

import java.util.HashMap;

abstract public class Template {
    public String render() {
        return render(new HashMap<>());
    }

    public String render(HashMap<String, String> context) {
        return doRender(context);
    }

    abstract protected String doRender(HashMap<String, String> context);
}
