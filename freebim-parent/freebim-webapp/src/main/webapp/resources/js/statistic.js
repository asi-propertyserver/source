/**
 * 
 */
at.freebim.db.statistic = {
		
//		active : false,
		
		init: function () {
			
//			at.freebim.db.statistic.active = true;
	
			var stat = jq("#statistic"), i18n = net.spectroom.js.i18n;
			stat.empty();
			
			var myFormatters = d3.locale({
				  "decimal": ",",
				  "thousands": ".",
				  "grouping": [3],
				  "currency": ["€", ""],
				  "dateTime": i18n.strings.dateTime,
				  "date": i18n.strings.date,
				  "time": "%H:%M:%S",
				  "periods": ["AM", "PM"],
				  "days": i18n.strings.days,
				  "shortDays": i18n.strings.shortDays,
				  "months": i18n.strings.months,
				  "shortMonths": i18n.strings.shortMonths
			});
			
			d3.time.format = myFormatters.timeFormat;

			var margin = {
				top : 20,
				right : 80,
				bottom : 30,
				left : 50
			}, width = stat.width() - margin.left - margin.right, height = stat.height() - margin.top - margin.bottom;

			var parseDate = d3.time.format.utc("%Y-%m-%dT%H:%M:%S").parse;
			
	
			var x = d3.time.scale().range([ 0, width ]);
	
			var y = d3.scale.linear().range([ height, 0 ]);
	
			var color = d3.scale.category10();
	
			var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(d3.time.format("%B %Y"));
	
			var yAxis = d3.svg.axis().scale(y).orient("left");
	
			var line = d3.svg.line().interpolate("basis").x(function(d) {
				return x(d.date);
			}).y(function(d) {
				return y(d.v);
			});
	
			var svg = d3.select("#statistic")
				.append("svg")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom)
				.append("g")
				.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			d3.json("/statistic/added/0/0", function(error, response) {
	
				var data = response.result;
	
				color.domain(d3.keys(data[0]).filter(function(key) {
					return key !== "t";
				}));
	
				data.forEach(function(d) {
					d.date = parseDate(d.t);
				});
	
				var cities = color.domain().map(function(name) {
					switch (name) {
	//				case "a":
	//					return {
	//						name : name,
	//						values : data.map(function(d) {
	//							return {
	//								date : d.date,
	//								v : d.a // ((d.a > 200) ? 200 : d.a)
	//							};
	//						})
	//					};
					case "m":
						return {
							name : name,
							values : data.map(function(d) {
								return {
									date : d.date,
									v : d.m // ((d.m > 200) ? 200 : d.m)
								};
							})
						};
	//				case "d":
	//					return {
	//						name : name,
	//						values : data.map(function(d) {
	//							return {
	//								date : d.date,
	//								v : d.d //((d.d > 200) ? 200 : d.d)
	//							};
	//						})
	//					};
					case "s":
						return {
							name : name,
							values : data.map(function(d) {
								return {
									date : d.date,
									v : d.s
								};
							})
						};
					}
				});
	
				x.domain(d3.extent(data, function(d) {
					return d.date;
				}));
	
				y.domain([ d3.min(cities, function(c) {
					return d3.min(c.values, function(v) {
						return v.v;
					});
				}), d3.max(cities, function(c) {
					return d3.max(c.values, function(v) {
						return v.v;
					});
				}) ]);
	
				svg.append("g")
					.attr("class", "x axis")
					.attr("transform", "translate(0," + height + ")")
					.call(xAxis);
	
				svg.append("g")
					.attr("class", "y axis")
					.call(yAxis).append("text")
					.attr("transform", "rotate(-90)")
					.attr("y", 6)
					.attr("dy", ".71em")
					.style("text-anchor", "end")
					.text(i18n.g("STAT_COUNT"));
	
				var city = svg.selectAll(".city")
					.data(cities)
					.enter()
					.append("g")
					.attr("class", "city");
	
				city.append("path")
					.attr("class", "line")
					.attr("d", function(d) {
						return line(d.values);
					})
					.style("stroke", function(d) {
						return color(d.name);
					});
	
				city.append("text")
					.attr("i18n", function(d) { 
						return ((d.name === "s") ? "STAT_ELEMENTS" : ((d.name === "m") ? "STAT_MODIFICATIONS" : "")); 
					})
					.datum(function(d) {
						return {
							name : ((d.name === "s") ? i18n.g("STAT_ELEMENTS") : ((d.name === "m") ? i18n.g("STAT_MODIFICATIONS") : "")),
							value : d.values[d.values.length - 1]
						};
					})
					.attr("transform", function(d) {
						return "translate(" + x(d.value.date) + "," + y(d.value.v) + ")";
					})
					.attr("x", 3)
					.attr("dy", ".35em")
					.text(function(d) {
						return d.name;
					});
		});
	}
};

/*jq(document).on("i18n_translate", function (event, data) {
	if (at.freebim.db.statistic.active) {
		setTimeout(function () {
			at.freebim.db.statistic.init();
		}, 100);
	}
});*/