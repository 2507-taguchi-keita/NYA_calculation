package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.DetailForm;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    public List<DetailForm> parseCsv(MultipartFile file) {
        List<DetailForm> result = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(
                            "billingDate",
                            "reason",
                            "transportation",
                            "roundTrip",
                            "amount",
                            "remark"
                    ) // ← CSVヘッダ名
                    .withSkipHeaderRecord()
                    .parse(reader);

            for (CSVRecord record : records) {

                DetailForm form = new DetailForm();

                // LocalDate に変換
                String billingDateStr = record.get("billingDate").trim();
                form.setBillingDate(billingDateStr.isEmpty() ? null : LocalDate.parse(billingDateStr));

                form.setReason(record.get("reason").trim());
                form.setTransportation(record.get("transportation").trim());

                // roundTrip は String 型
                form.setRoundTrip(record.get("roundTrip").trim());

                // amount は String 型 + バリデーション付き（NotNull & Pattern）
                form.setAmount(record.get("amount").trim());

                // subtotal は（amount * 2）などの計算
                try {
                    int amount = Integer.parseInt(form.getAmount());
                    form.setSubtotal("往復".equals(form.getRoundTrip()) ? amount * 2 : amount);
                } catch (NumberFormatException e) {
                    form.setSubtotal(0); // 不正なら 0
                }

                // 備考
                form.setRemark(record.isSet("remark") ? record.get("remark").trim() : "");

                result.add(form);
            }

        } catch (Exception e) {
            throw new RuntimeException("CSVの読込に失敗しました", e);
        }

        return result;
    }
}
