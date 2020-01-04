package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Matthew Jihoon Lee
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        characterList = new char[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            characterList[i] = _chars.charAt(i);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return _chars.contains(String.valueOf(ch));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw EnigmaException.error("Index is out of bound!");
        } else {
            return characterList[index];
        }
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        boolean checkifin = false;
        int index = 0;
        for (int i = 0; i < characterList.length; i++) {
            if (characterList[i] == ch) {
                checkifin = true;
                index = i;
            }
        }
        if (!checkifin) {
            throw EnigmaException.error("can't do that!");
        }
        return index;
    }


    /** A STRING of all the characters in an alphabet. */
    private String _chars;

    /** A characterList containing all the characters in an alphabet. */
    private char[] characterList;

}
