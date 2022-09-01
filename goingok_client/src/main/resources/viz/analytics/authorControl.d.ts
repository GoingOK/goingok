var d3 = require("d3");
import { ChartNetwork, ChartTimeNetwork } from "../charts/charts.js";
import { IHelp, Help } from "../charts/help.js";
import { IAuthorControlInteractions, AuthorControlInteractions } from "../charts/interactions.js";
import { IRelfectionAuthorAnalytics, ITags, INetworkData, IReflectionAuthor, IReflectionAnalytics } from "../data/data.js";
export interface IAuthorControlCharts {
    help: IHelp;
    interactions: IAuthorControlInteractions;
    preloadTags(entries: IRelfectionAuthorAnalytics[], enable?: boolean): ITags[];
    processNetworkData(chart: ChartNetwork, entries: IRelfectionAuthorAnalytics[]): INetworkData;
    processSimulation(chart: ChartNetwork, data: INetworkData): void;
    processTimelineSimulation(chart: ChartTimeNetwork, centerX: number, centerY: number, nodes: ITags[]): void;
    renderTimeline(chart: ChartTimeNetwork, data: IReflectionAuthor[]): ChartTimeNetwork;
    renderNetwork(chart: ChartNetwork, data: INetworkData): ChartNetwork;
    renderReflections(data: IReflectionAuthor[]): void;
}
export declare class AuthorControlCharts implements IAuthorControlCharts {
    help: Help;
    interactions: AuthorControlInteractions;
    preloadTags(entries: IRelfectionAuthorAnalytics[], enable?: boolean): ITags[];
    processNetworkData(chart: ChartNetwork, entries: IRelfectionAuthorAnalytics[]): INetworkData;
    processSimulation(chart: ChartNetwork, data: INetworkData): d3.Simulation<ITags, undefined>;
    processTimelineSimulation(chart: ChartTimeNetwork, centerX: number, centerY: number, nodes: ITags[]): void;
    renderTimeline(chart: ChartTimeNetwork, data: IRelfectionAuthorAnalytics[]): ChartTimeNetwork;
    renderNetwork(chart: ChartNetwork, data: INetworkData): ChartNetwork;
    renderReflections(data: IRelfectionAuthorAnalytics[]): void;
    processReflectionsText(data: IRelfectionAuthorAnalytics): string;
}
export declare function buildControlAuthorAnalyticsCharts(entriesRaw: IReflectionAuthor[], analyticsRaw: IReflectionAnalytics[]): Promise<void>;
