import { IAdminAnalyticsData } from "../../data/data.js";
import { Sort } from "../../interactions/sort.js";
export declare class Users {
    id: string;
    sorted: string;
    sort: Sort;
    private _data;
    get data(): IAdminAnalyticsData[];
    set data(entries: IAdminAnalyticsData[]);
    private _thresholds;
    get thresholds(): number[];
    set thresholds(thresholds: number[]);
    constructor(data: IAdminAnalyticsData[]);
    render(): void;
    private renderTabContent;
    private renderReflections;
    private handleSort;
    private renderUserMeter;
}
