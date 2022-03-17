import io.geekya.kasumi.List;
import org.junit.jupiter.api.Test;

import static io.geekya.kasumi.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListTest {
    static final List<Integer> l1 = of(1, 2, 3);
    static final List<Integer> l2 = of(4, 5, 6);
    static final List<Integer> l3 = of(1, 2, 3, 4, 5, 6);

    @Test
    void testConcat() {
        assertEquals(l3, concat(l1, l2));
        assertEquals(l1, concat(l1, empty()));
        assertEquals(l2, concat(empty(), l2));
    }

    @Test
    void testHead() {
        assertEquals(1, head(l1));
        assertEquals(4, head(l2));
        assertThrows(IllegalArgumentException.class, () -> head(empty()));
    }

    @Test
    void testTail() {
        assertEquals(of(2, 3), tail(l1));
        assertEquals(of(5, 6), tail(l2));
        assertThrows(IllegalArgumentException.class, () -> tail(empty()));
    }

    @Test
    void testMap() {
        assertEquals(of(2, 4, 6), map(x -> x * 2, l1));
        assertEquals(of('A', 'B', 'C'), map(Character::toUpperCase, of('a', 'b', 'c')));
    }

    @Test
    void testFlatMap() {
        assertEquals(of(1, 2, 2, 3, 3, 4), flatMap(x -> of(x, x + 1), l1));
        assertEquals(of('a', 'A', 'b', 'B'), flatMap(c -> of(c, Character.toUpperCase(c)), of('a', 'b')));
    }

    @Test
    void testFilter() {
        assertEquals(of(2, 4, 6), filter(x -> x % 2 == 0, l3));
        assertEquals(of('a', 'c'), filter(Character::isLowerCase, of('a', 'B', 'c', 'A')));
    }

    @Test
    void testFoldl() {
        assertEquals(6, foldl(a -> b -> a + b, 0, l1));
        assertEquals(-6, foldl(a -> b -> a - b, 0, l1));
        assertEquals(10, foldl(a -> b -> a + b, 10, empty()));
    }

    @Test
    void testFoldl1() {
        assertEquals(6, foldl1(a -> b -> a + b, l1));
    }

    @Test
    void testFoldr() {
        assertEquals(6, foldr(a -> b -> a + b, 0, l1));
        assertEquals(2, foldr(a -> b -> a - b, 0, l1));
        assertEquals(10, foldr(a -> b -> a + b, 10, empty()));
    }

    @Test
    void testFoldr1() {
        assertEquals(6, foldr1(a -> b -> a + b, l1));
        assertEquals(0, foldr1(a -> b -> a - b, l1));
    }
}
