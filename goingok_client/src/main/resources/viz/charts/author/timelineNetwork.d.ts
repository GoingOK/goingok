import { IReflectionAnalytics } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip } from "../../interactions/tooltip.js";
import { ChartTime } from "../chartBase.js";
export declare class TimelineNetwork extends ChartTime {
    tooltip: Tooltip<this>;
    clicking: ClickTimelineNetwork<this>;
    extend?: Function;
    private _data;
    get data(): IReflectionAnalytics[];
    set data(entries: IReflectionAnalytics[]);
    constructor(data: IReflectionAnalytics[]);
    render(): void;
    private renderReflectionNetwork;
    private simulation;
}
declare class ClickTimelineNetwork<T extends TimelineNetwork> extends Click<T> {
    removeClick(): void;
}
export {};
