const canvas = document.getElementById('holst');
const ctx = canvas.getContext("2d");

const canvasCfg = {
    basisR: canvas.width * 0.45,
    r: 2,
    shift: 10
}

function initStyle() {
    ctx.font = "18px Roboto";
    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.fillStyle = "rgba(51,153,255,0.5)";
    ctx.strokeStyle = "rgba(0,0,0,1)";
    ctx.lineWidth = 2;
}

function draw() {
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    initStyle();
    drawBottomLeftShape(canvasCfg.basisR);
    drawTopLeftRect(canvasCfg.basisR);
    drawBottomRightTriangle(canvasCfg.basisR);
    drawAxes();
    drawLabels();
    drawArrows();

}

function drawAxes() {
    ctx.beginPath();
    ctx.moveTo(0, -190);
    ctx.lineTo(0, 190);
    ctx.moveTo(-190, 0);
    ctx.lineTo(190, 0);
    ctx.closePath();
    ctx.stroke();
}



function drawArrows() {
    const arrowSize = 10;

    ctx.beginPath();
    ctx.moveTo(0, -190);
    ctx.lineTo(-arrowSize / 2, -190 + arrowSize);
    ctx.moveTo(0, -190);
    ctx.lineTo(arrowSize / 2, -190 + arrowSize);
    ctx.fillStyle = 'black';
    ctx.fillText("Y", -arrowSize / 2 - 15, -190 + arrowSize);

    ctx.moveTo(190, 0);
    ctx.lineTo(190 - arrowSize, arrowSize / 2);
    ctx.moveTo(190, 0);
    ctx.lineTo(190 - arrowSize, -arrowSize / 2);
    ctx.fillText("X", 190 - arrowSize, arrowSize / 2 + 15);
    ctx.closePath();
    ctx.stroke();
}


function drawBottomLeftShape(R) {
    ctx.save()
    ctx.beginPath();
    ctx.arc(0, 0, R / 2, Math.PI / 2, Math.PI, false);
    ctx.lineTo(0, 0);
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "rgba(51,153,255,0.5)"
    ctx.stroke();
    ctx.restore();

}

function drawTopLeftRect(R) {
    ctx.save()
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(-R / 2, 0);
    ctx.lineTo(-R / 2, -R + canvasCfg.shift);
    ctx.lineTo(0, -R + canvasCfg.shift);
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "rgba(51,153,255,0.5)"
    ctx.stroke();
    ctx.restore();
}

function drawBottomRightTriangle(R) {
    ctx.save();
    ctx.beginPath();
    ctx.moveTo(0, 0)
    ctx.lineTo(R - canvasCfg.shift, 0);
    ctx.lineTo(0, R - canvasCfg.shift);
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "rgba(51,153,255,0.5)"
    ctx.stroke();
    ctx.restore();
}

let dots = []

function addPoint(x, y) {
     let newDots = {
         x: x,
         y: y
     }
     dots.push(newDots);
     drawDot(newDots);

}

function drawDot(dot, color = 'rgb(0, 0, 0)', r = canvasCfg.r) {
    ctx.save();
    ctx.fillStyle = color;
    ctx.moveTo(dot.x, dot.y);
    ctx.arc(canvas.width / 2 + canvasCfg.basisR / r * dot.x,
        canvas.height / 2 - canvasCfg.basisR / r * dot.y, canvasCfg.basisR / 50, 0, Math.PI * 2);
    ctx.fill();

    ctx.restore();

}

function drawPoint() {
    dots.forEach(dot => drawDot(dot));
}

function getLabels() {
    const unit = canvasCfg.basisR;

    return [
        {mult: 1, x: unit - 10, y: 0},
        {mult: 0.5, x: unit / 2 - 10, y: 0},

        {mult: 1, x: 0, y: unit - 10},
        {mult: 0.5, x: 0, y: unit / 2},

        {mult: -1, x: -unit, y: 0},
        {mult: -0.5, x: -unit / 2, y: 0},

        {mult: -1, x: 0, y: -unit + 10},
        {mult: -0.5, x: 0, y: -unit / 2 + 10}
    ];
}

function drawLabels() {
    ctx.save();
    ctx.fillStyle = 'black';
    const labels = getLabels();
    labels.forEach(label => {
        drawLabel(label)
        drawTick(label)
    })
    ctx.restore();

}

function drawLabel(label) {
    let shiftX = label.x === 0 ? canvasCfg.shift : 0;
    let shiftY = label.y === 0 ? canvasCfg.shift : 0;

    let radius = Math.round(label.mult * canvasCfg.r * 100) / 100;

    ctx.fillText(radius.toString(), label.x + shiftX, label.y - shiftY);
}

function drawTick(label) {
    const tickLength = 5;
    ctx.beginPath();

    if (label.x === 0) {
        ctx.moveTo(-tickLength, label.y);
        ctx.lineTo( tickLength, label.y);
    } else {
        ctx.moveTo(label.x, -tickLength);
        ctx.lineTo(label.x,  tickLength);
    }
    ctx.stroke();
}



window.addEventListener('load', draw);



