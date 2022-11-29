import { IAnalytics, IAuthorAnalyticsData, ITags, IReflectionAnalytics } from "../data/data.js";
import { Dashboard } from "./authorControl.js";
import { IAuthorAnalyticsEntriesRaw, IAuthorEntriesRaw } from "../data/db.js";
export declare class ExperimentalDashboard extends Dashboard {
    tags: ITags[];
    reflectionAnalytics: IReflectionAnalytics[];
    analytics: IAnalytics;
    constructor(data: IAuthorAnalyticsData[]);
    handleMultiUser(entries: IAuthorAnalyticsData[]): void;
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
export declare function buildExperimentAuthorAnalyticsCharts(entriesRaw: IAuthorEntriesRaw[], analyticsRaw: IAuthorAnalyticsEntriesRaw[]): Promise<void>;
