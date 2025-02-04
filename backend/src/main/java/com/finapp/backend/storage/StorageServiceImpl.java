package com.finapp.backend.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Override
    @Async
    public CompletableFuture<String> uploadFile(MultipartFile file) {
        return CompletableFuture.completedFuture("");
    }
}
