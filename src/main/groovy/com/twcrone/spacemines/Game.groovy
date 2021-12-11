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

    static Game generate(int size) {
        int id = 0
        def game = new Game()
        for(x in (0..size)) {
            for(y in (0..size)) {
                for(z in (0..size)) {
                    game.sectors << new Sector(id: ++id, x: x, y: y, z: z)
                }
            }
        }
        game
    }
}
