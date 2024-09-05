package com.project.book.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record BookRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,

        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String author,

        @NotNull(message = "102")
        Integer bookNumber,

        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String about,

        @NotNull(message = "104")
        boolean shareable
) {
}
