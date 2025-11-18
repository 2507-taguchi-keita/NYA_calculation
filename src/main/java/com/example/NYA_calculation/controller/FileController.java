package com.example.NYA_calculation.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/files")
public class FileController {

    private final Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
    private final Path permanentDir = Paths.get("C:/app/receipt");

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getTempFile(@PathVariable String fileName) throws IOException {
        return serveFile(tempDir.resolve(fileName), fileName);
    }

    @GetMapping("/perm/{fileName:.+}")
    public ResponseEntity<Resource> getPermanentFile(@PathVariable String fileName) throws IOException {
        return serveFile(permanentDir.resolve(fileName), fileName);
    }

    private ResponseEntity<Resource> serveFile(Path filePath, String fileName) throws IOException {

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        // Content-Type を自動判別
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";  // バイナリとして扱う
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
