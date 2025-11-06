package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.repository.entity.User;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/slip")
public class SlipController {

    @Autowired
    UserService userService;

    @GetMapping("/new")
    public String showSlip(Model model,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        User loginUser = userService.findById(loginUserDetails.getUser().getId());
        User approver = userService.findById(loginUser.getApproverId());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("approver", approver);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        return "slip/new";
    }

}
