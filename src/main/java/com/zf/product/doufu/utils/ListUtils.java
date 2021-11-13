package com.zf.product.doufu.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ListUtils {

    public static <T> ArrayList<T> asList(T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    public static <T> ArrayList<ArrayList<T>> asList(int count, T... a) {
        ArrayList<ArrayList<T>> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(ListUtils.asList(a));
        }
        return result;
    }

    public static <T> ArrayList<ArrayList<T>> asList(int count, ArrayList<T> arrayList) {
        ArrayList<ArrayList<T>> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(arrayList);
        }
        return result;
    }

}
