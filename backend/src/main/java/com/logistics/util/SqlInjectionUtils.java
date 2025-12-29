package com.logistics.util;

import java.util.regex.Pattern;

public class SqlInjectionUtils {

    private static final Pattern[] SQL_INJECTION_PATTERNS = {
        Pattern.compile("('|(\\-\\-)|(;)|(\\||\\|)|(\\*|\\*)|(%27)|(%3B)|(%7C)|(%2A))", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|TRUNCATE|EXEC|UNION|EXECUTE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(OR|AND|XOR|NOT)\\b\\s*\\d+\\s*=\\s*\\d+)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(OR|AND|XOR|NOT)\\b\\s*'.*?'\\s*=\\s*'.*?')", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(OR|AND|XOR|NOT)\\b\\s*\\d+\\s*LIKE\\s*\\d+)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(OR|AND|XOR|NOT)\\b\\s*'.*?'\\s*LIKE\\s*'.*?')", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(WAITFOR|DELAY|SLEEP)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(BENCHMARK|IF)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(CAST|CONVERT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(CHAR|VARCHAR|NVARCHAR|NCHAR)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(HEX|UNHEX)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(LOAD_FILE|INTO\\s+OUTFILE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(SUBSTRING|MID|LEFT|RIGHT|LENGTH|ASCII|ORD)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(CONCAT|GROUP_CONCAT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(COUNT|SUM|AVG|MAX|MIN)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(HAVING|GROUP\\s+BY|ORDER\\s+BY)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(LIMIT|OFFSET)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(IN|EXISTS|BETWEEN|LIKE|IS\\s+NULL)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(INFORMATION_SCHEMA|SYS|MASTER|MSDB)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(TABLES|COLUMNS|SCHEMATA)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(XP_|SP_)\\w+)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(SHUTDOWN|RESTART)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(GRANT|REVOKE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(TRUNCATE|DROP)\\b\\s+(TABLE|DATABASE|INDEX))", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(DECLARE|SET|EXEC)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(@@|@@)\\w+)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(0x[0-9a-fA-F]+)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(CHR|ASCII)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(UNHEX|HEX)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MD5|SHA1|SHA256)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(BASE64_DECODE|BASE64_ENCODE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(UNCOMPRESS|COMPRESS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(DECODE|ENCODE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(AES_DECRYPT|AES_ENCRYPT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(DES_DECRYPT|DES_ENCRYPT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(ENCRYPT|DECRYPT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(PASSWORD|OLD_PASSWORD)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(USER|CURRENT_USER|SYSTEM_USER|SESSION_USER)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(DATABASE|SCHEMA)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(VERSION|@@VERSION)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(HOSTNAME|@@HOSTNAME)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(DATADIR|@@DATADIR)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(BASEDIR|@@BASEDIR)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(PLUGIN_DIR|@@PLUGIN_DIR)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(SOCKET|@@SOCKET)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(PORT|@@PORT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(PID_FILE|@@PID_FILE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(SLAVE_SKIP_ERRORS|@@SLAVE_SKIP_ERRORS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(AUTO_INCREMENT_INCREMENT|@@AUTO_INCREMENT_INCREMENT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(AUTO_INCREMENT_OFFSET|@@AUTO_INCREMENT_OFFSET)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_ALLOWED_PACKET|@@MAX_ALLOWED_PACKET)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONNECTIONS|@@MAX_CONNECTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_USER_CONNECTIONS|@@MAX_USER_CONNECTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_PREPARED_STMT_COUNT|@@MAX_PREPARED_STMT_COUNT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_EXECUTION_TIME|@@MAX_EXECUTION_TIME)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_ERROR_COUNT|@@MAX_ERROR_COUNT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_JOIN_SIZE|@@MAX_JOIN_SIZE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_SORT_LENGTH|@@MAX_SORT_LENGTH)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_SP_RECURSION_DEPTH|@@MAX_SP_RECURSION_DEPTH)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_HEAP_TABLE_SIZE|@@MAX_HEAP_TABLE_SIZE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_TMP_TABLES|@@MAX_TMP_TABLES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_WRITE_LOCK_COUNT|@@MAX_WRITE_LOCK_COUNT)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_LENGTH_FOR_SORT_DATA|@@MAX_LENGTH_FOR_SORT_DATA)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_OPTIMIZER_MEMORY|@@MAX_OPTIMIZER_MEMORY)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_SEEKS_FOR_KEY|@@MAX_SEEKS_FOR_KEY)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_RANGE_CARDINALITY|@@MAX_RANGE_CARDINALITY)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_ROWID_FILTER_SIZE|@@MAX_ROWID_FILTER_SIZE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_INDEXES_PER_TABLE|@@MAX_INDEXES_PER_TABLE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_COLUMNS_PER_TABLE|@@MAX_COLUMNS_PER_TABLE)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TRX|@@MAX_CONCURRENT_TRX)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INSTANCES|@@MAX_CONCURRENT_INSTANCES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_THREADS|@@MAX_CONCURRENT_THREADS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_OPERATIONS|@@MAX_CONCURRENT_OPERATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_REQUESTS|@@MAX_CONCURRENT_REQUESTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CONNECTIONS|@@MAX_CONCURRENT_CONNECTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_USERS|@@MAX_CONCURRENT_USERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SESSIONS|@@MAX_CONCURRENT_SESSIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PROCESSES|@@MAX_CONCURRENT_PROCESSES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_JOBS|@@MAX_CONCURRENT_JOBS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TASKS|@@MAX_CONCURRENT_TASKS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TRANSACTIONS|@@MAX_CONCURRENT_TRANSACTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_QUERIES|@@MAX_CONCURRENT_QUERIES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STATEMENTS|@@MAX_CONCURRENT_STATEMENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_COMMANDS|@@MAX_CONCURRENT_COMMANDS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CALLS|@@MAX_CONCURRENT_CALLS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INVOCATIONS|@@MAX_CONCURRENT_INVOCATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EXECUTIONS|@@MAX_CONCURRENT_EXECUTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_RUNS|@@MAX_CONCURRENT_RUNS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STARTS|@@MAX_CONCURRENT_STARTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STOPS|@@MAX_CONCURRENT_STOPS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PAUSES|@@MAX_CONCURRENT_PAUSES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_RESUMES|@@MAX_CONCURRENT_RESUMES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ABORTS|@@MAX_CONCURRENT_ABORTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CANCELS|@@MAX_CONCURRENT_CANCELS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TIMEOUTS|@@MAX_CONCURRENT_TIMEOUTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ERRORS|@@MAX_CONCURRENT_ERRORS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EXCEPTIONS|@@MAX_CONCURRENT_EXCEPTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_WARNINGS|@@MAX_CONCURRENT_WARNINGS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_NOTICES|@@MAX_CONCURRENT_NOTICES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INFOS|@@MAX_CONCURRENT_INFOS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_DEBUGS|@@MAX_CONCURRENT_DEBUGS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TRACES|@@MAX_CONCURRENT_TRACES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_LOGS|@@MAX_CONCURRENT_LOGS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_METRICS|@@MAX_CONCURRENT_METRICS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STATS|@@MAX_CONCURRENT_STATS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_MONITORS|@@MAX_CONCURRENT_MONITORS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ALERTS|@@MAX_CONCURRENT_ALERTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_NOTIFICATIONS|@@MAX_CONCURRENT_NOTIFICATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EVENTS|@@MAX_CONCURRENT_EVENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_MESSAGES|@@MAX_CONCURRENT_MESSAGES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EMAILS|@@MAX_CONCURRENT_EMAILS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SMS|@@MAX_CONCURRENT_SMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PUSH|@@MAX_CONCURRENT_PUSH)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_WEBHOOKS|@@MAX_CONCURRENT_WEBHOOKS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_APIS|@@MAX_CONCURRENT_APIS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SERVICES|@@MAX_CONCURRENT_SERVICES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_APPLICATIONS|@@MAX_CONCURRENT_APPLICATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SYSTEMS|@@MAX_CONCURRENT_SYSTEMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PLATFORMS|@@MAX_CONCURRENT_PLATFORMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ENVIRONMENTS|@@MAX_CONCURRENT_ENVIRONMENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_REGIONS|@@MAX_CONCURRENT_REGIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ZONES|@@MAX_CONCURRENT_ZONES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CLUSTERS|@@MAX_CONCURRENT_CLUSTERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_NODES|@@MAX_CONCURRENT_NODES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PODS|@@MAX_CONCURRENT_PODS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CONTAINERS|@@MAX_CONCURRENT_CONTAINERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INSTANCES|@@MAX_CONCURRENT_INSTANCES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_VMS|@@MAX_CONCURRENT_VMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SERVERS|@@MAX_CONCURRENT_SERVERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_HOSTS|@@MAX_CONCURRENT_HOSTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_DEVICES|@@MAX_CONCURRENT_DEVICES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CLIENTS|@@MAX_CONCURRENT_CLIENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_USERS|@@MAX_CONCURRENT_USERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SESSIONS|@@MAX_CONCURRENT_SESSIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TRANSACTIONS|@@MAX_CONCURRENT_TRANSACTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_OPERATIONS|@@MAX_CONCURRENT_OPERATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_REQUESTS|@@MAX_CONCURRENT_REQUESTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_RESPONSES|@@MAX_CONCURRENT_RESPONSES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CALLS|@@MAX_CONCURRENT_CALLS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INVOCATIONS|@@MAX_CONCURRENT_INVOCATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EXECUTIONS|@@MAX_CONCURRENT_EXECUTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_RUNS|@@MAX_CONCURRENT_RUNS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STARTS|@@MAX_CONCURRENT_STARTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STOPS|@@MAX_CONCURRENT_STOPS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PAUSES|@@MAX_CONCURRENT_PAUSES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_RESUMES|@@MAX_CONCURRENT_RESUMES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ABORTS|@@MAX_CONCURRENT_ABORTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CANCELS|@@MAX_CONCURRENT_CANCELS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TIMEOUTS|@@MAX_CONCURRENT_TIMEOUTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ERRORS|@@MAX_CONCURRENT_ERRORS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EXCEPTIONS|@@MAX_CONCURRENT_EXCEPTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_WARNINGS|@@MAX_CONCURRENT_WARNINGS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_NOTICES|@@MAX_CONCURRENT_NOTICES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INFOS|@@MAX_CONCURRENT_INFOS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_DEBUGS|@@MAX_CONCURRENT_DEBUGS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TRACES|@@MAX_CONCURRENT_TRACES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_LOGS|@@MAX_CONCURRENT_LOGS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_METRICS|@@MAX_CONCURRENT_METRICS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_STATS|@@MAX_CONCURRENT_STATS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_MONITORS|@@MAX_CONCURRENT_MONITORS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ALERTS|@@MAX_CONCURRENT_ALERTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_NOTIFICATIONS|@@MAX_CONCURRENT_NOTIFICATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EVENTS|@@MAX_CONCURRENT_EVENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_MESSAGES|@@MAX_CONCURRENT_MESSAGES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_EMAILS|@@MAX_CONCURRENT_EMAILS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SMS|@@MAX_CONCURRENT_SMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PUSH|@@MAX_CONCURRENT_PUSH)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_WEBHOOKS|@@MAX_CONCURRENT_WEBHOOKS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_APIS|@@MAX_CONCURRENT_APIS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SERVICES|@@MAX_CONCURRENT_SERVICES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_APPLICATIONS|@@MAX_CONCURRENT_APPLICATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SYSTEMS|@@MAX_CONCURRENT_SYSTEMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PLATFORMS|@@MAX_CONCURRENT_PLATFORMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ENVIRONMENTS|@@MAX_CONCURRENT_ENVIRONMENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_REGIONS|@@MAX_CONCURRENT_REGIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_ZONES|@@MAX_CONCURRENT_ZONES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CLUSTERS|@@MAX_CONCURRENT_CLUSTERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_NODES|@@MAX_CONCURRENT_NODES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_PODS|@@MAX_CONCURRENT_PODS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CONTAINERS|@@MAX_CONCURRENT_CONTAINERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_INSTANCES|@@MAX_CONCURRENT_INSTANCES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_VMS|@@MAX_CONCURRENT_VMS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SERVERS|@@MAX_CONCURRENT_SERVERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_HOSTS|@@MAX_CONCURRENT_HOSTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_DEVICES|@@MAX_CONCURRENT_DEVICES)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_CLIENTS|@@MAX_CONCURRENT_CLIENTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_USERS|@@MAX_CONCURRENT_USERS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_SESSIONS|@@MAX_CONCURRENT_SESSIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_TRANSACTIONS|@@MAX_CONCURRENT_TRANSACTIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_OPERATIONS|@@MAX_CONCURRENT_OPERATIONS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(\\b(MAX_CONCURRENT_REQUESTS|@@MAX_CONCURRENT_REQUESTS)\\b)", Pattern.CASE_INSENSITIVE),
        Pattern.Compile("(\\b(MAX_CONCURRENT_RESPONSES|@@MAX_CONCURRENT_RESPONSES)\\b)", Pattern.CASE_INSENSITIVE)
    };

