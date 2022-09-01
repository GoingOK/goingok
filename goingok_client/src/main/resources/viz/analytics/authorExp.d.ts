import { ChartTimeNetwork, ChartNetwork } from "../charts/charts.js";
import { IAuthorExperimentalInteractions, AuthorExperimentalInteractions } from "../charts/interactions.js";
import { IRelfectionAuthorAnalytics, INetworkData, ITags, IReflectionAnalytics, IReflectionAuthor } from "../data/data.js";
import { AuthorControlCharts, IAuthorControlCharts } from "./authorControl.js";
export interface IAuthorExperimentalCharts extends IAuthorControlCharts {
    interactions: IAuthorExperimentalInteractions;
    allEntries: IRelfectionAuthorAnalytics[];
    allNetworkData: INetworkData;
    allTags: ITags[];
    timelineChart: ChartTimeNetwork;
    networkChart: ChartNetwork;
    sorted: string;
    handleTags(): void;
    handleTagsColours(): void;
    handleReflectionsSort(): void;
}
export declare class AuthorExperimentalCharts extends AuthorControlCharts implements IAuthorExperimentalCharts {
    interactions: AuthorExperimentalInteractions;
    allEntries: IRelfectionAuthorAnalytics[];
    allNetworkData: INetworkData;
    allTags: ITags[];
    timelineChart: ChartTimeNetwork;
    networkChart: ChartNetwork;
    sorted: string;
    preloadTags(entries: IRelfectionAuthorAnalytics[], enable?: boolean): ITags[];
    handleTags(): void;
    handleTagsColours(): void;
    handleReflectionsSort(): void;
    handleFilterButton(): void;
    private getUpdatedEntriesData;
    private getUpdatedNetworkData;
    renderTimeline(chart: ChartTimeNetwork, data: IRelfectionAuthorAnalytics[]): ChartTimeNetwork;
    renderNetwork(chart: ChartNetwork, data: INetworkData): ChartNetwork;
    renderReflections(data: IRelfectionAuthorAnalytics[]): void;
}
export declare function buildExperimentAuthorAnalyticsCharts(entriesRaw: IReflectionAuthor[], analyticsRaw: IReflectionAnalytics[]): Promise<void>;
