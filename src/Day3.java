import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day3
{
  public static void main(String[] args) {

    var lines = Util.readFile(Path.of("resources/day3.dat"));
    var lineCount = lines.size();

    // Part 1 - bitwise operations in Java - stupid idea. Welcome to brute-force 101 :)
    var gammaCheck = checkCommon(lines, true);
    var epsilonCheck = checkCommon(lines, false);
    var result = Integer.valueOf(gammaCheck, 2) * Integer.valueOf(epsilonCheck, 2);

    System.out.printf("result: %d%n", result);

    // Part 2
    List<String> oxygenLines = new ArrayList<>(lines);
    List<String> co2Lines = new ArrayList<>(lines);

    var lineLength = lines.get(0).length();
    for (int bit = 0; bit < lineLength; bit++) {
      var oxygenCheck = checkCommon(oxygenLines, true);
      var co2Check = checkCommon(co2Lines, false);
      for (String line : lines) {
        if (oxygenLines.contains(line) && line.charAt(bit) != oxygenCheck.charAt(bit)) {
          if (oxygenLines.size() > 1) {
            oxygenLines.remove(line);
          }
        }
        if (co2Lines.contains(line) && line.charAt(bit) != co2Check.charAt(bit)) {
          if (co2Lines.size() > 1) {
            co2Lines.remove(line);
          }
        }
      }
    }

    var oxygenVal = Integer.valueOf(oxygenLines.get(0), 2);
    var co2Val = Integer.valueOf(co2Lines.get(0), 2);

    System.out.printf("result: %d%n", oxygenVal * co2Val);
  }

  private static String checkCommon(List<String> lines, boolean checkMost) {
    var lineLength = lines.get(0).length();
    var zeroCheck = new int[lineLength];
    var oneCheck = new int[lineLength];
    for (String line : lines) {
      for (int i = 0; i < lineLength; i++) {
        if (line.charAt(i) == '0') {
          zeroCheck[i]++;
        }
        else {
          oneCheck[i]++;
        }
      }
    }
    var result = new StringBuilder(lineLength);
    for (int i = 0; i < lineLength; i++) {
      if (checkMost) {
        result.append(oneCheck[i] >= zeroCheck[i] ? '1' : '0');
      }
      else {
        result.append(zeroCheck[i] <= oneCheck[i] ? '0' : '1');
      }
    }
    return result.toString();
  }
}


