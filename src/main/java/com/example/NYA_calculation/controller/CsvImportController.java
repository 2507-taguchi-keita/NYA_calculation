package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/csv")
@RequiredArgsConstructor
public class CsvImportController {

    @Autowired
    CsvImportService csvImportService;

    @PostMapping("/import")
    public String previewCsvAjax(@RequestParam("file") MultipartFile file,
                                           @ModelAttribute("slipForm") SlipForm slipForm,
                                           Model model) {

        // CSV → DetailForm list を作成
        List<DetailForm> newDetails = csvImportService.parseCsv(file);

        // 既存明細に追加
        slipForm.getDetailForms().addAll(newDetails);

        // 明細テーブル fragment を返却
        model.addAttribute("slipForm", slipForm);
        return "slip/new :: detailFragment";
    }

}
