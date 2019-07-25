let d3 = require('d3');

export function buildChart(entries) {

    let svg = null;

    //let entries = [{timestamp:"2018-05-02T06:01:29.077Z",point:20},{timestamp:"2018-05-03T06:01:29.077Z",point:80},{timestamp:"2018-05-04T06:01:29.077Z",point:54}];

    //console.log("entries: ",entries);
    let data = entries.map( r => {
        let dp = {};
        dp.timestamp  = new Date(r.timestamp);
        dp.point = r.point;
        return dp;
    });
    //console.log("data: ",data);

    let width = 1000;
    let height = width/4;
    let margin = {top: 10, right: 15, bottom: 20, left: 55};
    let w = width - margin.left - margin.right;
    let h = height - margin.top - margin.bottom;

    let x = d3.scaleTime().range([0, w]);
    let y = d3.scaleLinear().range([h, 0]);

    x.domain(d3.extent(data.map(r => r.timestamp)));
    y.domain([0,100]); // range of reflection points
    //
    let xAxis = d3.axisBottom(x)
        .ticks(8);

    let labels = new Map();
    labels.set(0, "distressed");
    labels.set(50, "going ok");
    labels.set(100, "soaring");

    let yAxis = d3.axisLeft(y)
        .tickValues([0,25,50,75,100])
        .tickFormat(function(d) { return labels.get(d);});


    d3.select("svg").remove();

    svg = d3.select("div#reflection-point-chart")
        .append("svg")
        .attr("preserveAspectRatio", "xMinYMin meet")
        .attr("viewBox", "0 0 "+1000+" "+4000)
        .classed("svg-content", true);

    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + h + ")")
        .call(xAxis);

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis);

    svg.selectAll(".point")
        .data(data)
        .enter().append("svg:circle")
        .attr("class","point")
        .attr("r",5)
        .attr("cx",function(d) { return x(d.timestamp); })
        .attr("cy",function(d) { return y(d.point); });

    let hardline = d3.line()
        .x(function(d) { return x(d.timestamp); })
        .y(function(d) { return y(d.point); })
        .curve(d3.curveMonotoneX);

    let softline = d3.line()
        .x(function(d) { return x(d.timestamp); })
        .y(function(d) { return y(d.point); })
        .curve(d3.curveBasis);


    let avg = d3.mean(data,function(d) { return y(d.point); });
    let meanline = d3.line()
        .x(function(d) { return x(d.timestamp); })
        .y(function(d) { return avg; });

    svg.append("path")
        .data([data])
        .attr("class", "hardline")
        .attr("d", hardline);

    svg.append("path")
        .data([data])
        .attr("class", "softline")
        .attr("d", softline);

    svg.append("path")
        .data([data])
        .attr("class", "meanline")
        .attr("d", meanline);

    //.on('click', function(d) { alert("Reflection point "+ d.reflection.point+" at "+ d.timestamp); });

}

