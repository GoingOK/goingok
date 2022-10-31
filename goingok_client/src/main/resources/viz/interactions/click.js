export class Click {
    constructor(chart) {
        this.chart = chart;
    }
    enableClick(onClick) {
        this.chart.elements.content.on("click", onClick);
    }
    ;
    removeClick() {
        this.clicked = false;
        this.chart.elements.contentContainer.selectAll(".click-text").remove();
        this.chart.elements.contentContainer.selectAll(".click-line").remove();
        this.chart.elements.contentContainer.selectAll(".click-container").remove();
        this.chart.elements.content.classed("clicked", false);
        this.chart.elements.content.classed("not-clicked", false);
        this.chart.elements.content.classed("main", false);
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
