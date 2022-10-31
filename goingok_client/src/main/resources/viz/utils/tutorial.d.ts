var d3 = require("d3");
export interface ITutorialData {
    id: string;
    content: string;
}
export declare class TutorialData implements ITutorialData {
    id: string;
    content: string;
    constructor(id: string, content: string);
}
export interface ITutorial {
    tutorial: d3.Selection<HTMLDivElement, unknown, HTMLElement, any>;
    tutorialData: ITutorialData[];
    slide: number;
    appendTutorial(id: string): d3.Selection<HTMLDivElement, unknown, HTMLElement, any>;
    removeTutorial(): void;
}
export declare class Tutorial implements ITutorial {
    tutorial: d3.Selection<HTMLDivElement, unknown, HTMLElement, any>;
    tutorialData: ITutorialData[];
    slide: number;
    constructor(data: ITutorialData[]);
    appendTutorial(): d3.Selection<HTMLDivElement, unknown, HTMLElement, any>;
    private appendTutorialBackdrop;
    private appendTutorialContent;
    private drawArrow;
    removeTutorial(): void;
}
