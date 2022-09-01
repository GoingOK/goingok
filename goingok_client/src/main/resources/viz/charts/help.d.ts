import { IChart } from "./charts.js";
export interface IHelp {
    helpPopover(button: any, id: string, content: string): boolean;
    removeHelp(chart: IChart): void;
}
export declare class Help implements IHelp {
    helpPopover(button: any, id: string, content: string): boolean;
    removeHelp(chart: IChart): void;
}
