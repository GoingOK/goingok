var d3 = require("d3");
import { ClickTextData } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip, TooltipValues } from "../../interactions/tooltip.js";
import { ChartSeries } from "../chartBase.js";
export class BarChart extends ChartSeries {
    constructor(data) {
        super("users", data.map(d => d.group), false, data.map(d => d.getStat("usersTotal").value));
        this.tooltip = new Tooltip(this);
        this.clicking = new ClickBarChart(this);
        this.data = data;
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        this._data = entries.filter(d => d.selected);
        if (this.data.length != 0) {
            this.x.transition(this.data.map(d => d.group));
            this.y.transition([0, d3.max(this.data.map(d => d.getStat("usersTotal").value))]);
        }
        this.render();
        this.extend !== undefined ? this.extend() : null;
    }
    render() {
        let _this = this;
        d3.select(`#${_this.id} .card-title span`)
            .html();
        d3.select(`#${_this.id} .card-subtitle`)
            .html(_this.data.length <= 1 ? "Add more group codes from the left bar" : "Click a group code to filter");
        //Boxes processing
        _this.elements.content = _this.elements.contentContainer.selectAll(`#${_this.id}-data`)
            .data(_this.data)
            .join(enter => enter.append("rect")
            .attr("id", `${_this.id}-data`)
            .attr("class", "bar")
            .attr("y", d => _this.y.scale(0))
            .attr("x", d => _this.x.scale(d.group))
            .attr("width", _this.x.scale.bandwidth())
            .attr("height", 0)
            .style("stroke", d => d.colour)
            .style("fill", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("height", d => _this.y.scale(0) - _this.y.scale(d.getStat("usersTotal").value))
            .attr("y", d => _this.y.scale(d.getStat("usersTotal").value))), update => update.style("stroke", d => d.colour)
            .style("fill", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("y", d => _this.y.scale(d.getStat("usersTotal").value))
            .attr("x", d => _this.x.scale(d.group))
            .attr("width", _this.x.scale.bandwidth())
            .attr("height", d => _this.y.scale(0) - _this.y.scale(d.getStat("usersTotal").value))), exit => exit.style("fill", "#cccccc")
            .style("stroke", "#b3b3b3")
            .call(exit => exit.transition()
            .duration(250)
            .attr("y", d => _this.y.scale(0))
            .attr("height", 0)
            .remove()));
        const onMouseover = function (e, d) {
            //If box is clicked not append tooltip
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.tooltip.appendTooltipContainer();
            //Append tooltip box with text
            let tooltipBox = _this.tooltip.appendTooltipText(d.group, d.stats.filter((c, i) => i < 2).map(c => new TooltipValues(c.displayName, c.value)));
            //Position tooltip container
            _this.tooltip.positionTooltipContainer(xTooltip(d.group, tooltipBox), yTooltip(d.getStat("usersTotal").value, tooltipBox));
            function xTooltip(x, tooltipBox) {
                //Position tooltip right of the box
                let xTooltip = _this.x.scale(x) + _this.x.scale.bandwidth();
                //If tooltip does not fit position left of the box
                if (_this.width - _this.padding.yAxis < xTooltip + tooltipBox.node().getBBox().width) {
                    return xTooltip - _this.x.scale.bandwidth() - tooltipBox.node().getBBox().width;
                }
                return xTooltip;
            }
            function yTooltip(y, tooltipBox) {
                //Position tooltip on top of the box
                let yTooltip = _this.y.scale(y) + (tooltipBox.node().getBBox().height / 2);
                //If tooltip does not fit position at the same height as the box
                if (_this.y.scale.invert(yTooltip) < 0) {
                    return _this.y.scale(y + _this.y.scale.invert(yTooltip));
                }
                return yTooltip;
            }
        };
        const onMouseout = function () {
            //Transition tooltip to opacity 0
            _this.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            //Remove tooltip
            _this.tooltip.removeTooltip();
        };
        //Enable tooltip
        _this.tooltip.enableTooltip(onMouseover, onMouseout);
    }
}
class ClickBarChart extends Click {
    constructor(chart) {
        super(chart);
    }
    appendGroupsText(data, clickData) {
        this.chart.elements.content.classed("clicked", (d) => d.group == clickData.group);
        this.chart.elements.contentContainer.selectAll(".click-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "click-container")
            .attr("transform", c => `translate(${this.chart.x.scale(c.group) + this.chart.x.scale.bandwidth() / 2}, 0)`)
            .call(enter => enter.selectAll("text")
            .data(c => c.stats.filter(d => d.stat == "q3" || d.stat == "median" || d.stat == "q1").map(d => new ClickTextData(clickData.stats.find(a => a.stat == d.stat), d, clickData.group, c.group)))
            .enter()
            .append("text")
            .attr("class", "click-text black")
            .attr("y", c => this.chart.y.scale(c.data.stat.value) - 5)
            .text(c => `${c.data.stat.displayName}: ${c.data.stat.value} `)
            .append("tspan")
            .attr("class", c => this.comparativeText(c)[0])
            .text(c => c.data.group != clickData.group ? `(${this.comparativeText(c)[1]})` : "")), update => update.call(update => update.transition()
            .duration(750)
            .attr("transform", c => `translate(${this.chart.x.scale(c.group) + this.chart.x.scale.bandwidth() / 2}, 0)`))
            .call(update => update.selectAll("text")
            .data(c => c.stats.filter(d => d.stat == "q3" || d.stat == "median" || d.stat == "q1").map(d => new ClickTextData(clickData.stats.find(a => a.stat == d.stat), d, clickData.group, c.group)))
            .join(enter => enter, update => update.attr("y", c => this.chart.y.scale(c.data.stat.value) - 5)
            .text(c => `${c.data.stat.displayName}: ${c.data.stat.value} `)
            .append("tspan")
            .attr("class", c => this.comparativeText(c)[0])
            .text(c => c.data.group != clickData.group ? `(${this.comparativeText(c)[1]})` : ""), exit => exit)), exit => exit.remove());
    }
    ;
}
