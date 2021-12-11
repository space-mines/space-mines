package com.twcrone.spacemines

class Game {
    List<Sector> sectors = new ArrayList<>()
    List<Integer> mines = new ArrayList<>()

    Game reset() {
        sectors.each {
            it.flagged = false
            it.radiation = -1
        }
        this
    }

    static Game generate(int size, int mineCount) {
        int id = 0
        def game = new Game()
        for(x in (0..size)) {
            for(y in (0..size)) {
                for(z in (0..size)) {
                    game.sectors << new Sector(id: ++id, x: x, y: y, z: z)
                }
            }
        }
        def random = new Random()
        mineCount.times {
            int i = random.nextInt(size)
            while(game.mines.contains(i)) {
                i = random.nextInt(size)
            }
            game.mines << i
        }
        game
    }
}
