;
export class Transitions {
    axisSeries(chart, data) {
        chart.x.scale.domain(data.map(d => d.group));
        return d3.select(`#${chart.id} .x-axis`).transition()
            .duration(750)
            .call(chart.x.axis);
    }
    ;
    axisTime(chart, data) {
        chart.x.scale.domain([d3.min(data.map(d => d3.min(d.value.map(d => d.timestamp)))), d3.max(data.map(d => d3.max(d.value.map(d => d.timestamp))))]);
        d3.select(`#${chart.id} .x-axis`).transition()
            .duration(750)
            .call(chart.x.axis);
    }
    ;
    axisLinear(chart) {
        d3.select(`#${chart.id} .y-axis`).transition()
            .duration(750)
            .call(chart.y.axis);
    }
    ;
}
