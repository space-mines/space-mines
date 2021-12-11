package com.twcrone.spacemines

import org.springframework.stereotype.Service

@Service
class GameService {
    static final int DEFAULT_SIZE = 4
    static Game instance = Game.generate(DEFAULT_SIZE, 1)

    Game reset() {
        instance.reset()
    }

    Game revealSector(int sectorId) {
        instance.reveal(sectorId)
    }

    Game markSector(int sectorId) {
        def sector = findById(sectorId)
        if(sector) {
            sector.flagged = !sector.flagged
        }
        instance
    }

    Sector findById(int sectorId) {
        instance.sectors.find { it.id == sectorId }
    }
}