    public static boolean isSafeInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return true;
        }

        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return false;
            }
        }

        return true;
    }

    public static String cleanInput(String input) {
        if (input == null) {
            return null;
        }

        String cleaned = input.trim();

        cleaned = cleaned.replaceAll("'", "''");
        cleaned = cleaned.replaceAll("--", "");
        cleaned = cleaned.replaceAll("#", "");
        cleaned = cleaned.replaceAll("/\\*.*?\\*/", "");
        cleaned = cleaned.replaceAll("\\b(OR|AND|XOR|NOT)\\b\\s*\\d+\\s*=\\s*\\d+", "", Pattern.CASE_INSENSITIVE);
        cleaned = cleaned.replaceAll("\\b(OR|AND|XOR|NOT)\\b\\s*'.*?'\\s*=\\s*'.*?'", "", Pattern.CASE_INSENSITIVE);
        cleaned = cleaned.replaceAll("\\b(OR|AND|XOR|NOT)\\b\\s*\\d+\\s*LIKE\\s*\\d+", "", Pattern.CASE_INSENSITIVE);
        cleaned = cleaned.replaceAll("\\b(OR|AND|XOR|NOT)\\b\\s*'.*?'\\s*LIKE\\s*'.*?'", "", Pattern.CASE_INSENSITIVE);

        return cleaned;
    }

    public static void validateInput(String input, String fieldName) {
        if (!isSafeInput(input)) {
            throw new IllegalArgumentException("Invalid input detected in field: " + fieldName);
        }
    }
}
