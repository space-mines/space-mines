package com.twcrone.spacemines

class Game {
    List<Sector> sectors = new ArrayList<>()

    Game reset() {
        sectors.each {
            it.flagged = false
            it.radiation = -1
        }
        this
    }
}
