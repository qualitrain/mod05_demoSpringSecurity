package mx.com.qtx.ejmSpSec;

import javax.sql.DataSource;

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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
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
	
    @Bean
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
	
}
