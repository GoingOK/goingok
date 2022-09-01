export interface ILoading {
    isLoading: boolean;
    spinner: HTMLElement;
    appendDiv(): HTMLElement;
    removeDiv(): void;
}
export declare class Loading implements ILoading {
    isLoading: boolean;
    spinner: HTMLElement;
    constructor();
    appendDiv(): HTMLElement;
    removeDiv(): void;
}
