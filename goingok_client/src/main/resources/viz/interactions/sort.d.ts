export declare class Sort<T> {
    protected id: string;
    protected asc: boolean;
    private _sortBy;
    get sortBy(): string;
    set sortBy(sortBy: string);
    constructor(id: string, sortBy: string);
    sortData(data: Array<T>): Array<T>;
    private sortFunction;
    private setAsc;
    protected setChevronVisibility(): void;
    protected handleChevronChange(): void;
    private getParentEl;
}
