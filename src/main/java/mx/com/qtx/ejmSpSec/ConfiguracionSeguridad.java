package mx.com.qtx.ejmSpSec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
	@Bean
	SecurityFilterChain crearSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(autorizador->autorizador
				           .requestMatchers("/css/**").permitAll()
				           .requestMatchers("/info","/vistaInfo.html").permitAll()
				           .requestMatchers("/api/**").hasRole("VTAS")
				           .requestMatchers("/admin/**").hasRole("ADMIN")
				           .requestMatchers("logistica/**").hasAnyRole("DESARROLLO","COMPRAS")
				           .requestMatchers("/**").authenticated()
				)
		     .httpBasic(Customizer.withDefaults())
		     .formLogin(Customizer.withDefaults())
		     .build();
	}
	
	@Bean
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
	
}
