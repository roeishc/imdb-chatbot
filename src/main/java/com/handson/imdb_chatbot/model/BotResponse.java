package com.handson.imdb_chatbot.model;

/***
 * This app's response to the POST request sent by DialogFlow
 */
public class BotResponse {

    String fulfillmentText;
    String source = "BOT";

    public String getFulfillmentText() {
        return fulfillmentText;
    }

    public String getSource() {
        return source;
    }

    public static BotResponse of(String fulfillmentText) {
        BotResponse res = new BotResponse();
        res.fulfillmentText = fulfillmentText;
        return res;
    }

}
