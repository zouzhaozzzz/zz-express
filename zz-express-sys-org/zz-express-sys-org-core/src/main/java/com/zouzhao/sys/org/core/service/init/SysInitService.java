package com.zouzhao.sys.org.core.service.init;

import com.zouzhao.common.dto.IdNameDTO;
import com.zouzhao.opt.file.client.init.OptFileInitClient;
import com.zouzhao.opt.manage.client.init.OptManageInitClient;
import com.zouzhao.sys.org.api.init.ISysInitApi;
import com.zouzhao.sys.org.core.entity.SysRightRole;
import com.zouzhao.sys.org.core.mapper.SysRightRoleMapper;
import com.zouzhao.sys.org.core.security.config.RoleAnnotationValidRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@Service
public class SysInitService implements ISysInitApi {

    private static final String LockKey = "initLock";
    @Autowired
    private SysRightRoleMapper sysRightRoleMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OptManageInitClient optManageInitClient;
    @Autowired
    private OptFileInitClient optFileInitClient;

    @Transactional
    public List<IdNameDTO> init() {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(LockKey, uuid, 5, TimeUnit.MINUTES);
        List<IdNameDTO> result = new ArrayList<>();
        if (lock) {
            List<SysRightRole> allRoleList = new ArrayList<>();
            RoleAnnotationValidRegistrar.roleList.forEach(list -> {
                if (list != null && list.size() > 0) allRoleList.addAll(list);
            });
            //权限机制-运单管理模块初始化
            try {
                getRoles(allRoleList, optManageInitClient.roleConfig());
                result.add(IdNameDTO.of("权限机制-运单管理模块初始化", "true"));
            } catch (Exception e) {
                e.printStackTrace();
                result.add(IdNameDTO.of("权限机制-运单管理模块初始化", "false"));
            } finally {
                //权限机制-中间件模块初始化
                try {
                    getRoles(allRoleList, optFileInitClient.roleConfig());
                    result.add(IdNameDTO.of("权限机制-附件模块初始化", "true"));
                } catch (Exception e) {
                    e.printStackTrace();
                    result.add(IdNameDTO.of("权限机制-附件模块初始化", "false"));
                }finally {
                    try {
                        if (allRoleList.size() > 0) {
                            //更新或增加role
                            sysRightRoleMapper.saveOrUpdateBatchByName(allRoleList);
                            //删除多余权限及权限角色关系
                            List<String> rightRoleCodeList = allRoleList.stream().map(SysRightRole::getRightRoleCode).collect(Collectors.toList());
                            sysRightRoleMapper.deleteWithRelation(rightRoleCodeList);
                        }
                        result.add(IdNameDTO.of("权限机制-系统管理模块初始化", "true"));
                        result.add(IdNameDTO.of("权限机制-组织架构模块初始化", "true"));
                    } catch (Exception e) {
                        result.add(IdNameDTO.of("权限机制-系统管理模块初始化", "false"));
                        result.add(IdNameDTO.of("权限机制-组织架构模块初始化", "false"));
                        e.printStackTrace();
                    } finally {
                        result.add(IdNameDTO.of("菜单管理_初始化", "true"));
                        result.add(IdNameDTO.of("组织架构_组织分类初始化", "true"));
                        result.add(IdNameDTO.of("组织架构_组织初始化", "true"));
                        result.add(IdNameDTO.of("运单管理_寄派件初始化", "true"));
                        result.add(IdNameDTO.of("运单管理_费用管理初始化", "true"));
                        //释放lock
                        String lockUuid = (String) redisTemplate.opsForValue().get(LockKey);
                        if (uuid.equals(lockUuid)) {
                            redisTemplate.delete(LockKey);
                        }
                        return result;
                    }
                }

            }
        } else {
            result.add(IdNameDTO.of("正在初始化"));
            return result;
        }

    }

    private void getRoles(List<SysRightRole> allRoleList, List<Map<String, String>> maps2) {
        List<Map<String, String>> maps = maps2;
        maps.forEach(map->{
            SysRightRole vo = new SysRightRole();
            vo.setRightRoleName(map.get("name"));
            vo.setRightRoleModule(map.get("module"));
            vo.setRightRoleCode(map.get("code"));
            vo.setRightRoleDesc(map.get("desc"));
            allRoleList.add(vo);
        });
    }
}
