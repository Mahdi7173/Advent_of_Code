import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day1 {

	public static int partOne(String s) {
		return s.chars().map(x -> x == '(' ? 1 : -1).sum();
	}

	public static int partTwo(String s, int acc, int step) {
		if (s.length() == 0 || acc == -1)
			return step;
		else if (s.charAt(0) == '(')
			acc++;
		else if (s.charAt(0) == ')')
			acc--;
		return partTwo(s.substring(1), acc, step + 1);
	}

	public static void main(String[] args) throws IOException {
		String s = new String(Files.readAllBytes(Paths.get("./input/Day1_input.txt")));
		System.out.println("Part One = " + partOne(s));
		System.out.println("Part Two = " + partTwo(s, 0, 0));
	}
}
