const canvas = document.getElementById('holst');
const ctx = canvas.getContext("2d");
const rSelect = document.getElementById('r-select');


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
    drawBottomLeftShape();
    drawTopLeftRect();
    drawBottomRightTriangle();
    drawAxes();
    drawLabels();
    drawArrows();

}

function drawAxes() {
    ctx.beginPath();
    ctx.moveTo(0, -195);
    ctx.lineTo(0, 195);
    ctx.moveTo(-195, 0);
    ctx.lineTo(195, 0);
    ctx.closePath();
    ctx.stroke();
}



function drawArrows() {
    const arrowSize = 10;

    ctx.beginPath();
    ctx.moveTo(0, -195);
    ctx.lineTo(-arrowSize / 2, -195 + arrowSize);
    ctx.moveTo(0, -195);
    ctx.lineTo(arrowSize / 2, -195 + arrowSize);
    ctx.fillStyle = 'black';
    ctx.fillText("Y", -arrowSize / 2 - 15, -195 + arrowSize);

    ctx.moveTo(195, 0);
    ctx.lineTo(195 - arrowSize, arrowSize / 2);
    ctx.moveTo(195, 0);
    ctx.lineTo(195 - arrowSize, -arrowSize / 2);
    ctx.fillText("X", 195 - arrowSize, arrowSize / 2 + 15);
    ctx.closePath();
    ctx.stroke();
}


function scalePx(value) {
    return value * (canvasCfg.basisR / canvasCfg.r);
}


function drawBottomLeftShape() {
    ctx.save()
    ctx.beginPath();
    const radius = scalePx(canvasCfg.r / 2);
    ctx.arc(0, 0, radius, Math.PI / 2, Math.PI, false);
    ctx.lineTo(0, 0);
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "rgba(51,153,255,0.5)"
    ctx.stroke();
    ctx.restore();

}

function drawTopLeftRect() {
    ctx.save()
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(scalePx(-canvasCfg.r / 2), 0);
    ctx.lineTo(scalePx(-canvasCfg.r / 2), -scalePx(canvasCfg.r));
    ctx.lineTo(0, -scalePx(canvasCfg.r));
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "rgba(51,153,255,0.5)"
    ctx.stroke();
    ctx.restore();
}

function drawBottomRightTriangle() {
    ctx.save();
    ctx.beginPath();
    ctx.moveTo(0, 0)
    ctx.lineTo(scalePx(canvasCfg.r), 0);
    ctx.lineTo(0, scalePx(canvasCfg.r));
    ctx.closePath();
    ctx.fill();
    ctx.strokeStyle = "rgba(51,153,255,0.5)"
    ctx.stroke();
    ctx.restore();
}

let dots = []

function addPoint(x, y, r = canvasCfg.r) {
    const newDots = { x, y, r };
    dots.push(newDots);
    draw();
    drawPoint();

}

function drawDot(dot, color = 'rgb(0, 0, 0)') {
    ctx.save();
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.fillStyle = color;
    const useR = canvasCfg.r;
    const cx = canvas.width / 2 + (canvasCfg.basisR / useR) * dot.x;
    const cy = canvas.height / 2 - (canvasCfg.basisR / useR) * dot.y;
    ctx.beginPath();
    ctx.arc(cx, cy, canvasCfg.basisR / 50, 0, Math.PI * 2);
    ctx.fill();
    ctx.restore();
}

function drawPoint() {
    dots.forEach(dot => drawDot(dot));
}

function getLabels() {
    const unit = canvasCfg.basisR; // фиксированные риски по базовому R

    return [
        {mult: 1, x: unit, y: 0},
        {mult: 0.5, x: unit / 2, y: 0},

        {mult: 1, x: 0, y: -(unit)},
        {mult: 0.5, x: 0, y: -(unit / 2)},

        {mult: -1, x: -unit, y: 0},
        {mult: -0.5, x: -unit / 2, y: 0},

        {mult: -1, x: 0, y: unit},
        {mult: -0.5, x: 0, y: unit / 2}
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



rSelect?.addEventListener('change', (event) => {
    const value = parseFloat(event.target.value);
    if (!Number.isNaN(value) && value > 0) {
        canvasCfg.r = value;
        draw();
        drawPoint();
    }
});

function addSelectedPointsToCanvas(xs, y, r) {
    xs.forEach((x) => {
        const numX = parseFloat(x);
        if (!Number.isNaN(numX)) {
            addPoint(numX, y, r);
        }
    });
}

function loadDotsFromTable() {
    const rows = document.querySelectorAll('#table-results tr');
    rows.forEach((row) => {
        const cells = row.querySelectorAll('td');
        if (cells.length >= 3) {
            const x = parseFloat(cells[0].textContent);
            const y = parseFloat(cells[1].textContent);
            const r = parseFloat(cells[2].textContent);
            if (!Number.isNaN(x) && !Number.isNaN(y) && !Number.isNaN(r)) {
                dots.push({ x, y, r });
            }
        }
    });
    if (dots.length) {
        draw();
        drawPoint();
    }
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        draw();
        loadDotsFromTable();
        drawPoint();
    });
} else {
    draw();
    loadDotsFromTable();
    drawPoint();
}

function clearCanvasPoints() {
    dots = [];
    draw();
    drawPoint();
}


canvas.addEventListener("click", onClickCanvas)

function onClickCanvas(event) {
    const rect = canvas.getBoundingClientRect();

    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;
    const pixelX = (event.clientX - rect.left) * scaleX;
    const pixelY = (event.clientY - rect.top) * scaleY;

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    const R = parseFloat(rSelect.value);
    const basisR = canvasCfg.basisR;
    const x = (pixelX - centerX) * R / basisR;
    const y = (centerY - pixelY) * R / basisR;

    const xVal = +x.toFixed(2);
    const yVal = +y.toFixed(2);

    console.log('Клик:', { xVal, yVal, R });
    drawDot({ x: xVal, y: yVal }, 'red');
    sendDataToServer(xVal, yVal, R);


}
async function sendDataToServer(x, y, r) {
    const params = new URLSearchParams();
    params.append('x', x.toString());
    params.append('y', y.toString());
    params.append('r', r.toString());

    try {
        const resp = await fetch(`validationParam?${params.toString()}`, {
        });
        if (!resp.ok) {
            throw new Error(`HTTP error! status ${resp.status}`);
        }
        const data = await resp.json();
        updateTableAndCanvas(data.hits);

    } catch (error) {
        console.error('Ошибка при отправке точки на сервер:', error);
        alert('Не удалось отправить точку на сервер :(');
    }
}

function updateTableAndCanvas(hits) {
    if (!Array.isArray(hits) || hits.length === 0) return;
    const tbody = document.getElementById('table-results');
    hits.forEach((res) => {
        const xVal = parseFloat(res.x);
        const yVal = parseFloat(res.y);
        const rVal = parseFloat(res.r);

        if (!Number.isNaN(xVal) && !Number.isNaN(yVal) && !Number.isNaN(rVal)) {
            dots.push({ x: xVal, y: yVal, r: rVal });
        }

        if (tbody) {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${res.x}</td>
                <td>${res.y}</td>
                <td>${res.r}</td>
                <td class="hit ${res.hit ? 'yes' : 'no'}">${res.hit}</td>
                <td>${res.nowTime ?? ''}</td>
                <td>${res.execTime ?? ''}</td>
            `;
            tbody.appendChild(tr);
        }
    });
    draw();
    drawPoint();
    if (typeof refreshPagination === 'function') {
        refreshPagination();
    }
}
