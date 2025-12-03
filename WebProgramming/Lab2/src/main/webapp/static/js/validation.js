const yInput = document.getElementById("y-input");
const yError = document.getElementById("y-err");
const form = document.querySelector(".input-card form");
const xContainer = document.querySelector('.pill-group');
const xError = document.getElementById("x-err");
const getXInputs = () => document.querySelectorAll('input[name="x"]');

function showYError(message) {
    yError.textContent = message;
    yError.classList.add('show');
    yInput.classList.add('input-error');
}

function hideYError() {
    yError.textContent = "";
    yError.classList.remove('show');
    yInput.classList.remove('input-error');
}

function validateY(fullCheck = true) {
    hideYError();

    const y =  yInput.value.trim();

    if (fullCheck && y === "") {
        showYError("Поле Y должно быть заполнено");
        return false;
    }

    if (!fullCheck && y === "") {
        return true
    }

    const value = parseFloat(y);

    if (isNaN(value) || value < -3 || value > 5) {
        showYError("Пожалуйста введите в поле Y валидные данные!");
        return false;
    }

    return true;

}



function validateX() {
    if (!xError) return true;
    xError.textContent = "";
    xError.classList.remove('show');

    const checked = Array.from(getXInputs()).filter((input) => input.checked);
    if (checked.length === 0) {
        xError.textContent = "Выберите хотя бы одно значение X";
        xError.classList.add('show');
        return false;
    }
    return true;
}



function checkSubmitForm() {
    form.addEventListener('submit', (event) => {
        const okY = validateY();
        const okX = validateX();
        if (!okY || !okX) {
            event.preventDefault();
        }

    });

    yInput.addEventListener('input', () => {
        validateY(false);
    });

    yInput.addEventListener('focus', () => {
        yInput.classList.remove('input-error');
    })
}

checkSubmitForm();



xContainer.addEventListener('change', (event) => {
    const target = event.target;
    if (target.matches('input[name="x"]')) {
        validateX();
    }
});

