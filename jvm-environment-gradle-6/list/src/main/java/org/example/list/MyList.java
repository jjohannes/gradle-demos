package org.example.list;

import com.google.common.collect.ImmutableList;

public class MyList<E> {
    private final ImmutableList<E> delegate;

    public MyList(ImmutableList<E> content) {
        delegate = content;
    }

    public static <E>MyList<E> build(E[] e) {
        return new MyList<E>(ImmutableList.copyOf(e));
    }

    public Iterable<E> all() {
        return delegate;
    }

}
