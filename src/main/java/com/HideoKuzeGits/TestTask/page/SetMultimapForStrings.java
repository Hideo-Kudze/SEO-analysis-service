package com.HideoKuzeGits.TestTask.page;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 04.11.14.
 */
public class SetMultimapForStrings extends HashMap<String, Set<String>> {

    public boolean put(String key, String value) {

        Set<String> set = get(key);

        if (set == null) {
            set = new HashSet<String>();
            put(key, set);
        }

        return set.add(value);

    }
}
