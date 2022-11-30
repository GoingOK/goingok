import { IAuthorAnalyticsData, ITags } from "../data/data.js";
import { Network } from "../charts/author/network.js";
import { TimelineNetwork } from "../charts/author/timelineNetwork.js";
import { IAuthorAnalyticsEntriesRaw, IAuthorEntriesRaw } from "../data/db.js";
import { Reflections } from "../charts/author/reflections.js";
export declare class Dashboard {
    timeline: TimelineNetwork;
    network: Network;
    reflections: Reflections;
    constructor(data: IAuthorAnalyticsData[]);
    renderError(e: any, chartId: string): void;
    resizeTimeline(): void;
    handleMultiUser(entries: IAuthorAnalyticsData[], extend?: Function): void;
    preloadTags(entries: IAuthorAnalyticsData, enable?: boolean): ITags[];
}
export declare function buildControlAuthorAnalyticsCharts(entriesRaw: IAuthorEntriesRaw[], analyticsRaw: IAuthorAnalyticsEntriesRaw[]): void;
