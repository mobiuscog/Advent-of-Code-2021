import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class Day4
{
  private static class BingoSquare
  {
    final int row;
    final int column;
    boolean isMarked = false;

    BingoSquare(int row, int column) {
      this.row = row;
      this.column = column;
    }
  }

  private static class BingoBoard {

    private final HashMap<Integer, BingoSquare> bingoSquares = new HashMap<>();

    private final int[] rows;
    private final int[] columns;
    private boolean hasWon;

    BingoBoard(List<String> boardLines) {
      rows = new int[boardLines.size()];
      columns = new int[boardLines.get(0).length()];
      hasWon = false;
      for (int row=0; row<boardLines.size(); row++) {
        var numbers = Arrays.stream(boardLines.get(row).trim().split("\\s+"))
            .map(Integer::parseInt).toList();
        for (int column=0; column<numbers.size(); column++) {
          bingoSquares.put(numbers.get(column), new BingoSquare(row, column));
          rows[row]++;
          columns[column]++;
        }
      }
    }

    Optional<Integer> numberSelected(int number) {
      var square = bingoSquares.get(number);
      if (square != null && !square.isMarked) {
        square.isMarked = true;
        if (--rows[square.row] == 0 || --columns[square.column] == 0) {
          hasWon = true;
          return Optional.of(bingoSquares.entrySet().stream()
              .filter(e -> !e.getValue().isMarked)
              .map(Entry::getKey)
              .mapToInt(Integer::intValue).sum());
        }
      }
      return Optional.empty();
    }

    public boolean hasWon() {
      return hasWon;
    }
  }

  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day4.dat"));
    var numbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).toList();

    // Build boards
    int lineOffset = 2;
    var boards = new ArrayList<BingoBoard>((lines.size()-2) / 6);
    while (lineOffset < lines.size()) {
      boards.add(new BingoBoard(lines.subList(lineOffset, lineOffset+5)));
      lineOffset += 6;
    }

    record Win(int number, int boardRemaining) {}

    // Record wins
    var wins = new ArrayList<Win>();
    for (Integer number: numbers) {
      for (BingoBoard board: boards) {
        if (!board.hasWon()) {
          Optional<Integer> bingo = board.numberSelected(number);
          bingo.ifPresent(integer -> wins.add(new Win(number, integer)));
        }
      }
    }

    // Part 1
    Win firstWin = wins.get(0);
    System.out.printf("Bingo !! -> %d%n", firstWin.number() * firstWin.boardRemaining());

    // Part 2
    Win lastWin = wins.get(wins.size()-1);
    System.out.printf("Squid Bingo !! -> %d%n", lastWin.number() * lastWin.boardRemaining());
  }
}

