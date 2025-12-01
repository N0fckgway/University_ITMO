const canvas = document.getElementById('holst');
const ctx = canvas.getContext("2d");
function draw() {
    initStyle();
    ctx.beginPath();
    ctx.moveTo(0, -190);
    ctx.lineTo(0, 190);
    ctx.moveTo(-190, 0);
    ctx.lineTo(190, 0);
    ctx.stroke();
    drawArrows();


}

function initStyle() {
    ctx.font = "18px Roboto";
    ctx.translate(canvas.width / 2, canvas.height / 2);
    ctx.strokeStyle = "black";
    ctx.lineWidth = 2;
}

function drawArrows() {
    const arrowSize = 10;

    ctx.beginPath();
    ctx.moveTo(0, -190);
    ctx.lineTo(-arrowSize / 2, -190 + arrowSize);
    ctx.moveTo(0, -190);
    ctx.lineTo(arrowSize / 2, -190 + arrowSize);
    ctx.fillText("Y", -arrowSize / 2 - 15, -190 + arrowSize);

    ctx.moveTo(190, 0);
    ctx.lineTo(190 - arrowSize, arrowSize / 2);
    ctx.moveTo(190, 0);
    ctx.lineTo(190 - arrowSize, -arrowSize / 2);
    ctx.fillText("X", 190 - arrowSize, arrowSize / 2 - 15)
    ctx.stroke();
}



window.addEventListener('load', draw);



