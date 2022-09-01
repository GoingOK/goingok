export class Loading {
    constructor() {
        this.isLoading = true;
        this.spinner = this.appendDiv();
    }
    appendDiv() {
        let wrapper = document.querySelector(".wrapper");
        let loader = document.createElement("div");
        loader.setAttribute("class", "loader");
        wrapper.appendChild(loader);
        let inner = document.createElement("div");
        inner.setAttribute("class", "loader-inner");
        for (var i = 0; i < 5; i++) {
            let wrap = document.createElement("div");
            wrap.setAttribute("class", "loader-line-wrap");
            let line = document.createElement("div");
            line.setAttribute("class", "loader-line-wrap");
            wrap.appendChild(line);
            inner.appendChild(wrap);
        }
        return loader;
    }
    removeDiv() {
        this.spinner.remove();
    }
}
