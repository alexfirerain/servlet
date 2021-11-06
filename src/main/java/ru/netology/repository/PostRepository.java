package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
    private final Map<Long, Post> posts;

    public PostRepository() {
        posts = new ConcurrentHashMap<>();
        // можно при создании также указать в отдельное поле адрес сохранения
        // тогда эта инициализация добавляется в MainServlet.init()

        try (ObjectInputStream extractor = new ObjectInputStream(new FileInputStream("STORAGE"))) {
            PostStorage preceding = (PostStorage) extractor.readObject();
            for (Post post : preceding.getPosts())
                posts.put(post.getId(), post);

        } catch (Exception e) {
            System.out.println("Данные из STORAGE не добавлены по той или иной причине.");
            e.printStackTrace();
        }
    }

    public void store() {
        try (ObjectOutputStream conservator = new ObjectOutputStream(new FileOutputStream("STORAGE"))) {
            conservator.writeObject(new PostStorage(all()));
        } catch (IOException e) {
            System.out.println("Данные в STORAGE не сохранены по той или иной причине.");
            e.printStackTrace();
        }
    }


    public List<Post> all() {
        List<Post> postList = new ArrayList<>();
        for (Map.Entry<Long, Post> entry : posts.entrySet())
            postList.add(entry.getValue());
        return postList;
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            var newId = (long) posts.size() + 1;
            while (posts.containsKey(newId)) newId++;
            post.setId(newId);
            posts.put(newId, post);
        } else {
            // как клиент вообще может послать POST-запрос на отсутствующий пост?
            // если он ранее его удалил, то пусть такое сохранение отменит удаление
            posts.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        if (!posts.containsKey(id))
            throw new NotFoundException("Пост не найден.");
        posts.remove(id);
    }
}
