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
    String getGame(@RequestParam(required = false) Integer size,
                   @RequestParam(required = false) Integer mineCount) {
        def game = gameService.create(size, mineCount)
        def json = jsonGenerator.toJson(game)
        json
    }

    @GetMapping("/game/reveal")
    String reveal(@RequestParam Integer sectorId) {
        def game = gameService.revealSector(sectorId)
        def json = jsonGenerator.toJson(game)
        json
    }

    @GetMapping("/game/mark")
    String mark(@RequestParam Integer sectorId) {
        def game = gameService.markSector(sectorId)
        def json = jsonGenerator.toJson(game)
        json
    }
}
