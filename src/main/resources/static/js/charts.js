// Function to dynamically create a canvas and render a chart with Chart.js
function buildChart(chartData) {
    const container = document.getElementById('chartContainer');
    container.innerHTML = ''; // Clear any existing chart

    // Create a new canvas element
    const canvas = document.createElement('canvas');
    canvas.id = 'dynamicChart';
    container.appendChild(canvas);

    const colorPalette = [
            '#FF5733', '#3498DB', '#76D7C4', '#F1C40F', '#8E44AD',
            '#2ECC71', '#E74C3C', '#1ABC9C', '#F39C12', '#9B59B6'
        ];

    // Filter dataset keys by excluding special keys like xAxis and chartTitle.
    const datasets = Object.keys(chartData)
        .filter(key => key !== "xAxis" && key !== "chartTitle")
        .map((key, index) => ({
            label: key,
            data: chartData[key],
            borderColor: colorPalette[index % colorPalette.length],
            backgroundColor: '#ffffff',
            borderWidth: 2,
            pointStyle: 'false',
        }));

    const ctx = canvas.getContext('2d');
    new Chart(ctx, {
        type: 'line', // Change chart type as needed
        data: {
            labels: chartData.xAxis,
            datasets: datasets
        },
        options: {
            spanGaps: true,
            responsive: true,
            scales: {
                x: {
                    ticks: {
                        color: 'white'
                    },
                    title: {
                        display: true,
                        text: chartData.xAxisLabel || 'X Axis',
                        color: 'white'
                    }
                },
                y: {
                    ticks: {
                        color: 'white'
                    },
                    title: {
                        display: true,
                        text: chartData.yAxisLabel || 'Y Axis',
                        color: 'white'
                    },
                    beginAtZero: false
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: chartData.chartTitle,  // use the title from the dataset
                    color: 'white',
                    font: {
                        size: 14
                    }
                },
                legend: {
                    labels: {
                        color: 'white'
                    },
                    position: 'top'
                },
                tooltip: {
                    backgroundColor: 'black',
                    titleColor: 'white',
                    bodyColor: 'white',
                    mode: 'index',
                    intersect: true
                }
            }
        }
    });
}

function chartFilters() {
    // Toggle active state on filter-option buttons on click
    document.querySelectorAll('.filter-option').forEach(option => {
        option.addEventListener('click', function() {
            if (this.classList.contains('selected')) {
                this.classList.remove('selected');
            } else {
                this.classList.add('selected');
            }
        });
    });

    // Handle the Apply Filters button click
    document.getElementById('applyFilters').addEventListener('click', function () {
        // Gather selected column names
        const selectedFilters = [];
        document.querySelectorAll('.filter-option').forEach(btn => {
            let attr = btn.getAttribute('data-column');
            if (btn.classList.contains('selected')) {
                if (attr === 'event') {
                    selectedFilters.push('B');
                } else if (attr === 'equipment') {
                    selectedFilters.push('Single-ply');
                } else {
                    selectedFilters.push(attr);
                }
            } else {
                if (attr === 'event') {
                    selectedFilters.push('SBD');
                } else if (attr === 'equipment') {
                    selectedFilters.push('Raw');
                } else {
                    selectedFilters.push('n/a');
                }
            }
        });

        console.log('Selected filters:', selectedFilters);

        const name = document.getElementById('lifter-info').getAttribute('data-name');

        // Send the selected filters to your backend endpoint via AJAX
        fetch('/web/charts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 'filters': selectedFilters, 'name': [name] })
        })
            .then(response => response.json())
            .then(data => {
            buildChart(data);
        })
            .catch(error => console.error('Error fetching chart data:', error));
    });
};