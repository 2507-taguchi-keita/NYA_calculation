package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.constant.SlipConstants;
import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.controller.form.SlipForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/detail")
@SessionAttributes("slipForm")
public class DetailController {

    @PostMapping("/submit")
    public String addOrUpdateDetail(@ModelAttribute("slipForm") SlipForm slipForm,
                                    @Validated @ModelAttribute("detailForm") DetailForm detailForm,
                                    BindingResult bindingResult,
                                    @RequestParam(required = false) String tempId,
                                    Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("detailForm", detailForm);
            model.addAttribute("reasonList", SlipConstants.REASONS);
            model.addAttribute("transportList", SlipConstants.TRANSPORTS);
        }

        MultipartFile file = detailForm.getUploadFile();
        String errorMessage = null;

        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();

            // 拡張子チェック
            if (!Objects.requireNonNull(originalFilename).endsWith(".pdf") &&
                    !originalFilename.endsWith(".jpg") &&
                    !originalFilename.endsWith(".jpeg") &&
                    !originalFilename.endsWith(".png")) {
                errorMessage = "PDFまたは画像ファイルのみアップロード可能です";
                model.addAttribute("errorMessage", errorMessage);
            }

            // MIMEタイプチェック
            String contentType = file.getContentType();
            if (!"application/pdf".equals(contentType) &&
                    !"image/jpeg".equals(contentType) &&
                    !"image/png".equals(contentType)) {
                errorMessage = "不正なファイル形式です";
                model.addAttribute("errorMessage", errorMessage);
            }
        }

        if (bindingResult.hasErrors() || errorMessage != null) {
            return "fragments/detailFragment :: detailFragment";
        }

        model.addAttribute("hasErrors", false);

        if (!file.isEmpty()) {

            // 保存ディレクトリ（※後で変更可能）
            Path uploadDir = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
            Files.createDirectories(uploadDir);

            String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(storedFileName);

            // ファイル保存
            file.transferTo(filePath.toFile());

            // DetailForm にセット
            detailForm.setStoredFileName(storedFileName);
            detailForm.setOriginalFileName(file.getOriginalFilename());
            detailForm.setFileUrl("/files/" + storedFileName); // 表示用URL
        }

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

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("status", slipForm.getStatus());

        return "fragments/slipFragment :: slip-detailListFragment";
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

        model.addAttribute("slipForm", slipForm);
        model.addAttribute("status", slipForm.getStatus());

        return "fragments/slipFragment :: slip-detailListFragment";
    }

}
