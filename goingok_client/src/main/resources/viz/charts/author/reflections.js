var d3 = require("d3");
import { Sort } from "../../interactions/sort.js";
export class Reflections {
    constructor(data) {
        this.id = "reflections";
        this.sort = new Sort("sort", "timestamp");
        this.data = data;
    }
    get nodes() {
        return this._nodes;
    }
    set nodes(nodes) {
        this._nodes = nodes;
        this.fillNodesText();
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        this._data = entries.map(c => {
            c.nodes = c.nodes.filter(d => d.selected);
            return c;
        });
        this.render();
        this.extend !== undefined ? this.extend() : null;
    }
    render() {
        const _this = this;
        d3.select(`#${_this.id} .card-subtitle`)
            .html(_this.data.length == 1 ? `Filtering by <span class="badge badge-pill badge-info">${_this.data[0].timestamp.toDateString()} <i class="fas fa-window-close"></i></span>` :
            "");
        d3.select(`#${_this.id} .reflections-tab`)
            .selectAll(".reflection")
            .data(_this.data)
            .join(enter => enter.append("div")
            .attr("id", d => `ref-${d.refId}`)
            .attr("class", "reflection")
            .call(div => div.append("p")
            .attr("class", "reflection-text")
            .html(d => _this.text(d))), update => update.attr("id", d => `ref-${d.refId}`)
            .select("p")
            .html(d => _this.text(d)), exit => exit.remove());
        _this.handleSort();
    }
    text(data) {
        let html = `<i>${data.timestamp.toDateString()} | Point: ${data.point}</i><br>`;
        for (var i = 0; i < data.text.length; i++) {
            const isOpenTag = data.nodes.find(c => c.startIdx === i);
            const isCloseTag = data.nodes.find(c => c.endIdx === i);
            if (isOpenTag !== undefined) {
                html += `<span id="node-${isOpenTag.idx}" class="badge badge-pill" style="border: 2px solid ${isOpenTag.properties["color"]}">${data.text[i]}`;
            }
            else if (isCloseTag !== undefined) {
                html += `${data.text[i]}</span>`;
            }
            else {
                html += data.text[i];
            }
        }
        return html;
    }
    fillNodesText() {
        d3.selectAll(`#${this.id} .reflections-tab div`)
            .filter(c => this.nodes.map(d => d.refId).includes(c.refId))
            .selectAll("span")
            .each((c, i, g) => {
            let node = this.nodes.find(r => r.idx === parseInt(d3.select(g[i]).attr("id").replace("node-", "")));
            if (node !== undefined) {
                d3.select(g[i]).style("background-color", node.properties["color"]);
            }
        });
    }
    handleSort() {
        const _this = this;
        const id = "sort";
        d3.selectAll(`#${id} .btn-group-toggle label`).on("click", function () {
            const selectedOption = this.control.value;
            _this.sort.sortBy = selectedOption;
            _this._data = _this.sort.sortData(_this.data);
            _this.render();
            if (_this.nodes !== undefined)
                _this.fillNodesText();
        });
    }
    ;
}
