package actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains the keypresses and durations for a single action. Works like an uneditable {@link java.util.Queue}. Call
 * {@link Action#poll()} to get the keystrokes at each timestep execution. Call {@link Action#hasNext()} to make
 * sure there are timesteps left in this action. Call {@link Action#reset()} to restore the duration of the action back to
 * original.
 * <p>
 * Note that when constructed, actions may not be changed or polled. They serve as an "immutable" backup of the
 * action. To get a pollable version of the {@link Action}, call {@link Action#getCopy()}.
 *
 * @author Matt
 * @see java.util.Queue
 * @see ActionSet
 */
public class Action implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Total number of Box2d timesteps that this key combination should be held.
     **/
    private final int timestepsTotal;

    /**
     * Number of timesteps left to hold this command.
     **/
    private int timestepsRemaining;

    /**
     * Which of the QWOP keys are pressed?
     **/
    private final boolean[] keysPressed;

    /**
     * Is this the immutable original or a derived, mutable copy. A little bit hacky, but a way of avoiding threading
     * issues without major modifications.
     **/
    private boolean isExecutableCopy = false;

    /**
     * Create an action containing the time to hold and the key combination.
     *
     * @param totalTimestepsToHold Number of timesteps to hold the keys associated with this Action.
     * @param keysPressed          4-element boolean array representing whether the Q, W, O, and P keys are pressed.
     *                             True means pressed down. False means not pressed.
     */
    public Action(int totalTimestepsToHold, boolean[] keysPressed) {
        if (keysPressed.length != 4)
            throw new IllegalArgumentException("A QWOP action should have booleans for exactly 4 keys. Tried to " +
                    "create one with a boolean array of size: " + keysPressed.length);
        if (totalTimestepsToHold < 0)
            throw new IllegalArgumentException("New QWOP Action must have non-negative duration. Given: " + totalTimestepsToHold);
        this.timestepsTotal = totalTimestepsToHold;
        this.keysPressed = keysPressed;
        timestepsRemaining = timestepsTotal;
    }

    /**
     * Create an action containing the time to hold and the key combination.
     *
     * @param totalTimestepsToHold Number of timesteps to hold the keys associated with this Action.
     * @param Q                    Whether the Q key is pressed during this action.
     * @param W                    Whether the W key is pressed during this action.
     * @param O                    Whether the O key is pressed during this action.
     * @param P                    Whether the P key is pressed during this action.
     */
    public Action(int totalTimestepsToHold, boolean Q, boolean W, boolean O, boolean P) {
        this(totalTimestepsToHold, new boolean[]{Q, W, O, P}); // Chain to the other constructor.
    }

    /**
     * Return the keys for this action and decrement the timestepsRemaining.
     *
     * @return A 4-element array containing true/false for whether each of the Q, W, O, and P keys are pressed.
     */
    public boolean[] poll() {
        if (!isExecutableCopy)
            throw new RuntimeException("Trying to execute the base version of the Action. Due to multi-threading, " +
                    "this REALLY screws with the counters in the action. Call getCopy for the version you should use.");
        if (timestepsRemaining <= 0)
            throw new IndexOutOfBoundsException("Tried to poll an action which was already completed. Call hasNext() " +
                    "to check before calling poll().");
        timestepsRemaining--;

        return keysPressed;
    }

    /**
     * Return the keys pressed in this action without changing the number of timesteps remaining in this action.
     *
     * @return A 4-element array containing true/false for whether each of the Q, W, O, and P keys are pressed in
     * this action.
     */
    public boolean[] peek() {
        return keysPressed;
    }

    /**
     * Check whether this action is finished (i.e. internal step counter hit zero).
     *
     * @return Whether this action is finished.
     */
    public boolean hasNext() {
        return timestepsRemaining > 0;
    }

    /**
     * Reset the number of timesteps in this action remaining. Do this before repeating an action.
     **/
    public void reset() {
        if (!isExecutableCopy) throw new RuntimeException("Tried to reset the base copy of an action." +
                "This is not inherently wrong, but it will do nothing. Could indicate logic flaws in the caller.");
        timestepsRemaining = timestepsTotal;
    }

    /**
     * Get the number of timesteps left to hold this key combination.
     *
     * @return The number of timesteps remaining in this action.
     */
    public int getTimestepsRemaining() {
        return timestepsRemaining;
    }

    /**
     * Get the total duration of this action in timesteps.
     *
     * @return Total duration of this action (timesteps).
     */
    public int getTimestepsTotal() {
        return timestepsTotal;
    }

    /**
     * Check if this action is equal to another in regards to keypresses and durations. Completely overrides default
     * equals, so when doing ArrayList checks, this will be the only way to judge. Note that the actions do not need
     * to have the same number of timesteps remaining to be judged as equal as long as their timestep totals and keys
     * are the same.
     *
     * @param other An action to check whether it is equivalent to this action.
     * @return Whether the other action is equivalent to this one.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Action)) {
            return false;
        }
        Action otherAction = (Action) other;

        boolean equal = true;

        // Negate equality if any of the QWOP keys don't match.
        for (int i = 0; i < keysPressed.length; i++) {
            if (keysPressed[i] != otherAction.peek()[i]) {
                equal = false;
                break;
            }
        }

        // Negate if we haven't already and they have different durations.
        if (equal && timestepsTotal != otherAction.getTimestepsTotal()) equal = false;

        return equal;
    }

    /**
     * Return a string with the current action keys, total time to hold, and time remaining. This method does not
     * print, it just returns the string for the caller to use.
     *
     * @return String of information about this action.
     */
    @Override
    public String toString() {
        String reportString = " Keys pressed: ";
        reportString += keysPressed[0] ? "Q" : "";
        reportString += keysPressed[1] ? "W" : "";
        reportString += keysPressed[2] ? "O" : "";
        reportString += keysPressed[3] ? "P" : "";

        reportString += "; Timesteps elapsed/total: " + timestepsRemaining + "/" + timestepsTotal;

        return reportString;
    }

    /**
     * Get a copy of this action. This avoid multi-threading issues.
     *
     * @return A poll-able copy of this Action with all timesteps of the duration remaining.
     */
    public synchronized Action getCopy() {
        Action copiedAction = new Action(timestepsTotal, keysPressed);
        copiedAction.isExecutableCopy = true;
        return copiedAction;
    }

    /**
     * Is this a mutable copy of the original action? Important if we plan to use this as a pollable queue. If the
     * action is not mutable, then you must get a copy with {@link Action#getCopy()}.
     *
     * @return Returns whether this action can be polled. If false, then it is the original version of the action.
     */
    public boolean isMutable() {
        return isExecutableCopy;
    }

    /**
     * Take a list of actions and combine adjacent actions which have the same keypresses.
     * These mostly arise when doing control on a timestep-by-timestep basis. Only timestepsTotal are
     * used. Timesteps remaining are not preserved. 0-duration actions are squashed away.
     * An empty array input or one containing nothing but 0 length actions will produce an exception.
     *
     * @param inActions A list of actions which we wish to consolidate.
     * @return A new list of actions which is the consolidated version of the input action list.
     * @throws IllegalArgumentException When trying to consolidate a list of actions containing nothing but 0-length
     *                                  actions.
     */
    public static List<Action> consolidateActions(List<Action> inActions) {
        List<Action> outActions = new ArrayList<>(); //TODO: keeps making lists even when recursing. May just want to
        // modify in place.

        // Single element input.
        if (inActions.size() == 1) {
            if (inActions.get(0).getTimestepsTotal() == 0) { // Single element input + 0-duration -> exception.
                throw new IllegalArgumentException("Input action list had only one element, and this one element had " +
                        "0 duration.");
            }
            outActions.add(inActions.get(0));
            return outActions;
        }

        int consolidations = 0;
        // Combine adjacent same button actions.
        for (int i = 0; i < inActions.size() - 1; ) {
            Action a1 = inActions.get(i);
            Action a2 = inActions.get(i + 1);

            if (a1.getTimestepsTotal() == 0) {
                // Eliminate 0-duration actions.
                i++;
                if (inActions.size() - 1 == i && a2.getTimestepsTotal() != 0) outActions.add(a2);
            } else if (Arrays.equals(a1.peek(), a2.peek())) {
                outActions.add(new Action(a1.getTimestepsTotal() + a2.getTimestepsTotal(), a1.peek()));
                consolidations++;
                i += 2;
            } else {
                outActions.add(a1);
                i++;
                if (inActions.size() - 1 == i && a2.getTimestepsTotal() != 0) outActions.add(a2);
            }
        }

        // Recurse until no more combinations are made.
        if (consolidations == 0) {
            if (outActions.isEmpty()) {
                throw new IllegalArgumentException("Tried to consolidate a multi-element list of Actions. All had " +
                        "0-duration, so consolidation does not make sense.");
            }
            return outActions;
        } else {
            return consolidateActions(outActions);
        }
    }
}
