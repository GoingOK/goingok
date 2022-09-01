var d3 = require("d3");
import { IAdminAnalyticsData, IAdminAnalyticsDataStats, IHistogramData, IReflectionAuthor } from "../data/data.js";
import { ChartSeries, ChartTime, HistogramChartSeries, ChartTimeZoom, IChart, ChartNetwork } from "./charts.js";
export interface ITransitions {
    axisSeries(chart: ChartSeries, data: IAdminAnalyticsData[]): void;
    axisTime(chart: ChartTime, data: IAdminAnalyticsData[]): void;
    axisLinear(chart: ChartSeries): void;
}
export declare class Transitions {
    axisSeries(chart: ChartSeries, data: IAdminAnalyticsData[]): void;
    axisTime(chart: ChartTime, data: IAdminAnalyticsData[]): void;
    axisLinear(chart: ChartSeries): void;
}
export interface IAdminControlTransitions extends ITransitions {
    histogram(chart: HistogramChartSeries, update: d3.Selection<SVGGElement, IAdminAnalyticsData, SVGGElement, unknown>): void;
    timelineDensity(update: d3.Selection<SVGGElement, IAdminAnalyticsData, SVGGElement, unknown>, getDensityData: Function): void;
    timelineScatter(update: d3.Selection<SVGGElement, IAdminAnalyticsData, SVGGElement, unknown>, chart: ChartTime | ChartTimeZoom, zoom?: boolean, invisible?: boolean): void;
}
export declare class AdminControlTransitions extends Transitions implements IAdminControlTransitions {
    histogram(chart: HistogramChartSeries, update: d3.Selection<SVGGElement, IAdminAnalyticsData, SVGGElement, unknown>): void;
    timelineDensity(update: d3.Selection<SVGGElement, IAdminAnalyticsData, SVGGElement, unknown>, getDensityData: Function): void;
    timelineScatter(update: d3.Selection<SVGGElement, IAdminAnalyticsData, SVGGElement, unknown>, chart: ChartTime | ChartTimeZoom, zoom?: boolean, invisible?: boolean): void;
}
export interface IAdminControlInteractions extends IAdminControlTransitions {
    tooltip: ITooltip;
    zoom: IZoom;
}
export declare class AdminControlInteractions extends AdminControlTransitions implements IAdminControlInteractions {
    tooltip: Tooltip;
    zoom: Zoom;
}
export interface ITooltipValues {
    label: string;
    value: number | string;
}
export declare class TooltipValues implements ITooltipValues {
    label: string;
    value: number | string;
    constructor(label?: string, value?: number | string);
}
export interface ITooltip {
    enableTooltip(chart: IChart, onMouseover: any, onMouseout: any): void;
    removeTooltip(chart: IChart): void;
    appendTooltipContainer(chart: IChart): void;
    appendTooltipText(chart: IChart, title: string, values: ITooltipValues[]): d3.Selection<SVGRectElement, unknown, HTMLElement, any>;
    positionTooltipContainer(chart: IChart, x: number, y: number): void;
    appendLine(chart: IChart, x1: number, y1: number, x2: number, y2: number, colour: string): void;
}
export declare class Tooltip implements ITooltip {
    enableTooltip(chart: IChart, onMouseover: any, onMouseout: any): void;
    removeTooltip(chart: IChart): void;
    appendTooltipContainer(chart: IChart): void;
    appendTooltipText(chart: IChart, title: string, values?: ITooltipValues[]): d3.Selection<SVGRectElement, unknown, HTMLElement, any>;
    positionTooltipContainer(chart: IChart, x: number, y: number): void;
    appendLine(chart: IChart, x1: number, y1: number, x2: number, y2: number, colour: string): void;
}
export interface IZoom {
    enableZoom(chart: ChartTime, zoomed: any): void;
    appendZoomBar(chart: ChartTime): d3.Selection<SVGGElement, unknown, HTMLElement, any>;
}
export declare class Zoom implements IZoom {
    enableZoom(chart: ChartTime | ChartNetwork, zoomed: any): void;
    appendZoomBar(chart: ChartTime): d3.Selection<SVGGElement, unknown, HTMLElement, any>;
}
export interface IAdminExperimentalInteractions extends IAdminControlInteractions {
    click: IClick;
    sort: ISort;
}
export declare class AdminExperimentalInteractions extends AdminControlInteractions implements IAdminExperimentalInteractions {
    click: ClickAdmin;
    sort: Sort;
}
export interface IClick {
    enableClick(chart: IChart, onClick: any): void;
    removeClick(chart: IChart): void;
}
export interface IClickAdmin {
    appendScatterText(chart: IChart, d: IReflectionAuthor, title: string, values: ITooltipValues[]): void;
    positionClickContainer(chart: ChartTime, box: any, text: any, d: IReflectionAuthor): string;
    appendGroupsText(chart: ChartSeries, data: IAdminAnalyticsDataStats[], clickData: IAdminAnalyticsDataStats): void;
    appendThresholdPercentages(chart: HistogramChartSeries, data: IAdminAnalyticsData[], clickData: IHistogramData): void;
}
export declare class Click implements IClick {
    enableClick(chart: IChart, onClick: any): void;
    removeClick(chart: IChart): void;
}
export declare class ClickAdmin extends Click implements IClickAdmin {
    appendScatterText(chart: ChartTime, d: IReflectionAuthor, title: string, values?: ITooltipValues[]): void;
    positionClickContainer(chart: ChartTime, box: any, text: any, d: IReflectionAuthor): string;
    appendGroupsText(chart: ChartSeries, data: IAdminAnalyticsDataStats[], clickData: IAdminAnalyticsDataStats): void;
    appendThresholdPercentages(chart: HistogramChartSeries, data: IAdminAnalyticsData[], clickData: IHistogramData): void;
    private comparativeText;
}
export interface ISort {
    sortData(a: number, b: number, sorted: boolean): number;
    setSorted(sorted: string, option: string): string;
}
export declare class Sort implements ISort {
    sortData(a: number | Date | string, b: number | Date | string, sorted: boolean): number;
    setSorted(sorted: string, option: string): string;
}
export interface IAuthorControlTransitions extends ITransitions {
}
export declare class AuthorControlTransitions extends Transitions implements IAuthorControlTransitions {
}
export interface IAuthorControlInteractions extends IAuthorControlTransitions {
    tooltip: ITooltip;
    zoom: IZoom;
}
export declare class AuthorControlInteractions extends AuthorControlTransitions implements IAuthorControlInteractions {
    tooltip: Tooltip;
    zoom: Zoom;
}
export interface IAuthorExperimentalInteractions extends IAuthorControlInteractions {
    click: IClick;
    sort: ISort;
}
export declare class AuthorExperimentalInteractions extends AuthorControlInteractions implements IAuthorExperimentalInteractions {
    click: Click;
    sort: Sort;
}
