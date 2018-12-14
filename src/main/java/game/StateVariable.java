package game;

import java.io.Serializable;
import java.util.List;

/**
 * Container for state values for a single body link at a single timestep.
 * <p>
 * These StateVariables are generally stored by {@link State State} to represent the full runner state.
 *
 * @author matt
 */
public class StateVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Horizontal position of the body.
     */
    private final float x;

    /**
     * Vertical position of the body.
     */
    private final float y;

    /**
     * Counterclockwise angle of the body.
     */
    private final float th;

    /**
     * Horizontal velocity of the body.
     */
    private final float dx;

    /**
     * Vertical velocity of the body.
     */
    private final float dy;

    /**
     * Counterclockwise angular rate of the body.
     */
    private final float dth;

    /**
     * Make a new StateVariables holding the configuration and velocity information for a single runner link.
     *
     * @param x   Horizontal position of the body.
     * @param y   Vertical position of the body.
     * @param th  Counterclockwise angle of the body.
     * @param dx  Horizontal velocity of the body.
     * @param dy  Vertical velocity of the body.
     * @param dth Counterclockwise angular rate of the body.
     */
    public StateVariable(float x, float y, float th, float dx, float dy, float dth) {
        this.x = x;
        this.y = y;
        this.th = th;
        this.dx = dx;
        this.dy = dy;
        this.dth = dth;
    }

    /**
     * Make a new StateVariables holding the configuration and velocity information for a single runner link.
     *
     * @param stateVals List containing the 6 state values for a single link. Order should be x, y, th, dx, dy, dth.
     */
    public StateVariable(List<Float> stateVals) {
        if (stateVals.size() != 6)
            throw new RuntimeException("Tried to make a StateVariable with the wrong number of values.");

        x = stateVals.get(0);
        y = stateVals.get(1);
        th = stateVals.get(2);
        dx = stateVals.get(3);
        dy = stateVals.get(4);
        dth = stateVals.get(5);
    }

    /**
     * Get the horizontal position of the body.
     *
     * @return Horizontal position of the body.
     */
    public float getX() {
        return x;
    }

    /**
     * Get the vertical position of the body.
     *
     * @return Vertical position of the body.
     */
    public float getY() {
        return y;
    }

    /**
     * Get the counterclockwise angle of the body.
     *
     * @return Counterclockwise angle of the body.
     */
    public float getTh() {
        return th;
    }

    /**
     * Get the horizontal velocity of the body.
     *
     * @return Horizontal velocity of the body.
     */
    public float getDx() {
        return dx;
    }

    /**
     * Get the vertical velocity of the body.
     *
     * @return Vertical velocity of the body.
     */
    public float getDy() {
        return dy;
    }

    /**
     * Get the counterclockwise angular rate of the body.
     *
     * @return Counterclockwise angular rate of the body.
     */
    public float getDth() {
        return dth;
    }
}
