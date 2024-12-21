package br.csi.sistema_escolar.infraestrutura.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AutenticacaoFilter autenticacaoFilter;
    public SecurityConfig(AutenticacaoFilter filtro){
        this.autenticacaoFilter = filtro;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(crsf-> crsf.disable())
                .sessionManagement(sm-> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/administradores").permitAll()

                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/administradores/**", "/alunos/**", "/disciplinas/**", "/horarios/**", "/professores/**", "/turmas/**").hasAnyAuthority("ROLE_VISU", "ROLE_EDIT")
                        .requestMatchers(HttpMethod.POST, "/alunos", "/disciplinas", "/horarios", "/professores", "/turmas/**").hasAuthority("ROLE_EDIT")
                        .requestMatchers(HttpMethod.PUT, "/administradores/**", "/alunos/**", "/disciplinas/**", "/horarios/**", "/professores/**", "/turmas/**").hasAuthority("ROLE_EDIT")
                        .requestMatchers(HttpMethod.DELETE, "/administradores/**", "/alunos/**", "/disciplinas/**", "/horarios/**", "/professores/**", "/turmas/**").hasAuthority("ROLE_EDIT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(this.autenticacaoFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
