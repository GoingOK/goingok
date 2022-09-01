var d3 = require("d3");
import { IAdminAnalyticsData, ITags } from "../data/data.js";
import { ChartSeriesAxis, ChartTimeAxis, ChartLinearAxis } from "./scales.js";
import { IHelp } from "./help.js";
import { IChartPadding, IChartElements, IHistogramChartElements, ChartPadding } from "./render.js";
export interface IChartScales {
    x: ChartSeriesAxis | ChartTimeAxis | ChartLinearAxis;
    y: ChartLinearAxis | ChartSeriesAxis;
}
export interface IChart extends IChartScales {
    id: string;
    width: number;
    height: number;
    padding: IChartPadding;
    elements: IChartElements | IHistogramChartElements;
    click: boolean;
}
export declare class ChartSeries implements IChart {
    id: string;
    width: number;
    height: number;
    x: ChartSeriesAxis;
    y: ChartLinearAxis;
    elements: IChartElements;
    padding: IChartPadding;
    click: boolean;
    constructor(id: string, domain: string[], isGoingOk?: boolean, yDomain?: number[]);
}
export declare class ChartTime implements IChart {
    id: string;
    width: number;
    height: number;
    x: ChartTimeAxis;
    y: ChartLinearAxis;
    elements: IChartElements;
    help: IHelp;
    padding: IChartPadding;
    click: boolean;
    constructor(id: string, domain: Date[], chartPadding?: ChartPadding);
}
export declare class ChartTimeZoom implements IChartScales {
    x: ChartTimeAxis;
    y: ChartLinearAxis;
    constructor(chart: IChart, domain: Date[]);
}
export declare class UserChart implements IChart {
    id: string;
    width: number;
    height: number;
    x: ChartLinearAxis;
    y: ChartSeriesAxis;
    elements: IChartElements;
    padding: IChartPadding;
    click: boolean;
    constructor(id: string, containerClass: string);
}
export interface IHistogramChartSeries extends IChart {
    elements: IHistogramChartElements;
    thresholdAxis: d3.Axis<d3.NumberValue>;
    bandwidth: d3.ScaleLinear<number, number, never>;
    bin: d3.HistogramGeneratorNumber<number, number>;
    y: ChartLinearAxis;
    setBandwidth(data: IAdminAnalyticsData[]): void;
    setBin(): void;
}
export declare class HistogramChartSeries extends ChartSeries implements IHistogramChartSeries {
    elements: IHistogramChartElements;
    thresholdAxis: d3.Axis<d3.NumberValue>;
    bandwidth: d3.ScaleLinear<number, number, never>;
    bin: d3.HistogramGeneratorNumber<number, number>;
    constructor(id: string, domain: string[]);
    setBandwidth(data: IAdminAnalyticsData[]): void;
    setBin(): void;
}
export declare class ChartTimeNetwork extends ChartTime {
    constructor(id: string, domain: Date[], chartPadding: ChartPadding);
    addDays(date: Date, days: number): Date;
}
export declare class ChartNetwork implements IChart {
    id: string;
    width: number;
    height: number;
    x: ChartTimeAxis;
    y: ChartLinearAxis;
    elements: IChartElements;
    padding: IChartPadding;
    click: boolean;
    simulation: d3.Simulation<ITags, undefined>;
    constructor(id: string, containerClass: string, domain: Date[]);
    addDays(date: Date, days: number): Date;
    resetZoomRange(): void;
}
