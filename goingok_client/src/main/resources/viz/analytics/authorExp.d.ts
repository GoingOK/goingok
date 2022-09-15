import { ChartTimeNetwork, ChartNetwork } from "../charts/charts.js";
import { IAuthorExperimentalInteractions, AuthorExperimentalInteractions } from "../charts/interactions.js";
import { IReflectionAnalytics, IReflection, INodes } from "../data/data.js";
import { AuthorControlCharts, IAuthorControlCharts } from "./authorControl.js";
export interface IAuthorExperimentalCharts extends IAuthorControlCharts {
    interactions: IAuthorExperimentalInteractions;
    allAnalytics: IReflectionAnalytics[];
    timelineChart: ChartTimeNetwork;
    networkChart: ChartNetwork;
    sorted: string;
    handleTags(): void;
    handleTagsColours(): void;
    handleReflectionsSort(): void;
}
export declare class AuthorExperimentalCharts extends AuthorControlCharts implements IAuthorExperimentalCharts {
    interactions: AuthorExperimentalInteractions;
    allAnalytics: IReflectionAnalytics[];
    timelineChart: ChartTimeNetwork;
    networkChart: ChartNetwork;
    sorted: string;
    preloadTags(entries: IReflectionAnalytics[], enable?: boolean): INodes[];
    handleTags(): void;
    handleTagsColours(): void;
    handleReflectionsSort(): void;
    handleFilterButton(): void;
    private getUpdatedAnalyticsData;
    private getUpdatedNetworkData;
    renderTimeline(chart: ChartTimeNetwork, data: IReflection[], analytics: IReflectionAnalytics): ChartTimeNetwork;
    renderNetwork(chart: ChartNetwork, data: IReflectionAnalytics): ChartNetwork;
    renderReflections(data: IReflection[]): void;
}
export declare function buildExperimentAuthorAnalyticsCharts(entriesRaw: IReflection[], analyticsRaw: IReflectionAnalytics[]): Promise<void>;
