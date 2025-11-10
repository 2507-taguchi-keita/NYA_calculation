package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.repository.entity.Slip;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.DetailService;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    DetailService detailService;
    @Autowired
    UserService userService;
    @Autowired
    SlipService slipService;

    //管理者画面表示
    @GetMapping("/admin")
    public ModelAndView showAdmin(){
        ModelAndView mav = new ModelAndView("admin/index");
        List<ExpenseSummary> summaryList = detailService.getMonthlyTotal();
        mav.addObject("summaryList", summaryList);
        return mav;
    }

    //ユーザー一覧画面表示
    @GetMapping("/admin/users")
    public ModelAndView allUsers(@AuthenticationPrincipal LoginUserDetails loginUser, @RequestParam(defaultValue = "0") int page){
        ModelAndView mav = new ModelAndView("admin/users");
        Page<UserForm> resultPage = userService.pageUser(page, 10);
        int totalPages = resultPage.getTotalPages();
        int currentPage = resultPage.getNumber();
        int displayRange = 5;
        int startPage = Math.max(0, currentPage - displayRange);
        int endPage = Math.min(totalPages - 1, currentPage + displayRange);

        mav.setViewName("admin/users");
        // ページネーション
        mav.addObject("users", resultPage.getContent());
        mav.addObject("page", resultPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("loginUser", loginUser);
        return mav;
    }

    //ユーザー停止・有効切り替え
    @PostMapping("/admin/users/change/{id}")
    public ModelAndView changeIsStopped(@PathVariable("id") Integer id,
                                        @RequestParam("isStopped") boolean isStopped) {
        userService.changeIsStopped(id, isStopped);
        return new ModelAndView("redirect:/admin/users");
    }

    //管理者用申請一覧画面
    @GetMapping("/admin/requests")
    public ModelAndView adminApplication(@RequestParam(defaultValue = "0") int page){
        ModelAndView mav = new ModelAndView("admin/application");
        // すべての申請データを取得
        List<Slip> allSlips = slipService.getAllSlips();
        int pageSize = 10;
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allSlips.size());
        List<Slip> slipList = new ArrayList<>();

        if (fromIndex < allSlips.size()) {
            slipList = allSlips.subList(fromIndex, toIndex);
        }

        int totalPages = (int) Math.ceil((double) allSlips.size() / pageSize);
        int currentPage = page;
        int displayRange = 5;
        int startPage = Math.max(0, currentPage - displayRange);
        int endPage = Math.min(totalPages - 1, currentPage + displayRange);

        // 数値 → 状態名変換
        for (Slip s : slipList) {
            String label = switch (s.getStatus()) {
                case 0 -> "一時保存";
                case 1 -> "申請中";
                case 2 -> "差し戻し";
                case 3 -> "承認済み";
                default -> "不明";
            };
            s.setStatusLabel(label);

            // ステップラベル変換
            String stepLabel = switch (s.getStep()) {
                case 0 -> "未申請";
                case 1 -> "経理部";
                case 2 -> "経理部長";
                case 3 -> "所属上長";
                case 4 -> "承認済み";
                default -> "不明";
            };
            s.setStepLabel(stepLabel);
        }
        mav.addObject("slipList", slipList);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        return mav;
    }
}
