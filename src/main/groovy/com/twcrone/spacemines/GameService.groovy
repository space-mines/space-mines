package com.twcrone.spacemines

import org.springframework.stereotype.Service

@Service
class GameService {
    static final int DEFAULT_SIZE = 4
    static Game instance

    static {
        int id = 0
        instance = new Game()
        for(x in (0..DEFAULT_SIZE)) {
            for(y in (0..DEFAULT_SIZE)) {
                for(z in (0..DEFAULT_SIZE)) {
                    instance.sectors << new Sector(id: ++id, x: x, y: y, z: z)
                }
            }
        }
    }

    Game getCurrent() {
        instance.reset()
    }

    Game revealSector(int sectorId) {
        def sector = findById(sectorId)
        if(sector) {
            sector.radiation++
        }
        instance
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
