var d3 = require("d3");
import { HistogramData, TimelineData, ClickTextData } from "../data/data.js";
export class Transitions {
    axisSeries(chart, data) {
        chart.x.scale.domain(data.map(d => d.group));
        d3.select(`#${chart.id} .x-axis`).transition()
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
export class AdminControlTransitions extends Transitions {
    histogram(chart, update) {
        update.selectAll(".histogram-rect")
            .data(d => chart.bin(d.value.map(d => d.point)).map(c => { return new HistogramData(d.value, d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }))
            .join(enter => enter, update => update.style("stroke", d => d.colour)
            .style("fill", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("x", d => chart.bandwidth(-d.percentage))
            .attr("y", d => chart.y.scale(d.bin.x1))
            .attr("height", d => chart.y.scale(d.bin.x0) - chart.y.scale(d.bin.x1))
            .attr("width", d => chart.bandwidth(d.percentage) - chart.bandwidth(-d.percentage))), exit => exit);
    }
    ;
    timelineDensity(update, getDensityData) {
        update.selectAll(".contour")
            .data(d => getDensityData(d))
            .join(enter => enter.append("path")
            .attr("class", "contour")
            .attr("d", d3.geoPath())
            .attr("opacity", (d) => d.value * 25), update => update.attr("d", d3.geoPath())
            .attr("opacity", (d) => d.value * 20), exit => exit.remove());
    }
    ;
    timelineScatter(update, chart, zoom = false, invisible = false) {
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
}
export class AdminControlInteractions extends AdminControlTransitions {
    constructor() {
        super(...arguments);
        this.tooltip = new Tooltip();
        this.zoom = new Zoom();
    }
}
export class TooltipValues {
    constructor(label, value) {
        this.label = label == undefined ? "" : label;
        this.value = value == undefined ? 0 : value;
    }
}
export class Tooltip {
    enableTooltip(chart, onMouseover, onMouseout) {
        chart.elements.content.on("mouseover", onMouseover);
        chart.elements.content.on("mouseout", onMouseout);
    }
    ;
    removeTooltip(chart) {
        chart.elements.contentContainer.selectAll(".tooltip-container").remove();
        chart.elements.contentContainer.selectAll(".tooltip-line").remove();
    }
    ;
    appendTooltipContainer(chart) {
        chart.elements.contentContainer.append("g")
            .attr("class", "tooltip-container");
    }
    ;
    appendTooltipText(chart, title, values = null) {
        let result = chart.elements.contentContainer.select(".tooltip-container").append("rect")
            .attr("class", "tooltip-box");
        let text = chart.elements.contentContainer.select(".tooltip-container").append("text")
            .attr("class", "tooltip-text title")
            .attr("x", 10)
            .text(title);
        let textSize = text.node().getBBox().height;
        text.attr("y", textSize);
        if (values != null) {
            values.forEach((c, i) => {
                text.append("tspan")
                    .attr("class", "tooltip-text")
                    .attr("y", textSize * (i + 2))
                    .attr("x", 15)
                    .text(`${c.label}: ${c.value}`);
            });
        }
        return result.attr("width", text.node().getBBox().width + 20)
            .attr("height", text.node().getBBox().height + 5);
    }
    ;
    positionTooltipContainer(chart, x, y) {
        chart.elements.contentContainer.select(".tooltip-container")
            .attr("transform", `translate(${x}, ${y})`)
            .transition()
            .style("opacity", 1);
    }
    ;
    appendLine(chart, x1, y1, x2, y2, colour) {
        chart.elements.contentContainer.append("line")
            .attr("class", "tooltip-line")
            .attr("x1", x1)
            .attr("y1", y1)
            .attr("x2", x2)
            .attr("y2", y2)
            .style("stroke", colour);
    }
    ;
}
export class Zoom {
    enableZoom(chart, zoomed) {
        chart.elements.svg.selectAll(".zoom-rect")
            .attr("class", "zoom-rect active");
        let zoom = d3.zoom()
            .scaleExtent([1, 5])
            .extent([[0, 0], [chart.width - chart.padding.yAxis, chart.height]])
            .translateExtent([[0, 0], [chart.width - chart.padding.yAxis, chart.height]])
            .on("zoom", zoomed);
        chart.elements.contentContainer.select(".zoom-rect").call(zoom);
    }
    ;
    appendZoomBar(chart) {
        return chart.elements.svg.append("g")
            .attr("class", "zoom-container")
            .attr("height", 30)
            .attr("width", chart.width - chart.padding.yAxis)
            .attr("transform", `translate(${chart.padding.yAxis}, ${chart.height - 30})`);
    }
    ;
}
export class AdminExperimentalInteractions extends AdminControlInteractions {
    constructor() {
        super(...arguments);
        this.click = new ClickAdmin();
        this.sort = new Sort();
    }
}
export class Click {
    enableClick(chart, onClick) {
        chart.elements.content.on("click", onClick);
    }
    ;
    removeClick(chart) {
        chart.click = false;
        chart.elements.contentContainer.selectAll(".click-text").remove();
        chart.elements.contentContainer.selectAll(".click-line").remove();
        chart.elements.contentContainer.selectAll(".click-container").remove();
        chart.elements.content.classed("clicked", false);
        chart.elements.content.classed("main", false);
    }
    ;
}
export class ClickAdmin extends Click {
    appendScatterText(chart, d, title, values = null) {
        let container = chart.elements.contentContainer.append("g")
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
            .attr("clip-path", `url(#clip-${chart.id})`);
        container.attr("transform", this.positionClickContainer(chart, box, text, d));
    }
    ;
    positionClickContainer(chart, box, text, d) {
        let positionX = chart.x.scale(d.timestamp);
        let positionY = chart.y.scale(d.point) - box.node().getBBox().height - 10;
        if (chart.width - chart.padding.yAxis < chart.x.scale(d.timestamp) + text.node().getBBox().width) {
            positionX = chart.x.scale(d.timestamp) - box.node().getBBox().width;
        }
        ;
        if (chart.y.scale(d.point) - box.node().getBBox().height - 10 < 0) {
            positionY = positionY + box.node().getBBox().height + 20;
        }
        ;
        return `translate(${positionX}, ${positionY})`;
    }
    ;
    appendGroupsText(chart, data, clickData) {
        chart.elements.content.classed("clicked", (d) => d.group == clickData.group);
        chart.elements.contentContainer.selectAll(".click-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "click-container")
            .attr("transform", c => `translate(${chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2}, 0)`)
            .call(enter => enter.selectAll("text")
            .data(c => c.stats.filter(d => d.stat == "q3" || d.stat == "median" || d.stat == "q1").map(d => new ClickTextData(clickData.stats.find(a => a.stat == d.stat), d, clickData.group, c.group)))
            .enter()
            .append("text")
            .attr("class", "click-text black")
            .attr("y", c => chart.y.scale(c.data.stat.value) - 5)
            .text(c => `${c.data.stat.displayName}: ${c.data.stat.value} `)
            .append("tspan")
            .attr("class", c => this.comparativeText(c)[0])
            .text(c => c.data.group != clickData.group ? `(${this.comparativeText(c)[1]})` : "")), update => update.call(update => update.transition()
            .duration(750)
            .attr("transform", c => `translate(${chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2}, 0)`))
            .call(update => update.selectAll("text")
            .data(c => c.stats.filter(d => d.stat == "q3" || d.stat == "median" || d.stat == "q1").map(d => new ClickTextData(clickData.stats.find(a => a.stat == d.stat), d, clickData.group, c.group)))
            .join(enter => enter, update => update.attr("y", c => chart.y.scale(c.data.stat.value) - 5)
            .text(c => `${c.data.stat.displayName}: ${c.data.stat.value} `)
            .append("tspan")
            .attr("class", c => this.comparativeText(c)[0])
            .text(c => c.data.group != clickData.group ? `(${this.comparativeText(c)[1]})` : ""), exit => exit)), exit => exit.remove());
    }
    ;
    appendThresholdPercentages(chart, data, clickData) {
        let thresholds = chart.elements.getThresholdsValues(chart);
        let tDistressed = thresholds[0];
        let tSoaring = thresholds[1];
        chart.elements.content.classed("clicked", (d) => d.group == clickData.group && clickData.bin.length - d.bin.length == 0);
        chart.elements.contentContainer.selectAll(".click-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "click-container")
            .attr("transform", c => `translate(${chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2}, 0)`)
            .call(enter => enter.selectAll("text")
            .data(d => chart.bin(d.value.map(d => d.point)).map(c => { return new HistogramData(d.value, d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }))
            .enter()
            .append("text")
            .attr("class", "click-text black")
            .attr("y", c => c.bin.x0 == 0 ? chart.y.scale(0 + tDistressed / 2) : c.bin.x1 == 100 ? chart.y.scale(tSoaring + (100 - tSoaring) / 2) : chart.y.scale(50))
            .text(c => `${c.percentage}% `)
            .append("tspan")
            .attr("class", c => this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[0])
            .text(c => c.group != clickData.group && c.bin.x0 == clickData.bin.x0 && c.bin.x1 == clickData.bin.x1 ? `(${this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[1]})` : "")), update => update.call(update => update.transition()
            .duration(750)
            .attr("transform", c => `translate(${chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2}, 0)`))
            .call(update => update.selectAll("text")
            .data(d => chart.bin(d.value.map(d => d.point)).map(c => { return new HistogramData(d.value, d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }))
            .join(enter => enter, update => update.attr("y", c => c.bin.x0 == 0 ? chart.y.scale(0 + tDistressed / 2) : c.bin.x1 == 100 ? chart.y.scale(tSoaring + (100 - tSoaring) / 2) : chart.y.scale(50))
            .text(c => `${c.percentage}% `)
            .append("tspan")
            .attr("class", c => this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[0])
            .text(c => c.group != clickData.group && c.bin.x0 == clickData.bin.x0 && c.bin.x1 == clickData.bin.x1 ? `(${this.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[1]})` : ""), exit => exit)), exit => exit.remove());
    }
    ;
    comparativeText(textData) {
        let textClass = "click-text";
        let textSymbol = "";
        let textValue;
        if (typeof (textData.clickData.stat) != "number" && typeof (textData.data.stat) != "number") {
            textValue = textData.clickData.stat.value - textData.data.stat.value;
        }
        else {
            textValue = textData.clickData.stat - textData.data.stat;
        }
        if (textValue < 0) {
            textClass = textClass + " positive";
            textSymbol = "+";
        }
        else if (textValue > 0) {
            textClass = textClass + " negative";
            textSymbol = "-";
        }
        else {
            textClass = textClass + " black";
        }
        if (textData.clickData.group != null && textData.data.group != null) {
            return [textClass, `${textSymbol}${textData.clickData.group == textData.data.group
                    && textValue == 0 ? typeof (textData.clickData.stat) != "number" ? textData.clickData.stat.value : textData.clickData.stat : (Math.abs(textValue))}`];
        }
        else {
            return [textClass, `${textSymbol}${(Math.abs(textValue))}`];
        }
    }
}
export class Sort {
    sortData(a, b, sorted) {
        if (a < b) {
            if (sorted) {
                return -1;
            }
            else {
                return 1;
            }
        }
        if (a > b) {
            if (sorted) {
                return 1;
            }
            else {
                return -1;
            }
        }
        return 0;
    }
    ;
    setSorted(sorted, option) {
        return sorted == option ? "" : option;
    }
}
export class AuthorControlTransitions extends Transitions {
}
export class AuthorControlInteractions extends AuthorControlTransitions {
    constructor() {
        super(...arguments);
        this.tooltip = new Tooltip();
        this.zoom = new Zoom();
    }
}
export class AuthorExperimentalInteractions extends AuthorControlInteractions {
    constructor() {
        super(...arguments);
        this.click = new Click();
        this.sort = new Sort();
    }
}
