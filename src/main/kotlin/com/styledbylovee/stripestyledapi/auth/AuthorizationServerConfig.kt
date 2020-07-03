//package com.styledbylovee.stripestyledapi.auth
//
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.crypto.password.NoOpPasswordEncoder
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
//import org.springframework.security.oauth2.provider.token.TokenStore
//import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore
//
//
//@Configuration
//@EnableAuthorizationServer
//class AuthorizationServerConfig: AuthorizationServerConfigurerAdapter() {
//    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
//        security!!.passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .checkTokenAccess("permitAll()")
//                .tokenKeyAccess("permitAll()")
//    }
//
//    override fun configure(clients: ClientDetailsServiceConfigurer?) {
//
//        clients!!.inMemory()
//                .withClient("guest")
//                .secret("secret")
//                .autoApprove(true)
//                .authorizedGrantTypes("client_credentials")
//
//    }
//
//    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
//        endpoints!!.tokenStore(InMemoryTokenStore())
//    }
//}