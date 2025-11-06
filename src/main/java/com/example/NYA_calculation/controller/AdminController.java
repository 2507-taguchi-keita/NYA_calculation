package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    DetailService detailService;

    //管理者画面
    @GetMapping("/admin")
    public ModelAndView showAdmin(){
        ModelAndView mav = new ModelAndView("admin/index");
        List<ExpenseSummary> summaryList = detailService.getMonthlyTotal();
        mav.addObject("summaryList", summaryList);
        return mav;
    }
}
