package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepositoryImpl;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Осуществляет диспетчеризацию запросов.
 */
public class MainServlet extends HttpServlet {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    private PostController controller;

    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");

    /**
     * Инициализует слои.
     */
    @Override
    public void init() {
        final var repository = (PostRepositoryImpl) context.getBean("postRepositoryImpl");
        final var service = (PostService) context.getBean("postService");
        controller = (PostController) context.getBean("postController");
    }

    /**
     * Осуществляет обработку запроса и переводит его в команду для контроллера.
     * @param req  запрос, который обрабатывается.
     * @param resp ответ, который будет послан.
     */
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
            if (GET.equals(method) && "/api/posts".equals(path)) {
                controller.all(resp);
                return;
            }

            // получение поста по номеру
            if (GET.equals(method) && isNumbered) {
                if (postId <= 0)
                    throw new NotFoundException("Невнятный номер поста.");
                controller.getById(postId, resp );
                return;
            }

            // публикация или обновление поста (номер в теле)
            if (POST.equals(method) && "/api/posts".equals(path)) {
                controller.save(req.getReader(), resp);
                return;
            }

            // удаление поста по номеру
            if (DELETE.equals(method) && isNumbered) {
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

