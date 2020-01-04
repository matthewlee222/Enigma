package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Matthew Jihoon Lee
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        String result;
        String msg;
        boolean begin = true;

        while (_input.hasNextLine()) {
            String nextInput = _input.nextLine().trim();
            if (begin) {
                begin = false;
                if (nextInput.charAt(0) != '*') {
                    throw error("setting is incorrect");
                }
            }
            if (nextInput.startsWith("*")) {
                setUp(enigma, nextInput);
            } else {
                msg = nextInput.toUpperCase();
                try {
                    result = enigma.convert(msg);
                    printMessageLine(result);
                } catch (EnigmaException excp) {
                    throw new EnigmaException("Message could"
                            + "not be converted!");
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.nextLine();
            _alphabet = new Alphabet(alphabet);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Format of config wrong");
            }
            int numberofRotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Format of config wrong");
            }
            int pawls = _config.nextInt();
            next = (_config.next()).toUpperCase();
            while (_config.hasNext()) {
                currentRotorName = next;
                currentRotorNotches = (_config.next()).toUpperCase();
                Rotor r = readRotor();
                _allRotors.add(r);
            }
            return new Machine(_alphabet, numberofRotors, pawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            perm = "";
            next = (_config.next()).toUpperCase();
            while (next.contains("(") && _config.hasNext()) {
                if (!next.contains(")")) {
                    throw new EnigmaException("This format is wrong!");
                }

                perm = perm.concat(next + " ");
                next = (_config.next().toUpperCase());
            }
            if (!_config.hasNext()) {
                perm = perm.concat(next + " ");
            }
            if (currentRotorNotches.charAt(0) == 'M') {
                return new MovingRotor(currentRotorName,
                        new Permutation(perm, _alphabet),
                        currentRotorNotches.substring(1));
            } else if (currentRotorNotches.charAt(0) == 'N') {
                return new FixedRotor(currentRotorName,
                        new Permutation(perm, _alphabet));
            } else {
                return new Reflector(currentRotorName,
                        new Permutation(perm, _alphabet));
            }
        } catch (NoSuchElementException exception) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] set = settings.split(" ");
        if (set.length - 1 < M.numRotors()) {
            throw new EnigmaException("Not enough arguments here");
        }

        String[] rotors = new String[M.numRotors()];
        for (int i = 1; i < M.numRotors() + 1; i++) {
            rotors[i - 1] = set[i];
        }
        for (int i = 0; i < rotors.length - 1; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("Rotor cannot be used again!");
                }
            }
        }
        String strng = "";
        for (int i = 7; i < set.length; i++) {
            strng = strng.concat(set[i] + " ");
        }
        M.insertRotors(rotors);
        if (!M.retrieveRotors()[0].reflecting()) {
            throw new EnigmaException("First rotor should reflect");
        }
        try {
            M.setRotors(set[M.numRotors() + 1]);
        } catch (IndexOutOfBoundsException excp) {
            throw new EnigmaException("Rotor name is wrong");
        }
        M.setPlugboard(new Permutation(strng, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.trim().replaceAll("\\s+", "");
        String result = "";
        int i = 0;
        while (i < msg.length()) {
            if (i % 5 == 0) {
                result += " ";
            }
            result = result + msg.substring(i, i + 1);
            i = i + 1;
        }
        _output.println(result.trim());
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** A perm in the format STRING. */
    private String perm;

    /** A STRING that represents the current
     * Rotor that the code is looking at. */
    private String currentRotorName;

    /** A STRING that marks the next value in the iteration. */
    private String next;

    /** A STRING that represents the notches in the current rotor. */
    private String currentRotorNotches;

    /** An arrayList of Rotors that contains all the Rotors in this system. */
    private ArrayList<Rotor> _allRotors = new ArrayList<>();

}
