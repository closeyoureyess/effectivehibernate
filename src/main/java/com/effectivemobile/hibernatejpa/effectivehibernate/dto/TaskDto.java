package com.effectivemobile.hibernatejpa.effectivehibernate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TaskDto implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи", example = "1")
    @Min(value = 1, message = "Идентификатор задачи не должен быть меньше единицы")
    @Max(value = 2147483647, message = "Идентификатор задачи не должен быть больше 2147483647")
    private Long id;

    @Schema(description = "Заголовок задачи", example = "Тестовая задача")
    @NotBlank(message = "Заголовок не может быть пуст")
    @Size(min = 1, message = "Длина заголовка задачи не должна быть меньше 1 символа")
    @Size(max = 2000, message = "Длина заголовка задачи не должна быть больше 2 тысяч символов")
    private String title;

    @Schema(description = "Описание задачи", example = "Тестовое описание задачи")
    @Size(max = 30000, message = "Длина описания не должна превышать 30000 символов")
    private String description;

    @Schema(description = "Статус задачи", example = "true")
    private Boolean status;

    @Schema(description = "Дата задачи в календаре", example = "2020-01-01T00:01:10")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCalendar;

}
