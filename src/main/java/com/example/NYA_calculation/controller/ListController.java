package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/list")
public class ListController {

    @Autowired
    SlipService slipService;
    @Autowired
    UserService userService;

    @GetMapping("/temp")
    public String showTempList(Model model, @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        model.addAttribute("slipForms", slipService.getSTempSlips(loginUserDetails.getUser().getId()));

        return "list/temp";
    }

    @GetMapping("/approval")
    public String showApprovalList(Model model, @AuthenticationPrincipal LoginUserDetails loginUserDetails) {

        model.addAttribute("slipForms", slipService.getApprovalSlips(loginUserDetails.getUser().getId()));

        return "list/approval";
    }
}
