package com.twcrone.spacemines

import org.springframework.stereotype.Service

@Service
class GameService {
    static final int DEFAULT_SIZE = 4
    static Game instance

    static {
        instance = new Game()
        for(x in (0..DEFAULT_SIZE)) {
            for(y in (0..DEFAULT_SIZE)) {
                for(z in (0..DEFAULT_SIZE)) {
                    instance.sectors << new Sector(x: x, y: y, z: z)
                }
            }
        }
    }

    Game getCurrent() {
        instance
    }

    Game revealSector(int x, int y, int z) {
        instance
    }

    Game markSector(int x, int y, int z) {
        instance
    }
}
