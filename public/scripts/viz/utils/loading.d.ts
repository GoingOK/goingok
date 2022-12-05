import { IChart } from "../charts/chartBase";
export interface ILoading {
    isLoading: boolean;
    spinner: HTMLElement;
    appendDiv(): HTMLElement;
    removeDiv(): void;
}
export declare class Loading<T extends IChart> implements ILoading {
    private _isLoading;
    chart: T;
    get isLoading(): boolean;
    set isLoading(loading: boolean);
    constructor(chart: T);
    spinner: HTMLElement;
    appendDiv(): HTMLElement;
    removeDiv(): void;
}
