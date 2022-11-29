export interface ILoading {
    isLoading: boolean;
    spinner: HTMLElement;
    appendDiv(): HTMLElement;
    removeDiv(): void;
}
export declare class Loading implements ILoading {
    private _isLoading;
    get isLoading(): boolean;
    set isLoading(loading: boolean);
    spinner: HTMLElement;
    constructor();
    appendDiv(): HTMLElement;
    removeDiv(): void;
}
