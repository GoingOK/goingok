export function groupBy(arr, criteria) {
    const newObj = arr.reduce(function (acc, currentValue) {
        if (!acc.map(d => d.key).includes(currentValue[criteria])) {
            acc.push({ "key": currentValue[criteria], "value": [] });
        }
        acc.find(d => d.key == currentValue[criteria]).value.push(currentValue);
        return acc;
    }, []);
    return newObj;
}
export function caculateSum(arr) {
    let sum = 0;
    for (var i = 0; i < arr.length; i++) {
        sum += arr[i];
    }
    return sum;
}
export function calculateMean(arr) {
    return caculateSum(arr) / arr.length;
}
export function getDOMRect(id) {
    return document.querySelector(id).getBoundingClientRect();
}
export function addDays(date, days) {
    let result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}
export function minDate(arr) {
    return new Date(Math.min.apply(null, arr));
}
export function maxDate(arr) {
    return new Date(Math.max.apply(null, arr));
}
