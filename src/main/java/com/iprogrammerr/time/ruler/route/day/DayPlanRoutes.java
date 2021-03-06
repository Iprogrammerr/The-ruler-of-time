package com.iprogrammerr.time.ruler.route.day;

import com.iprogrammerr.time.ruler.model.param.QueryParams;
import com.iprogrammerr.time.ruler.respondent.day.DayPlanRespondent;
import com.iprogrammerr.time.ruler.route.GroupedRoutes;
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import io.javalin.http.Context;

public class DayPlanRoutes implements GroupedRoutes {

    private static final String CACHE_VALUE = "no-store";
    private final DayPlanRespondent respondent;

    public DayPlanRoutes(DayPlanRespondent respondent) {
        this.respondent = respondent;
    }

    @Override
    public void init(String group, Javalin app) {
        app.get(group + DayPlanRespondent.DAY_PLAN, this::showDayPlan);
    }

    private void showDayPlan(Context context) {
        String date = context.queryParam(QueryParams.DATE, "");
        context.html(respondent.dayPlanPage(context.req, date).html);
        context.header(Header.CACHE_CONTROL, CACHE_VALUE);
    }
}
