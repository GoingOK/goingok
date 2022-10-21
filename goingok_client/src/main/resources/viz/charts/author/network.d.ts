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
    clicking: Click<this>;
    simulation: d3.Simulation<INodes, undefined>;
    extend?: Function;
    private _data;
    get data(): IAnalytics;
    set data(entries: IAnalytics);
    constructor(data: IAnalytics, domain: Date[]);
    render(): void;
    resetZoomRange(): void;
    getTooltipNodes(data: IAnalytics, nodeData: INodes): INodes[];
    private processSimulation;
    private filterData;
}
