import { AdminAnalyticsData } from "./data.js";
export class AdminAnalyticsDataRaw {
    constructor(group, value, createDate) {
        this.group = group;
        this.value = value;
        this.createDate = createDate;
    }
    transformData() {
        return new AdminAnalyticsData(this.group, this.value.map(d => {
            return {
                timestamp: new Date(d.timestamp), pseudonym: d.pseudonym, point: parseInt(d.point), text: d.text
            };
        }), new Date(this.createDate), undefined, false);
    }
}
