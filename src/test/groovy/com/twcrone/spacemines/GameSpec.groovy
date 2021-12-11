package com.twcrone.spacemines

import spock.lang.Specification

class GameSpec extends Specification {

    def "reveal only sector and there are no mines"() {
        given:
        def game = Game.generate(size, 0)
        mines.each { game.putMineAt(it[0], it[1], it[2]) }

        when:
        def sectorId = game.getSectorIdFor(sectorLocation[0], sectorLocation[1], sectorLocation[2])
        game.reveal(sectorId)

        then:
        game.sectors[sectorId].radiation == expectedRadiation

        where:
        sectorLocation  |   size    |   mines                   ||  expectedRadiation
        [0, 0, 0]       |   1       |   []                      ||  0
        [0, 0, 0]       |   2       |   [[1, 0, 0]]             ||  1
        [0, 0, 0]       |   2       |   [[1, 0, 0], [0, 1, 0]]  ||  2
    }


}
