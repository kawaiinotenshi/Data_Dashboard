package com.logistics.config;

import com.p6spy.engine.spy.appender.StdoutLogger;

public class P6SpyLogger extends StdoutLogger {
    @Override
    public void logText(String text) {
        if (text != null && !text.trim().isEmpty()) {
            super.logText(formatSql(text));
        }
    }

    private String formatSql(String sql) {
        if (sql.contains("Execute SQL:")) {
            return sql;
        }
        return sql;
    }
}
