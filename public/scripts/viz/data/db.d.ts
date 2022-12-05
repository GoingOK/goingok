import { IReflectionAuthor, AdminAnalyticsData, AuthorAnalyticsData, IAnalytics } from "./data.js";
export interface IReflectionAuthorRaw {
    refId: string;
    timestamp: string;
    pseudonym: string;
    point: string;
    text: string;
    transformData(): IReflectionAuthor;
}
export interface IAdminAnalyticsDataRaw {
    group: string;
    value: IReflectionAuthorRaw[];
    createDate: string;
    transformData(): AdminAnalyticsData;
}
export declare class AdminAnalyticsDataRaw implements IAdminAnalyticsDataRaw {
    group: string;
    value: IReflectionAuthorRaw[];
    createDate: string;
    constructor(group: string, value: IReflectionAuthorRaw[], createDate: string);
    transformData(): AdminAnalyticsData;
}
export interface IAuthorEntriesRaw {
    pseudonym: string;
    reflections: IReflectionAuthorRaw[];
}
export interface IAuthorAnalyticsEntriesRaw {
    pseudonym: string;
    analytics: IAnalytics;
}
export interface IAuthorAnalyticsDataRaw {
    pseudonym: string;
    reflections: IReflectionAuthorRaw[];
    analytics: IAnalytics;
    transformData(): AuthorAnalyticsData;
}
export declare class AuthorAnalyticsDataRaw implements IAuthorAnalyticsDataRaw {
    pseudonym: string;
    reflections: IReflectionAuthorRaw[];
    analytics: IAnalytics;
    constructor(entries: IReflectionAuthorRaw[], analytics: IAuthorAnalyticsEntriesRaw);
    transformData(colourScale?: Function): AuthorAnalyticsData;
}
