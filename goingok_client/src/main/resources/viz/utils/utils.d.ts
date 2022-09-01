export interface IGroupBy<T> {
    key: string;
    value: Array<T>;
}
export declare function groupBy<T>(arr: Array<T>, criteria: string): IGroupBy<T>[];
export declare function caculateSum(arr: number[]): number;
export declare function calculateMean(arr: number[]): number;
