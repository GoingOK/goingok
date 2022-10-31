import { IAdminAnalyticsData, IReflectionAuthor } from "../../data/data.js";
import { Sort } from "../../interactions/sort.js";
declare type UserData = {
    pseudonym: string;
    mean: number;
    total: number;
    minDate: Date;
    maxDate: Date;
    reflections: IReflectionAuthor[];
};
export declare class Users {
    id: string;
    sort: Sort<UserData>;
    group: string;
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
    private renderNoData;
    private getUserData;
}
export {};
