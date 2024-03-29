package io.github.glandais.solitaire.common.board;

import io.github.glandais.solitaire.common.execution.CardAction;
import io.github.glandais.solitaire.common.move.MovableStack;
import io.github.glandais.solitaire.common.move.Move;
import io.github.glandais.solitaire.common.move.Movement;

import java.util.List;

public interface PlayablePile<T extends PileType<T>> {
    List<MovableStack<T>> getMovableStacks(Board<T> board, Pile<T> pile);

    Movement<T> accept(Board<T> board, Pile<T> pile, MovableStack<T> movableStack);

    List<CardAction<T>> getActions(Board<T> board, Pile<T> pile, Move<T> move);
}
