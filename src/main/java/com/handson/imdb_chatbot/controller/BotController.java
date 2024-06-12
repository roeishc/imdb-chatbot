package com.handson.imdb_chatbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
public class BotController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String helloWorld(){
        return "Hello, World!";
    }

}
