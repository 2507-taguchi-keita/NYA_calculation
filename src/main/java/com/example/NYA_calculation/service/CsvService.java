package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.error.CsvFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CsvService {

    private static final DateTimeFormatter CSV_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public List<DetailForm> parseCsv(MultipartFile csvFile) {

        List<DetailForm> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            br.readLine(); // ヘッダー行スキップ

            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");

                if (cols.length < 5) {
                    throw new CsvFormatException("CSVフォーマットが不正です。（日付, 理由, 交通手段, 金額, 往復 が必要）");
                }

                DetailForm form = new DetailForm();

                // ✅ 日付形式を安全に変換
                form.setBillingDate(LocalDate.parse(cols[0], CSV_DATE_FORMAT));

                // ✅ CSV列の並びに対応（※あなたのCSV順序に合わせて要調整）
                form.setReason(cols[1]);
                form.setTransportation(cols[2]);
                form.setAmount(cols[3]);
                form.setRoundTrip(cols[4]);

                if (cols.length >= 6) {
                    form.setRemark(cols[5]);
                }

                // ✅ subtotal自動計算
                int amount = 0;
                try {
                    amount = Integer.parseInt(form.getAmount());
                } catch (NumberFormatException e) {
                    throw new CsvFormatException("金額が数値ではありません: " + form.getAmount());
                }

                if ("往復".equals(form.getRoundTrip())) {
                    form.setSubtotal(amount * 2);
                } else {
                    form.setSubtotal(amount);
                }

                // ✅ 一意のUUIDを自動生成
                form.setTempId(UUID.randomUUID().toString());

                list.add(form);
            }

        } catch (DateTimeParseException e) {
            throw new CsvFormatException("日付形式が不正です。yyyy/MM/dd 形式で指定してください。");
        } catch (CsvFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new CsvFormatException("CSVの読み込み中にエラーが発生しました。");
        }

        return list;
    }
}
