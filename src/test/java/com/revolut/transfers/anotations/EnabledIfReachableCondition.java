package com.revolut.transfers.anotations;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.String.format;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

public class EnabledIfReachableCondition implements ExecutionCondition {

    private static final ConditionEvaluationResult ENABLED_BY_DEFAULT =
            enabled(
                    "@EnabledIfReachable is not present");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(
            ExtensionContext context) {
        AnnotatedElement element = context
                .getElement()
                .orElseThrow(IllegalStateException::new);
        return findAnnotation(element, EnabledIfReachable.class)
                .map(annotation -> disableIfUnreachable(annotation, element))
                .orElse(ENABLED_BY_DEFAULT);
    }

    private ConditionEvaluationResult disableIfUnreachable(
            EnabledIfReachable annotation, AnnotatedElement element) {
        String host = annotation.host();
        int port = annotation.port();
        int timeoutMillis = annotation.timeoutMillis();
        boolean reachable = pingUrl(host,port, timeoutMillis);
        if (reachable)
            return enabled(format(
                    "%s is enabled because %s is reachable",
                    element, host + port));
        else
            return disabled(format(
                    "%s is disabled because %s could not be reached in %dms",
                    element,  host + port, timeoutMillis));
    }

    private boolean pingUrl(String host,int port , int timeoutMillis){

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMillis);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }


    }

}