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
    public String addOrUpdateDetail(@ModelAttribute("slipForm") SlipForm slipForm,
                                    @Validated @ModelAttribute("detailForm") DetailForm detailForm,
                                    BindingResult bindingResult,
                                    @RequestParam(required = false) String tempId,
                                    @RequestParam(required = false) String type,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("detailForm", detailForm);
            model.addAttribute("reasonList", SlipConstants.REASONS);
            model.addAttribute("transportList", SlipConstants.TRANSPORTS);
            // ✅ エラーフラグメントだけ返す
            return "fragments/slipFragment :: detailModalFragment";
        }

        model.addAttribute("hasErrors", false);

        // 既存の明細更新 or 新規追加
        if (tempId != null) {
            slipForm.getDetailForms().replaceAll(d ->
                    d.getTempId().equals(tempId) ? detailForm : d
            );
        } else {
            slipForm.getDetailForms().add(detailForm);
        }

        // 小計再計算
        slipForm.getDetailForms().forEach(d -> {
            int amount = d.getAmount() != null ? Integer.parseInt(d.getAmount()) : 0;
            d.setSubtotal("往復".equals(d.getRoundTrip()) ? amount * 2 : amount);
        });

        // 合計再計算
        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        // CSVフラグがあるかどうか
        boolean hasCsvDetails = slipForm.getDetailForms().stream()
                .anyMatch(DetailForm::isNewFromCsv);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("editable", true);
        model.addAttribute("showCsvOnly", hasCsvDetails);
        model.addAttribute("type", resolveType(type));

        // ✅ 明細テーブル部のみ返す
        return "fragments/slipFragment :: slipMainFragment";
    }


    @PostMapping("/delete")
    public String deleteDetail(@ModelAttribute("slipForm") SlipForm slipForm,
                               @RequestParam(required = false) String tempId,
                               @RequestParam(required = false) String type,
                               Model model) {

        slipForm.getDetailForms().removeIf(d -> d.getTempId().equals(tempId));

        // 合計計算
        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        boolean hasCsvDetails = slipForm.getDetailForms().stream()
                .anyMatch(DetailForm::isNewFromCsv);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("editable", true);
        model.addAttribute("showCsvOnly", hasCsvDetails);
        model.addAttribute("type", resolveType(type));

        return "fragments/slipFragment :: slipMainFragment";
    }

    private String resolveType(String type) {
        if (type == null || type.isBlank()) {
            return "temporary"; // デフォルト
        }

        return switch (type) {
            case "new", "temporary", "approval", "remand", "confirm" -> type;
            default -> "temporary";
        };
    }

}
