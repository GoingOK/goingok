var d3 = require("d3");
import { TimelineData } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip, TooltipValues } from "../../interactions/tooltip.js";
import { Zoom } from "../../interactions/zoom.js";
import { maxDate, minDate } from "../../utils/utils.js";
import { ChartTime } from "../chartBase.js";
import { ChartTimeAxis, ChartLinearAxis } from "../scaleBase.js";
export class Timeline extends ChartTime {
    constructor(data) {
        super("timeline", [minDate(data.map(d => minDate(d.value.map(c => c.timestamp)))), maxDate(data.map(d => maxDate(d.value.map(c => c.timestamp))))]);
        this.tooltip = new Tooltip(this);
        this.zoom = new Zoom(this);
        this.zoomChart = new ChartTimeZoom(this, [this.minTimelineDate(data), this.maxTimelineDate(data)]);
        this.clicking = new ClickTimeline(this);
        this.data = data;
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        this._data = entries.filter(d => d.selected);
        this.zoomChart.x.scale.domain([this.minTimelineDate(), this.maxTimelineDate()]);
        this.x.transition([minDate(this.data.map(d => minDate(d.value.map(c => c.timestamp)))), maxDate(this.data.map(d => maxDate(d.value.map(c => c.timestamp))))]);
        if (this.clicking.clicked) {
            this.clicking.removeClick();
        }
        this.render();
        this.extend !== undefined ? this.extend() : null;
    }
    render() {
        let _this = this;
        if (_this.data.length == 0) {
            d3.selectAll(`#${_this.id} .card-subtitle span`)
                .html("");
        }
        else {
            d3.select(`#${_this.id} .card-subtitle .instructions`)
                .html(_this.data.length == 1 ? `Filtering by <span class="badge badge-pill badge-info pointer">${_this.data[0].group} <i class="fas fa-window-close"></i></span>` : null);
            d3.select(`#${_this.id} .card-subtitle .text-muted`)
                .html(`The oldest reflection was on ${_this.minTimelineDate().toDateString()}${_this.data.length != 1 ? ` in the group code ${_this.data[d3.minIndex(_this.data.map(d => d3.min(d.value.map(d => d.timestamp))))].group}` : ""}, while
                    the newest reflection was on ${_this.maxTimelineDate().toDateString()}${_this.data.length != 1 ? ` in the group code ${_this.data[d3.maxIndex(_this.data.map(d => d3.max(d.value.map(d => d.timestamp))))].group}` : ""}`);
        }
        //Draw circles
        _this.elements.contentContainer.selectAll(".timeline-container")
            .data(_this.data)
            .join(enter => enter.append("g")
            .attr("class", "timeline-container")
            .call(enter => _this.scatter(enter, _this)), update => update.call(update => _this.scatter(update, _this)), exit => exit.remove());
        _this.elements.content = _this.elements.contentContainer.selectAll(".circle");
        const onMouseover = function (e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.tooltip.appendTooltipContainer();
            let tooltipBox = _this.tooltip.appendTooltipText(d.timestamp.toDateString(), [new TooltipValues("User", d.pseudonym),
                new TooltipValues("Point", d.point)]);
            _this.tooltip.positionTooltipContainer(xTooltip(d.timestamp, tooltipBox), yTooltip(d.point, tooltipBox));
            function xTooltip(x, tooltipBox) {
                let xTooltip = _this.x.scale(x);
                if (_this.width - _this.padding.yAxis < xTooltip + tooltipBox.node().getBBox().width) {
                    return xTooltip - tooltipBox.node().getBBox().width;
                }
                return xTooltip;
            }
            ;
            function yTooltip(y, tooltipBox) {
                var yTooltip = _this.y.scale(y) - tooltipBox.node().getBBox().height - 10;
                if (yTooltip < 0) {
                    return yTooltip + tooltipBox.node().getBBox().height + 20;
                }
                return yTooltip;
            }
            ;
            _this.tooltip.appendLine(0, _this.y.scale(d.point), _this.x.scale(d.timestamp), _this.y.scale(d.point), d.colour);
            _this.tooltip.appendLine(_this.x.scale(d.timestamp), _this.y.scale(0), _this.x.scale(d.timestamp), _this.y.scale(d.point), d.colour);
        };
        const onMouseout = function () {
            _this.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            _this.tooltip.removeTooltip();
        };
        //Enable tooltip       
        _this.tooltip.enableTooltip(onMouseover, onMouseout);
        //Append zoom bar
        if (_this.elements.zoomSVG == undefined) {
            _this.elements.zoomSVG = _this.zoom.appendZoomBar();
            _this.elements.zoomFocus = _this.elements.zoomSVG.append("g")
                .attr("class", "zoom-focus");
        }
        //Process zoom circles
        _this.elements.zoomFocus.selectAll(".zoom-timeline-content-container")
            .data(_this.data)
            .join(enter => enter.append("g")
            .attr("class", "zoom-timeline-content-container")
            .call(enter => _this.scatter(enter, _this.zoomChart, true, true)), update => update.call(update => _this.scatter(update, _this.zoomChart, true, true)), exit => exit.remove());
        _this.elements.zoomSVG.selectAll(".zoom-timeline-container")
            .data(_this.data)
            .join(enter => enter.append("g")
            .attr("class", "zoom-timeline-container")
            .call(enter => { _this.zoomChart.x.scale.rangeRound([0, _this.width - _this.padding.yAxis]); _this.scatter(enter, _this.zoomChart, true); }), update => update.call(update => { _this.zoomChart.x.scale.rangeRound([0, _this.width - _this.padding.yAxis]); _this.scatter(update, _this.zoomChart, true); }), exit => exit.remove());
        //Enable zoom
        const zoomed = function (e) {
            if (e.sourceEvent !== null) {
                if (e.sourceEvent.type === "dblclick" || e.sourceEvent.type === "wheel")
                    return;
            }
            let newChartRange = [0, _this.width - _this.padding.yAxis].map(d => e.transform.applyX(d));
            _this.x.scale.rangeRound(newChartRange);
            _this.zoomChart.x.scale.rangeRound([0, _this.width - _this.padding.yAxis - 5].map(d => e.transform.invertX(d)));
            let newLine = d3.line()
                .x(d => _this.x.scale(d.timestamp))
                .y(d => _this.y.scale(d.point));
            _this.elements.contentContainer.selectAll(".circle")
                .attr("cx", d => _this.x.scale(d.timestamp));
            _this.elements.zoomFocus.selectAll(".zoom-content")
                .attr("cx", d => _this.zoomChart.x.scale(d.timestamp));
            _this.elements.contentContainer.selectAll(".click-line")
                .attr("d", d => newLine(d));
            _this.elements.contentContainer.selectAll(".click-container")
                .attr("transform", d => `translate(${_this.x.scale(d.timestamp)}, ${_this.y.scale(d.point)})`);
            _this.x.axis.ticks(newChartRange[1] / 75);
            _this.elements.xAxis.call(_this.x.axis);
            _this.help.removeHelp(_this);
        };
        _this.zoom.enableZoom(zoomed);
    }
    scatter(update, chart, zoom = false, invisible = false) {
        update.selectAll("circle")
            .data(d => d.value.map(c => new TimelineData(c, d.colour, d.group)))
            .join(enter => enter.append("circle")
            .attr("class", invisible ? "zoom-content" : zoom ? "circle no-hover" : "circle")
            .attr("r", zoom ? 2 : 5)
            .attr("cx", d => chart.x.scale(d.timestamp))
            .attr("cy", d => chart.y.scale(d.point))
            .attr("fill", d => d.colour)
            .attr("stroke", d => d.colour), update => update.attr("fill", d => d.colour)
            .attr("stroke", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("cx", d => chart.x.scale(d.timestamp))
            .attr("cy", d => chart.y.scale(d.point))), exit => exit.remove());
    }
    ;
    minTimelineDate(data) {
        const processData = data === undefined ? this.data : data;
        return minDate(processData.map(d => minDate(d.value.map(c => c.timestamp))));
    }
    maxTimelineDate(data) {
        const processData = data === undefined ? this.data : data;
        return maxDate(processData.map(d => maxDate(d.value.map(c => c.timestamp))));
    }
}
class ChartTimeZoom {
    constructor(chart, domain) {
        this.x = new ChartTimeAxis("zoom-container", "", domain, [0, chart.width - chart.padding.yAxis - 5]);
        this.y = new ChartLinearAxis("zoom-container", "", [0, 100], [25, 0], "left");
    }
}
class ClickTimeline extends Click {
    appendScatterText(d, title, values = null) {
        let container = this.chart.elements.contentContainer.append("g")
            .datum(d)
            .attr("class", "click-container");
        let box = container.append("rect")
            .attr("class", "click-box");
        let text = container.append("text")
            .attr("class", "click-text title")
            .attr("x", 10)
            .text(title);
        let textSize = text.node().getBBox().height;
        text.attr("y", textSize);
        if (values != null) {
            values.forEach((c, i) => {
                text.append("tspan")
                    .attr("class", "click-text")
                    .attr("y", textSize * (i + 2))
                    .attr("x", 15)
                    .text(`${c.label}: ${c.value}`);
            });
        }
        box.attr("width", text.node().getBBox().width + 20)
            .attr("height", text.node().getBBox().height + 5)
            .attr("clip-path", `url(#clip-${this.chart.id})`);
        container.attr("transform", this.positionClickContainer(box, text, d));
    }
    ;
    positionClickContainer(box, text, d) {
        let positionX = this.chart.x.scale(d.timestamp);
        let positionY = this.chart.y.scale(d.point) - box.node().getBBox().height - 10;
        if (this.chart.width - this.chart.padding.yAxis < this.chart.x.scale(d.timestamp) + text.node().getBBox().width) {
            positionX = this.chart.x.scale(d.timestamp) - box.node().getBBox().width;
        }
        ;
        if (this.chart.y.scale(d.point) - box.node().getBBox().height - 10 < 0) {
            positionY = positionY + box.node().getBBox().height + 20;
        }
        ;
        return `translate(${positionX}, ${positionY})`;
    }
    ;
}
