package com.nuchange.psianalytics.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {

    public static String format(Date date, Locale locale, Formatter.FORMAT_TYPE type) {
        if(date != null && locale != null && type != null) {
            DateFormat dateFormat = null;
            if(type == Formatter.FORMAT_TYPE.TIMESTAMP) {
                dateFormat = DateFormat.getDateTimeInstance(1, 1, locale);
            } else if(type == Formatter.FORMAT_TYPE.TIME) {
                dateFormat = DateFormat.getTimeInstance(2, locale);
            } else {
                dateFormat = DateFormat.getDateInstance(3, locale);
            }

            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    public static enum FORMAT_TYPE {
        DATE,
        TIME,
        TIMESTAMP;

        private FORMAT_TYPE() {
        }
    }
}
