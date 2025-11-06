package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.security.LoginUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ホーム画面表示
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("userName", loginUser.getUser().getName());
        model.addAttribute("department", loginUser.getUser().getDepartmentId());

        return "index";
    }
}
