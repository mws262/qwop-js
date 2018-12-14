// Loads up the transcompiled Java QWOP implementation (in bundle.js) and sets up GUI.

var frameRate  = 1/25;
var frameDelay = frameRate * 1000;

var canvas;
var ctx;
var width;
var height;
var qwopGame = new game.GameSingleThread();
var scaling = 20;
var xOffset = 100;
var yOffset = 300;
var q = false;
var w = false;
var o = false;
var p = false;

var loop = function() {

    // Step the physics forward in time.
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
    canvas.height = canvas.offsetHeight;
    width = canvas.width;
    height = canvas.height;
    xOffset = width/2;
    yOffset = Math.min(250, height/3);
};

var setup = function() {

    window.onresize = setCanvasDimensions;

    canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');

    canvas.style.width ='100%';
    canvas.style.height='100%';
    setCanvasDimensions();

    // QWOP and R reset key listeners.
    window.addEventListener('keydown', function(event) {
        switch (event.key) {
            case 'q':
                q = true;
                break;
            case 'w':
                w = true;
                break;
            case 'o':
                o = true;
                break;
            case 'p':
                p = true;
                break;
            case 'r':
                q = false;
                w = false;
                o = false;
                p = false;
                qwopGame.makeNewWorld();
                break;
        }
    }, false);
    window.addEventListener('keyup', function(event) {
        switch (event.key) {
            case 'q':
                q = false;
                break;
            case 'w':
                w = false;
                break;
            case 'o':
                o = false;
                break;
            case 'p':
                p = false;
                break;
        }
    }, false);

    setInterval(loop, frameDelay);
};

setup();

