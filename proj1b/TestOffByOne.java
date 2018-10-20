import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    @Test
    public void testOffByOne() {
        CharacterComparator cc = new OffByOne();
        assertEquals(cc.equalChars('a', 'b'), true);
        assertEquals(cc.equalChars('b', 'a'), true);
        assertEquals(cc.equalChars('a', 'a'), false);

        CharacterComparator ccc = new OffByN(5);
        assertEquals(ccc.equalChars('a', 'f'), true);
        assertEquals(ccc.equalChars('f', 'a'), true);
        assertEquals(ccc.equalChars('a', 'a'), false);
    }
}
