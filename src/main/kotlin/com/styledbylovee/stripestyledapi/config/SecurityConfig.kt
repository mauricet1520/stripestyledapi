package com.styledbylovee.stripestyledapi.config

import org.springframework.context.annotation.Bean
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
//import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails

//@EnableWebFluxSecurity
class SecurityConfig {

//    @Bean
//    fun userDetailService() : MapReactiveUserDetailsService {
//        val userDetails = ArrayList<UserDetails>()
//        userDetails.add(User.withDefaultPasswordEncoder()
//                .username("reece").password("password").build())
//        return MapReactiveUserDetailsService(userDetails)
//    }
//
//	@Bean
//	fun springSecurityFilterChain(serverHttpSecurity: ServerHttpSecurity) : SecurityWebFilterChain{
//
//		serverHttpSecurity.authorizeExchange().pathMatchers("/createSubscription").permitAll().and().httpBasic()
//
//	return	serverHttpSecurity.build()
//	}
}