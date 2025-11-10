package com.example.NYA_calculation.controller;

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
import org.springframework.web.bind.annotation.*;
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
                                   @PathVariable String id,
                                   RedirectAttributes redirectAttributes) {

        //ID形式チェック
        if (id == null || id.isBlank() || !id.matches("^[0-9]+$")) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(E0013));
            return new ModelAndView("redirect:/application-list");
        }

        Integer slipId = Integer.valueOf(id);
        Slip slip = slipService.findById(slipId);

        //DBに存在するかのチェック(存在しなければ一覧画面へ）
        if (slip == null) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(E0013));
            return new ModelAndView("redirect:/application-list");
        }

        //明細が空の場合はそのまま表示
        List<Detail> details = detailService.findBySlipId(slipId);
        if (details == null) {
            details = new ArrayList<>();
        }

        ModelAndView mav = new ModelAndView("application/detail");
        mav.addObject("slip", slip);
        mav.addObject("details", details);
        mav.addObject("loginUser", loginUser);
        return mav;
    }

    //申請取り消し機能
    @PostMapping("/application-list/cancel/{id}")
    public ModelAndView cancelSlips(
            @PathVariable("id") String id,
            @AuthenticationPrincipal LoginUserDetails loginUser,
            RedirectAttributes redirectAttributes){

        List<String> errorMessages = new ArrayList<>();

        // ID形式チェック
        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
            errorMessages.add(E0013);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/application-list");
        }

        Integer slipId = Integer.valueOf(id);
        slipService.cancelSlip(slipId, loginUser.getUser().getId());
        redirectAttributes.addFlashAttribute("successMessage", "申請を取り消しました。");
        return new ModelAndView("redirect:/application-list");
    }
}
