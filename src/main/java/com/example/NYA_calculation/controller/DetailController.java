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
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("errors", bindingResult);
            model.addAttribute("detailForm", detailForm);
            model.addAttribute("reasonList", SlipConstants.REASONS);
            model.addAttribute("transportList", SlipConstants.TRANSPORTS);
            return "fragments/detailModalFragment :: detailModalFragment";
        }

        model.addAttribute("hasErrors", false);

        if (tempId != null) {
            // 既存のUUIDを持つ明細を探して置き換え
            slipForm.getDetailForms().replaceAll(d ->
                    d.getTempId().equals(tempId) ? detailForm : d
            );
        } else {
            // 新規登録時は新しいUUIDを発行（DetailFormのデフォルトで生成済み）
            slipForm.getDetailForms().add(detailForm);
        }

        slipForm.getDetailForms().forEach(d -> {
            int amount = d.getAmount() != null ? Integer.parseInt(d.getAmount()) : 0;
            d.setSubtotal("往復".equals(d.getRoundTrip()) ? amount * 2 : amount);
        });

        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("editable", true);

        return "fragments/detailFragment :: detailFragment";
    }

    @PostMapping("/delete")
    public String deleteDetail(@ModelAttribute("slipForm") SlipForm slipForm,
                               @RequestParam String tempId,
                               Model model) {

        slipForm.getDetailForms().removeIf(d -> d.getTempId().equals(tempId));

        // 合計計算
        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("editable", true);
        return "fragments/detailFragment :: detailFragment";
    }

}
