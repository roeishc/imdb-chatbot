package com.handson.imdb_chatbot.controller;

import com.handson.imdb_chatbot.model.BotQuery;
import com.handson.imdb_chatbot.model.BotResponse;
import com.handson.imdb_chatbot.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    private ImdbService imdbService;

    @RequestMapping(value = "getMovieDetails", method = RequestMethod.GET)
    public ResponseEntity<?> getMovieDetails(@RequestParam String movieName) throws IOException {
        return new ResponseEntity<>(imdbService.searchMovie(movieName), HttpStatus.OK);
    }

    @GetMapping(value = "getMovieId")
    public ResponseEntity<?> getMovieId(@RequestParam String movieName) throws IOException {
        return new ResponseEntity<>(imdbService.getMovieObject(movieName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException{
        HashMap<String, String> params = query.getQueryResult().getParameters();
        String res = ImdbService.failure;
        if (params.containsKey("movieName"))
            res = imdbService.searchMovie(params.get("movieName"));
        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
    }

}
