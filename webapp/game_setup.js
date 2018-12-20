// Loads up the transcompiled Java QWOP implementation (in bundle.js) and sets up GUI.

var frameRate  = 1/25;
var frameDelay = frameRate * 1000;

var canvas;
var ctx;
var width;
var height;
var qwopGame = new game.GameSingleThread();
var qwopInitialState = game.GameSingleThread.getInitialState();
var yGravitySlider = document.getElementById('yGravitySlider');
var maxTorqueMultSlider = document.getElementById('maxTorqueMultSlider');
var torsInertiaMultSlider = document.getElementById('torsInertiaMultSlider');
var pointFeetCheckbox = document.getElementById('usePointFeet');

var torsoAngleStabilization = false;
var torsoAngleStabilizerCheckbox = document.getElementById('useTorsoAngleStabilizer');
var torsoAngleStabilizerGainSlider = document.getElementById('torsoStabilizerGainSlider');
var torsoAngleStabilizerK = 0;
var torsoAngleStabilizerC = 0;

var torsoVerticalStabilization = false;
var torsoVerticalStabilizerCheckbox = document.getElementById('useTorsoVerticalStabilizer');
var torsoVerticalStabilizerGainSlider = document.getElementById('torsoVerticalStabilizerSlider');
var torsoVerticalStabilizerK = 0;
var torsoVerticalStabilizerC = 0;

var actionQueue = new actions.ActionQueue();
var sequenceTextbox = document.getElementById('sequenceTextbox');
var sequenceGoButton = document.getElementById('sequenceGoButton');

var scaling = 17;
var xOffset = 100;
var yOffset = 300;
var q = false;
var w = false;
var o = false;
var p = false;


var loop = function() {

    // Step the physics forward in time.
    if (!actionQueue.isEmpty()) {
        var command = actionQueue.pollCommand();
        q = command[0];
        w = command[1];
        o = command[2];
        p = command[3];
    }

    // PD controller on torso angle using hand of god.
    if (torsoAngleStabilization) {
        var currState = qwopGame.getCurrentState();
        qwopGame.applyBodyTorque(torsoAngleStabilizerK*(qwopInitialState.body.getTh() - currState.body.getTh() - 0.2)
         - torsoAngleStabilizerC * currState.body.getDth());
    }

    // PD controller on torso vertical position using hand of god.
    if (torsoVerticalStabilization) {
        var currState = qwopGame.getCurrentState();
        qwopGame.applyBodyImpulse(0, torsoVerticalStabilizerK * (qwopInitialState.body.getY() - currState.body.getY())
        - torsoVerticalStabilizerC * currState.body.getDy());
    }

    qwopGame.stepGame(q, w, o, p);

    // Get the body shape vertices from the game.
    var bodyVerts = qwopGame.getDebugVertices();

    runnerX = bodyVerts.torsoX;

    /// Set up for drawing
    ctx.clearRect(0, 0, width, height);
    ctx.save();
    ctx.fillStyle = '#631000'; // Dark red.

    // Draw each rectangular body.
    for (body = 0; body < bodyVerts.bodyVerts.length; body++) {
        var poly = bodyVerts.bodyVerts[body];

        ctx.beginPath();
        ctx.moveTo(scaling * (poly[0] - runnerX) + xOffset, scaling * poly[1] + yOffset);
        let item;
        for (item = 2; item < poly.length - 1; item += 2) {
            ctx.lineTo(scaling * (poly[item] - runnerX) + xOffset, scaling * poly[item + 1] + yOffset);
        }
        ctx.closePath();
        ctx.fill();
    }

    // Draw head circle.
    var radius = scaling * bodyVerts.headLocAndRadius[2];
    ctx.beginPath();
    ctx.arc(scaling * (bodyVerts.headLocAndRadius[0] - runnerX) + xOffset, scaling * bodyVerts.headLocAndRadius[1] + yOffset, radius, 0, 2*Math.PI, false);
    ctx.fill();
    ctx.closePath();

    // Draw the ground.
    ctx.fillStyle = '#636030'; // Dark red.
    groundH = bodyVerts.groundHeight;
    ctx.beginPath();
    ctx.moveTo(0, scaling * groundH + yOffset);
    ctx.lineTo(width, scaling * groundH + yOffset);
    ctx.stroke();
    ctx.closePath();

    // Dashes along ground to show movement.
    var dashSpacing = 25; // Pixel spacing in between dashes.
    ctx.beginPath();
    for (dashPos = -(scaling * runnerX) % dashSpacing; dashPos < width; dashPos += dashSpacing) {
        ctx.moveTo(dashPos, scaling * groundH + yOffset);
        ctx.lineTo(dashPos - 8, scaling * groundH + yOffset + 12);
    }
    ctx.stroke();
    ctx.closePath();

    ctx.restore();
};

