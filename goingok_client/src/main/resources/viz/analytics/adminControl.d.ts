var d3 = require("d3");
import { ChartSeries, HistogramChartSeries, ChartTime, ChartTimeZoom } from "../charts/charts.js";
import { IHelp, Help } from "../charts/help.js";
import { IAdminAnalyticsData, IAdminAnalyticsDataStats, ITimelineData, IReflectionAuthor } from "../data/data.js";
import { IAdminControlInteractions, AdminControlInteractions } from "../charts/interactions.js";
import { IAdminAnalyticsDataRaw } from "../data/db.js";
export interface IAdminControlCharts {
    help: IHelp;
    interactions: IAdminControlInteractions;
    sidebarBtn(): void;
    preloadGroups(allEntries: IAdminAnalyticsData[]): IAdminAnalyticsData[];
    renderTotals(data: IAdminAnalyticsDataStats[]): void;
    renderBarChart(chart: ChartSeries, data: IAdminAnalyticsDataStats[]): ChartSeries;
    renderHistogram(chart: HistogramChartSeries, data: IAdminAnalyticsData[]): HistogramChartSeries;
    handleHistogramHover(chart: HistogramChartSeries, bandwidth: d3.ScaleLinear<number, number, never>): void;
    renderTimelineDensity(chart: ChartTime, data: IAdminAnalyticsData[]): ChartTime;
    renderTimelineScatter(chart: ChartTime, zoomChart: ChartTimeZoom, data: IAdminAnalyticsData[]): ChartTime;
    handleTimelineButtons(chart: ChartTime, zoomChart: ChartTimeZoom, data: IAdminAnalyticsData[], func?: Function): void;
    renderUserStatistics(card: d3.Selection<HTMLDivElement, unknown, HTMLElement, any>, data: IAdminAnalyticsData, thresholds: number[], timelineData?: ITimelineData): void;
}
export declare class AdminControlCharts implements IAdminControlCharts {
    help: Help;
    interactions: AdminControlInteractions;
    sidebarBtn(): void;
    preloadGroups(allEntries: IAdminAnalyticsData[], enable?: boolean): IAdminAnalyticsData[];
    renderTotals(data: IAdminAnalyticsDataStats[]): void;
    renderBarChart(chart: ChartSeries, data: IAdminAnalyticsDataStats[]): ChartSeries;
    renderHistogram(chart: HistogramChartSeries, data: IAdminAnalyticsData[]): HistogramChartSeries;
    handleHistogramHover(chart: HistogramChartSeries): void;
    renderTimelineDensity(chart: ChartTime, data: IAdminAnalyticsData[]): ChartTime;
    renderTimelineScatter(chart: ChartTime, zoomChart: ChartTimeZoom, data: IAdminAnalyticsData[]): ChartTime;
    handleTimelineButtons(chart: ChartTime, zoomChart: ChartTimeZoom, data: IAdminAnalyticsData[], func?: Function): void;
    renderUserStatistics(card: d3.Selection<HTMLDivElement, unknown, HTMLElement, any>, data: IAdminAnalyticsData, thresholds: number[], timelineData?: ITimelineData): void;
    protected getUserStatisticBinName(data: IReflectionAuthor, thresholds: number[]): string;
}
export declare function buildControlAdminAnalyticsCharts(entriesRaw: IAdminAnalyticsDataRaw[]): Promise<void>;
