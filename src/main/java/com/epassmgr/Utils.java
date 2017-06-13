package com.epassmgr;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * Created by USER on 12/06/2017.
 */
public class Utils {

    public static ItemPass[] parse(String jsonContent) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(jsonContent, ItemPass[].class);
    }
}
