package com.example.app;

import com.example.common.Util;

import org.bouncycastle.util.Bytes;

public class Main {

    public void stuff() {
        Util.validate();
        Bytes.xor(1, new byte[]{1}, new byte[]{2}, new byte[]{3});
    }
}
