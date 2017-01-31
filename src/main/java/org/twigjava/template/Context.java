package org.twigjava.template;

import java.util.HashMap;

public class Context extends HashMap<String, Object> {
    public Object clone() {
        return super.clone();
    }
}
