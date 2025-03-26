function drawChart(containerId, chartData) {
    const xAxis = chartData.xAxis;
    const colorPalette = [
        '#FF5733', // Bright red
        '#3498DB', // Bright blue
        '#76D7C4', // Teal
        '#F1C40F', // Yellow
        '#8E44AD', // Purple
        '#2ECC71', // Green
        '#E74C3C', // Light red
        '#1ABC9C', // Aqua green
        '#F39C12', // Orange
        '#9B59B6', // Violet
    ];
    const datasets = Object.keys(chartData)
        .filter(key => key.startsWith("lift"))
        .map((key, index) => ({
            label: key,
            data: chartData[key],
            borderColor: colorPalette[index % colorPalette.length],
            backgroundColor: '#ffffff',
            borderWidth: 2,
        }));

    new Chart(document.getElementById(containerId).getContext('2d'), {
        type: 'line',
        data: {
            labels: xAxis,
            datasets: datasets,
        },
        options: {
            spanGaps: true,
            responsive: true,
            scales: {
                x: {
                    ticks: {
                        color: 'white', // Text color for x-axis ticks
                    }, // <--- Added missing comma here
                    title: {
                        display: true,
                        text: 'Competition Dates',
                        color: 'white', // Text color for x-axis title
                    },
                },
                y: {
                    ticks: {
                        color: 'white', // Text color for y-axis ticks
                    },
                    title: {
                        display: true,
                        text: 'Lift Progression (kg)',
                        color: 'white', // Text color for y-axis title
                    },
                    beginAtZero: false, // Ensure y-axis starts at 0
                },
            },
            plugins: {
                title: {
                    display: true,
                    text: 'Lifter Progression Over Time',
                    color: 'white',
                    font: {
                        size: 14, // Title font size
                    },
                }, // <--- Added missing comma here
                legend: {
                    labels: {
                        color: 'white', // Legend text color
                    },
                    position: 'top', // Legend position
                },
                tooltip: {
                    backgroundColor: 'black', // Tooltip background color
                    titleColor: 'white', // Tooltip title text color
                    bodyColor: 'white', // Tooltip body text color
                    mode: 'index',
                    intersect: true, // Tooltip displays for intersecting points only
                },
            },
        },
    });

}
