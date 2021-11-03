package ru.netology.repository;

import ru.netology.model.Post;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PostStorage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Post> posts;

    public PostStorage(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

}
