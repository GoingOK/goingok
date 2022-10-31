export interface IReflection {
    refId: number;
    timestamp: Date;
    point: number;
    text: string;
}
export interface IReflectionAuthor extends IReflection {
    pseudonym: string;
    selected: boolean;
}
export interface IAdminAnalyticsData {
    group: string;
    value: IReflectionAuthor[];
    createDate: Date;
    colour: string;
    selected: boolean;
    usersTotal: number;
    refTotal: number;
    ruRate: number;
    mean: number;
}
export declare class AdminAnalyticsData implements IAdminAnalyticsData {
    group: string;
    value: IReflectionAuthor[];
    createDate: Date;
    colour: string;
    selected: boolean;
    usersTotal: number;
    refTotal: number;
    ruRate: number;
    mean: number;
    constructor(group: string, value: IReflectionAuthor[], createDate?: Date, colour?: string, selected?: boolean);
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
    selected: boolean;
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
export interface IAnalytics {
    name: string;
    description: string;
    nodes: INodes[];
    edges: IEdges<INodes>[];
}
export interface IReflectionAnalytics extends IReflection {
    nodes: INodes[];
}
export interface ITags {
    name: string;
    properties: any;
    selected?: boolean;
}
export interface IAuthorAnalyticsData {
    reflections: IReflectionAnalytics[];
    analytics: IAnalytics;
}
export declare class AuthorAnalyticsData implements IAuthorAnalyticsData {
    reflections: IReflectionAnalytics[];
    analytics: IAnalytics;
    constructor(reflections: IReflection[], analytics: IAnalytics, colourScale: Function);
    private processColour;
}
