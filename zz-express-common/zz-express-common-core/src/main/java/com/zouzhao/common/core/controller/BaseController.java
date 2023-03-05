package com.zouzhao.common.core.controller;

import com.zouzhao.common.api.IApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 姚超
 * @DATE: 2023-1-7
 */
@NoArgsConstructor
public class BaseController<A extends IApi<V>, V > implements IController<A,V>{
    @Autowired
    A api;

    public A getApi() {
        return this.api;
    }


}
