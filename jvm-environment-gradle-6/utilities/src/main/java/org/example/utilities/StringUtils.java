package org.example.utilities;

import org.example.list.MyList;

public class StringUtils {
    public static String join(MyList<String> source) {
        return String.join(" ", source.all());
    }
}
