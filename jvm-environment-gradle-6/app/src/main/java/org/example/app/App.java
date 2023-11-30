package org.example.app;

import org.example.list.MyList;

import static org.example.utilities.StringUtils.join;

public class App {
    public static void main(String[] args) {
        String result = join(MyList.build(new String[] {"Hello", "World"}));
        System.out.println(result);
    }
}
