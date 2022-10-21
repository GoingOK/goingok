var d3 = require("d3");
export class Totals {
    constructor(data) {
        this.data = data;
    }
    get data() {
        return this._data;
    }
    set data(entries) {
        this._data = entries.filter(d => d.selected);
        this.render();
    }
    render() {
        let _this = this;
        let users = d3.select("#users-total .card-title span").datum();
        d3.select("#users-total .card-title span")
            .datum(d3.sum(_this.data.map(d => d.getStat("usersTotal").value)))
            .transition()
            .duration(1000)
            .tween("html", function () {
            let oldUsers = users == undefined ? 0 : users;
            let newUsers = d3.sum(_this.data.map(d => d.getStat("usersTotal").value));
            return function (t) {
                if (oldUsers < newUsers) {
                    this.innerHTML = (oldUsers + Math.round(t * (newUsers - oldUsers))).toString();
                }
                else {
                    this.innerHTML = (oldUsers - Math.round(t * (oldUsers - newUsers))).toString();
                }
            };
        });
        let refs = d3.select("#ref-total .card-title span").datum();
        d3.select("#ref-total .card-title span")
            .datum(d3.sum(_this.data.map(d => d.getStat("refTotal").value)))
            .transition()
            .duration(1000)
            .tween("html", function () {
            let oldRefs = refs == undefined ? 0 : refs;
            let newRefs = d3.sum(_this.data.map(d => d.getStat("refTotal").value));
            return function (t) {
                if (oldRefs < newRefs) {
                    this.innerHTML = (oldRefs + Math.round(t * (newRefs - oldRefs))).toString();
                }
                else {
                    this.innerHTML = (oldRefs - Math.round(t * (oldRefs - newRefs))).toString();
                }
            };
        });
        let ruRate = d3.select("#ru-rate .card-title span").datum();
        d3.select("#ru-rate .card-title span")
            .datum(_this.data.length != 0 ? Math.round(d3.mean(_this.data.map(d => d.getStat("ruRate").value * 100))) / 100 : 0)
            .transition()
            .duration(1000)
            .tween("html", function () {
            let oldRURate = ruRate == undefined ? 0 : ruRate;
            let newRURate = _this.data.length != 0 ? Math.round(d3.mean(_this.data.map(d => d.getStat("ruRate").value * 100))) / 100 : 0;
            return function (t) {
                if (oldRURate < newRURate) {
                    this.innerHTML = (oldRURate + (t * (newRURate - oldRURate))).toFixed(2);
                }
                else {
                    this.innerHTML = (oldRURate - (t * (oldRURate - newRURate))).toFixed(2);
                }
            };
        });
    }
    ;
}
