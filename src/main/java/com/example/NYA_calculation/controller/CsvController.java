package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.error.CsvFormatException;
import com.example.NYA_calculation.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/csv")
@SessionAttributes("slipForm")
public class CsvController {

    @Autowired
    CsvService csvService;

    /**
     * CSVアップロード画面表示
     */
    @GetMapping("/upload")
    public String showUploadForm() {
        return "csv/upload";
    }

    /**
     * CSVファイルを読み込み、slipFormに追加
     */
    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("csvFile") MultipartFile csvFile,
                            @ModelAttribute("slipForm") SlipForm slipForm,
                            Model model) {

        if (csvFile == null || csvFile.isEmpty()) {
            model.addAttribute("error", "CSVファイルを選択してください。");
            return "csv/upload";
        }

        try {
            List<DetailForm> importedDetails = csvService.parseCsv(csvFile);

            // ✅ 既存明細と統合（UUIDで区別される）
            slipForm.getDetailForms().addAll(importedDetails);

            // ✅ 合計再計算
            int total = slipForm.getDetailForms().stream()
                    .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                    .sum();
            slipForm.setTotalAmount(total);

            // 画面再描画用データ
            model.addAttribute("slipForm", slipForm);
            model.addAttribute("detailForm", new DetailForm());
            model.addAttribute("reasonList", SlipConstants.REASONS);
            model.addAttribute("transportList", SlipConstants.TRANSPORTS);

        } catch (CsvFormatException e) {
            model.addAttribute("error", e.getMessage());
            return "csv/upload";
        }

        return "csv/confirm";
    }

    /**
     * CSV確認画面表示
     */
    @GetMapping("/confirm")
    public String confirmBulkAdd(@ModelAttribute("slipForm") SlipForm slipForm,
                                 Model model) {

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);
        return "csv/confirm";
    }
}
