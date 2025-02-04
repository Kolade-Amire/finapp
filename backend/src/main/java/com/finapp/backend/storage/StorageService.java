package com.finapp.backend.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface StorageService {

    CompletableFuture<String> uploadFile(MultipartFile file);
}
