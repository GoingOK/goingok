var d3 = require("d3");
export class ChartSeriesAxis {
    constructor(label, domain, range, position) {
        this.label = label;
        this.scale = d3.scaleBand()
            .domain(domain)
            .rangeRound(range)
            .padding(0.25);
        if (position == "right") {
            this.axis = d3.axisRight(this.scale);
        }
        else if (position == "left") {
            this.axis = d3.axisLeft(this.scale);
        }
        else {
            this.axis = d3.axisBottom(this.scale);
        }
    }
    ;
}
export class ChartLinearAxis {
    constructor(label, domain, range, position, isGoingOk = true) {
        this.label = label;
        this.scale = d3.scaleLinear()
            .domain([d3.min(domain) < 0 ? d3.min(domain) : 0, d3.max(domain)])
            .range(range);
        if (position == "right") {
            this.axis = d3.axisRight(this.scale);
        }
        else if (position == "bottom") {
            this.axis = d3.axisBottom(this.scale);
        }
        else {
            this.axis = d3.axisLeft(this.scale);
        }
        if (isGoingOk) {
            let labels = new Map();
            labels.set(0, "distressed");
            labels.set(50, "going ok");
            labels.set(100, "soaring");
            this.axis.tickValues([0, 25, 50, 75, 100])
                .tickFormat(d => labels.get(d));
        }
    }
    ;
    setThresholdAxis(tDistressed, tSoaring) {
        return d3.axisRight(this.scale)
            .tickValues([tDistressed, tSoaring])
            .tickFormat(d => d == tDistressed ? "Distressed" : d == tSoaring ? "Soaring" : "");
    }
}
export class ChartTimeAxis {
    constructor(label, domain, range) {
        this.label = label;
        this.scale = d3.scaleTime()
            .domain(d3.extent(domain))
            .range(range);
        this.axis = d3.axisBottom(this.scale);
    }
    ;
}
