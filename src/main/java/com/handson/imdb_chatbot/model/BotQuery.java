package com.handson.imdb_chatbot.model;

/***
 * The request body as received by this app from DialogFlow's chatbot
 */
public class BotQuery {

    QueryResult queryResult;

    public QueryResult getQueryResult() {
        return queryResult;
    }

}
