export class TooltipValues {
    constructor(label, value) {
        this.label = label == undefined ? "" : label;
        this.value = value == undefined ? 0 : value;
    }
}
export class Tooltip {
    constructor(chart) {
        this.chart = chart;
    }
    enableTooltip(onMouseover, onMouseout) {
        this.chart.elements.content.on("mouseover", onMouseover);
        this.chart.elements.content.on("mouseout", onMouseout);
    }
    ;
    removeTooltip() {
        this.chart.elements.contentContainer.selectAll(".tooltip-container").remove();
        this.chart.elements.contentContainer.selectAll(".tooltip-line").remove();
    }
    ;
    appendTooltipContainer() {
        this.chart.elements.contentContainer.append("g")
            .attr("class", "tooltip-container");
    }
    ;
    appendTooltipText(title, values = null) {
        let result = this.chart.elements.contentContainer.select(".tooltip-container").append("rect")
            .attr("class", "tooltip-box");
        let text = this.chart.elements.contentContainer.select(".tooltip-container").append("text")
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
    positionTooltipContainer(x, y) {
        this.chart.elements.contentContainer.select(".tooltip-container")
            .attr("transform", `translate(${x}, ${y})`)
            .transition()
            .style("opacity", 1);
    }
    ;
    appendLine(x1, y1, x2, y2, colour) {
        this.chart.elements.contentContainer.append("line")
            .attr("class", "tooltip-line")
            .attr("x1", x1)
            .attr("y1", y1)
            .attr("x2", x2)
            .attr("y2", y2)
            .style("stroke", colour);
    }
    ;
}
