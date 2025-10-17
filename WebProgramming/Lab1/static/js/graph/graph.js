const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");

const canvasCfg = {
    basisR: canvas.width * 0.4,
    r: 2,
    shift: 10
}

$(document).ready(function() {
    console.log("Graph.js loaded, canvas:", canvas);
    console.log("Canvas context:", ctx);
    console.log("Canvas config:", canvasCfg);
    draw();
})

function initStyles() {
    ctx.fillStyle = "rgba(51,153,255,0.5)";
    ctx.strokeStyle = "rgba(0,0,0,1)";
    ctx.font = "18px Roboto";
}

function refresh(r) {
    canvasCfg.r = r;
    draw();
}

function draw() {
    console.log("Drawing graph...");
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    initStyles()

    drawAxis();
    drawArrows();

    drawShape();

    drawText();
    drawPoints();
    console.log("Graph drawn successfully");
}

function drawShape() {
    const R = canvasCfg.basisR;

    ctx.save();
    ctx.translate(canvas.width / 2, canvas.height / 2);

    drawLeftSemicircle(R);
    drawTopRightSquare(R);
    drawBottomRightTriangle(R);

    ctx.restore();
}

function drawLeftSemicircle(R) {
    ctx.beginPath();
    // Четверть круга во второй четверти: от π/2 до π
    ctx.arc(0, 0, R, Math.PI, Math.PI * 1.5, false);
    ctx.lineTo(0, 0);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}

function drawTopRightSquare(R) {
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(R, 0);
    ctx.lineTo(R, -R);
    ctx.lineTo(0, -R);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}

function drawBottomRightTriangle(R) {
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(R / 2, 0);   // вправо до R/2
    ctx.lineTo(0, R / 2);   // вниз до R/2
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}

let dots = []

function addPoint(x, y) {
    let newDot = {
        x: x,
        y: y
    }
    dots.push(newDot);
    drawDot(newDot)
}

function drawDot(dot, color = 'rgb(0,0,0)', r = canvasCfg.r) {
    ctx.save();
    ctx.fillStyle = color;

    ctx.beginPath();
    ctx.moveTo(dot.x, dot.y);
    ctx.arc(canvas.width / 2 + canvasCfg.basisR / r * dot.x,
        canvas.height / 2 - canvasCfg.basisR / r * dot.y, canvasCfg.basisR / 50, 0, Math.PI * 2);
    ctx.fill();

    ctx.restore();
}

function drawPoints() {
    dots.forEach(dot => drawDot(dot));
}


function drawAxis() {
    ctx.beginPath();
    //Y-axis
    ctx.moveTo(canvas.width / 2, 0);
    ctx.lineTo(canvas.width / 2, canvas.height);
    //X-axis
    ctx.moveTo(0, canvas.height / 2);
    ctx.lineTo(canvas.width, canvas.height / 2);
    ctx.stroke();
}

function drawArrows() {
    ctx.beginPath();
    //Y arrow
    ctx.moveTo(canvas.width / 2, 0);
    ctx.lineTo(canvas.width / 2 - canvas.width / 100, canvas.height / 50);
    ctx.moveTo(canvas.width / 2, 0);
    ctx.lineTo(canvas.width / 2 + canvas.width / 100, canvas.height / 50);
    //X-axis arrow
    ctx.moveTo(canvas.width, canvas.height / 2);
    ctx.lineTo(canvas.width - canvas.width / 50, canvas.height / 2 - canvas.height / 100);
    ctx.moveTo(canvas.width, canvas.height / 2);
    ctx.lineTo(canvas.width - canvas.width / 50, canvas.height / 2 + canvas.height / 100);
    ctx.stroke();
}

function getLabels() {
    const unitInPixels = canvasCfg.basisR;

    return [
        {mult: 1, x: unitInPixels, y: 0},
        {mult: 1, x: 0, y: unitInPixels},

        {mult: 0.5, x: unitInPixels / 2, y: 0},
        {mult: 0.5, x: 0, y: unitInPixels / 2},

        {mult: -1, x: -unitInPixels, y: 0},
        {mult: -1, x: 0, y: -unitInPixels},

        {mult: -0.5, x: -unitInPixels / 2, y: 0},
        {mult: -0.5, x: 0, y: -unitInPixels / 2}
    ];
}

function drawText() {
    drawLabels();
    drawAxisSymbols();
}

function drawLabels() {
    ctx.save();
    ctx.fillStyle = "black";
    const labels = getLabels();
    labels.forEach(label => {
        drawLabel(label);
        drawTick(label);
    })
    ctx.restore();
}

function drawLabel(label) {
    let shiftX = label.x === 0 ? canvasCfg.shift : 0;
    let shiftY = label.y === 0 ? canvasCfg.shift : 0;

    let radius = Math.round(label.mult * canvasCfg.r * 100) / 100;

    ctx.fillText(radius.toString(), canvas.width / 2 + label.x + shiftX, canvas.height / 2 - label.y - shiftY);
}

function drawTick(label) {
    const tickLength = 5;
    if (label.x === 0) {
        drawLine({x: canvas.width / 2 + tickLength, y: canvas.height / 2 + label.y}, {
            x: canvas.width / 2 - tickLength,
            y: canvas.height / 2 + label.y
        });
    } else {
        drawLine({x: canvas.width / 2 + label.x, y: canvas.height / 2 + tickLength}, {
            x: canvas.width / 2 + label.x,
            y: canvas.height / 2 - tickLength
        },);
    }
}

function drawLine(from, to) {
    ctx.beginPath();
    ctx.moveTo(from.x, from.y);
    ctx.lineTo(to.x, to.y);
    ctx.stroke();
}

function drawAxisSymbols() {
    ctx.save();
    ctx.fillStyle = "black";
    ctx.fillText("X", canvas.width - 15, canvas.height / 2 - canvasCfg.shift);
    ctx.fillText("Y", canvas.width / 2 + canvasCfg.shift, 15);
    ctx.restore();
}