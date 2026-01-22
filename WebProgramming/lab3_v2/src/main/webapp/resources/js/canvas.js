function getCurrentR() {
    const field = document.getElementById('graphForm:clickR');
    let raw = field && field.value ? field.value : '';
    if (!raw) {
        const fallback = document.getElementById('inputForm:r');
        raw = fallback && fallback.value ? fallback.value : '';
    }
    if (!raw) {
        return null;
    }
    const value = parseFloat(raw.replace(',', '.'));
    return Number.isFinite(value) ? value : null;
}

function getPoints() {
    const dataEl = document.getElementById('graphForm:pointsData');
    if (!dataEl) {
        return [];
    }
    const text = dataEl.textContent.trim();
    if (!text) {
        return [];
    }
    try {
        return JSON.parse(text);
    } catch (error) {
        return [];
    }
}

function submitCanvas() {
    const ajaxButton = document.getElementById('graphForm:submitCanvasButton');
    if (ajaxButton) {
        ajaxButton.click();
        return;
    }
    if (typeof submitFromCanvas === 'function') {
        submitFromCanvas();
        return;
    }
    const fullSubmit = document.getElementById('graphForm:fullSubmitButton');
    if (fullSubmit) {
        fullSubmit.click();
        return;
    }
    const form = document.getElementById('graphForm');
    if (form) {
        form.submit();
    }
}


const graphState = {
    width: 0,
    height: 0,
    size: 0,
    offsetX: 0,
    offsetY: 0,
    padding: 0,
    drawSize: 0,
    centerX: 0,
    centerY: 0,
    scale: 0
};

function computeCanvasGeometry(canvas) {
    const rect = canvas.getBoundingClientRect();
    const width = Math.max(0, rect.width);
    const height = Math.max(0, rect.height);
    const size = Math.max(0, Math.min(width, height));
    const offsetX = (width - size) / 2;
    const offsetY = (height - size) / 2;
    const padding = Math.max(10, size * 0.03);
    const drawSize = Math.max(0, size - padding * 2);
    return {
        rect: rect,
        width: width,
        height: height,
        size: size,
        offsetX: offsetX,
        offsetY: offsetY,
        padding: padding,
        drawSize: drawSize
    };
}

function getLocalPoint(canvas, event) {
    if (event.target === canvas && typeof event.offsetX === 'number' && typeof event.offsetY === 'number') {
        return { x: event.offsetX, y: event.offsetY };
    }
    const rect = canvas.getBoundingClientRect();
    return {
        x: event.clientX - rect.left,
        y: event.clientY - rect.top
    };
}

function handleCanvasEvent(canvas, event) {
    const r = getCurrentR();
    if (!r) {
        alert('Сначала выберите значение R.');
        return;
    }
    const geometry = computeCanvasGeometry(canvas);
    if (!graphState.size || Math.abs(graphState.size - geometry.size) > 0.5) {
        drawGraph();
    }
    const local = getLocalPoint(canvas, event);
    const x = (local.x - graphState.centerX) / graphState.scale;
    const y = (graphState.centerY - local.y) / graphState.scale;
    const xField = document.getElementById('graphForm:clickX');
    const yField = document.getElementById('graphForm:clickY');
    const rField = document.getElementById('graphForm:clickR');
    if (!xField || !yField || !rField) {
        return;
    }
    if (x < -3 || x > 3 || y < -5 || y > 5) {
        alert('Точка вне допустимого диапазона: X [-3; 3], Y [-5; 5].');
        return;
    }
    xField.value = x.toFixed(2);
    yField.value = y.toFixed(2);
    rField.value = r.toFixed(2);
    submitCanvas();
}

function attachCanvasHandler(canvas) {
    if (!canvas || canvas.dataset.bound === 'true') {
        return;
    }
    canvas.dataset.bound = 'true';
    const usePointer = typeof window !== 'undefined' && 'PointerEvent' in window;
    const handler = function (event) {
        event.preventDefault();
        event.stopPropagation();
        handleCanvasEvent(canvas, event);
    };
    canvas.addEventListener(usePointer ? 'pointerdown' : 'click', handler);

    const wrapper = document.getElementById('graphWrapper');
    if (wrapper && wrapper.dataset.bound !== 'true') {
        wrapper.dataset.bound = 'true';
        const wrapperHandler = function (event) {
            if (event.target === canvas) {
                return;
            }
            const rect = canvas.getBoundingClientRect();
            const inside = event.clientX >= rect.left && event.clientX <= rect.right
                && event.clientY >= rect.top && event.clientY <= rect.bottom;
            if (!inside) {
                return;
            }
            handleCanvasEvent(canvas, event);
        };
        wrapper.addEventListener(usePointer ? 'pointerdown' : 'click', wrapperHandler);
    }
}

