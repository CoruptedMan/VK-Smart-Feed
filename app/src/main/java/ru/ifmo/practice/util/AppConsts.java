package ru.ifmo.practice.util;

public class AppConsts {
    public static final int     MIN_NOTES_COUNT                 =   6;
    public static final String  REGEX_REFERENCE_PATTERN         =   "\\[(id|club|public)?[0-9]+\\|[^]]+\\]";
    public static final String  REGEX_SEPARATE_HASHTAG_PATTERN  =   "#[А-яёЁA-z0-9_]+";
    public static final String  REGEX_GROUP_HASHTAG_PATTERN     =   "#[А-яёЁA-z0-9_]+@[А-яёЁA-z0-9_]+";
    public static final String  REGEX_WEB_LINK_PATTERN          =   "(^|[?\\s])(?:https?://)?[a-zA-Z0-9_/\\-\\.]+" +
                                                                    "\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*[^?. ]";
    public static final String  REGEX_EMAIL_PATTERN             =   "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)* " +
                                                                    "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                                                                    "(?:[A-Z]{2}|com|org|net|gov|biz|info|name|aero|biz|info|jobs|museum)\\b";
}
