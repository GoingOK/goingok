import { IReflectionAnalytics } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip } from "../../interactions/tooltip.js";
import { ChartTime } from "../chartBase.js";
export declare class TimelineNetwork extends ChartTime {
    tooltip: Tooltip<this>;
    clicking: Click<this>;
    extend?: Function;
    private _data;
    get data(): IReflectionAnalytics[];
    set data(entries: IReflectionAnalytics[]);
    constructor(data: IReflectionAnalytics[]);
    render(): void;
    private renderReflectionNetwork;
    private simulation;
}
