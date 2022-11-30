var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var d3 = require("d3");
import { Click } from "../../interactions/click.js";
import { Tooltip, TooltipValues } from "../../interactions/tooltip.js";
import { addDays, calculateMean, groupBy, maxDate, minDate } from "../../utils/utils.js";
import { ChartPadding, ChartTime } from "../chartBase.js";
export class TimelineNetwork extends ChartTime {
    constructor(data) {
        super("timeline", [addDays(minDate(data.map(d => d.timestamp)), -30), addDays(maxDate(data.map(d => d.timestamp)), 30)], new ChartPadding(40, 75, 10, 10));
        this.tooltip = new Tooltip(this);
        this.clicking = new ClickTimelineNetwork(this);
        this.data = data.map(c => {
            this.simulation(c);
            return c;
        });
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        (() => __awaiter(this, void 0, void 0, function* () {
            this.loading.isLoading = true;
            this._data = entries.map(c => {
                c.nodes = c.nodes.filter(d => d.selected);
                c.nodes.forEach(d => d.index === undefined ? this.simulation(c) : d);
                return c;
            });
            this.x.scale.domain([addDays(minDate(entries.map(d => d.timestamp)), -30), addDays(maxDate(entries.map(d => d.timestamp)), 30)]);
            this.x.transition([addDays(minDate(entries.map(d => d.timestamp)), -30), addDays(maxDate(entries.map(d => d.timestamp)), 30)]);
            try {
                yield this.render();
            }
            catch (e) {
                this.renderError(e);
            }
            this.extend !== undefined ? this.extend() : null;
            this.loading.isLoading = false;
        }))();
    }
    render() {
        return __awaiter(this, void 0, void 0, function* () {
            const _this = this;
            _this.elements.contentContainer.selectAll(".timeline-line-container")
                .data(_this.getLines())
                .join(enter => enter.append("g")
                .attr("class", "timeline-line-container")
                .call(enter => enter.append("path")
                .datum(d => d)
                .attr("class", d => d.name)
                .attr("d", d => d.line(d.datum))), update => update.select("path")
                .call(update => update.transition()
                .duration(750)
                .attr("d", d => d.line(d.datum))), exit => exit.remove());
            _this.elements.contentContainer.selectAll(".circle-tag-container")
                .data(_this.data)
                .join(enter => enter.append("g")
                .attr("class", "circle-tag-container")
                .call(enter => enter.append("circle")
                .attr("class", "circle")
                .attr("r", 5)
                .style("fill", "#999999")
                .style("stroke", "#999999"))
                .call(enter => _this.renderReflectionNetwork(enter))
                .call(enter => enter.transition()
                .duration(750)
                .attr("transform", d => `translate (${_this.x.scale(d.timestamp)}, ${_this.y.scale(d.point)})`)), update => update.call(update => update.transition()
                .duration(750)
                .attr("transform", d => `translate (${_this.x.scale(d.timestamp)}, ${_this.y.scale(d.point)})`)
                .style("fill", "#999999")
                .style("stroke", "#999999"))
                .call(update => _this.renderReflectionNetwork(update)), exit => exit.remove());
            _this.elements.content = _this.elements.contentContainer.selectAll(".circle");
            const onMouseover = function () {
                if (d3.select(this).attr("class").includes("clicked"))
                    return;
                _this.tooltip.appendTooltipContainer();
                const parentData = d3.select(d3.select(this).node().parentElement).datum();
                let tooltipValues = [new TooltipValues("Point", parentData.point)];
                let tags = groupBy(_this.data.find(c => c.refId === parentData.refId).nodes, "name").map(c => { return { "name": c.key, "total": c.value.length }; });
                tags.forEach(c => {
                    tooltipValues.push(new TooltipValues(c.name, c.total));
                });
                let tooltipBox = _this.tooltip.appendTooltipText(parentData.timestamp.toDateString(), tooltipValues);
                _this.tooltip.positionTooltipContainer(xTooltip(parentData.timestamp, tooltipBox), yTooltip(parentData.point, tooltipBox));
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
                d3.select(this).attr("r", 10);
                _this.tooltip.appendLine(0, _this.y.scale(parentData.point), _this.x.scale(parentData.timestamp) - 10, _this.y.scale(parentData.point), "#999999");
                _this.tooltip.appendLine(_this.x.scale(parentData.timestamp), _this.y.scale(0), _this.x.scale(parentData.timestamp), _this.y.scale(parentData.point) + 10, "#999999");
            };
            const onMouseout = function () {
                _this.elements.svg.select(".tooltip-container").transition()
                    .style("opacity", 0);
                _this.tooltip.removeTooltip();
                if (d3.select(this).attr("class").includes("clicked"))
                    return;
                d3.select(this).attr("r", 5);
            };
            //Enable tooltip       
            _this.tooltip.enableTooltip(onMouseover, onMouseout);
        });
    }
    getLines() {
        const hardLine = d3.line()
            .x(d => this.x.scale(d.timestamp))
            .y(d => this.y.scale(d.point))
            .curve(d3.curveMonotoneX);
        const softLine = d3.line()
            .x(d => this.x.scale(d.timestamp))
            .y(d => this.y.scale(d.point))
            .curve(d3.curveBasis);
        const mean = calculateMean(this.data.map(d => d.point));
        const meanLine = d3.line()
            .x(d => this.x.scale(d.timestamp))
            .y(this.y.scale(mean));
        return [
            { "name": "hardline", "line": hardLine, "datum": d3.sort(this.data, d => d.timestamp) },
            { "name": "softline", "line": softLine, "datum": d3.sort(this.data, d => d.timestamp) },
            { "name": "meanline", "line": meanLine, "datum": d3.sort(this.data, d => d.timestamp) }
        ];
    }
    renderReflectionNetwork(enter) {
        enter.selectAll(".circle-tag")
            .data(d => d.nodes)
            .join(enter => enter.append("circle")
            .attr("class", "circle-tag")
            .attr("cx", d => d.x)
            .attr("cy", d => d.y)
            .attr("r", 5)
            .style("fill", d => d.properties["color"])
            .style("stroke", d => d.properties["color"]), update => update.call(update => update.transition()
            .duration(750)
            .attr("cx", d => d.x)
            .attr("cy", d => d.y)
            .style("fill", d => d.properties["color"])
            .style("stroke", d => d.properties["color"])), exit => exit.remove());
    }
    simulation(reflection) {
        let simulation = d3.forceSimulation(reflection.nodes)
            .force("collide", d3.forceCollide().radius(5))
            .force("forceRadial", d3.forceRadial(0, 0).radius(15));
        const centerY = this.y.scale(reflection.point);
        const centerX = this.x.scale(reflection.timestamp);
        if (centerY < 20) {
            simulation.force("forceY", d3.forceY(20).strength(0.25));
        }
        if (this.height - this.padding.top - this.padding.xAxis - 20 < centerY) {
            simulation.force("forceY", d3.forceY(-20).strength(0.25));
        }
        if (centerX < 20) {
            simulation.force("forceX", d3.forceX(20).strength(0.25));
        }
        if (this.width - this.padding.yAxis - this.padding.right - 20 < centerX) {
            simulation.force("forceX", d3.forceX(-20).strength(0.25));
        }
        simulation.tick(300);
    }
}
class ClickTimelineNetwork extends Click {
    removeClick() {
        super.removeClick();
        this.chart.elements.content
            .attr("r", 5);
    }
}
