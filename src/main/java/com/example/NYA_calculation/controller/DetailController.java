package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detail")
@SessionAttributes("slipForm")
public class DetailController {

    @PostMapping("/submit")
    public String addOrUpdateDetailAjax(@ModelAttribute("slipForm") SlipForm slipForm,
                                        @Validated @ModelAttribute("detailForm") DetailForm detailForm,
                                        BindingResult bindingResult,
                                        @RequestParam(required = false) Integer index,
                                        Model model) {

        // バリデーションエラー判定用フラグ
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);          // ←追加
            model.addAttribute("errors", bindingResult);    // 元のエラーもセット
            model.addAttribute("detailForm", detailForm);// 元の値も保持
            model.addAttribute("reasonList", SlipConstants.REASONS);
            model.addAttribute("transportList", SlipConstants.TRANSPORTS);
            return "slip/new :: detailModalFragment";       // モーダル部分のみ返す
        }

        // 成功時フラグ
        model.addAttribute("hasErrors", false);

        // 編集 or 追加
        if (index != null) {
            slipForm.getDetailForms().set(index, detailForm);
        } else {
            slipForm.getDetailForms().add(detailForm);
        }

        // 合計計算
        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        model.addAttribute("slipForm", slipForm);

        return "slip/new :: detailFragment";   // 明細一覧フラグメントを返す
    }


    @PostMapping("/delete")
    public String deleteDetailAjax(@ModelAttribute("slipForm") SlipForm slipForm,
                                   @RequestParam Integer index,
                                   Model model) {

        slipForm.getDetailForms().remove((int) index);

        // 合計計算
        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        model.addAttribute("slipForm", slipForm);

        return "slip/new :: detailFragment";
    }

}
