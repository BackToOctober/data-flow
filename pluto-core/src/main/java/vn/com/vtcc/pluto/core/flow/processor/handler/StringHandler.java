package vn.com.vtcc.pluto.core.flow.processor.handler;

import vn.com.vtcc.pluto.core.flow.processor.Handler;

public class StringHandler extends Handler<String, String> {
    @Override
    public String handle(String value) {
        if (value == null) {
            return null;
        }
        return value + " - linhnv52";
    }
}
