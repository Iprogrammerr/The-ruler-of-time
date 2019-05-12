package com.iprogrammerr.time.ruler.respondent.day;

import com.iprogrammerr.time.ruler.model.Identity;
import com.iprogrammerr.time.ruler.model.QueryParamKey;
import com.iprogrammerr.time.ruler.model.UrlQueryBuilder;
import com.iprogrammerr.time.ruler.model.activity.ActivitiesSearch;
import com.iprogrammerr.time.ruler.model.activity.Activity;
import com.iprogrammerr.time.ruler.model.date.DateParsing;
import com.iprogrammerr.time.ruler.model.date.LimitedDate;
import com.iprogrammerr.time.ruler.model.date.ServerClientDates;
import com.iprogrammerr.time.ruler.model.date.SmartDate;
import com.iprogrammerr.time.ruler.respondent.HtmlResponse;
import com.iprogrammerr.time.ruler.respondent.Redirection;
import com.iprogrammerr.time.ruler.view.rendering.DayPlanExecutionViews;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

public class DayPlanExecutionRespondent {

    public static final String TODAY = "today";
    public static final String DAY_PLAN_EXECUTION = "day-plan-execution";
    private final Identity<Long> identity;
    private final DayPlanExecutionViews views;
    private final ActivitiesSearch activities;
    private final LimitedDate limitedDate;
    private final DateParsing parsing;
    private final ServerClientDates serverClientDates;
    private String prefix;

    public DayPlanExecutionRespondent(Identity<Long> identity, DayPlanExecutionViews views,
        ActivitiesSearch activities, LimitedDate limitedDate, DateParsing parsing,
        ServerClientDates serverClientDates) {
        this.identity = identity;
        this.views = views;
        this.activities = activities;
        this.limitedDate = limitedDate;
        this.parsing = parsing;
        this.serverClientDates = serverClientDates;
        this.prefix = "";
    }

    public HtmlResponse planExecutionPage(HttpServletRequest request, String date) {
        Instant clientNow = serverClientDates.clientDate(request);
        Instant requestedDate = limitedDate.fromString(date, clientNow);
        List<Activity> dayActivities = activities.ofUserDate(identity.value(request),
            requestedDate.getEpochSecond());
        boolean history = new SmartDate(clientNow).dayBeginning() > requestedDate.getEpochSecond();
        return new HtmlResponse(views.view(requestedDate, history, dayActivities,
            d -> serverClientDates.clientDate(request, d)));
    }

    public Redirection redirection() {
        return new Redirection(prefix + TODAY);
    }

    public Redirection redirection(Instant date) {
        return new Redirection(new UrlQueryBuilder().put(QueryParamKey.DATE, parsing.write(date))
            .build(prefix + DAY_PLAN_EXECUTION));
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
