package com.spring.config.shiro;
import com.spring.shiroRealm.realm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.*;

@Configuration
public class shiroConfig {

    @Bean(name = "credentialsMatcherMy")
    public HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setAuthenticator(realmAuthenticator());
        securityManager.setAuthorizer(modularRealmAuthorizer());
        return securityManager;
    }
    @Bean(name = "sessionManager")
    public SessionManager sessionManager(){
        return new DefaultWebSessionManager();

    }
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "cacheManagerLocalShiro")
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:configFile/ehcache-spring.xml");
        return ehCacheManager;
    }

    @Bean(name = "ShiroRealm")
    public realm shiroRealm(){
       realm Rr = new realm();
       Rr.setCredentialsMatcher(credentialsMatcher());
       return Rr;
    }
    @Bean(name = "shiroFile")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/root");
        shiroFilterFactoryBean.setSuccessUrl("/success.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error.html");
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
//        linkedHashMap.put("/Login","anon");
        linkedHashMap.put("/logout","logout");
//        linkedHashMap.put("/registerPage","anon");
//        linkedHashMap.put("/registerUser","anon");
//        linkedHashMap.put("/static/**","anon");
        linkedHashMap.put("/admin","roles[admin]");
        linkedHashMap.put("/student","roles[student]");
        linkedHashMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    @Bean(name = "realmAuthenticatorLocal")
    public ModularRealmAuthenticator realmAuthenticator(){
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        Set<Realm> collection = new HashSet<>();
        collection.add(shiroRealm());
        authenticator.setRealms(collection);
        return authenticator;
    }
    @Bean(name = "modularRealmAuthorize")
    public ModularRealmAuthorizer modularRealmAuthorizer(){
        ModularRealmAuthorizer realmAuthorize = new ModularRealmAuthorizer();
        Set<Realm> collection = new HashSet<>();
        collection.add(shiroRealm());
        realmAuthorize.setRealms(collection);
        return realmAuthorize;
    }
}
