package com.spring.shiroRealm;

import com.spring.EntiryPage.mysqlEntiry.UserEntiry;
import com.spring.Mappers.UserInterface;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class realm extends AuthorizingRealm {

    @Autowired
    private UserInterface userInterface;
    
    @Override
    public String getName() {
        return "realm";
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String name = principalCollection.toString();
        UserEntiry byUser = userInterface.getUserByUser(name);
        Set<String> set = new HashSet<>();
        set.add(byUser.getRoles());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(set);
        return info;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken Token) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) Token;
        String name =(String) token.getPrincipal();
        UserEntiry e = userInterface.getUserByUser(name);
        try {
            if (e.getName()!=null&&e.getPassword()!=null){
                String ePassword = e.getPassword();
                System.out.println("ePassword = " + ePassword);
                SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(e.getName(),e.getPassword(),getName());
                System.out.println("info = " + info.getCredentials());
                return info;
            }
        }catch (Exception e1){
            e1.printStackTrace();
            throw new AuthenticationException();
        }
        return null;
    }
}
