package com.onlineshopthymeleaf.model;

import lombok.Builder;

@Builder
public record FileDto(
        String fileName,
        String fileLocation,
        String extension,
        Long size
) {
}
