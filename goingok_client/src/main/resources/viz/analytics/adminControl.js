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
import { ChartSeries, HistogramChartSeries, ChartTime, ChartTimeZoom, UserChart } from "../charts/charts.js";
import { Help } from "../charts/help.js";
import { HistogramData, UserChartData, AdminAnalyticsData, AdminAnalyticsDataStats } from "../data/data.js";
import { AdminControlInteractions, TooltipValues } from "../charts/interactions.js";
import { AdminAnalyticsDataRaw } from "../data/db.js";
import { Loading } from "../utils/loading.js";
import { Tutorial, TutorialData } from "../utils/tutorial.js";
export class AdminControlCharts {
    constructor() {
        this.help = new Help();
        this.interactions = new AdminControlInteractions();
    }
    sidebarBtn() {
        //Handle side bar btn click
        d3.select("#sidebar-btn").on("click", function () {
            let isActive = d3.select("#sidebar").attr("class").includes("active");
            d3.select("#sidebar")
                .classed("active", !isActive);
            d3.select("#groups")
                .classed("active", isActive);
            d3.select("#switch-dashboard")
                .classed("active", isActive);
            d3.select(this)
                .classed("active", isActive);
        });
    }
    ;
    preloadGroups(allEntries, enable = false) {
        d3.select("#groups")
            .selectAll("li")
            .data(allEntries)
            .enter()
            .append("li")
            .append("div")
            .attr("class", "input-group mb-1")
            .call(div => div.append("div")
            .attr("class", "input-group-prepend")
            .call(div => div.append("div")
            .attr("class", "input-group-text group-row")
            .html((d, i) => ` <input type="checkbox" value="${d.group}" checked ${!enable ? "disabled" : ""} />`)))
            .call(div => div.append("input")
            .attr("type", "text")
            .attr("class", "form-control group-row")
            .attr("value", d => d.group)
            .property("disabled", true))
            .call(div => div.append("div")
            .attr("class", "input-group-append")
            .call(div => div.append("div")
            .attr("class", "input-group-text group-row")
            .html(d => `<input type="color" value="${d.colour}" id="colour-${d.group}" ${!enable ? "disabled" : ""} />`)));
        return allEntries;
    }
    ;
    renderTotals(data) {
        let users = d3.select("#users-total .card-title span").datum();
        d3.select("#users-total .card-title span")
            .datum(d3.sum(data.map(d => d.getStat("usersTotal").value)))
            .transition()
            .duration(1000)
            .tween("html", function () {
            let oldUsers = users == undefined ? 0 : users;
            let newUsers = d3.sum(data.map(d => d.getStat("usersTotal").value));
            return function (t) {
                if (oldUsers < newUsers) {
                    this.innerHTML = (oldUsers + Math.round(t * (newUsers - oldUsers))).toString();
                }
                else {
                    this.innerHTML = (oldUsers - Math.round(t * (oldUsers - newUsers))).toString();
                }
            };
        });
        let refs = d3.select("#ref-total .card-title span").datum();
        d3.select("#ref-total .card-title span")
            .datum(d3.sum(data.map(d => d.getStat("refTotal").value)))
            .transition()
            .duration(1000)
            .tween("html", function () {
            let oldRefs = refs == undefined ? 0 : refs;
            let newRefs = d3.sum(data.map(d => d.getStat("refTotal").value));
            return function (t) {
                if (oldRefs < newRefs) {
                    this.innerHTML = (oldRefs + Math.round(t * (newRefs - oldRefs))).toString();
                }
                else {
                    this.innerHTML = (oldRefs - Math.round(t * (oldRefs - newRefs))).toString();
                }
            };
        });
        let ruRate = d3.select("#ru-rate .card-title span").datum();
        d3.select("#ru-rate .card-title span")
            .datum(data.length != 0 ? Math.round(d3.mean(data.map(d => d.getStat("ruRate").value * 100))) / 100 : 0)
            .transition()
            .duration(1000)
            .tween("html", function () {
            let oldRURate = ruRate == undefined ? 0 : ruRate;
            let newRURate = data.length != 0 ? Math.round(d3.mean(data.map(d => d.getStat("ruRate").value * 100))) / 100 : 0;
            return function (t) {
                if (oldRURate < newRURate) {
                    this.innerHTML = (oldRURate + (t * (newRURate - oldRURate))).toFixed(2);
                }
                else {
                    this.innerHTML = (oldRURate - (t * (oldRURate - newRURate))).toFixed(2);
                }
            };
        });
    }
    ;
    renderBarChart(chart, data) {
        d3.select(`#${chart.id} .card-title span`)
            .html();
        d3.select(`#${chart.id} .card-subtitle`)
            .html(data.length <= 1 ? "Add more group codes from the left bar" : "Click a group code to filter");
        //Boxes processing
        chart.elements.content = chart.elements.contentContainer.selectAll(`#${chart.id}-data`)
            .data(data)
            .join(enter => enter.append("rect")
            .attr("id", `${chart.id}-data`)
            .attr("class", "bar")
            .attr("y", d => chart.y.scale(0))
            .attr("x", d => chart.x.scale(d.group))
            .attr("width", chart.x.scale.bandwidth())
            .attr("height", 0)
            .style("stroke", d => d.colour)
            .style("fill", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("height", d => chart.y.scale(0) - chart.y.scale(d.getStat("usersTotal").value))
            .attr("y", d => chart.y.scale(d.getStat("usersTotal").value))), update => update.style("stroke", d => d.colour)
            .style("fill", d => d.colour)
            .call(update => update.transition()
            .duration(750)
            .attr("y", d => chart.y.scale(d.getStat("usersTotal").value))
            .attr("x", d => chart.x.scale(d.group))
            .attr("width", chart.x.scale.bandwidth())
            .attr("height", d => chart.y.scale(0) - chart.y.scale(d.getStat("usersTotal").value))), exit => exit.style("fill", "#cccccc")
            .style("stroke", "#b3b3b3")
            .call(exit => exit.transition()
            .duration(250)
            .attr("y", d => chart.y.scale(0))
            .attr("height", 0)
            .remove()));
        let _this = this;
        //Enable tooltip
        this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            //If box is clicked not append tooltip
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.interactions.tooltip.appendTooltipContainer(chart);
            //Append tooltip box with text
            let tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.group, d.stats.filter((c, i) => i < 2).map(c => new TooltipValues(c.displayName, c.value)));
            //Position tooltip container
            _this.interactions.tooltip.positionTooltipContainer(chart, xTooltip(d.group, tooltipBox), yTooltip(d.getStat("usersTotal").value, tooltipBox));
            function xTooltip(x, tooltipBox) {
                //Position tooltip right of the box
                let xTooltip = chart.x.scale(x) + chart.x.scale.bandwidth();
                //If tooltip does not fit position left of the box
                if (chart.width - chart.padding.yAxis < xTooltip + tooltipBox.node().getBBox().width) {
                    return xTooltip - chart.x.scale.bandwidth() - tooltipBox.node().getBBox().width;
                }
                return xTooltip;
            }
            function yTooltip(y, tooltipBox) {
                //Position tooltip on top of the box
                let yTooltip = chart.y.scale(y) + (tooltipBox.node().getBBox().height / 2);
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
    }
    renderHistogram(chart, data) {
        chart.setBandwidth(data);
        chart.setBin();
        d3.select(`#${chart.id} .card-subtitle`)
            .html(data.length == 1 ? `Filtering by <span class="badge badge-pill badge-info">${data[0].group} <i class="fas fa-window-close"></i></span>` :
            "");
        //Process histogram
        chart.elements.contentContainer.selectAll(`.${chart.id}-histogram-container`)
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", `${chart.id}-histogram-container`)
            .attr("transform", d => `translate(${chart.x.scale(d.group)}, 0)`)
            .call(enter => enter.selectAll(".histogram-rect")
            .data(d => chart.bin(d.value.map(d => d.point)).map(c => { return new HistogramData(d.value, d.group, d.colour, c, Math.round(c.length / d.value.length * 100)); }))
            .enter()
            .append("rect")
            .attr("id", `${chart.id}-data`)
            .attr("class", "histogram-rect")
            .attr("x", c => chart.bandwidth(-c.percentage))
            .attr("y", c => chart.y.scale(c.bin.x0))
            .attr("height", 0)
            .attr("width", c => chart.bandwidth(c.percentage) - chart.bandwidth(-c.percentage))
            .style("stroke", c => c.colour)
            .style("fill", c => c.colour)
            .transition()
            .duration(750)
            .attr("y", c => chart.y.scale(c.bin.x1))
            .attr("height", c => chart.y.scale(c.bin.x0) - chart.y.scale(c.bin.x1))), update => update
            .call(update => this.interactions.histogram(chart, update))
            .call(update => update.transition()
            .duration(750)
            .attr("transform", d => `translate(${chart.x.scale(d.group)}, 0)`)), exit => exit
            .call(exit => exit.selectAll(".histogram-rect")
            .style("fill", "#cccccc")
            .style("stroke", "#b3b3b3")
            .transition()
            .duration(250)
            .attr("y", c => chart.y.scale(c.bin.x0))
            .attr("height", 0))
            .call(exit => exit.transition()
            .duration(250)
            .remove()));
        chart.elements.content = chart.elements.contentContainer.selectAll(`#${chart.id}-data`);
        //Append tooltip container
        this.handleHistogramHover(chart);
        return chart;
    }
    ;
    handleHistogramHover(chart) {
        let _this = this;
        _this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            _this.interactions.tooltip.appendTooltipContainer(chart);
            let tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.bin.x0 == 0 ? "Distressed" : d.bin.x1 == 100 ? "Soaring" : "GoingOK", [new TooltipValues("Total", `${d.bin.length} (${d.percentage}%)`)]);
            _this.interactions.tooltip.positionTooltipContainer(chart, chart.x.scale(d.group) + chart.bandwidth(d.bin.length), d.bin.x1 > 25 ? chart.y.scale(d.bin.x1) : chart.y.scale(d.bin.x0) - tooltipBox.node().getBBox().height);
        }
        function onMouseout() {
            chart.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            _this.interactions.tooltip.removeTooltip(chart);
        }
    }
    renderTimelineDensity(chart, data) {
        let _this = this;
        if (data.length == 0) {
            d3.select(`#${chart.id} .card-subtitle`)
                .html("");
            return chart;
        }
        d3.select(`#${chart.id} .card-subtitle`)
            .classed("instructions", data.length <= 1)
            .classed("text-muted", data.length != 1)
            .html(data.length != 1 ? `The oldest reflection was on ${d3.min(data.map(d => d3.min(d.value.map(d => d.timestamp)))).toDateString()} in the group code ${data[d3.minIndex(data.map(d => d3.min(d.value.map(d => d.timestamp))))].group}, while
                the newest reflection was on ${d3.max(data.map(d => d3.max(d.value.map(d => d.timestamp)))).toDateString()} in the group code ${data[d3.maxIndex(data.map(d => d3.max(d.value.map(d => d.timestamp))))].group}` :
            `Filtering by <span class="badge badge-pill badge-info">${data[0].group} <i class="fas fa-window-close"></i></span>`);
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
                .join(enter => enter.append("g")
                .attr("class", "timeline-container")
                .attr("stroke", d => d.colour)
                .attr("fill", d => d.colour)
                .call(enter => _this.interactions.timelineDensity(enter, getDensityData)), update => update.attr("stroke", d => d.colour)
                .attr("fill", d => d.colour)
                .call(update => _this.interactions.timelineDensity(update, getDensityData)), exit => exit.remove());
            function getDensityData(data) {
                return d3.contourDensity()
                    .x(d => chart.x.scale(d.timestamp))
                    .y(d => chart.y.scale(d.point))
                    .bandwidth(5)
                    .thresholds(20)
                    .size([chart.width - chart.padding.yAxis, chart.height - chart.padding.xAxis - chart.padding.top])(data.value);
            }
        }
        //Enable zoom
        this.interactions.zoom.enableZoom(chart, zoomed);
        function zoomed(e) {
            let newChartRange = [0, chart.width - chart.padding.yAxis].map(d => e.transform.applyX(d));
            chart.x.scale.rangeRound(newChartRange);
            drawContours();
            chart.x.axis.ticks(newChartRange[1] / 75);
            chart.elements.xAxis.call(chart.x.axis);
            _this.help.removeHelp(chart);
        }
        return chart;
    }
    ;
    renderTimelineScatter(chart, zoomChart, data) {
        //Remove density plot
        chart.elements.contentContainer.selectAll(".contour").remove();
        if (data.length == 0) {
            d3.select(`#${chart.id} .card-subtitle`)
                .html("");
            return chart;
        }
        let _this = this;
        d3.select(`#${chart.id} .card-subtitle`)
            .classed("instructions", data.length <= 1)
            .classed("text-muted", data.length != 1)
            .html(data.length != 1 ? `The oldest reflection was on ${d3.min(data.map(d => d3.min(d.value.map(d => d.timestamp)))).toDateString()} in the group code ${data[d3.minIndex(data.map(d => d3.min(d.value.map(d => d.timestamp))))].group}, while
                the newest reflection was on ${d3.max(data.map(d => d3.max(d.value.map(d => d.timestamp)))).toDateString()} in the group code ${data[d3.maxIndex(data.map(d => d3.max(d.value.map(d => d.timestamp))))].group}` :
            `Filtering by <span class="badge badge-pill badge-info">${data[0].group} <i class="fas fa-window-close"></i></span>`);
        //Draw circles
        chart.elements.contentContainer.selectAll(".timeline-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "timeline-container")
            .call(enter => _this.interactions.timelineScatter(enter, chart)), update => update.call(update => _this.interactions.timelineScatter(update, chart)), exit => exit.remove());
        chart.elements.content = chart.elements.contentContainer.selectAll(".circle");
        //Enable tooltip       
        _this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.interactions.tooltip.appendTooltipContainer(chart);
            let tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.timestamp.toDateString(), [new TooltipValues("User", d.pseudonym),
                new TooltipValues("Point", d.point)]);
            _this.interactions.tooltip.positionTooltipContainer(chart, xTooltip(d.timestamp, tooltipBox), yTooltip(d.point, tooltipBox));
            function xTooltip(x, tooltipBox) {
                let xTooltip = chart.x.scale(x);
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
            .join(enter => enter.append("g")
            .attr("class", "zoom-timeline-content-container")
            .call(enter => _this.interactions.timelineScatter(enter, zoomChart, true, true)), update => update.call(update => _this.interactions.timelineScatter(update, zoomChart, true, true)), exit => exit.remove());
        chart.elements.zoomSVG.selectAll(".zoom-timeline-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "zoom-timeline-container")
            .call(enter => { zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis]); _this.interactions.timelineScatter(enter, zoomChart, true); }), update => update.call(update => { zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis]); _this.interactions.timelineScatter(update, zoomChart, true); }), exit => exit.remove());
        //Enable zoom
        _this.interactions.zoom.enableZoom(chart, zoomed);
        function zoomed(e) {
            let newChartRange = [0, chart.width - chart.padding.yAxis].map(d => e.transform.applyX(d));
            chart.x.scale.rangeRound(newChartRange);
            zoomChart.x.scale.rangeRound([0, chart.width - chart.padding.yAxis - 5].map(d => e.transform.invertX(d)));
            let newLine = d3.line()
                .x(d => chart.x.scale(d.timestamp))
                .y(d => chart.y.scale(d.point));
            chart.elements.contentContainer.selectAll(".circle")
                .attr("cx", d => chart.x.scale(d.timestamp));
            chart.elements.zoomFocus.selectAll(".zoom-content")
                .attr("cx", d => zoomChart.x.scale(d.timestamp));
            chart.elements.contentContainer.selectAll(".click-line")
                .attr("d", d => newLine(d));
            chart.elements.contentContainer.selectAll(".click-container")
                .attr("transform", d => `translate(${chart.x.scale(d.timestamp)}, ${chart.y.scale(d.point)})`);
            chart.x.axis.ticks(newChartRange[1] / 75);
            chart.elements.xAxis.call(chart.x.axis);
            _this.help.removeHelp(chart);
        }
        return chart;
    }
    ;
    handleTimelineButtons(chart, zoomChart, data, func) {
        let _this = this;
        d3.select(`#${chart.id} #timeline-plot`).on("click", func != undefined ? (e) => func(e) : (e) => {
            var selectedOption = e.target.control.value;
            if (selectedOption == "density") {
                _this.renderTimelineDensity(chart, data);
            }
            if (selectedOption == "scatter") {
                _this.renderTimelineScatter(chart, zoomChart, data);
            }
            if (!d3.select(`#${chart.id}-help`).empty()) {
                _this.help.removeHelp(chart);
            }
        });
    }
    ;
    renderUserStatistics(card, data, thresholds, timelineData) {
        let _this = this;
        let usersData = data.getUsersData();
        d3.select("#reflections .card-subtitle")
            .classed("text-muted", true)
            .classed("instructions", false)
            .html(timelineData == undefined ? `The user ${usersData.value[d3.minIndex(usersData.value.map(d => d.point))].pseudonym} is the most distressed, while
                the user ${usersData.value[d3.maxIndex(usersData.value.map(d => d.point))].pseudonym} is the most soaring` :
            `The user ${timelineData.pseudonym} has a total of ${data.value.filter(d => d.pseudonym == timelineData.pseudonym).length} reflections between
                ${d3.min(data.value.filter(d => d.pseudonym == timelineData.pseudonym).map(d => d.timestamp)).toDateString()} and
                ${d3.max(data.value.filter(d => d.pseudonym == timelineData.pseudonym).map(d => d.timestamp)).toDateString()}`);
        card.selectAll("div")
            .data(timelineData == undefined ? usersData.value : usersData.value.filter(d => d.pseudonym == timelineData.pseudonym))
            .enter()
            .append("div")
            .attr("class", "row statistics-text")
            .attr("id", d => d.pseudonym)
            .call(div => div.append("div")
            .attr("class", "col-md-4")
            .call(div => div.append("h5")
            .attr("class", "mb-0 mt-1")
            .html(d => `${d.pseudonym} is`))
            .call(div => div.append("span")
            .attr("class", d => `bin-name ${_this.getUserStatisticBinName(d, thresholds).toLowerCase()}`)
            .html(d => `<b>${_this.getUserStatisticBinName(d, thresholds)}</b>`))
            .call(div => div.append("div")
            .attr("class", "mt-2")
            .append("h6")
            .html("Percentage of reflections"))
            .call(div => div.append("div")
            .attr("class", "w-100 mt-1 user-chart")))
            .call(div => div.append("div")
            .attr("class", "col-md-8")
            .append("p")
            .attr("class", "mb-1")
            .html(d => `User ${d.pseudonym} reflections in chronological order:`)
            .call(div => div.append("ul")
            .attr("class", "pr-3")
            .selectAll("li")
            .data(d => d3.sort(d3.filter(data.value, x => x.pseudonym == d.pseudonym), r => r.timestamp))
            .enter()
            .append("li")
            .classed("reflection-selected", d => timelineData != undefined ? d.timestamp == timelineData.timestamp : false)
            .html(d => `<i>${d.timestamp.toDateString()} | Reflection point ${d.point}</i><br> ${d.text}`)))
            .each((d, i, g) => drawUserChart(d3.select(d3.select(g[i]).node().parentElement).attr("id") + " #" + d3.select(g[i]).attr("id"), d.pseudonym, thresholds));
        function drawUserChart(id, pseudonym, thresholds) {
            let chart = new UserChart(id, "user-chart");
            let bin = d3.bin().domain([0, 100]).thresholds(thresholds);
            let userData = data.value.filter(d => d.pseudonym == pseudonym);
            let userChartData = bin(usersData.value.map(d => d.point)).map(c => { return new UserChartData(c, usersData.value, Math.round(c.length / usersData.value.length * 100), true); });
            userChartData.push(...bin(userData.map(d => d.point)).map(c => { return new UserChartData(c, userData, Math.round(c.length / userData.length * 100), false); }));
            chart.elements.svg.classed("chart-svg", false);
            chart.elements.svg.select(".x-axis").attr("clip-path", null);
            chart.elements.contentContainer.selectAll("circle")
                .data(userChartData)
                .enter()
                .append("circle")
                .attr("class", d => d.isGroup ? "circle-group" : "circle-user")
                .attr("r", 5)
                .attr("cx", d => chart.x.scale(d.percentage))
                .attr("cy", d => chart.y.scale(d.binName) + chart.y.scale.bandwidth() / 2)
                .attr("fill", usersData.colour)
                .attr("stroke", usersData.colour);
            chart.elements.contentContainer.selectAll("line")
                .data(d3.group(userChartData, d => d.binName))
                .enter()
                .append("line")
                .attr("class", "line-user")
                .attr("x1", d => chart.x.scale(d3.min(d[1].map(c => c.percentage))))
                .attr("x2", d => chart.x.scale(d3.max(d[1].map(c => c.percentage))))
                .attr("y1", d => chart.y.scale(d[0]) + chart.y.scale.bandwidth() / 2)
                .attr("y2", d => chart.y.scale(d[0]) + chart.y.scale.bandwidth() / 2)
                .attr("stroke", usersData.colour);
            chart.elements.svg.append("g")
                .attr("class", "user-legend-container")
                .attr("transform", `translate(${(chart.width - chart.padding.xAxis - chart.padding.right) / 2}, ${chart.height - 15})`)
                .selectAll("g")
                .data([usersData.group, pseudonym])
                .enter()
                .append("g")
                .attr("class", "user-legend")
                .call(g => g.append("rect")
                .attr("class", (d, i) => i == 0 ? "circle-group" : "circle-user")
                .attr("x", 0)
                .attr("y", 0)
                .attr("height", 10)
                .attr("width", 10)
                .attr("fill", usersData.colour)
                .attr("stroke", usersData.colour))
                .call(g => g.append("text")
                .attr("class", "user-legend-text")
                .attr("x", 15)
                .attr("y", 5)
                .text(d => d));
            chart.elements.svg.selectAll(".user-legend")
                .attr("transform", (d, i, g) => `translate(${i == 0 ? 0 : d3.select(g[i - 1]).node().getBoundingClientRect().width + 20}, 0)`);
        }
    }
    ;
    getUserStatisticBinName(data, thresholds) {
        let distressed = thresholds[0];
        let soaring = thresholds[1];
        if (data.point <= distressed) {
            return "Distressed";
        }
        else if (data.point >= soaring) {
            return "Soaring";
        }
        else {
            return "GoingOK";
        }
    }
}
export function buildControlAdminAnalyticsCharts(entriesRaw) {
    return __awaiter(this, void 0, void 0, function* () {
        let loading = new Loading();
        let rawData = entriesRaw.map(d => new AdminAnalyticsDataRaw(d.group, d.value, d.createDate));
        let entries = rawData.map(d => d.transformData());
        let colourScale = d3.scaleOrdinal(d3.schemeCategory10);
        entries = entries.map(d => new AdminAnalyticsData(d.group, d.value, d.creteDate, colourScale(d.group), d.selected));
        yield drawCharts(entries);
        new Tutorial([new TutorialData("#groups", "All your groups are selected to visualise and colours assigned. You cannot change this section"),
            new TutorialData(".card-title button", "Click the help symbol in any chart to get additional information"),
            new TutorialData("#users .bar", "Hover for information on demand"),
            new TutorialData("#histogram .histogram-rect", "Hover for information on demand"),
            new TutorialData("#timeline-plot", "Swap chart types. Both charts have zoom available")]);
        loading.isLoading = false;
        loading.removeDiv();
        function drawCharts(allEntries) {
            return __awaiter(this, void 0, void 0, function* () {
                let adminControlCharts = new AdminControlCharts();
                //Handle sidebar button
                adminControlCharts.sidebarBtn();
                adminControlCharts.preloadGroups(allEntries);
                //Create data with current entries
                let data = allEntries.map(d => new AdminAnalyticsDataStats(d));
                //Render totals
                adminControlCharts.renderTotals(data);
                //Create groups chart with current data
                let usersChart = new ChartSeries("users", data.map(d => d.group), false, data.map(d => d.getStat("usersTotal").value));
                adminControlCharts.renderBarChart(usersChart, data);
                d3.select("#users .card-subtitle")
                    .html("");
                //Handle groups chart help
                d3.select("#users .card-title button")
                    .on("click", function (e) {
                    adminControlCharts.help.helpPopover(d3.select(this), `${usersChart.id}-help`, "<b>Bar chart</b><br>A bar chart of the users in each group code");
                    adminControlCharts.help.helpPopover(usersChart.elements.contentContainer.select(".bar"), `${usersChart.id}-help-data`, "<u><i>hover</i></u> me for information on demand");
                });
                //Draw users histogram container
                let usersData = data.map(d => d.getUsersData());
                let histogram = new HistogramChartSeries("histogram", data.map(d => d.group));
                adminControlCharts.renderHistogram(histogram, usersData);
                //Handle users histogram chart help
                d3.select("#histogram .card-title button")
                    .on("click", function (e) {
                    adminControlCharts.help.helpPopover(d3.select(this), `${histogram.id}-help`, "<b>Histogram</b><br>A histogram group data points into user-specific ranges. The data points in this histogram are <i>users average reflection point</i>");
                    adminControlCharts.help.helpPopover(histogram.elements.contentContainer.select(`#${histogram.id}-data`), `${histogram.id}-help-data`, "<u><i>hover</i></u> me for information on demand");
                });
                //Draw timeline 
                let timelineChart = new ChartTime("timeline", [d3.min(data.map(d => d.getStat("oldRef").value)), d3.max(data.map(d => d.getStat("newRef").value))]);
                let timelineZoomChart = new ChartTimeZoom(timelineChart, [d3.min(data.map(d => d.getStat("oldRef").value)), d3.max(data.map(d => d.getStat("newRef").value))]);
                adminControlCharts.renderTimelineScatter(timelineChart, timelineZoomChart, data);
                adminControlCharts.handleTimelineButtons(timelineChart, timelineZoomChart, data);
                //Handle timeline chart help
                d3.select("#timeline .card-title button")
                    .on("click", function (e) {
                    adminControlCharts.help.helpPopover(d3.select(this), `${timelineChart.id}-help`, "<b>Density plot</b><br>A density plot shows the distribution of a numeric variable<br><b>Scatter plot</b><br>The data is showed as a collection of points<br>The data represented are <i>reflections over time</i>");
                    adminControlCharts.help.helpPopover(d3.select("#timeline #timeline-plot"), `${timelineChart.id}-help-button`, "<u><i>click</i></u> me to change chart type");
                    adminControlCharts.help.helpPopover(d3.select("#timeline .zoom-rect.active"), `${timelineChart.id}-help-zoom`, "use the mouse <u><i>wheel</i></u> to zoom me<br><u><i>click and hold</i></u> while zoomed to move");
                    if (!timelineChart.elements.contentContainer.select(".circle").empty()) {
                        let showDataHelp = adminControlCharts.help.helpPopover(timelineChart.elements.contentContainer.select(".circle"), `${timelineChart.id}-help-data`, "<u><i>hover</i></u> me for information on demand");
                        if (showDataHelp) {
                            d3.select(`#${timelineChart.id}-help-data`).style("top", parseInt(d3.select(`#${timelineChart.id}-help-data`).style("top")) - 14 + "px");
                        }
                    }
                });
                //Draw user statistics
                let userStatistics = d3.select("#reflections .card-body");
                userStatistics.append("ul")
                    .attr("class", "nav nav-tabs")
                    .selectAll("li")
                    .data(data)
                    .enter()
                    .append("li")
                    .attr("class", "nav-item")
                    .append("a")
                    .attr("class", (d, i) => `nav-link ${i == 0 ? "active" : ""}`)
                    .attr("href", d => `#reflections-${d.group}`)
                    .attr("data-toggle", "tab")
                    .html(d => d.group)
                    .on("click", (e, d) => setTimeout(() => adminControlCharts.renderUserStatistics(d3.select(`#reflections-${d.group}`), d, [30, 70]), 250));
                let users = userStatistics.append("div")
                    .attr("class", "tab-content")
                    .selectAll("div")
                    .data(data)
                    .enter()
                    .append("div")
                    .attr("class", (d, i) => `tab-pane fade ${i == 0 ? "show active" : ""} users-tab-pane`)
                    .attr("id", d => `reflections-${d.group}`);
                users.each((d, i, g) => i == 0 ? adminControlCharts.renderUserStatistics(d3.select(g[i]), d, [30, 70]) : "");
                //Handle users histogram chart help
                d3.select("#reflections .card-title button")
                    .on("click", function (e) {
                    adminControlCharts.help.helpPopover(d3.select(this), "reflections-help", "<b>Reflections</b><br>Each user's reflections are shown sorted by time. The chart depicts the percentage of reflections in each reflection point group");
                });
            });
        }
    });
}
