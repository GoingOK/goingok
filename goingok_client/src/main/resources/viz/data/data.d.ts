export interface IReflection {
    refId: number;
    timestamp: Date;
    point: number;
    text: string;
}
export interface IReflectionAuthor extends IReflection {
    pseudonym: string;
}
export interface IAdminAnalyticsData {
    group: string;
    value: IReflectionAuthor[];
    creteDate: Date;
    colour: string;
    selected: boolean;
    getUsersData(): AdminAnalyticsData;
}
export declare class AdminAnalyticsData implements IAdminAnalyticsData {
    group: string;
    value: IReflectionAuthor[];
    creteDate: Date;
    colour: string;
    selected: boolean;
    constructor(group: string, value: IReflectionAuthor[], createDate?: Date, colour?: string, selected?: boolean);
    getUsersData(): AdminAnalyticsData;
}
export interface IDataStats {
    stat: string;
    displayName: string;
    value: number | Date;
}
export declare class DataStats implements IDataStats {
    stat: string;
    displayName: string;
    value: number | Date;
    constructor(stat: string, displayName: string, value: number | Date);
}
export interface IAdminAnalyticsDataStats extends IAdminAnalyticsData {
    stats: IDataStats[];
    getStat(stat: string): IDataStats;
}
export declare class AdminAnalyticsDataStats extends AdminAnalyticsData implements IAdminAnalyticsDataStats {
    stats: IDataStats[];
    constructor(entries: IAdminAnalyticsData);
    getStat(stat: string): IDataStats;
}
export interface ITimelineData extends IReflectionAuthor {
    colour: string;
    group: string;
}
export declare class TimelineData implements ITimelineData {
    refId: number;
    timestamp: Date;
    pseudonym: string;
    point: number;
    text: string;
    colour: string;
    group: string;
    constructor(data: IReflectionAuthor, colour: string, group: string);
}
export interface IHistogramData extends IAdminAnalyticsData {
    bin: d3.Bin<number, number>;
    percentage: number;
}
export declare class HistogramData extends AdminAnalyticsData implements IHistogramData {
    bin: d3.Bin<number, number>;
    percentage: number;
    constructor(value: IReflectionAuthor[], group: string, colour: string, bin: d3.Bin<number, number>, percentage: number);
}
export interface IUserChartData {
    binName: string;
    percentage: number;
    value: IReflectionAuthor[];
    isGroup: boolean;
}
export declare class UserChartData implements IUserChartData {
    binName: string;
    percentage: number;
    value: IReflectionAuthor[];
    isGroup: boolean;
    constructor(bin: d3.Bin<number, number>, value: IReflectionAuthor[], percentage: number, isGroup: boolean);
}
export interface IClickTextData {
    clickData: {
        stat: IDataStats | number;
        group: string;
    };
    data: {
        stat: IDataStats | number;
        group: string;
    };
}
export declare class ClickTextData implements IClickTextData {
    clickData: {
        stat: IDataStats | number;
        group: string;
    };
    data: {
        stat: IDataStats | number;
        group: string;
    };
    constructor(clickStat: IDataStats | number, dataStat: IDataStats | number, clickGroup: string, dataGroup: string);
}
export interface INodes extends d3.SimulationNodeDatum {
    idx: number;
    nodeType: string;
    refId: number;
    startIdx?: number;
    endIdx?: number;
    expression: string;
    labelType: string;
    name: string;
    description: string;
    selected?: boolean;
    properties: any;
}
export interface IEdges<T> extends d3.SimulationLinkDatum<T> {
    idx: number;
    edgeType: string;
    directional: boolean;
    weight: number;
    labelType: string;
    name: string;
    description: string;
    selected?: boolean;
    properties: any;
    isReflection?: boolean;
}
export interface IReflectionAnalytics {
    name: string;
    description: string;
    nodes: INodes[];
    edges: IEdges<INodes>[];
}
