package io.github.glandais.solitaire.klondike;

import io.github.glandais.solitaire.common.board.Board;
import io.github.glandais.solitaire.common.board.Cards;
import io.github.glandais.solitaire.common.board.Pile;
import io.github.glandais.solitaire.common.board.PlayablePile;
import io.github.glandais.solitaire.common.cards.CardEnum;
import io.github.glandais.solitaire.common.cards.ColorEnum;
import io.github.glandais.solitaire.common.cards.OrderEnum;
import io.github.glandais.solitaire.common.execution.ActionEnum;
import io.github.glandais.solitaire.common.execution.CardAction;
import io.github.glandais.solitaire.common.execution.TargetEnum;
import io.github.glandais.solitaire.common.move.MovableStack;
import io.github.glandais.solitaire.common.move.Move;
import io.github.glandais.solitaire.common.move.Movement;
import io.github.glandais.solitaire.klondike.enums.KlondikePilesEnum;
import io.github.glandais.solitaire.klondike.enums.PileTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class TableauPile implements PlayablePile<KlondikePilesEnum> {

    public static final TableauPile INSTANCE = new TableauPile();

    @Override
    public List<MovableStack<KlondikePilesEnum>> getMovableStacks(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile) {
        List<MovableStack<KlondikePilesEnum>> movableStacks = new ArrayList<>(2);
        // all stack
        if (!pile.visible().isEmpty()) {
            Cards cards = pile.visible().copy();
            movableStacks.add(new MovableStack<>(pile.pileType(), cards));
        }
        if (pile.visible().size() > 1) {
            Cards cards = Cards.of(pile.visible().getLast());
            // single card
            movableStacks.add(new MovableStack<>(pile.pileType(), cards));
        }
        return movableStacks;
    }

    @Override
    public Movement<KlondikePilesEnum> accept(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile, MovableStack<KlondikePilesEnum> movableStack) {
        // can't move stack to itself
        if (movableStack.from() == pile.pileType()) {
            return null;
        }
        CardEnum cardEnum = movableStack.cards().getFirst();
        if (movableStack.from() == KlondikePilesEnum.STOCK &&
            !board.getPile(movableStack.from()).hidden().isEmpty() &&
            board.getPile(movableStack.from()).hidden().getLast() == cardEnum) {
            return null;
        }
        if (!isPossible(pile, movableStack)) {
            return null;
        }
        if (movableStack.from().getPileTypeEnum() == PileTypeEnum.TABLEAU) {
            return acceptFromTableau(board, pile, movableStack);
        }
        return new Movement<>(movableStack, pile.pileType());
    }

    private boolean isPossible(Pile<KlondikePilesEnum> pile, MovableStack<KlondikePilesEnum> movableStack) {
        if (movableStack.cards().isEmpty()) {
            return false;
        }
        CardEnum first = movableStack.cards().getFirst();
        if (pile.visible().isEmpty()) {
            return first.getOrderEnum() == OrderEnum.KING;
        }
        CardEnum last = pile.visible().getLast();
        ColorEnum lastColor = last.getColorEnum();
        ColorEnum firstColor = first.getColorEnum();
        if (lastColor == firstColor) {
            return false;
        } else {
            int lastOrder = last.getOrderEnum().getOrder();
            int firstOrder = first.getOrderEnum().getOrder();
            return firstOrder + 1 == lastOrder;
        }
    }

    private Movement<KlondikePilesEnum> acceptFromTableau(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile,
            MovableStack<KlondikePilesEnum> movableStack) {
        // accept only full stacks
        if (movableStack.cards().size() != board.getPile(movableStack.from()).visible().size()) {
            return null;
        }
        // do not move a king starting stack without hidden cards
        if (
                movableStack.cards().getFirst().getOrderEnum() == OrderEnum.KING &&
                board.getPile(movableStack.from()).hidden().isEmpty()
        ) {
            return null;
        } else {
            return new Movement<>(movableStack, pile.pileType());
        }
    }

    @Override
    public List<CardAction<KlondikePilesEnum>> getActions(Board<KlondikePilesEnum> board, Pile<KlondikePilesEnum> pile, Move<KlondikePilesEnum> move) {
        Cards cards = move.getCards();
        List<CardAction<KlondikePilesEnum>> actions = new ArrayList<>(cards.size() + 1);
        if (move.getFrom() == pile.pileType()) {
            // remove cards
            for (int i = cards.size() - 1; i >= 0; i--) {
                CardEnum cardEnum = cards.get(i);
                actions.add(new CardAction<>(pile.pileType(), TargetEnum.VISIBLE_LAST, ActionEnum.REMOVE, cardEnum));
            }
            // no more visible card, show last hidden if present
            if (cards.size() == pile.visible().size() && !pile.hidden().isEmpty()) {
                CardEnum last = pile.hidden().getLast();
                actions.add(new CardAction<>(pile.pileType(), TargetEnum.HIDDEN_LAST, ActionEnum.REMOVE, last));
                actions.add(new CardAction<>(pile.pileType(), TargetEnum.VISIBLE_LAST, ActionEnum.ADD, last));
            }
        } else {
            // add cards
            for (CardEnum cardEnum : cards) {
                actions.add(new CardAction<>(pile.pileType(), TargetEnum.VISIBLE_LAST, ActionEnum.ADD, cardEnum));
            }
        }
        return actions;
    }

}
