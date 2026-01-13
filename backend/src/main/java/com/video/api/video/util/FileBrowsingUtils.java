package com.video.api.video.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileBrowsingUtils {
    public static Map<String, String> parseDirectoryName(String directoryName) {
        String title = directoryName, year = null, id = null;

        Pattern pattern = Pattern.compile("^(.+?)(?:\\s*\\((\\d{4})\\))?(?:\\s*\\[(.+?)])?$");
        Matcher matcher = pattern.matcher(directoryName);

        if(matcher.matches()){
            title = matcher.group(1).trim();
            year = matcher.group(2);
            id = matcher.group(3);
        }

        Map<String, String> result = new HashMap<>();
        result.put("title", title);
        result.put("year", year);
        result.put("id", id);

        return result;
    }

    public static List<Map<String, String>> parseDirectories(String directoryName) {
        File[] directories = new File(directoryName).listFiles(File::isDirectory);

        String[] directoriesNames = directories != null ? Arrays.stream(directories).map(File::getName).toArray(String[]::new) : new String[0];

        List<Map<String, String>> parsedDirectories = new ArrayList<>();
        for (String dirName : directoriesNames) {
            parsedDirectories.add(parseDirectoryName(dirName));
        }

        return parsedDirectories;
    }
}
