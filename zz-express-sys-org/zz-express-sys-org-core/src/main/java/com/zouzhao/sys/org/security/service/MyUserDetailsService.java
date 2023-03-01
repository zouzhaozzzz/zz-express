package com.zouzhao.sys.org.security.service;

import com.zouzhao.common.dto.IdDTO;
import com.zouzhao.sys.org.api.ISysOrgElementApi;
import com.zouzhao.sys.org.dto.SysOrgElementVO;
import com.zouzhao.sys.org.entity.SysOrgAccount;
import com.zouzhao.sys.org.service.SysOrgAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Log4j2
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SysOrgAccountService sysOrgAccountService;
    @Autowired
    private ISysOrgElementApi sysOrgElementApi;


    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        log.debug("{} -> {}", "UserDetailsService", "loadUserByUsername");
        SysOrgAccount user = null;

        try {
            SysOrgAccount query = new SysOrgAccount();
            query.setOrgAccountLoginName(loginName);
            List<SysOrgAccount> list = sysOrgAccountService.findList(query);
            if(!ObjectUtils.isEmpty(list)) {
                SysOrgAccount orgAccount = list.get(0);
                SysOrgElementVO orgElementVO = sysOrgElementApi.findVOById(IdDTO.of(orgAccount.getOrgAccountDefPersonId()));
                if(orgElementVO.getOrgElementStatus())user=orgAccount;
                else throw new RuntimeException("用户未启用");
            }

            // user.setMenus(Arrays.asList("类别信息管理", "电影基本信息管理"));
            // 从db中查询出用户信息，然后在业务逻辑中进行判断是否合法
            // select * from user_info where username=? and password=?
            // 在业务逻辑中 SpringSecurity 为了保护密码，做了很多类似于加密工作，为的是不让密码以“明文”方式呈现在代码逻辑中（日志中）

            // 先创建一个基于ROLE_USER（“角色名称”）的一套权限
            // List<GrantedAuthority> authorities =
            //         // AuthorityUtils.commaSeparatedStringToAuthorityList("create,search");
            //         // AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin,ROLE_user,ROLE_guest");
            //         AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DEFAULT");
            // user.setAuthorities(null);
        } catch (Exception e) {
            log.error("认证出现异常:{}", e.getMessage());
            // if (user == null) throw new UsernameNotFoundException("用户未找到");
            // e是造成当前UsernameNotFoundException出现的原因
            throw new UsernameNotFoundException("用户未找到", e);// 一定要把原始的异常作为cause(底层原因)参数提交
        }
        return user;
    }
}
