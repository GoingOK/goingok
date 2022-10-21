var d3 = require("d3");
import { Sort } from "../../interactions/sort.js";
import { calculateMean, groupBy, maxDate, minDate } from "../../utils/utils.js";
export class Users {
    constructor(data) {
        this.sorted = "";
        this.sort = new Sort();
        this._thresholds = [30, 70];
        this.id = "reflections";
        this.data = data;
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        this._data = entries.filter(d => d.selected);
        this.sort.setChevronVisibility("sort-users", "name");
        this.sort.handleChevronChange("sort-users", "name", "fa-chevron-down");
        this.render();
    }
    get thresholds() {
        return this._thresholds;
    }
    set thresholds(thresholds) {
        this._thresholds = thresholds;
        d3.selectAll(`#${this.id} meter`)
            .attr("low", this.thresholds[0])
            .attr("high", this.thresholds[1]);
    }
    render() {
        const _this = this;
        d3.select(`#${_this.id} .nav.nav-tabs`).selectAll("li")
            .data(_this.data)
            .join(enter => enter.append("li")
            .attr("class", "nav-item")
            .append("a")
            .attr("class", (d, i) => `nav-link ${i == 0 ? "active" : ""}`)
            .html(d => d.group)
            .on("click", function (e, d) {
            d3.select(`#${_this.id} .nav.nav-tabs`).selectAll("a")
                .classed("active", false);
            d3.select(this).classed("active", true);
            setTimeout(() => _this.renderTabContent(d.value));
        }), update => update.select("a")
            .classed("active", (d, i) => i == 0)
            .html(d => d.group), exit => exit.remove());
        _this.handleSort(_this.data[0].value);
        _this.renderTabContent(_this.data[0].value);
    }
    renderTabContent(data, groupByData) {
        const _this = this;
        const usersStats = groupBy(data, "pseudonym").map(d => {
            return {
                "pseudonym": d.key,
                "mean": calculateMean(d.value.map(c => c.point)),
                "total": d.value.length,
                "minDate": minDate(d.value.map(c => c.timestamp)),
                "maxDate": maxDate(d.value.map(c => c.timestamp))
            };
        });
        const minUser = usersStats.sort((a, b) => a.mean - b.mean)[0];
        const maxUser = usersStats.sort((a, b) => a.mean - b.mean)[usersStats.length - 1];
        d3.select(`#${_this.id} .text-muted`)
            .html(usersStats.length === 1 ? `The user ${usersStats[0].pseudonym} has a total of ${usersStats[0].total} reflections between
                ${usersStats[0].minDate.toDateString()} and ${usersStats[0].maxDate.toDateString()}` :
            `The user ${minUser.pseudonym} is the most distressed, while the user ${maxUser.pseudonym} is the most soaring`);
        d3.select(`#${_this.id} .tab-content`)
            .selectAll("div .statistics-text")
            .data(groupByData === undefined ? d3.sort(groupBy(data, "pseudonym"), c => c.key) : groupByData)
            .join(enter => enter.append("div")
            .attr("class", "row statistics-text")
            .attr("id", d => d.key)
            .call(div => div.append("div")
            .attr("class", "col-md-4")
            .call(div => div.append("h5")
            .attr("class", "mb-0 mt-1")
            .html(d => `${d.key} <small>average is</small>`))
            .call(div => div.append(d => _this.renderUserMeter(d)))
            .call(div => div.append("div")
            .attr("class", "mt-2")
            .append("h6")
            .html("Percentage of reflections"))
            .call(div => div.append("div")
            .attr("class", "w-100 mt-1 user-chart")))
            .call(div => div.append("div")
            .attr("class", "col-md-8")
            .append("p")
            .attr("class", "mb-1")
            .call(p => p.append("span")
            .html(d => `User ${d.key} reflections in chronological order:`))
            .call(p => p.append("ul")
            .attr("class", "pr-3")
            .call(ul => _this.renderReflections(ul)))), update => update.attr("id", d => d.key)
            .call(update => update.select("h5")
            .html(d => `${d.key} is`))
            .call(update => update.select("meter")
            .attr("value", d => calculateMean(d.value.map(c => c.point))))
            .call(update => update.select("p span")
            .html(d => `User ${d.key} reflections in chronological order:`))
            .call(update => _this.renderReflections(update.select("p ul"))), exit => exit.remove());
    }
    renderReflections(update) {
        update.selectAll("li")
            .data(d => d3.sort(d.value, r => r.timestamp))
            .join(enter => enter.append("li")
            .classed("reflection-selected", d => d.selected)
            .html(d => `<i>${d.timestamp.toDateString()} | Reflection state point ${d.point}</i><br> ${d.text}`), update => update.classed("reflection-selected", d => d.selected)
            .html(d => `<i>${d.timestamp.toDateString()} | Reflection state point ${d.point}</i><br> ${d.text}`), exit => exit.remove());
    }
    handleSort(data) {
        const _this = this;
        const id = "sort-users";
        let sortedData = groupBy(data, "pseudonym");
        d3.selectAll(`#${id} .btn-group-toggle label`).on("click", function () {
            const selectedOption = this.control.value;
            const chevron = _this.sorted === selectedOption ? "fa-chevron-down" : "fa-chevron-up";
            _this.sort.setChevronVisibility(id, selectedOption);
            sortedData = sortedData.sort(function (a, b) {
                if (selectedOption == "name") {
                    _this.sort.handleChevronChange(id, selectedOption, chevron);
                    return _this.sort.sortData(a.key, b.key, _this.sorted == "name" ? true : false);
                }
                else if (selectedOption == "point") {
                    _this.sort.handleChevronChange(id, selectedOption, chevron);
                    return _this.sort.sortData(calculateMean(a.value.map(d => d.point)), calculateMean(b.value.map(d => d.point)), _this.sorted == "point" ? true : false);
                }
            });
            _this.sorted = _this.sort.setSorted(_this.sorted, selectedOption);
            _this.renderTabContent(data, sortedData);
        });
    }
    ;
    renderUserMeter(data) {
        const row = document.createElement("div");
        row.classList.add("row", "mt-1");
        const scale = meterScale();
        row.appendChild(scale[0]);
        row.appendChild(scale[1]);
        row.appendChild(scale[2]);
        const meterCol = document.createElement("div");
        meterCol.classList.add("col-12");
        const meter = document.createElement("meter");
        meter.classList.add("w-100", "user-meter");
        meter.setAttribute("max", "100");
        meter.setAttribute("value", calculateMean(data.value.map(d => d.point)).toString());
        meter.setAttribute("low", this.thresholds[0].toString());
        meter.setAttribute("optimum", "99.99");
        meter.setAttribute("high", this.thresholds[1].toString());
        meterCol.appendChild(meter);
        row.appendChild(meterCol);
        return row;
        function meterScale() {
            const labels = ["distressed", "going ok", "soaring"];
            let scale = [];
            labels.forEach(c => {
                let label = meterLabel(c);
                c === "going ok" ? label.classList.add("text-center") : null;
                c === "soaring" ? label.classList.add("text-right") : null;
                scale.push(label);
            });
            return scale;
        }
        function meterLabel(name) {
            let col = document.createElement("div");
            col.classList.add("col-4", "xx-small");
            col.innerHTML = name;
            return col;
        }
    }
}
