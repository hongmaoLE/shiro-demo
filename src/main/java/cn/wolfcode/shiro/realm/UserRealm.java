package cn.wolfcode.shiro.realm;

import cn.wolfcode.shiro.dao.IPermissionDAO;
import cn.wolfcode.shiro.dao.IRoleDAO;
import cn.wolfcode.shiro.dao.IUserDAO;
import cn.wolfcode.shiro.domain.User;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Date 2018/12/30 20:56
 * @Created by simple
 */
public class UserRealm extends AuthorizingRealm {


    @Setter
    private IUserDAO userDAO;
    @Setter
    private IRoleDAO RoleDAO;
    @Setter
    private IPermissionDAO PermissionDAO;

    @Override
    public String getName() {
        return "UserRealm";
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        System.out.println("sssss");
        List<String> permissions = new ArrayList<String>();
        List<String> roles = new ArrayList<>();
        if ("admin".equals(user.getUsername())) {
            permissions.add("*:*");
            roles = RoleDAO.getAllRoleSn();
        } else {
            //根据用户ID查询出用户的所有角色
            roles = RoleDAO.getRoleSnByUserId(user.getId());
            permissions = PermissionDAO.getPermissionResourceByUserId(user.getId());
        }


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //info.addStringPermissions(permissions);
        info.addStringPermissions(permissions);
        info.addRoles(roles);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        User user = userDAO.getUserByUsername(principal);
        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(),
                ByteSource.Util.bytes("love"), getName());
        return info;
    }

    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public IRoleDAO getRoleDAO() {
        return RoleDAO;
    }

    public void setRoleDAO(IRoleDAO roleDAO) {
        RoleDAO = roleDAO;
    }

    public IPermissionDAO getPermissionDAO() {
        return PermissionDAO;
    }

    public void setPermissionDAO(IPermissionDAO permissionDAO) {
        PermissionDAO = permissionDAO;
    }
}
