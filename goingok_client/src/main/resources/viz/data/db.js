import { AdminAnalyticsData, AuthorAnalyticsData } from "./data.js";
export class AdminAnalyticsDataRaw {
    constructor(group, value, createDate) {
        this.group = group;
        this.value = value;
        this.createDate = createDate;
    }
    transformData() {
        return new AdminAnalyticsData(this.group, this.value.map(d => {
            return {
                refId: parseInt(d.refId), timestamp: new Date(d.timestamp), pseudonym: d.pseudonym, point: parseInt(d.point), text: d.text
            };
        }), new Date(this.createDate), undefined, false);
    }
}
export class AuthorAnalyticsDataRaw {
    constructor(entries, analytics) {
        this.reflections = entries;
        this.analytics = analytics;
    }
    transformData(colourScale) {
        return new AuthorAnalyticsData(this.reflections.map(d => {
            return { "refId": parseInt(d.refId), "timestamp": new Date(d.timestamp), "point": parseInt(d.point), "text": d.text };
        }), this.analytics, colourScale);
    }
}