var setCanvasDimensions = function(){
    canvas.width  = canvas.offsetWidth;
    canvas.height = 300;
    width = canvas.width;
    height = canvas.height;
    xOffset = width/2;
    yOffset = Math.min(300, height/2.2);
};

var resetRunner = function() {
    actionQueue = new actions.ActionQueue();
    q = false;
    w = false;
    o = false;
    p = false;
    qwopGame.makeNewWorld();
    qwopGame.setGravity(0., parseFloat(yGravitySlider.value));
    qwopGame.setMaxTorqueMultiplier(parseFloat(maxTorqueMultSlider.value) / 10);
    qwopGame.setBodyInertiaMultiplier(parseFloat(torsInertiaMultSlider.value) / 10)};

var setup = function() {

    window.onresize = setCanvasDimensions;

    canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');

    canvas.style.width ='100%';
    canvas.style.height='100%';
    setCanvasDimensions();

    // Trying to disable pinch zoom. Doesn't seem to work on IOS.
    document.addEventListener('touchmove', function (event) {
        if (event.scale !== 1) { event.preventDefault(); }
    }, false);

    var qbutton = document.getElementById('qbutton');
    var wbutton = document.getElementById('wbutton');
    var obutton = document.getElementById('obutton');
    var pbutton = document.getElementById('pbutton');
    var resetbutton = document.getElementById('resetbutton');

    var yGravityVal = document.getElementById('yGravityVal');
    var maxTorqueMultVal = document.getElementById('maxTorqueMultVal');
    var torsoInertiaMultVal = document.getElementById('torsoInertiaMultVal');
    var torsoStabilizerGainVal = document.getElementById('torsoStabilizerGainValue');
    var torsoVertStabilizerGainVal = document.getElementById('torsoVertStabilizerGainVal');

    yGravityVal.innerHTML = yGravitySlider.value;

    yGravitySlider.oninput = function() {
        yGravityVal.innerHTML = this.value;
        qwopGame.setGravity(0., parseFloat(this.value));
    };

    maxTorqueMultSlider.oninput = function() {
        maxTorqueMultVal.innerHTML = this.value / 10;
        qwopGame.setMaxTorqueMultiplier(parseFloat(this.value) / 10);
    };

    torsInertiaMultSlider.oninput = function() {
        torsoInertiaMultVal.innerHTML = this.value / 10;
        qwopGame.setBodyInertiaMultiplier(parseFloat(this.value) / 10);
    };

    // Turn on/off point feet (resets the game too).
    pointFeetCheckbox.oninput = function() {
        qwopGame.setPointFeet(pointFeetCheckbox.checked);
        resetRunner();
    };

    // Turn on/off torso angle stabilization controller.
    torsoAngleStabilizerCheckbox.oninput = function() {
        torsoAngleStabilization = torsoAngleStabilizerCheckbox.checked;
        torsoAngleStabilizerGainSlider.hidden = !torsoAngleStabilization;
        torsoStabilizerGainVal.hidden = !torsoAngleStabilization;
        torsoStabilizerGainVal.innerHTML = "Strength: " + torsoAngleStabilizerGainSlider.value;
        torsoAngleStabilizerK = torsoAngleStabilizerGainSlider.value * 100;
        torsoAngleStabilizerC = torsoAngleStabilizerGainSlider.value * 10;
    };

    torsoAngleStabilizerGainSlider.oninput = function() {
        torsoAngleStabilizerK = this.value * 100;
        torsoAngleStabilizerC = this.value * 10;
        torsoStabilizerGainVal.innerHTML = "Strength: " + this.value;
    };

    // Turn on/off torso angle stabilization controller.
    torsoVerticalStabilizerCheckbox.oninput = function() {
        torsoVerticalStabilization = torsoVerticalStabilizerCheckbox.checked;
        torsoVerticalStabilizerGainSlider.hidden = !torsoVerticalStabilization;
        torsoVertStabilizerGainVal.hidden = !torsoVerticalStabilization;
        torsoVertStabilizerGainVal.innerHTML = "Strength: " + torsoVerticalStabilizerGainSlider.value;
        torsoVerticalStabilizerK = torsoAngleStabilizerGainSlider.value * 0.6;
        torsoVerticalStabilizerC = torsoAngleStabilizerGainSlider.value * 0.06;
    };

    torsoVerticalStabilizerGainSlider.oninput = function() {
        torsoVerticalStabilizerK = this.value * 0.6;
        torsoVerticalStabilizerC = this.value * 0.06;
        torsoVertStabilizerGainVal.innerHTML = "Strength: " + this.value;
    };

    sequenceGoButton.onclick = function() {
        var sequenceStrArray = sequenceTextbox.value.split(',');
        actionQueue.clearAll();
        var currentKeyPos = 0;
        var keyOrder = [[false, false, false, false],
        [false, true, true, false], [false, false, false, false], [true, false, false, true]];
        resetRunner();
        for (let idx = 0; idx < sequenceStrArray.length; idx++) {
            actionQueue.addAction(new actions.Action(parseInt(sequenceStrArray[idx]),
                keyOrder[currentKeyPos][0],
                keyOrder[currentKeyPos][1],
                keyOrder[currentKeyPos][2],
                keyOrder[currentKeyPos][3]));
            currentKeyPos++;
            currentKeyPos %= 4;
        }
    };


    // QWOP and R reset key listeners.
    window.addEventListener('keydown', function(event) {
        switch (event.key) {
            case 'q':
                q = true;
                qbutton.style.borderStyle = "inset";
                break;
            case 'w':
                w = true;
                wbutton.style.borderStyle = "inset";
                break;
            case 'o':
                o = true;
                obutton.style.borderStyle = "inset";
                break;
            case 'p':
                p = true;
                pbutton.style.borderStyle = "inset";
                break;
            case 'r':
                resetbutton.style.borderStyle = "inset";
                resetRunner();
                break;
        }
    }, false);
    window.addEventListener('keyup', function(event) {
        switch (event.key) {
            case 'q':
                q = false;
                qbutton.style.borderStyle = "outset";
                break;
            case 'w':
                w = false;
                wbutton.style.borderStyle = "outset";
                break;
            case 'o':
                o = false;
                obutton.style.borderStyle = "outset";
                break;
            case 'p':
                p = false;
                pbutton.style.borderStyle = "outset";
                break;
            case 'r':
                resetbutton.style.borderStyle = "outset";
                break;
        }
    }, false);

    // Touch listeners for mobile.
    qbutton.addEventListener('touchstart', function(event) {
        q= true;
        qbutton.style.borderStyle = "inset";
    }, false);
    qbutton.addEventListener('touchend', function(event) {
        q= false;
        qbutton.style.borderStyle = "outset";
    }, false);

    wbutton.addEventListener('touchstart', function(event) {
        w= true;
        wbutton.style.borderStyle = "inset";
    }, false);
    wbutton.addEventListener('touchend', function(event) {
        w= false;
        wbutton.style.borderStyle = "outset";
    }, false);

    obutton.addEventListener('touchstart', function(event) {
        o= true;
        obutton.style.borderStyle = "inset";
    }, false);
    obutton.addEventListener('touchend', function(event) {
        o= false;
        obutton.style.borderStyle = "outset";
    }, false);


    pbutton.addEventListener('touchstart', function(event) {
        p= true;
        pbutton.style.borderStyle = "inset";
    }, false);
    pbutton.addEventListener('touchend', function(event) {
        p= false;
        pbutton.style.borderStyle = "outset";
    }, false);

    resetbutton.addEventListener('touchstart', function(event) {
        resetRunner();
        resetbutton.style.borderStyle = "inset";
    }, false);

    resetbutton.addEventListener('touchend', function(event) {
        resetbutton.style.borderStyle = "outset";
    }, false);

    resetbutton.addEventListener('mousedown', function(event) {
        resetRunner();
        resetbutton.style.borderStyle = "inset";
    }, false);

    resetbutton.addEventListener('mouseup', function(event) {
        resetbutton.style.borderStyle = "outset";
    }, false);

    setInterval(loop, frameDelay);
};

setup();

