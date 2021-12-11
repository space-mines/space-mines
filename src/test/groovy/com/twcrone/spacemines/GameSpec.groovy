package com.twcrone.spacemines

import spock.lang.Specification

class GameSpec extends Specification {

    def "reveal only sector and there are no mines"() {
        given:
        def game = Game.generate(size, mineCount)

        when:
        game.reveal(0)

        then:
        game.sectors[0].radiation == expectedRadiation

        where:
        size    |   mineCount   ||  expectedRadiation
        1       |   0           ||  0
    }


}
