var d3 = require("d3");
import { IChart, HistogramChartSeries, IHistogramChartSeries } from "./charts.js";
export interface IChartElements {
    svg: d3.Selection<SVGSVGElement, unknown, HTMLElement, any>;
    contentContainer: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    content: d3.Selection<SVGRectElement | SVGCircleElement | SVGPathElement | d3.BaseType, unknown, SVGGElement, any>;
    xAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    yAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomSVG: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomFocus: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
}
export declare class ChartElements implements IChartElements {
    svg: d3.Selection<SVGSVGElement, unknown, HTMLElement, any>;
    contentContainer: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    content: d3.Selection<SVGRectElement | SVGCircleElement | SVGPathElement, unknown, SVGGElement, any>;
    xAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    yAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomSVG: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomFocus: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    constructor(chart: IChart, containerClass?: string);
    private appendSVG;
    private appendContentContainer;
    private appendXAxis;
    private appendXAxisLabel;
    private appendYAxis;
    private appendYAxisLabel;
}
export interface IHistogramChartElements extends IChartElements {
    getThresholdsValues(chart: HistogramChartSeries): number[];
}
export declare class HistogramChartElements extends ChartElements implements IHistogramChartElements {
    constructor(chart: IHistogramChartSeries);
    private appendThresholdAxis;
    private appendThresholdLabel;
    private appendThresholdIndicators;
    getThresholdsValues(chart: IHistogramChartSeries): number[];
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
