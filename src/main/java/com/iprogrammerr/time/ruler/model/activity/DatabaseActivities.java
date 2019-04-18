package com.iprogrammerr.time.ruler.model.activity;

import com.iprogrammerr.time.ruler.database.DatabaseSession;
import com.iprogrammerr.time.ruler.database.Record;
import com.iprogrammerr.time.ruler.model.SmartDate;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivities implements Activities {

    private final DatabaseSession session;

    public DatabaseActivities(DatabaseSession session) {
        this.session = session;
    }

    @Override
    public List<Activity> ofUserDate(long id, long date) {
        return ofUserDate(id, date, ActivitiesFilter.ALL);
    }

    private List<Activity> ofUserDate(long id, long date, ActivitiesFilter filter) {
        SmartDate smartDate = new SmartDate(date);
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM activity a INNER JOIN day d ON ")
            .append("d.user_id = ? AND d.date >= ? AND d.date <= ? ")
            .append("WHERE a.day_id = d.id");
        if (filter != ActivitiesFilter.ALL) {
            queryBuilder.append(" AND done = ").append(filter == ActivitiesFilter.DONE ? "1" : "0");
        }
        queryBuilder.append(" ORDER BY d.date ASC");
        return session.select(r -> {
            List<Activity> activities = new ArrayList<>();
            while (r.next()) {
                activities.add(new Activity(r));
            }
            return activities;
        }, queryBuilder.toString(), id, smartDate.dayBeginning(), smartDate.dayEnd());
    }

    @Override
    public List<Activity> ofUserDatePlanned(long id, long date) {
        return ofUserDate(id, date, ActivitiesFilter.PLANNED);
    }

    @Override
    public List<Activity> ofUserDateDone(long id, long date) {
        return ofUserDate(id, date, ActivitiesFilter.DONE);
    }

    @Override
    public long create(Activity activity) {
        return session.create(
            new Record(Activity.TABLE).put(Activity.NAME, activity.name)
                .put(Activity.DAY_ID, activity.dayId).put(Activity.START_TIME, activity.startTime)
                .put(Activity.END_TIME, activity.endTime).put(Activity.DONE, activity.done)
        );
    }

    private enum ActivitiesFilter {
        ALL, DONE, PLANNED
    }
}