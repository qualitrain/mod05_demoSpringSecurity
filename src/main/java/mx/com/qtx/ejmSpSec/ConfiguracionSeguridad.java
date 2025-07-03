package mx.com.qtx.ejmSpSec;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.zaxxer.hikari.HikariDataSource;

import mx.com.qtx.ejmSpSec.seguridad.web.FiltroTokensJwt_SS;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
	private static Logger bitacora = LoggerFactory.getLogger(ConfiguracionSeguridad.class);

	@Bean
	@Order(1)	
	SecurityFilterChain getSecurityFilterChainApiWeb(HttpSecurity http, FiltroTokensJwt_SS filtroJWT
			            ) throws Exception {
		http.securityMatchers(config -> config.requestMatchers("/api/**","/api/autenticacion"))
			.authorizeHttpRequests((authorize) ->  authorize
			     .requestMatchers("/api/autenticacion").permitAll()
			     .requestMatchers("/api/**").hasRole("AGENTE")
			)
			.csrf(config -> config.disable())
			.formLogin(config -> config.disable())
			.logout(config -> config.disable())
		  	.addFilterBefore(filtroJWT, UsernamePasswordAuthenticationFilter.class)
		  	.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
	
	@Bean
	@Order(2)	
	SecurityFilterChain getSecurityFilterChainMvc(HttpSecurity http) 
		    throws Exception {
		
		RequestMatcher urisXatender = this.buildRequestMatcherTodosExcepto("/api/**", "/api/autenticacion");
		
		http.securityMatcher(urisXatender)
			.authorizeHttpRequests((authorize) ->  authorize
			     .requestMatchers("/css/*","/login").permitAll()
				 .requestMatchers("/info","/vistaInfo.html").permitAll()
			     .requestMatchers("/admin/**").hasRole("ADMIN")
			     .requestMatchers("/logistica/**").hasAnyRole("LOGISTICA","ADMIN")
			     .requestMatchers("/**").authenticated()
			)
			.csrf(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults())
			.formLogin(Customizer.withDefaults())
			.logout(config -> config.invalidateHttpSession(true))
			.sessionManagement(config -> config.maximumSessions(1));

		return http.build();
	}
	
	private RequestMatcher buildRequestMatcherTodosExcepto(String urlExcluida1, String urlExcluida2) {
		OrRequestMatcher orRequestMatcher = new OrRequestMatcher(new AntPathRequestMatcher(urlExcluida1), 
				                                                 new AntPathRequestMatcher(urlExcluida2)); 
		RequestMatcher patronUrlsXatender = new NegatedRequestMatcher(orRequestMatcher);
		return patronUrlsXatender;
	}
	
	//@Bean
	SecurityFilterChain crearSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(autorizador->autorizador
				           .requestMatchers("/api/autenticacion").permitAll()
				           .requestMatchers("/css/**").permitAll()
				           .requestMatchers("/info","/vistaInfo.html").permitAll()
				           .requestMatchers("/api/**").hasRole("VTAS")
				           .requestMatchers("/admin/**").hasRole("ADMIN")
				           .requestMatchers("/logistica/**").hasAnyRole("DESARROLLO","COMPRAS")
				           .requestMatchers("/**").authenticated()
				)
				.csrf(config -> config.ignoringRequestMatchers("/api/**"))
		     .httpBasic(Customizer.withDefaults())
		     .formLogin(Customizer.withDefaults())
		     .build();
	}
	
	//@Bean
	UserDetailsService crearGestorDeUsuarios() {
		 UserDetails usuarioAlex = User.withDefaultPasswordEncoder()
		     .username("Alex")
		     .password("estrellamarinera")
		     .roles("ADMIN","VTAS")
		     .build();
		 
		 UserDetails usuarioEdgar = User.withDefaultPasswordEncoder()
			     .username("Edgar")
			     .password("perroamarillo")
			     .roles("COMPRAS")
			     .build();
		 
		 UserDetails usuarioRosalinda = User.withDefaultPasswordEncoder()
			     .username("Rosalinda")
			     .password("tortugaazul")
			     .roles("DESARROLLO")
			     .build();
		 
		InMemoryUserDetailsManager gestorUsuarios = new InMemoryUserDetailsManager(usuarioRosalinda, 
				                                                                   usuarioEdgar, 
				                                                                   usuarioAlex);
		return gestorUsuarios;
	}
	
    //@Bean
    UserDetailsManager getGestorBdUsuarios(DataSource dataSource) {
        UserDetails usuarioAlex = User.withUsername("betote")
                 .password("{bcrypt}$2a$10$rz1ehHUZCmkf2zdQ0cZQsefzyOJmHb2NeD2gPmeQjMZPqXcjEYLEe")
                .roles("USER","AGENTE","ADMIN")
                .build();
        
        UserDetails usuarioDavid = User.withUsername("david")
                        .password("{bcrypt}$2a$10$MT.E/Kh/p.lRm6PjCD0FceHGSL0SAx.F.9uvr66bzErLdl6uXct22")
                        .roles("AGENTE")
                        .build();
        
        UserDetails usuarioTavo = User.withUsername("tavo")
                        .password("{bcrypt}$2a$10$0s8uv8VHRc0L5qR5EwNpj.OipjeE4bYsSD3Mvrzh3sG2nMCgoK7re")
                        .roles("LOGISTICA")
                        .build();
        
        JdbcUserDetailsManager gestorUsuariosBD = new JdbcUserDetailsManager(dataSource);
               
        if(gestorUsuariosBD.userExists(usuarioAlex.getUsername()) == false)
            gestorUsuariosBD.createUser(usuarioAlex);
        if(gestorUsuariosBD.userExists(usuarioDavid.getUsername()) == false)
           gestorUsuariosBD.createUser(usuarioDavid);
        if(gestorUsuariosBD.userExists(usuarioTavo.getUsername()) == false)
           gestorUsuariosBD.createUser(usuarioTavo);
        
        return gestorUsuariosBD;
   }
    @Bean
   UserDetailsService getGestorBdUsuariosPersonalizada(DataSource dataSource) {
               bitacora.trace("getGestorBdUsuariosPersonalizada()");
               HikariDataSource hds = (HikariDataSource) dataSource;
               bitacora.debug("Se ha instanciado data source mysql que apunta a BD:" + hds.getJdbcUrl());
               
           //Se usa una BD Personalizada. Ya debe contener los datos de usuarios y roles
               final String QUERY_DATOS_USUARIO_X_NOMBRE = "SELECT usr_nombre, usr_paswd, usr_habilitado "
                               + "FROM usuario WHERE usr_nombre = ?";

               //Los roles deben estar escritos en los registros de la base de datos con el prefijo "ROLE_"
               //Por ejemplo, ROLE_AGENTE o ROLE_LOGISTICA
               final String QUERY_ROLES_X_USUARIO ="SELECT usr_nombre, aut_nombre "
                               + "FROM usuario, autoridad "
                               + "WHERE usr_nombre = ? "
                               + "AND usr_nombre = aut_nombre_usr";
               
               JdbcDaoImpl gestorBdUsuariosPersonalizada = new JdbcDaoImpl();
               gestorBdUsuariosPersonalizada.setDataSource(dataSource);
               gestorBdUsuariosPersonalizada.setUsersByUsernameQuery(QUERY_DATOS_USUARIO_X_NOMBRE);
               gestorBdUsuariosPersonalizada.setAuthoritiesByUsernameQuery(QUERY_ROLES_X_USUARIO);
               
               return gestorBdUsuariosPersonalizada;
   }	
    @Bean
    AuthenticationManager publicarAuthenticationManagerDesdeConfiguracion(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	bitacora.trace("publicarAuthenticationManagerDesdeConfiguracion()");
    	AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
    	bitacora.debug("authenticationManager instanciado:" + authenticationManager.getClass().getName());
        return authenticationManager;
    }   
    
}
