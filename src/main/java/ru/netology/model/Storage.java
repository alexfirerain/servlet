package ru.netology.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Storage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Post> posts;

    public Storage(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

}
