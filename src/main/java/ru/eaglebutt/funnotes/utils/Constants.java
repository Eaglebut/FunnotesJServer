package ru.eaglebutt.funnotes.utils;

public class Constants {
    public enum Statuses {
        SUCCESSFUL,
        FAILED,
        FORBIDDEN
    }

    public static class Strings {
        public static class UserStrings {
            public static final String UserNotFoundedExceptionString = "user not founded";
            public static final String tableName = "users";
            public static final String userIDSQLName = "user_id";
            public static final String registrationTimeSQLName = "registration_time";
            public static final String registrationTypeSQLName = "registration_type";
            public static final String emailSQLName = "email";
        }

        public static class GroupStrings {
            public static final String GroupNotFoundedExceptionString = "Group not founded";
        }
    }
}
