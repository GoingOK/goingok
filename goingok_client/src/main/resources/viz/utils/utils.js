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
