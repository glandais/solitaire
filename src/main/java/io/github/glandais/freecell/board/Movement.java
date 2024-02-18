package io.github.glandais.freecell.board;

import io.github.glandais.freecell.board.enums.PilesEnum;

public record Movement(MovableStack movableStack, PilesEnum to, int score) {
}