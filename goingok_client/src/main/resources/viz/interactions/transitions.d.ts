;
import { IAdminAnalyticsData } from "../data/data.js";
import { ChartSeries, ChartTime } from "../charts/chartBase.js";
export interface ITransitions {
    axisSeries(chart: ChartSeries, data: IAdminAnalyticsData[]): void;
    axisTime(chart: ChartTime, data: IAdminAnalyticsData[]): void;
    axisLinear(chart: ChartSeries): void;
}
export declare class Transitions {
    axisSeries(chart: ChartSeries, data: IAdminAnalyticsData[]): d3.Transition<SVGGElement, unknown, HTMLElement, any>;
    axisTime(chart: ChartTime, data: IAdminAnalyticsData[]): void;
    axisLinear(chart: ChartSeries): void;
}
