var d3 = require("d3");
import { IChart } from "../charts/chartBase.js";
declare type ZoomFunction = {
    (this: Element, event: d3.D3ZoomEvent<SVGRectElement, unknown>, d: unknown): void;
};
export interface IZoom {
    zoom: d3.ZoomBehavior<Element, unknown>;
    k: number;
    enableZoom(zoomed: any): void;
    appendZoomBar(): d3.Selection<SVGGElement, unknown, HTMLElement, any>;
}
export declare class Zoom<T extends IChart> implements IZoom {
    protected chart: T;
    zoom: d3.ZoomBehavior<Element, unknown>;
    private _k;
    get k(): number;
    set k(k: number);
    constructor(chart: T);
    enableZoom(zoomed: ZoomFunction): void;
    appendZoomBar(): d3.Selection<SVGGElement, unknown, HTMLElement, any>;
    resetZoom(): void;
    protected handleZoom(): void;
    private updateZoomNumber;
}
export {};
