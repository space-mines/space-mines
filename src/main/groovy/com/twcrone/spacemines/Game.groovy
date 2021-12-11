package com.twcrone.spacemines

class Game {
    GameState state = GameState.PLAYING
    List<Sector> sectors = new ArrayList<>()
    List<Integer> mines = new ArrayList<>()

    Game reset() {
        state = GameState.PLAYING
        sectors.each {
            it.flagged = false
            it.radiation = -1
        }
        this
    }

    static Game generate(int size, int mineCount) {
        int id = 0
        def game = new Game()
        for(x in (0..size-1)) {
            for(y in (0..size-1)) {
                for(z in (0..size-1)) {
                    game.sectors[id] = new Sector(id: id, x: x, y: y, z: z)
                    id++
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

    Game reveal(int sectorId) {
        def sector = sectors[sectorId]
        sector.radiation = 0
        for(x in (sector.x-1..sector.x+2)) {
            for(y in (sector.y-1..sector.y+1)) {
                for(z in (sector.z-1..sector.z+1)) {
                   sector.radiation += radiationFrom(x, y, z)
                }
            }
        }
        return this
    }
    Game putMineAt(int x, int y, int z) {
        int sectorId = getSectorIdFor(x, y, z)
        this.mines << sectorId
        this
    }

    int radiationFrom(int x, int y, int z) {
        int sectorId = getSectorIdFor(x, y, z)
        mines.contains(sectorId) ? 1 : 0
    }

    int getSectorIdFor(int x, int y, int z) {
        def sector = this.sectors.find {
            it.x == x && it.y == y && it.z == z
        }
        sector?.id ?: -1
    }
}
