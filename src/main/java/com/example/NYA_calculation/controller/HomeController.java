package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.DepartmentConstants;
import com.example.NYA_calculation.security.LoginUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ホーム画面表示
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal LoginUserDetails loginUser,
                       HttpSession session) {

        //CustomCustomAccessDeniedHandlerで出たエラーを拾う
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        Integer depId = loginUser.getUser().getDepartmentId();
        String departmentLabel;

        if (depId != null
                && depId >= 0
                && depId < DepartmentConstants.DEPARTMENTS.size()) {
            departmentLabel = DepartmentConstants.DEPARTMENTS.get(depId);
        } else {
            departmentLabel = "未所属";
        }

        String authorityLabel = switch (loginUser.getUser().getAuthority()) {
            case 0 -> "一般社員";
            case 1 -> "承認者";
            default -> "未所属";
        };

        String departmentName = switch (loginUser.getUser().getDepartmentId()) {
            case 1 -> "経理部";
            case 2 -> "人事部";
            case 3 -> "営業部";
            default -> "未所属";
        };

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("userName", loginUser.getUser().getName());
        model.addAttribute("department", departmentName);
        model.addAttribute("authorityLabel", authorityLabel);
        return "index";
    }
}
