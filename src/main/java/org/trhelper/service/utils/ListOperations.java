package org.trhelper.service.utils;

import java.util.*;

public class ListOperations {

    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new LinkedHashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

}
