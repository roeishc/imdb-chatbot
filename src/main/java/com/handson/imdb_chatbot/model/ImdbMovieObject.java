package com.handson.imdb_chatbot.model;

public class ImdbMovieObject {

    private final String l;   // movie name/title
    private final String id;

    public ImdbMovieObject(String l, String id) {
        this.l = l;
        this.id = id;
    }

    public String getL() {
        return l;
    }

    public String getId() {
        return id;
    }

}
