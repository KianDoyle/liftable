function drawChart(containerId, chartData) {
    const xAxis = chartData.xAxis;
    // Prepare a color palette for the datasets.
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
        pointStyle: 'false'
    }));

    // Create the chart instance.
    new Chart(document.getElementById(containerId).getContext('2d'), {
        type: 'line',
        data: {
            labels: xAxis,
            datasets: datasets
        },
        options: {
            spanGaps: true,
            responsive: true,
            scales: {
                x: {
                    ticks: { color: 'white' },
                    title: { display: true, text: 'Competition Dates', color: 'white' }
                },
                y: {
                    ticks: { color: 'white' },
                    title: { display: true, text: 'Lift Progression (kg)', color: 'white' },
                    beginAtZero: false
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: chartData.chartTitle,  // use the title from the dataset
                    color: 'white',
                    font: { size: 14 }
                },
                legend: {
                    labels: { color: 'white' },
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