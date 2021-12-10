package com.twcrone.spacemines

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController {
    @GetMapping("/game")
    String getGame() {
        "{}"
    }
}
