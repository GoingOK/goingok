"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.buildExperimentAdminAnalyticsCharts = exports.buildControlAdminAnalyticsCharts = void 0;
var d3 = require("d3");
var AnalyticsChartsDataRaw = /** @class */ (function () {
    function AnalyticsChartsDataRaw(group, value, createDate) {
        this.group = group;
        this.value = value;
        this.createDate = createDate;
    }
    AnalyticsChartsDataRaw.prototype.transformData = function () {
        return new AnalyticsChartsData(this.group, this.value.map(function (d) {
            return {
                timestamp: new Date(d.timestamp), pseudonym: d.pseudonym, point: parseInt(d.point), text: d.text
            };
        }), new Date(this.createDate), undefined, false);
    };
    return AnalyticsChartsDataRaw;
}());
var AnalyticsChartsData = /** @class */ (function () {
    function AnalyticsChartsData(group, value, createDate, colour, selected) {
        if (colour === void 0) { colour = undefined; }
        if (selected === void 0) { selected = false; }
        this.group = group;
        this.value = value;
        this.creteDate = createDate;
        this.colour = colour;
        this.selected = selected;
    }
    AnalyticsChartsData.prototype.getUsersData = function () {
        var usersMean = Array.from(d3.rollup(this.value, function (d) { return Math.round(d3.mean(d.map(function (r) { return r.point; }))); }, function (d) { return d.pseudonym; }), function (_a) {
            var pseudonym = _a[0], point = _a[1];
            return ({ pseudonym: pseudonym, point: point });
        });
        return new AnalyticsChartsData(this.group, usersMean, this.creteDate, this.colour);
    };
    return AnalyticsChartsData;
}());
var DataStats = /** @class */ (function () {
    function DataStats(stat, displayName, value) {
        this.stat = stat,
            this.displayName = displayName,
            this.value = value;
    }
    return DataStats;
}());
var AnalyticsChartsDataStats = /** @class */ (function (_super) {
    __extends(AnalyticsChartsDataStats, _super);
    function AnalyticsChartsDataStats(entries) {
        var _this_1 = _super.call(this, entries.group, entries.value, entries.creteDate, entries.colour, entries.selected) || this;
        var uniqueUsers = Array.from(d3.rollup(entries.value, function (d) { return d.length; }, function (d) { return d.pseudonym; }), function (_a) {
            var key = _a[0], value = _a[1];
            return ({ key: key, value: value });
        });
        _this_1.stats = [];
        _this_1.stats.push(new DataStats("mean", "Mean", Math.round(d3.mean(entries.value.map(function (r) { return r.point; })))));
        _this_1.stats.push(new DataStats("median", "Median", Math.round(d3.median(entries.value.map(function (r) { return r.point; })))));
        _this_1.stats.push(new DataStats("q1", "Q1", Math.round(d3.quantile(entries.value.map(function (r) { return r.point; }), 0.25))));
        _this_1.stats.push(new DataStats("q3", "Q3", Math.round(d3.quantile(entries.value.map(function (r) { return r.point; }), 0.75))));
        _this_1.stats.push(new DataStats("max", "Max", d3.max(entries.value.map(function (r) { return r.point; }))));
        _this_1.stats.push(new DataStats("min", "Min", d3.min(entries.value.map(function (r) { return r.point; }))));
        _this_1.stats.push(new DataStats("oldRef", "Oldest reflection", d3.min(entries.value.map(function (r) { return new Date(r.timestamp); }))));
        _this_1.stats.push(new DataStats("newRef", "Newest reflection", d3.max(entries.value.map(function (r) { return new Date(r.timestamp); }))));
        _this_1.stats.push(new DataStats("totalRef", "Total reflections", entries.value.length));
        _this_1.stats.push(new DataStats("totalUsers", "Total users", uniqueUsers.length));
        return _this_1;
    }
    ;
    AnalyticsChartsDataStats.prototype.roundDecimal = function (value) {
        var p = d3.precisionFixed(0.1);
        var f = d3.format("." + p + "f");
        return f(value);
    };
    ;
    AnalyticsChartsDataStats.prototype.getStat = function (stat) {
        var exists = this.stats.find(function (d) { return d.stat == stat; });
        if (exists != undefined) {
            return exists;
        }
        else {
            return new DataStats("na", "Not found", 0);
        }
    };
    return AnalyticsChartsDataStats;
}(AnalyticsChartsData));
// Basic class for series charts
var ChartSeries = /** @class */ (function () {
    function ChartSeries(id, domain) {
        this.id = id;
        var containerDimensions = d3.select("#" + id + " .chart-container").node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = containerDimensions.height;
        this.padding = new ChartPadding();
        this.y = new ChartLinearAxis("Reflection Point", [0, 100], [this.height - this.padding.xAxis - this.padding.top, 0], "left");
        this.x = new ChartSeriesAxis("Group Code", domain, [0, this.width - this.padding.yAxis - this.padding.right]);
        this.click = false;
        this.elements = new ChartElements(this);
    }
    return ChartSeries;
}());
// Basic class for time series charts
var ChartTime = /** @class */ (function () {
    function ChartTime(id, domain) {
        this.id = id;
        var containerDimensions = d3.select("#" + id + " .chart-container").node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = containerDimensions.height;
        this.padding = new ChartPadding(75, 75, 5);
        this.htmlContainers = new HtmlContainers();
        this.y = new ChartLinearAxis("Reflection Point", [0, 100], [this.height - this.padding.xAxis - this.padding.top, 0], "left");
        this.x = new ChartTimeAxis("Time", domain, [0, this.width - this.padding.yAxis]);
        this.click = false;
        this.elements = new ChartElements(this);
    }
    return ChartTime;
}());
// Basic class for time series zoom bar
var ChartTimeZoom = /** @class */ (function () {
    function ChartTimeZoom(chart, domain) {
        this.x = new ChartTimeAxis("", domain, [0, chart.width - chart.padding.yAxis - 5]);
        this.y = new ChartLinearAxis("", [0, 100], [25, 0], "left");
    }
    return ChartTimeZoom;
}());
// Basic class for user chart
var UserChart = /** @class */ (function () {
    function UserChart(id, containerClass) {
        this.id = id;
        var containerDimensions = d3.select("#" + id + " ." + containerClass).node().getBoundingClientRect();
        this.width = containerDimensions.width;
        this.height = 50;
        this.padding = new ChartPadding(20, 30, 10, 30);
        this.y = new ChartLinearAxis("", [0, 1], [20, 0], "left");
        this.x = new ChartLinearAxis("Reflection Point Average", [0, 100], [0, this.width - this.padding.yAxis - this.padding.right], "bottom");
        this.click = false;
        this.elements = new ChartElements(this, containerClass);
    }
    return UserChart;
}());
// Class for histogram chart series
var HistogramChartSeries = /** @class */ (function (_super) {
    __extends(HistogramChartSeries, _super);
    function HistogramChartSeries(id, domain) {
        var _this_1 = _super.call(this, id, domain) || this;
        _this_1.padding = new ChartPadding(40, 75, 5, 85);
        _this_1.x = new ChartSeriesAxis("Group Code", domain, [0, _this_1.width - _this_1.padding.yAxis - _this_1.padding.right]);
        d3.select("#" + _this_1.id + " svg").remove();
        _this_1.thresholdAxis = _this_1.y.setThresholdAxis(30, 70);
        _this_1.elements = new HistogramChartElements(_this_1);
        return _this_1;
    }
    HistogramChartSeries.prototype.setBandwidth = function (data) {
        this.bandwidth = d3.scaleLinear()
            .range([0, this.x.scale.bandwidth()])
            .domain([-100, 100]);
    };
    ;
    HistogramChartSeries.prototype.setBin = function () {
        this.bin = d3.bin().domain([0, 100]).thresholds([0, this.elements.getThresholdsValues(this)[0], this.elements.getThresholdsValues(this)[1]]);
    };
    return HistogramChartSeries;
}(ChartSeries));
// Basic class for series axis scale
var ChartSeriesAxis = /** @class */ (function () {
    function ChartSeriesAxis(label, domain, range) {
        this.label = label;
        this.scale = d3.scaleBand()
            .domain(domain)
            .rangeRound(range)
            .padding(0.25);
        this.axis = d3.axisBottom(this.scale);
    }
    ;
    return ChartSeriesAxis;
}());
// Basic class for linear axis scale
var ChartLinearAxis = /** @class */ (function () {
    function ChartLinearAxis(label, domain, range, position) {
        this.label = label;
        this.scale = d3.scaleLinear()
            .domain([d3.min(domain), d3.max(domain)])
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
        var labels = new Map();
        labels.set(0, "distressed");
        labels.set(50, "going ok");
        labels.set(100, "soaring");
        this.axis.tickValues([0, 25, 50, 75, 100])
            .tickFormat(function (d) { return labels.get(d); });
    }
    ;
    ChartLinearAxis.prototype.setThresholdAxis = function (tDistressed, tSoaring) {
        return d3.axisRight(this.scale)
            .tickValues([tDistressed, tSoaring])
            .tickFormat(function (d) { return d == tDistressed ? "Distressed" : d == tSoaring ? "Soaring" : ""; });
    };
    return ChartLinearAxis;
}());
// Basic class for time axis scale
var ChartTimeAxis = /** @class */ (function () {
    function ChartTimeAxis(label, domain, range) {
        this.label = label;
        this.scale = d3.scaleTime()
            .domain(domain)
            .range(range);
        this.axis = d3.axisBottom(this.scale);
    }
    ;
    return ChartTimeAxis;
}());
// Basic class for chart elements (includes zoom)
var ChartElements = /** @class */ (function () {
    function ChartElements(chart, containerClass) {
        this.svg = this.appendSVG(chart, containerClass);
        this.contentContainer = this.appendContentContainer(chart);
        this.xAxis = this.appendXAxis(chart);
        this.appendXAxisLabel(chart);
        this.yAxis = this.appendYAxis(chart);
        this.appendYAxisLabel(chart);
    }
    ChartElements.prototype.appendSVG = function (chart, containerClass) {
        return d3.select("#" + chart.id + " " + (containerClass == undefined ? ".chart-container" : "." + containerClass))
            .append("svg")
            .attr("id", "chart-" + chart.id)
            .attr("preserveAspectRatio", "xMinYMin meet")
            .attr("viewBox", "0 0 " + chart.width + " " + chart.height);
    };
    ;
    ChartElements.prototype.appendContentContainer = function (chart) {
        var result = this.svg.append("g")
            .attr("class", "content-container")
            .attr("transform", "translate(" + chart.padding.yAxis + ", " + chart.padding.top + ")")
            .attr("clip-path", "url(#clip-" + chart.id + ")");
        result.append("rect")
            .attr("class", "zoom-rect")
            .attr("width", chart.width - chart.padding.yAxis - chart.padding.right)
            .attr("height", chart.height - chart.padding.xAxis - chart.padding.top);
        result.append("clipPath")
            .attr("id", "clip-" + chart.id)
            .append("rect")
            .attr("x", 1)
            .attr("width", chart.width - chart.padding.yAxis)
            .attr("height", chart.height - chart.padding.xAxis - chart.padding.top);
        return result;
    };
    ;
    ChartElements.prototype.appendXAxis = function (chart) {
        return this.svg.append("g")
            .attr("transform", "translate(" + chart.padding.yAxis + ", " + (chart.height - chart.padding.xAxis) + ")")
            .attr("class", "x-axis")
            .attr("clip-path", "url(#clip-" + chart.id + ")")
            .call(chart.x.axis);
    };
    ;
    ChartElements.prototype.appendXAxisLabel = function (chart) {
        return this.svg.append("g")
            .attr("class", "x-label-container")
            .attr("transform", "translate(" + (this.svg.select(".x-axis").node().getBBox().width / 2 + chart.padding.yAxis) + ", " + (chart.height - chart.padding.xAxis + this.svg.select(".x-axis").node().getBBox().height * 2) + ")")
            .append("text")
            .attr("class", "x-label-text")
            .attr("text-anchor", "middle")
            .text(chart.x.label);
    };
    ;
    ChartElements.prototype.appendYAxis = function (chart) {
        return this.svg.append("g")
            .attr("transform", "translate(" + chart.padding.yAxis + ", " + chart.padding.top + ")")
            .attr("class", "y-axis")
            .call(chart.y.axis);
    };
    ;
    ChartElements.prototype.appendYAxisLabel = function (chart) {
        return this.svg.append("g")
            .attr("class", "y-label-container")
            .attr("transform", "translate(" + (chart.padding.yAxis - this.svg.select(".y-axis").node().getBBox().width) + ", " + (chart.padding.top + this.svg.select(".y-axis").node().getBBox().height / 2) + ") rotate(-90)")
            .append("text")
            .attr("class", "y-label-text")
            .attr("text-anchor", "middle")
            .text(chart.y.label);
    };
    return ChartElements;
}());
// Class for histogram charts
var HistogramChartElements = /** @class */ (function (_super) {
    __extends(HistogramChartElements, _super);
    function HistogramChartElements(chart) {
        var _this_1 = _super.call(this, chart) || this;
        var thresholds = _this_1.getThresholdsValues(chart);
        _this_1.appendThresholdAxis(chart);
        _this_1.appendThresholdIndicators(chart, thresholds);
        _this_1.appendThresholdLabel(chart);
        return _this_1;
    }
    HistogramChartElements.prototype.appendThresholdAxis = function (chart) {
        return this.contentContainer.append("g")
            .attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right) + ", 0)")
            .attr("class", "threshold-axis")
            .call(chart.thresholdAxis);
    };
    ;
    HistogramChartElements.prototype.appendThresholdLabel = function (chart) {
        var label = this.svg.append("g")
            .attr("class", "threshold-label-container");
        label.append("text")
            .attr("class", "y-label-text")
            .attr("text-anchor", "middle")
            .text("Thresholds");
        label.attr("transform", "translate(" + (chart.width - chart.padding.right + this.contentContainer.select(".threshold-axis").node().getBBox().width + label.node().getBBox().height) + ", " + (chart.padding.top + this.svg.select(".y-axis").node().getBBox().height / 2) + ") rotate(-90)");
        return label;
    };
    ;
    HistogramChartElements.prototype.appendThresholdIndicators = function (chart, thresholds) {
        this.contentContainer.selectAll(".threshold-indicator-container")
            .data(thresholds)
            .enter()
            .append("g")
            .attr("class", "threshold-indicator-container")
            .classed("distressed", function (d) { return d < 50 ? true : false; })
            .classed("soaring", function (d) { return d > 50 ? true : false; })
            .attr("transform", function (d) { return "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right + 5) + ", " + (d < 50 ? chart.y.scale(d) + 25 : chart.y.scale(d) - 15) + ")"; })
            .call(function (g) { return g.append("rect")
                .attr("class", "threshold-indicator-box")
                .classed("distressed", function (d) { return d < 50 ? true : false; })
                .classed("soaring", function (d) { return d > 50 ? true : false; }); })
            .call(function (g) { return g.append("text")
                .attr("class", "threshold-indicator-text")
                .attr("x", 5)
                .text(function (d) { return d; }); })
            .call(function (g) { return g.selectAll("rect")
                .attr("width", g.select("text").node().getBBox().width + 10)
                .attr("height", g.select("text").node().getBBox().height + 5)
                .attr("y", -g.select("text").node().getBBox().height); });
        this.contentContainer.selectAll(".threshold-line")
            .data(thresholds)
            .enter()
            .append("line")
            .attr("class", "threshold-line")
            .classed("distressed", function (d) { return d < 50 ? true : false; })
            .classed("soaring", function (d) { return d > 50 ? true : false; })
            .attr("x1", 0)
            .attr("x2", chart.width - chart.padding.yAxis - chart.padding.right)
            .attr("y1", function (d) { return chart.y.scale(d); })
            .attr("y2", function (d) { return chart.y.scale(d); });
    };
    HistogramChartElements.prototype.getThresholdsValues = function (chart) {
        var result = [30, 70];
        var dThreshold = this.contentContainer.select(".threshold-line.distressed");
        if (!dThreshold.empty()) {
            result[0] = chart.y.scale.invert(parseInt(dThreshold.attr("y1")));
        }
        var sThreshold = this.contentContainer.select(".threshold-line.soaring");
        if (!sThreshold.empty()) {
            result[1] = chart.y.scale.invert(parseInt(sThreshold.attr("y1")));
        }
        return result;
    };
    ;
    return HistogramChartElements;
}(ChartElements));
// Basic class for chart paddinf
var ChartPadding = /** @class */ (function () {
    function ChartPadding(xAxis, yAxis, top, right) {
        this.xAxis = xAxis == undefined ? 40 : xAxis;
        this.yAxis = yAxis == undefined ? 75 : yAxis;
        this.top = top == undefined ? 5 : top;
        this.right = right == undefined ? 0 : right;
    }
    return ChartPadding;
}());
// Class for timeline data
var TimelineData = /** @class */ (function () {
    function TimelineData(data, colour, group) {
        this.timestamp = data.timestamp;
        this.pseudonym = data.pseudonym;
        this.point = data.point;
        this.text = data.text;
        this.colour = colour;
        this.group = group;
    }
    return TimelineData;
}());
// Class for bin hover data
var HistogramData = /** @class */ (function () {
    function HistogramData(group, colour, bin, percentage) {
        this.group = group;
        this.colour = colour;
        this.bin = bin;
        this.percentage = percentage;
    }
    return HistogramData;
}());
// Class click text data
var ClickTextData = /** @class */ (function () {
    function ClickTextData(clickStat, dataStat, clickGroup, dataGroup) {
        this.clickData = { stat: clickStat, group: clickGroup },
            this.data = { stat: dataStat, group: dataGroup };
    }
    return ClickTextData;
}());
// Basic class for Html containers
var HtmlContainers = /** @class */ (function () {
    function HtmlContainers() {
    }
    HtmlContainers.prototype.remove = function () {
        if (this.statistics != undefined) {
            this.statistics.remove();
            this.statistics = undefined;
        }
        if (this.timeline != undefined) {
            this.removeNavbarScrollspyItem(this.timeline.attr("id"));
            this.timeline.remove();
            this.timeline = undefined;
        }
        if (this.histogram != undefined) {
            this.removeNavbarScrollspyItem(this.histogram.attr("id"));
            this.histogram.remove();
            this.histogram = undefined;
        }
        if (this.userHistogram != undefined) {
            this.userHistogram.remove();
            this.userHistogram = undefined;
        }
        if (this.compare != undefined) {
            this.compare.remove();
        }
        this.removeUsers();
    };
    ;
    HtmlContainers.prototype.removeUsers = function () {
        if (this.userStatistics != undefined) {
            this.removeNavbarScrollspyItem(this.userStatistics.attr("id"));
            this.userStatistics.remove();
            this.userStatistics = undefined;
        }
    };
    ;
    HtmlContainers.prototype.appendDiv = function (id, css) {
        return d3.select("#analytics-charts").append("div")
            .attr("id", id)
            .attr("class", css);
    };
    ;
    HtmlContainers.prototype.appendCard = function (div, header, id, help) {
        if (help === void 0) { help = false; }
        return div.append("div")
            .attr("class", "card")
            .attr("id", id != null ? id : "no-id")
            .append("div")
            .attr("class", "card-body")
            .call(function (div) { return div.append("h5")
                .attr("class", "card-title")
                .html(!help ? header : header + "<button type=\"button\" class=\"btn btn-light btn-sm float-right\"><i class=\"fas fa-question-circle\"></i></button>"); })
            .call(function (div) { return div.append("h6")
                .attr("class", "card-subtitle mb-2 text-muted"); })
            .call(function (div) { return div.append("div")
                .attr("class", "chart-container"); });
    };
    ;
    HtmlContainers.prototype.helpPopover = function (button, id, content) {
        if (d3.select("#" + id).empty()) {
            var popover = d3.select("body").append("div")
                .attr("id", id)
                .attr("class", "popover fade bs-popover-left show")
                .style("top", window.pageYOffset + button.node().getBoundingClientRect().top + "px");
            popover.append("div")
                .attr("class", "arrow")
                .style("top", "6px");
            popover.append("div")
                .attr("class", "popover-body")
                .html(content);
            popover.style("left", button.node().getBoundingClientRect().left - popover.node().getBoundingClientRect().width + "px");
            button.select("i")
                .attr("class", "fas fa-window-close");
            return true;
        }
        else {
            d3.select("#" + id).remove();
            button.select("i")
                .attr("class", "fas fa-question-circle");
            return false;
        }
    };
    ;
    HtmlContainers.prototype.removeHelp = function (chart) {
        d3.select("#" + chart.id + "-help").remove();
        d3.select("#" + chart.id + "-help-button").remove();
        d3.select("#" + chart.id + "-help-data").remove();
        d3.select("#" + chart.id + "-help-drag").remove();
        d3.select("#" + chart.id + "-help-zoom").remove();
    };
    ;
    HtmlContainers.prototype.renderNavbarScrollspy = function () {
        d3.select("body")
            .attr("data-spy", "scroll")
            .attr("data-target", "#analytics-navbar")
            .attr("data-offset", 0);
        this.renderNavbarScrollspyItem(this.boxPlot.attr("id"), "Reflections");
        if (this.timeline != undefined) {
            this.renderNavbarScrollspyItem(this.timeline.attr("id"), "Timeline");
        }
        if (this.userStatistics != undefined) {
            this.renderNavbarScrollspyItem(this.userStatistics.attr("id"), "Users");
        }
    };
    ;
    HtmlContainers.prototype.renderNavbarScrollspyItem = function (id, name) {
        var exists = d3.select("#analytics-navbar ul").selectAll("a").filter(function () {
            return d3.select(this).attr("href") == "#" + id;
        });
        if (exists.empty()) {
            d3.select("#analytics-navbar ul").append("li")
                .attr("class", "nav-item")
                .attr("id", id + "-li")
                .append("a")
                .attr("class", "nav-link")
                .attr("href", "#" + id)
                .html(name);
        }
    };
    ;
    HtmlContainers.prototype.removeNavbarScrollspyItem = function (id) {
        d3.select("#analytics-navbar ul #" + id + "-li").remove();
    };
    return HtmlContainers;
}());
var AdminControlCharts = /** @class */ (function () {
    function AdminControlCharts() {
        this.htmlContainers = new HtmlContainers();
        this.interactions = new AdminControlInteractions();
    }
    AdminControlCharts.prototype.sidebarBtn = function () {
        //Handle side bar btn click
        d3.select("#sidebar-btn").on("click", function () {
            var isActive = d3.select("#sidebar").attr("class").includes("active");
            d3.select("#sidebar")
                .attr("class", isActive ? "" : "active");
        });
    };
    ;
    AdminControlCharts.prototype.preloadGroups = function (allEntries, enable) {
        if (enable === void 0) { enable = false; }
        d3.select("#groups")
            .selectAll("li")
            .data(allEntries)
            .enter()
            .append("li")
            .append("div")
            .attr("class", "input-group mb-1")
            .call(function (div) { return div.append("div")
                .attr("class", "input-group-prepend")
                .call(function (div) { return div.append("div")
                    .attr("class", "input-group-text group-row")
                    .html(function (d, i) { return " <input type=\"checkbox\" value=\"" + d.group + "\" " + (!enable ? "checked disabled" : i == 0 ? "checked" : "") + " />"; }); }); })
            .call(function (div) { return div.append("input")
                .attr("type", "text")
                .attr("class", "form-control group-row")
                .attr("value", function (d) { return d.group; })
                .property("disabled", true); })
            .call(function (div) { return div.append("div")
                .attr("class", "input-group-append")
                .call(function (div) { return div.append("div")
                    .attr("class", "input-group-text group-row")
                    .html(function (d) { return "<input type=\"color\" value=\"" + d.colour + "\" id=\"colour-" + d.group + "\" " + (!enable ? "disabled" : "") + " />"; }); }); });
        return allEntries;
    };
    ;
    AdminControlCharts.prototype.renderGroupChart = function (chart, data) {
        d3.select("#" + chart.id + " .card-subtitle")
            .html(data.length != 1 ? "The reflections of the group code " + data[d3.minIndex(data.map(function (c) { return c.stats.find(function (d) { return d.stat == "q1"; }).value; }))].group + " tends to be distressed, while\n                the reflections of the group code " + data[d3.maxIndex(data.map(function (c) { return c.stats.find(function (d) { return d.stat == "q3"; }).value; }))].group + " tends to be soaring" :
                "You can add more group codes to this chart by selecting them from the left bar");
        //MinMax lines processing
        chart.elements.contentContainer.selectAll("#" + chart.id + "-data-min-max")
            .data(data)
            .join(function (enter) { return enter.append("line")
                .attr("id", chart.id + "-data-min-max")
                .attr("class", "box-line")
                .attr("x1", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                .attr("x2", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                .attr("y1", function (d) { return chart.y.scale(d.getStat("min").value); })
                .attr("y2", function (d) { return chart.y.scale(d.getStat("max").value); })
                .style("stroke", function (d) { return d.colour; })
                .call(function (enter) { return enter.transition().duration(750)
                    .attr("y2", function (d) { return chart.y.scale(d.getStat("max").value); }); }); }, function (update) { return update.style("stroke", function (d) { return d.colour; })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("x1", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                    .attr("x2", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                    .attr("y1", function (d) { return chart.y.scale(d.getStat("min").value); })
                    .attr("y2", function (d) { return chart.y.scale(d.getStat("max").value); }); }); }, function (exit) { return exit.style("stroke", "#b3b3b3")
                .call(function (exit) { return exit.transition()
                    .duration(250)
                    .attr("y2", function (d) { return chart.y.scale(d.getStat("min").value); })
                    .remove(); }); });
        //Median lines processing
        chart.elements.contentContainer.selectAll("#" + chart.id + "-data-median")
            .data(data)
            .join(function (enter) { return enter.append("line")
                .attr("id", chart.id + "-data-median")
                .attr("class", "box-line")
                .attr("x1", function (d) { return chart.x.scale(d.group); })
                .attr("x2", function (d) { return chart.x.scale(d.group); })
                .attr("y1", function (d) { return chart.y.scale(d.getStat("median").value); })
                .attr("y2", function (d) { return chart.y.scale(d.getStat("median").value); })
                .style("stroke", function (d) { return d.colour; })
                .call(function (enter) { return enter.transition()
                    .duration(750)
                    .attr("x2", function (d) { return chart.x.scale(d.group) + chart.x.scale.bandwidth(); }); }); }, function (update) { return update.style("stroke", function (d) { return d.colour; })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("x1", function (d) { return chart.x.scale(d.group); })
                    .attr("x2", function (d) { return chart.x.scale(d.group) + chart.x.scale.bandwidth(); })
                    .attr("y1", function (d) { return chart.y.scale(d.getStat("median").value); })
                    .attr("y2", function (d) { return chart.y.scale(d.getStat("median").value); }); }); }, function (exit) { return exit.style("stroke", "#b3b3b3")
                .call(function (exit) { return exit.transition()
                    .duration(250)
                    .attr("x2", function () { return parseInt(this.getAttribute("x1")); })
                    .remove(); }); });
        //Boxes processing
        chart.elements.content = chart.elements.contentContainer.selectAll("#" + chart.id + "-data")
            .data(data)
            .join(function (enter) { return enter.append("rect")
                .attr("id", chart.id + "-data")
                .attr("class", "bar")
                .attr("y", function (d) { return chart.y.scale(d.getStat("q1").value); })
                .attr("x", function (d) { return chart.x.scale(d.group); })
                .attr("width", chart.x.scale.bandwidth())
                .attr("height", 0)
                .style("stroke", function (d) { return d.colour; })
                .style("fill", function (d) { return d.colour; })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("height", function (d) { return chart.y.scale(d.getStat("q1").value) - chart.y.scale(d.getStat("q3").value); })
                    .attr("y", function (d) { return chart.y.scale(d.getStat("q3").value); }); }); }, function (update) { return update.style("stroke", function (d) { return d.colour; })
                .style("fill", function (d) { return d.colour; })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("y", function (d) { return chart.y.scale(d.getStat("q3").value); })
                    .attr("x", function (d) { return chart.x.scale(d.group); })
                    .attr("width", chart.x.scale.bandwidth())
                    .attr("height", function (d) { return chart.y.scale(d.getStat("q1").value) - chart.y.scale(d.getStat("q3").value); }); }); }, function (exit) { return exit.style("fill", "#cccccc")
                .style("stroke", "#b3b3b3")
                .call(function (exit) { return exit.transition()
                    .duration(250)
                    .attr("y", function (d) { return chart.y.scale(d.getStat("q1").value); })
                    .attr("height", 0)
                    .remove(); }); });
        var _this = this;
        //Enable tooltip
        this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            //If box is clicked not append tooltip
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.interactions.tooltip.appendTooltipContainer(chart);
            //Append tooltip box with text
            var tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.group, d.stats.filter(function (c, i) { return i < 4; }).map(function (c) { return new TooltipValues(c.displayName, c.value); }));
            //Position tooltip container
            _this.interactions.tooltip.positionTooltipContainer(chart, xTooltip(d.group, tooltipBox), yTooltip(d.getStat("q3").value, tooltipBox));
            function xTooltip(x, tooltipBox) {
                //Position tooltip right of the box
                var xTooltip = chart.x.scale(x) + chart.x.scale.bandwidth();
                //If tooltip does not fit position left of the box
                if (chart.width - chart.padding.yAxis < xTooltip + tooltipBox.node().getBBox().width) {
                    return xTooltip - chart.x.scale.bandwidth() - tooltipBox.node().getBBox().width;
                }
                return xTooltip;
            }
            function yTooltip(y, tooltipBox) {
                //Position tooltip on top of the box
                var yTooltip = chart.y.scale(y) - (tooltipBox.node().getBBox().height / 2);
                //If tooltip does not fit position at the same height as the box
                if (chart.y.scale.invert(yTooltip) < 0) {
                    return chart.y.scale(y + chart.y.scale.invert(yTooltip));
                }
                return yTooltip;
            }
        }
        function onMouseout() {
            //Transition tooltip to opacity 0
            chart.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            //Remove tooltip
            _this.interactions.tooltip.removeTooltip(chart);
        }
        return chart;
    };
    AdminControlCharts.prototype.renderHistogram = function (chart, data) {
        var _this_1 = this;
        chart.setBandwidth(data);
        chart.setBin();
        var binData = data.map(function (d) { return chart.bin(d.value.map(function (d) { return d.point; })).map(function (c) { return new HistogramData(d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }); });
        d3.select("#" + chart.id + " .card-subtitle")
            .html(data.length != 1 ? "The group code " + binData[d3.maxIndex(binData.map(function (d) { return d3.max(d.filter(function (d) { return d.bin.x0 == 0; }).map(function (d) { return d.percentage; })); }))][0].group + " has the highest percentage in the distressed bin, while\n                the group code " + binData[d3.maxIndex(binData.map(function (d) { return d3.max(d.filter(function (d) { return d.bin.x1 == 100; }).map(function (d) { return d.percentage; })); }))][0].group + " has the highest percentage in the soaring bin" :
                "Filtering by <span class=\"badge badge-pill badge-info\">" + data[0].group + " <i class=\"fas fa-window-close\"></i></span>");
        //Process histogram
        chart.elements.contentContainer.selectAll("." + chart.id + "-histogram-container")
            .data(data)
            .join(function (enter) { return enter.append("g")
                .attr("class", chart.id + "-histogram-container")
                .attr("transform", function (d) { return "translate(" + chart.x.scale(d.group) + ", 0)"; })
                .call(function (enter) { return enter.selectAll(".histogram-rect")
                    .data(function (d) { return chart.bin(d.value.map(function (d) { return d.point; })).map(function (c) { return new HistogramData(d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }); })
                    .enter()
                    .append("rect")
                    .attr("id", chart.id + "-data")
                    .attr("class", "histogram-rect")
                    .attr("x", function (c) { return chart.bandwidth(-c.percentage); })
                    .attr("y", function (c) { return chart.y.scale(c.bin.x0); })
                    .attr("height", 0)
                    .attr("width", function (c) { return chart.bandwidth(c.percentage) - chart.bandwidth(-c.percentage); })
                    .style("stroke", function (c) { return c.colour; })
                    .style("fill", function (c) { return c.colour; })
                    .transition()
                    .duration(750)
                    .attr("y", function (c) { return chart.y.scale(c.bin.x1); })
                    .attr("height", function (c) { return chart.y.scale(c.bin.x0) - chart.y.scale(c.bin.x1); }); }); }, function (update) { return update
                .call(function (update) { return _this_1.interactions.histogram(chart, update); })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("transform", function (d) { return "translate(" + chart.x.scale(d.group) + ", 0)"; }); }); }, function (exit) { return exit
                .call(function (exit) { return exit.selectAll(".histogram-rect")
                    .style("fill", "#cccccc")
                    .style("stroke", "#b3b3b3")
                    .transition()
                    .duration(250)
                    .attr("y", function (c) { return chart.y.scale(c.bin.x0); })
                    .attr("height", 0); })
                .call(function (exit) { return exit.transition()
                    .duration(250)
                    .remove(); }); });
        chart.elements.content = chart.elements.contentContainer.selectAll("#" + chart.id + "-data");
        //Append tooltip container
        this.handleHistogramHover(chart);
        return chart;
    };
    ;
    AdminControlCharts.prototype.handleHistogramHover = function (chart) {
        var _this = this;
        _this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            _this.interactions.tooltip.appendTooltipContainer(chart);
            var tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.bin.x0 == 0 ? "Distressed" : d.bin.x1 == 100 ? "Soaring" : "GoingOK", [new TooltipValues("Total", d.bin.length + " (" + d.percentage + "%)")]);
            _this.interactions.tooltip.positionTooltipContainer(chart, chart.x.scale(d.group) + chart.bandwidth(d.bin.length), d.bin.x1 > 25 ? chart.y.scale(d.bin.x1) : chart.y.scale(d.bin.x0) - tooltipBox.node().getBBox().height);
        }
        function onMouseout() {
            chart.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            _this.interactions.tooltip.removeTooltip(chart);
        }
    };
    AdminControlCharts.prototype.renderTimelineDensity = function (chart, data) {
        var _this = this;
        d3.select("#" + chart.id + " .card-subtitle")
            .html(data.length != 1 ? "The oldest reflection was on " + d3.min(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); })).toDateString() + " in the group code " + data[d3.minIndex(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); }))].group + ", while\n                the newest reflection was on " + d3.max(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); })).toDateString() + " in the group code " + data[d3.maxIndex(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); }))].group :
                "Filtering by <span class=\"badge badge-pill badge-info\">" + data[0].group + " <i class=\"fas fa-window-close\"></i></span>");
        //Remove scatter plot
        chart.elements.contentContainer.selectAll(".circle").remove();
        chart.elements.svg.selectAll(".zoom-container").remove();
        chart.elements.contentContainer.selectAll(".click-line").remove();
        chart.elements.zoomSVG = undefined;
        chart.elements.zoomFocus = undefined;
        drawContours();
        //Draw contours function
        function drawContours() {
            chart.elements.content = chart.elements.contentContainer.selectAll(".timeline-container")
                .data(data)
                .join(function (enter) { return enter.append("g")
                    .attr("class", "timeline-container")
                    .attr("stroke", function (d) { return d.colour; })
                    .attr("fill", function (d) { return d.colour; })
                    .call(function (enter) { return _this.interactions.timelineDensity(enter, getDensityData); }); }, function (update) { return update.attr("stroke", function (d) { return d.colour; })
                    .attr("fill", function (d) { return d.colour; })
                    .call(function (update) { return _this.interactions.timelineDensity(update, getDensityData); }); }, function (exit) { return exit.remove(); });
            function getDensityData(data) {
                return d3.contourDensity()
                    .x(function (d) { return chart.x.scale(d.timestamp); })
                    .y(function (d) { return chart.y.scale(d.point); })
                    .bandwidth(5)
                    .thresholds(20)
                    .size([chart.width - chart.padding.yAxis, chart.height - chart.padding.xAxis - chart.padding.top])(data.value);
            }
        }
        //Enable zoom
        this.interactions.zoom.enableZoom(chart, zoomed);
        function zoomed(e) {
            var newChartRange = [0, chart.width - chart.padding.yAxis].map(function (d) { return e.transform.applyX(d); });
            chart.x.scale.rangeRound(newChartRange);
            drawContours();
            chart.x.axis.ticks(newChartRange[1] / 75);
            chart.elements.xAxis.call(chart.x.axis);
            _this.htmlContainers.removeHelp(chart);
        }
        return chart;
    };
    ;
    AdminControlCharts.prototype.renderTimelineScatter = function (chart, zoomChart, data) {
        //Remove density plot
        chart.elements.contentContainer.selectAll(".contour").remove();
        var _this = this;
        d3.select("#" + chart.id + " .card-subtitle")
            .html(data.length != 1 ? "The oldest reflection was on " + d3.min(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); })).toDateString() + " in the group code " + data[d3.minIndex(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); }))].group + ", while\n                the newest reflection was on " + d3.max(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); })).toDateString() + " in the group code " + data[d3.maxIndex(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); }))].group :
                "Filtering by <span class=\"badge badge-pill badge-info\">" + data[0].group + " <i class=\"fas fa-window-close\"></i></span>");
        //Draw circles
        chart.elements.contentContainer.selectAll(".timeline-container")
            .data(data)
            .join(function (enter) { return enter.append("g")
                .attr("class", "timeline-container")
                .call(function (enter) { return _this.interactions.timelineScatter(enter, chart); }); }, function (update) { return update.call(function (update) { return _this.interactions.timelineScatter(update, chart); }); }, function (exit) { return exit.remove(); });
        chart.elements.content = chart.elements.contentContainer.selectAll(".circle");
        //Enable tooltip
        _this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.interactions.tooltip.appendTooltipContainer(chart);
            var tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.timestamp.toDateString(), [new TooltipValues("User", d.pseudonym),
                new TooltipValues("Point", d.point)]);
            _this.interactions.tooltip.positionTooltipContainer(chart, xTooltip(d.timestamp, tooltipBox), yTooltip(d.point, tooltipBox));
            function xTooltip(x, tooltipBox) {
                var xTooltip = chart.x.scale(x);
                if (chart.width - chart.padding.yAxis < xTooltip + tooltipBox.node().getBBox().width) {
                    return xTooltip - tooltipBox.node().getBBox().width;
                }
                return xTooltip;
            }
            ;
            function yTooltip(y, tooltipBox) {
                var yTooltip = chart.y.scale(y) - tooltipBox.node().getBBox().height - 10;
                if (yTooltip < 0) {
                    return yTooltip + tooltipBox.node().getBBox().height + 20;
                }
                return yTooltip;
            }
            ;
            _this.interactions.tooltip.appendLine(chart, 0, chart.y.scale(d.point), chart.x.scale(d.timestamp), chart.y.scale(d.point), d.colour);
            _this.interactions.tooltip.appendLine(chart, chart.x.scale(d.timestamp), chart.y.scale(0), chart.x.scale(d.timestamp), chart.y.scale(d.point), d.colour);
        }
        function onMouseout() {
            chart.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            _this.interactions.tooltip.removeTooltip(chart);
        }
        //Append zoom bar
        if (chart.elements.zoomSVG == undefined) {
            chart.elements.zoomSVG = _this.interactions.zoom.appendZoomBar(chart);
            chart.elements.zoomFocus = chart.elements.zoomSVG.append("g")
                .attr("class", "zoom-focus");
        }
        //Process zoom circles
        chart.elements.zoomFocus.selectAll(".zoom-timeline-content-container")
            .data(data)
            .join(function (enter) { return enter.append("g")
                .attr("class", "zoom-timeline-content-container")
                .call(function (enter) { return _this.interactions.timelineScatter(enter, zoomChart, true, true); }); }, function (update) { return update.call(function (update) { return _this.interactions.timelineScatter(update, zoomChart, true, true); }); }, function (exit) { return exit.remove(); });
        chart.elements.zoomSVG.selectAll(".zoom-timeline-container")
            .data(data)
            .join(function (enter) { return enter.append("g")
                .attr("class", "zoom-timeline-container")
                .call(function (enter) { zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis]); _this.interactions.timelineScatter(enter, zoomChart, true); }); }, function (update) { return update.call(function (update) { zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis]); _this.interactions.timelineScatter(update, zoomChart, true); }); }, function (exit) { return exit.remove(); });
        //Enable zoom
        _this.interactions.zoom.enableZoom(chart, zoomed);
        function zoomed(e) {
            var newChartRange = [0, chart.width - chart.padding.yAxis].map(function (d) { return e.transform.applyX(d); });
            chart.x.scale.rangeRound(newChartRange);
            zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis - 5].map(function (d) { return e.transform.invertX(d); }));
            var newLine = d3.line()
                .x(function (d) { return chart.x.scale(d.timestamp); })
                .y(function (d) { return chart.y.scale(d.point); });
            chart.elements.contentContainer.selectAll(".circle")
                .attr("cx", function (d) { return chart.x.scale(d.timestamp); });
            chart.elements.zoomFocus.selectAll(".zoom-content")
                .attr("cx", function (d) { return zoomChart.x.scale(d.timestamp); });
            chart.elements.contentContainer.selectAll(".click-line")
                .attr("d", function (d) { return newLine(d); });
            chart.elements.contentContainer.selectAll(".click-container")
                .attr("transform", function (d) { return "translate(" + chart.x.scale(d.timestamp) + ", " + chart.y.scale(d.point) + ")"; });
            chart.x.axis.ticks(newChartRange[1] / 75);
            chart.elements.xAxis.call(chart.x.axis);
            _this.htmlContainers.removeHelp(chart);
        }
        return chart;
    };
    ;
    AdminControlCharts.prototype.renderTimelineButtons = function (card) {
        card.insert("div", ".chart-container")
            .attr("class", "row mt-3")
            .append("div")
            .attr("id", "timeline-plot")
            .attr("class", "btn-group btn-group-toggle mr-auto ml-auto")
            .attr("data-toggle", "buttons")
            .call(function (div) { return div.append("label")
                .attr("class", "btn btn-light active")
                .html("<input type=\"radio\" name=\"plot\" value=\"density\" checked>Density Plot<br>"); })
            .call(function (div) { return div.append("label")
                .attr("class", "btn btn-light")
                .html("<input type=\"radio\" name=\"plot\" value=\"scatter\">Scatter Plot<br>"); });
    };
    AdminControlCharts.prototype.handleTimelineButtons = function (chart, zoomChart, data, func) {
        var _this = this;
        d3.select("#" + chart.id + " #timeline-plot").on("click", func != undefined ? function (e) { return func(e); } : function (e) {
            var selectedOption = e.target.control.value;
            if (selectedOption == "density") {
                _this.renderTimelineDensity(chart, data);
            }
            if (selectedOption == "scatter") {
                _this.renderTimelineScatter(chart, zoomChart, data);
            }
            if (!d3.select("#" + chart.id + "-help").empty()) {
                _this.htmlContainers.removeHelp(chart);
            }
        });
    };
    ;
    AdminControlCharts.prototype.renderUserStatistics = function (card, data, thresholds, pseudonym) {
        var _this = this;
        var userData = data.getUsersData();
        var groupMean = Math.round(d3.mean(data.value.map(function (d) { return d.point; })));
        d3.select("#user-statistics .card-subtitle")
            .html(pseudonym == undefined ? "The user " + userData.value[d3.minIndex(userData.value.map(function (d) { return d.point; }))].pseudonym + " is the most distressed, while\n                the user " + userData.value[d3.maxIndex(userData.value.map(function (d) { return d.point; }))].pseudonym + " in the most soaring" :
                "The user " + pseudonym + " has a total of " + data.value.filter(function (d) { return d.pseudonym == pseudonym; }).length + " reflections between\n                " + d3.min(data.value.filter(function (d) { return d.pseudonym == pseudonym; }).map(function (d) { return d.timestamp; })).toDateString() + " and\n                " + d3.max(data.value.filter(function (d) { return d.pseudonym == pseudonym; }).map(function (d) { return d.timestamp; })).toDateString());
        card.selectAll("div")
            .data(pseudonym == undefined ? userData.value : userData.value.filter(function (d) { return d.pseudonym == pseudonym; }))
            .enter()
            .append("div")
            .attr("class", "row statistics-text")
            .attr("id", function (d) { return d.pseudonym; })
            .call(function (div) { return div.append("div")
                .attr("class", "col-md-4")
                .call(function (div) { return div.append("h6")
                    .attr("class", "mb-0 mt-1")
                    .html(function (d) { return d.pseudonym + " (<span class=\"" + ((d.point - groupMean) < 0 ? "negative" : "positive") + "\">" + ((d.point - groupMean) < 0 ? "" : "+") + (d.point - groupMean) + "</span>)"; }); })
                .call(function (div) { return div.append("span")
                    .attr("class", "text-muted")
                    .html(function (d) { return "<b>" + _this.getUserStaitsticBinName(d, thresholds) + "</b>"; }); }); })
            .call(function (div) { return div.append("div")
                .attr("class", "col-md-8 mt-1 user-chart"); })
            .call(function (div) { return div.append("div")
                .attr("class", "col-md-12")
                .selectAll("p")
                .data(function (d) { return d3.sort(d3.filter(data.value, function (x) { return x.pseudonym == d.pseudonym; }), function (r) { return r.timestamp; }); })
                .enter()
                .append("p")
                .html(function (d) { return "<i>" + d.timestamp.toDateString() + ":</i> " + d.text; }); })
            .each(function (d, i, g) { return drawUserChart(d3.select(d3.select(g[i]).node().parentElement).attr("id") + " #" + d3.select(g[i]).attr("id"), d); });
        function drawUserChart(id, data) {
            var chart = new UserChart(id, "user-chart");
            if (chart.x.scale(data.point) < 0) {
                console.log(data, chart.x.scale(data.point), id);
            }
            chart.elements.svg.select(".y-axis").remove();
            chart.elements.svg.select(".x-axis").attr("clip-path", null);
            chart.elements.contentContainer.append("rect")
                .attr("class", "bar no-click")
                .attr("x", 0)
                .attr("y", 0)
                .attr("height", chart.y.scale(0))
                .attr("width", chart.x.scale(data.point))
                .style("stroke", userData.colour)
                .style("fill", userData.colour);
            chart.elements.contentContainer.append("line")
                .attr("class", "threshold-line")
                .attr("x1", chart.x.scale(groupMean))
                .attr("x2", chart.x.scale(groupMean))
                .attr("y1", chart.y.scale(1))
                .attr("y2", chart.y.scale(0))
                .style("stroke", userData.colour);
            chart.elements.contentContainer.append("text")
                .attr("x", chart.x.scale(groupMean))
                .attr("y", chart.y.scale(1))
                .attr("font-size", 10)
                .attr("font-family", "sans-serif")
                .attr("text-anchor", "middle")
                .text("Group average: " + groupMean);
            chart.elements.contentContainer.append("text")
                .attr("x", chart.x.scale(data.point))
                .attr("y", chart.y.scale(0.5))
                .attr("font-family", "sans-serif")
                .style("dominant-baseline", "central")
                .text(data.point);
        }
    };
    ;
    AdminControlCharts.prototype.getUserStaitsticBinName = function (data, thresholds) {
        var distressed = thresholds[0];
        var soaring = thresholds[1];
        if (data.point <= distressed) {
            return "Distressed";
        }
        else if (data.point >= soaring) {
            return "Soaring";
        }
        else {
            return "GoingOK";
        }
    };
    return AdminControlCharts;
}());
var AdminControlTransitions = /** @class */ (function () {
    function AdminControlTransitions() {
    }
    AdminControlTransitions.prototype.axisSeries = function (chart, data) {
        chart.x.scale.domain(data.map(function (d) { return d.group; }));
        d3.select("#" + chart.id + " .x-axis").transition()
            .duration(750)
            .call(chart.x.axis);
    };
    ;
    AdminControlTransitions.prototype.axisTime = function (chart, data) {
        chart.x.scale.domain([d3.min(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); })), d3.max(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); }))]);
        d3.select("#" + chart.id + " .x-axis").transition()
            .duration(750)
            .call(chart.x.axis);
    };
    ;
    AdminControlTransitions.prototype.histogram = function (chart, update) {
        update.selectAll(".histogram-rect")
            .data(function (d) { return chart.bin(d.value.map(function (d) { return d.point; })).map(function (c) { return new HistogramData(d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }); })
            .join(function (enter) { return enter; }, function (update) { return update.style("stroke", function (d) { return d.colour; })
                .style("fill", function (d) { return d.colour; })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("x", function (d) { return chart.bandwidth(-d.percentage); })
                    .attr("y", function (d) { return chart.y.scale(d.bin.x1); })
                    .attr("height", function (d) { return chart.y.scale(d.bin.x0) - chart.y.scale(d.bin.x1); })
                    .attr("width", function (d) { return chart.bandwidth(d.percentage) - chart.bandwidth(-d.percentage); }); }); }, function (exit) { return exit; });
    };
    ;
    AdminControlTransitions.prototype.timelineDensity = function (update, getDensityData) {
        update.selectAll(".contour")
            .data(function (d) { return getDensityData(d); })
            .join(function (enter) { return enter.append("path")
                .attr("class", "contour")
                .attr("d", d3.geoPath())
                .attr("opacity", function (d) { return d.value * 25; }); }, function (update) { return update.attr("d", d3.geoPath())
                .attr("opacity", function (d) { return d.value * 20; }); }, function (exit) { return exit.remove(); });
    };
    ;
    AdminControlTransitions.prototype.timelineScatter = function (update, chart, zoom, invisible) {
        if (zoom === void 0) { zoom = false; }
        if (invisible === void 0) { invisible = false; }
        update.selectAll("circle")
            .data(function (d) { return d.value.map(function (c) { return new TimelineData(c, d.colour, d.group); }); })
            .join(function (enter) { return enter.append("circle")
                .attr("class", invisible ? "zoom-content" : zoom ? "circle no-hover" : "circle")
                .attr("r", zoom ? 2 : 5)
                .attr("cx", function (d) { return chart.x.scale(d.timestamp); })
                .attr("cy", function (d) { return chart.y.scale(d.point); })
                .attr("fill", function (d) { return d.colour; })
                .attr("stroke", function (d) { return d.colour; }); }, function (update) { return update.attr("fill", function (d) { return d.colour; })
                .attr("stroke", function (d) { return d.colour; })
                .call(function (update) { return update.transition()
                    .duration(750)
                    .attr("cx", function (d) { return chart.x.scale(d.timestamp); })
                    .attr("cy", function (d) { return chart.y.scale(d.point); }); }); }, function (exit) { return exit.remove(); });
    };
    ;
    return AdminControlTransitions;
}());
var AdminControlInteractions = /** @class */ (function (_super) {
    __extends(AdminControlInteractions, _super);
    function AdminControlInteractions() {
        var _this_1 = _super !== null && _super.apply(this, arguments) || this;
        _this_1.tooltip = new Tooltip();
        _this_1.zoom = new Zoom();
        return _this_1;
    }
    return AdminControlInteractions;
}(AdminControlTransitions));
// Basic class for tooltip content interaction
var TooltipValues = /** @class */ (function () {
    function TooltipValues(label, value) {
        this.label = label == undefined ? "" : label;
        this.value = value == undefined ? 0 : value;
    }
    return TooltipValues;
}());
// Class for tooltip interaction
var Tooltip = /** @class */ (function () {
    function Tooltip() {
    }
    Tooltip.prototype.enableTooltip = function (chart, onMouseover, onMouseout) {
        chart.elements.content.on("mouseover", onMouseover);
        chart.elements.content.on("mouseout", onMouseout);
    };
    ;
    Tooltip.prototype.removeTooltip = function (chart) {
        chart.elements.contentContainer.selectAll(".tooltip-container").remove();
        chart.elements.contentContainer.selectAll(".tooltip-line").remove();
    };
    ;
    Tooltip.prototype.appendTooltipContainer = function (chart) {
        chart.elements.contentContainer.append("g")
            .attr("class", "tooltip-container");
    };
    ;
    Tooltip.prototype.appendTooltipText = function (chart, title, values) {
        if (values === void 0) { values = null; }
        var result = chart.elements.contentContainer.select(".tooltip-container").append("rect")
            .attr("class", "tooltip-box");
        var text = chart.elements.contentContainer.select(".tooltip-container").append("text")
            .attr("class", "tooltip-text title")
            .attr("x", 10)
            .text(title);
        var textSize = text.node().getBBox().height;
        text.attr("y", textSize);
        if (values != null) {
            values.forEach(function (c, i) {
                text.append("tspan")
                    .attr("class", "tooltip-text")
                    .attr("y", textSize * (i + 2))
                    .attr("x", 15)
                    .text(c.label + ": " + c.value);
            });
        }
        return result.attr("width", text.node().getBBox().width + 20)
            .attr("height", text.node().getBBox().height + 5);
    };
    ;
    Tooltip.prototype.positionTooltipContainer = function (chart, x, y) {
        chart.elements.contentContainer.select(".tooltip-container")
            .attr("transform", "translate(" + x + ", " + y + ")")
            .transition()
            .style("opacity", 1);
    };
    ;
    Tooltip.prototype.appendLine = function (chart, x1, y1, x2, y2, colour) {
        chart.elements.contentContainer.append("line")
            .attr("class", "tooltip-line")
            .attr("x1", x1)
            .attr("y1", y1)
            .attr("x2", x2)
            .attr("y2", y2)
            .style("stroke", colour);
    };
    ;
    return Tooltip;
}());
// Class for zoom interaction
var Zoom = /** @class */ (function () {
    function Zoom() {
    }
    Zoom.prototype.enableZoom = function (chart, zoomed) {
        chart.elements.svg.selectAll(".zoom-rect")
            .attr("class", "zoom-rect active");
        var zoom = d3.zoom()
            .scaleExtent([1, 5])
            .extent([[0, 0], [chart.width - chart.padding.yAxis, chart.height]])
            .translateExtent([[0, 0], [chart.width - chart.padding.yAxis, chart.height]])
            .on("zoom", zoomed);
        chart.elements.contentContainer.select(".zoom-rect").call(zoom);
    };
    ;
    Zoom.prototype.appendZoomBar = function (chart) {
        return chart.elements.svg.append("g")
            .attr("class", "zoom-container")
            .attr("height", 30)
            .attr("width", chart.width - chart.padding.yAxis)
            .attr("transform", "translate(" + chart.padding.yAxis + ", " + (chart.height - 30) + ")");
    };
    ;
    return Zoom;
}());
var AdminExperimentalCharts = /** @class */ (function (_super) {
    __extends(AdminExperimentalCharts, _super);
    function AdminExperimentalCharts() {
        var _this_1 = _super !== null && _super.apply(this, arguments) || this;
        _this_1.sorted = "date";
        _this_1.interactions = new AdminExperimentalInteractions();
        return _this_1;
    }
    AdminExperimentalCharts.prototype.preloadGroups = function (allEntries) {
        _super.prototype.preloadGroups.call(this, allEntries, true);
        this.allEntries = allEntries;
        return d3.filter(allEntries, function (d) { return d.selected == true; });
    };
    ;
    AdminExperimentalCharts.prototype.handleGroups = function () {
        var _this = this;
        function updateGroupChart(chart, data) {
            chart.x.scale.domain(data.map(function (d) { return d.group; }));
            _this.interactions.axisSeries(chart, data);
            _this.renderGroupChart(chart, data);
        }
        d3.selectAll("#groups input[type=checkbox]").on("change", function (e) {
            var target = e.target;
            if (target.checked) {
                _this.allEntries.find(function (d) { return d.group == target.value; }).selected = true;
            }
            else {
                _this.allEntries.find(function (d) { return d.group == target.value; }).selected = false;
            }
            var data = _this.getUpdatedData();
            var clickData = _this.getClickData(_this.boxPlot.elements.contentContainer);
            updateGroupChart(_this.boxPlot, data);
            if (_this.boxPlot.click) {
                if (!target.checked && target.value == clickData.group) {
                    _this.interactions.click.removeClick(_this.boxPlot);
                    _this.updateTimeline(data);
                    _this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }), data.map(function (d) { return d.group; }));
                }
                else {
                    _this.interactions.click.appendGroupsText(_this.boxPlot, data, clickData);
                }
            }
            else {
                _this.updateTimeline(data);
                _this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }), data.map(function (d) { return d.group; }));
            }
            _this.removeAllHelp(_this.boxPlot);
        });
    };
    ;
    AdminExperimentalCharts.prototype.handleGroupsColours = function () {
        var _this = this;
        d3.selectAll("#groups input[type=color]").on("change", function (e) {
            var target = e.target;
            var groupId = target.id.replace("colour-", "");
            _this.allEntries.find(function (d) { return d.group == groupId; }).colour = target.value;
            var data = _this.getUpdatedData();
            _this.renderGroupChart(_this.boxPlot, data);
            if (_this.boxPlot.click) {
                var clickData = _this.getClickData(_this.boxPlot.elements.contentContainer);
                if (clickData.group == groupId) {
                    _this.updateTimeline([_this.allEntries.find(function (d) { return d.group == groupId; })]);
                    _this.updateHistograms([clickData], [clickData.getUsersData()]);
                }
            }
            else {
                _this.updateTimeline(data);
                _this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }));
            }
        });
    };
    ;
    AdminExperimentalCharts.prototype.handleGroupsSort = function () {
        var _this = this;
        d3.select("#groups-chart #sort-by").on("click", function (e) {
            var selectedOption = e.target.control.value;
            _this.allEntries = _this.allEntries.sort(function (a, b) {
                if (selectedOption == "date") {
                    return _this.interactions.sort.sortData(a.creteDate, b.creteDate, _this.sorted == "date" ? true : false);
                }
                else if (selectedOption == "name") {
                    return _this.interactions.sort.sortData(a.group, b.group, _this.sorted == "name" ? true : false);
                }
                else if (selectedOption == "mean") {
                    return _this.interactions.sort.sortData(d3.mean(a.value.map(function (d) { return d.point; })), d3.mean(b.value.map(function (d) { return d.point; })), _this.sorted == "mean" ? true : false);
                }
            });
            _this.sorted = _this.interactions.sort.setSorted(_this.sorted, selectedOption);
            var data = _this.getUpdatedData();
            _this.boxPlot.x.scale.domain(data.map(function (r) { return r.group; }));
            _this.interactions.axisSeries(_this.boxPlot, data);
            var groupClickData = _this.getClickData(_this.boxPlot.elements.contentContainer);
            _this.renderGroupChart(_this.boxPlot, data);
            if (_this.boxPlot.click) {
                _this.interactions.click.appendGroupsText(_this.boxPlot, data, groupClickData);
            }
            else {
                _this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }), data.map(function (r) { return r.group; }));
            }
            _this.removeAllHelp(_this.boxPlot);
        });
    };
    ;
    AdminExperimentalCharts.prototype.handleFilterButton = function () {
        var data = this.getUpdatedData();
        this.interactions.click.removeClick(this.boxPlot);
        this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }), data.map(function (d) { return d.group; }));
        this.updateTimeline(data);
    };
    ;
    AdminExperimentalCharts.prototype.getUpdatedData = function () {
        return d3.filter(this.allEntries, function (d) { return d.selected; }).map(function (d) { return new AnalyticsChartsDataStats(d); });
    };
    ;
    AdminExperimentalCharts.prototype.getClickData = function (contentContainer) {
        if (!contentContainer.select(".clicked").empty()) {
            return contentContainer.select(".clicked").datum();
        }
        return;
    };
    ;
    AdminExperimentalCharts.prototype.updateHistograms = function (data, usersData, scale) {
        if (scale != undefined) {
            this.histogram.x.scale.domain(scale);
            this.usersHistogram.x.scale.domain(scale);
        }
        this.renderHistogram(this.histogram, data);
        this.renderHistogram(this.usersHistogram, usersData);
        this.interactions.axisSeries(this.histogram, data);
        this.interactions.axisSeries(this.usersHistogram, usersData);
        if (this.histogram.click) {
            this.interactions.click.removeClick(this.histogram);
        }
        if (this.usersHistogram.click) {
            this.interactions.click.removeClick(this.usersHistogram);
        }
    };
    ;
    AdminExperimentalCharts.prototype.updateTimeline = function (data) {
        this.timeline.x.scale.domain([d3.min(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); })), d3.max(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); }))]);
        this.timelineZoom.x.scale.domain([d3.min(data.map(function (d) { return d3.min(d.value.map(function (d) { return d.timestamp; })); })), d3.max(data.map(function (d) { return d3.max(d.value.map(function (d) { return d.timestamp; })); }))]);
        this.interactions.axisTime(this.timeline, data);
        if (this.timeline.elements.contentContainer.selectAll(".contour").empty()) {
            this.renderTimelineScatter(this.timeline, this.timelineZoom, data);
        }
        else {
            this.renderTimelineDensity(this.timeline, data);
        }
        if (this.timeline.click) {
            this.interactions.click.removeClick(this.timeline);
            this.removeUserStatistics();
        }
        this.handleTimelineButtons(this.timeline, this.timelineZoom, data);
    };
    ;
    AdminExperimentalCharts.prototype.removeUserStatistics = function () {
        d3.select("#user-statistics .card-title")
            .html("Users compared to their group");
        d3.select("#user-statistics .card-subtitle")
            .html("Select a reflection from the scatter plot to view specific users");
        d3.select("#user-statistics .users-tab-pane").remove();
    };
    ;
    AdminExperimentalCharts.prototype.removeAllHelp = function (boxPlot) {
        this.htmlContainers.removeHelp(boxPlot);
        this.htmlContainers.removeHelp(this.histogram);
        this.htmlContainers.removeHelp(this.usersHistogram);
        this.htmlContainers.removeHelp(this.timeline);
    };
    AdminExperimentalCharts.prototype.renderGroupChart = function (chart, data) {
        chart = _super.prototype.renderGroupChart.call(this, chart, data);
        var _this = this;
        _this.interactions.click.enableClick(chart, onClick);
        chart.elements.contentContainer.select(".zoom-rect").on("click", function () {
            _this.interactions.click.removeClick(chart);
            _this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }), data.map(function (d) { return d.group; }));
            _this.updateTimeline(data);
        });
        function onClick(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                _this.interactions.click.removeClick(chart);
                _this.updateHistograms(data, data.map(function (d) { return d.getUsersData(); }), data.map(function (d) { return d.group; }));
                _this.updateTimeline(data);
                return;
            }
            _this.interactions.click.removeClick(chart);
            chart.click = true;
            _this.interactions.click.appendGroupsText(chart, data, d);
            _this.updateHistograms([d], [d.getUsersData()], data.filter(function (c) { return c.group == d.group; }).map(function (d) { return d.group; }));
            _this.updateTimeline([d]);
            _this.removeAllHelp(chart);
        }
        return chart;
    };
    ;
    AdminExperimentalCharts.prototype.renderHistogram = function (chart, data) {
        var _this = this;
        chart = _super.prototype.renderHistogram.call(this, chart, data);
        d3.select("#" + chart.id + " .badge").on("click", function () { return _this.handleFilterButton(); });
        chart.elements.contentContainer.select(".zoom-rect").on("click", function () {
            _this.interactions.click.removeClick(chart);
        });
        //Add drag functions to the distressed threshold
        chart.elements.contentContainer.selectAll(".threshold-line")
            .classed("grab", true)
            .call(d3.drag()
                .on("start", dragStart)
                .on("drag", dragging)
                .on("end", dragEnd));
        //Start dragging functions
        function dragStart() {
            chart.elements.contentContainer.selectAll("." + chart.id + "-histogram-text-container").remove();
            d3.select(this).classed("grab", false);
            d3.select(this).classed("grabbing", true);
            _this.htmlContainers.removeHelp(chart);
        }
        function dragging(e, d) {
            if (d > 50) {
                if (chart.y.scale.invert(e.y) < 51 || chart.y.scale.invert(e.y) > 99) {
                    return;
                }
            }
            else {
                if (chart.y.scale.invert(e.y) < 1 || chart.y.scale.invert(e.y) > 49) {
                    return;
                }
            }
            var thresholds = chart.elements.getThresholdsValues(chart);
            var tDistressed = thresholds[0];
            var tSoaring = thresholds[1];
            d3.select(this)
                .datum(chart.y.scale.invert(e.y))
                .attr("y1", function (d) { return chart.y.scale(d); })
                .attr("y2", function (d) { return chart.y.scale(d); })
                .call(function (line) { return chart.thresholdAxis
                    .tickValues(line.datum() > 50 ? [tDistressed, line.datum()] : [line.datum(), tSoaring])
                    .tickFormat(function (d) { return line.datum() > 50 ? d == tDistressed ? "Distressed" : d == line.datum() ? "Soaring" : "" : d == line.datum() ? "Distressed" : d == tSoaring ? "Soaring" : ""; }); })
                .call(function (line) { return chart.elements.contentContainer.selectAll(".threshold-axis")
                    .call(chart.thresholdAxis); })
                .call(function (line) { return chart.elements.contentContainer.select(".threshold-indicator-container." + (line.datum() > 50 ? "soaring" : "distressed"))
                    .attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right + 5) + ", " + ((line.datum() > 85 && d > 50) || (line.datum() > 15 && d < 50) ? chart.y.scale(line.datum()) + 25 : chart.y.scale(line.datum()) - 15) + ")")
                    .select("text")
                    .text(Math.round(line.datum())); });
        }
        function dragEnd() {
            chart.setBin();
            _this.interactions.histogram(chart, chart.elements.contentContainer.selectAll("." + chart.id + "-histogram-container"));
            d3.select(this).classed("grabbing", false);
            d3.select(this).classed("grab", true);
            _this.handleHistogramHover(chart);
            if (chart.click) {
                var clickData = chart.elements.contentContainer.select(".clicked").datum();
                _this.interactions.click.appendThresholdPercentages(chart, data, clickData);
            }
            if (chart.id == "group-histogram-users-chart" && !_this.timeline.elements.contentContainer.selectAll(".clicked").empty()) {
                var userData_1 = _this.timeline.elements.contentContainer.selectAll(".clicked").datum();
                var binName = _this.getUserStaitsticBinName(data.map(function (d) { return d.value.find(function (d) { return d.pseudonym == userData_1.pseudonym; }); })[0], chart.elements.getThresholdsValues(chart));
                d3.select("#user-statistics #" + userData_1.pseudonym + " .text-muted")
                    .html("<b>" + binName + "</b>");
            }
        }
        _this.interactions.click.enableClick(chart, onClick);
        function onClick(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                _this.interactions.click.removeClick(chart);
                return;
            }
            _this.interactions.click.removeClick(chart);
            chart.click = true;
            _this.interactions.click.appendThresholdPercentages(chart, data, d);
        }
        return chart;
    };
    ;
    AdminExperimentalCharts.prototype.renderTimelineScatter = function (chart, zoomChart, data) {
        var _this = this;
        chart = _super.prototype.renderTimelineScatter.call(this, chart, zoomChart, data);
        d3.select("#" + chart.id + " .badge").on("click", function () { return _this.handleFilterButton(); });
        //Enable click
        _this.interactions.click.enableClick(chart, onClick);
        chart.elements.contentContainer.select(".zoom-rect").on("click", function () {
            _this.interactions.click.removeClick(chart);
            _this.removeUserStatistics();
        });
        function onClick(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                _this.interactions.click.removeClick(chart);
                _this.removeUserStatistics();
                return;
            }
            _this.interactions.click.removeClick(chart);
            //Remove users html containers
            _this.removeUserStatistics();
            chart.click = true;
            chart.elements.content.attr("class", function (data) { return "circle " + (data.pseudonym == d.pseudonym ? "clicked" : ""); });
            var userData = data.find(function (c) { return c.group == d.group; }).value.filter(function (c) { return c.pseudonym == d.pseudonym; });
            var line = d3.line()
                .x(function (d) { return chart.x.scale(d.timestamp); })
                .y(function (d) { return chart.y.scale(d.point); });
            chart.elements.contentContainer.append("path")
                .datum(d3.sort(userData, function (d) { return d.timestamp; }))
                .attr("class", "click-line")
                .attr("d", function (d) { return line(d); })
                .style("stroke", d.colour);
            //Draw click containers
            userData.forEach(function (c) { return _this.interactions.click.appendScatterText(chart, c, c.point.toString()); });
            //Draw user statistics container
            _this.htmlContainers.userStatistics = _this.htmlContainers.appendDiv("user-statistics", "col-md-12 mt-3");
            d3.select("#user-statistics .card-title")
                .html("User " + d.pseudonym + " compared to their group");
            var userCard = d3.select("#user-statistics .card-body")
                .append("div")
                .attr("class", "users-tab-pane")
                .attr("id", "user-statistics-" + d.pseudonym);
            _this.renderUserStatistics(userCard, data.find(function (c) { return c.group == d.group; }), _this.usersHistogram.elements.getThresholdsValues(_this.usersHistogram), d.pseudonym);
            _this.htmlContainers.removeHelp(chart);
            //Scroll
            document.querySelector("#group-timeline").scrollIntoView({ behavior: 'smooth', block: 'start' });
            _this.htmlContainers.renderNavbarScrollspy();
        }
        return chart;
    };
    ;
    AdminExperimentalCharts.prototype.renderTimelineDensity = function (chart, data) {
        var _this_1 = this;
        chart = _super.prototype.renderTimelineDensity.call(this, chart, data);
        d3.select("#" + chart.id + " .badge").on("click", function () { return _this_1.handleFilterButton(); });
        this.interactions.click.removeClick(chart);
        return chart;
    };
    ;
    AdminExperimentalCharts.prototype.handleTimelineButtons = function (chart, zoomChart, data) {
        var _this = this;
        _super.prototype.handleTimelineButtons.call(this, chart, zoomChart, data, newFunc);
        function newFunc(e) {
            var selectedOption = e.target.control.value;
            if (selectedOption == "density") {
                if (!chart.elements.contentContainer.selectAll(".click-line").empty()) {
                    _this.removeUserStatistics();
                }
                _this.renderTimelineDensity(chart, data);
            }
            if (selectedOption == "scatter") {
                _this.renderTimelineScatter(chart, zoomChart, data);
            }
            if (!d3.select("#" + chart.id + "-help").empty()) {
                _this.htmlContainers.removeHelp(chart);
            }
        }
    };
    return AdminExperimentalCharts;
}(AdminControlCharts));
var AdminExperimentalInteractions = /** @class */ (function (_super) {
    __extends(AdminExperimentalInteractions, _super);
    function AdminExperimentalInteractions() {
        var _this_1 = _super !== null && _super.apply(this, arguments) || this;
        _this_1.click = new Click();
        _this_1.sort = new Sort();
        return _this_1;
    }
    return AdminExperimentalInteractions;
}(AdminControlInteractions));
// Class for click interaction
var Click = /** @class */ (function () {
    function Click() {
    }
    Click.prototype.enableClick = function (chart, onClick) {
        chart.elements.content.on("click", onClick);
    };
    ;
    Click.prototype.removeClick = function (chart) {
        chart.click = false;
        chart.elements.contentContainer.selectAll(".click-text").remove();
        chart.elements.contentContainer.selectAll(".click-line").remove();
        chart.elements.contentContainer.selectAll(".click-container").remove();
        chart.elements.content.classed("clicked", false);
    };
    ;
    Click.prototype.appendScatterText = function (chart, d, title, values) {
        if (values === void 0) { values = null; }
        var container = chart.elements.contentContainer.append("g")
            .datum(d)
            .attr("class", "click-container");
        var box = container.append("rect")
            .attr("class", "click-box");
        var text = container.append("text")
            .attr("class", "click-text title")
            .attr("x", 10)
            .text(title);
        var textSize = text.node().getBBox().height;
        text.attr("y", textSize);
        if (values != null) {
            values.forEach(function (c, i) {
                text.append("tspan")
                    .attr("class", "click-text")
                    .attr("y", textSize * (i + 2))
                    .attr("x", 15)
                    .text(c.label + ": " + c.value);
            });
        }
        box.attr("width", text.node().getBBox().width + 20)
            .attr("height", text.node().getBBox().height + 5)
            .attr("clip-path", "url(#clip-" + chart.id + ")");
        container.attr("transform", this.positionClickContainer(chart, box, text, d));
    };
    ;
    Click.prototype.positionClickContainer = function (chart, box, text, d) {
        var positionX = chart.x.scale(d.timestamp);
        var positionY = chart.y.scale(d.point) - box.node().getBBox().height - 10;
        if (chart.width - chart.padding.yAxis < chart.x.scale(d.timestamp) + text.node().getBBox().width) {
            positionX = chart.x.scale(d.timestamp) - box.node().getBBox().width;
        }
        ;
        if (chart.y.scale(d.point) - box.node().getBBox().height - 10 < 0) {
            positionY = positionY + box.node().getBBox().height + 20;
        }
        ;
        return "translate(" + positionX + ", " + positionY + ")";
    };
    ;
    Click.prototype.appendGroupsText = function (chart, data, clickData) {
        var _this_1 = this;
        chart.elements.content.classed("clicked", function (d) { return d.group == clickData.group; });
        chart.elements.contentContainer.selectAll(".click-container")
            .data(data)
            .join(function (enter) { return enter.append("g")
                .attr("class", "click-container")
                .attr("transform", function (c) { return "translate(" + (chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2) + ", 0)"; })
                .call(function (enter) { return enter.selectAll("text")
                    .data(function (c) { return c.stats.filter(function (d) { return d.stat == "q3" || d.stat == "median" || d.stat == "q1"; }).map(function (d) { return new ClickTextData(clickData.stats.find(function (a) { return a.stat == d.stat; }), d, clickData.group, c.group); }); })
                    .enter()
                    .append("text")
                    .attr("class", "click-text black")
                    .attr("y", function (c) { return chart.y.scale(c.data.stat.value) - 5; })
                    .text(function (c) { return c.data.stat.displayName + ": " + c.data.stat.value + " "; })
                    .append("tspan")
                    .attr("class", function (c) { return _this_1.comparativeText(c)[0]; })
                    .text(function (c) { return c.data.group != clickData.group ? "(" + _this_1.comparativeText(c)[1] + ")" : ""; }); }); }, function (update) { return update.call(function (update) { return update.transition()
                .duration(750)
                .attr("transform", function (c) { return "translate(" + (chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2) + ", 0)"; }); })
                .call(function (update) { return update.selectAll("text")
                    .data(function (c) { return c.stats.filter(function (d) { return d.stat == "q3" || d.stat == "median" || d.stat == "q1"; }).map(function (d) { return new ClickTextData(clickData.stats.find(function (a) { return a.stat == d.stat; }), d, clickData.group, c.group); }); })
                    .join(function (enter) { return enter; }, function (update) { return update.attr("y", function (c) { return chart.y.scale(c.data.stat.value) - 5; })
                        .text(function (c) { return c.data.stat.displayName + ": " + c.data.stat.value + " "; })
                        .append("tspan")
                        .attr("class", function (c) { return _this_1.comparativeText(c)[0]; })
                        .text(function (c) { return c.data.group != clickData.group ? "(" + _this_1.comparativeText(c)[1] + ")" : ""; }); }, function (exit) { return exit; }); }); }, function (exit) { return exit.remove(); });
    };
    ;
    Click.prototype.appendThresholdPercentages = function (chart, data, clickData) {
        var _this_1 = this;
        var thresholds = chart.elements.getThresholdsValues(chart);
        var tDistressed = thresholds[0];
        var tSoaring = thresholds[1];
        chart.elements.content.classed("clicked", function (d) { return d.group == clickData.group && clickData.bin.length - d.bin.length == 0; });
        chart.elements.contentContainer.selectAll(".click-container")
            .data(data)
            .join(function (enter) { return enter.append("g")
                .attr("class", "click-container")
                .attr("transform", function (c) { return "translate(" + (chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2) + ", 0)"; })
                .call(function (enter) { return enter.selectAll("text")
                    .data(function (d) { return chart.bin(d.value.map(function (d) { return d.point; })).map(function (c) { return new HistogramData(d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }); })
                    .enter()
                    .append("text")
                    .attr("class", "click-text black")
                    .attr("y", function (c) { return c.bin.x0 == 0 ? chart.y.scale(0 + tDistressed / 2) : c.bin.x1 == 100 ? chart.y.scale(tSoaring + (100 - tSoaring) / 2) : chart.y.scale(50); })
                    .text(function (c) { return c.percentage + "% "; })
                    .append("tspan")
                    .attr("class", function (c) { return _this_1.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[0]; })
                    .text(function (c) { return c.group != clickData.group && c.bin.x0 == clickData.bin.x0 && c.bin.x1 == clickData.bin.x1 ? "(" + _this_1.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[1] + ")" : ""; }); }); }, function (update) { return update.call(function (update) { return update.transition()
                .duration(750)
                .attr("transform", function (c) { return "translate(" + (chart.x.scale(c.group) + chart.x.scale.bandwidth() / 2) + ", 0)"; }); })
                .call(function (update) { return update.selectAll("text")
                    .data(function (d) { return chart.bin(d.value.map(function (d) { return d.point; })).map(function (c) { return new HistogramData(d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }); })
                    .join(function (enter) { return enter; }, function (update) { return update.attr("y", function (c) { return c.bin.x0 == 0 ? chart.y.scale(0 + tDistressed / 2) : c.bin.x1 == 100 ? chart.y.scale(tSoaring + (100 - tSoaring) / 2) : chart.y.scale(50); })
                        .text(function (c) { return c.percentage + "% "; })
                        .append("tspan")
                        .attr("class", function (c) { return _this_1.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[0]; })
                        .text(function (c) { return c.group != clickData.group && c.bin.x0 == clickData.bin.x0 && c.bin.x1 == clickData.bin.x1 ? "(" + _this_1.comparativeText(new ClickTextData(clickData.percentage, c.percentage, clickData.group, c.group))[1] + ")" : ""; }); }, function (exit) { return exit; }); }); }, function (exit) { return exit.remove(); });
    };
    ;
    Click.prototype.comparativeText = function (textData) {
        var textClass = "click-text";
        var textSymbol = "";
        var textValue;
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
            return [textClass, "" + textSymbol + (textData.clickData.group == textData.data.group
            && textValue == 0 ? typeof (textData.clickData.stat) != "number" ? textData.clickData.stat.value : textData.clickData.stat : (Math.abs(textValue)))];
        }
        else {
            return [textClass, "" + textSymbol + (Math.abs(textValue))];
        }
    };
    return Click;
}());
// Class for sort interaction
var Sort = /** @class */ (function () {
    function Sort() {
    }
    Sort.prototype.sortData = function (a, b, sorted) {
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
    };
    ;
    Sort.prototype.setSorted = function (sorted, option) {
        return sorted == option ? "" : option;
    };
    return Sort;
}());
var Loading = /** @class */ (function () {
    function Loading() {
        this.isLoading = true;
        this.spinner = this.appendDiv();
    }
    Loading.prototype.appendDiv = function () {
        var div = d3.select(".wrapper")
            .append("div")
            .attr("class", "loader");
        div.append("div")
            .attr("class", "loader-inner")
            .selectAll(".loader-line-wrap")
            .data([1, 2, 3, 4, 5])
            .enter()
            .append("div")
            .attr("class", "loader-line-wrap")
            .append("div")
            .attr("class", "loader-line");
        return div;
    };
    Loading.prototype.removeDiv = function () {
        this.spinner.remove();
    };
    return Loading;
}());
var Tutorial = /** @class */ (function () {
    function Tutorial(constructor) {
        this.tutorial = this.appendTutorial();
        this.tutorialData = constructor;
        this.slide = 0;
        this.appendTutorialBackdrop();
    }
    Tutorial.prototype.appendTutorial = function () {
        d3.select("body")
            .classed("no-overflow", true);
        var div = d3.select(".wrapper")
            .append("div")
            .attr("class", "tutorial");
        return div;
    };
    ;
    Tutorial.prototype.appendTutorialBackdrop = function () {
        if (this.slide >= this.tutorialData.length) {
            this.removeTutorial();
            return;
        }
        var tutorialData = this.tutorialData[this.slide];
        var tutorialFocus = d3.select(tutorialData.id).node().getBoundingClientRect();
        var TutorialContentData = /** @class */ (function () {
            function TutorialContentData(top, left, width, height) {
                this.top = top;
                this.left = left;
                this.width = width;
                this.height = height;
            }
            return TutorialContentData;
        }());
        var data = [new TutorialContentData("0px", "0px", "100%", tutorialFocus.top + "px"),
            new TutorialContentData(tutorialFocus.bottom + "px", "0px", "100%", "100%"),
            new TutorialContentData(tutorialFocus.top + "px", "0px", tutorialFocus.left + "px", tutorialFocus.height + "px"),
            new TutorialContentData(tutorialFocus.top + "px", tutorialFocus.right + "px", "100%", tutorialFocus.height + "px")];
        this.tutorial.selectAll(".tutorial-backdrop")
            .data(data)
            .join(function (enter) { return enter.append("div")
                .attr("class", "tutorial-backdrop")
                .style("top", function (d) { return d.top; })
                .style("left", function (d) { return d.left; })
                .style("width", function (d) { return d.width; })
                .style("height", function (d) { return d.height; }); }, function (update) { return update.style("top", function (d) { return d.top; })
                .style("left", function (d) { return d.left; })
                .style("width", function (d) { return d.width; })
                .style("height", function (d) { return d.height; }); }, function (exit) { return exit.remove(); });
        this.appendTutorialContent(tutorialFocus, tutorialData.content);
    };
    ;
    Tutorial.prototype.appendTutorialContent = function (tutorialFocus, content) {
        var _this_1 = this;
        var isLeft = true;
        if (tutorialFocus.left + 50 > window.innerWidth / 2) {
            isLeft = false;
        }
        console.log(isLeft);
        if (this.tutorial.selectAll(".tutorial-content").empty()) {
            this.tutorial.append("div")
                .attr("class", "tutorial-content")
                .style("top", (tutorialFocus.top - 50) + "px")
                .style("left", tutorialFocus.left + tutorialFocus.width + 50 + "px")
                .call(function (div) { return div.append("div")
                    .attr("class", "row")
                    .call(function (div) { return div.append("div")
                        .attr("class", "col-md-12")
                        .html(content); })
                    .call(function (div) { return div.append("div")
                        .attr("class", "col-md-6"); })
                    .call(function (div) { return div.append("div")
                        .attr("class", "col-md-5 d-flex")
                        .call(function (div) { return div.append("button")
                            .attr("class", "btn btn-success d-block w-50")
                            .html("Next")
                            .on("click", function () { _this_1.slide = _this_1.slide + 1; _this_1.appendTutorialBackdrop(); }); })
                        .call(function (div) { return div.append("button")
                            .attr("class", "btn btn-warning d-block w-50")
                            .html("Skip")
                            .on("click", function () { return _this_1.removeTutorial(); }); }); }); });
            this.drawArrow(tutorialFocus, isLeft);
        }
        else {
            this.tutorial.select(".tutorial-content")
                .style("top", (tutorialFocus.top - 50) + "px")
                .style("left", isLeft ? tutorialFocus.left + tutorialFocus.width + 50 + "px" :
                    tutorialFocus.left - this.tutorial.select(".tutorial-content").node().getBoundingClientRect().width - 50 + "px");
            this.tutorial.select(".col-md-12")
                .html(content);
            this.tutorial.select(".tutorial-arrow").remove();
            this.drawArrow(tutorialFocus, isLeft);
        }
    };
    ;
    Tutorial.prototype.drawArrow = function (tutorialFocus, isLeft) {
        var tutorialArrow = this.tutorial.append("div")
            .attr("class", "tutorial-arrow")
            .style("top", (tutorialFocus.top - 50) + "px")
            .style("left", isLeft ? tutorialFocus.left + (tutorialFocus.width / 4) + "px" :
                this.tutorial.select(".tutorial-content").node().getBoundingClientRect().left + this.tutorial.select(".tutorial-content").node().getBoundingClientRect().width + "px")
            .style("width", (tutorialFocus.width / 4 * 3) + 50 + "px")
            .style("height", "50px");
        var svg = tutorialArrow.append("svg")
            .attr("viewBox", "0 0 " + tutorialArrow.node().getBoundingClientRect().width + " " + tutorialArrow.node().getBoundingClientRect().height);
        svg.append("defs")
            .append("marker")
            .attr("id", "arrow-head")
            .attr("markerWidth", 5)
            .attr("markerHeight", 5)
            .attr("refX", 2)
            .attr("refY", 2)
            .attr("orient", "auto")
            .append("path")
            .attr("d", "M0,0 L0,4 L4,2 L0,0")
            .attr("class", "arrow-head");
        var xScale = d3.scaleLinear()
            .domain([0, 100])
            .range([0, tutorialArrow.node().getBoundingClientRect().width]);
        var yScale = d3.scaleLinear()
            .domain([100, 0])
            .range([0, tutorialArrow.node().getBoundingClientRect().height]);
        var pathGenerator = d3.line()
            .x(function (d) { return xScale(d.x); })
            .y(function (d) { return yScale(d.y); })
            .curve(d3.curveCatmullRom);
        svg.append("path")
            .attr("d", isLeft ? pathGenerator([{ x: 95, y: 80 }, { x: 25, y: 70 }, { x: 25, y: 25 }]) : pathGenerator([{ x: 0, y: 80 }, { x: 75, y: 70 }, { x: 75, y: 25 }]))
            .attr("class", "arrow")
            .attr("marker-end", "url(#arrow-head)");
    };
    Tutorial.prototype.removeTutorial = function () {
        d3.select("body")
            .classed("no-overflow", false);
        this.tutorial.remove();
    };
    return Tutorial;
}());
/* ------------------------------------------------
    End of admin experimental interfaces and classes
-------------------------------------------------- */
function buildControlAdminAnalyticsCharts(entriesRaw) {
    return __awaiter(this, void 0, void 0, function () {
        function drawCharts(allEntries) {
            return __awaiter(this, void 0, void 0, function () {
                var adminControlCharts, data, groupChart, histogramChart, timelineCard, timelineChart, timelineZoomChart, usersData, histogramUsersChart, userStatistics, users;
                return __generator(this, function (_a) {
                    adminControlCharts = new AdminControlCharts();
                    //Handle sidebar button
                    adminControlCharts.sidebarBtn();
                    adminControlCharts.preloadGroups(allEntries);
                    data = allEntries.map(function (d) { return new AnalyticsChartsDataStats(d); });
                    //Append groups chart container
                    adminControlCharts.htmlContainers.boxPlot = adminControlCharts.htmlContainers.appendDiv("groups-chart", "col-md-6");
                    adminControlCharts.htmlContainers.appendCard(adminControlCharts.htmlContainers.boxPlot, "All reflections by group - Box plot", undefined, true);
                    groupChart = new ChartSeries("groups-chart", data.map(function (d) { return d.group; }));
                    adminControlCharts.renderGroupChart(groupChart, data);
                    //Handle groups chart help
                    adminControlCharts.htmlContainers.boxPlot.select(".card-title button")
                        .on("click", function (e) {
                            adminControlCharts.htmlContainers.helpPopover(d3.select(this), groupChart.id + "-help", "<b>Box plot</b><br>A box plot is used to show the data distribution<br><i>Q3:</i> The median of the upper half of the data set<br><i>Median:</i> The middle value of a dataset<br><i>Q1:</i> The median of the lower half of the dataset");
                            adminControlCharts.htmlContainers.helpPopover(groupChart.elements.contentContainer.select(".bar"), groupChart.id + "-help-data", "<u><i>hover</i></u> me for information on demand");
                        });
                    //Draw groups histogram container
                    adminControlCharts.htmlContainers.histogram = adminControlCharts.htmlContainers.appendDiv("group-histogram-chart", "col-md-6");
                    adminControlCharts.htmlContainers.appendCard(adminControlCharts.htmlContainers.histogram, "All reflections by group - Histogram", undefined, true);
                    histogramChart = new HistogramChartSeries("group-histogram-chart", data.map(function (d) { return d.group; }));
                    adminControlCharts.renderHistogram(histogramChart, data);
                    //Handle histogram chart help
                    adminControlCharts.htmlContainers.histogram.select(".card-title button")
                        .on("click", function (e) {
                            adminControlCharts.htmlContainers.helpPopover(d3.select(this), histogramChart.id + "-help", "<b>Histogram</b><br>A histogram group data points into user-specific ranges. The data points in this histogram are <i>reflections</i>");
                            adminControlCharts.htmlContainers.helpPopover(histogramChart.elements.contentContainer.select(".histogram-rect"), histogramChart.id + "-help-data", "<u><i>hover</i></u> me for information on demand");
                        });
                    //Draw timeline
                    adminControlCharts.htmlContainers.timeline = adminControlCharts.htmlContainers.appendDiv("group-timeline", "col-md-12 mt-3");
                    timelineCard = adminControlCharts.htmlContainers.appendCard(adminControlCharts.htmlContainers.timeline, "Reflections by group vs time", undefined, true);
                    timelineChart = new ChartTime("group-timeline", [d3.min(data.map(function (d) { return d.getStat("oldRef").value; })), d3.max(data.map(function (d) { return d.getStat("newRef").value; }))]);
                    adminControlCharts.renderTimelineDensity(timelineChart, data);
                    timelineZoomChart = new ChartTimeZoom(timelineChart, [d3.min(data.map(function (d) { return d.getStat("oldRef").value; })), d3.max(data.map(function (d) { return d.getStat("newRef").value; }))]);
                    adminControlCharts.renderTimelineButtons(timelineCard);
                    adminControlCharts.handleTimelineButtons(timelineChart, timelineZoomChart, data);
                    //Handle timeline chart help
                    adminControlCharts.htmlContainers.timeline.select(".card-title button")
                        .on("click", function (e) {
                            adminControlCharts.htmlContainers.helpPopover(d3.select(this), timelineChart.id + "-help", "<b>Density plot</b><br>A density plot shows the distribution of a numeric variable<br><b>Scatter plot</b><br>The data is showed as a collection of points<br>The data represented are <i>reflections over time</i>");
                            adminControlCharts.htmlContainers.helpPopover(adminControlCharts.htmlContainers.timeline.select("#timeline-plot"), timelineChart.id + "-help-button", "<u><i>click</i></u> me to change chart type");
                            adminControlCharts.htmlContainers.helpPopover(adminControlCharts.htmlContainers.timeline.select(".zoom-rect.active"), timelineChart.id + "-help-zoom", "use the mouse <u><i>wheel</i></u> to zoom me<br><u><i>click and hold</i></u> while zoomed to move");
                            if (!timelineChart.elements.contentContainer.select(".circle").empty()) {
                                var showDataHelp = adminControlCharts.htmlContainers.helpPopover(timelineChart.elements.contentContainer.select(".circle"), timelineChart.id + "-help-data", "<u><i>hover</i></u> me for information on demand");
                                if (showDataHelp) {
                                    d3.select("#" + timelineChart.id + "-help-data").style("top", parseInt(d3.select("#" + timelineChart.id + "-help-data").style("top")) - 14 + "px");
                                }
                            }
                        });
                    //Draw users histogram container
                    adminControlCharts.htmlContainers.userHistogram = adminControlCharts.htmlContainers.appendDiv("group-histogram-users-chart", "col-md-6 mt-3");
                    adminControlCharts.htmlContainers.appendCard(adminControlCharts.htmlContainers.userHistogram, "All reflections by users and group - Histogram", undefined, true);
                    usersData = data.map(function (d) { return d.getUsersData(); });
                    histogramUsersChart = new HistogramChartSeries("group-histogram-users-chart", data.map(function (d) { return d.group; }));
                    adminControlCharts.renderHistogram(histogramUsersChart, usersData);
                    //Handle users histogram chart help
                    adminControlCharts.htmlContainers.userHistogram.select(".card-title button")
                        .on("click", function (e) {
                            adminControlCharts.htmlContainers.helpPopover(d3.select(this), histogramUsersChart.id + "-help", "<b>Histogram</b><br>A histogram group data points into user-specific ranges. The data points in this histogram are <i>users average reflection point</i>");
                            adminControlCharts.htmlContainers.helpPopover(histogramUsersChart.elements.contentContainer.select("#" + histogramUsersChart.id + "-data"), histogramUsersChart.id + "-help-data", "<u><i>hover</i></u> me for information on demand");
                        });
                    //Draw user statistics
                    adminControlCharts.htmlContainers.userStatistics = adminControlCharts.htmlContainers.appendDiv("user-statistics", "col-md-6 mt-3");
                    userStatistics = adminControlCharts.htmlContainers.appendCard(adminControlCharts.htmlContainers.userStatistics, "Users compared to their group", "user-statistics", false);
                    userStatistics.select(".chart-container").remove();
                    userStatistics.append("ul")
                        .attr("class", "nav nav-tabs")
                        .selectAll("li")
                        .data(data)
                        .enter()
                        .append("li")
                        .attr("class", "nav-item")
                        .append("a")
                        .attr("class", function (d, i) { return "nav-link " + (i == 0 ? "active" : ""); })
                        .attr("href", function (d) { return "#user-statistics-" + d.group; })
                        .attr("data-toggle", "tab")
                        .html(function (d) { return d.group; })
                        .on("click", function (e, d) { return setTimeout(function () { return adminControlCharts.renderUserStatistics(d3.select("#user-statistics-" + d.group), d, [30, 70]); }, 250); });
                    users = userStatistics.append("div")
                        .attr("class", "tab-content")
                        .selectAll("div")
                        .data(data)
                        .enter()
                        .append("div")
                        .attr("class", function (d, i) { return "tab-pane fade " + (i == 0 ? "show active" : "") + " users-tab-pane"; })
                        .attr("id", function (d) { return "user-statistics-" + d.group; });
                    users.each(function (d, i, g) { return i == 0 ? adminControlCharts.renderUserStatistics(d3.select(g[i]), d, [30, 70]) : ""; });
                    adminControlCharts.htmlContainers.renderNavbarScrollspy();
                    return [2 /*return*/];
                });
            });
        }
        var loading, rawData, entries, colourScale;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    loading = new Loading();
                    rawData = entriesRaw.map(function (d) { return new AnalyticsChartsDataRaw(d.group, d.value, d.createDate); });
                    entries = rawData.map(function (d) { return d.transformData(); });
                    colourScale = d3.scaleOrdinal(d3.schemeCategory10);
                    entries = entries.map(function (d) { return new AnalyticsChartsData(d.group, d.value, d.creteDate, colourScale(d.group), d.selected); });
                    return [4 /*yield*/, drawCharts(entries)];
                case 1:
                    _a.sent();
                    loading.isLoading = false;
                    loading.removeDiv();
                    return [2 /*return*/];
            }
        });
    });
}
exports.buildControlAdminAnalyticsCharts = buildControlAdminAnalyticsCharts;
function buildExperimentAdminAnalyticsCharts(entriesRaw) {
    return __awaiter(this, void 0, void 0, function () {
        function drawCharts(allEntries) {
            return __awaiter(this, void 0, void 0, function () {
                var adminExperimentalCharts, entries, data, groupCard, timelineCard, usersData, userStatistics;
                return __generator(this, function (_a) {
                    adminExperimentalCharts = new AdminExperimentalCharts();
                    //Handle sidebar button
                    adminExperimentalCharts.sidebarBtn();
                    entries = adminExperimentalCharts.preloadGroups(allEntries);
                    data = entries.map(function (d) { return new AnalyticsChartsDataStats(d); });
                    //Append groups chart container
                    adminExperimentalCharts.htmlContainers.boxPlot = adminExperimentalCharts.htmlContainers.appendDiv("groups-chart", "col-md-6");
                    groupCard = adminExperimentalCharts.htmlContainers.appendCard(adminExperimentalCharts.htmlContainers.boxPlot, "Reflections of selected groups - Box plot", undefined, true);
                    groupCard.insert("div", ".chart-container")
                        .attr("class", "row")
                        .call(function (div) { return div.append("span")
                            .attr("class", "mx-2")
                            .html("<small>Sort groups by:</small>"); })
                        .call(function (div) { return div.append("div")
                            .attr("id", "sort-by")
                            .attr("class", "btn-group btn-group-sm btn-group-toggle")
                            .attr("data-toggle", "buttons")
                            .html("<label class=\"btn btn-light active\">\n                            <input type=\"radio\" name=\"sort\" value=\"date\" checked>Create date<br>\n                        </label>\n                        <label class=\"btn btn-light\">\n                            <input type=\"radio\" name=\"sort\" value=\"name\">Name<br>\n                        </label>\n                        <label class=\"btn btn-light\">\n                            <input type=\"radio\" name=\"sort\" value=\"mean\">Mean<br>\n                        </label>"); });
                    //Create group chart with current data
                    adminExperimentalCharts.boxPlot = new ChartSeries("groups-chart", data.map(function (d) { return d.group; }));
                    adminExperimentalCharts.boxPlot = adminExperimentalCharts.renderGroupChart(adminExperimentalCharts.boxPlot, data);
                    //Handle groups chart help
                    adminExperimentalCharts.htmlContainers.boxPlot.select(".card-title button")
                        .on("click", function (e) {
                            adminExperimentalCharts.htmlContainers.helpPopover(d3.select(this), adminExperimentalCharts.boxPlot.id + "-help", "<b>Box plot</b><br>A box plot is used to show the data distribution<br><i>Q3:</i> The median of the upper half of the data set<br><i>Median:</i> The middle value of a dataset<br><i>Q1:</i> The median of the lower half of the dataset");
                            adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.boxPlot.elements.contentContainer.select("#" + adminExperimentalCharts.boxPlot.id + "-data"), adminExperimentalCharts.boxPlot.id + "-help-data", "<u><i>hover</i></u> me for information on demand<br><u><i>click</i></u> me to compare and drill-down");
                            adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.htmlContainers.boxPlot.select("#sort-by"), adminExperimentalCharts.boxPlot.id + "-help-button", "<u><i>click</i></u> me to sort the groups");
                        });
                    //Draw groups histogram container
                    adminExperimentalCharts.htmlContainers.histogram = adminExperimentalCharts.htmlContainers.appendDiv("group-histogram-chart", "col-md-6");
                    adminExperimentalCharts.htmlContainers.appendCard(adminExperimentalCharts.htmlContainers.histogram, "Selected groups reflections - Histogram", undefined, true);
                    adminExperimentalCharts.histogram = new HistogramChartSeries("group-histogram-chart", data.map(function (d) { return d.group; }));
                    adminExperimentalCharts.renderHistogram(adminExperimentalCharts.histogram, data);
                    //Handle histogram chart help
                    adminExperimentalCharts.htmlContainers.histogram.select(".card-title button")
                        .on("click", function (e) {
                            adminExperimentalCharts.htmlContainers.helpPopover(d3.select(this), adminExperimentalCharts.histogram.id + "-help", "<b>Histogram</b><br>A histogram group data points into user-specific ranges. The data points in this histogram are <i>reflections point</i>");
                            adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.histogram.elements.contentContainer.select(".histogram-rect"), adminExperimentalCharts.histogram.id + "-help-data", "<u><i>hover</i></u> me for information on demand<br><u><i>click</i></u> me to compare");
                            var showDragHelp = adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.histogram.elements.contentContainer.select(".threshold-line.soaring"), adminExperimentalCharts.histogram.id + "-help-drag", "<u><i>drag</i></u> me to change the thresholds");
                            if (showDragHelp) {
                                d3.select("#" + adminExperimentalCharts.histogram.id + "-help-drag").style("top", parseInt(d3.select("#" + adminExperimentalCharts.histogram.id + "-help-drag").style("top")) - 19 + "px");
                            }
                        });
                    //Draw timeline
                    adminExperimentalCharts.htmlContainers.timeline = adminExperimentalCharts.htmlContainers.appendDiv("group-timeline", "col-md-12 mt-3");
                    timelineCard = adminExperimentalCharts.htmlContainers.appendCard(adminExperimentalCharts.htmlContainers.timeline, "Selected group reflections vs time - Timeline", undefined, true);
                    adminExperimentalCharts.timeline = new ChartTime("group-timeline", [d3.min(data.map(function (d) { return d.getStat("oldRef").value; })), d3.max(data.map(function (d) { return d.getStat("newRef").value; }))]);
                    adminExperimentalCharts.renderTimelineDensity(adminExperimentalCharts.timeline, data);
                    adminExperimentalCharts.timelineZoom = new ChartTimeZoom(adminExperimentalCharts.timeline, [d3.min(data.map(function (d) { return d.getStat("oldRef").value; })), d3.max(data.map(function (d) { return d.getStat("newRef").value; }))]);
                    adminExperimentalCharts.renderTimelineButtons(timelineCard);
                    adminExperimentalCharts.handleTimelineButtons(adminExperimentalCharts.timeline, adminExperimentalCharts.timelineZoom, data);
                    //Handle timeline chart help
                    adminExperimentalCharts.htmlContainers.timeline.select(".card-title button")
                        .on("click", function (e) {
                            adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.htmlContainers.timeline.select("#timeline-plot"), adminExperimentalCharts.timeline.id + "-help-button", "<u><i>click</i></u> me to change chart type");
                            adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.htmlContainers.timeline.select(".zoom-rect.active"), adminExperimentalCharts.timeline.id + "-help-zoom", "use the mouse <u><i>wheel</i></u> to zoom me<br><u><i>click and hold</i></u> while zoomed to move");
                            if (!adminExperimentalCharts.timeline.elements.contentContainer.select(".circle").empty()) {
                                adminExperimentalCharts.htmlContainers.helpPopover(d3.select(this), adminExperimentalCharts.timeline.id + "-help", "<b>Scatter plot</b><br>A scatter plot shows the data as a collection of points<br>The data represented are <i>reflections over time</i>");
                                var showDataHelp = adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.timeline.elements.contentContainer.select(".circle"), adminExperimentalCharts.timeline.id + "-help-data", "<u><i>hover</i></u> me for information on demand<br><u><i>click</i></u> me to connect the user's reflections");
                                if (showDataHelp) {
                                    d3.select("#" + adminExperimentalCharts.timeline.id + "-help-data").style("top", parseInt(d3.select("#" + adminExperimentalCharts.timeline.id + "-help-data").style("top")) - 14 + "px");
                                }
                            }
                            else {
                                adminExperimentalCharts.htmlContainers.helpPopover(d3.select(this), adminExperimentalCharts.timeline.id + "-help", "<b>Density plot</b><br>A density plot shows the distribution of a numeric variable<br>The data represented are <i>reflections over time</i>");
                            }
                        });
                    //Draw users histogram container
                    adminExperimentalCharts.htmlContainers.userHistogram = adminExperimentalCharts.htmlContainers.appendDiv("group-histogram-users-chart", "col-md-6 mt-3");
                    adminExperimentalCharts.htmlContainers.appendCard(adminExperimentalCharts.htmlContainers.userHistogram, "Selected groups reflections by users - Histogram", undefined, true);
                    usersData = data.map(function (d) { return d.getUsersData(); });
                    adminExperimentalCharts.usersHistogram = new HistogramChartSeries("group-histogram-users-chart", data.map(function (d) { return d.group; }));
                    adminExperimentalCharts.renderHistogram(adminExperimentalCharts.usersHistogram, usersData);
                    //Handle users histogram chart help
                    adminExperimentalCharts.htmlContainers.userHistogram.select(".card-title button")
                        .on("click", function (e) {
                            adminExperimentalCharts.htmlContainers.helpPopover(d3.select(this), adminExperimentalCharts.usersHistogram.id + "-help", "<b>Histogram</b><br>A histogram group data points into user-specific ranges. The data points in this histogram are <i>users average reflection point</i>");
                            adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.usersHistogram.elements.contentContainer.select(".histogram-rect"), adminExperimentalCharts.usersHistogram.id + "-help-data", "<u><i>hover</i></u> me for information on demand<br><u><i>click</i></u> me to compare");
                            var showDragHelp = adminExperimentalCharts.htmlContainers.helpPopover(adminExperimentalCharts.usersHistogram.elements.contentContainer.select(".threshold-line.soaring"), adminExperimentalCharts.usersHistogram.id + "-help-drag", "<u><i>drag</i></u> me to change the thresholds");
                            if (showDragHelp) {
                                d3.select("#" + adminExperimentalCharts.usersHistogram.id + "-help-drag").style("top", parseInt(d3.select("#" + adminExperimentalCharts.usersHistogram.id + "-help-drag").style("top")) - 19 + "px");
                            }
                        });
                    //Draw user statistics
                    adminExperimentalCharts.htmlContainers.userStatistics = adminExperimentalCharts.htmlContainers.appendDiv("user-statistics", "col-md-6 mt-3");
                    userStatistics = adminExperimentalCharts.htmlContainers.appendCard(adminExperimentalCharts.htmlContainers.userStatistics, "Users compared to their group", "user-statistics", false);
                    userStatistics.select(".chart-container").remove();
                    userStatistics.select(".card-subtitle")
                        .html("Select a reflection from the scatter plot to view specific users");
                    adminExperimentalCharts.htmlContainers.renderNavbarScrollspy();
                    //Update charts depending on group
                    adminExperimentalCharts.handleGroups();
                    adminExperimentalCharts.handleGroupsColours();
                    adminExperimentalCharts.handleGroupsSort();
                    return [2 /*return*/];
                });
            });
        }
        var loading, rawData, entries, colourScale;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    loading = new Loading();
                    rawData = entriesRaw.map(function (d) { return new AnalyticsChartsDataRaw(d.group, d.value, d.createDate); });
                    entries = rawData.map(function (d) { return d.transformData(); });
                    colourScale = d3.scaleOrdinal(d3.schemeCategory10);
                    entries = entries.map(function (d, i) { return new AnalyticsChartsData(d.group, d.value, d.creteDate, colourScale(d.group), i == 0 ? true : false); });
                    return [4 /*yield*/, drawCharts(entries)];
                case 1:
                    _a.sent();
                    new Tutorial([{ id: "#groups", content: "Add groups to the charts and change their colours" },
                        { id: ".fa-question-circle", content: "Click the help symbol in any chart to get additional information" },
                        { id: "#groups-chart .bar", content: "Hover for information on demand or click to compare and drill-down. Other visualisations will show only the selected group" },
                        { id: "#group-histogram-chart .threshold-line", content: "Drag to change the threshold (soaring or distressed) and recalculate the bins" },
                        { id: "#group-histogram-chart .histogram-rect", content: "Click to compare the bin with other's group bins" },
                        { id: "#timeline-plot", content: "Swap chart types. Both charts have zoom available. In the scatter plot, click a bubble to access the user's information" }]);
                    loading.isLoading = false;
                    loading.removeDiv();
                    return [2 /*return*/];
            }
        });
    });
}
exports.buildExperimentAdminAnalyticsCharts = buildExperimentAdminAnalyticsCharts;
