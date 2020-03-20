package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import java.util.List;
public class PrettyPrinting {
  @EpiTest(testDataFile = "pretty_printing.tsv")

  public static int minimumMessiness(List<String> words, int lineLength) {
    int[] messiness = new int[words.size()];
    int spaces = lineLength - words.get(0).length();
    messiness[0] = spaces * spaces;
    for (int i = 1; i < words.size(); i++) {
      spaces = lineLength - words.get(i).length();
      int m = spaces * spaces;
      messiness[i] = m + messiness[i-1];
      for (int j = 1; j <= i; j++) {
        spaces -= words.get(i-j).length() + 1;
        if (spaces < 0) break;
        m = spaces * spaces;
        int prevMessiness = i-j-1>=0 ? messiness[i-j-1]: 0;
        messiness[i] = Math.min(messiness[i], m + prevMessiness);
      }
    }
    return messiness[messiness.length - 1];
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "PrettyPrinting.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
