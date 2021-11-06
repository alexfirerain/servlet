package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // разворачивались в корневой контекст
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            final var isNumbered = path.matches("/api/posts/\\d+");
            long postId = -1;
            if (isNumbered) {
                try {
                    postId = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                } catch (Exception ignored) {}
            }
            // получение всех постов
            if (method.equals("GET") && path.equals("/api/posts")) {
                controller.all(resp);
                return;
            }

            // получение поста по номеру
            if (method.equals("GET") && isNumbered) {
                if (postId <= 0)
                    throw new NotFoundException("Невнятный номер поста.");
                controller.getById(postId, resp );
                return;
            }

            // публикация или обновление поста (номер в теле)
            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }

            // удаление поста по номеру
            if (method.equals("DELETE") && isNumbered) {
                if (postId <= 0)
                    throw new NotFoundException("Невнятный номер поста.");
                controller.removeById(postId, resp);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

