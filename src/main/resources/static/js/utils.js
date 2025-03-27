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

function regionPopulate(name) {
    fetch('/web')
        .then(response => response.text())
        .then(html => {
        let container = document.getElementById(containerId);
        if (!container) {
            console.error("Container not found:", containerId);
            return;
        }
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;
        container.innerHTML = tempDiv.innerHTML;
    })
        .catch(error => console.error('Error fetching lifter info:', error));
}

function populateShowdown(endpoint, element) {
    let targetElement = element;
    let resultsContainer = null;
    if (targetElement.closest('#resultsContainer1')) {
        resultsContainer = document.getElementById('resultsContainer1');
    } else if (targetElement.closest('#resultsContainer2')) {
        resultsContainer = document.getElementById('resultsContainer2');
    }
    if (!resultsContainer) {
        console.error("No container found for the clicked element.");
        return;
    }
    // Fetch the data from the endpoint
    fetch(endpoint)
        .then(response => response.text())
        .then(html => {
        // If the container is found, populate it
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;
        resultsContainer.innerHTML = tempDiv.innerHTML;
        const resultsCard = resultsContainer.querySelector('.results-card');
        if (resultsCard) {
            resultsCard.classList.remove('w-25');
            resultsCard.classList.add('w-100');
        } else {
            console.error('Results card not found');
        }
    })
        .catch(error => console.error('Error fetching lifter info:', error));
}

function populate(containerId, endpoint) {
    if (!endpoint.includes("showdown")) {
        let extrasContainer = document.getElementById("extrasContainer");
        if (!extrasContainer.classList.contains("hidden")) {
            extrasContainer.classList.add("hidden");
        }
    }
    fetch(endpoint)
        .then(response => response.text())
        .then(html => {
        let container = document.getElementById(containerId);
        if (!container) {
            console.error("Container not found:", containerId);
            return;
        }
        let tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;
        container.innerHTML = tempDiv.innerHTML;
        if (endpoint.includes("lifter")) {
            const observer = new MutationObserver((mutations, obs) => {
                if (document.querySelector('.filter-option') && document.getElementById('applyFilters')) {
                    chartFilters();
                    obs.disconnect(); // Stop observing after running once
                }
            });
            observer.observe(document.getElementById('chartContainer'), { childList: true, subtree: true });
            fetch('/web/chart-builder')
                .then(response => response.text())
                .then(html => {
                    let chartContainer = document.getElementById('chartContainer');
                    if (!chartContainer) {
                        console.error("Container not found:", 'chartContainer');
                        return;
                    }
                    let tempDiv2 = document.createElement('div');
                    tempDiv2.innerHTML = html;
                    chartContainer.innerHTML = tempDiv2.innerHTML;
                    chartFilters();
                })
                .catch(error => console.error('Error inserting chart-builder:', error));
        }
        })
        .catch(error => console.error('Error fetching lifter info:', error));
}

function search(searchBoxID, containerId, endpoint) {
    let query = document.getElementById(searchBoxID).value;
    if (query) {
        if (!endpoint.includes("showdown")) {
            let extrasContainer = document.getElementById("extrasContainer");
            if (!extrasContainer.classList.contains("hidden")) {
                extrasContainer.classList.add("hidden");
            }
        }
        populate(containerId, endpoint + `?query=` + encodeURIComponent(query));
    }
}

