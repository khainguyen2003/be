package com.datn.motchill.admin.common.converters;

import com.datn.motchill.admin.common.utils.Constants;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.AbstractConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class StringToDateConverter extends AbstractConverter<String, Date> {

    @Override
    protected Date convert(String source) {
        if (Strings.isBlank(source)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormatType.DD_MM_YYYY_HH_MM_SS2);
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            // Tùy chính sách hệ thống mà bạn có thể throw RuntimeException, hoặc ghi log
            throw new IllegalArgumentException("Định dạng ngày giờ không hợp lệ: " + source, e);
        }
    }
}

