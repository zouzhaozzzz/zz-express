package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface IController <A extends IApi<V>, V>{
    A getApi();
}
