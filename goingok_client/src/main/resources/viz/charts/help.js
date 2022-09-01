export class Help {
    helpPopover(button, id, content) {
        if (document.querySelector(`#${id}`) === null) {
            let popover = document.createElement("div");
            popover.setAttribute("id", id);
            popover.setAttribute("class", "popover fade bs-popover-left show");
            popover.style.top = `${window.pageYOffset + button.node().getBoundingClientRect().top}px`;
            document.querySelector("body").appendChild(popover);
            let arrow = document.createElement("div");
            arrow.setAttribute("class", "arrow");
            arrow.style.top = "6px";
            popover.appendChild(arrow);
            let popoverBody = document.createElement("div");
            popoverBody.setAttribute("class", "popover-body");
            popoverBody.innerHTML = content;
            popover.appendChild(popoverBody);
            if (button.node().getBoundingClientRect().left - popover.getBoundingClientRect().width > 0) {
                popover.style.left = "left", `${button.node().getBoundingClientRect().left - popover.getBoundingClientRect().width}px`;
            }
            else {
                popover.style.left = "left", `${button.node().getBoundingClientRect().right}px`;
                popover.setAttribute("class", "popover fade bs-popover-right show");
            }
            button.select("i")
                .attr("class", "fas fa-window-close");
            return true;
        }
        else {
            document.querySelector(`#${id}`).remove();
            button.select("i")
                .attr("class", "fas fa-question-circle");
            return false;
        }
    }
    ;
    removeHelp(chart) {
        var _a, _b, _c, _d, _e;
        (_a = document.querySelector(`#${chart.id}-help`)) === null || _a === void 0 ? void 0 : _a.remove();
        (_b = document.querySelector(`#${chart.id}-help-button`)) === null || _b === void 0 ? void 0 : _b.remove();
        (_c = document.querySelector(`#${chart.id}-help-data`)) === null || _c === void 0 ? void 0 : _c.remove();
        (_d = document.querySelector(`#${chart.id}-help-drag`)) === null || _d === void 0 ? void 0 : _d.remove();
        (_e = document.querySelector(`#${chart.id}-help-zoom`)) === null || _e === void 0 ? void 0 : _e.remove();
        let icon = document.querySelector(`#${chart.id} .card-title i`);
        icon.setAttribute("class", "fas fa-question-circle");
    }
    ;
}
