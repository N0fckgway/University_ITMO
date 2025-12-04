const clearButton = document.getElementById('clear-button');
const tableBody = document.getElementById('table-results');
const pageSizeSelect = document.getElementById('page-size');
const prevButton = document.getElementById('prev-page');
const nextButton = document.getElementById('next-page');
const pageNumber = document.getElementById('page-number');

let allRows = [];
let currentPage = 1;
let pageSize = parseInt(pageSizeSelect?.value || '5', 10);

async function fetchClearTable() {
    try {
        const request = await fetch('areaCheck?action=clear');
        if (!request.ok) {
            throw new Error(`Http Error! status ${request.status}`);
        }

        while (tableBody.firstChild) {
            tableBody.removeChild(tableBody.firstChild);
        }

        refreshPagination();
        if (typeof clearCanvasPoints === 'function') {
            clearCanvasPoints();
        }
    } catch (error) {
        console.error("Ошибка при выполнении запроса для очистки таблицы" + error);
    }
}

function collectRows() {
    allRows = Array.from(tableBody.querySelectorAll('tr'));
}

function renderPage() {
    const totalPages = Math.max(1, Math.ceil(allRows.length / pageSize));
    if (currentPage > totalPages) currentPage = totalPages;

    allRows.forEach((row, index) => {
        const page = Math.floor(index / pageSize) + 1;
        row.style.display = page === currentPage ? '' : 'none';
    });

    if (pageNumber) pageNumber.textContent = String(currentPage);
    if (prevButton) prevButton.disabled = currentPage <= 1;
    if (nextButton) nextButton.disabled = currentPage >= totalPages || allRows.length === 0;
}

function refreshPagination() {
    collectRows();
    renderPage();
}

pageSizeSelect.addEventListener('change', () => {
    const size = parseInt(pageSizeSelect.value, 10);
    if (!Number.isNaN(size) && size > 0) {
        pageSize = size;
        currentPage = 1;
        renderPage();
    }
});

prevButton.addEventListener('click', (event) => {
    event.preventDefault();
    if (currentPage > 1) {
        currentPage -= 1;
        renderPage();
    }
});

nextButton.addEventListener('click', (event) => {
    event.preventDefault();
    const totalPages = Math.max(1, Math.ceil(allRows.length / pageSize));
    if (currentPage < totalPages) {
        currentPage += 1;
        renderPage();
    }
});

clearButton.addEventListener('click', (event) => {
    event.preventDefault();
    fetchClearTable();
    
});

document.addEventListener('DOMContentLoaded', () => {
    refreshPagination();
});
