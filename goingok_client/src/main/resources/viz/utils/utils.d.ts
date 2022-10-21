export interface IGroupBy<T> {
    key: string;
    value: Array<T>;
}
export declare function groupBy<T>(arr: Array<T>, criteria: string): IGroupBy<T>[];
export declare function caculateSum(arr: number[]): number;
export declare function calculateMean(arr: number[]): number;
export declare function getDOMRect(id: string): DOMRect;
export declare function addDays(date: Date, days: number): Date;
export declare function minDate(arr: Date[]): Date;
export declare function maxDate(arr: Date[]): Date;
