package com.example.NYA_calculation.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessages.*;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        List<String> errorMessages = new ArrayList<>();
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        boolean hasInputError = false;

        if (account == null || account.isBlank()) {
            errorMessages.add(E0001);
            hasInputError = true;
        }

        if (password == null || password.isBlank()) {
            errorMessages.add(E0002);
            hasInputError = true;
        }

        if (!hasInputError) {
            if (exception instanceof UsernameNotFoundException) {
                //対応するユーザー情報が存在しない場合の例外
                errorMessages.add(E0003);
            } else if (exception instanceof BadCredentialsException) {
                //ユーザーが存在しないまたはパスワードが間違っているなどした時の例外
                errorMessages.add(E0003);
            } else if (exception instanceof DisabledException) {
                //UserDetails.isEnabledがfalseを返した時　＝　ユーザーが停止の時の例外
                errorMessages.add(E0003);
            }
        }

        request.getSession().setAttribute("account", account);
        request.getSession().setAttribute("errorMessages", errorMessages);
        response.sendRedirect("/login");
    }

}
