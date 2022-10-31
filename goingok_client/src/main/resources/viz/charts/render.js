var d3 = require("d3");
class ChartElementsContainers {
    constructor(chart, containerClass) {
        this.chart = chart;
        this.svg = this.appendSVG(containerClass);
        this.contentContainer = this.appendContentContainer();
    }
    appendSVG(containerClass) {
        return d3.select(`#${this.chart.id} ${containerClass == undefined ? ".chart-container" : "." + containerClass}`)
            .append("svg")
            .attr("class", "chart-svg")
            .attr("preserveAspectRatio", "xMinYMin meet")
            .attr("viewBox", `0 0 ${this.chart.width} ${this.chart.height}`);
    }
    appendContentContainer() {
        let result = this.svg.append("g")
            .attr("class", "content-container")
            .attr("transform", `translate(${this.chart.padding.yAxis}, ${this.chart.padding.top})`)
            .attr("clip-path", `url(#clip-${this.chart.id})`);
        result.append("rect")
            .attr("class", "zoom-rect")
            .attr("width", this.chart.width - this.chart.padding.yAxis - this.chart.padding.right)
            .attr("height", this.chart.height - this.chart.padding.xAxis - this.chart.padding.top);
        result.append("clipPath")
            .attr("id", `clip-${this.chart.id}`)
            .append("rect")
            .attr("x", 1)
            .attr("width", this.chart.width - this.chart.padding.yAxis)
            .attr("height", this.chart.height - this.chart.padding.xAxis - this.chart.padding.top);
        return result;
    }
}
export class ChartElements extends ChartElementsContainers {
    constructor(chart, containerClass) {
        super(chart, containerClass);
        this.xAxis = this.appendXAxis();
        this.appendXAxisLabel();
        this.yAxis = this.appendYAxis();
        this.appendYAxisLabel();
    }
    appendXAxis() {
        return this.svg.append("g")
            .attr("transform", `translate(${this.chart.padding.yAxis}, ${this.chart.height - this.chart.padding.xAxis})`)
            .attr("class", "x-axis")
            .attr("clip-path", `url(#clip-${this.chart.id})`)
            .call(this.chart.x.axis);
    }
    ;
    appendXAxisLabel() {
        return this.svg.append("g")
            .attr("class", "x-label-container")
            .attr("transform", "translate(" + (this.svg.select(".x-axis").node().getBBox().width / 2 + this.chart.padding.yAxis) + ", " + (this.chart.height - this.chart.padding.xAxis + this.svg.select(".x-axis").node().getBBox().height * 2) + ")")
            .append("text")
            .attr("class", "x-label-text")
            .attr("text-anchor", "middle")
            .text(this.chart.x.label);
    }
    ;
    appendYAxis() {
        return this.svg.append("g")
            .attr("transform", `translate(${this.chart.padding.yAxis}, ${this.chart.padding.top})`)
            .attr("class", "y-axis")
            .call(this.chart.y.axis);
    }
    ;
    appendYAxisLabel() {
        return this.svg.append("g")
            .attr("class", "y-label-container")
            .attr("transform", "translate(" + (this.chart.padding.yAxis - this.svg.select(".y-axis").node().getBBox().width) + ", " + (this.chart.padding.top + this.svg.select(".y-axis").node().getBBox().height / 2) + ") rotate(-90)")
            .append("text")
            .attr("class", "y-label-text")
            .attr("text-anchor", "middle")
            .text(this.chart.y.label);
    }
}
