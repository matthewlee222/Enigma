# Enigma

This project recreates the cipher that was created in order to crack the enigma machine used by the Nazi regime in World War II to communicate secret messages to one another.

This program takes descriptions of possible initial configurations of the machine and messages to encode or decode (the Enigma algorithms were reciprocal, meaning that encryption is its own inverse operation.)

The Enigmas effect a substitution cipher on the letters of a message. That is, at any given time, the machine performs a permutation—a one-to-one mapping—of the alphabet onto itself. The alphabet consists solely of the 26 letters in one case.

Plain substitution ciphers are easy to break (you've probably seen puzzles in newspapers that consist of breaking such ciphers). The Enigma, however, implements a progressive substitution, different for each subsequent letter of the message. This made decryption considerably more difficult.

The device consists of a simple mechanical system of interchangeable rotors that sit side-by-side on a shaft and make electrical contact with each other. Most of these rotors have 26 contacts on both sides, which are wired together internally so as to effect a permutation of signals coming in from one side onto the contacts on the other (and the inverse permutation when going in the reverse direction). To the left of the rotors, one could select one of a set of reflectors (Umkehrwalzen), with contacts on their right sides only, and wired to connect half of those contacts to the other half. A signal starting from the rightmost rotor enters through one of the 26 possible contacts, flows through wires in the rotors, "bounces" off the reflector, and then comes back through the same rotors (in reverse) by a different route, always ending up being permuted to a letter position different from where it started; that is, the permutation was always a derangement.
