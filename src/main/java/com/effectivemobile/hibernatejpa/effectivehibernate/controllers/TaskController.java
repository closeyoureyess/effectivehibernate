package com.effectivemobile.hibernatejpa.effectivehibernate.controllers;

import com.effectivemobile.hibernatejpa.effectivehibernate.dto.TaskDto;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.DataCalendarNotBeNullException;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.EntityNotFoundException;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.StatusTaskNotBeNullException;
import com.effectivemobile.hibernatejpa.effectivehibernate.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.effectivemobile.hibernatejpa.effectivehibernate.other.ConstantsClass.IS_DELETE;

/**
 * <pre>
 *     Контроллер создания, редактирования, удаления, получения информации о задачах
 * </pre>
 */
@Tag(name = "Задачи", description = "Позволяет создать, редактировать, удалить, получить информацию о задачах")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
public class TaskController {

    private final TaskService taskService;

    @Autowired
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Эндпоинд POST для создания задачи
     *
     * @param tasksDto DTO объект задачи для создания
     * @return {@link ResponseEntity} с созданной задачей
     */
    @Operation(summary = "Создание задачи", description = "Позволяет создать задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана", content = @Content(examples = @ExampleObject(value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"title\\\": \\\"Тестовая задача\\\",\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"status\\\": true,\\n  \\\"dateCalendar\\\": \\\"2020-01-01T00:01:10\\\"\\n}\""))),
            @ApiResponse(responseCode = "400", description = "Не удалось создать задачу", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример тела запроса для создания задачи", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Создание задачи", value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"title\\\": \\\"Тестовая задача\\\",\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"status\\\": true,\\n  \\\"dateCalendar\\\": \\\"2020-01-01T00:01:10\\\"\\n}\"")))
    @PostMapping(value = "/task/create")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody @NotNull(message = "Объект не может быть null")
                                              TaskDto tasksDto) throws DataCalendarNotBeNullException, StatusTaskNotBeNullException {
        log.info("Создание задачи, POST ");
        TaskDto localTasksDto = taskService.createTasks(tasksDto);
        if (localTasksDto != null) {
            return ResponseEntity.ok(localTasksDto);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Эндпоинд GET для получения информации о задачах по исполнителю
     *
     * @param taskTitle - емейл пользователя, по которому будут найдены задачи, комментарии
     * @param offset    - номер страницы
     * @param limit     - кол-во сущностей на странице
     * @return {@link ResponseEntity<List<TaskDto>>}
     */
    @Operation(summary = "Получить задачи по заголовку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращены задачи по заголовку", content = @Content(examples = @ExampleObject(value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"title\\\": \\\"Тестовая задача\\\",\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"status\\\": true,\\n  \\\"dateCalendar\\\": \\\"2020-01-01T00:01:10\\\"\\n}\""))),
            @ApiResponse(responseCode = "400", description = "Не удалось получить задачи по заголовку", content = @Content)
    })
    @GetMapping(value = "/task/list/{title}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<TaskDto>> getTasksByTitle(
            @PathVariable("title") @Parameter(description = "Заголовок задачи") @NotBlank(message = "Заголовок задачи не может быть пустым")
            String taskTitle,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) @Parameter(description = "Номер страницы", example = "0") @NotNull(message = "Страница не может быть пустой")
            Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(10) @Parameter(description = "Количество сущностей на странице",
                    example = "10") @NotNull(message = "Кол-во сущностей не может быть пустым") Integer limit
    ) {
        log.info("Получение задачи по исполнителю, метод GET " + taskTitle);
        Optional<List<TaskDto>> optionalExecutorTasksDtoList = taskService
                .getTasksByTitle(taskTitle, offset, limit);
        return optionalExecutorTasksDtoList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Эндпоинд PUT для редактирования задач
     *
     * @param tasksDto объект {@link TaskDto}
     * @return {@link ResponseEntity<>}
     */
    @Operation(summary = "Отредактировать задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно отредактирована", content = @Content(examples = @ExampleObject(value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"title\\\": \\\"Тестовая задача\\\",\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"status\\\": true,\\n  \\\"dateCalendar\\\": \\\"2020-01-01T00:01:10\\\"\\n}\""))),
            @ApiResponse(responseCode = "400", description = "Не удалось отредактировать задачу", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Пример тела запроса для редактирования задачи", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Редактирование задачи", value = "\"{\\n  \\\"id\\\": 1,\\n  \\\"title\\\": \\\"Тестовая задача\\\",\\n  \\\"description\\\": \\\"Тестовое описание задачи\\\",\\n  \\\"status\\\": true,\\n  \\\"dateCalendar\\\": \\\"2020-01-01T00:01:10\\\"\\n}\"")))
    @PutMapping("/task/update-tasks")
    public ResponseEntity<TaskDto> editTasks(@Valid @RequestBody @Parameter(description = "Объект TasksDto с полями, требующими редактирования")
                                             @NotNull(message = "TasksDto не может быть null") TaskDto tasksDto) throws EntityNotFoundException {
        Optional<TaskDto> newTasksDto = taskService
                .changeTasks(tasksDto);
        return newTasksDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Эндпоинд DELETE для удаления задач
     *
     * @param idTasks - id задачи для удаления
     * @return {@link ResponseEntity<String>}
     */
    @Operation(summary = "Удалить задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно отредактирована", content = @Content(examples = @ExampleObject(value = "{ \"message\": \"Сущность была удалена\" }"))),
            @ApiResponse(responseCode = "400", description = "Не удалось произвести удаление задачи", content = @Content)
    })
    @DeleteMapping(value = "/task/delete/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> deleteTasks(@PathVariable("id") @Parameter(description = "ID задачи") @NotNull(message = "ID задачи не может быть null")
                                              Long idTasks) {
        log.info("Удаление задачи по id, метод DELETE" + idTasks);
        boolean result = taskService.deleteTasks(idTasks);
        if (result) {
            return ResponseEntity.ok(IS_DELETE);
        }
        return ResponseEntity.noContent().build();
    }
}
