package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Matthew Jihoon Lee
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _listOfNotches = notches;
    }

    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }

    @Override
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        for (int i = 0; i < _listOfNotches.length(); i++) {
            if (alphabet().toInt(_listOfNotches.charAt(i)) == this.setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
        super.set(super.permutation().wrap(super.setting() + 1));
    }


    /** The location(s) of a rotor's notches in the format STRING. */
    private String _listOfNotches;
}
