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
import { ChartNetwork, ChartTimeNetwork } from "../charts/charts.js";
import { Help } from "../charts/help.js";
import { AuthorControlInteractions, TooltipValues } from "../charts/interactions.js";
import { ChartPadding } from "../charts/render.js";
import { Loading } from "../utils/loading.js";
import { Tutorial, TutorialData } from "../utils/tutorial.js";
export class AuthorControlCharts {
    constructor() {
        this.help = new Help();
        this.interactions = new AuthorControlInteractions();
        this.allNodes = [];
    }
    resizeTimeline() {
        let height = document.querySelector("#reflection-entry").getBoundingClientRect().height;
        document.querySelector("#timeline .chart-container").setAttribute("style", `min-height:${height - 80}px`);
    }
    preloadTags(analytics, enable = false) {
        analytics.forEach(c => {
            this.allNodes = this.allNodes.concat(c.nodes);
        });
        let groupTags = Array.from(d3.rollup(this.allNodes, d => d.map(r => r.properties)[0], d => d.name), ([name, properties]) => ({ name, properties }));
        let uniqueTags = groupTags.map(d => { return { "name": d.name, "properties": d.properties, "selected": true }; });
        d3.select("#tags").selectAll("li")
            .data(uniqueTags)
            .enter()
            .append("li")
            .attr("class", "mx-3")
            .append("div")
            .attr("class", "input-group")
            .call(div => div.append("input")
            .attr("type", "text")
            .attr("class", "form-control tag-row")
            .attr("value", d => d.name)
            .property("disabled", true))
            .call(div => div.append("div")
            .attr("class", "input-group-append")
            .append("div")
            .attr("class", "input-group-text tag-row")
            .append("input")
            .attr("id", d => `colour-${d.name}`)
            .attr("type", "color")
            .attr("value", d => d.properties["color"])
            .property("disabled", !enable));
        return uniqueTags;
    }
    processSimulation(chart, data) {
        return d3.forceSimulation(data.nodes)
            .force("link", d3.forceLink()
            .id(d => d.idx)
            .distance(100)
            .links(data.edges))
            .force("charge", d3.forceManyBody().strength(-25))
            .force("collide", d3.forceCollide().radius(30).iterations(5))
            .force("center", d3.forceCenter((chart.width - chart.padding.yAxis - chart.padding.right - 10) / 2, (chart.height - chart.padding.top - chart.padding.xAxis + 5) / 2));
    }
    processTimelineSimulation(chart, centerX, centerY, nodes) {
        let simulation = d3.forceSimulation(nodes)
            .force("collide", d3.forceCollide().radius(5))
            .force("forceRadial", d3.forceRadial(0, 0).radius(15));
        if (centerY < 20) {
            simulation.force("forceY", d3.forceY(20).strength(0.25));
        }
        if (chart.height - chart.padding.top - chart.padding.xAxis - 20 < centerY) {
            simulation.force("forceY", d3.forceY(-20).strength(0.25));
        }
        if (centerX < 20) {
            simulation.force("forceX", d3.forceX(20).strength(0.25));
        }
        if (chart.width - chart.padding.yAxis - chart.padding.right - 20 < centerX) {
            simulation.force("forceX", d3.forceX(-20).strength(0.25));
        }
        return simulation.tick(300);
    }
    renderTimeline(chart, data, analytics) {
        const _this = this;
        const hardLine = d3.line()
            .x(d => chart.x.scale(d.timestamp))
            .y(d => chart.y.scale(d.point))
            .curve(d3.curveMonotoneX);
        if (chart.elements.contentContainer.select(".hardline").empty()) {
            chart.elements.contentContainer.append("path")
                .datum(d3.sort(data, d => d.timestamp))
                .attr("class", "hardline")
                .attr("d", d => hardLine(d));
        }
        chart.elements.contentContainer.selectAll(".circle-tag-container")
            .data(data)
            .join(enter => enter.append("g")
            .attr("class", "circle-tag-container")
            .call(enter => enter.append("circle")
            .attr("class", "circle")
            .attr("r", 5)
            .style("fill", "#999999")
            .style("stroke", "#999999"))
            .call(enter => renderTimelineNetwork(enter, analytics))
            .call(enter => enter.transition()
            .duration(750)
            .attr("transform", d => `translate (${chart.x.scale(d.timestamp)}, ${chart.y.scale(d.point)})`)), update => update.call(update => update.transition()
            .duration(750)
            .attr("cx", d => chart.x.scale(d.timestamp))
            .attr("cy", d => chart.y.scale(d.point))
            .style("fill", "#999999")
            .style("stroke", "#999999"))
            .call(update => renderTimelineNetwork(update, analytics)), exit => exit.remove());
        function renderTimelineNetwork(enter, analytics) {
            enter.selectAll(".circle-tag")
                .data(d => analytics.nodes.filter(c => c.refId == d.refId))
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
        chart.elements.content = chart.elements.contentContainer.selectAll(".circle");
        //Enable tooltip       
        _this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            _this.interactions.tooltip.appendTooltipContainer(chart);
            let tooltipValues = [new TooltipValues("Point", d.point)];
            let tags = Array.from(d3.rollup(analytics.nodes.filter(c => c.refId == d.refId), d => d.length, d => d.name), ([name, total]) => ({ name, total }));
            tags.forEach(c => {
                tooltipValues.push(new TooltipValues(c.name, c.total));
            });
            let tooltipBox = _this.interactions.tooltip.appendTooltipText(chart, d.timestamp.toDateString(), tooltipValues);
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
            _this.interactions.tooltip.appendLine(chart, 0, chart.y.scale(d.point), chart.x.scale(d.timestamp), chart.y.scale(d.point), "#999999");
            _this.interactions.tooltip.appendLine(chart, chart.x.scale(d.timestamp), chart.y.scale(0), chart.x.scale(d.timestamp), chart.y.scale(d.point), "#999999");
        }
        function onMouseout() {
            chart.elements.svg.select(".tooltip-container").transition()
                .style("opacity", 0);
            _this.interactions.tooltip.removeTooltip(chart);
        }
        return chart;
    }
    renderNetwork(chart, data) {
        const _this = this;
        d3.select(`#${chart.id} .card-subtitle`)
            .html(data.nodes.filter(d => d.name === "ref").length == 1 ? `Filtering by <span class="badge badge-pill badge-info">${chart.x.scale.invert(data.nodes.find(d => d.name === "ref").fx).toDateString()} <i class="fas fa-window-close"></i></span>` :
            "");
        let links = chart.elements.contentContainer.selectAll(".network-link")
            .data(data.edges)
            .join(enter => enter.append("line")
            .attr("class", "network-link")
            .classed("reflection-link", d => d.isReflection)
            .attr("x1", chart.width / 2)
            .attr("y1", chart.height / 2)
            .attr("x2", chart.width / 2)
            .attr("y2", chart.height / 2)
            .call(enter => enter.transition()
            .duration(750)
            .attr("x1", d => d.source.x)
            .attr("y1", d => d.source.y)
            .attr("x2", d => d.target.x)
            .attr("y2", d => d.target.y)), update => update.call(update => update.classed("reflection-link", d => d.isReflection)
            .transition()
            .duration(750)
            .attr("x1", d => d.source.x)
            .attr("y1", d => d.source.y)
            .attr("x2", d => d.target.x)
            .attr("y2", d => d.target.y)), exit => exit.remove());
        let nodes = chart.elements.contentContainer.selectAll(".network-node-group")
            .data(data.nodes)
            .join(enter => enter.append("g")
            .attr("class", "network-node-group")
            .attr("transform", `translate(${chart.width / 2}, ${chart.height / 2})`)
            .call(enter => enter.append("rect")
            .attr("class", "network-node")
            .style("fill", d => d.properties["color"])
            .style("stroke", d => d.properties["color"]))
            .call(enter => enter.append("text")
            .attr("id", d => `text-${d.idx}`)
            .attr("class", "network-text"))
            .call(enter => enter.select("rect")
            .attr("x", -5)
            .attr("y", -5)
            .attr("width", 10)
            .attr("height", 10))
            .call(enter => enter.transition()
            .duration(750)
            .attr("transform", d => `translate(${d.x}, ${d.y})`)), update => update.call(update => update.transition()
            .duration(750)
            .attr("transform", d => `translate(${d.x}, ${d.y})`))
            .call(update => update.select("rect")
            .style("fill", d => d.properties["color"])
            .style("stroke", d => d.properties["color"]))
            .call(update => update.select("text")
            .attr("id", d => `text-${d.idx}`)), exit => exit.remove());
        chart.elements.content = chart.elements.contentContainer.selectAll(".network-node-group");
        chart.simulation.on("tick", ticked);
        function ticked() {
            links.attr("x1", d => d.source.x)
                .attr("y1", d => d.source.y)
                .attr("x2", d => d.target.x)
                .attr("y2", d => d.target.y);
            nodes.attr("transform", (d) => `translate(${d.x}, ${d.y})`);
        }
        //Enable tooltip       
        _this.interactions.tooltip.enableTooltip(chart, onMouseover, onMouseout);
        function onMouseover(e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                return;
            }
            let edges = data.edges.filter(d => d.source === d3.select(this).datum()).map(d => d.target);
            edges = edges.concat(data.edges.filter(d => d.target === d3.select(this).datum()).map(d => d.source));
            edges.push(d3.select(this).datum());
            d3.selectAll(".network-node-group")
                .filter(d => edges.includes(d))
                .call(enter => enter.select("text")
                .text(d => d.expression)
                .style("opacity", 0)
                .transition()
                .duration(500)
                .style("opacity", "1"))
                .call(enter => enter.select(".network-node")
                .transition()
                .duration(500)
                .attr("x", d => -(enter.select(`#text-${d.idx}`).node().getBoundingClientRect().width + 10) / 2)
                .attr("y", d => -(enter.select(`#text-${d.idx}`).node().getBoundingClientRect().height + 5) / 2)
                .attr("width", d => enter.select(`#text-${d.idx}`).node().getBoundingClientRect().width + 10)
                .attr("height", d => enter.select(`#text-${d.idx}`).node().getBoundingClientRect().height + 5));
        }
        function onMouseout() {
            let links = data.edges.filter(d => d.source === d3.select(this).datum()).map(d => d.target);
            links = links.concat(data.edges.filter(d => d.target === d3.select(this).datum()).map(d => d.source));
            links.push(d3.select(this).datum());
            d3.selectAll(".network-node-group")
                .filter(d => links.includes(d))
                .call(enter => enter.select("text")
                .text(null)
                .style("opacity", 0)
                .transition()
                .duration(500)
                .style("opacity", "1"))
                .call(enter => enter.select(".network-node")
                .transition()
                .duration(500)
                .attr("x", -5)
                .attr("y", -5)
                .attr("width", 10)
                .attr("height", 10));
            _this.interactions.tooltip.removeTooltip(chart);
        }
        //Enable zoom
        _this.interactions.zoom.enableZoom(chart, zoomed);
        function zoomed(e) {
            let newChartRange = [0, chart.width - chart.padding.yAxis - chart.padding.right].map(d => e.transform.applyX(d));
            chart.x.scale.rangeRound(newChartRange);
            chart.elements.contentContainer.selectAll(".network-link")
                .attr("x1", d => e.transform.applyX(d.source.x))
                .attr("x2", d => e.transform.applyX(d.target.x));
            chart.elements.contentContainer.selectAll(".network-node-group")
                .attr("transform", (d, i, g) => `translate(${e.transform.applyX(d.x)}, ${d.y})`);
            chart.x.axis.ticks(newChartRange[1] / 75);
            chart.elements.xAxis.call(chart.x.axis);
            _this.help.removeHelp(chart);
        }
        return chart;
    }
    renderReflections(data) {
        const _this = this;
        d3.select("#reflections .card-subtitle")
            .html(data.length == 1 ? `Filtering by <span class="badge badge-pill badge-info">${data[0].timestamp.toDateString()} <i class="fas fa-window-close"></i></span>` :
            "");
        d3.select("#reflections .reflections-tab")
            .selectAll(".reflection")
            .data(data)
            .join(enter => enter.append("div")
            .attr("class", "reflection")
            .call(div => div.append("p")
            .attr("class", "reflection-text")
            .html(d => _this.processReflectionsText(d))), update => update.select("p")
            .html(d => _this.processReflectionsText(d)), exit => exit.remove());
    }
    processReflectionsText(data) {
        let html = `<i>${data.timestamp.toDateString()} | Point: ${data.point}</i><br>`;
        let nodes = this.allNodes.filter(d => d.refId == data.refId && d.selected);
        for (var i = 0; i < data.text.length; i++) {
            const isOpenTag = nodes.find(c => c.startIdx === i);
            const isCloseTag = nodes.find(c => c.endIdx === i);
            if (isOpenTag !== undefined) {
                html += `<span class="badge badge-pill" style="background-color: ${isOpenTag.properties["color"]}">${data.text[i]}`;
            }
            else if (isCloseTag !== undefined) {
                html += `${data.text[i]}</span>`;
            }
            else {
                html += data.text[i];
            }
        }
        return html;
    }
}
export function buildControlAuthorAnalyticsCharts(entriesRaw, analyticsRaw) {
    return __awaiter(this, void 0, void 0, function* () {
        let loading = new Loading();
        const colourScale = d3.scaleOrdinal(d3.schemeCategory10);
        const entries = d3.sort(entriesRaw.map(d => { return { "refId": d.refId, "timestamp": new Date(d.timestamp), "point": d.point, "text": d.text }; }), d => d.timestamp);
        const analytics = analyticsRaw.map(d => { return { "name": d.name, "description": d.description, "nodes": d.nodes.map(c => processColour(c)), "edges": d.edges }; });
        yield drawCharts(entries, analytics);
        new Tutorial([new TutorialData("#timeline .card-title button", "Click the help symbol in any chart to get additional information"),
            new TutorialData("#timeline .circle", "Hover for information on demand"),
            new TutorialData("#network .network-node-group", "Hover for information on demand, zoom is also available")]);
        loading.isLoading = false;
        loading.removeDiv();
        function processColour(node) {
            if (node.properties["color"] === undefined) {
                node.properties = { "color": colourScale(node.name) };
            }
            return node;
        }
        function drawCharts(entries, analytics) {
            return __awaiter(this, void 0, void 0, function* () {
                const authorControlCharts = new AuthorControlCharts();
                authorControlCharts.resizeTimeline();
                authorControlCharts.preloadTags(analytics);
                if (analytics.find(d => d.name == "Your Network") === undefined) {
                    d3.select("#network .chart-container.network").html("Chart not found");
                }
                else {
                    const networkChart = new ChartNetwork("network", "chart-container.network", entries.map(d => d.timestamp));
                    networkChart.simulation = authorControlCharts.processSimulation(networkChart, analytics.find(d => d.name == "Your Network"));
                    authorControlCharts.renderNetwork(networkChart, analytics.find(d => d.name == "Your Network"));
                    //Handle timeline chart help
                    d3.select("#network .card-title button")
                        .on("click", function (e) {
                        authorControlCharts.help.helpPopover(d3.select("#network .zoom-rect.active"), `${networkChart.id}-help-zoom`, "use the mouse <u><i>wheel</i></u> to zoom me<br><u><i>click and hold</i></u> while zoomed to move");
                        authorControlCharts.help.helpPopover(d3.select(this), `${networkChart.id}-help`, "<b>Network diagram</b><br>A network diagram that shows the phrases and tags associated to your reflections<br>The data represented are your <i>reflections over time</i>");
                        let showDataHelp = authorControlCharts.help.helpPopover(networkChart.elements.contentContainer.select(".network-node-group"), `${networkChart.id}-help-data`, "<u><i>hover</i></u> me for information on demand");
                        if (showDataHelp) {
                            d3.select(`#${networkChart.id}-help-data`).style("top", parseInt(d3.select(`#${networkChart.id}-help-data`).style("top")) - 14 + "px");
                        }
                    });
                }
                if (analytics.find(d => d.name == "Your Timeline") === undefined) {
                    d3.select("#timeline .chart-container").html("Chart not found");
                }
                else {
                    let timelineChart = new ChartTimeNetwork("timeline", entries.map(d => d.timestamp), new ChartPadding(40, 75, 10, 10));
                    entries.forEach(c => authorControlCharts.processTimelineSimulation(timelineChart, timelineChart.x.scale(c.timestamp), timelineChart.y.scale(c.point), authorControlCharts.allNodes.filter(d => d.refId == c.refId)));
                    authorControlCharts.renderTimeline(timelineChart, entries, analytics.find(d => d.name == "Your Timeline"));
                    //Handle timeline chart help
                    d3.select("#timeline .card-title button")
                        .on("click", function (e) {
                        authorControlCharts.help.helpPopover(d3.select(this), "reflections-help", "<b>Timeline</b><br>Your reflections and the tags associated to them are shown over time");
                        authorControlCharts.help.helpPopover(timelineChart.elements.contentContainer.select(".circle"), `${timelineChart.id}-help-data`, "<u><i>hover</i></u> me for information on demand");
                    });
                }
                authorControlCharts.renderReflections(entries);
                //Handle users histogram chart help
                d3.select("#reflections .card-title button")
                    .on("click", function (e) {
                    authorControlCharts.help.helpPopover(d3.select(this), "reflections-help", "<b>Reflections</b><br>Your reflections are shown sorted by time. The words with associated tags have a different background colour");
                });
            });
        }
    });
}
