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
import { Help } from "../utils/help.js";
import { AdminAnalyticsData } from "../data/data.js";
import { AdminAnalyticsDataRaw } from "../data/db.js";
import { Tutorial, TutorialData } from "../utils/tutorial.js";
import { Histogram } from "../charts/admin/histogram.js";
import { BarChart } from "../charts/admin/barChart.js";
import { Timeline } from "../charts/admin/timeline.js";
import { Users } from "../charts/admin/users.js";
import { Totals } from "../charts/admin/totals.js";
export class Dashboard {
    constructor(data) {
        this.totals = new Totals(data);
        this.barChart = new BarChart(data);
        this.histogram = new Histogram(data);
        this.timeline = new Timeline(data);
        this.users = new Users(data);
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
            .call(div => div.append("input")
            .attr("type", "text")
            .attr("class", "form-control group-row")
            .attr("value", d => d.group)
            .property("disabled", true))
            .call(div => div.append("div")
            .attr("class", "input-group-append")
            .append("div")
            .attr("class", "input-group-text group-row")
            .append("input")
            .attr("id", d => `colour-${d.group}`)
            .attr("type", "color")
            .attr("value", d => d.colour)
            .property("disabled", !enable));
        return allEntries;
    }
}
export function buildControlAdminAnalyticsCharts(entriesRaw) {
    return __awaiter(this, void 0, void 0, function* () {
        const rawData = entriesRaw.map(d => new AdminAnalyticsDataRaw(d.group, d.value, d.createDate));
        let entries = rawData.map(d => d.transformData());
        const colourScale = d3.scaleOrdinal(d3.schemeCategory10);
        entries = entries.map(d => new AdminAnalyticsData(d.group, d.value, d.createDate, colourScale(d.group), true));
        yield drawCharts(entries);
        new Tutorial([new TutorialData("#groups", "All your groups are selected to visualise and colours assigned. You cannot change this section"),
            new TutorialData(".card-title button", "Click the help symbol in any chart to get additional information"),
            new TutorialData("#users .bar", "Hover for information on demand"),
            new TutorialData("#histogram .histogram-rect", "Hover for information on demand"),
            new TutorialData("#timeline .zoom-buttons", "Click to zoom in and out. To pan the chart click, hold and move left or right in any blank area")]);
        function drawCharts(allEntries) {
            return __awaiter(this, void 0, void 0, function* () {
                const help = new Help();
                const dashboard = new Dashboard(allEntries);
                //Handle sidebar button
                dashboard.sidebarBtn();
                dashboard.preloadGroups(allEntries);
                //Create groups chart with current data
                d3.select("#users .card-subtitle")
                    .html("");
                //Handle groups chart help
                help.helpPopover(dashboard.barChart.id, `<b>Bar chart</b><br>
            A bar chart of the users in each group code<br>
            <u><i>Hover</i></u> over the bars for information on demand`);
                //Handle users histogram chart help
                help.helpPopover(dashboard.histogram.id, `<b>Histogram</b><br>
            A histogram group data points into user-specific ranges. The data points in this histogram are <i>users average reflection point</i>
            <u><i>Hover</i></u> over the boxes for information on demand`);
                //Handle timeline chart help
                d3.select("#timeline .card-title button")
                    .on("click", function (e) {
                    help.helpPopover(`${dashboard.timeline.id}-help`, `<b>Scatter plot</b><br>
                    The data is showed as a collection of points<br>The data represented are <i>reflections over time</i><br>
                    <u><i>Hover</i></u> over the circles for information on demand`);
                });
                //Handle users histogram chart help
                help.helpPopover("reflections", `<b>Reflections</b><br>
            Each user's reflections are shown sorted by time. The chart depicts the percentage of reflections in each reflection point group`);
            });
        }
    });
}
