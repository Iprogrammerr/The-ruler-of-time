import { tabsNavigation, router, routes, dateTimeParams } from "./app.js";
import { SmartDate } from "./date/smart-date.js";

const STATE = {
    PLAN: "plan",
    HISTORY: "history"
};
const NOT_AVAILABLE_CLASS =  "not-available";

const yearMonth = dateTimeParams.currentYearMonthFromUrl();
tabsNavigation.setup(document.querySelector("div"));
const state = stateFromActive(tabsNavigation.activeIndex());
setupMonthsNavigation();
setupDaysNavigation();

function stateFromActive(activeIndex) {
    let name;
    let mainRoute;
    let detailRoute;
    if (activeIndex == 1) {
        name = STATE.PLAN;
        mainRoute = routes.plan;
        detailRoute = routes.dayPlan;
    } else {
        name = STATE.HISTORY;
        mainRoute = routes.history;
        detailRoute = routes.dayPlanExecution;
    }
    return {
        name: name,
        mainRoute: mainRoute,
        detailRoute: detailRoute
    };
};

function setupMonthsNavigation() {
    let date = new SmartDate();
    date.setYearMonth(yearMonth.year, yearMonth.month);
    let prevPointer = document.getElementById("prev");
    if (prevPointer != null) {
        date.subtractMonth(1);
        let newYearMonth = date.asYearMonth();
        date.addMonth(1);
        prevPointer.onclick = () => router.replaceWithParams(state.mainRoute,
            dateTimeParams.yearMonthAsParams(newYearMonth.year, newYearMonth.month));
    }
    let nextPointer = document.getElementById("next");
    if (nextPointer != null) {
        date.addMonth(1);
        let newYearMonth = date.asYearMonth();
        nextPointer.onclick = () => router.replaceWithParams(state.mainRoute,
            dateTimeParams.yearMonthAsParams(newYearMonth.year, newYearMonth.month));
    }
};

function setupDaysNavigation() {
    let days = document.getElementsByClassName("day");
    let date = new SmartDate();
    for (let i = 0; i < days.length; i++) {
        let className = days[i].children[0].className;
        if (className !== NOT_AVAILABLE_CLASS) {
            let day = i + 1;
            if (date.isNow(yearMonth.year, yearMonth.month, day)) {
                days[i].onclick = () => router.forward(routes.today);
            } else {
                let param = dateTimeParams.yearMonthDayAsDateParam(yearMonth.year, yearMonth.month, day);
                days[i].onclick = () => router.forwardWithParams(state.detailRoute, param);
            }
        }
    }
};
