import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Day4
{

  private static class Board {
    private final List<List<Integer>> rows = new ArrayList<>(5);
    private final List<List<Integer>> columns = new ArrayList<>(5);

    Board(List<String> boardLines) {
      for (int count=0; count<5; count++) {
        columns.add(new ArrayList<>(5));
      }
      for (String boardLine : boardLines) {
        List<Integer> numbers = new ArrayList<>(Arrays.stream(boardLine.trim().split("\\s+")).map(Integer::parseInt).toList());
        rows.add(numbers);
        for (int columnOffset = 0; columnOffset < numbers.size(); columnOffset++) {
          List<Integer> columnNumbers = columns.get(columnOffset);
          columnNumbers.add(numbers.get(columnOffset));
        }
      }
    }

    Optional<Integer> numberSelected(int number) {
      Optional<Integer> result = checkList(rows, number);
      boolean matched = false;
      Integer value = 0;
      if (result.isPresent()) {
        value = result.get();
        matched = true;
      }
      result = checkList(columns, number);
      if (result.isPresent()) {
        value = result.get();
        matched = true;
      }
      return matched ? Optional.of(value) : Optional.empty();
    }

    Optional<Integer> checkList(List<List<Integer>> list, int number) {
      boolean matched = false;
      for (List<Integer> listEntry: list) {
        if (listEntry.remove((Integer)number)) {
          if (listEntry.size()==0) {
            matched = true;
            break;
          }
        }
      }
      if (matched) {
        int total = 0;
        for (List<Integer> listEntry: list) {
          total += listEntry.stream().mapToInt(Integer::intValue).sum();
        }
        return Optional.of(total);
      } else {
        return Optional.empty();
      }
    }
  }

  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day4.dat"));
    var lineCount = lines.size();

    // Part 1
    List<Integer> numbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).toList();

    // Build boards
    int lineOffset = 2;
    List<Board> boards = new ArrayList<>((lines.size()-2) / 6);
    while (lineOffset < lines.size()) {
      boards.add(new Board(lines.subList(lineOffset, lineOffset+5)));
      lineOffset += 6;
    }

    boolean foundBingo = false;
    for (Integer number: numbers) {
      for (Board board: boards) {
        Optional<Integer> bingo = board.numberSelected(number);
        if (bingo.isPresent()) {
          System.out.printf("Bingo !! -> %d%n", bingo.get() * number);
          foundBingo = true;
          break;
        }
      }
      if (foundBingo) {
        break;
      }
    }

    // Part 2
    lineOffset = 2;
    boards = new ArrayList<>((lines.size()-2) / 6);
    while (lineOffset < lines.size()) {
      boards.add(new Board(lines.subList(lineOffset, lineOffset+5)));
      lineOffset += 6;
    }

    int lastBingo = 0;
    int lastNumber = 0;
    List<Board> winningBoards = new ArrayList<>();
    for (Integer number: numbers) {
      for (Board board: boards) {
        Optional<Integer> bingo = board.numberSelected(number);
        if (bingo.isPresent()) {
          lastBingo = bingo.get();
          lastNumber = number;
          winningBoards.add(board);
        }
      }
      if (winningBoards.size()>0) {
        boards.removeAll(winningBoards);
        winningBoards.clear();
      }
      if (boards.size()==0) {
        break;
      }
    }
    System.out.printf("Squid Bingo !! -> %d%n", lastBingo * lastNumber);

  }
}

