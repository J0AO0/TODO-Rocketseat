package com.todo.filter;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.todo.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    //***COMERÇAR POR AQUI***
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //pegar a autenticação (usuario e senha)
        var authorization = request.getHeader("Authorization");
        var authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecode = Base64.getDecoder().decode(authEncoded);
        var authString = new String(authDecode);

        //***USAR ESSES CONSOLE LOGS COM ELA***
        //System.out.println("Autenticação: ");
        //System.out.println(authEncoded);
        //System.out.println(authDecode);

        // ["juaum", "123"]
        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        //*** Usar esses console logs com ela ***
//        System.out.println("Autenticação: ");
//        System.out.println(username);
//        System.out.println(password);

        //valida usuario
            var user =this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário ou senha inválidos");
            }else{
                //valida senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified){
                    filterChain.doFilter(request, response);
                }else{
                    response.sendError(401, "Usuário ou senha inválidos");
                }
                //segue viagem
            }
    }
}
