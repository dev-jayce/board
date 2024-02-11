package com.fastcampus.board.model.post;

import java.util.Objects;

public class PostPostRequestBody {
    private String body;

    public PostPostRequestBody() {
    }

    public PostPostRequestBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostPostRequestBody that)) return false;
        return Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBody());
    }
}
