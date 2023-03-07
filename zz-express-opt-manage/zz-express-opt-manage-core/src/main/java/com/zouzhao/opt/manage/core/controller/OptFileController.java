package com.zouzhao.opt.manage.core.controller;

import com.zouzhao.common.core.controller.BaseController;
import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.opt.manage.api.IOptFileApi;
import com.zouzhao.opt.manage.core.service.OssService;
import com.zouzhao.opt.manage.dto.OptFileVO;
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
@RequestMapping("/data/opt-manage/optFile")
@Api(
        tags = "附件接口"
)
public class OptFileController extends BaseController<IOptFileApi, OptFileVO> {

    private static final Logger log = LoggerFactory.getLogger(OptFileController.class);

    @Autowired
    private OssService ossService;

    @ApiOperation("上传")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_FILE_UPLOAD')")
    public IdDTO uploadMoviePic(@RequestParam("file") MultipartFile file) {
        log.debug("新增的文件:{}", file);
        String path = ossService.upload(file);
        //增加附件表记录
        OptFileVO vo = new OptFileVO();
        vo.setFilePath(path);
        //放回附件id给前端
        return getApi().add(vo);
    }

    @ApiOperation("下载")
    @PostMapping("/download")
    @PreAuthorize("hasAnyRole('OPT_MANAGE_FILE_DOWNLOAD')")
    public void uploadMoviePic(@RequestBody IdDTO idDTO, HttpServletResponse response) {
        //拿到路径
        OptFileVO fileVO = getApi().findVOById(idDTO);
        String path = fileVO.getFilePath();
        //传文件
        ossService.download(path,response);
    }
}
