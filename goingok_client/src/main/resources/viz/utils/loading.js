export class Loading {
    constructor() {
        this.isLoading = true;
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
        let wrapper = document.querySelector(".wrapper");
        let loader = document.createElement("div");
        loader.setAttribute("class", "loader");
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
        cancel.setAttribute("class", "btn btn-danger cancel-loading");
        cancel.innerText = "Cancel";
        cancel.addEventListener("click", () => this.isLoading = false);
        loader.appendChild(cancel);
        wrapper.appendChild(loader);
        return loader;
    }
    removeDiv() {
        this.spinner.remove();
    }
}
