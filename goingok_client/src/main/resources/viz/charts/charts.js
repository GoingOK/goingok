var d3 = require("d3");
import { ChartSeriesAxis, ChartTimeAxis, ChartLinearAxis } from "./scales.js";
import { Help } from "./help.js";
import { ChartPadding, ChartElements, HistogramChartElements } from "./render.js";
export class ChartSeries {
    constructor(id, domain, isGoingOk = true, yDomain) {
        this.id = id;
        let containerDimensions = d3.select(`#${id} .chart-container`).node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = containerDimensions.height;
        this.padding = new ChartPadding();
        if (!isGoingOk) {
            this.padding.yAxis = 40;
        }
        this.y = new ChartLinearAxis(isGoingOk ? "Reflection Point" : "", isGoingOk ? [0, 100] : yDomain, [this.height - this.padding.xAxis - this.padding.top, 0], "left", isGoingOk);
        this.x = new ChartSeriesAxis("Group Code", domain, [0, this.width - this.padding.yAxis - this.padding.right]);
        this.click = false;
        this.elements = new ChartElements(this);
    }
}
export class ChartTime {
    constructor(id, domain, chartPadding) {
        this.id = id;
        let containerDimensions = d3.select(`#${id} .chart-container`).node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = containerDimensions.height;
        this.padding = chartPadding !== undefined ? chartPadding : new ChartPadding(75, 75, 5);
        this.help = new Help();
        this.y = new ChartLinearAxis("Reflection Point", [0, 100], [this.height - this.padding.xAxis - this.padding.top, 0], "left");
        this.x = new ChartTimeAxis("Time", domain, [0, this.width - this.padding.yAxis]);
        this.click = false;
        this.elements = new ChartElements(this);
    }
}
export class ChartTimeZoom {
    constructor(chart, domain) {
        this.x = new ChartTimeAxis("", domain, [0, chart.width - chart.padding.yAxis - 5]);
        this.y = new ChartLinearAxis("", [0, 100], [25, 0], "left");
    }
}
export class UserChart {
    constructor(id, containerClass) {
        this.id = id;
        let containerDimensions = d3.select(`#${id} .${containerClass}`).node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = containerDimensions.height;
        this.padding = new ChartPadding(40, 55, 10, 10);
        this.y = new ChartSeriesAxis("", ["distressed", "going ok", "soaring"], [this.height - this.padding.xAxis - this.padding.top, 0], "left");
        this.x = new ChartLinearAxis("", [0, 100], [0, this.width - this.padding.yAxis - this.padding.right], "bottom", false);
        this.x.axis.tickValues([0, 25, 50, 75, 100]);
        this.click = false;
        this.elements = new ChartElements(this, containerClass);
    }
}
export class HistogramChartSeries extends ChartSeries {
    constructor(id, domain) {
        super(id, domain);
        this.padding = new ChartPadding(40, 75, 5, 85);
        this.x = new ChartSeriesAxis("Group Code", domain, [0, this.width - this.padding.yAxis - this.padding.right]);
        d3.select(`#${this.id} svg`).remove();
        this.thresholdAxis = this.y.setThresholdAxis(30, 70);
        this.elements = new HistogramChartElements(this);
    }
    setBandwidth(data) {
        this.bandwidth = d3.scaleLinear()
            .range([0, this.x.scale.bandwidth()])
            .domain([-100, 100]);
    }
    ;
    setBin() {
        this.bin = d3.bin().domain([0, 100]).thresholds([0, this.elements.getThresholdsValues(this)[0], this.elements.getThresholdsValues(this)[1]]);
    }
}
export class ChartTimeNetwork extends ChartTime {
    constructor(id, domain, chartPadding) {
        super(id, domain, chartPadding);
        this.x.scale.domain([this.addDays(d3.min(domain), -30), this.addDays(d3.max(domain), 30)]);
        this.elements.xAxis.call(this.x.axis);
    }
    addDays(date, days) {
        let result = new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }
}
// Basic class for network chart timeline
export class ChartNetwork {
    constructor(id, containerClass, domain) {
        this.id = id;
        let containerDimensions = d3.select(`#${id} .${containerClass}`).node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = containerDimensions.height;
        this.padding = new ChartPadding(30, 10, 10, 10);
        this.y = new ChartLinearAxis("", [0, 100], [this.height - this.padding.xAxis - this.padding.top, 0], "left");
        this.x = new ChartTimeAxis("", [this.addDays(d3.min(domain), -30), this.addDays(d3.max(domain), 30)], [0, this.width - this.padding.yAxis - this.padding.right]);
        this.click = false;
        this.elements = new ChartElements(this, containerClass);
        this.elements.yAxis.remove();
        this.elements.xAxis.remove();
    }
    addDays(date, days) {
        let result = new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }
    resetZoomRange() {
        this.x.scale.range([0, this.width - this.padding.yAxis - this.padding.right]);
        d3.zoom().transform(this.elements.contentContainer.select(".zoom-rect"), d3.zoomIdentity);
        this.x.axis.ticks((this.width - this.padding.yAxis - this.padding.right) / 75);
        this.elements.xAxis.transition().duration(750).call(this.x.axis);
    }
}
