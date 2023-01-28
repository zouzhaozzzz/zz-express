package com.zouzhao.sys.org.service.init;

import com.zouzhao.common.dto.IdNameDTO;
import com.zouzhao.sys.org.api.init.ISysInitApi;
import com.zouzhao.sys.org.config.RoleAnnotationValidRegistrar;
import com.zouzhao.sys.org.entity.SysRightRole;
import com.zouzhao.sys.org.mapper.SysRightRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 姚超
 * @DATE: 2023-1-26
 */
@Service
public class SysInitService implements ISysInitApi {
    @Autowired
    private SysRightRoleMapper sysRightRoleMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private static final String LockKey="initLock";

    @Override
    public List<IdNameDTO> init() {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(LockKey, uuid, 5, TimeUnit.MINUTES);
        List<IdNameDTO> result = new ArrayList<>();
        if (lock){
            List<SysRightRole> allRoleList=new ArrayList<>();
            RoleAnnotationValidRegistrar.roleList.forEach(list->{
                if(list != null && list.size()>0) allRoleList.addAll(list);
            });
            try {
                if (allRoleList.size() > 0) {
                    //更新或增加role
                    sysRightRoleMapper.saveOrUpdateBatchByName(allRoleList);
                    //删除多余权限及权限角色关系
                    List<String> rightRoleCodeList = allRoleList.stream().map(SysRightRole::getRightRoleCode).collect(Collectors.toList());
                    sysRightRoleMapper.deleteWithRelation(rightRoleCodeList);
                }
                result.add(IdNameDTO.of("权限机制初始化器", "true"));
            } catch (Exception e) {
                result.add(IdNameDTO.of("权限机制初始化器", "false"));
                e.printStackTrace();
            } finally {
                result.add(IdNameDTO.of("组织架构_组织分类初始化", "true"));
                //释放lock
                String lockUuid = (String) redisTemplate.opsForValue().get(LockKey);
                if (uuid.equals(lockUuid)) {
                    redisTemplate.delete(LockKey);
                }
                return result;
            }
        }else{
            result.add(IdNameDTO.of("正在初始化"));
          return result;
        }

    }
}
