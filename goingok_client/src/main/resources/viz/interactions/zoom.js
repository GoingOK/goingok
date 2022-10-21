var d3 = require("d3");
export class Zoom {
    constructor(chart) {
        this.chart = chart;
        this.zoom = d3.zoom()
            .scaleExtent([1, 5])
            .extent([[0, 0], [this.chart.width - this.chart.padding.yAxis, this.chart.height]])
            .translateExtent([[0, 0], [this.chart.width - this.chart.padding.yAxis, this.chart.height]]);
        this.k = 1;
        this.handleZoom();
    }
    get k() {
        return this._k;
    }
    set k(k) {
        this._k = k < 1 ? 1 : k > 5 ? 5 : k;
        this.zoom.scaleTo(this.chart.elements.contentContainer.select(".zoom-rect"), this.k);
        this.chart.elements.contentContainer.select(".zoom-rect")
            .classed("active", this.k > 1);
    }
    enableZoom(zoomed) {
        this.zoom.on("zoom", zoomed);
        this.chart.elements.contentContainer.select(".zoom-rect").call(this.zoom);
    }
    ;
    appendZoomBar() {
        return this.chart.elements.svg.append("g")
            .attr("id", "zoom-container")
            .attr("class", "zoom-container")
            .attr("height", 30)
            .attr("width", this.chart.width - this.chart.padding.yAxis)
            .attr("transform", `translate(${this.chart.padding.yAxis}, ${this.chart.height - 30})`);
    }
    ;
    handleZoom() {
        d3.select(`#${this.chart.id} #zoom-minus`).on("click", () => {
            this.k = this.k - 1;
            this.updateZoomNumber();
        });
        d3.select(`#${this.chart.id} #zoom-plus`).on("click", () => {
            this.k = this.k + 1;
            this.updateZoomNumber();
        });
    }
    updateZoomNumber() {
        d3.select(`#${this.chart.id} #zoom-number`).attr("value", `${100 + ((this.k - 1) * 25)}%`);
    }
}
