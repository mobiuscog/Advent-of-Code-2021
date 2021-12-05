import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class Day5
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day5.dat"), LineDef::fromString);

    // Part 1
    var gridCount = new HashMap<Point, Integer>();
    assert lines != null;
    for (LineDef line : lines) {
      if (!line.isDiagonal()) {
        for (Point point : line) {
          gridCount.merge(point, 1, Integer::sum);
        }
      }
    }
    long result = gridCount.values().stream().filter(v -> v > 1).count();
    System.out.printf("Part 1 Result: %d %n", result);

    // Part 2
    gridCount.clear();
    for (LineDef line : lines) {
      for (Point point : line) {
        gridCount.merge(point, 1, Integer::sum);
      }
    }
    result = gridCount.values().stream().filter(v -> v > 1).count();
    System.out.printf("Part 2 Result: %d %n", result);
  }

  record Point(int x, int y) { }
  
  static class LineDef
      implements Iterable<Point>
  {
    final Point start;

    final Point end;

    LineDef(Point[] points) {
      assert points.length == 2;
      this.start = points[0];
      this.end = points[1];
    }

    static Stream<LineDef> fromString(Stream<String> line) {
      return line
          .map(String::trim)
          .map(s -> Arrays.stream(s.split("\\s+"))
              .filter(c -> !c.startsWith("-"))
              .map(p -> Arrays.stream(p.split(","))
                  .map(Integer::parseInt).toArray(Integer[]::new))
              .map(a -> new Point(a[0], a[1])).toArray(Point[]::new))
          .map(LineDef::new);
    }

    boolean isHorizontal() {
      return start.y == end.y;
    }

    boolean isVertical() {
      return start.x == end.x;
    }

    boolean isDiagonal() {
      return !isHorizontal() && !isVertical();
    }

    @Override
    public Iterator<Point> iterator() {
      return new Iterator<>()
      {
        int x = start.x;

        int y = start.y;

        Point lastValue = null;

        @Override
        public boolean hasNext() {
          return lastValue == null || lastValue.x != end.x || lastValue.y != end.y;
        }

        @Override
        public Point next() {
          if (!hasNext()) {
            throw new IllegalStateException();
          }
          Point nextValue = new Point(x, y);
          if (isHorizontal()) {
            if (x < end.x) {
              x += 1;
            }
            else {
              x -= 1;
            }
          }
          else if (isVertical()) {
            if (y < end.y) {
              y += 1;
            }
            else {
              y -= 1;
            }
          }
          else {
            if (x < end.x) {
              x += 1;
            }
            else if (x > end.x) {
              x -= 1;
            }
            if (y < end.y) {
              y += 1;
            }
            else if (y > end.y) {
              y -= 1;
            }
          }
          lastValue = nextValue;
          return nextValue;
        }
      };
    }
  }
}
