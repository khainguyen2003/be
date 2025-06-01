package com.datn.motchill.common.converters;

import com.datn.motchill.common.utils.Constants;
import org.modelmapper.AbstractConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToStringConverter extends AbstractConverter<Date, String> {

    @Override
    protected String convert(Date source) {
        if (source == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormatType.DD_MM_YYYY_HH_MM_SS2);
        return sdf.format(source);
    }
}