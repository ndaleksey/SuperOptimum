package com.denlex.superoptimum.config;

import com.denlex.superoptimum.domain.user.RoleKind;
import com.denlex.superoptimum.service.user.impl.CustomerCredentialsService;
import com.denlex.superoptimum.service.user.impl.DistributorCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created by Shishkov A.V. on 10.08.18.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;


	@Configuration
	@Order(1)
	public static class CustomerSecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		@Qualifier("customerCredentialsService")
		private CustomerCredentialsService customerCredentialsService;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.httpBasic().and()
					.antMatcher("/customer/**")
					.authorizeRequests()
					.antMatchers("/customer/loading").permitAll()
					.antMatchers("/customer/**").hasRole(RoleKind.CUSTOMER.getValue())
//					.anyRequest().hasRole(RoleKind.CUSTOMER.getValue())

					.and()

					.formLogin()
					.loginPage("/customer/login")
					.defaultSuccessUrl("/customer/main")
					.permitAll()

					.and()

					.csrf().disable()

					.logout()
					.logoutUrl("/customer/logout")
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID")
					.permitAll();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(customerCredentialsService).passwordEncoder(bCryptPasswordEncoder());
		}
	}

	@Configuration
	@Order(2)
	public static class DistributorSecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		@Qualifier("distributorCredentialsService")
		private DistributorCredentialsService distributorCredentialsService;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.httpBasic().and()
					.antMatcher("/distributor/**")
					.authorizeRequests()
					.antMatchers("/distributor/loading").permitAll()
					.anyRequest().hasRole(RoleKind.DISTRIBUTOR.getValue())

					.and()

					.formLogin()
					.loginPage("/distributor/login")
					.defaultSuccessUrl("/distributor/main")
					.permitAll()

					.and()

					.csrf().disable()

					.logout()
					.logoutUrl("/distributor/logout")
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID")
					.permitAll();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(distributorCredentialsService).passwordEncoder(bCryptPasswordEncoder());
		}
	}

	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
					.antMatchers("/", "/index", "/loading_c", "/loading_d").permitAll()
					.antMatchers("/css/**", "/js/**", "/font/**", "/img/**").permitAll()
					.anyRequest()
					.authenticated()
				.and()
					.formLogin().successHandler(authenticationSuccessHandler)
					.loginPage("/login")
					.failureUrl("/login?error=true")
				.permitAll()
				.and()
					.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.invalidateHttpSession(true)
					.logoutSuccessUrl("/index")
				.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
					*//*.logoutSuccessHandler((request, response, authentication) -> {
						response.sendRedirect("/index");
					})*//*;
	}*/
}
