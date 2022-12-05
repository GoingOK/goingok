var d3 = require("d3");
export class TutorialData {
    constructor(id, content) {
        this.id = id;
        this.content = content;
    }
}
export class Tutorial {
    constructor(data) {
        this.tutorial = this.appendTutorial();
        this.tutorialData = data;
        this.slide = 0;
        this.appendTutorialBackdrop();
    }
    appendTutorial() {
        d3.select("body")
            .classed("no-overflow", true);
        let div = d3.select(".wrapper")
            .append("div")
            .attr("class", "tutorial");
        return div;
    }
    ;
    appendTutorialBackdrop() {
        if (this.slide >= this.tutorialData.length) {
            this.removeTutorial();
            return;
        }
        window.scroll(0, 0);
        let tutorialData = this.tutorialData[this.slide];
        let tutorialFocus = d3.select(tutorialData.id).node().getBoundingClientRect();
        class TutorialContentData {
            constructor(top, left, width, height) {
                this.top = top;
                this.left = left;
                this.width = width;
                this.height = height;
            }
        }
        window.scroll(0, tutorialFocus.top - 200);
        let data = [new TutorialContentData("0px", "0px", "100%", tutorialFocus.top + "px"),
            new TutorialContentData(tutorialFocus.bottom + "px", "0px", "100%", "100%"),
            new TutorialContentData(tutorialFocus.top + "px", "0px", tutorialFocus.left + "px", tutorialFocus.height + "px"),
            new TutorialContentData(tutorialFocus.top + "px", tutorialFocus.right + "px", "100%", tutorialFocus.height + "px")];
        this.tutorial.selectAll(".tutorial-backdrop")
            .data(data)
            .join(enter => enter.append("div")
            .attr("class", "tutorial-backdrop")
            .style("top", d => d.top)
            .style("left", d => d.left)
            .style("width", d => d.width)
            .style("height", d => d.height), update => update.style("top", d => d.top)
            .style("left", d => d.left)
            .style("width", d => d.width)
            .style("height", d => d.height), exit => exit.remove());
        this.appendTutorialContent(tutorialFocus, tutorialData.content);
    }
    ;
    appendTutorialContent(tutorialFocus, content) {
        let isLeft = true;
        let left = tutorialFocus.left + tutorialFocus.width - 200;
        if (tutorialFocus.left + 50 > window.innerWidth / 2) {
            isLeft = false;
        }
        if (tutorialFocus.left < window.innerWidth / 4) {
            left = left + 250;
        }
        if (tutorialFocus.left > window.innerWidth * 3 / 4) {
            left = left - 200;
        }
        if (this.tutorial.selectAll(".tutorial-content").empty()) {
            this.tutorial.append("div")
                .attr("class", "tutorial-content")
                .style("top", (tutorialFocus.top - 50) + "px")
                .style("left", left + "px")
                .call(div => div.append("div")
                .attr("class", "row")
                .call(div => div.append("div")
                .attr("class", "col-md-12")
                .html(content))
                .call(div => div.append("div")
                .attr("class", "col-md-5 mt-2"))
                .call(div => div.append("div")
                .attr("class", "col-md-7 d-flex mt-2")
                .call(div => div.append("button")
                .attr("class", "btn btn-success d-block w-50 mx-1")
                .html("Next")
                .on("click", () => { this.slide = this.slide + 1; this.appendTutorialBackdrop(); }))
                .call(div => div.append("button")
                .attr("class", "btn btn-warning d-block w-50 mx-1")
                .html("Skip")
                .on("click", () => this.removeTutorial()))));
            if (!isLeft) {
                this.tutorial.select(".tutorial-content")
                    .style("left", tutorialFocus.left - this.tutorial.select(".tutorial-content").node().getBoundingClientRect().width - 50 + "px");
            }
            this.drawArrow(tutorialFocus, isLeft);
        }
        else {
            this.tutorial.select(".tutorial-content")
                .style("top", (tutorialFocus.top - 50) + "px")
                .style("left", isLeft ? tutorialFocus.left + tutorialFocus.width + 50 + "px" :
                tutorialFocus.left - this.tutorial.select(".tutorial-content").node().getBoundingClientRect().width - 50 + "px");
            this.tutorial.select(".col-md-12")
                .html(content);
            this.tutorial.select(".tutorial-arrow").remove();
            this.drawArrow(tutorialFocus, isLeft);
        }
    }
    ;
    drawArrow(tutorialFocus, isLeft) {
        let tutorialArrow = this.tutorial.append("div")
            .attr("class", "tutorial-arrow")
            .style("top", (tutorialFocus.top - 50) + "px")
            .style("left", isLeft ? tutorialFocus.left + (tutorialFocus.width / 4) + "px" :
            this.tutorial.select(".tutorial-content").node().getBoundingClientRect().left + this.tutorial.select(".tutorial-content").node().getBoundingClientRect().width + "px")
            .style("width", (tutorialFocus.width / 4 * 3) + 50 + "px")
            .style("height", "50px");
        let svg = tutorialArrow.append("svg")
            .attr("viewBox", `0 0 ${tutorialArrow.node().getBoundingClientRect().width} ${tutorialArrow.node().getBoundingClientRect().height}`);
        svg.append("defs")
            .append("marker")
            .attr("id", "arrow-head")
            .attr("markerWidth", 5)
            .attr("markerHeight", 5)
            .attr("refX", 2)
            .attr("refY", 2)
            .attr("orient", "auto")
            .append("path")
            .attr("d", "M0,0 L0,4 L4,2 L0,0")
            .attr("class", "arrow-head");
        let xScale = d3.scaleLinear()
            .domain([0, 100])
            .range([0, tutorialArrow.node().getBoundingClientRect().width]);
        let yScale = d3.scaleLinear()
            .domain([100, 0])
            .range([0, tutorialArrow.node().getBoundingClientRect().height]);
        let pathGenerator = d3.line()
            .x(d => xScale(d.x))
            .y(d => yScale(d.y))
            .curve(d3.curveCatmullRom);
        svg.append("path")
            .attr("d", isLeft ? pathGenerator([{ x: 95, y: 80 }, { x: 25, y: 70 }, { x: 25, y: 25 }]) : pathGenerator([{ x: 0, y: 80 }, { x: 75, y: 70 }, { x: 75, y: 25 }]))
            .attr("class", "arrow")
            .attr("marker-end", "url(#arrow-head)");
    }
    ;
    removeTutorial() {
        d3.select("body")
            .classed("no-overflow", false);
        window.scroll(0, 0);
        this.tutorial.remove();
    }
    ;
}
