package game;

class GameConstants {

    /**
     * Physics engine stepping parameters.
     */
    static final float timestep = 0.04f;

    /**
     * Number of Box2D solver iterations.
     */
    static final int physIterations = 5;

    /**
     * AABB bounds.
     */
    static final float aabbMinX = -100f, aabbMinY = -30f, aabbMaxX = 5000f, aabbMaxY = 80f;

    /**
     * Gravity in downward direction.
     */
    static final float gravityMagnitude = 10f;

    /**
     * Track parameters.
     */
    static final float trackPosX = -30f, trackPosY = 8.90813f + 20f, trackFric = 1f, trackRest = 0.2f,
    trackXDim = 1000f, trackYDim = 20f;

    /**
     * Foot parameters.
     */
    static final float rFootPosX = -0.96750f, rFootPosY = 7.77200f, lFootPosX = 3.763f, lFootPosY = 8.101f;
    static final float rFootAng = 0.7498f, rFootMass = 11.630f, rFootInertia = 9.017f, rFootL = 2.68750f,
            rFootH = 1.44249f, rFootFric = 1.5f, rFootDensity = 3f, lFootAng = 0.1429f, lFootMass = 10.895f,
            lFootInertia = 8.242f, lFootL = 2.695f, lFootH = 1.34750f, lFootFric = 1.5f, lFootDensity = 3f;

    /**
     * Shank parameters.
     */
    static final float rCalfPosX = 0.0850f, rCalfPosY = 5.381f, lCalfPosX = 2.986f, lCalfPosY = 5.523f;
    static final float rCalfAng = -0.821f, lCalfAng = -1.582f, rCalfAngAdj = 1.606188724f, lCalfAngAdj =
            1.607108307f, rCalfMass = 7.407f, lCalfMass = 7.464f, rCalfInertia = 16.644f, lCalfInertia = 16.893f;
    // Length and width for the calves are just for collisions with the ground, so not very important.
    static final float rCalfL = 4.21f, lCalfL = 4.43f, rCalfW = 0.4f, lCalfW = 0.4f, rCalfFric = 0.2f,
            lCalfFric = 0.2f, rCalfDensity = 1f, lCalfDensity = 1f;

    /**
     * Thigh parameters.
     */
    static final float rThighPosX = 1.659f, rThighPosY = 1.999f, lThighPosX = 2.52f, lThighPosY = 1.615f,
            rThighAng = 1.468f, lThighAng = -1.977f, rThighAngAdj = -1.544382589f, lThighAngAdj = 1.619256373f,
            rThighMass = 10.54f, lThighMass = 10.037f, rThighInertia = 28.067f, lThighInertia = 24.546f;
    // Length and width for the calves are just for collisions with the ground, so not very important.
    static final float rThighL = 4.19f, lThighL = 3.56f, rThighW = 0.6f, lThighW = 0.6f, rThighFric = 0.2f,
            lThighFric = 0.2f, rThighDensity = 1f, lThighDensity = 1f;

    /**
     * Torso parameters.
     */
    static final float torsoPosX = 2.525f, torsoPosY = -1.926f, torsoAng = -1.251f, torsoAngAdj =
            1.651902129f, torsoMass = 18.668f, torsoInertia = 79.376f;
    static final float torsoL = 5f, torsoW = 1.5f, torsoFric = 0.2f, torsoDensity = 1f;

    /**
     * Head parameters.
     */
    static final float headPosX = 3.896f, headPosY = -5.679f,
            headAng = 0.058f, headAngAdj = 0.201921414f,
            headMass = 5.674f, headInertia = 5.483f;
    // Radius is just for collision shape
    static final float headR = 1.1f, headFric = 0.2f, headDensity = 1f;

