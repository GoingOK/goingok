import { IAdminAnalyticsData } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip } from "../../interactions/tooltip.js";
import { ChartSeries } from "../chartBase.js";
export declare class BarChart extends ChartSeries {
    tooltip: Tooltip<this>;
    clicking: Click<this>;
    extend?: Function;
    private _data;
    get data(): IAdminAnalyticsData[];
    set data(entries: IAdminAnalyticsData[]);
    constructor(data: IAdminAnalyticsData[]);
    render(): void;
}
