export interface ISort {
    sortData(a: number, b: number, sorted: boolean): number;
    setSorted(sorted: string, option: string): string;
    setChevronVisibility(id: string, option: string): void;
    handleChevronChange(id: string, option: string, chevron: string): void;
}
export declare class Sort implements ISort {
    sortData(a: number | Date | string, b: number | Date | string, sorted: boolean): number;
    setSorted(sorted: string, option: string): string;
    setChevronVisibility(id: string, option: string): void;
    handleChevronChange(id: string, option: string, chevron: string): void;
    private getParentEl;
}
