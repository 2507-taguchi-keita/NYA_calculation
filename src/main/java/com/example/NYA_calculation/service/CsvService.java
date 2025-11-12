package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.error.CsvFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private static final DateTimeFormatter CSV_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public List<DetailForm> parseCsv(MultipartFile csvFile) {

        List<DetailForm> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {

            String line;
            br.readLine(); // ヘッダー行スキップ

            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");

                if (cols.length < 5) {
                    throw new CsvFormatException("CSVフォーマットが不正です。（日付, 往復, 金額, 交通手段, 申請理由 が必要）");
                }

                DetailForm form = new DetailForm();

                // ✅ フォーマットを指定してパース
                form.setBillingDate(LocalDate.parse(cols[0], CSV_DATE_FORMAT));

                form.setRoundTrip(cols[1]);
                form.setAmount(cols[2]);
                form.setTransportation(cols[3]);
                form.setReason(cols[4]);

                if (cols.length >= 6) {
                    form.setRemark(cols[5]);
                }

                list.add(form);
            }

        } catch (DateTimeParseException e) {
            throw new CsvFormatException("日付形式が不正です。yyyy/MM/dd 形式で指定してください。");
        } catch (Exception e) {
            throw new CsvFormatException("CSVの読み込み中にエラーが発生しました。");
        }

        return list;
    }

}
