import { ChartSeriesAxis, ChartTimeAxis, ChartLinearAxis } from "./scaleBase.js";
import { IHelp } from "../utils/help.js";
import { IChartElements } from "./render.js";
import { ILoading } from "../utils/loading.js";
export interface IChartScales {
    x: ChartSeriesAxis | ChartTimeAxis | ChartLinearAxis;
    y: ChartLinearAxis | ChartSeriesAxis;
}
export interface IChartBasic {
    id: string;
    width: number;
    height: number;
    padding: IChartPadding;
}
export interface IChart extends IChartScales, IChartBasic {
    elements: IChartElements;
    loading: ILoading;
    renderError(e: any): void;
}
export interface IChartPadding {
    xAxis: number;
    yAxis: number;
    top: number;
    right: number;
}
export declare class ChartPadding implements IChartPadding {
    xAxis: number;
    yAxis: number;
    top: number;
    right: number;
    constructor(xAxis?: number, yAxis?: number, top?: number, right?: number);
}
export declare class ChartSeries implements IChart {
    id: string;
    width: number;
    height: number;
    x: ChartSeriesAxis;
    y: ChartLinearAxis;
    elements: IChartElements;
    padding: IChartPadding;
    loading: ILoading;
    constructor(id: string, domain: string[], isGoingOk?: boolean, yDomain?: number[]);
    renderError(e: any): void;
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
    loading: ILoading;
    constructor(id: string, domain: Date[], chartPadding?: ChartPadding);
    renderError(e: any): void;
}
export declare class ChartNetwork implements IChart {
    id: string;
    width: number;
    height: number;
    x: ChartTimeAxis;
    y: ChartLinearAxis;
    padding: IChartPadding;
    elements: IChartElements;
    loading: ILoading;
    constructor(id: string, containerClass: string, domain: Date[]);
    renderError(e: any): void;
}
