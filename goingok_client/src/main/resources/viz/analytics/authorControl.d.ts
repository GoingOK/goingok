var d3 = require("d3");
import { ChartNetwork, ChartTimeNetwork } from "../charts/charts.js";
import { IHelp, Help } from "../charts/help.js";
import { IAuthorControlInteractions, AuthorControlInteractions } from "../charts/interactions.js";
import { IReflectionAuthor, IReflectionAnalytics, IReflection, INodes } from "../data/data.js";
export interface IAuthorControlCharts {
    help: IHelp;
    interactions: IAuthorControlInteractions;
    allNodes: INodes[];
    allEntries: IReflection[];
    resizeTimeline(): void;
    preloadTags(entries: IReflectionAnalytics[], enable?: boolean): INodes[];
    processSimulation(chart: ChartNetwork, data: IReflectionAnalytics): void;
    processTimelineSimulation(chart: ChartTimeNetwork, centerX: number, centerY: number, nodes: INodes[]): void;
    renderTimeline(chart: ChartTimeNetwork, data: IReflection[], analytics: IReflectionAnalytics): ChartTimeNetwork;
    renderNetwork(chart: ChartNetwork, data: IReflectionAnalytics): ChartNetwork;
    renderReflections(data: IReflectionAuthor[]): void;
}
export declare class AuthorControlCharts implements IAuthorControlCharts {
    help: Help;
    interactions: AuthorControlInteractions;
    allNodes: INodes[];
    allEntries: IReflection[];
    resizeTimeline(): void;
    preloadTags(analytics: IReflectionAnalytics[], enable?: boolean): INodes[];
    processSimulation(chart: ChartNetwork, data: IReflectionAnalytics): d3.Simulation<INodes, undefined>;
    processTimelineSimulation(chart: ChartTimeNetwork, centerX: number, centerY: number, nodes: INodes[]): void;
    renderTimeline(chart: ChartTimeNetwork, data: IReflection[], analytics: IReflectionAnalytics): ChartTimeNetwork;
    renderNetwork(chart: ChartNetwork, data: IReflectionAnalytics): ChartNetwork;
    renderReflections(data: IReflection[]): void;
    processReflectionsText(data: IReflection): string;
}
export declare function buildControlAuthorAnalyticsCharts(entriesRaw: IReflection[], analyticsRaw: IReflectionAnalytics[]): Promise<void>;
