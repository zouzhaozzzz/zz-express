package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;
import com.zouzhao.common.dto.BaseVO;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface IController <A extends IApi<V>, V extends BaseVO>{
    A getApi();
}
