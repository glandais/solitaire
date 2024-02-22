package io.github.glandais.freecell;

import io.github.glandais.freecell.board.Board;
import io.github.glandais.freecell.board.MovementScore;
import io.github.glandais.freecell.printer.BoardPrinter;
import io.github.glandais.freecell.printer.console.BoardConsolePrinter;
import io.github.glandais.freecell.printer.gui.BoardGuiPrinter;
import io.github.glandais.freecell.serde.BoardMovements;
import io.github.glandais.freecell.serde.Serde;
import io.github.glandais.freecell.solver.BoardSolver;

import java.util.List;

public class SolveNoPrint {

    public static void main(String[] args) {
        // 1126119823
        Board board = new Board(0);
        new BoardConsolePrinter().print(board);
        BoardSolver boardSolver = new BoardSolver(board);
        List<MovementScore> movements = boardSolver.solve();
        Serde.save("board.json", new BoardMovements(board.getSeed(), movements));
        if (movements != null) {
            BoardPrinter boardPrinter = new BoardGuiPrinter();
            boardPrinter.printMovements(board, movements);
        }
    }

}
