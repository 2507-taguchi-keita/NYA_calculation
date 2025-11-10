package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.DepartmentConstants;
import com.example.NYA_calculation.repository.entity.Slip;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    // ホーム画面表示
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal LoginUserDetails loginUser) {
        Integer depId = loginUser.getUser().getDepartmentId();
        String departmentLabel;

        if (depId != null
                && depId >= 0
                && depId < DepartmentConstants.DEPARTMENTS.size()) {
            departmentLabel = DepartmentConstants.DEPARTMENTS.get(depId);
        } else {
            departmentLabel = "未所属";
        }
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("userName", loginUser.getUser().getName());
        model.addAttribute("department", departmentLabel);

        return "index";
    }
}
