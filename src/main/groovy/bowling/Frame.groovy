package bowling

class Frame {
    static final MAX_PINS = 10
    final pins = []

    Frame() {
    }

    def accept(numKnockedDown) {
        validateInput(numKnockedDown)
        def didConsume = consumes()
        if (accepts()) pins << numKnockedDown
        didConsume
    }

    def accepts() {
        consumes() || acceptsBonus()
    }

    def isComplete() {
        !accepts()
    }

    def score() {
        pins.sum(0)
    }

    def validateInput(numKnockedDown) {
        if (accepts() && numKnockedDown > MAX_PINS
                || consumes() && score() + numKnockedDown > MAX_PINS)
            throw new InvalidNumberOfPins()
    }

    def consumes() {
        pins.size() == 0 || pins.size() == 1 && !isStrike()
    }

    def acceptsBonus() {
        (isStrike() || isSpare()) && pins.size() < 3
    }

    def isSpare() {
        !isStrike() && pins.size() >= 2 && pins[0] + pins[1] == MAX_PINS
    }

    def isStrike() {
        pins.size() >= 1 && pins[0] == MAX_PINS
    }
}
