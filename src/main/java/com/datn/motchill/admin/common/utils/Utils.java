package com.datn.motchill.admin.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static Logger log = LogManager.getLogger(Utils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Object", e);
        }
    }

    public static Sort generatedSort(String input) {
        // Nếu input là null hoặc rỗng, trả về sắp xếp mặc định theo "id" giảm dần
        if (input == null || input.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        // Tách các cặp trường và hướng sắp xếp
        String[] sortParams = input.split(",");

        // Danh sách các Sort.Order
        List<Sort.Order> orders = new ArrayList<>();

        // Duyệt qua các cặp trường và hướng sắp xếp
        for (int i = 0; i < sortParams.length; i += 2) {
            String field = sortParams[i].trim(); // Trường cần sắp xếp
            String direction = (i + 1 < sortParams.length) ? sortParams[i + 1].trim() : "asc"; // Hướng sắp xếp, mặc định là "asc"

            // Thêm Sort.Order tương ứng vào danh sách
            if ("desc".equalsIgnoreCase(direction)) {
                orders.add(Sort.Order.desc(field));
            } else {
                orders.add(Sort.Order.asc(field));
            }
        }

        // Trả về Sort với các Sort.Order đã tạo
        return Sort.by(orders);
    }

    public static String extractClientIdAddr(HttpServletRequest request) {
        //client request through proxy
        String ip = request.getHeader("X-Forwarded-For");
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //client request through proxy or balancer
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //client request through proxy or balancer
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //Some old devices send ip
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //Some old devices send ip
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //request by device
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
