var d3 = require("d3");
import { ClickTextData, HistogramData } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip, TooltipValues } from "../../interactions/tooltip.js";
import { groupBy, calculateMean } from "../../utils/utils.js";
import { ChartSeries, ChartPadding } from "../chartBase.js";
import { ChartElements } from "../render.js";
import { ChartSeriesAxis } from "../scaleBase.js";
export class Histogram extends ChartSeries {
    constructor(data) {
        super("histogram", data.map(d => d.group));
        this.tooltip = new Tooltip(this);
        this.padding = new ChartPadding(40, 75, 5, 85);
        this.x = new ChartSeriesAxis(this.id, "Group Code", data.map(d => d.group), [0, this.width - this.padding.yAxis - this.padding.right]);
        d3.select(`#${this.id} svg`).remove();
        this.thresholdAxis = this.y.setThresholdAxis(30, 70);
        this.elements = new HistogramChartElements(this);
        this.clicking = new ClickHistogram(this);
        this.data = data;
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        this._data = entries.filter(d => d.selected);
        this.x.scale.domain(entries.filter(d => d.selected).map(d => d.group));
        this.bandwidth = d3.scaleLinear()
            .range([0, this.x.scale.bandwidth()])
            .domain([-100, 100]);
        this.x.transition(this.data.map(d => d.group));
        this.render();
        this.extend !== undefined ? this.extend() : null;
    }
    getBinData(d) {
        const bin = d3.bin().domain([0, 100]).thresholds([0, this.elements.getThresholdsValues()[0], this.elements.getThresholdsValues()[1]]);
        const usersData = groupBy(d.value, "pseudonym").map(c => { return { "pseudonym": c.key, "mean": calculateMean(c.value.map(r => r.point)) }; });
        return bin(usersData.map(c => c.mean)).map(c => {
            return new HistogramData(d.value.filter(a => usersData.filter(r => c.includes(r.mean)).map(r => r.pseudonym).includes(a.pseudonym)), d.group, d.colour, c, Math.round(c.length / usersData.length * 100));
        });
    }
    render() {
        d3.select(`#${this.id} .card-subtitle`)
            .html(this.data.length == 1 ? `Filtering by <span class="badge badge-pill badge-info pointer">${this.data[0].group} <i class="fas fa-window-close"></i></span>` :
            "");
        //Process histogram
        this.elements.contentContainer.selectAll(`.${this.id}-histogram-container`)
            .data(this.data)
            .join(enter => enter.append("g")
            .attr("class", `${this.id}-histogram-container`)
            .attr("transform", d => `translate(${this.x.scale(d.group)}, 0)`)
            .call(enter => enter.selectAll(".histogram-rect")
            .data(d => this.getBinData(d))
            .enter()
            .append("rect")
            .attr("id", `${this.id}-data`)
            .attr("class", "histogram-rect")
            .attr("x", c => this.bandwidth(-c.percentage))
            .attr("y", c => this.y.scale(c.bin.x0))
            .attr("height", 0)
            .attr("width", c => this.bandwidth(c.percentage) - this.bandwidth(-c.percentage))
            .style("stroke", c => c.colour)
            .style("fill", c => c.colour)
            .transition()
            .duration(750)
            .attr("y", c => this.y.scale(c.bin.x1))
            .attr("height", c => this.y.scale(c.bin.x0) - this.y.scale(c.bin.x1))), update => update
            .call(update => update.selectAll(".histogram-rect")
            .data(d => this.getBinData(d))
            .join(enter => enter, update => update.style("stroke", d => d.colour)
            .style("fill", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("x", d => this.bandwidth(-d.percentage))
            .attr("y", d => this.y.scale(d.bin.x1))
            .attr("height", d => this.y.scale(d.bin.x0) - this.y.scale(d.bin.x1))
            .attr("width", d => this.bandwidth(d.percentage) - this.bandwidth(-d.percentage))), exit => exit))
            .call(update => update.transition()
            .duration(750)
            .attr("transform", d => `translate(${this.x.scale(d.group)}, 0)`)), exit => exit
            .call(exit => exit.selectAll(".histogram-rect")
            .style("fill", "#cccccc")
            .style("stroke", "#b3b3b3")
            .transition()
            .duration(250)
            .attr("y", c => this.y.scale(c.bin.x0))
            .attr("height", 0))
            .call(exit => exit.transition()
            .duration(250)
            .remove()));
        this.elements.content = this.elements.contentContainer.selectAll(`#${this.id}-data`);
        const onMouseover = (e, d) => {
            this.tooltip.appendTooltipContainer();
            let tooltipBox = this.tooltip.appendTooltipText(d.bin.x0 == 0 ? "Distressed" : d.bin.x1 == 100 ? "Soaring" : "GoingOK", [new TooltipValues("Total", `${d.bin.length} (${d.percentage}%)`)]);
            this.tooltip.positionTooltipContainer(this.x.scale(d.group) + this.bandwidth(d.bin.length), d.bin.x1 > 25 ? this.y.scale(d.bin.x1) : this.y.scale(d.bin.x0) - tooltipBox.node().getBBox().height);
        };
        const onMouseout = () => {
            this.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            this.tooltip.removeTooltip();
        };
        //Append tooltip container
        this.tooltip.enableTooltip(onMouseover, onMouseout);
        if (this.clicking.clicked) {
            const clickData = this.elements.contentContainer.select(".clicked").datum();
            this.clicking.removeClick();
            this.clicking.appendThresholdPercentages(this.data, clickData);
        }
    }
}
class HistogramChartElements extends ChartElements {
    constructor(chart) {
        super(chart);
        let thresholds = this.getThresholdsValues();
        this.appendThresholdAxis();
        this.appendThresholdIndicators(thresholds);
        this.appendThresholdLabel();
    }
    appendThresholdAxis() {
        return this.contentContainer.append("g")
            .attr("transform", `translate(${this.chart.width - this.chart.padding.yAxis - this.chart.padding.right}, 0)`)
            .attr("class", "threshold-axis")
            .call(this.chart.thresholdAxis);
    }
    ;
    appendThresholdLabel() {
        let label = this.svg.append("g")
            .attr("class", "threshold-label-container");
        label.append("text")
            .attr("class", "y-label-text")
            .attr("text-anchor", "middle")
            .text("Thresholds");
        label.attr("transform", `translate(${this.chart.width - this.chart.padding.right + this.contentContainer.select(".threshold-axis").node().getBBox().width + label.node().getBBox().height}, ${this.chart.padding.top + this.svg.select(".y-axis").node().getBBox().height / 2}) rotate(-90)`);
        return label;
    }
    ;
    appendThresholdIndicators(thresholds) {
        this.contentContainer.selectAll(".threshold-indicator-container")
            .data(thresholds)
            .enter()
            .append("g")
            .attr("class", "threshold-indicator-container")
            .classed("distressed", d => d < 50 ? true : false)
            .classed("soaring", d => d > 50 ? true : false)
            .attr("transform", d => `translate(${this.chart.width - this.chart.padding.yAxis - this.chart.padding.right + 5}, ${d < 50 ? this.chart.y.scale(d) + 25 : this.chart.y.scale(d) - 15})`)
            .call(g => g.append("rect")
            .attr("class", "threshold-indicator-box")
            .classed("distressed", d => d < 50 ? true : false)
            .classed("soaring", d => d > 50 ? true : false))
            .call(g => g.append("text")
            .attr("class", "threshold-indicator-text")
            .attr("x", 5)
            .text(d => d))
            .call(g => g.selectAll("rect")
            .attr("width", g.select("text").node().getBBox().width + 10)
            .attr("height", g.select("text").node().getBBox().height + 5)
            .attr("y", -g.select("text").node().getBBox().height));
        this.contentContainer.selectAll(".threshold-line")
            .data(thresholds)
            .enter()
            .append("line")
            .attr("class", "threshold-line")
            .classed("distressed", d => d < 50 ? true : false)
            .classed("soaring", d => d > 50 ? true : false)
            .attr("x1", 0)
            .attr("x2", this.chart.width - this.chart.padding.yAxis - this.chart.padding.right)
            .attr("y1", d => this.chart.y.scale(d))
            .attr("y2", d => this.chart.y.scale(d));
    }
    getThresholdsValues() {
        let result = [30, 70];
        let dThreshold = this.contentContainer.select(".threshold-line.distressed");
        if (!dThreshold.empty()) {
            result[0] = this.chart.y.scale.invert(parseInt(dThreshold.attr("y1")));
        }
        let sThreshold = this.contentContainer.select(".threshold-line.soaring");
        if (!sThreshold.empty()) {
            result[1] = this.chart.y.scale.invert(parseInt(sThreshold.attr("y1")));
        }
        return result;
    }
    ;
}
class ClickHistogram extends Click {
    appendThresholdPercentages(data, clickData) {
        let thresholds = this.chart.elements.getThresholdsValues();
        let tDistressed = thresholds[0];
        let tSoaring = thresholds[1];
        this.clickedBin = clickData.bin.x0 === 0 ? "distressed" : clickData.bin.x1 === 100 ? "soaring" : "going ok";
        this.chart.elements.content.classed("clicked", (d) => d.group == clickData.group && clickData.bin.length - d.bin.length == 0);
        this.chart.elements.contentContainer.selectAll(".click-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "click-container")
            .attr("transform", c => `translate(${this.chart.x.scale(c.group) + this.chart.x.scale.bandwidth() / 2}, 0)`)
            .call(enter => enter.selectAll("text")
            .data(d => this.chart.getBinData(d))
            .enter()
            .append("text")
            .attr("class", "click-text black")
            .attr("y", c => c.bin.x0 == 0 ? this.chart.y.scale(0 + tDistressed / 2) : c.bin.x1 == 100 ? this.chart.y.scale(tSoaring + (100 - tSoaring) / 2) : this.chart.y.scale(50))
            .text(c => `${c.percentage}% `)
            .append("tspan")
            .attr("class", c => this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[0])
            .text(c => c.group != clickData.group && c.bin.x0 == clickData.bin.x0 && c.bin.x1 == clickData.bin.x1 ? `(${this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[1]})` : "")), update => update.call(update => update.transition()
            .duration(750)
            .attr("transform", c => `translate(${this.chart.x.scale(c.group) + this.chart.x.scale.bandwidth() / 2}, 0)`))
            .call(update => update.selectAll("text")
            .data(d => this.chart.getBinData(d))
            .join(enter => enter, update => update.attr("y", c => c.bin.x0 == 0 ? this.chart.y.scale(0 + tDistressed / 2) : c.bin.x1 == 100 ? this.chart.y.scale(tSoaring + (100 - tSoaring) / 2) : this.chart.y.scale(50))
            .text(c => `${c.percentage}% `)
            .append("tspan")
            .attr("class", c => this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[0])
            .text(c => c.group != clickData.group && c.bin.x0 == clickData.bin.x0 && c.bin.x1 == clickData.bin.x1 ? `(${this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[1]})` : ""), exit => exit)), exit => exit.remove());
    }
}