function prepareCanvas(canvas) {
    const rect = canvas.getBoundingClientRect();
    const width = Math.max(0, rect.width);
    const height = Math.max(0, rect.height);
    const pixelRatio = window.devicePixelRatio || 1;
    if (width > 0 && height > 0) {
        canvas.width = Math.round(width * pixelRatio);
        canvas.height = Math.round(height * pixelRatio);
    }
    const ctx = canvas.getContext('2d');
    ctx.setTransform(pixelRatio, 0, 0, pixelRatio, 0, 0);
    return { ctx: ctx, width: width, height: height };
}

function drawGraph() {
    const canvas = document.getElementById('graph');
    if (!canvas) {
        return;
    }
    const prepared = prepareCanvas(canvas);
    const ctx = prepared.ctx;
    const width = prepared.width || canvas.width;
    const height = prepared.height || canvas.height;
    const size = Math.max(0, Math.min(width, height));
    const r = getCurrentR() || 1;
    const offsetX = (width - size) / 2;
    const offsetY = (height - size) / 2;
    const padding = Math.max(10, size * 0.03);
    const drawSize = Math.max(0, size - padding * 2);
    const centerX = offsetX + padding + drawSize / 2;
    const centerY = offsetY + padding + drawSize / 2;
    const scale = (drawSize / 2) / r;
    graphState.size = size;
    graphState.width = width;
    graphState.height = height;
    graphState.offsetX = offsetX;
    graphState.offsetY = offsetY;
    graphState.padding = padding;
    graphState.drawSize = drawSize;
    graphState.centerX = centerX;
    graphState.centerY = centerY;
    graphState.scale = scale;

    ctx.clearRect(0, 0, width, height);

    const mapX = (x) => centerX + x * scale;
    const mapY = (y) => centerY - y * scale;

    const background = ctx.createLinearGradient(0, 0, width, height);
    background.addColorStop(0, '#ffffff');
    background.addColorStop(1, '#eef1f9');
    ctx.fillStyle = background;
    ctx.fillRect(0, 0, width, height);

    ctx.strokeStyle = 'rgba(27, 45, 54, 0.12)';
    ctx.lineWidth = 1;
    ctx.strokeRect(offsetX + padding, offsetY + padding, drawSize, drawSize);

    ctx.strokeStyle = 'rgba(27, 45, 54, 0.12)';
    ctx.lineWidth = 1;
    const gridTicks = [-r, -r / 2, 0, r / 2, r];
    gridTicks.forEach(function (tick) {
        const gx = mapX(tick);
        const gy = mapY(tick);
        ctx.beginPath();
        ctx.moveTo(gx, 0);
        ctx.lineTo(gx, height);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(0, gy);
        ctx.lineTo(width, gy);
        ctx.stroke();
    });

    ctx.fillStyle = 'rgba(255, 139, 99, 0.28)';
    ctx.strokeStyle = 'rgba(255, 139, 99, 0.35)';
    ctx.lineWidth = 1;

    const rectX = mapX(-r);
    const rectY = mapY(r / 2);
    const rectW = mapX(0) - rectX;
    const rectH = mapY(0) - rectY;
    ctx.fillRect(rectX, rectY, rectW, rectH);
    ctx.strokeRect(rectX, rectY, rectW, rectH);

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, r * scale, Math.PI * 1.5, Math.PI * 2);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(mapX(r / 2), mapY(0));
    ctx.lineTo(mapX(0), mapY(-r));
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    const axisLeft = offsetX + padding;
    const axisRight = offsetX + padding + drawSize;
    const axisTop = offsetY + padding;
    const axisBottom = offsetY + padding + drawSize;

    ctx.strokeStyle = '#1b2d36';
    ctx.lineWidth = 1.8;
    ctx.beginPath();
    ctx.moveTo(axisLeft, centerY);
    ctx.lineTo(axisRight, centerY);
    ctx.moveTo(centerX, axisBottom);
    ctx.lineTo(centerX, axisTop);
    ctx.stroke();

    const ticks = [-r, -r / 2, r / 2, r];
    ctx.strokeStyle = '#1b2d36';
    ctx.fillStyle = '#1b2d36';
    ctx.font = '12px Manrope, sans-serif';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'top';
    ticks.forEach(function (tick) {
        const x = mapX(tick);
        ctx.beginPath();
        ctx.moveTo(x, centerY - 6);
        ctx.lineTo(x, centerY + 6);
        ctx.stroke();
        ctx.fillText(formatTick(tick), x, centerY + 10);
    });
    ctx.textAlign = 'left';
    ctx.textBaseline = 'middle';
    ticks.forEach(function (tick) {
        const y = mapY(tick);
        ctx.beginPath();
        ctx.moveTo(centerX - 6, y);
        ctx.lineTo(centerX + 6, y);
        ctx.stroke();
        ctx.fillText(formatTick(tick), centerX + 10, y);
    });
    ctx.textAlign = 'left';
    ctx.textBaseline = 'top';
    ctx.fillText('0', centerX + 8, centerY + 8);

    const arrowSize = Math.max(6, drawSize * 0.04);
    const arrowPad = Math.max(4, drawSize * 0.015);
    ctx.strokeStyle = '#1b2d36';
    ctx.lineWidth = 1.6;
    ctx.beginPath();
    ctx.moveTo(centerX, axisTop - arrowPad);
    ctx.lineTo(centerX - arrowSize / 2, axisTop + arrowSize - arrowPad);
    ctx.moveTo(centerX, axisTop - arrowPad);
    ctx.lineTo(centerX + arrowSize / 2, axisTop + arrowSize - arrowPad);
    ctx.moveTo(axisRight + arrowPad, centerY);
    ctx.lineTo(axisRight - arrowSize + arrowPad, centerY - arrowSize / 2);
    ctx.moveTo(axisRight + arrowPad, centerY);
    ctx.lineTo(axisRight - arrowSize + arrowPad, centerY + arrowSize / 2);
    ctx.stroke();

    ctx.fillStyle = '#1b2d36';
    ctx.font = '12px Manrope, sans-serif';
    ctx.textAlign = 'left';
    ctx.textBaseline = 'top';
    ctx.fillText('Y', centerX + arrowSize / 2 + 4, axisTop + arrowSize - arrowPad + 2);
    ctx.textAlign = 'right';
    ctx.textBaseline = 'middle';
    ctx.fillText('X', axisRight - arrowSize + arrowPad - 4, centerY - arrowSize / 2);

    const points = getPoints();
    points.forEach(function (point) {
        const px = mapX(point.x);
        const py = mapY(point.y);
        const color = point.hit ? '#2fd676' : '#ff5c5c';
        ctx.save();
        ctx.shadowColor = color;
        ctx.shadowBlur = 14;
        ctx.beginPath();
        ctx.fillStyle = color;
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.9)';
        ctx.lineWidth = 1.6;
        ctx.arc(px, py, 5, 0, Math.PI * 2);
        ctx.fill();
        ctx.stroke();
        ctx.beginPath();
        ctx.shadowBlur = 0;
        ctx.fillStyle = 'rgba(255, 255, 255, 0.7)';
        ctx.arc(px - 1.5, py - 1.5, 1.6, 0, Math.PI * 2);
        ctx.fill();
        ctx.restore();
    });

    attachCanvasHandler(canvas);
    bindPillGroups();
}

