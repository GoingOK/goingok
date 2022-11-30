export class Loading {
    constructor(chart) {
        this.chart = chart;
    }
    get isLoading() {
        return this._isLoading;
    }
    set isLoading(loading) {
        this._isLoading = loading;
        if (this.isLoading) {
            this.spinner = this.appendDiv();
        }
        else {
            this.removeDiv();
        }
    }
    appendDiv() {
        if (document.querySelector(`#${this.chart.id} .loader`) !== null)
            return;
        let wrapper = document.querySelector(`#${this.chart.id} .chart-container`);
        let loader = document.createElement("div");
        loader.setAttribute("class", "loader");
        loader.style.height = `${this.chart.elements.contentContainer.node().getBoundingClientRect().height}px`;
        loader.style.width = `${this.chart.elements.contentContainer.node().getBoundingClientRect().width}px`;
        loader.style.top = `${this.chart.padding.top}px`;
        loader.style.left = `${this.chart.padding.yAxis}px`;
        let inner = document.createElement("div");
        inner.setAttribute("class", "loader-inner");
        for (var i = 0; i < 5; i++) {
            let wrap = document.createElement("div");
            wrap.setAttribute("class", "loader-line-wrap");
            let line = document.createElement("div");
            line.setAttribute("class", "loader-line");
            wrap.appendChild(line);
            inner.appendChild(wrap);
        }
        loader.appendChild(inner);
        let cancel = document.createElement("button");
        cancel.setAttribute("class", "btn btn-sm btn-danger cancel-loading");
        cancel.innerHTML = `<i class="fas fa-window-close"></i>`;
        cancel.addEventListener("click", () => this.isLoading = false);
        loader.appendChild(cancel);
        wrapper.appendChild(loader);
        return loader;
    }
    removeDiv() {
        this.spinner.remove();
    }
}
