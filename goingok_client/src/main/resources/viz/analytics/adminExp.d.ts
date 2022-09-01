import { ChartSeries, IHistogramChartSeries, ChartTime, ChartTimeZoom, HistogramChartSeries } from "../charts/charts.js";
import { IAdminAnalyticsData, IAdminAnalyticsDataStats } from "../data/data.js";
import { IAdminControlCharts, AdminControlCharts } from "./adminControl.js";
import { AdminExperimentalInteractions } from "../charts/interactions.js";
import { IAdminAnalyticsDataRaw } from "../data/db.js";
export interface IAdminExperimentalCharts extends IAdminControlCharts {
    barChart: ChartSeries;
    histogram: IHistogramChartSeries;
    timeline: ChartTime;
    timelineZoom: ChartTimeZoom;
    sorted: string;
    allEntries: IAdminAnalyticsData[];
    handleGroups(): void;
    handleGroupsColours(): void;
    handleGroupsSort(): void;
    handleFilterButton(): void;
}
export declare class AdminExperimentalCharts extends AdminControlCharts implements IAdminExperimentalCharts {
    barChart: ChartSeries;
    histogram: HistogramChartSeries;
    timeline: ChartTime;
    timelineZoom: ChartTimeZoom;
    sorted: string;
    allEntries: IAdminAnalyticsData[];
    interactions: AdminExperimentalInteractions;
    preloadGroups(allEntries: IAdminAnalyticsData[]): IAdminAnalyticsData[];
    handleGroups(): void;
    handleGroupsColours(): void;
    handleGroupsSort(): void;
    handleFilterButton(): void;
    private getUpdatedData;
    private getClickData;
    private updateBarChart;
    private updateHistogram;
    private updateTimeline;
    private removeUserStatistics;
    private removeAllHelp;
    renderBarChart(chart: ChartSeries, data: IAdminAnalyticsDataStats[]): ChartSeries;
    renderHistogram(chart: HistogramChartSeries, data: IAdminAnalyticsData[]): HistogramChartSeries;
    renderTimelineScatter(chart: ChartTime, zoomChart: ChartTimeZoom, data: IAdminAnalyticsData[]): ChartTime;
    renderTimelineDensity(chart: ChartTime, data: IAdminAnalyticsData[]): ChartTime;
    handleTimelineButtons(chart: ChartTime, zoomChart: ChartTimeZoom, data: IAdminAnalyticsData[]): void;
}
export declare function buildExperimentAdminAnalyticsCharts(entriesRaw: IAdminAnalyticsDataRaw[]): Promise<void>;
