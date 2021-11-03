package ru.netology.repository;

import ru.netology.model.Post;
import ru.netology.model.Storage;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
    private final Map<Long, Post> posts;

    public PostRepository() {
        posts = new ConcurrentHashMap<>();

        try (ObjectInputStream stateReader = new ObjectInputStream(new FileInputStream("STORAGE"))) {
            Storage storage = (Storage) stateReader.readObject();
            for (Post post : storage.getPosts())
                posts.put(post.getId(), post);

        } catch (Exception e) {
            System.out.println("Данные из STORAGE не добавлены по той или иной причине.");
            e.printStackTrace();
        }
    }

    public void saveToStorage() {
        try (ObjectOutputStream store = new ObjectOutputStream(new FileOutputStream("STORAGE"))) {
            store.writeObject(all());
        } catch (IOException e) {
            System.out.println("Данные в STORAGE не сохранены по той или иной причине.");
            e.printStackTrace();
        }
    }


    public List<Post> all() {
        ArrayList<Post> postList = new ArrayList<>();
        for (Map.Entry<Long, Post> entry : posts.entrySet())
            postList.add(entry.getValue());
        return postList;
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        return post;
    }

    public void removeById(long id) {

    }
}
