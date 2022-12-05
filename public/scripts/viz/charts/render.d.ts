var d3 = require("d3");
import { IChart, IChartBasic } from "./chartBase.js";
interface IChartElementsContainers {
    svg: d3.Selection<SVGSVGElement, unknown, HTMLElement, any>;
    contentContainer: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    content: d3.Selection<SVGRectElement | SVGCircleElement | SVGPathElement | d3.BaseType, unknown, SVGGElement, any>;
}
declare class ChartElementsContainers<T extends IChartBasic> implements IChartElementsContainers {
    protected chart: T;
    svg: d3.Selection<SVGSVGElement, unknown, HTMLElement, any>;
    contentContainer: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    content: d3.Selection<SVGRectElement | SVGCircleElement | SVGPathElement, unknown, SVGGElement, any>;
    constructor(chart: T, containerClass?: string);
    private appendSVG;
    private appendContentContainer;
}
export interface IChartElements extends IChartElementsContainers {
    xAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    yAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomSVG: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomFocus: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
}
export declare class ChartElements<T extends IChart> extends ChartElementsContainers<T> implements IChartElements {
    xAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    yAxis: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomSVG: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    zoomFocus: d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    constructor(chart: T, containerClass?: string);
    private appendXAxis;
    private appendXAxisLabel;
    private appendYAxis;
    private appendYAxisLabel;
}
export {};
