//var d3 = require("d3");
//var $ = require("$");
var Chart = /** @class */ (function () {
    function Chart() {
        this.renderElements = {};
        this.padding = { xAxis: 50, yAxis: 75, top: 25, right: 0 };
    }
    return Chart;
}());
var HtmlContainers = /** @class */ (function () {
    function HtmlContainers() {
    }
    HtmlContainers.prototype.remove = function () {
        this.groupStatistics.remove();
        this.groupTimeline.remove();
        this.groupViolin.remove();
        this.userViolin.remove();
        this.compare.remove();
    };
    ;
    return HtmlContainers;
}());

export function buildAnalyticsCharts(entries) {
    d3.select("#sidebar-btn").on("click", function (e) {
        $("#sidebar").toggleClass("active");
        $("#sidebar .sidebar-header").toggleClass("active");
        $("#sidebar #groups").toggleClass("active");
    });
    var selectedGroups = [];
    preloadGroups();
    addRemoveGroups();
    //Check preloaded groups
    function preloadGroups() {
        d3.selectAll("#groups input").each(function () {
            d3.select(this).attr("checked") == null ? "" : selectedGroups.push(d3.select(this).attr("value"));
        });
        var groupData = d3.filter(entries, function (d) { return selectedGroups.includes(d.group); });
        drawCharts(groupData);
    }
    //Handle add or remove groups
    function addRemoveGroups() {
        d3.selectAll("#groups input").on("change", function (e) {
            if (e.target.checked) {
                selectedGroups.push(e.target.value);
                var groupData = d3.filter(entries, function (d) { return selectedGroups.includes(d.group); });
                d3.select("#analytics-charts").html("");
                drawCharts(groupData);
            }
            else {
                selectedGroups.splice(selectedGroups.indexOf(e.target.value), 1);
                if (selectedGroups.length == 0) {
                    d3.select("#analytics-charts").html("");
                }
                var groupData = d3.filter(entries, function (d) { return selectedGroups.includes(d.group); });
                d3.select("#analytics-charts").html("");
                drawCharts(groupData);
            }
        });
    }
    function drawCharts(entries) {
        var htmlContainer = new HtmlContainers();
        //Append groups chart container
        htmlContainer.groupsChart = chartFunctions.appendDiv("groups-chart", "col-md-9");
        chartFunctions.appendCard(htmlContainer.groupsChart, "Reflections box plot by group");
        var data = [];
        entries.forEach(function (c) {
            var uniqueUsers = Array.from(d3.rollup(c.value, function (d) { return d.length; }, function (d) { return d.pseudonym; }), function (_a) {
                var key = _a[0], value = _a[1];
                return ({ key: key, value: value });
            });
            data.push({
                value: c.value.map(function (r) { return { timestamp: new Date(r.timestamp), point: parseInt(r.point), pseudonym: r.pseudonym }; }),
                group: c.group,
                mean: Math.round(d3.mean(c.value.map(function (r) { return r.point; }))),
                median: d3.median(c.value.map(function (r) { return r.point; })),
                q1: d3.quantile(c.value.map(function (r) { return r.point; }), 0.25),
                q3: d3.quantile(c.value.map(function (r) { return r.point; }), 0.75),
                max: d3.max(c.value.map(function (r) { return r.point; })),
                min: d3.min(c.value.map(function (r) { return r.point; })),
                variance: chartFunctions.data.roundDecimal(d3.variance(c.value.map(function (r) { return r.point; }))),
                deviation: chartFunctions.data.roundDecimal(d3.deviation(c.value.map(function (r) { return r.point; }))),
                oldestReflection: d3.min(c.value.map(function (r) { return new Date(r.timestamp); })),
                newestReflection: d3.max(c.value.map(function (r) { return new Date(r.timestamp); })),
                avgReflectionsPerUser: chartFunctions.data.roundDecimal(d3.mean(uniqueUsers.map(function (r) { return r.value; }))),
                userMostReflective: d3.max(uniqueUsers.map(function (r) { return r.value; })),
                userLessReflective: d3.min(uniqueUsers.map(function (r) { return r.value; }))
            });
        });
        var groupChart = setGroupChart(data);
        preRender(groupChart);
        renderGroupChart(groupChart, data);
        function setGroupChart(data) {
            var result = new Chart();
            result.id = "groups-chart";
            var containerDimensions = chartFunctions.getContainerDimension(result.id);
            result.width = containerDimensions.width;
            result.height = containerDimensions.height;
            result.x = chartFunctions.axis.setSeriesAxis("Group Code", data.map(function (r) { return r.group; }), [0, result.width - result.padding.yAxis - result.padding.right], "bottom");
            result.y = chartFunctions.axis.setValuesAxis("State", [0, 100], [result.height - result.padding.xAxis - result.padding.top, 0], "left");
            return result;
        }
        function preRender(chart) {
            chart.renderElements.svg = chartFunctions.appendSVG(chart);
            chart.renderElements.contentContainer = chartFunctions.appendContentContainer(chart);
            chart.renderElements.xAxis = chartFunctions.axis.appendXAxis(chart);
            chartFunctions.axis.appendXAxisLabel(chart);
            chartFunctions.axis.appendYAxis(chart);
            chartFunctions.axis.appendYAxisLabel(chart);
        }
        function renderGroupChart(chart, data) {
            //Draw min to max line
            chart.renderElements.contentContainer.selectAll(chart.id + "-data-min-max")
                .data(data)
                .enter()
                .append("line")
                .attr("id", chart.id + "-data-min-max")
                .attr("class", "box-line")
                .attr("x1", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                .attr("y1", function (d) { return chart.y.scale(d.min); })
                .attr("x2", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                .attr("y2", function (d) { return chart.y.scale(d.max); });
            //Draw median line
            chart.renderElements.contentContainer.selectAll(chart.id + "-data-median")
                .data(data)
                .enter()
                .append("line")
                .attr("id", chart.id + "-data-median")
                .attr("class", "box-line")
                .attr("x1", function (d) { return chart.x.scale(d.group); })
                .attr("y1", function (d) { return chart.y.scale(d.median); })
                .attr("x2", function (d) { return chart.x.scale(d.group) + chart.x.scale.bandwidth(); })
                .attr("y2", function (d) { return chart.y.scale(d.median); });
            //Draw boxes
            chart.renderElements.content = chart.renderElements.contentContainer.selectAll(chart.id + "-data")
                .data(data)
                .enter()
                .append("rect")
                .attr("id", chart.id + "-data")
                .classed("bar", true)
                .attr("width", chart.x.scale.bandwidth())
                .attr("height", function (d) { return chart.y.scale(d.q1) - chart.y.scale(d.q3); })
                .attr("x", function (d) { return chart.x.scale(d.group); })
                .attr("y", function (d) { return chart.y.scale(d.q3); });
            //Enable tooltip
            chartFunctions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
            function onMouseover(e, d) {
                if (d3.select(this).attr("class").includes("clicked")) {
                    return;
                }
                var tooltipBox = chartFunctions.tooltip.appendTooltipBox(chart);
                chartFunctions.tooltip.appendTooltipText(chart, d.group, [{ label: "q1", value: d.q1 }, { label: "q3", value: d.q3 }, { label: "Median", value: d.median }, { label: "Mean", value: d.mean }, { label: "Max", value: d.max }, { label: "Min", value: d.min }]);
                chartFunctions.tooltip.positionTooltipContainer(chart, xTooltip(d.group, tooltipBox), yTooltip(d.q3, tooltipBox));
                function xTooltip(x, tooltipBox) {
                    var xTooltip = chart.x.scale(x) + chart.x.scale.bandwidth();
                    if (chart.width - chart.padding.yAxis < xTooltip + tooltipBox.node().getBBox().width) {
                        return xTooltip - chart.x.scale.bandwidth() - tooltipBox.node().getBBox().width;
                    }
                    return xTooltip;
                }
                function yTooltip(y, tooltipBox) {
                    var yTooltip = chart.y.scale(y) - (tooltipBox.node().getBBox().height / 2);
                    if (chart.y.scale.invert(yTooltip) < 0) {
                        return chart.y.scale(y + chart.y.scale.invert(yTooltip));
                    }
                    return yTooltip;
                }
            }
            function onMouseout() {
                chart.renderElements.svg.select(".tooltip-container").transition()
                    .style("opacity", 0);
                chartFunctions.tooltip.removeTooltip(chart);
            }
            //Enable click
            chartFunctions.click.enableClick(chart, onClick);
            function onClick(e, d) {
                if (d3.select(this).attr("class") == "bar clicked") {
                    chartFunctions.click.removeClick(chart);
                    d3.select(this).attr("class", "bar");
                    htmlContainer.remove();
                    return;
                }
                chartFunctions.click.removeClick(chart);
                if (htmlContainer.groupStatistics != undefined) {
                    htmlContainer.remove();
                }
                chart.renderElements.contentContainer.selectAll(".bar.clicked")
                    .attr("class", "bar");
                d3.select(this).attr("class", "bar clicked");
                data.forEach(function (c) {
                    appendText(d.q3, c.q3, d.group, c.group, "q3");
                    appendText(d.median, c.median, d.group, c.group, "Median");
                    appendText(d.q1, c.q1, d.group, c.group, "q1");
                });
                function appendText(clickValue, value, clickXValue, xValue, label) {
                    var textClass = "click-text";
                    var textSymbol = "";
                    if (clickValue - value < 0) {
                        textClass = textClass + " positive";
                        textSymbol = "+";
                    }
                    else if (clickValue - value > 0) {
                        textClass = textClass + " negative";
                        textSymbol = "-";
                    }
                    else {
                        textClass = textClass + " black";
                    }
                    chart.renderElements.contentContainer.append("text")
                        .attr("class", textClass)
                        .attr("x", chart.x.scale(xValue) + (chart.x.scale.bandwidth() / 2))
                        .attr("y", chart.y.scale(value) - 5)
                        .text(label + ": " + textSymbol + (clickXValue == xValue ? clickValue : (Math.abs(clickValue - value))));
                }
                //Show selected group general statistics
                htmlContainer.groupStatistics = chartFunctions.appendDiv("groups-statistics", "col-md-3");
                var groupsStatisticsCard = chartFunctions.appendCard(htmlContainer.groupStatistics, "Statitics (" + d.group + ")");
                groupsStatisticsCard.select(".card-body")
                    .attr("class", "card-body statistics-text")
                    .html("<b>Q1: </b>" + d.q1 + "<br>\n                        <b>Median: </b>" + d.median + "<br>\n                        <b>Q3: </b>" + d.q3 + "<br>\n                        <b>Mean: </b>" + d.mean + "<br>\n                        <b>Variance: </b>" + d.variance + "<br>\n                        <b>Std Deviation: </b>" + d.deviation + "<br>\n                        <b>Max: </b>" + d.max + "<br>\n                        <b>Min: </b>" + d.min + "<br>\n                        <b>Reflections per user: </b>" + d.avgReflectionsPerUser + "<br>\n                        <b>Max reflections per user: </b>" + d.userMostReflective + "<br>\n                        <b>Min reflections per user: </b>" + d.userLessReflective + "<br>\n                        <b>Oldest reflection</b><br>" + d.oldestReflection.toDateString() + "<br>\n                        <b>Newest reflection</b><br>" + d.newestReflection.toDateString() + "<br>");
                //Draw selected group timeline
                htmlContainer.groupTimeline = chartFunctions.appendDiv("group-timeline", "col-md-12 mt-3");
                var timelineCard = chartFunctions.appendCard(htmlContainer.groupTimeline, "Reflections vs Time (" + d.group + ")");
                timelineCard.select(".card-body")
                    .attr("class", "card-body")
                    .html("<div class=\"row\">\n                        <div id=\"timeline-plot\" class=\"btn-group btn-group-toggle mr-auto ml-auto\" data-toggle=\"buttons\">\n                            <label class=\"btn btn-light active\">\n                                <input type=\"radio\" name=\"plot\" value=\"density\" checked>Density Plot<br>\n                            </label>\n                            <label class=\"btn btn-light\">\n                                <input type=\"radio\" name=\"plot\" value=\"scatter\">Scatter Plot<br>\n                            </label>\n                        </div>\n                    </div>")
                    .append("div")
                    .attr("class", "chart-container");
                groupTimeline(d.value);
                //Draw compare
                htmlContainer.compare = chartFunctions.appendDiv("group-compare", "col-md-2 mt-3");
                var compareCard = chartFunctions.appendCard(htmlContainer.compare, "Compare " + d.group + " with:");
                compareCard.select(".card-body").attr("class", "card-body");
                groupCompare(data, d.group);
                //Draw groups violin container
                htmlContainer.groupViolin = chartFunctions.appendDiv("group-violin-chart", "col-md-5 mt-3");
                chartFunctions.appendCard(htmlContainer.groupViolin, "Reflections distribution (" + d.group + ")");
                //Draw users violin container
                htmlContainer.userViolin = chartFunctions.appendDiv("group-violin-users-chart", "col-md-5 mt-3");
                chartFunctions.appendCard(htmlContainer.userViolin, "Users distribution (" + d.group + ")");
                //Draw violin charts
                groupViolinChart(data, d.group);
            }
            //Enable sort
            var sortButton = chart.renderElements.svg.select(".y-label-container").attr("class", "y-label-container zoom");
            var yArrow = chartFunctions.sort.appendArrow(sortButton, chart, false, true);
            sortButton.on("click", function () {
                chartFunctions.sort.setSorted(chart, false, true);
                chartFunctions.sort.arrowTransition(chart.renderElements.svg, chart, yArrow, false, true);
                data = data.sort(function (a, b) {
                    return chartFunctions.sort.sortData(a.mean, b.mean, chart.y.sorted);
                });
                chartFunctions.click.removeClick(chart);
                chart.renderElements.svg.selectAll(".bar.clicked")
                    .attr("class", "bar");
                axisTransition(chart, data);
                barsTransition(chart, data);
            });
        }
        function groupTimeline(data) {
            var timelineChart = setTimelineChart(data);
            renderTimelineDensity(timelineChart, data);
            d3.select("#group-timeline #timeline-plot").on("click", function (e) {
                var selectedOption = e.target.control.value;
                if (selectedOption == "density") {
                    var timelineChart_1 = setTimelineChart(data);
                    renderTimelineDensity(timelineChart_1, data);
                }
                if (selectedOption == "scatter") {
                    var timelineChart_2 = setTimelineChart(data);
                    var timelineZoomChart = setTimelineZoomChart(timelineChart_2, data);
                    renderTimelineScatter(timelineChart_2, timelineZoomChart, data);
                }
            });
            function setTimelineChart(data) {
                var result = new Chart();
                result.id = "group-timeline";
                var containerDimensions = chartFunctions.getContainerDimension(result.id);
                result.width = containerDimensions.width;
                result.height = containerDimensions.height;
                result.padding.xAxis = result.padding.xAxis + 25;
                result.padding.top = 5;
                result.x = chartFunctions.axis.setTimeSeriesAxis("Time", d3.extent(data.map(function (r) { return r.timestamp; })), [0, result.width - result.padding.yAxis], "bottom");
                result.y = chartFunctions.axis.setValuesAxis("State", [0, 100], [result.height - result.padding.xAxis - result.padding.top, 0], "left");
                return result;
            }
            function setTimelineZoomChart(chart, data) {
                var result = new Chart();
                result.x = chartFunctions.axis.setTimeSeriesAxis("", d3.extent(data.map(function (r) { return r.timestamp; })), [0, chart.width - chart.padding.yAxis - 5], "bottom");
                result.y = chartFunctions.axis.setValuesAxis("", [0, 100], [25, 0], "left");
                return result;
            }
            function renderTimelineDensity(chart, data) {
                //Remove scatter plot
                d3.select("#group-timeline").selectAll("svg").remove();
                preRender(chart);
                //Create density data
                var densityData = d3.contourDensity()
                    .x(function (d) { return chart.x.scale(d.timestamp); })
                    .y(function (d) { return chart.y.scale(d.point); })
                    .bandwidth(5)
                    .thresholds(20)(data);
                //Draw contours
                chart.renderElements.content = chart.renderElements.contentContainer.selectAll(chart.id + "-timeline-contours")
                    .data(densityData)
                    .enter()
                    .append("path")
                    .attr("id", chart.id + "-timeline-contours")
                    .attr("class", "contour")
                    .attr("d", d3.geoPath())
                    .attr("stroke", function (d) { return d3.interpolateBlues(d.value * 25); })
                    .attr("fill", function (d) { return d3.interpolateBlues(d.value * 20); });
                //Enable zoom
                chartFunctions.zoom.enableZoom(chart, zoomed);
                function zoomed(e) {
                    var newChartRange = [0, chart.width - chart.padding.yAxis].map(function (d) { return e.transform.applyX(d); });
                    chart.x.scale.rangeRound(newChartRange);
                    var newDensityData = d3.contourDensity()
                        .x(function (d) { return chart.x.scale(d.timestamp); })
                        .y(function (d) { return chart.y.scale(d.point); })
                        .bandwidth(5)
                        .thresholds(20)(data);
                    chart.renderElements.contentContainer.selectAll("#" + chart.id + "-timeline-contours").remove();
                    chart.renderElements.contentContainer.selectAll("#" + chart.id + "-timeline-contours")
                        .data(newDensityData)
                        .enter()
                        .append("path")
                        .attr("id", chart.id + "-timeline-contours")
                        .attr("class", "contour")
                        .attr("d", d3.geoPath())
                        .attr("stroke", function (d) { return d3.interpolateBlues(d.value * 25); })
                        .attr("fill", function (d) { return d3.interpolateBlues(d.value * 20); });
                    chart.renderElements.contentContainer.selectAll(".contour")
                        .attr("clip-path", "url(#clip-" + chart.id + ")");
                    chart.x.axis.ticks(newChartRange[1] / 75);
                    chart.renderElements.xAxis.call(chart.x.axis);
                }
            }
            function renderTimelineScatter(chart, zoomChart, data) {
                //Remove density plot
                d3.select("#group-timeline").selectAll("svg").remove();
                preRender(chart);
                //Draw circles
                chart.renderElements.content = chart.renderElements.contentContainer.selectAll(chart.id + "-timeline-circles")
                    .data(data)
                    .enter()
                    .append("circle")
                    .classed("line-circle", true)
                    .attr("id", chart.id + "-timeline-circles")
                    .attr("r", 5)
                    .attr("cx", function (d) { return chart.x.scale(d.timestamp); })
                    .attr("cy", function (d) { return chart.y.scale(d.point); });
                //Enable tooltip
                chartFunctions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
                function onMouseover(e, d) {
                    if (d3.select(this).attr("class").includes("clicked")) {
                        return;
                    }
                    var tooltipBox = chartFunctions.tooltip.appendTooltipBox(chart);
                    chartFunctions.tooltip.appendTooltipText(chart, d.timestamp.toDateString(), [{ label: "State", value: d.point }]);
                    chartFunctions.tooltip.positionTooltipContainer(chart, xTooltip(d.timestamp, tooltipBox), yTooltip(d.point, tooltipBox));
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
                    chartFunctions.tooltip.appendLine(chart, 0, chart.y.scale(d.point), chart.x.scale(d.timestamp), chart.y.scale(d.point));
                    chartFunctions.tooltip.appendLine(chart, chart.x.scale(d.timestamp), chart.y.scale(0), chart.x.scale(d.timestamp), chart.y.scale(d.point));
                }
                function onMouseout() {
                    chart.renderElements.svg.select(".tooltip-container").transition()
                        .style("opacity", 0);
                    chartFunctions.tooltip.removeTooltip(chart);
                }
                //Enable click
                chartFunctions.click.enableClick(chart, onClick);
                function onClick(e, d) {
                    if (d3.select(this).attr("class") == "line-circle clicked") {
                        chartFunctions.click.removeClick(chart);
                        chart.renderElements.content.attr("class", "line-circle");
                        chart.renderElements.contentContainer.selectAll("#" + chart.id + "-timeline-circles-line").remove();
                        return;
                    }
                    chartFunctions.click.removeClick(chart);
                    chart.renderElements.contentContainer.selectAll("#" + chart.id + "-timeline-circles-line").remove();
                    chart.renderElements.content.attr("class", function (data) { return "line-circle " + (data.pseudonym == d.pseudonym ? "clicked" : ""); });
                    var line = d3.line()
                        .x(function (d) { return chart.x.scale(d.timestamp); })
                        .y(function (d) { return chart.y.scale(d.point); });
                    chart.renderElements.contentContainer.append("path")
                        .datum(d3.sort(data.filter(function (c) { return c.pseudonym == d.pseudonym; }), function (d) { return d.timestamp; }))
                        .classed("line", true)
                        .attr("id", chart.id + "-timeline-circles-line")
                        .attr("d", function (d) { return line(d); });
                    chart.renderElements.contentContainer.selectAll(".line")
                        .attr("clip-path", "url(#clip-" + chart.id + ")");
                    chartFunctions.click.appendText(chart, d, d.pseudonym, [{ label: "Avg", value: Math.round(d3.mean(data.filter(function (c) { return c.pseudonym == d.pseudonym; }).map(function (r) { return r.point; }))) }, { label: "Count", value: data.filter(function (c) { return c.pseudonym == d.pseudonym; }).length }]);
                }
                //Append zoom bar
                chart.renderElements.zoomSVG = chartFunctions.zoom.appendZoomBar(chart);
                chart.renderElements.zoomFocus = chart.renderElements.zoomSVG.append("g")
                    .attr("class", "zoom-focus");
                //Draw in zoom bar
                chart.renderElements.zoomSVG.selectAll(chart.id + "zoom-bar-content")
                    .data(data)
                    .enter()
                    .append("circle")
                    .classed("zoom-line-circle", true)
                    .attr("id", chart.id + "zoom-bar-content")
                    .attr("r", 2)
                    .attr("cx", function (d) { return zoomChart.x.scale(d.timestamp); })
                    .attr("cy", function (d) { return zoomChart.y.scale(d.point); });
                //Draw hidden content that will handle the borders
                chart.renderElements.zoomFocus.selectAll(chart.id + "zoom-content")
                    .data(data)
                    .enter()
                    .append("circle")
                    .classed("zoom-content", true)
                    .attr("id", chart.id + "zoom-bar-content")
                    .attr("r", 2)
                    .attr("cx", function (d) { return zoomChart.x.scale(d.timestamp); })
                    .attr("cy", function (d) { return zoomChart.y.scale(d.point); });
                //Enable zoom
                chartFunctions.zoom.enableZoom(chart, zoomed);
                function zoomed(e) {
                    var newChartRange = [0, chart.width - chart.padding.yAxis].map(function (d) { return e.transform.applyX(d); });
                    chart.x.scale.rangeRound(newChartRange);
                    zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis - 5].map(function (d) { return e.transform.invertX(d); }));
                    var newLine = d3.line()
                        .x(function (d) { return chart.x.scale(d.timestamp); })
                        .y(function (d) { return chart.y.scale(d.point); });
                    chart.renderElements.contentContainer.selectAll("#" + chart.id + "-timeline-circles")
                        .attr("cx", function (d) { return chart.x.scale(d.timestamp); });
                    chart.renderElements.contentContainer.selectAll("#" + chart.id + "-timeline-circles-line")
                        .attr("d", function (d) { return newLine(d); });
                    chart.renderElements.zoomFocus.selectAll(".zoom-content")
                        .attr("cx", function (d) { return zoomChart.x.scale(d.timestamp); });
                    chart.x.axis.ticks(newChartRange[1] / 75);
                    chart.renderElements.xAxis.call(chart.x.axis);
                    chartFunctions.click.removeClick(chart);
                }
            }
        }
        function groupViolinChart(data, group) {
            var currentGroups = [group];
            var groupData = d3.filter(data, function (d) { return currentGroups.includes(d.group); });
            var currentData = [];
            groupData.forEach(function (c) {
                var userAvg = Array.from(d3.rollup(c.value, function (d) { return Math.round(d3.mean(d.map(function (r) { return r.point; }))); }, function (d) { return d.pseudonym; }), function (_a) {
                    var pseudonym = _a[0], point = _a[1];
                    return ({ pseudonym: pseudonym, point: point });
                });
                currentData.push({ group: c.group, value: userAvg });
            });
            //Remove svg
            d3.select("#group-violin-chart").selectAll("svg").remove();
            d3.select("#group-violin-users-chart").selectAll("svg").remove();
            var violinChart = setViolinChart(groupData, "group-violin-chart");
            preRender(violinChart);
            renderViolin(violinChart, groupData, 30, 70);
            var violinUsersChart = setViolinChart(currentData, "group-violin-users-chart");
            preRender(violinUsersChart);
            renderViolin(violinUsersChart, currentData, 30, 70);
            function setViolinChart(data, id) {
                var result = new Chart();
                result.id = id;
                var containerDimensions = chartFunctions.getContainerDimension(result.id);
                result.width = containerDimensions.width;
                result.height = containerDimensions.height;
                result.padding.right = 85;
                result.x = chartFunctions.axis.setSeriesAxis("Group Code", data.map(function (r) { return r.group; }), [0, result.width - result.padding.yAxis - result.padding.right], "bottom");
                result.y = chartFunctions.axis.setValuesAxis("State", [0, 100], [result.height - result.padding.xAxis - result.padding.top, 0], "left");
                return result;
            }
            function renderViolin(chart, data, tDistressed, tSoaring) {
                //Create threshold axis
                var thresholdAxis = chartFunctions.axis.setThresholdAxis(chart, tDistressed, tSoaring);
                //Draw threshold axis
                chartFunctions.axis.appendThresholdAxis(chart, thresholdAxis);
                //Draw threshold indicators
                chartFunctions.axis.appendThresholdIndicators(chart, [tDistressed, tSoaring]);
                //Draw threshold label
                chartFunctions.axis.appendThresholdLabel(chart);
                //Draw threshold lines
                chartFunctions.drag.appendThresholdLine(chart, [tDistressed, tSoaring]);
                chart.renderElements.contentContainer.select(".threshold-line.distressed")
                    .call(d3.drag()
                        .on("start", dragStartDistressed)
                        .on("drag", draggingDistressed)
                        .on("end", dragEndDistressed));
                chart.renderElements.contentContainer.select(".threshold-line.soaring")
                    .call(d3.drag()
                        .on("start", dragStartSoaring)
                        .on("drag", draggingSoaring)
                        .on("end", dragEndSoaring));
                //Start drag soaring functions
                function dragStartSoaring(e, d) {
                    chart.renderElements.contentContainer.selectAll("." + chart.id + "-violin-text-container").remove();
                }
                function draggingSoaring(e, d) {
                    if (chart.y.scale.invert(e.y) < 51 || chart.y.scale.invert(e.y) > 99) {
                        return;
                    }
                    d3.select(this)
                        .attr("y1", chart.y.scale(chart.y.scale.invert(e.y)))
                        .attr("y2", chart.y.scale(chart.y.scale.invert(e.y)));
                    tSoaring = chart.y.scale.invert(e.y);
                    thresholdAxis.tickValues([tDistressed, chart.y.scale.invert(e.y)])
                        .tickFormat(function (d) { return d == tDistressed ? "Distressed" : d == chart.y.scale.invert(e.y) ? "Soaring" : ""; });
                    chart.renderElements.contentContainer.selectAll(".threshold-axis")
                        .call(thresholdAxis);
                    var positionX = chart.width - chart.padding.yAxis - chart.padding.right + 5;
                    var positionY = chart.y.scale(tSoaring) + 25;
                    var indicator = chart.renderElements.contentContainer.select(".threshold-indicator-container.soaring");
                    if (positionY + indicator.node().getBBox().height > chart.y.scale(tDistressed)) {
                        positionY = chart.y.scale(tSoaring) - 15;
                    }
                    indicator.attr("transform", "translate(" + positionX + ", " + positionY + ")");
                    indicator.select("text")
                        .text(Math.round(tSoaring));
                }
                function dragEndSoaring(e, d) {
                    var newT = chart.y.scale.invert(e.y);
                    if (newT < 51) {
                        newT = 51;
                    }
                    if (newT > 99) {
                        newT = 99;
                    }
                    var newBin = d3.bin().domain([0, 100]).thresholds([0, tDistressed, newT]);
                    chart.renderElements.content.remove();
                    chart.renderElements.content = binContainer.append("path")
                        .attr("id", chart.id + "-violin")
                        .attr("class", "violin-path")
                        .datum(function (d) { return newBin(d.value.map(function (d) { return d.point; })); })
                        .attr("d", d3.area()
                            .x0(function (d) { return bandwithScale(-d.length); })
                            .x1(function (d) { return bandwithScale(d.length); })
                            .y(function (d, i) { return chart.y.scale(i == 0 ? 0 : i == 1 ? 50 : 100); })
                            .curve(d3.curveCatmullRom));
                    chartFunctions.drag.appendThresholdPercentages(chart, binContainer, newBin, bandwithScale, tDistressed, newT);
                }
                //Start drag distressed functions
                function dragStartDistressed(e, d) {
                    chart.renderElements.contentContainer.selectAll("." + chart.id + "-violin-text-container").remove();
                }
                function draggingDistressed(e, d) {
                    if (chart.y.scale.invert(e.y) < 1 || chart.y.scale.invert(e.y) > 49) {
                        return;
                    }
                    d3.select(this)
                        .attr("y1", chart.y.scale(chart.y.scale.invert(e.y)))
                        .attr("y2", chart.y.scale(chart.y.scale.invert(e.y)));
                    tDistressed = chart.y.scale.invert(e.y);
                    thresholdAxis.tickValues([chart.y.scale.invert(e.y), tSoaring])
                        .tickFormat(function (d) { return d == chart.y.scale.invert(e.y) ? "Distressed" : d == tSoaring ? "Soaring" : ""; });
                    chart.renderElements.contentContainer.selectAll(".threshold-axis")
                        .call(thresholdAxis);
                    var soaringIndicator = chart.renderElements.contentContainer.select(".threshold-indicator-container.soaring");
                    if (chart.y.scale(tDistressed) < chart.y.scale(tSoaring) + soaringIndicator.node().getBBox().height + 25) {
                        soaringIndicator.attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right + 5) + ", " + (chart.y.scale(tSoaring) - 15) + ")");
                    }
                    else {
                        soaringIndicator.attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right + 5) + ", " + (chart.y.scale(tSoaring) + 25) + ")");
                    }
                    var indicator = chart.renderElements.contentContainer.select(".threshold-indicator-container.distressed")
                        .attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right + 5) + ", " + (chart.y.scale(tDistressed) + 25) + ")");
                    indicator.select("text")
                        .text(Math.round(tDistressed));
                }
                function dragEndDistressed(e, d) {
                    var newT = chart.y.scale.invert(e.y);
                    if (newT < 1) {
                        newT = 1;
                    }
                    if (newT > 49) {
                        newT = 49;
                    }
                    var newBin = d3.bin().domain([0, 100]).thresholds([0, newT, tSoaring]);
                    chart.renderElements.content.remove();
                    chart.renderElements.content = binContainer.append("path")
                        .attr("id", chart.id + "-violin")
                        .attr("class", "violin-path")
                        .datum(function (d) { return newBin(d.value.map(function (d) { return d.point; })); })
                        .attr("d", d3.area()
                            .x0(function (d) { return bandwithScale(-d.length); })
                            .x1(function (d) { return bandwithScale(d.length); })
                            .y(function (d, i) { return chart.y.scale(i == 0 ? 0 : i == 1 ? 50 : 100); })
                            .curve(d3.curveCatmullRom));
                    chartFunctions.drag.appendThresholdPercentages(chart, binContainer, newBin, bandwithScale, newT, tSoaring);
                }
                //Create bandwidth scale
                var bandwithScale = d3.scaleLinear()
                    .range([0, chart.x.scale.bandwidth()])
                    .domain([-d3.max(data.map(function (r) { return r.value.length; })), d3.max(data.map(function (r) { return r.value.length; }))]);
                //Create bins
                var bin = d3.bin().domain([0, 100]).thresholds([0, tDistressed, tSoaring]);
                //Create bins container
                var binContainer = chart.renderElements.contentContainer.selectAll(chart.id + "-violin-container")
                    .data(data)
                    .enter()
                    .append("g")
                    .attr("class", chart.id + "-violin-container")
                    .attr("transform", function (d) { return "translate(" + chart.x.scale(d.group) + ", 0)"; });
                //Draw violins
                chart.renderElements.content = binContainer.append("path")
                    .attr("id", chart.id + "-violin")
                    .attr("class", "violin-path")
                    .datum(function (d) { return bin(d.value.map(function (d) { return d.point; })); })
                    .attr("d", d3.area()
                        .x0(function (d) { return bandwithScale(-d.length); })
                        .x1(function (d) { return bandwithScale(d.length); })
                        .y(function (d, i) { return chart.y.scale(i == 0 ? 0 : i == 1 ? 50 : 100); })
                        .curve(d3.curveCatmullRom));
                //Draw threshold percentages
                chartFunctions.drag.appendThresholdPercentages(chart, binContainer, bin, bandwithScale, tDistressed, tSoaring);
            }
            d3.selectAll("#group-compare input").on("change", function (e) {
                if (e.target.checked) {
                    currentGroups.push(e.target.value);
                }
                else {
                    currentGroups.splice(currentGroups.indexOf(e.target.value), 1);
                }
                var groupData = d3.filter(data, function (d) { return currentGroups.includes(d.group); });
                var currentData = [];
                groupData.forEach(function (c) {
                    var userAvg = Array.from(d3.rollup(c.value, function (d) { return Math.round(d3.mean(d.map(function (r) { return r.point; }))); }, function (d) { return d.pseudonym; }), function (_a) {
                        var pseudonym = _a[0], point = _a[1];
                        return ({ pseudonym: pseudonym, point: point });
                    });
                    currentData.push({ group: c.group, value: userAvg });
                });
                d3.select("#group-violin-chart svg").remove();
                var violinChart = setViolinChart(groupData, "group-violin-chart");
                preRender(violinChart);
                renderViolin(violinChart, groupData, 30, 70);
                d3.select("#group-violin-users-chart svg").remove();
                var violinUsersChart = setViolinChart(currentData, "group-violin-users-chart");
                preRender(violinUsersChart);
                renderViolin(violinUsersChart, currentData, 30, 70);
            });
        }
        function groupCompare(data, group) {
            //Remove current groups
            d3.select("#group-compare .card-body").html("");
            var currentGroups = [group];
            var groupsData = d3.filter(data, function (d) { return !currentGroups.includes(d.group); });
            var container = d3.select("#group-compare .card-body");
            groupsData.forEach(function (c) {
                container.html(container.html() + ("<div class=\"form-check\">\n                                    <input class=\"form-check-input\" type=\"checkbox\" value=\"" + c.group + "\" id=\"compare-" + c.group + "\" />\n                                    <label class=\"form-check-label\" for=\"compare-" + c.group + "\">" + c.group + "</label>\n                                </div>"));
            });
        }
        function axisTransition(chart, data) {
            chart.x.scale.domain(data.map(function (d) { return d.group; }));
            chart.renderElements.svg.select(".x-axis").transition()
                .duration(750)
                .call(chart.x.axis);
        }
        function barsTransition(chart, data) {
            chart.renderElements.contentContainer.selectAll("#" + chart.id + "-data")
                .data(data)
                .transition()
                .duration(750)
                .attr("height", function (d) { return chart.y.scale(d.q1) - chart.y.scale(d.q3); })
                .attr("y", function (d) { return chart.y.scale(d.q3); });
            chart.renderElements.contentContainer.selectAll("#" + chart.id + "-data-min-max")
                .data(data)
                .transition()
                .duration(750)
                .attr("x1", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                .attr("y1", function (d) { return chart.y.scale(d.min); })
                .attr("x2", function (d) { return chart.x.scale(d.group) + (chart.x.scale.bandwidth() / 2); })
                .attr("y2", function (d) { return chart.y.scale(d.max); });
            chart.renderElements.contentContainer.selectAll("#" + chart.id + "-data-median")
                .data(data)
                .transition()
                .duration(750)
                .attr("x1", function (d) { return chart.x.scale(d.group); })
                .attr("y1", function (d) { return chart.y.scale(d.median); })
                .attr("x2", function (d) { return chart.x.scale(d.group) + chart.x.scale.bandwidth(); })
                .attr("y2", function (d) { return chart.y.scale(d.median); });
        }
    }
    //Resize window
    $(window).resize(function () {
        d3.select("#analytics-charts").html("");
        buildAnalyticsCharts(entries);
    });
}
var chartFunctions = {
    appendDiv: function (id, css) {
        return d3.select("#analytics-charts").append("div")
            .attr("id", id)
            .attr("class", css);
    },
    appendCard: function (div, header) {
        var card = div.append("div")
            .attr("class", "card");
        card.append("div")
            .attr("class", "card-header")
            .html(header);
        card.append("div")
            .attr("class", "card-body chart-container");
        return card;
    },
    getContainerDimension: function (containerId) {
        return { width: $("#" + containerId + " .chart-container").width(), height: $("#" + containerId + " .chart-container").height() };
    },
    appendSVG: function (chart) {
        return d3.select("#" + chart.id)
            .select(".chart-container")
            .append("svg")
            .attr("id", "chart-" + chart.id)
            .attr("preserveAspectRatio", "xMinYMin meet")
            .attr("viewBox", "0 0 "+chart.width+" "+chart.height);
    },
    appendContentContainer: function (chart) {
        var result = chart.renderElements.svg.append("g")
            .attr("class", "content-container")
            .attr("transform", "translate(" + chart.padding.yAxis + ", " + chart.padding.top + ")");
        result.append("rect")
            .attr("class", "zoom-rect")
            .attr("width", chart.width - chart.padding.yAxis - chart.padding.right)
            .attr("height", chart.height - chart.padding.xAxis - chart.padding.top);
        return result;
    },
    data: {
        roundDecimal: function (value) {
            var p = d3.precisionFixed(0.1);
            var f = d3.format("." + p + "f");
            return f(value);
        }
    },
    axis: {
        setSeriesAxis: function (label, series, range, position) {
            var result = {};
            result.label = label;
            result.scale = d3.scaleBand()
                .domain(series)
                .rangeRound(range)
                .padding(0.25);
            result.axis = this.positionAxis(position, result.scale);
            return result;
        },
        setValuesAxis: function (label, values, range, position, isGoingOk) {
            if (isGoingOk === void 0) { isGoingOk = true; }
            var result = {};
            result.label = label;
            result.scale = d3.scaleLinear()
                .domain([d3.min(values), d3.max(values)])
                .range(range);
            result.axis = this.positionAxis(position, result.scale);
            if (isGoingOk) {
                var labels_1 = new Map();
                labels_1.set(0, "distressed");
                labels_1.set(50, "going ok");
                labels_1.set(100, "soaring");
                result.axis.tickValues([0, 25, 50, 75, 100])
                    .tickFormat(function (d) { return labels_1.get(d); });
            }
            return result;
        },
        setTimeSeriesAxis: function (label, domain, range, position) {
            var result = {};
            result.label = label;
            result.scale = d3.scaleTime()
                .domain(domain)
                .range(range);
            result.axis = this.positionAxis(position, result.scale);
            result.axis = result.axis.ticks(range[1] / 75);
            return result;
        },
        setThresholdAxis: function (chart, tDistressed, tSoaring) {
            return d3.axisRight()
                .scale(chart.y.scale)
                .tickValues([tDistressed, tSoaring])
                .tickFormat(function (d) { return d == tDistressed ? "Distressed" : d == tSoaring ? "Soaring" : ""; });
        },
        positionAxis: function (position, scale) {
            if (position == "left") {
                return d3.axisLeft()
                    .scale(scale);
            }
            else if (position == "top") {
                return d3.axisTop()
                    .scale(scale);
            }
            else if (position == "bottom") {
                return d3.axisBottom()
                    .scale(scale);
            }
            else if (position == "right") {
                return d3.axisRight()
                    .scale(scale);
            }
        },
        appendXAxis: function (chart) {
            return chart.renderElements.contentContainer.append("g")
                .attr("transform", "translate(0, " + (chart.height - chart.padding.xAxis - chart.padding.top) + ")")
                .attr("class", "x-axis")
                .call(chart.x.axis);
        },
        appendXAxisLabel: function (chart) {
            return chart.renderElements.svg.append("g")
                .attr("class", "x-label-container")
                .attr("transform", "translate(" + (chart.renderElements.svg.select(".x-axis").node().getBBox().width / 2 + chart.padding.yAxis) + ", " + (chart.height - chart.padding.xAxis + chart.renderElements.svg.select(".x-axis").node().getBBox().height * 2) + ")")
                .append("text")
                .attr("class", "x-label-text")
                .attr("text-anchor", "middle")
                .text(chart.x.label);
        },
        appendYAxis: function (chart) {
            return chart.renderElements.contentContainer.append("g")
                .attr("transform", "translate(0 , 0)")
                .attr("class", "y-axis")
                .call(chart.y.axis);
        },
        appendYAxisLabel: function (chart) {
            return chart.renderElements.svg.append("g")
                .attr("class", "y-label-container")
                .attr("transform", "translate(" + (chart.padding.yAxis - chart.renderElements.svg.select(".y-axis").node().getBBox().width) + ", " + (chart.padding.top + chart.renderElements.svg.select(".y-axis").node().getBBox().height / 2) + ") rotate(-90)")
                .append("text")
                .attr("class", "y-label-text")
                .attr("text-anchor", "middle")
                .text(chart.y.label);
        },
        appendThresholdAxis: function (chart, axis) {
            return chart.renderElements.contentContainer.append("g")
                .attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right) + ", 0)")
                .attr("class", "threshold-axis")
                .call(axis);
        },
        appendThresholdLabel: function (chart) {
            var label = chart.renderElements.svg.append("g")
                .attr("class", "threshold-label-container");
            label.append("text")
                .attr("class", "y-label-text")
                .attr("text-anchor", "middle")
                .text("Thresholds");
            label.attr("transform", "translate(" + (chart.width - chart.padding.right + chart.renderElements.contentContainer.select(".threshold-axis").node().getBBox().width + label.node().getBBox().height) + ", " + (chart.padding.top + chart.renderElements.svg.select(".y-axis").node().getBBox().height / 2) + ") rotate(-90)");
            return label;
        },
        appendThresholdIndicators: function (chart, thresholds) {
            thresholds.forEach(function (c, i) {
                var indicator = chart.renderElements.contentContainer.append("g")
                    .attr("class", "threshold-indicator-container " + (i == 0 ? "distressed" : "soaring"))
                    .attr("transform", "translate(" + (chart.width - chart.padding.yAxis - chart.padding.right + 5) + ", " + (chart.y.scale(c) + 25) + ")");
                var box = indicator.append("rect")
                    .attr("class", "threshold-indicator-box " + (i == 0 ? "distressed" : "soaring"));
                var text = indicator.append("text")
                    .attr("class", "threshold-indicator-text")
                    .attr("x", 5)
                    .text(c);
                box.attr("width", text.node().getBBox().width + 10)
                    .attr("height", text.node().getBBox().height + 5)
                    .attr("y", -text.node().getBBox().height);
            });
        }
    },
    tooltip: {
        enableTooltip: function (chart, onMouseover, onMouseout) {
            chart.renderElements.contentContainer.selectAll(".tooltip-container").remove();
            this.appendTooltipContainer(chart);
            chart.renderElements.content.on("mouseover", onMouseover)
                .on("mouseout", onMouseout);
        },
        appendTooltipContainer: function (chart) {
            return chart.renderElements.contentContainer.append("g")
                .attr("class", "tooltip-container");
        },
        appendTooltipBox: function (chart) {
            return chart.renderElements.contentContainer.select(".tooltip-container").append("rect")
                .attr("class", "tooltip-box");
        },
        appendTooltipText: function (chart, title, values) {
            var result = chart.renderElements.contentContainer.select(".tooltip-container").append("text")
                .attr("class", "tooltip-text title")
                .attr("x", 10)
                .text(title);
            var textSize = result.node().getBBox().height;
            result.attr("y", textSize);
            values.forEach(function (c, i) {
                result.append("tspan")
                    .attr("class", "tooltip-text")
                    .attr("y", textSize * (i + 2))
                    .attr("x", 15)
                    .text(c.label + ": " + c.value);
            });
            chart.renderElements.contentContainer.select(".tooltip-box").attr("width", result.node().getBBox().width + 20)
                .attr("height", result.node().getBBox().height + 5);
            return result;
        },
        appendLine: function (chart, x1, y1, x2, y2) {
            chart.renderElements.contentContainer.append("line")
                .attr("class", "tooltip-line")
                .attr("x1", x1)
                .attr("y1", y1)
                .attr("x2", x2)
                .attr("y2", y2);
        },
        positionTooltipContainer: function (chart, x, y) {
            chart.renderElements.contentContainer.select(".tooltip-container")
                .attr("transform", "translate(" + x + ", " + y + ")")
                .transition()
                .style("opacity", 1);
        },
        removeTooltip: function (chart) {
            chart.renderElements.contentContainer.selectAll(".tooltip-box").remove();
            chart.renderElements.contentContainer.selectAll(".tooltip-text").remove();
            chart.renderElements.contentContainer.selectAll(".tooltip-line").remove();
        }
    },
    sort: {
        appendArrow: function (button, chart, x, y) {
            if (x === void 0) { x = false; }
            if (y === void 0) { y = false; }
            return button.append("polygon")
                .attr("class", "sort-arrow")
                .attr("points", this.arrowPoints(button, chart, x, y));
        },
        arrowPoints: function (svg, chart, x, y) {
            var selector = x == true ? ".x-label-text" : ".y-label-text";
            var height = svg.select(selector).node().getBBox().height;
            var width = svg.select(selector).node().getBBox().width;
            var point1 = [(width / 2) + 5, 0];
            var point2 = [(width / 2) + 5, -height / 2];
            var point3 = [(width / 2) + 15, -height / 4];
            if ((x == true && chart.x.sorted == false) || (y == true && chart.y.sorted == false)) {
                point1 = [-(width / 2) - 5, 0];
                point2 = [-(width / 2) - 5, -height / 2];
                point3 = [-(width / 2) - 15, -height / 4];
            }
            return point1 + ", " + point2 + ", " + point3;
        },
        setSorted: function (chart, x, y) {
            if (x === void 0) { x = false; }
            if (y === void 0) { y = false; }
            if (x == true && chart.x.sorted == true) {
                chart.x.sorted = false;
            }
            else if (x == true && chart.x.sorted == false) {
                chart.x.sorted = true;
            }
            else if (y == true && chart.y.sorted == true) {
                chart.y.sorted = false;
            }
            else if (y == true && chart.y.sorted == false) {
                chart.y.sorted = true;
            }
        },
        arrowTransition: function (svg, chart, arrow, x, y) {
            if (x === void 0) { x = false; }
            if (y === void 0) { y = false; }
            svg.select(".sort-arrow.active")
                .attr("class", "sort-arrow");
            arrow.transition()
                .attr("points", this.arrowPoints(svg, chart, x, y))
                .attr("class", "sort-arrow active");
        },
        sortData: function (a, b, sorted) {
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
    },
    click: {
        enableClick: function (chart, onClick) {
            chart.renderElements.content.on("click", onClick);
        },
        removeClick: function (chart) {
            chart.renderElements.contentContainer.selectAll(".click-text").remove();
            chart.renderElements.contentContainer.selectAll(".click-line").remove();
            chart.renderElements.contentContainer.selectAll(".click-container").remove();
        },
        appendText: function (chart, d, title, values) {
            var container = chart.renderElements.contentContainer.append("g")
                .attr("class", "click-container");
            var box = container.append("rect")
                .attr("class", "click-box");
            var text = container.append("text")
                .attr("class", "click-text title")
                .attr("x", 10)
                .text(title);
            var textSize = text.node().getBBox().height;
            text.attr("y", textSize);
            values.forEach(function (c, i) {
                text.append("tspan")
                    .attr("class", "click-text")
                    .attr("y", textSize * (i + 2))
                    .attr("x", 15)
                    .text(c.label + ": " + c.value);
            });
            box.attr("width", text.node().getBBox().width + 20)
                .attr("height", text.node().getBBox().height + 5);
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
            container.attr("transform", "translate(" + positionX + ", " + positionY + ")");
        }
    },
    zoom: {
        enableZoom: function (chart, zoomed) {
            var zoom = d3.zoom()
                .scaleExtent([1, 5])
                .extent([[0, 0], [chart.width - chart.padding.yAxis, chart.height]])
                .translateExtent([[0, 0], [chart.width - chart.padding.yAxis, chart.height]])
                .on("zoom", zoomed);
            chart.renderElements.contentContainer.append("clipPath")
                .attr("id", "clip-" + chart.id)
                .append("rect")
                .attr("x", 1)
                .attr("width", chart.width - chart.padding.yAxis)
                .attr("height", chart.height - chart.padding.xAxis - chart.padding.top);
            chart.renderElements.xAxis.attr("clip-path", "url(#clip-" + chart.id + ")");
            chart.renderElements.contentContainer.selectAll(".bar")
                .attr("clip-path", "url(#clip-" + chart.id + ")");
            chart.renderElements.contentContainer.selectAll(".line")
                .attr("clip-path", "url(#clip-" + chart.id + ")");
            chart.renderElements.contentContainer.selectAll(".line-circle")
                .attr("clip-path", "url(#clip-" + chart.id + ")");
            chart.renderElements.contentContainer.selectAll(".contour")
                .attr("clip-path", "url(#clip-" + chart.id + ")");
            chart.renderElements.contentContainer.select(".zoom-rect").call(zoom);
        },
        appendZoomBar: function (chart) {
            return chart.renderElements.svg.append("g")
                .attr("class", "zoom-container")
                .attr("height", 30)
                .attr("width", chart.width - chart.padding.yAxis)
                .attr("transform", "translate(" + chart.padding.yAxis + ", " + (chart.height - 30) + ")");
        }
    },
    drag: {
        appendThresholdLine: function (chart, thresholds) {
            thresholds.forEach(function (c, i) {
                chart.renderElements.contentContainer.append("line")
                    .attr("class", "threshold-line " + (i == 0 ? "distressed" : "soaring"))
                    .attr("x1", 0)
                    .attr("x2", chart.width - chart.padding.yAxis - chart.padding.right)
                    .attr("y1", chart.y.scale(c))
                    .attr("y2", chart.y.scale(c));
            });
        },
        appendThresholdPercentages: function (chart, binContainer, bin, bandwithScale, tDistressed, tSoaring) {
            var binData = function (data) {
                var bins = bin(data.map(function (r) { return r.point; }));
                var result = [];
                bins.forEach(function (c) {
                    result.push({ bin: c, percentage: c.length / data.length * 100 });
                });
                return result;
            };
            var binTextContainer = binContainer.append("g")
                .attr("class", chart.id + "-violin-text-container");
            var binTextBox = binTextContainer.selectAll("rect")
                .data(function (d) { return binData(d.value); })
                .enter()
                .append("rect")
                .attr("class", "violin-text-box");
            var binText = binTextContainer.selectAll("text")
                .data(function (d) { return binData(d.value); })
                .enter()
                .append("text")
                .attr("class", "violin-text")
                .text(function (d) { return Math.round(d.percentage) + "%"; });
            binTextBox.attr("width", binText.node().getBBox().width + 10)
                .attr("height", binText.node().getBBox().height + 5);
            binTextBox.attr("y", function (d, i) { return chart.y.scale(i == 0 ? tDistressed / 2 : i == 1 ? 50 : (100 - tSoaring) / 2 + tSoaring) - binTextBox.node().getBBox().height / 2; })
                .attr("x", bandwithScale(0) - binTextBox.node().getBBox().width / 2);
            binText.attr("y", function (d, i) { return chart.y.scale(i == 0 ? tDistressed / 2 : i == 1 ? 50 : (100 - tSoaring) / 2 + tSoaring) - binTextBox.node().getBBox().height / 2 + binText.node().getBBox().height; })
                .attr("x", bandwithScale(0) - binText.node().getBBox().width / 2);
        }
    }
};
