package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Matthew Jihoon Lee
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _cycles = cycles;
        _alphabet = alphabet;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char character = _alphabet.toChar(wrap(p));
        String[] newcycles = _cycles.split(" ");
        for (int i = 0; i <= newcycles.length - 1; i++) {
            if (newcycles[i].length() == 3
                    && character == newcycles[i].charAt(1)) {
                return _alphabet.toInt(newcycles[i].charAt(1));
            }
            for (int j = 1; j <= newcycles[i].length() - 2; j++) {
                if (character == newcycles[i].charAt(j)) {
                    if (j == newcycles[i].length() - 2) {
                        return _alphabet.toInt(newcycles[i].charAt(1));
                    } else {
                        return _alphabet.toInt(newcycles[i].charAt(j + 1));
                    }
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        char character = _alphabet.toChar(wrap(c));
        String[] newcycles = _cycles.split(" ");
        for (int i = 0; i <= newcycles.length - 1; i++) {
            if (newcycles[i].length() == 3
                    && character == newcycles[i].charAt(1)) {
                return _alphabet.toInt(newcycles[i].charAt(1));
            }
            for (int j = newcycles[i].length() - 2; j >= 1; j--) {
                if (character == newcycles[i].charAt(j)) {
                    if (j == 1) {
                        return _alphabet.toInt(newcycles[i].
                                charAt(newcycles[i].length() - 2));
                    } else {
                        return _alphabet.toInt(newcycles[i].charAt(j - 1));
                    }
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        String[] newcycles = _cycles.split(" ");
        for (int i = 0; i <= newcycles.length - 1; i++) {
            if (newcycles[i].length() == 3 && p == newcycles[i].charAt(1)) {
                return newcycles[i].charAt(1);
            }
            for (int j = 1; j <= newcycles[i].length() - 2; j++) {
                if (p == newcycles[i].charAt(j)) {
                    if (j == newcycles[i].length() - 2) {
                        return newcycles[i].charAt(1);
                    } else {
                        return newcycles[i].charAt(j + 1);
                    }
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        String[] newcycles = _cycles.split(" ");
        for (int i = 0; i <= newcycles.length - 1; i++) {
            if (newcycles[i].length() == 3 && c == newcycles[i].charAt(1)) {
                return newcycles[i].charAt(1);
            }
            for (int j = newcycles[i].length() - 2; j >= 1; j--) {
                if (c == newcycles[i].charAt(j)) {
                    if (j == 1) {
                        return newcycles[i].charAt(newcycles[i].length() - 2);
                    } else {
                        return newcycles[i].charAt(j - 1);
                    }
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        String[] listofcycles = _cycles.split(" ");
        for (int i = 0; i < listofcycles.length; i++) {
            if (listofcycles[i].length() == 3) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** a string in the form "(cccc) (cc) ..." where the
     * c's are characters in ALPHABET. */
    private String _cycles;
}
