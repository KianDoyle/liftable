window.onload = () => {
    const delay = ms => new Promise(res => setTimeout(res, ms));
    document.getElementById('compare-button').addEventListener('click', async () => {
        const card = document.getElementById('showdown-card');
        card.classList.add('highlight');
        await delay(2800);
        card.classList.remove('highlight');
    });
    document.getElementById('regions-button').addEventListener('click', async () => {
        const card = document.getElementById('regions-card');
        card.classList.add('highlight');
        await delay(2800);
        card.classList.remove('highlight');
    });
}

function swapTable(index) {
    const buttons = document.querySelectorAll(".button-wrapper");
    const tables = document.querySelectorAll(".table-wrapper");
    buttons.forEach((button, i) => {
        if (i === index) {
            button.classList.add("active");
            button.children[1].classList.add("active");
            tables[i].classList.remove("hidden");
        } else {
            button.classList.remove("active");
            button.children[1].classList.remove("active");
            tables[i].classList.add("hidden");
        }
    });
}

function toggleSidebar() {
    document.getElementById("sidebar").classList.toggle("expanded");
}
