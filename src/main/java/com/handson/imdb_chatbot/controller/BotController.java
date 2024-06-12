package com.handson.imdb_chatbot.controller;

import com.handson.imdb_chatbot.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

}