window.addEventListener('load', function () {
    drawGraph();
    bindPillGroups();
});

window.addEventListener('resize', function () {
    drawGraph();
});

function formatTick(value) {
    const rounded = Math.round(value * 100) / 100;
    if (Number.isInteger(rounded)) {
        return String(rounded);
    }
    return rounded.toFixed(2);
}

function bindPillGroups() {
    document.querySelectorAll('.pill-group').forEach(function (group) {
        if (group.dataset.bound === 'true') {
            return;
        }
        group.dataset.bound = 'true';
        group.addEventListener('click', function (event) {
            const button = event.target.closest('.pill');
            if (!button) {
                return;
            }
            const targetId = group.dataset.target;
            const value = button.dataset.value;
            if (!targetId || value == null) {
                return;
            }
            const targetField = document.getElementById(targetId);
            if (targetField) {
                if (group.dataset.multi === 'true') {
                    button.classList.toggle('is-active');
                    const values = Array.from(group.querySelectorAll('.pill.is-active'))
                        .map(function (pill) {
                            return pill.dataset.value;
                        });
                    targetField.value = values.join(',');
                } else {
                    targetField.value = value;
                }
            }
            const syncId = group.dataset.sync;
            if (syncId) {
                const syncField = document.getElementById(syncId);
                if (syncField) {
                    syncField.value = value;
                }
            }
            if (group.dataset.multi !== 'true') {
                group.querySelectorAll('.pill').forEach(function (pill) {
                    pill.classList.toggle('is-active', pill === button);
                });
            }
            if (group.dataset.onchange === 'drawGraph') {
                drawGraph();
            }
        });
    });
    syncPillGroups();
}

function syncPillGroups() {
    document.querySelectorAll('.pill-group').forEach(function (group) {
        const targetId = group.dataset.target;
        if (!targetId) {
            return;
        }
        const targetField = document.getElementById(targetId);
        if (!targetField) {
            return;
        }
        let appliedDefault = false;
        if (!targetField.value) {
            const fallback = group.dataset.default;
            if (fallback && group.dataset.multi !== 'true') {
                targetField.value = fallback;
                const syncId = group.dataset.sync;
                if (syncId) {
                    const syncField = document.getElementById(syncId);
                    if (syncField) {
                        syncField.value = fallback;
                    }
                }
                appliedDefault = true;
            }
        }
        if (!targetField.value) {
            return;
        }
        const currentValues = targetField.value.split(',').map(function (part) {
            return part.trim();
        });
        group.querySelectorAll('.pill').forEach(function (pill) {
            pill.classList.toggle('is-active', currentValues.includes(pill.dataset.value));
        });
        if (appliedDefault && group.dataset.onchange === 'drawGraph') {
            drawGraph();
        }
    });
}

function onAjaxEvent(data) {
    if (data.status === 'success') {
        drawGraph();
    }
}
