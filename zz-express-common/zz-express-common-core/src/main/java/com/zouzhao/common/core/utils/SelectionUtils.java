package com.zouzhao.common.core.utils;

import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-2-28
 */
public class SelectionUtils {

    /**
     * 处理前端Select选择框的属性值
     * @param oldList 原来的属性list
     * @param newList 更新的属性list
     * @param id      更新的属性对应的主对象id
     * @param insert  新增主对象id的属性list方法
     * @param delete  删除主对象id的属性list方法
     */
    public static void handleSelect(List<String> oldList, List<String> newList, String id, BiConsumer<String,List<String>> insert, BiConsumer<String,List<String>> delete){
        //既没有更新又没有删除
        if (ObjectUtil.isEmpty(oldList) && ObjectUtil.isEmpty(newList)) return;
        //只有新增
        if (ObjectUtil.isEmpty(oldList)) {
            insert.accept(id,newList);
            return;
        }
        //只有删除
        if (ObjectUtil.isEmpty(newList)) {
            delete.accept(id,oldList);
            return;
        }
        //既有更新又有删除
        //转换map
        Map<String, String> oldMap = oldList.stream().collect(Collectors.toMap(item -> item, item -> item));
        for (int i = 0; i < newList.size(); i++) {
            String newId = newList.get(i);
            if (oldMap.get(newId) != null) {
                oldMap.remove(newId);
                newList.remove(i);
                i--;
            }
        }
        //添加需要新增的权限
        if (newList.size() > 0)
            insert.accept(id,newList);
        //删除多余的权限
        if (oldMap.keySet().size() > 0)
            delete.accept(id,new ArrayList<>(oldMap.keySet()));;
    }
}
