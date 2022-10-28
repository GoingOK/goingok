import { IAnalytics, IAuthorAnalyticsData, ITags, IReflectionAnalytics } from "../data/data.js";
import { Dashboard } from "./authorControl.js";
import { IReflectionAuthorRaw } from "../data/db.js";
export declare class ExperimentalDashboard extends Dashboard {
    tags: ITags[];
    reflectionAnalytics: IReflectionAnalytics[];
    analytics: IAnalytics;
    constructor(data: IAuthorAnalyticsData);
    preloadTags(entries: IAuthorAnalyticsData): ITags[];
    handleTags(): void;
    handleTagsColours(): void;
    extendTimeline(): void;
    extendNetwork(): void;
    extendReflections(): void;
    private handleFilterButton;
    private updateReflectionNodesData;
    private updateAnalyticsData;
    private getClickTimelineNetworkData;
    private getClickTimelineNetworkNodes;
}
export declare function buildExperimentAuthorAnalyticsCharts(entriesRaw: IReflectionAuthorRaw[], analyticsRaw: IAnalytics): Promise<void>;
