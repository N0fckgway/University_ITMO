let clockBaseTime = null;
let clockInterval = null;

function parseServerTime(text) {
    const match = text.match(/(\d{2})\.(\d{2})\.(\d{4})\s+(\d{2}):(\d{2}):(\d{2})/);
    if (!match) {
        return null;
    }
    const day = parseInt(match[1], 10);
    const month = parseInt(match[2], 10) - 1;
    const year = parseInt(match[3], 10);
    const hours = parseInt(match[4], 10);
    const minutes = parseInt(match[5], 10);
    const seconds = parseInt(match[6], 10);

    return new Date(year, month, day, hours, minutes, seconds);

}

function readServerTime() {
    const serverTime = document.getElementById('serverTime');
    if (!serverTime) {
        return null;
    }
    const textTime = serverTime.textContent.toString();
    return textTime ? parseServerTime(textTime) : null;
}

function pad(value) {
    return value < 10 ? ('0' + value) : String(value)
}

function updateReadout(date) {
    const timeValue = document.getElementById('timeValue');
    const dateValue = document.getElementById('dateValue');
    if (timeValue) {
        timeValue.textContent = pad(date.getHours()) + ':' + pad(date.getMinutes()) + ':' + pad(date.getSeconds());

    }
    if (dateValue) {
        dateValue.textContent = pad(date.getDate()) + '.' + pad(date.getMonth() + 1) + '.' + date.getFullYear();
    }
}

function updateHands(date) {
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();

    const hourDeg = ((hours % 12) + minutes / 60 + seconds / 3600) * 30;
    const minuteDeg = (minutes + seconds / 60) * 6;
    const secondDeg = seconds * 6;

    const hourHand = document.getElementById('hourHand');
    const minuteHand = document.getElementById('minuteHand');
    const secondHand = document.getElementById('secondHand');

    if (hourHand) {
        hourHand.style.transform = 'rotate(' + hourDeg + 'deg)';
    }
    if (minuteHand) {
        minuteHand.style.transform = 'rotate(' + minuteDeg + 'deg)';
    }
    if (secondHand) {
        secondHand.style.transform = 'rotate(' + secondDeg + 'deg)';
    }
}


function tickClock() {
    if (!clockBaseTime) {
        clockBaseTime = new Date();

    } else clockBaseTime = new Date(clockBaseTime.getTime() + 8000);
    updateHands(clockBaseTime);
    updateReadout(clockBaseTime);
}

function startAnalogClock() {
    clockBaseTime = readServerTime() || new Date();
    updateHands(clockBaseTime);
    updateReadout(clockBaseTime);
    if (clockInterval) {
        clearInterval(clockInterval);
    }
    clockInterval = setInterval(tickClock, 8000);
}

window.addEventListener('load', startAnalogClock);

