import { calculateMean, groupBy } from "../utils/utils.js";
export class AdminAnalyticsData {
    constructor(group, value, createDate = undefined, colour = undefined, selected = false) {
        this.group = group;
        this.value = value;
        this.creteDate = createDate;
        this.colour = colour;
        this.selected = selected;
    }
    getUsersData() {
        let usersMean = groupBy(this.value, "pseudonym").map(d => { return { "pseudonym": d.key, "point": calculateMean(d.value.map(d => d.point)) }; });
        return new AdminAnalyticsData(this.group, usersMean, this.creteDate, this.colour);
    }
}
export class DataStats {
    constructor(stat, displayName, value) {
        this.stat = stat,
            this.displayName = displayName,
            this.value = value;
    }
}
export class AdminAnalyticsDataStats extends AdminAnalyticsData {
    constructor(entries) {
        super(entries.group, entries.value, entries.creteDate, entries.colour, entries.selected);
        let uniqueUsers = groupBy(entries.value, "pseudonym");
        this.stats = [];
        this.stats.push(new DataStats("usersTotal", "Users", uniqueUsers.length));
        this.stats.push(new DataStats("refTotal", "Reflections", entries.value.length));
        this.stats.push(new DataStats("mean", "Mean", Math.round(calculateMean(entries.value.map(r => r.point)))));
        this.stats.push(new DataStats("oldRef", "Oldest reflection", new Date(Math.min.apply(null, entries.value.map(r => new Date(r.timestamp))))));
        this.stats.push(new DataStats("newRef", "Newest reflection", new Date(Math.max.apply(null, entries.value.map(r => new Date(r.timestamp))))));
        this.stats.push(new DataStats("ruRate", "Reflections per user", Math.round(entries.value.length / uniqueUsers.length * 100) / 100));
    }
    ;
    getStat(stat) {
        var exists = this.stats.find(d => d.stat == stat);
        if (exists != undefined) {
            return exists;
        }
        else {
            return new DataStats("na", "Not found", 0);
        }
    }
}
export class TimelineData {
    constructor(data, colour, group) {
        this.refId = data.refId;
        this.timestamp = data.timestamp;
        this.pseudonym = data.pseudonym;
        this.point = data.point;
        this.text = data.text;
        this.colour = colour;
        this.group = group;
    }
}
export class HistogramData extends AdminAnalyticsData {
    constructor(value, group, colour, bin, percentage) {
        super(group, value, undefined, colour);
        this.bin = bin;
        this.percentage = percentage;
    }
}
export class UserChartData {
    constructor(bin, value, percentage, isGroup) {
        if (bin.x0 == 0) {
            this.binName = "distressed";
        }
        else if (bin.x1 == 100) {
            this.binName = "soaring";
        }
        else {
            this.binName = "going ok";
        }
        this.percentage = percentage;
        this.isGroup = isGroup;
    }
}
export class ClickTextData {
    constructor(clickStat, dataStat, clickGroup, dataGroup) {
        this.clickData = { stat: clickStat, group: clickGroup },
            this.data = { stat: dataStat, group: dataGroup };
    }
}
