package aoc2015.jav;

class Day10 {

  private static int solve(int count, String s) {
    for (int i = 0; i < count; i++)
      s = doIter(s);
    return s.length();
  }

  private static String doIter(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); ) {
      int count = 1;
      for (int j = i + 1; j < s.length() && s.charAt(i) == s.charAt(j); j++)
        count++;
      sb.append(count);
      sb.append(s.charAt(i));
      i += count;
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    String s = "3113322113";
    System.out.println("Part One = " + solve(40, s));
    System.out.println("Part Two = " + solve(50, s));
  }
}
