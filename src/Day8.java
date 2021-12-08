import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day8
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day8.dat"));

    var part1Count = 0;
    var part2Count = 0;
    for (var line : lines) {
      var lineSplit = line.trim().split("\\|");
      var output = lineSplit[1].trim().split("\\s+");
      var outputWordValues = new int[output.length];
      for (int i = 0; i < output.length; i++) {
        var word = output[i];
        var length = word.length();
        if (length == 2 || length == 3 || length == 4 || length == 7) {
          part1Count++;
        }
        outputWordValues[i] = calculateWordValue(word);
      }

      var input = Arrays.stream(lineSplit[0].trim().split("\\s+"))
          .collect(Collectors.groupingBy(String::length));

      var digitValues = new int[10];
      // get digit value based on number of segments
      digitValues[1] = calculateWordValue(input.get(2).get(0));
      digitValues[7] = calculateWordValue(input.get(3).get(0));
      digitValues[4] = calculateWordValue(input.get(4).get(0));
      digitValues[8] = calculateWordValue(input.get(7).get(0));

      // deduce
      var segmentValuesCF = digitValues[1];
      var segmentValueA = digitValues[7] - segmentValuesCF;
      var segmentValuesEG = digitValues[8] - digitValues[4] - segmentValueA;
      var segmentValuesBD = digitValues[4] - segmentValuesCF;
      String foundWord = null;
      for (var word : input.get(5)) {
        var wordValue = calculateWordValue(word);
        if ((wordValue & segmentValuesEG) == segmentValuesEG) {
          digitValues[2] = wordValue;
          foundWord = word;
          break;
        }
      }
      assert foundWord != null;
      input.get(5).remove(foundWord);
      var segmentValuesCD = digitValues[2] - segmentValuesEG - segmentValueA;
      var segmentValueD = segmentValuesBD & segmentValuesCD;
      digitValues[0] = digitValues[8] - segmentValueD;
      var segmentValueB = segmentValuesBD - segmentValueD;
      var segmentValueF = (digitValues[0] + segmentValueD) - (digitValues[2] + segmentValueB);
      var segmentValueC = segmentValuesCF - segmentValueF;
      foundWord = null;
      for (var word : input.get(5)) {
        var wordValue = calculateWordValue(word);
        if ((wordValue & segmentValueB) == segmentValueB) {
          digitValues[5] = wordValue;
          foundWord = word;
          break;
        }
      }
      assert foundWord != null;
      input.get(5).remove(foundWord);
      var segmentValueE = digitValues[8] - digitValues[5] - segmentValueC;
      var segmentValueG = segmentValuesEG - segmentValueE;
      digitValues[3] = segmentValueA + segmentValuesCF + segmentValueD + segmentValueG;
      digitValues[6] = digitValues[5] + segmentValueE;
      digitValues[9] = digitValues[8] - segmentValueE;
      StringBuilder outputValue = new StringBuilder();
      for (int outputWordValue : outputWordValues) {
        for (int i = 0; i < digitValues.length; i++) {
          if (outputWordValue == digitValues[i]) {
            outputValue.append(i);
            break;
          }
        }
      }
      part2Count += Integer.parseInt(outputValue.toString());
    }

    System.out.printf("Part 1 count: %d %n", part1Count);
    System.out.printf("Part 2 count: %d %n", part2Count);
  }

  static byte calculateWordValue(String word) {
    byte value = 0;
    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      switch (c) {
        case 'a' -> value |= 0b0000001;
        case 'b' -> value |= 0b0000010;
        case 'c' -> value |= 0b0000100;
        case 'd' -> value |= 0b0001000;
        case 'e' -> value |= 0b0010000;
        case 'f' -> value |= 0b0100000;
        case 'g' -> value |= 0b1000000;
      }
    }
    return value;
  }
}
