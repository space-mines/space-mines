package com.twcrone.spacemines

import spock.lang.Specification
import spock.lang.Unroll

class GameSpec extends Specification {

    def "reveal sector and its radiation based on adjacent mines"() {
        given:
        def game = Game.generate(size, 0)
        mines.each { game.putMineAt(it[0], it[1], it[2]) }

        when:
        def sectorId = game.getSectorIdFor(sectorLocation[0], sectorLocation[1], sectorLocation[2])
        game.reveal(sectorId)

        then:
        game.sectors[sectorId].radiation == expectedRadiation
        game.state == GameState.PLAY

        and: 'revealing again should be a noop'
        game.reveal(sectorId)
        game.sectors[sectorId].radiation == expectedRadiation
        game.state == GameState.PLAY

        where:
        sectorLocation  |   size    |   mines                               ||  expectedRadiation
        [0, 0, 0]       |   1       |   []                                  ||  0
        [0, 0, 0]       |   2       |   [[1, 0, 0]]                         ||  1
        [0, 0, 0]       |   2       |   [[1, 0, 0], [0, 1, 0]]              ||  2
        [0, 1, 0]       |   3       |   [[1, 0, 0], [0, 0, 1]]              ||  2
        [1, 1, 1]       |   3       |   [[1, 0, 0], [2, 2, 2], [0, 1, 1]]   ||  3
        [0, 0, 0]       |   3       |   [[2, 2, 2]]                         ||  0
        [1, 1, 1]       |   4       |   [[0, 1, 1], [3, 3, 3], [3, 1, 1]]   ||  1
    }

    def "reveal sector that is a mine, should end the game"() {
        given:
        def game = Game.generate(size, 0)
        mines.each { game.putMineAt(it[0], it[1], it[2]) }

        when:
        def sectorId = game.getSectorIdFor(sectorLocation[0], sectorLocation[1], sectorLocation[2])
        game.reveal(sectorId)

        then:
        //game.sectors.isEmpty()
        game.sectors.every { it.radiation == 0 }
        game.state == GameState.LOSE

        where:
        sectorLocation  |   size    |   mines
        [0, 0, 0]       |   1       |   [[0, 0, 0]]
        [0, 1, 0]       |   2       |   [[1, 0, 0], [0, 1, 0]]
        [1, 1, 1]       |   3       |   [[1, 0, 0], [1, 1, 1], [0, 1, 1]]
    }

    def "reveal sector that marked should be noop"() {
        given:
        def game = Game.generate(1, 0)
        game.mark(0)

        when:
        game.reveal(0)

        then:
        def sector = game.sectors[0]
        sector.radiation == -1
        game.state == GameState.PLAY
    }

    def "mark revealed sector should be noop"() {
        given:
        def game = Game.generate(1, 0)
        game.reveal(0)

        when:
        game.mark(0)

        then:
        def sector = game.sectors[0]
        sector.radiation == 0
        !sector.flagged
        game.state == GameState.PLAY
    }

    def "reveal sector with no mines around it should 'cascade' to other surrounding sectors"() {
        given:
        def game = Game.generate(size, 0)

        when:
        def sectorId = game.getSectorIdFor(sectorLocation[0], sectorLocation[1], sectorLocation[2])
        game.reveal(sectorId)

        then:
        game.sectors.every { it.radiation == 0 }
        game.state == GameState.PLAY

        where:
        sectorLocation  |   size    |   mines
        [0, 0, 0]       |   1       |   []
        [0, 0, 0]       |   2       |   []
        [0, 1, 0]       |   3       |   []
        [1, 1, 1]       |   4       |   []
    }

    def "mark a sector"() {
        given:
        def game = Game.generate(2, 0)
        game.sectors[2].radiation = radiation

        when:
        game.mark(2)

        then:
        game.sectors[2].flagged == expectedFlagged

        where:
        radiation   ||  expectedFlagged
        -1          ||  true
        0           ||  false
        1           ||  false
    }

    @Unroll
    def "#operation a sector not present should fail"() {
        given:
        def game = Game.generate(1, 0)

        when:
        game."$operation"(100)

        then:
        thrown(IllegalArgumentException)

        where:
        operation << ["mark", "reveal"]
    }
}
