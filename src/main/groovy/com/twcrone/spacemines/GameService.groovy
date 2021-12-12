package com.twcrone.spacemines

import org.springframework.stereotype.Service

@Service
class GameService {
    static final int DEFAULT_SIZE = 5
    static final int DEFAULT_MINE_COUNT = 3
    static Game instance = Game.generate(DEFAULT_SIZE, 1)

    Game create(Integer size, Integer mineCount) {
        instance = Game.generate(size ?: DEFAULT_SIZE, mineCount ?: DEFAULT_MINE_COUNT)
    }

    Game revealSector(int sectorId) {
        instance.reveal(sectorId)
    }

    Game markSector(int sectorId) {
        instance.mark(sectorId)
    }
}
