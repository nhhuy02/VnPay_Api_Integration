package com.example.VNPay_Api_Integration.category;

import lombok.*;

public abstract class CategoryDto {
    @Builder
    @Getter
    @Setter
    public static class CreateRequest {
        public String name;
        public String description;
    }

    @Builder
    @Getter
    @Setter
    public static class UpdateRequest {
        public String id;
        public String name;
        public String description;
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryResponse {
        public String id;
        public String name;
        public String description;
    }
}
