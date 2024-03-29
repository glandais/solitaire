package io.github.glandais.solitaire.klondike;

import io.github.glandais.solitaire.common.board.Board;
import io.github.glandais.solitaire.common.board.Cards;
import io.github.glandais.solitaire.common.board.Pile;
import io.github.glandais.solitaire.common.board.PlayablePile;
import io.github.glandais.solitaire.common.cards.CardEnum;
import io.github.glandais.solitaire.common.execution.ActionEnum;
import io.github.glandais.solitaire.common.execution.CardAction;
import io.github.glandais.solitaire.common.execution.TargetEnum;
import io.github.glandais.solitaire.common.move.MovableStack;
import io.github.glandais.solitaire.common.move.Move;
import io.github.glandais.solitaire.common.move.Movement;
import io.github.glandais.solitaire.klondike.enums.KlondikePilesEnum;

import java.util.ArrayList;
import java.util.List;

public class StockOldPile implements PlayablePile<KlondikePilesEnum> {

    @Override
    public List<MovableStack<KlondikePilesEnum>> getMovableStacks(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile) {
        if (!pile.visible().isEmpty()) {
            return List.of(
                    new MovableStack<>(KlondikePilesEnum.STOCK, Cards.of(pile.visible().getLast()))
            );
        } else if (!pile.hidden().isEmpty()) {
            return List.of(
                    new MovableStack<>(KlondikePilesEnum.STOCK, Cards.of(pile.hidden().getLast()))
            );
        } else {
            return List.of();
        }
    }

    @Override
    public Movement<KlondikePilesEnum> accept(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile, MovableStack<KlondikePilesEnum> movableStack) {
        if (movableStack.from() == KlondikePilesEnum.STOCK) {
            if (pile.hidden().isEmpty()) {
                return null;
            }
            // card can be discarded/picked
            return new Movement<>(movableStack, KlondikePilesEnum.STOCK);
        }
        // no card can be moved to stock pile
        return null;
    }

    @Override
    public List<CardAction<KlondikePilesEnum>> getActions(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile, Move<KlondikePilesEnum> move) {
        // a single card
        if (move.getFrom() == KlondikePilesEnum.STOCK) {
            List<CardAction<KlondikePilesEnum>> actions = new ArrayList<>(4);

            CardEnum cardEnum = move.getCards().getLast();
            if (!pile.visible().isEmpty() && cardEnum == pile.visible().getLast()) {
                // remove top visible card
                actions.add(new CardAction<>(KlondikePilesEnum.STOCK, TargetEnum.VISIBLE_LAST, ActionEnum.REMOVE, cardEnum));
                if (move.getTo() == KlondikePilesEnum.STOCK) {
                    // discard card
                    actions.add(new CardAction<>(KlondikePilesEnum.STOCK, TargetEnum.HIDDEN_FIRST, ActionEnum.ADD, cardEnum));
                }
            }

            // put new card
            if (!pile.hidden().isEmpty()) {
                CardEnum last = pile.hidden().getLast();
                actions.add(new CardAction<>(KlondikePilesEnum.STOCK, TargetEnum.HIDDEN_LAST, ActionEnum.REMOVE, last));
                actions.add(new CardAction<>(KlondikePilesEnum.STOCK, TargetEnum.VISIBLE_FIRST, ActionEnum.ADD, last));
            }
            return actions;
        }
        return List.of();
    }

}
