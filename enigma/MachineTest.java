package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Matthew Jihoon Lee
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Machine machine;
    private String alpha = UPPER_STRING;
    private Collection<Rotor> allRotors = new HashSet<>();

    /** Check that the machine maps each character of
     *  FROMALPHA to the corresponding character of TOALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkMachine(String testId,
                              String fromAlpha, String toAlpha) {
        int lengthOf = fromAlpha.length();
        for (int i = 0; i < lengthOf; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "incorrect", ci, c),
                    ei, machine.convert(ci));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkMachineConvertChar() {
        allRotors.add(new MovingRotor("B",
                new Permutation(NAVALA.get("B"), UPPER), ""));
        allRotors.add(new MovingRotor("Beta",
                new Permutation(NAVALA.get("Beta"), UPPER), ""));
        allRotors.add(new MovingRotor("III",
                new Permutation(NAVALA.get("III"), UPPER), "V"));
        allRotors.add(new MovingRotor("IV",
                new Permutation(NAVALA.get("IV"), UPPER), "J"));
        allRotors.add(new MovingRotor("I",
                new Permutation(NAVALA.get("I"), UPPER), "Q"));
        machine = new Machine(UPPER, 5, 5, allRotors);
        String [] rotors = {"B", "BETA", "III", "IV", "I"};
        machine.setPlugboard(new Permutation("(YF) (ZH)", UPPER));
        machine.insertRotors(rotors);
        machine.setRotors("AXLE");
    }

    @Test
    public void checkMachineConvertMsg() {
        allRotors.add(new MovingRotor("B",
                new Permutation(NAVALA.get("B"), UPPER), ""));
        allRotors.add(new MovingRotor("Beta",
                new Permutation(NAVALA.get("Beta"), UPPER), ""));
        allRotors.add(new MovingRotor("III",
                new Permutation(NAVALA.get("III"), UPPER), "V"));
        allRotors.add(new MovingRotor("IV",
                new Permutation(NAVALA.get("IV"), UPPER), "J"));
        allRotors.add(new MovingRotor("I",
                new Permutation(NAVALA.get("I"), UPPER), "Q"));
        machine = new Machine(UPPER, 5, 5, allRotors);
        String[] rotors = {"B", "BETA", "III", "IV", "I"};
        machine.insertRotors(rotors);
        machine.setRotors("AXLE");
        machine.setPlugboard(
                new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER));
        String msg = "FROM HIS shoulder Hiawatha";
        String expected =
                "QVPQ SOK OILPUBKJ ZPISFXDW";
        assertEquals(machine.convert(msg), expected);
    }


    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String thisSetting = "";
        for (Rotor r : machineRotors) {
            thisSetting += alph.toChar(r.setting());
        }
        return thisSetting;
    }
    @Test
    public void testDoubleStep() {
        Alphabet alph = new Alphabet("ABC");
        Rotor first = new Reflector("1",
                new Permutation("(ABC)", alph));
        Rotor second = new MovingRotor("2",
                new Permutation("(ABC)", alph), "C");
        Rotor third = new MovingRotor("3",
                new Permutation("(ABC)", alph), "C");
        Rotor fourth = new MovingRotor("4",
                new Permutation("(ABC)", alph), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {first, second, third, fourth};
        String[] rotors = {"1", "2", "3", "4"};
        Machine myMachine = new Machine(alph, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        myMachine.insertRotors(rotors);
        myMachine.setRotors(setting);
        assertEquals("AAAA", getSetting(alph, machineRotors));
        myMachine.convert('A');
        assertEquals("AAAB", getSetting(alph, machineRotors));
        myMachine.convert('A');
        assertEquals("AAAC", getSetting(alph, machineRotors));
        myMachine.convert('A');
        assertEquals("AABA", getSetting(alph, machineRotors));
    }




}
