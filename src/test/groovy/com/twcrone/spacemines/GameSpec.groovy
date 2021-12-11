package com.twcrone.spacemines

import spock.lang.Specification

class GameSpec extends Specification {

    def "reveal only sector and there are no mines"() {
        given:
        def game = Game.generate(size, 0)
        game.mines = mines

        when:
        game.reveal(0)

        then:
        game.sectors[0].radiation == expectedRadiation

        where:
        size    |   mines   ||  expectedRadiation
        1       |   []      ||  0
        2       |   [1]     ||  1
    }


}
