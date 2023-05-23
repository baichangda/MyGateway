package com.bcd.base.support_parser.util;

import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.immotors.ep33.data.Evt_0006;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LogUtil {
    public final static ConcurrentHashMap<Class<?>, HashMap<String, Integer>> class_fieldName_lineNo = new ConcurrentHashMap<>();

    public static String getDeclaredFieldStackTrace(Class<?> fieldDeclaringClass, String fieldName) {
        final HashMap<String, Integer> fieldName_lineNo = class_fieldName_lineNo.computeIfAbsent(fieldDeclaringClass, k1 -> {
            final HashMap<String, Integer> resMap = new HashMap<>();
            final String javaFieldPath = "src/main/java/" + k1.getName().replaceAll("\\.", "/") + ".java";
            final Path path = Paths.get(javaFieldPath);
            if (Files.exists(path)) {
                final Field[] declaredFields = k1.getDeclaredFields();
                final Set<String> endFieldStrSet = Arrays.stream(declaredFields).map(e -> " " + e.getName() + ";").collect(Collectors.toSet());
                try (final BufferedReader br = Files.newBufferedReader(path)) {
                    int no = 1;
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        line = line.trim();
                        for (String endFieldStr : endFieldStrSet) {
                            if (line.endsWith(endFieldStr)) {
                                resMap.put(endFieldStr.substring(1, endFieldStr.length() - 1), no);
                            }
                        }
                        no++;
                    }
                } catch (IOException ex) {
                    throw BaseRuntimeException.getException(ex);
                }
            }
            return resMap;
        });

        final Integer lineNo = fieldName_lineNo.get(fieldName);

        if (lineNo == null) {
            return "";
        } else {
            return "(" + fieldDeclaringClass.getSimpleName() + ".java:" + lineNo + ")";
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println("."+getDeclaredFieldStackTrace(Evt_0006.class,"HDop"));
        System.out.println("."+getDeclaredFieldStackTrace(Evt_0006.class,"VDop"));
    }
}
