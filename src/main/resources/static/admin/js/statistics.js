
function updateBar(){
 	$.get("../api/admin/timecost",function(data,status){ 	
		if(status == "success"){
			var pages = data.pages;
			var methods = data.methods;

			showBar("pages", pages.xdata,pages.ydata);
		}
 	});
}
function showBar(latbel,xdata,ydata){
	var barOptions = {
			series: {
				bars: {
					show: true,
					barWidth: 0.5,
					align: "center",
					horizontal: true,
				},
				color:"green",
			},
			yaxis: {
				ticks:ydata
			}
	    };
    var barData = {
            label: latbel,
            data: xdata
        };
	$.plot($("#flot-bar-pages"), [barData],barOptions);
};


$(function() {


    var barData = {
        label: "bar1",
        data: [
            [1, 1000],
            [2, 2000],
            [3, 3000],
            [4, 4000],
            [5, 5000],
            [7, 6000]
        ]
    };

    updateBar();
    //$.plot($("#flot-bar-chart"), [barData],barOptions);

});