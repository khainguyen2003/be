package com.datn.motchill.admin.common.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
    public static final Pattern NONLATIN = Pattern.compile("[^\\w_-]");
    public static final Pattern SLUG_SEPARATORS = Pattern.compile("[\\s\\p{Punct}&&[^-]]");

    public static String createSlug(String input) {
        String noseparators = StringUtils.SLUG_SEPARATORS.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noseparators, Normalizer.Form.NFD);
        String slug = StringUtils.NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}","-").replaceAll("^-|-$","");
    }
}
