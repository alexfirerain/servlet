package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

/**
 * Предоставляет методы, соответствующие функционалу сервера,
 * осуществляет трансформацию представления данных между форматом запроса
 * и форматом хранения-обработки и записывает данные в ответ.
 */
@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_PLAIN = "text/plain";
    private static final Gson converter = new Gson();

    private final PostService service;

    /**
     * Инициализует контроллер над указанным сервисом.
     * @param service сервис, над которым будет властвовать контроллер.
     */
    public PostController(PostService service) {
        this.service = service;
    }

    /**
     * Записывает в ответ список постов, полученный от сервиса.
     * @param response ответ, в который будет записан список всех доступных постов.
     * @throws IOException  при проблемах со связью.
     */
    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        response.getWriter().print(converter.toJson(data));
    }

    /**
     * Записывает в ответ пост с запрошенным номером, получив его от сервиса (если не будет прервано исключением).
     * @param id       запрашиваемый номер поста.
     * @param response ответ, в который будет записан найденный пост.
     * @throws IOException при проблемах со связью.
     */
    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.getById(id);
        response.getWriter().print(converter.toJson(data));
    }

    /**
     * Передаёт сервису полученный пост на сохранение, получает его обратно
     * в сохранённом виде и записывает в ответ.
     * @param body     входной поток, трактуемый как тело поста.
     * @param response ответ, в который будет записан сохранённый пост.
     * @throws IOException при проблемах со связью.
     */
    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var post = converter.fromJson(body, Post.class);
        final var data = service.save(post);
        response.getWriter().print(converter.toJson(data));
    }

    /**
     * Распоряжается контроллеру удалить пост с указанным номером и,
     * если этот процесс не будет прерван исключением, записывает в ответ "OK"
     * в стиле plain text.
     * @param id       номер поста, который требуется удалить.
     * @param response ответ, в который будет записано "OK".
     * @throws IOException при проблемах со связью.
     */
    public void removeById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_PLAIN);
        service.removeById(id);
        response.getWriter().print("OK");
    }
}
