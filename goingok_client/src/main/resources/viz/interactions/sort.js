export class Sort {
    constructor(id, sortBy) {
        this.asc = false;
        this.id = id;
        this.sortBy = sortBy;
    }
    get sortBy() {
        return this._sortBy;
    }
    set sortBy(sortBy) {
        this.setAsc(sortBy);
        this._sortBy = sortBy;
        this.setChevronVisibility();
        this.handleChevronChange();
    }
    sortData(data) {
        return data.sort((a, b) => {
            return this.sortFunction(a[this.sortBy], b[this.sortBy]);
        });
    }
    ;
    sortFunction(a, b) {
        if (a < b) {
            if (this.asc) {
                return 1;
            }
            else {
                return -1;
            }
        }
        if (a > b) {
            if (this.asc) {
                return -1;
            }
            else {
                return 1;
            }
        }
        return 0;
    }
    setAsc(sortBy) {
        this.asc = this.sortBy === sortBy ? !this.asc : false;
    }
    setChevronVisibility() {
        const parentEl = this.getParentEl();
        if (parentEl === undefined) {
            return;
        }
        document.querySelectorAll(`#${this.id} .sort-by i`).forEach(c => c.classList.add("d-none"));
        document.querySelectorAll(`#${this.id} .sort-by label`).forEach(c => c.classList.remove("active"));
        parentEl.querySelector("i").classList.remove("d-none");
        parentEl.classList.add("active");
    }
    handleChevronChange() {
        const parentEl = this.getParentEl();
        if (parentEl === undefined) {
            return;
        }
        parentEl.querySelector("i").classList.remove("fa-chevron-down", "fa-chevron-up");
        parentEl.querySelector("i").classList.add(this.asc ? "fa-chevron-up" : "fa-chevron-down");
    }
    getParentEl() {
        var _a;
        return (_a = document.querySelector(`#${this.id} .sort-by input[value="${this.sortBy}"]`)) === null || _a === void 0 ? void 0 : _a.parentElement;
    }
}
