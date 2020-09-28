package vn.com.vtcc.dataflow.flow.processor.handler;

import vn.com.vtcc.dataflow.flow.processor.Handler;

public class StringHandler extends Handler<String, String> {
    @Override
    public String handle(String value) {
        if (value == null) {
            return null;
        }
        return value + " - linhnv52";
    }
}
