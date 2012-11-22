package bowling

class Game {
    static final NUMBER_OF_FRAMES = 10
    final List<Frame> frames = []

    Game() {
        NUMBER_OF_FRAMES.times { frames << new Frame() }
    }

    def knockedDown(numberOfPins) {
        if (isComplete()) throw new GameAlreadyCompleted()
        frames.any { frame -> frame.accept(numberOfPins) }
    }

    def isComplete() {
        frames*.isComplete().every()
    }

    def score() {
        frames*.score().sum(0)
    }
}
