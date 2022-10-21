import { IAnalytics, IAuthorAnalyticsData, ITags, IReflectionAnalytics } from "../data/data.js";
import { Dashboard } from "./authorControl.js";
import { IReflectionAuthorRaw } from "../data/db.js";
import { Help } from "../utils/help.js";
import { Sort } from "../interactions/sort.js";
export declare class ExperimentalDashboard extends Dashboard {
    tags: ITags[];
    reflectionAnalytics: IReflectionAnalytics[];
    analytics: IAnalytics;
    sorted: string;
    sort: Sort;
    help: Help;
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
}
export declare function buildExperimentAuthorAnalyticsCharts(entriesRaw: IReflectionAuthorRaw[], analyticsRaw: IAnalytics): Promise<void>;
