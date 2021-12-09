import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day9
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day9.dat"));

    var height = lines.size();
    var width = lines.get(0).length();
    var heightMap = new int[height][width];
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        heightMap[y][x] = lines.get(y).charAt(x) - 48;
      }
    }

    record Location(int x, int y, int value) { }

    // Part 1
    var basinOrigins = new ArrayList<Location>();
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        var value = heightMap[y][x];
        if (y > 0) {
          if (heightMap[y - 1][x] <= value) {
            continue;
          }
        }
        if (y < height - 1) {
          if (heightMap[y + 1][x] <= value) {
            continue;
          }
        }
        if (x > 0) {
          if (heightMap[y][x - 1] <= value) {
            continue;
          }
        }
        if (x < width - 1) {
          if (heightMap[y][x + 1] <= value) {
            continue;
          }
        }
        basinOrigins.add(new Location(x, y, value + 1));
      }
    }

    var part1Total = basinOrigins.stream().mapToInt(l -> l.value).sum();
    System.out.printf("Total for part 1: %d %n", part1Total);

    // Part 2
    record Basin(Location origin, Set<Location> extents)
        implements Comparable<Basin>
    {
      @Override
      public int compareTo(final Basin otherBasin) {
        return otherBasin.extents.size() - Basin.this.extents.size();
      }
    }

    List<Basin> basins = new ArrayList<>();
    for (var origin : basinOrigins) {
      var basin = new Basin(origin, new HashSet<>());
      basins.add(basin);
      var basinExtents = basin.extents;
      var basinUnchecked = new ArrayList<Location>();
      basinUnchecked.add(origin);
      while (basinUnchecked.size() > 0) {
        for (var i = basinUnchecked.size() - 1; i >= 0; i--) {
          var location = basinUnchecked.get(i);
          var x = location.x;
          var y = location.y;
          if (y > 0) {
            if (heightMap[y - 1][x] != 9) {
              var extent = new Location(x, y - 1, heightMap[y - 1][x]);
              if (!basinExtents.contains(extent)) {
                basinUnchecked.add(extent);
                basinExtents.add(extent);
              }
            }
          }
          if (y < height - 1) {
            if (heightMap[y + 1][x] != 9) {
              var extent = new Location(x, y + 1, heightMap[y + 1][x]);
              if (!basinExtents.contains(extent)) {
                basinUnchecked.add(extent);
                basinExtents.add(extent);
              }
            }
          }
          if (x > 0) {
            if (heightMap[y][x - 1] != 9) {
              var extent = new Location(x - 1, y, heightMap[y][x - 1]);
              if (!basinExtents.contains(extent)) {
                basinUnchecked.add(extent);
                basinExtents.add(extent);
              }
            }
          }
          if (x < width - 1) {
            if (heightMap[y][x + 1] != 9) {
              var extent = new Location(x + 1, y, heightMap[y][x + 1]);
              if (!basinExtents.contains(extent)) {
                basinUnchecked.add(extent);
                basinExtents.add(extent);
              }
            }
          }
          basinUnchecked.remove(location);
        }
      }
    }

    var part2Value = basins.stream()
        .sorted()
        .limit(3)
        .mapToInt(b -> b.extents.size())
        .reduce(1, Math::multiplyExact);
    System.out.printf("Total for part 2: %d %n", part2Value);
  }
}
