package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.repository.entity.Detail;
import com.example.NYA_calculation.repository.entity.Slip;
import com.example.NYA_calculation.security.LoginUserDetails;
import com.example.NYA_calculation.service.DetailService;
import com.example.NYA_calculation.service.SlipService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@Controller
public class ReturnedController {

    @Autowired
    SlipService slipService;
    @Autowired
    DetailService detailService;

    //差し戻し一覧表示画面
    @GetMapping("/returned")
    public ModelAndView returnedDetail(
            @AuthenticationPrincipal LoginUserDetails loginUser,
            @RequestParam(defaultValue = "0") int page) {

        ModelAndView mav = new ModelAndView("returned");
        // すべての申請を取得
        List<Slip> allSlips = slipService.findByUserIdSlips(loginUser.getUser().getId());
        // 1ページあたりの件数
        int pageSize = 10;
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

            String departmentName = switch (s.getUserId()) {
                case 1 -> "経理部";
                case 2 -> "人事部";
                case 3 -> "営業部";
                default -> "未所属";
            };
            s.setDepartmentName(departmentName);
        }

        mav.addObject("slipList", slipList);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("userName", loginUser.getUser().getName());

        return mav;
    }

    //差し戻し伝票画面(差し戻された明細を一覧表示)
    @GetMapping("/remand/{id}")
    public ModelAndView returnSlip(
            @AuthenticationPrincipal LoginUserDetails loginUser,
            @PathVariable String id,
            @ModelAttribute("detailForm") DetailForm form, // 🔹 Flash Attributeを受け取る
            RedirectAttributes redirectAttributes) {

        //ID形式チェック
        if (id == null || id.isBlank() || !id.matches("^[0-9]+$")) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(E0013));
            return new ModelAndView("redirect:/returned");
        }

        Integer slipId = Integer.valueOf(id);
        Slip slip = slipService.findById(slipId);

        //DBに存在するかのチェック(存在しなければ一覧画面へ）
        if (slip == null) {
            redirectAttributes.addFlashAttribute("errorMessages", List.of(E0013));
            return new ModelAndView("redirect:/returned");
        }

        //明細が空の場合はそのまま表示
        List<Detail> details = detailService.findBySlipId(slipId);
        if (details == null) {
            details = new ArrayList<>();
        }

        ModelAndView mav = new ModelAndView("returned/detail");
        mav.addObject("transportList", SlipConstants.TRANSPORTS);
        mav.addObject("slip", slip);
        mav.addObject("details", details);
        mav.addObject("loginUser", loginUser);
        if (form == null || form.getId() == null) {
            mav.addObject("detailForm", new DetailForm());
        } else {
            mav.addObject("detailForm", form);
        }
        return mav;
    }

    //差し戻された明細編集処理
    @PostMapping("/remand/edit/{id}")
    public String editDetail(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute DetailForm form,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) throws IOException {

        // バリデーションエラー時の処理
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("errorMessages",
                    result.getAllErrors().stream()
                            .map(e -> e.getDefaultMessage())
                            .toList());
            redirectAttributes.addFlashAttribute("detailForm", form);
            redirectAttributes.addFlashAttribute("openModal", id);
            return "redirect:/remand/" + form.getSlipId();
        }

        form.setId(id);
        detailService.updateDetail(form);
        return "redirect:/remand/" + form.getSlipId();
    }

    //差し戻し伝票再申請処理
    @PostMapping("/remand/reapplication/{id}")
    public ModelAndView reapplicationSlip(@PathVariable("id") String id,
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
        slipService.reapplicationSlip(slipId, loginUser.getUser().getId());
        slipService.cancelSlip(slipId, loginUser.getUser().getId());
        redirectAttributes.addFlashAttribute("successMessage", "再申請を受け付けました。");
        return new ModelAndView("redirect:/returned");
    }

    //申請取り消し機能
    @PostMapping("/remand/cancel/{id}")
    public ModelAndView cancelSlip(
            @PathVariable("id") String id,
            @AuthenticationPrincipal LoginUserDetails loginUser,
            RedirectAttributes redirectAttributes){

        List<String> errorMessages = new ArrayList<>();

        // ID形式チェック
        if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
            errorMessages.add(E0013);
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/returned");
        }

        Integer slipId = Integer.valueOf(id);
        slipService.cancelSlip(slipId, loginUser.getUser().getId());
        redirectAttributes.addFlashAttribute("successMessage", "申請を取り消しました。");
        return new ModelAndView("redirect:/returned");
    }
}
