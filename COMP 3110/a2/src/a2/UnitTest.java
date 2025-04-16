package a2;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/* 	
 	Name: Keigo Katanaga
	ID: 110068805
*/

public class UnitTest {
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void testJoin() {
		assertEquals("", StringUtils.join(Arrays.asList(), "||", 0, 0));
		
		assertEquals("", StringUtils.join(Arrays.asList("12", "34", "56", "78"), "|", 2, -1));
		
		// testJoin A: uncomment this and comment out testJoin B if we want exceptions to be thrown and not caught
//		assertEquals(StringUtils.join(Arrays.asList("12", "34", "56", "78"), "|", -2, 2), "56|78|12|34");
		
		// testJoin B: uncomment this block (lines 35-39) and comment out testJoin A if we want exceptions to be caught
		IndexOutOfBoundsException thrown;
		thrown = assertThrows(
				IndexOutOfBoundsException.class,
				() -> StringUtils.join(Arrays.asList("12", "34", "56", "78"), "|", -2, 2));
		assertEquals("fromIndex = -2", thrown.getMessage());
		
		assertEquals("78||||\t", StringUtils.join(Arrays.asList("12", "34", "56", "78", "", "\t", "10"), "||", 3, 6));
	}
	
	@Test
	public void testLastIndexOf() {
		assertEquals(-1, StringUtils.lastIndexOf("   ", 0));
		assertEquals(4, StringUtils.lastIndexOf("ababa", 'a'));
		assertEquals(-1, StringUtils.lastIndexOf("ababa", 'z'));
		assertEquals(3, StringUtils.lastIndexOf("aba\nba", '\n'));
	}
	
	@Test
	public void testLeftPad() {
		assertEquals("", StringUtils.leftPad("", -5, '0'));
		assertEquals("12.00", StringUtils.leftPad("12.00", 2, ' '));
		assertEquals("0012.00", StringUtils.leftPad("12.00", 7, '0'));
		assertEquals("\n\n\tX\n", StringUtils.leftPad("\tX\n", 5, '\n'));
	}
	
	@Test
	public void testRepeat() {
		assertEquals("", StringUtils.repeat("", 3));
		assertEquals("", StringUtils.repeat("ab", 0));
		assertEquals("ababab", StringUtils.repeat("ab", 3));
		assertEquals("\n\n\n", StringUtils.repeat("\n", 3));
	}
	
	@Test
	public void testReverse() {
		assertEquals("", StringUtils.reverse(""));
		assertEquals("zyx", StringUtils.reverse("xyz"));
		assertEquals("xYX  ", StringUtils.reverse("  XYx"));
		assertEquals("t\n!", StringUtils.reverse("!\nt"));
	}
	
	@Test
	public void testSplit() {
		assertArrayEquals(new String[]{}, StringUtils.split("", ','));
		assertArrayEquals(new String[]{"abcd"}, StringUtils.split("abcd", ' '));
		assertArrayEquals(new String[]{"a", "b", "c", "d"}, StringUtils.split("a!!!b!c!!d!!", '!'));
		assertArrayEquals(new String[]{"a", "tb", "c", "td", "\n"}, StringUtils.split("a\ttb\tc\ttd\t\n", '\t'));
	}
	
	@Test
	public void testStartsWith() {
		assertFalse(StringUtils.startsWith("", "abc"));
		assertTrue(StringUtils.startsWith("abc", ""));
		assertTrue(StringUtils.startsWith(" ", " "));
		assertFalse(StringUtils.startsWith("\tabc", " "));
	}
	
	@Test
	public void testSubstring() {
		assertEquals("", StringUtils.substring(" ", 100));
		assertEquals("45", StringUtils.substring("12345", -2));
		assertEquals("123", StringUtils.substring("123", -100));
		assertEquals("\t345", StringUtils.substring("12\t345", 2));
	}
	
	@Test
	public void testTrim() {
		assertEquals("", StringUtils.trim("   "));
		assertEquals("a1b2", StringUtils.trim("a1b2"));
		assertEquals("a1 b2", StringUtils.trim("   a1 b2  "));
		assertEquals("a1 \n b2", StringUtils.trim("\n \t a1 \n b2 \t \n"));
	}
	
	@Test
	public void testUpperCase() {
		assertEquals(" ", StringUtils.upperCase(" "));
		assertEquals("1ABC", StringUtils.upperCase("1abc"));
		assertEquals("XYZ123", StringUtils.upperCase("XYZ123"));
		assertEquals("\nAB\tCD\n", StringUtils.upperCase("\nab\tcd\n"));
	}

	public static void main(String[] args) {
	}

}
