package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detail")
@SessionAttributes("slipForm")
public class DetailController {

    @PostMapping("/submit")
    public String addOrUpdateDetailAjax(@ModelAttribute("slipForm") SlipForm slipForm,
                                        @ModelAttribute DetailForm detailForm,
                                        @RequestParam(required = false) Integer index,
                                        Model model) {

        if (index != null) {
            // 編集
            slipForm.getDetailForms().set(index, detailForm);
        } else {
            // 追加
            slipForm.getDetailForms().add(detailForm);
        }

        // 合計計算
        int total = slipForm.getDetailForms().stream()
                .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                .sum();
        slipForm.setTotalAmount(total);

        model.addAttribute("slipForm", slipForm);

        return "slip/new :: detailFragment";
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
