export class Help {
    helpPopover(id, content) {
        const helpId = `${id}-help`;
        const button = document.querySelector(`#${id} .card-title button`);
        button.addEventListener("click", (e) => {
            if (document.querySelector(`#${helpId}`) === null) {
                let popover = document.createElement("div");
                popover.setAttribute("id", helpId);
                popover.setAttribute("class", "popover fade bs-popover-left show");
                popover.style.top = `${window.pageYOffset + button.getBoundingClientRect().top}px`;
                document.querySelector("body").appendChild(popover);
                let arrow = document.createElement("div");
                arrow.setAttribute("class", "arrow");
                arrow.style.top = "6px";
                popover.appendChild(arrow);
                let popoverBody = document.createElement("div");
                popoverBody.setAttribute("class", "popover-body");
                popoverBody.innerHTML = content;
                popover.appendChild(popoverBody);
                if (button.getBoundingClientRect().left - popover.getBoundingClientRect().width > 0) {
                    popover.style.left = `${button.getBoundingClientRect().left - popover.getBoundingClientRect().width}px`;
                }
                else {
                    popover.style.left = `${button.getBoundingClientRect().right}px`;
                    popover.setAttribute("class", "popover fade bs-popover-right show");
                }
                button.querySelector("i").setAttribute("class", "fas fa-window-close");
            }
            else {
                document.querySelector(`#${helpId}`).remove();
                button.querySelector("i").setAttribute("class", "fas fa-question-circle");
            }
        });
    }
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
}
