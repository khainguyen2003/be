package com.datn.motchill.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs
 */
public class SlugUtils {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w\\s-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");

    /**
     * Convert a string to a URL-friendly slug.
     * This method:
     * 1. Converts the string to lowercase
     * 2. Normalizes Vietnamese characters
     * 3. Removes all non-latin characters
     * 4. Replaces spaces with hyphens
     * 5. Removes leading and trailing hyphens
     * 6. Reduces multiple hyphens to a single hyphen
     *
     * @param input The string to convert to a slug
     * @return A URL-friendly slug
     */
    public static String slugify(String input) {
        if (input == null) {
            return "";
        }

        // Chuẩn hóa Unicode, chuyển về chữ thường, thay 'đ' thành 'd'
        String normalized = Normalizer
                .normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "") // bỏ dấu
                .toLowerCase(Locale.ROOT)
                .replaceAll("đ", "d");

        // Loại bỏ ký tự không hợp lệ (chỉ giữ chữ, số, khoảng trắng và '-')
        normalized = NONLATIN.matcher(normalized).replaceAll("");

        // Thay khoảng trắng (bao gồm tab, newline) thành dấu '-'
        normalized = WHITESPACE.matcher(normalized).replaceAll("-");

        // Xóa dấu '-' ở đầu hoặc cuối
        normalized = EDGES_DASHES.matcher(normalized).replaceAll("");

        // Gom các dấu '-' liên tiếp thành 1 dấu '-'
        normalized = normalized.replaceAll("-{2,}", "-");

        return normalized;
    }
}
