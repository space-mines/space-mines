package com.twcrone.spacemines

import groovy.json.JsonGenerator
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController {
    JsonGenerator jsonGenerator = new JsonGenerator.Options()
            .excludeNulls()
            .build()

    @Autowired
    GameService gameService

    @GetMapping("/game")
    String getGame(@RequestParam(required = false) Integer x,
                   @RequestParam(required = false) Integer y,
                   @RequestParam(required = false) Integer z,
                   @RequestParam(required = false) Boolean mark) {
        def game
        if(x && y && z) {
            if(mark) {
                game = gameService.markSector(x, y, z)
            }
            else {
                game = gameService.revealSector(x, y, z)
            }
        }
        else {
            game = gameService.getCurrent()
        }
        def json = jsonGenerator.toJson(game)
        json
    }
}
