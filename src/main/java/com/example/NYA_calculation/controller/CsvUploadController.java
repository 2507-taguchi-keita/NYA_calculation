package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/csv")
@SessionAttributes("slipForm")
public class CsvUploadController {

    @Autowired
    Validator validator;

    @GetMapping("/upload")
    public String showUploadPage(Model model, @ModelAttribute("slipForm") SlipForm slipForm) {
        model.addAttribute("slipForm", slipForm);
        return "csv/upload"; // 上記 HTML のテンプレート名
    }

    @PostMapping("/upload")
    public String handleCsvUpload(
            @RequestParam("csvFile") MultipartFile csvFile,
            @ModelAttribute("slipForm") SlipForm slipForm,
            Model model) {

        if (csvFile.isEmpty()) {
            model.addAttribute("csvError", "CSVファイルを選択してください");
            return "csv/upload";
        }

        if (!csvFile.getOriginalFilename().endsWith(".csv")) {
            model.addAttribute("csvError", "CSVファイルのみアップロード可能です");
            return "csv/upload";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine();
            int rowNum = 1;
            List<DetailForm> newDetails = new ArrayList<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            while ((line = reader.readLine()) != null) {
                rowNum++;

                // 空行は無視
                if (line.trim().isEmpty()) continue;

                // 空列保持のため -1 を付ける（最後の空欄カンマ対応）
                String[] cols = line.split(",", -1);

                // 5列（備考なし） or 6列（備考あり）を許可
                if (cols.length != 5 && cols.length != 6) {
                    model.addAttribute("csvError", "CSVの内容に不備があります（行 " + rowNum + "）");
                    return "csv/upload";
                }

                // 5列の場合 → 備考欄（cols[5]）を空文字で補完
                if (cols.length == 5) {
                    cols = Arrays.copyOf(cols, 6);
                    cols[5] = "";  // 備考を空に設定
                }

                DetailForm df = new DetailForm();
                try {
                    df.setBillingDate(LocalDate.parse(cols[0], formatter));
                } catch (Exception e) {
                    model.addAttribute("csvError", "日付形式が不正です（行 " + rowNum + "）");
                    return "csv/upload";
                }
                df.setReason(cols[1]);
                df.setTransportation(cols[2]);
                df.setAmount(cols[3]);
                df.setRoundTrip(cols[4]);
                df.setRemark(cols[5]); // 備考（5列なら空文字が入っている）

                Set<ConstraintViolation<DetailForm>> violations = validator.validate(df);
                if (!violations.isEmpty()) {
                    String msg = violations.iterator().next().getMessage();
                    model.addAttribute("csvError", "不正な値があります（行 " + rowNum + "）： " + msg);
                    return "csv/upload";
                }

                newDetails.add(df);
            }

            // CSVの内容を SlipForm に一括追加
            slipForm.getCsvDetailForms().clear();
            slipForm.getCsvDetailForms().addAll(newDetails);

            // 小計・合計計算（既存ロジック再利用）
            slipForm.getCsvDetailForms().forEach(d -> {
                int amount = d.getAmount() != null ? Integer.parseInt(d.getAmount()) : 0;
                d.setSubtotal("往復".equals(d.getRoundTrip()) ? amount * 2 : amount);
            });
            int total = slipForm.getCsvDetailForms().stream()
                    .mapToInt(d -> d.getSubtotal() != null ? d.getSubtotal() : 0)
                    .sum();
            slipForm.setTotalAmount(total);

        } catch (IOException e) {
            model.addAttribute("csvError", "CSVの読み込みに失敗しました");
            return "csv/upload";
        }

        model.addAttribute("csvDetailForms", slipForm.getCsvDetailForms());
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);
        return "csv/confirm";
    }

    @GetMapping("/confirm")
    public String confirmCsv(@ModelAttribute("slipForm") SlipForm slipForm,
                             Model model) {

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("detailForm", new DetailForm());
        model.addAttribute("reasonList", SlipConstants.REASONS);
        model.addAttribute("transportList", SlipConstants.TRANSPORTS);
        return "csv/confirm";
    }

}
