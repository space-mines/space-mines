package com.twcrone.spacemines

import groovy.json.JsonGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController {
    JsonGenerator jsonGenerator = new JsonGenerator.Options()
            .excludeNulls()
            .excludeFieldsByName("mines")
            .build()

    @Autowired
    GameService gameService

    @GetMapping("/game")
    String getGame(@RequestParam(required = false) Integer sectorId,
                   @RequestParam(required = false) Boolean mark) {
        def game
        if(sectorId != null) {
            if(mark) {
                game = gameService.markSector(sectorId)
            }
            else {
                game = gameService.revealSector(sectorId)
            }
        }
        else {
            game = gameService.reset()
        }
        def json = jsonGenerator.toJson(game)
        json
    }
}
