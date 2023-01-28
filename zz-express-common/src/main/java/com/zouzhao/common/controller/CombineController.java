package com.zouzhao.common.controller;

import com.zouzhao.common.api.IApi;

/**
 * @author 姚超
 * @DATE: 2023-1-20
 */
public interface CombineController <A extends IApi<V>, V> extends CrudController<A, V>, DeleteAllController<A, V>, ListController<A, V> {
}
