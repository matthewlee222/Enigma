package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Matthew Jihoon Lee
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPermute() {
        Alphabet alp = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        perm = new Permutation("(AELTPHQXRU) "
                + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alp);

        assertEquals("Error!", alp.toInt('E'), perm.permute(alp.toInt('A')));
        assertEquals("Error!", alp.toInt('A'), perm.permute(alp.toInt('U')));
        assertEquals("Error!", alp.toInt('S'), perm.permute(alp.toInt('S')));
        assertEquals("Error!", alp.toInt('Z'), perm.permute(alp.toInt('J')));
        assertEquals("Error!", alp.toInt('J'), perm.permute(alp.toInt('Z')));

        assertEquals("Error!", 4, perm.permute(0));
        assertEquals("Error!", 18, perm.permute(18));
        assertEquals("Error!", 9, perm.permute(25));


        assertEquals("Error!", 'E', perm.permute('A'));
        assertEquals("Error!", 'Z', perm.permute('J'));
        assertEquals("Error!", 'S', perm.permute('S'));


    }

    @Test
    public void testInvert() {
        Alphabet alp = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        perm = new Permutation("(AELTPHQXRU) "
                + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alp);

        assertEquals("Error!", 0, perm.invert(4));
        assertEquals("Error!", 20, perm.invert(0));
        assertEquals("Error!", 18, perm.invert(18));
        assertEquals("Error!", 9, perm.invert(25));
        assertEquals("Error!", 25, perm.invert(9));

        assertEquals("Error!", 'U', perm.invert('A'));
        assertEquals("Error!", 'A', perm.invert('E'));
        assertEquals("Error!", 'Z', perm.invert('J'));
        assertEquals("Error!", 'J', perm.invert('Z'));
        assertEquals("Error!", 'S', perm.invert('S'));

    }

    @Test
    public void testDerangement() {
        Alphabet alp = new Alphabet("ABCDEFGHIJKLM"
                + "NOPQRSTUVWXYZ");
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", alp);

        assertEquals("Error!", false, perm.derangement());
    }
}
