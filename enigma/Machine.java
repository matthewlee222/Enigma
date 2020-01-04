package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Matthew Jihoon Lee
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = new Permutation("", _alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** @return A method that allows other classes to retrieve rotors
     * while keeping _rotors private. */
    Rotor[] retrieveRotors() {
        return _rotors;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotors = new Rotor[numRotors()];
        HashMap<String, Rotor> hmap = new HashMap<>();
        for (Rotor r: _allRotors) {
            hmap.put(r.name().toUpperCase(), r);
        }
        for (int i = 0; i < _rotors.length; i++) {
            try {
                _rotors[i] = hmap.get(rotors[i].toUpperCase());
            } catch (EnigmaException e) {
                throw new EnigmaException("Wrong name");
            }
        }
        if (_rotors.length != rotors.length) {
            throw EnigmaException.error("The rotors are not named correctly!");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != (numRotors() - 1)) {
            throw new EnigmaException("Length of setting String"
                    + "is the wrong length!");
        }
        for (int i = 1; i < _rotors.length; i++) {
            if (!_rotors[i].reflecting()) {
                _rotors[i].set(setting.charAt(i - 1));
            } else {
                throw new EnigmaException("The reflector is not"
                        + "where it should be");
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {

        int rotorsthatmove = 0;
        for (int i = 0; i < _rotors.length; i++) {
            if (_rotors[i].rotates()) {
                rotorsthatmove += 1;
            }
        }
        if (rotorsthatmove != numPawls()) {
            throw new EnigmaException("Amount of moving rotors wrong!");
        }

        ArrayList<Rotor> advance = new ArrayList<>();
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        Rotor last = _rotors[_rotors.length - 1];
        advance.add(last);
        for (int i = 0; i < _rotors.length - 1; i++) {
            Rotor curr = _rotors[i];
            Rotor next = _rotors[i + 1];
            if (next.atNotch() && curr.rotates() && next.rotates()) {
                if (!advance.contains(_rotors[i])) {
                    advance.add(_rotors[i]);
                }
                if (!advance.contains(_rotors[i + 1])) {
                    advance.add(_rotors[i + 1]);
                }
            }
        }

        for (Rotor r: advance) {
            r.advance();
        }
        for (int i = _rotors.length - 1; i >= 0; i--) {
            c = _rotors[i].convertForward(c);
        }
        for (int i = 1; i < _rotors.length; i++) {
            c = _rotors[i].convertBackward(c);
        }
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int rotorsthatmove = 0;
        for (int i = 0; i < _rotors.length; i++) {
            if (_rotors[i].rotates()) {
                rotorsthatmove += 1;
            }
        }
        if (rotorsthatmove != numPawls()) {
            throw new EnigmaException("Amount of moving rotors wrong!");
        }
        String answer = "";
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            try {
                int in = _alphabet.toInt(msg.charAt(i));
                char out = _alphabet.toChar(convert(in));
                answer += out;
            } catch (EnigmaException excp) {
                answer += msg.charAt(i);
            }
        }
        return answer;
    }



    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotors in a specific machine. */
    private final int _numRotors;

    /** The number of pawls in a specific machine. */
    private final int _pawls;

    /** An iterable List of all the rotors in a machine. */
    private Collection<Rotor> _allRotors;

    /** An additional permutation in the plugboard. */
    private Permutation _plugboard;

    /** The rotors inserted in a specific instance of a machine. */
    private Rotor[] _rotors;


}
