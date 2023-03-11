package com.zouzhao.opt.file.core.controller;

import com.zouzhao.common.core.exception.MyException;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.opt.file.api.IOptFileApi;
import com.zouzhao.opt.file.core.service.OssService;
import com.zouzhao.opt.file.dto.OptFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 姚超
 * @DATE: 2023-3-7
 */
@RestController
@RequestMapping("/data/opt-file/optFile")
@Api(
        tags = "附件接口"
)
public class OptFileController {

    private static final Logger log = LoggerFactory.getLogger(OptFileController.class);

    @Autowired
    private IOptFileApi optFileApi;
    @Autowired
    private OssService ossService;

    @ApiOperation("上传")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_FILE_UPLOAD')")
    public IdDTO uploadMoviePic(@RequestParam("file") MultipartFile file) {
        log.debug("新增的文件:{}", file);
        String path = ossService.upload(file);
        if(path ==null)throw new MyException("文件上传失败");
        //增加附件表记录
        OptFileVO vo = new OptFileVO();
        vo.setFilePath(path);
        //放回附件id给前端
        return optFileApi.add(vo);
    }

    @ApiOperation("下载")
    @PostMapping("/download")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_FILE_DOWNLOAD')")
    public void uploadMoviePic(@RequestBody IdDTO idDTO, HttpServletResponse response) {
        //拿到路径
        OptFileVO fileVO = optFileApi.findVOById(idDTO);
        String path = fileVO.getFilePath();
        //传文件
        ossService.download(path,response);
    }


}
