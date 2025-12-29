package com.logistics.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        if (value == null) {
            return null;
        }

        String cleanValue = value;

        cleanValue = cleanValue.replaceAll("<", "&lt;");
        cleanValue = cleanValue.replaceAll(">", "&gt;");
        cleanValue = cleanValue.replaceAll("\\(", "&#40;");
        cleanValue = cleanValue.replaceAll("\\)", "&#41;");
        cleanValue = cleanValue.replaceAll("'", "&#39;");
        cleanValue = cleanValue.replaceAll("\"", "&quot;");
        cleanValue = cleanValue.replaceAll("eval\\((.*)\\)", "");
        cleanValue = cleanValue.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        cleanValue = cleanValue.replaceAll("(?i)script", "");
        cleanValue = cleanValue.replaceAll("(?i)onerror", "");
        cleanValue = cleanValue.replaceAll("(?i)onload", "");
        cleanValue = cleanValue.replaceAll("(?i)onclick", "");
        cleanValue = cleanValue.replaceAll("(?i)onmouseover", "");
        cleanValue = cleanValue.replaceAll("(?i)onfocus", "");
        cleanValue = cleanValue.replaceAll("(?i)onblur", "");
        cleanValue = cleanValue.replaceAll("(?i)onkeydown", "");
        cleanValue = cleanValue.replaceAll("(?i)onkeyup", "");
        cleanValue = cleanValue.replaceAll("(?i)onkeypress", "");
        cleanValue = cleanValue.replaceAll("(?i)onsubmit", "");
        cleanValue = cleanValue.replaceAll("(?i)onreset", "");
        cleanValue = cleanValue.replaceAll("(?i)onchange", "");
        cleanValue = cleanValue.replaceAll("(?i)onselect", "");

        return cleanValue;
    }
}
