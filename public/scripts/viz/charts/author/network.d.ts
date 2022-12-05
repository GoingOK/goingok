var d3 = require("d3");
import { IAnalytics, INodes } from "../../data/data.js";
import { Click } from "../../interactions/click.js";
import { Tooltip } from "../../interactions/tooltip.js";
import { Zoom } from "../../interactions/zoom.js";
import { ChartNetwork } from "../chartBase.js";
import { Help } from "../../utils/help.js";
export declare class Network extends ChartNetwork {
    tooltip: Tooltip<this>;
    zoom: Zoom<this>;
    help: Help;
    clicking: ClickNetwork<this>;
    simulation: d3.Simulation<INodes, undefined>;
    extend?: Function;
    private _data;
    get data(): IAnalytics;
    set data(entries: IAnalytics);
    constructor(data: IAnalytics, domain: Date[]);
    render(): Promise<void>;
    getTooltipNodes(data: IAnalytics, nodeData: INodes): INodes[];
    openNodes(data: INodes[]): void;
    closeNodes(): void;
    processSimulation(data: IAnalytics): void;
    private filterData;
}
declare class ClickNetwork<T extends Network> extends Click<T> {
    removeClick(): void;
}
export {};
