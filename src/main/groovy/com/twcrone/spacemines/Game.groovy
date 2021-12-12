package com.twcrone.spacemines

class Game {
    Integer size
    GameState state = GameState.PLAY
    List<Sector> sectors = new ArrayList<>()
    List<Integer> mines = new ArrayList<>()

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
        game.size = size
        game
    }

    Game over() {
        this.sectors.each { it.radiation = 0 } // make them all disap
        this.state = GameState.LOSE
        this
    }

    Game reveal(int sectorId) {
        if(!validSectorId(sectorId)) {
            throw new IllegalArgumentException("$sectorId is not a valid sector to reveal")
        }
        if(this.sectors[sectorId].marked) {
            return this // noop
        }
        if(mines.contains(sectorId)) {
            return this.over()
        }
        _reveal(sectorId)
        return this
    }

    private Game _reveal(int sectorId) {
        def sector = sectors[sectorId]
        // already revealed, don't do it again
        if(sector.radiation != -1) {
            return this
        }
        sector.radiation = 0
        for(x in (sector.x-1..sector.x+1)) {
            for(y in (sector.y-1..sector.y+1)) {
                for(z in (sector.z-1..sector.z+1)) {
                    sector.radiation += radiationFrom(x, y, z)
                }
            }
        }
        if(sector.radiation == 0) {
            for(x in (sector.x-1..sector.x+1)) {
                for(y in (sector.y-1..sector.y+1)) {
                    for(z in (sector.z-1..sector.z+1)) {
                        if(validCoordinate(x) && validCoordinate(y) && validCoordinate(z)) {
                            int id = getSectorIdFor(x, y, z)
                            _reveal(id)
                        }
                    }
                }
            }
        }
        this
    }

    Game mark(int sectorId) {
        if(!validSectorId(sectorId)) {
            throw new IllegalArgumentException("$sectorId is not a valid sector to mark")
        }
        def sector = sectors[sectorId]
        if(sector.radiation == -1) {
            sector.marked = !sector.marked
        }
        this
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
        sector ? sector.id : -1
    }

    private boolean validCoordinate(int i) {
        i > -1 && i < size
    }

    private boolean validSectorId(int i) {
        i > -1 && i < sectors.size()
    }
}
