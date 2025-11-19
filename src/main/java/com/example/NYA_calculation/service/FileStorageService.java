package com.example.NYA_calculation.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
    private final Path permanentDir = Paths.get("C:/app/receipt");
    // ★ 環境により変更可（AWSなら /var/app/receipt など）

    public FileStorageService() throws IOException {
        Files.createDirectories(tempDir);
        Files.createDirectories(permanentDir);
    }

    /**
     * 一時保存ファイルを本保存ディレクトリに移動し、保存後ファイル名を返す
     */
    public String moveToPermanent(String storedFileName) throws IOException {

        if (storedFileName == null) return null;

        Path source = tempDir.resolve(storedFileName);

        if (!Files.exists(source)) {
            // tmp にない場合（DBからの再表示時など）はそのまま返す
            return storedFileName;
        }

        String newFileName = UUID.randomUUID() + "_" + storedFileName;
        Path target = permanentDir.resolve(newFileName);

        // ファイル移動
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }

    /**
     * 本保存ファイルのURLを返す
     */
    public String getPermanentFileUrl(String storedFileName) {
        return "/files/perm/" + storedFileName;
    }

}
