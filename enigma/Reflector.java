package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Matthew Jihoon Lee
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("This permutation maps to itself!");
        }
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return true;
    }

    @Override
    int convertBackward(int e) {
        throw error("Remember that the "
                + "rotor reflector cannot convert backwards!");
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

}
