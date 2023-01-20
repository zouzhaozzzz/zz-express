package com.zouzhao.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 姚超
 * @DATE: 2023-1-18
 */
@Component
public class ModelUtil<Source, Target> {
    private static final Logger log = LoggerFactory.getLogger(ModelUtil.class);

    /**
     * 通过静态方法调用(单例模式)
     */
    private static ModelUtil modelUtil = new ModelUtil();

    public ModelUtil(){

    }
    /** -----------------------静态方法方式调用-------------------------------------- */
    /**
     * 转换为目标对象
     * @param source 源对象
     * @param type 目标对象类
     * @return 目标对象
     */
    public static Object toEntityStatic(Object source, Class<?> type){
        return modelUtil.toEntity(source, type);
    }

    /**
     * 转换为目标Page对象
     * @param source 源对象
     * @param type 目标类型
     * @return 目标Page对象
     */
    public static Page<?> toPageEntityStatic(Page<?> source, Class<?> type){
        return modelUtil.toPageEntity(source, type);
    }

    /**
     * 转换为目标List对象
     * @param source 源对象
     * @param type 目标类型
     * @return 目标List对象
     */
    public static List<?> toArrayListEntityStatic(List<?> source, Class<?> type){
        return modelUtil.toArrayListEntity(source, type);
    }
    /** -------------------------------------------------------------------------- */

    /**
     * 转换为实体对象
     *
     * @return Target
     */
    public Target toEntity(Source source, Class<Target> type) {
        Target target = createInstance(type);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 实例化对象
     *
     * @param clazz
     * @return
     */
    private Target createInstance(Class<Target> clazz) {
        Target target = null;
        try {
            target = clazz.newInstance();
        } catch (InstantiationException e) {
            log.info("转换对象错误: 实例化{}失败", clazz);
            e.getMessage();
        } catch (IllegalAccessException e) {
            log.info("转换对象错误: 无权访问指定类{}", clazz);
            e.getMessage();
        }
        return target;
    }

    /**
     * Page类型entity转换
     * @param source
     * @param type
     * @return
     */
    public Page<Target> toPageEntity(Page<Source> source, Class<Target> type) {
        Page<Target> target = new Page<>();
        BeanUtils.copyProperties(source, target);
        target.setRecords(toArrayListEntity(source.getRecords(), type));

        return target;
    }

    /**
     * List类型的数据entity转换
     * @param source
     * @param type
     * @return
     */
    private List<Target> toArrayListEntity(List<Source> source, Class<Target> type) {
        List<Target> target = new ArrayList<>();
        source.forEach(e -> {
            Target instance = createInstance(type);
            BeanUtils.copyProperties(e, instance);
            target.add(instance);
        });

        return target;
    }


}
