package bowling

import spock.lang.Specification

class BowlingTest extends Specification {

    def game = new Game()

    def "empty game"() {
        expect: "is incomplete and has a zero score"
        !game.isComplete()
        game.score() == 0
    }

    def "partial game"() {
        setup: "a game that starts out containing an open"
        [1, 2].each { game.knockedDown(it) }

        expect: "is incomplete and the score is the sum of the pins"
        !game.isComplete()
        game.score() == 1 + 2

        when: "a spare is added"
        [8, 2].each { game.knockedDown(it) }

        then: "the game is still incomplete and the score is still the sum of the pins"
        !game.isComplete()
        game.score() == (1 + 2) + (8 + 2)

        when: "another throw is added"
        game.knockedDown(4)

        then: "the game is still incomplete and the score increases by two times the number of pins (bonus on previous spare)"
        !game.isComplete()
        game.score() == (1 + 2) + (8 + 2 + 4) + 4

        when: "another throw is added"
        game.knockedDown(3)

        then: "the game is still incomplete and the score increases by the number of pins (no more bonus, spare is complete)"
        !game.isComplete()
        game.score() == (1 + 2) + (8 + 2 + 4) + (4 + 3)
    }

    def "complete game"() {
        when: "after adding enough throws to complete the game"
        knockedPins.each { game.knockedDown(it) }

        then: "it is complete and has the correct final score"
        game.isComplete()
        game.score() == finalScore

        where:
        knockedPins                                                  | finalScore
        [1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 2, 7]     | 125
        [1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 2, 8, 6]  | 133
        [1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 10, 8, 6] | 149
        [10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10]             | 300
    }

    def "a complete game doesn't accept more input"() {
        setup:
        20.times { game.knockedDown(1) }

        when:
        game.knockedDown(1)

        then:
        thrown(GameAlreadyCompleted)
        game.isComplete()
        game.score() == 20
    }

    def "you can't knock down more than 10 pins in one throw"() {
        when:
        game.knockedDown(11)

        then:
        thrown(InvalidNumberOfPins)
        game.score() == 0
        !game.isComplete()
    }

    def "you can't knock down more than 10 pins in a frame"() {
        setup:
        game.knockedDown(9)

        when:
        game.knockedDown(2)

        then:
        thrown(InvalidNumberOfPins)
        game.score() == 9
        !game.isComplete()
    }
}
