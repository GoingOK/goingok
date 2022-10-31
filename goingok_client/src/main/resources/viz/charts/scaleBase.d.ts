var d3 = require("d3");
interface IChartAxis {
    scale: d3.ScaleBand<string> | d3.ScaleLinear<number, number, never> | d3.ScaleTime<number, number, never>;
    axis: d3.Axis<d3.AxisDomain>;
    label: string;
    transition(data: string[] | number[] | Date[]): void;
}
export declare class ChartSeriesAxis implements IChartAxis {
    protected id: string;
    scale: d3.ScaleBand<string>;
    axis: d3.Axis<d3.AxisDomain>;
    label: string;
    constructor(id: string, label: string, domain: string[], range: number[], position?: string);
    transition(data: string[]): void;
}
export declare class ChartLinearAxis implements IChartAxis {
    protected id: string;
    scale: d3.ScaleLinear<number, number, never>;
    axis: d3.Axis<d3.AxisDomain>;
    label: string;
    constructor(id: string, label: string, domain: number[], range: number[], position?: string, isGoingOk?: boolean);
    transition(data: number[]): void;
    setThresholdAxis(tDistressed: number, tSoaring: number): d3.Axis<d3.NumberValue>;
}
export declare class ChartTimeAxis implements IChartAxis {
    protected id: string;
    scale: d3.ScaleTime<number, number, never>;
    axis: d3.Axis<d3.AxisDomain>;
    label: string;
    constructor(id: string, label: string, domain: Date[], range: number[]);
    transition(data: Date[]): void;
}
export {};
