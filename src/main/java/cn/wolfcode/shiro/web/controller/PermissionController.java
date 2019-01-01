package cn.wolfcode.shiro.web.controller;


import cn.wolfcode.shiro.dao.IPermissionDAO;
import cn.wolfcode.shiro.domain.Permission;
import cn.wolfcode.shiro.realm.PermissionName;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class PermissionController {

    @Autowired
    private RequestMappingHandlerMapping rmhm;
    @Autowired
    private IPermissionDAO iPermissionDAO;

    @RequestMapping("/reload")
    public String reload() throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = rmhm.getHandlerMethods();
        List<String> resources = iPermissionDAO.getAllResource();
        Collection<HandlerMethod> values = handlerMethods.values();
        for (HandlerMethod value : values) {
            RequiresPermissions methodAnnotation = value.getMethodAnnotation(RequiresPermissions.class);
            if (methodAnnotation != null) {
                String resource = methodAnnotation.value()[0];
                if (resources.contains(resource)) {
                    continue;
                }
                Permission permission = new Permission();
                permission.setResource(resource);
                permission.setName(value.getMethodAnnotation(PermissionName.class).value());
                iPermissionDAO.save(permission);
            }
            System.out.println(values);
        }

        return "main";
    }

}
