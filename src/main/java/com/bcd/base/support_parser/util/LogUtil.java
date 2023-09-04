package com.bcd.base.support_parser.util;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.immotors.ep33.data.Evt_0006;
import com.bcd.base.support_parser.impl.immotors.ep33.data.Evt_D00B;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LogUtil {
    public final static ConcurrentHashMap<Class<?>, HashMap<String, Integer>> class_fieldName_lineNo = new ConcurrentHashMap<>();

    public static String getFieldStackTrace(Class<?> clazz, String fieldName) {
        final Class<?>[] nestMembers = clazz.getNestMembers();
        final Class<?> topClass = nestMembers[0];
        HashMap<String, Integer> fieldName_lineNo = null;
        for (Class<?> nestMember : nestMembers) {
            final HashMap<String, Integer> temp = class_fieldName_lineNo.computeIfAbsent(nestMember, k -> {
                final HashMap<String, Integer> resMap = new HashMap<>();
                final String javaFieldPath = "src/main/java/" + topClass.getName().replaceAll("\\.", "/") + ".java";
                final Path path = Paths.get(javaFieldPath);
                if (Files.exists(path)) {
                    final Field[] declaredFields = nestMember.getDeclaredFields();
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
            if (clazz == nestMember) {
                fieldName_lineNo = temp;
            }
        }

        final Integer lineNo = fieldName_lineNo.get(fieldName);
        if (lineNo == null) {
            return "";
        } else {
            return "(" + topClass.getSimpleName() + ".java:" + lineNo + ")";
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println("." + getFieldStackTrace(Evt_0006.class, "HDop"));
        System.out.println("." + getFieldStackTrace(Evt_0006.class, "VDop"));
        System.out.println("." + getFieldStackTrace(Evt_D00B.class, "BMSCellVols"));
        System.out.println("." + getFieldStackTrace(Evt_D00B.Evt_D00B_BMSCellVol.class, "BMSCellVolV"));
    }
}
