import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testisPalindrome() {
        assertEquals(palindrome.isPalindrome("persiflage"), false);
        assertEquals(palindrome.isPalindrome("bt"), false);
        assertEquals(palindrome.isPalindrome("bat"), false);
        assertEquals(palindrome.isPalindrome("noon"), true);
       assertEquals(palindrome.isPalindrome("nabcn"), false);
        assertEquals(palindrome.isPalindrome("nabcban"), true);
        assertEquals(palindrome.isPalindrome("n"), true);
        assertEquals(palindrome.isPalindrome(""), true);
        assertEquals(palindrome.isPalindrome("nn"), true);

    }
}
