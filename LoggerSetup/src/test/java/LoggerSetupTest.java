import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Runs a couple of tests to make sure Log4j2 is setup.
 *
 * NOTE: There are better ways to test log configuration---we will keep it
 * simple here because we just want to make sure you can run and configure
 * Log4j2.
 *
 * This is also not the most informative configuration---it is just one of the
 * most testable ones that require you to learn about how to handle stack trace
 * output.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
public class LoggerSetupTest {

	/** Used to capture console output. */
	private static ByteArrayOutputStream capture = null;

	/**
	 * Setup that runs before each test.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	@BeforeAll
	public static void setup() throws IOException {
		// delete any old debug files
		Files.deleteIfExists(Path.of("debug.log"));

		// capture all system console output
		PrintStream original = System.out;
		capture = new ByteArrayOutputStream();
		System.setOut(new PrintStream(capture));

		// run main() only ONCE to avoid duplicate entries
		// and shutdown log manager to flush the debug files
		LoggerSetup.main(null);
		LogManager.shutdown();

		// restore system.out
		System.setOut(original);
		System.out.println(capture.toString());
	}

	/**
	 * Tests LoggerSetup logger console output.
	 */
	@Nested
	public class A_LoggerConsoleTests {
		/**
		 * Captures the console output and compares to expected.
		 * @param line the line number of console output to test
		 * @param expected the expected output for that line of console output
		 * @throws IOException if an I/O error occurs
		 */
		@ParameterizedTest
		@CsvSource({
			"0,wren",
			"1,egret eagle",
			"2,Catching finch"
		})
		public void testConsole(int line, String expected) throws IOException {
			String[] captured = capture.toString().split("[\\n\\r]+");
			assertEquals(expected, captured[line].strip());
		}
	}

	/**
	 * Tests Root logger console output.
	 */
	@Nested
	public class B_RootConsoleTests {
		/**
		 * Captures the console output and compares to expected.
		 * @param line the line number of console output to test
		 * @param expected the expected output for that line of console output
		 * @throws IOException if an I/O error occurs
		 */
		@ParameterizedTest
		@CsvSource({
			"3,ibis",
			"4,wren",
			"5,egret eagle",
			"6,Catching finch"
		})
		public void testConsole(int line, String expected) throws IOException {
			String[] captured = capture.toString().split("[\\n\\r]+");
			assertEquals(expected, captured[line].strip());
		}
	}

	/**
	 * Tests first part of file output.
	 */
	@Nested
	public class C_FileTests1 {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// test file output is as expected
			List<String> expected = Files.readAllLines(Path.of("src", "test", "resources", "debug.log"));
			List<String> actual = Files.readAllLines(Path.of("debug.log"));

			// only test a subset here
			expected = expected.subList(0, 4);
			actual = actual.subList(0, 4);

			assertTrue(expected.equals(actual), "Compare debug.log and test/debug.log in Eclipse.");
		}
	}

	/**
	 * Tests first part of file output.
	 */
	@Nested
	public class D_FileTests2 {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// test file output is as expected
			List<String> expected = Files.readAllLines(Path.of("src", "test", "resources", "debug.log"));
			List<String> actual = Files.readAllLines(Path.of("debug.log"));

			// only test a subset here
			expected = expected.subList(4, expected.size());
			actual = actual.subList(4, actual.size());

			assertTrue(expected.equals(actual), "Compare debug.log and test/debug.log in Eclipse.");
		}
	}
}
