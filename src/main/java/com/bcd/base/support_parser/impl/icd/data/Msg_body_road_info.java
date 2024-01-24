package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class Msg_body_road_info implements Msg_body {
    @F_num(type = NumType.uint16, var = 'a')
    public int road_count;
    @F_bean_list(listLenExpr = "a")
    public List<Road2_info> road_info_array;


    public static void main(String[] args) throws NoSuchFieldException {
        final Field roadInfoArray = Msg_body_road_info.class.getField("road_info_array");
        final Type genericSuperclass = roadInfoArray.getGenericType();
        final Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        System.out.println(actualTypeArguments);
    }
}
