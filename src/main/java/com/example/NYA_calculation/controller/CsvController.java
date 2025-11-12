package com.example.NYA_calculation.controller;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.error.CsvFormatException;
import com.example.NYA_calculation.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/csv")
@SessionAttributes("bulkDetails")
public class CsvController {

    @Autowired
    CsvService csvService;

    @ModelAttribute("bulkDetails")
    public List<DetailForm> initBulkDetails() {
        return new ArrayList<>();
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "csv/upload";
    }

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("csvFile") MultipartFile csvFile,
                            @ModelAttribute("bulkDetails") List<DetailForm> bulkDetails,
                            Model model) {

        if (csvFile == null || csvFile.isEmpty()) {
            model.addAttribute("error", "CSVファイルを選択してください。");
            return "csv/upload";
        }

        try {
            var parsedDetails = csvService.parseCsv(csvFile);
            bulkDetails.clear();
            bulkDetails.addAll(parsedDetails);

        } catch (CsvFormatException e) {
            model.addAttribute("error", e.getMessage());
            return "csv/upload";
        }

        return "redirect:/csv/confirm";
    }

    @GetMapping("/confirm")
    public String confirmBulkAdd(@ModelAttribute("bulkDetails") List<DetailForm> bulkDetails, Model model) {
        model.addAttribute("details", bulkDetails);
        return "csv/confirm";
    }

}
