package com.twcrone.spacemines

import org.springframework.stereotype.Service

@Service
class GameService {
    static final int DEFAULT_SIZE = 4
    static Game instance = Game.generate(DEFAULT_SIZE, 1)

    Game reset() {
        instance = Game.generate(DEFAULT_SIZE, 1)
    }

    Game revealSector(int sectorId) {
        instance.reveal(sectorId)
    }

    Game markSector(int sectorId) {
        instance.mark(sectorId)
    }
}
