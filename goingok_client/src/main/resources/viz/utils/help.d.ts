import { IChart } from "../charts/chartBase.js";
export interface IHelp {
    helpPopover(id: string, content: string): void;
    removeHelp(chart: IChart): void;
}
export declare class Help implements IHelp {
    helpPopover(id: string, content: string): void;
    removeHelp(chart: IChart): void;
}