    /**
     * Upper arm parameters.
     */
    static final float rUArmPosX = 1.165f, rUArmPosY = -3.616f, lUArmPosX = 4.475f, lUArmPosY = -2.911f,
            rUArmAng = -0.466f, lUArmAng = 0.843f, rUArmAngAdj = 1.571196588f, lUArmAngAdj = -1.690706418f,
            rUArmMass = 5.837f, lUArmMass = 4.6065f, rUArmInertia = 8.479f, lUArmInertia = 5.85f;
    // Dimensions for collision shapes
    static final float rUArmL = 2.58f, lUArmL = 2.68f, rUArmW = 0.2f, lUArmW = 0.15f,
            rUArmFric = 0.2f, lUArmFric = 0.2f, rUArmDensity = 1f, lUArmDensity = 1f;

    /**
     * Lower arm parameters.
     */
    static final float rLArmPosX = 0.3662f, rLArmPosY = -1.248f, lLArmPosX = 5.899f, lLArmPosY = -3.06f,
            rLArmAng = -1.762f, lLArmAng = -1.251f, rLArmAngAdj = 1.521319096f, lLArmAngAdj = 1.447045854f,
            rLArmMass = 5.99f, lLArmMass = 3.8445f, rLArmInertia = 10.768f, lLArmInertia = 4.301f;
    // For collision shapes
    static final float rLArmL = 3.56f, lLArmL = 2.54f, rLArmW = 0.15f, lLArmW = 0.12f,
            rLArmFric = 0.2f, lLArmFric = 0.2f, rLArmDensity = 1f, lLArmDensity = 1f;

    /**
     * Joint speed setpoints.
     */
    static final float rAnkleSpeed1 = 2f, rAnkleSpeed2 = -2f, lAnkleSpeed1 = -2f, lAnkleSpeed2 = 2f,
            rKneeSpeed1 = -2.5f, rKneeSpeed2 = 2.5f, lKneeSpeed1 = 2.5f, lKneeSpeed2 = -2.5f,
            rHipSpeed1 = -2.5f, rHipSpeed2 = 2.5f, lHipSpeed1 = 2.5f, lHipSpeed2 = -2.5f,
            rShoulderSpeed1 = 2f, rShoulderSpeed2 = -2f, lShoulderSpeed1 = -2f, lShoulderSpeed2 = 2f;

    /**
     * Joint limits.
     */
    static final float oRHipLimLo = -1.3f, oRHipLimHi = 0.7f, oLHipLimLo = -1f, oLHipLimHi = 1f, //O Hip
    // limits (changed to this when o is pressed):
    pRHipLimLo = -0.8f, pRHipLimHi = 1.2f, pLHipLimLo = -1.5f, pLHipLimHi = 0.5f; //P Hip limits

    /**
     * Springs and things.
     */
    static final float neckStiff = 15f, neckDamp = 5f, rElbowStiff = 1f, lElbowStiff = 1f, rElbowDamp = 0f,
            lElbowDamp = 0f;

    /**
     * Initial conditions.
     */
    static final float rAnklePosX = -0.96750f, rAnklePosY = 7.77200f, lAnklePosX = 3.763f, lAnklePosY = 8.101f,
            rKneePosX = 1.58f, rKneePosY = 4.11375f, lKneePosX = 3.26250f, lKneePosY = 3.51625f,
            rHipPosX = 1.260f, rHipPosY = -0.06750f, lHipPosX = 2.01625f, lHipPosY = 0.18125f,
            rShoulderPosX = 2.24375f, rShoulderPosY = -4.14250f, lShoulderPosX = 3.63875f, lShoulderPosY = -3.58875f,
            rElbowPosX = -0.06f, rElbowPosY = -2.985f, lElbowPosX = 5.65125f, lElbowPosY = -1.8125f,
            neckPosX = 3.60400f, neckPosY = -4.581f;

    /**
     * Angle failure limits. Fail if torso angle is too big or small to rule out stupid hopping that eventually falls.
     */
    static final float torsoAngUpper = 1.57f, torsoAngLower = -2.2f; // Negative is falling backwards. 0.4 is start angle.

}
