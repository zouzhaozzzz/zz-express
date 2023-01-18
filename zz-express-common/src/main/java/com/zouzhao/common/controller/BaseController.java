package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.IViewObject;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 姚超
 * @DATE: 2023-1-7
 */
@NoArgsConstructor
public class BaseController<A extends IApi<V>, V extends IViewObject> {
    @Autowired
    A api;

    public A getApi() {
        return this.api;
    }
}
