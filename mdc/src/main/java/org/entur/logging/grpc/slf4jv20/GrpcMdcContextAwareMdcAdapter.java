package org.entur.logging.grpc.slf4jv20;

import org.slf4j.spi.MDCAdapter;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class GrpcMdcContextAwareMdcAdapter implements MDCAdapter {

    private final MDCAdapter delegate;

    public GrpcMdcContextAwareMdcAdapter(MDCAdapter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void put(String key, String val) {
        GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
        if (grpcMdcContext != null) {
            grpcMdcContext.put(key, val);
        } else {
            delegate.put(key, val);
        }
    }

    @Override
    public String get(String key) {
        GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
        if (grpcMdcContext != null) {
            return grpcMdcContext.get(key);
        }
        return delegate.get(key);
    }

    @Override
    public void remove(String key) {
        GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
        if (grpcMdcContext != null) {
            grpcMdcContext.remove(key);
        } else {
            delegate.remove(key);
        }
    }

    @Override
    public void clear() {
        GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
        if (grpcMdcContext != null) {
            grpcMdcContext.clear();
        } else {
            delegate.clear();
        }
    }

    @Override
    public Map<String, String> getCopyOfContextMap() {
        GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
        if (grpcMdcContext != null) {
            return new HashMap<>(grpcMdcContext.getContext());
        }
        Map<String, String> copyOfContextMap = delegate.getCopyOfContextMap();
        if (copyOfContextMap == null) {
            return Collections.emptyMap();
        }
        return copyOfContextMap;
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        if (contextMap == null) {
            contextMap = new HashMap<>();
        }
        GrpcMdcContext grpcMdcContext = GrpcMdcContext.get();
        if (grpcMdcContext != null) {
            grpcMdcContext.setContext(contextMap);
        } else {
            delegate.setContextMap(contextMap);
        }
    }



    @Override
    public void pushByKey(String key, String value) {
        // XXX
        delegate.pushByKey(key, value);
    }

    @Override
    public String popByKey(String key) {
        // XXX
        return delegate.popByKey(key);
    }

    @Override
    public Deque<String> getCopyOfDequeByKey(String key) {
        // XXX
        return delegate.getCopyOfDequeByKey(key);
    }

    @Override
    public void clearDequeByKey(String key) {
        // XXX
        delegate.clearDequeByKey(key);
    }

}
