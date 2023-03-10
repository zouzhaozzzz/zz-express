package com.zouzhao.opt.file.api.init;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @author 姚超
 * @DATE: 2023-3-6
 */
public interface IOptFileInit {

    @PostMapping("/roleConfig")
    public List<Map<String,String>> roleConfig();
}
