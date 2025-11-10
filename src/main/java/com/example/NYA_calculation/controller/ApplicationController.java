package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.entity.Detail;
import com.example.NYA_calculation.repository.entity.Slip;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.DetailService;
import com.example.NYA_calculation.service.SlipService;
import com.example.NYA_calculation.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@Controller
public class ApplicationController {

    @Autowired
    SlipService slipService;
    @Autowired
    UserService userService;
    @Autowired
    DetailService detailService;

    //ユーザーが自分の申請一覧を確認するための画面
    @GetMapping("/application-list")
    public ModelAndView applicationList(
            @AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam(defaultValue = "0") int page) {

        ModelAndView mav = new ModelAndView("list");

        // すべての申請を取得
        List<Slip> allSlips = slipService.findByUserId(loginUser.getUser().getId());

        // 1ページあたりの件数
        int pageSize = 10;

        // 表示範囲を決める
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allSlips.size());

        // 範囲外チェック
        List<Slip> slipList = new ArrayList<>();
        if (fromIndex < allSlips.size()) {
            slipList = allSlips.subList(fromIndex, toIndex);
        }

        // 総ページ数
        int totalPages = (int) Math.ceil((double) allSlips.size() / pageSize);
        for (Slip s : slipList) {
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
        mav.addObject("userName", loginUser.getUser().getName());

        return mav;
    }

    //申請済み伝票画面表示(明細一覧を表示)
    @GetMapping("/application-detail/{id}")
    public ModelAndView submittedInvoice(@AuthenticationPrincipal LoginUserDetails loginUser,
                                   @PathVariable String id) {
        ModelAndView mav = new ModelAndView("application/detail");

        // ID形式チェック
        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
            mav.addObject("details", new ArrayList<Detail>());
            mav.addObject("loginUser", loginUser);
            return mav;
        }

        Integer slipId = Integer.valueOf(id);

        // 明細情報を取得
        List<Detail> details = detailService.findBySlipId(slipId);
        if (details == null) {
            details = new ArrayList<>();
        }

        mav.addObject("details", details);
        mav.addObject("loginUser", loginUser);
        return mav;
    }

}
