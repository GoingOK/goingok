var d3 = require("d3");
import { Dashboard } from "./authorControl.js";
import { Tutorial, TutorialData } from "../utils/tutorial.js";
import { Help } from "../utils/help.js";
export class ExperimentalDashboard extends Dashboard {
    constructor(entriesRaw, analyticsRaw) {
        super(entriesRaw, analyticsRaw);
        this.timeline.extend = this.extendTimeline.bind(this);
        this.extendTimeline();
        this.network.extend = this.extendNetwork.bind(this);
        this.extendNetwork();
        this.reflections.extend = this.extendReflections.bind(this);
        this.extendReflections();
        this.handleTags();
        this.handleTagsColours();
    }
    handleMultiUser(entries) {
        const extendFunction = (e, d) => {
            this.timeline.clicking.removeClick();
            this.reflectionAnalytics = d.reflections;
            this.analytics = d.analytics;
            const reflectionsData = this.updateReflectionNodesData();
            this.timeline.data = reflectionsData;
            this.reflections.data = reflectionsData;
            if (d.analytics.nodes.some(d => d.index === undefined))
                this.network.processSimulation(d.analytics);
            this.network.data = this.updateAnalyticsData();
        };
        super.handleMultiUser(entries, extendFunction);
    }
    preloadTags(entries) {
        this.tags = super.preloadTags(entries, true).filter(d => d.selected);
        this.reflectionAnalytics = entries.reflections;
        this.analytics = entries.analytics;
        d3.select("#tags").selectAll("li").select("div")
            .insert("div", "input")
            .attr("class", "input-group-prepend")
            .append("div")
            .attr("class", "input-group-text tag-row")
            .append("input")
            .attr("type", "checkbox")
            .attr("value", d => d.name)
            .property("checked", true);
        return this.tags;
    }
    handleTags() {
        const _this = this;
        d3.selectAll("#tags input[type=checkbox]").on("change", (e) => {
            const target = e.target;
            if (target.checked) {
                _this.tags.find(c => c.name === target.value).selected = true;
            }
            else {
                _this.tags.find(c => c.name === target.value).selected = false;
            }
            const reflectionsData = _this.updateReflectionNodesData();
            const clickData = _this.getClickTimelineNetworkData();
            const nodes = _this.network.clicking.clicked ? _this.reflections.nodes : null;
            _this.timeline.data = reflectionsData;
            if (_this.timeline.clicking.clicked) {
                _this.reflections.data = _this.updateReflectionNodesData(clickData);
                _this.network.data = _this.getClickTimelineNetworkNodes(clickData);
            }
            else {
                _this.reflections.data = reflectionsData;
                _this.network.data = _this.updateAnalyticsData();
            }
            if (nodes !== null) {
                _this.network.clicking.removeClick();
                _this.reflections.nodes = nodes;
                _this.network.clicking.clicked = true;
                _this.network.elements.content.classed("clicked", (d) => nodes.map(c => c.idx).includes(d.idx));
                _this.network.openNodes(nodes);
            }
        });
    }
    handleTagsColours() {
        const _this = this;
        d3.selectAll("#tags input[type=color]").on("change", (e) => {
            const target = e.target;
            const name = target.id.replace("colour-", "");
            _this.tags.find(c => c.name === name).properties["color"] = target.value;
            const reflectionsData = _this.updateReflectionNodesData();
            const nodes = _this.network.clicking.clicked ? _this.reflections.nodes : null;
            _this.timeline.data = reflectionsData;
            _this.reflections.data = reflectionsData;
            _this.network.data = _this.updateAnalyticsData();
            if (nodes !== null) {
                _this.reflections.nodes = nodes;
            }
        });
    }
    extendTimeline() {
        const _this = this;
        const chart = _this.timeline;
        chart.elements.contentContainer.select(".zoom-rect").on("click", () => {
            chart.clicking.removeClick();
            _this.network.data = _this.updateAnalyticsData();
            _this.reflections.data = _this.updateReflectionNodesData();
        });
        const onClick = function () {
            if (d3.select(this).attr("class").includes("clicked")) {
                chart.clicking.removeClick();
                _this.network.data = _this.updateAnalyticsData();
                _this.reflections.data = _this.updateReflectionNodesData();
                return;
            }
            chart.clicking.removeClick();
            chart.clicking.clicked = true;
            d3.select(this).classed("clicked", true)
                .attr("r", 10);
            const parentData = d3.select(d3.select(this).node().parentElement).datum();
            _this.network.data = _this.getClickTimelineNetworkNodes(parentData);
            _this.reflections.data = [parentData];
        };
        chart.clicking.enableClick(onClick);
    }
    extendNetwork() {
        const _this = this;
        const chart = _this.network;
        d3.select(`#${chart.id} .badge`).on("click", () => _this.handleFilterButton());
        chart.elements.contentContainer.select(".zoom-rect").on("click", () => {
            chart.clicking.removeClick();
        });
        const onClick = function (e, d) {
            if (d3.select(this).attr("class").includes("clicked")) {
                chart.clicking.removeClick();
            }
            chart.clicking.removeClick();
            chart.clicking.clicked = true;
            let nodes = chart.getTooltipNodes(_this.analytics, d);
            chart.openNodes(nodes);
            chart.elements.content.classed("clicked", (d) => nodes.map(c => c.idx).includes(d.idx));
            _this.reflections.nodes = nodes;
            document.querySelector(`#ref-${d.refId}`).scrollIntoView({ behavior: 'smooth', block: 'start' });
        };
        chart.clicking.enableClick(onClick);
        const dragStart = function (e) {
            if (!e.active)
                chart.simulation.alphaTarget(0.3).restart();
            e.subject.fx = e.subject.x;
            e.subject.fy = e.subject.y;
            d3.select(this).attr("transform", `translate(${e.subject.fx}, ${e.subject.fy})`);
        };
        const dragging = function (e) {
            e.subject.fx = e.x;
            e.subject.fy = e.y;
            d3.select(this).attr("transform", `translate(${e.subject.fx}, ${e.subject.fy})`);
        };
        const dragEnd = function (e) {
            if (!e.active)
                chart.simulation.alphaTarget(0);
            e.subject.fx = null;
            e.subject.fy = null;
            d3.select(this).attr("transform", `translate(${e.subject.x}, ${e.subject.y})`);
        };
        chart.elements.content
            .call(d3.drag()
            .on("start", dragStart)
            .on("drag", dragging)
            .on("end", dragEnd));
    }
    extendReflections() {
        d3.select(`#reflections .badge`).on("click", () => this.handleFilterButton());
    }
    handleFilterButton() {
        this.timeline.clicking.removeClick();
        const reflectionsData = this.updateReflectionNodesData();
        this.timeline.data = reflectionsData;
        this.reflections.data = reflectionsData;
        this.network.data = this.updateAnalyticsData();
    }
    ;
    updateReflectionNodesData(analytics) {
        const reflectionAnalytics = analytics === undefined ?
            this.reflectionAnalytics :
            this.reflectionAnalytics.filter(d => d.refId === analytics.refId);
        return reflectionAnalytics.map(c => {
            c.nodes = c.nodes.map(r => {
                let tag = this.tags.find(d => d.name === r.name);
                r.selected = tag.selected;
                r.properties["color"] = tag.properties["color"];
                return r;
            });
            return { "refId": c.refId, "point": c.point, "text": c.text, "timestamp": c.timestamp, "nodes": c.nodes };
        });
    }
    updateAnalyticsData() {
        let nodes = this.analytics.nodes.map(c => {
            let tag = this.tags.find(d => d.name === c.name);
            c.selected = tag.selected;
            c.properties["color"] = tag.properties["color"];
            return c;
        });
        let edges = this.analytics.edges.map(c => {
            c.source.selected = this.tags.find(d => d.name === c.source.name).selected;
            c.target.selected = this.tags.find(d => d.name === c.target.name).selected;
            return c;
        });
        return { "name": this.analytics.name, "description": this.analytics.description, "nodes": nodes, "edges": edges };
    }
    getClickTimelineNetworkData() {
        const el = document.querySelector(`#${this.timeline.id} .clicked`);
        if (el === null)
            return;
        return d3.select(el.parentElement).datum();
    }
    getClickTimelineNetworkNodes(d) {
        const analytics = this.updateAnalyticsData();
        let nodes = analytics.nodes.filter(c => {
            return (d.refId === c.refId || c.name === d.timestamp.toDateString()) && c.selected;
        });
        let edges = analytics.edges.filter(c => {
            return nodes.includes(c.source) || nodes.includes(c.target);
        });
        return { "name": this.analytics.name, "description": this.analytics.description, "nodes": nodes, "edges": edges };
    }
}
export function buildExperimentAuthorAnalyticsCharts(entriesRaw, analyticsRaw) {
    const dashboard = new ExperimentalDashboard(entriesRaw, analyticsRaw);
    const help = new Help();
    //Handle timeline chart help
    help.helpPopover(dashboard.network.id, `<b>Network diagram</b><br>
        A network diagram that shows the phrases and tags associated to your reflections<br>The data represented are your <i>reflections over time</i><br>
        <u><i>Hover</i></u> over the network nodes for information on demand<br>
        <u><i>Drag</i></u> the network nodes to rearrange the network<br>
        <u><i>Click</i></u> to fill the background colour the nodes in the reflection text`);
    //Handle timeline chart help
    help.helpPopover(dashboard.timeline.id, `<b>Timeline</b><br>
        Your reflections and the tags associated to them are shown over time<br>
        <u><i>Hover</i></u> over a reflection point for information on demand<br>
        <u><i>Click</i></u> a reflection point to filter the network diagram and reflection text`);
    //Handle reflections chart help
    help.helpPopover(dashboard.reflections.id, `<b>Reflections</b><br>
        Your reflections are shown sorted by time. The words with associated tags have a different outline colour<br>
        The reflections can be sorted by time or reflection point`);
    new Tutorial([new TutorialData("#timeline .card-title button", "Click the help symbol in any chart to get additional information"),
        new TutorialData("#timeline .circle", "Hover for information on demand. Click to drill-down updating the reflections text and network"),
        new TutorialData("#sort .sort-by", "Sort reflections by date or reflection state point"),
        new TutorialData("#reflections .reflection-text span", "Phrases outlined with a colour that matches the tags"),
        new TutorialData("#tags li", "Select which tags to see and change the colours if you like"),
        new TutorialData("#network .network-node-group", "Hover for information on demand. Click to fill the background colour of the nodes in the reflection text"),
        new TutorialData("#network .zoom-buttons", "Click to zoom in and out. To pan the chart click, hold and move left or right in any blank area")]);
}
