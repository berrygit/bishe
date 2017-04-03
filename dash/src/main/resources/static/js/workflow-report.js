
function loadPageReport() {
	
	var chart = new Highcharts.Chart('show-instance', {
	    title: {
	        text: '工作流运行状态统计',
	        x: -20
	    },
	    xAxis: {
	        categories: ['10:00', '10:01', '10:02', '10:03', '10:04', '10:05', '10:06', '10:07', '10:08', '10:09',
	                     '10:10', '10:11', '10:12', '10:13', '10:14', '10:15', '10:16', '10:17', '10:18', '10:19',
	                     '10:20', '10:21', '10:22', '10:23', '10:24', '10:25', '10:26', '10:27', '10:28', '10:29',
	                     '10:30', '10:31', '10:32', '10:33', '10:34', '10:35', '10:36', '10:37', '10:38', '10:39',
	                     '10:40', '10:41', '10:42', '10:43', '10:44', '10:45', '10:46', '10:47', '10:48', '10:49',
	                     '10:50', '10:51', '10:52', '10:53', '10:54', '10:55', '10:56', '10:57', '10:58', '10:59',]
	    },
	    yAxis: {
	        title: {
	            text: '数量'
	        },
	        plotLines: [{
	            value: 0,
	            width: 1,
	            color: '#808080'
	        }]
	    },
	    plotOptions: {
            line: {
                dataLabels: {
                    enabled: true          // 开启数据标签
                },
                enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
            }
        },
	    series: [{
	        name: '成功',
	        data: [207, 296, 229, 214, 228, 221, 225, 226, 223, 238, 213, 239,
	               207, 226, 229, 214, 218, 211, 215, 226, 223, 218, 242, 223,
	               207, 256, 249, 214, 218, 221, 225, 256, 263, 308, 273, 312,
	               293, 266, 269, 294, 248, 231, 245, 215, 213, 267, 253, 209,
	               223, 286, 239, 214, 268, 251, 235, 265, 253, 254, 273, 243]
	    },{
	        name: '失败',
	        data: [0, 0, 0, 0, 0, 0, 0, 16, 13, 0, 0, 9,
	               7, 2, 2, 2, 0, 4, 0, 6, 3, 5, 0, 0,
	               0, 0, 0, 2, 7, 10, 4, 0, 0, 17, 0, 0,
	               16, 0, 4, 0, 0, 0, 0, 7, 0, 0, 0, 0,
	               0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0]
	    },{
	        name: '调度中',
	        data: [37, 46, 59, 34, 58, 41, 25, 26, 23, 20, 20, 39,
	               37, 22, 22, 32, 30, 43, 54, 34, 54, 23, 43, 44,
	               32, 43, 52, 34, 38, 38, 63, 72, 83, 78, 62, 77,
	               34, 54, 26, 37, 42, 52, 27, 27, 58, 27, 37, 53,
	               36, 53, 27, 32, 36, 38, 32, 43, 32, 43, 32, 32]
	    },{
	        name: '超时',
	        data: [0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0,
	               7, 2, 2, 0, 0, 4, 0, 6, 3, 5, 0, 0,
	               0, 0, 0, 2, 7, 0, 4, 0, 0, 0, 0, 0,
	               16, 0, 4, 0, 0, 0, 0, 7, 0, 0, 0, 0,
	               0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
	    }]
	});
}

function ZoneInventoryTable(selector, loadingSelector) {
    this.data = [];
    this.charts = [];
    this.columns = [{field: 'item', title: ''}];
    this.selector = selector; // #zone-inventory-table
    this.loadingSelector = loadingSelector; // #zone-inventory-loading
    this.dataLoadedHandlers = [];

    this.data = [
        {item: '<strong>classic状态</strong>'}, // 0
        {item: '<strong>vpc状态</strong>'},
        {item: '<strong>售卖状态</strong>'},
        {item: '<strong>是否io优化</strong>'},
        {item: '<strong>网络类型</strong>'},
        {item: '<strong>存储类型</strong>'},    // 5
        {item: '<strong>本盘/云盘</strong>'},
        {item: '<strong>系列</strong>'},
        {item: '<strong>小规格</strong>'},
        {item: '<strong>库存状态</strong>'},
        {item: '<strong>总量</strong>'},       // 10
        {item: '<strong>余量</strong>'},
        {item: '<strong>资源雷达图</strong>'},
        {item: '<strong>生产折线图</strong>'}   // 13
    ];
};

ZoneInventoryTable.prototype.loadData = function(zoneNo) {
    var $this = this;
    $($this.loadingSelector).bootstrapTable('showLoading');

    $.get("/ops/json/inventory/zone/" + zoneNo, function(data, status) {
        $($this.loadingSelector).bootstrapTable('hideLoading');

        if (status != 'success') {
            $alertHtml = '<div class="alert alert-warning"><strong>Warning!</strong>&nbsp;load inventory data of ' + zoneNo + ' failed.</div>';
            $($this.loadingSelector).html($alertHtml);
            return;
        }

        $this.renderView(zoneNo, data, $this);

        // 将数据加载事件广播出去
        for (var i = 0; i < $this.dataLoadedHandlers.length; ++i) {
            $this.dataLoadedHandlers[i](data);
        }
    });

};

ZoneInventoryTable.prototype.renderView = function(zoneNo, data, $this) {
    $this.columns.push({field: zoneNo, title: zoneNo});

    var collectTime = data.date;
    var inventoryOfZone = data.inventoryOfZone;
    var lines = data.lines;
    var resourceInventory = inventoryOfZone.resourceInventory;
    var flavorInventory = inventoryOfZone.flavorInventory;

    pushData($this.data, 0, zoneNo, inventoryOfZone.classicStatus);
    pushData($this.data, 1, zoneNo, inventoryOfZone.vpcStatus);
    pushData($this.data, 2, zoneNo, inventoryOfZone.finalStatus);
    pushData($this.data, 3, zoneNo, inventoryOfZone.ioOptimized);
    pushData($this.data, 4, zoneNo, inventoryOfZone.network);
    pushData($this.data, 5, zoneNo, inventoryOfZone.storage);
    pushData($this.data, 6, zoneNo, inventoryOfZone.io);
    pushData($this.data, 7, zoneNo, inventoryOfZone.generation);

    var resFlavor = resourceInventory.flavor;

    pushData($this.data, 8, zoneNo, resFlavor.flavor);
    pushData($this.data, 9, zoneNo, resFlavor.status);
    pushData($this.data, 10, zoneNo, resFlavor.totalAmount);
    pushData($this.data, 11, zoneNo, resFlavor.availableAmount);
    var radarChartId = 'canvas-radar-' + zoneNo;
    pushData($this.data, 12, zoneNo, '<div style="width:256px; height:256px"><canvas id="' + radarChartId + '" style="width:256px; height:256px"></canvas></div>');

    var radarConfig = renderRadarConfig(zoneNo, inventoryOfZone.network, resourceInventory);
    $this.charts.push({'id': radarChartId, 'config': radarConfig});


    var lineChartId = 'canvas-line-' + zoneNo;
    pushData($this.data, 13, zoneNo, '<div style="width:256px; height:256px"><canvas id="' + lineChartId + '" style="width:256px; height:256px"></canvas></div>');

    var lineConfig = renderLineConfig('publicIp', lines['publicIp']);
    $this.charts.push({'id': lineChartId, 'config': lineConfig});

    $($this.selector).bootstrapTable('destroy');
    $($this.selector).bootstrapTable({
        columns: $this.columns,
        data: $this.data,
        striped: true
    });

    // 将全部资源chart重新加载一次
    $.each($this.charts, function(index, chart, array) {
        new Chart(document.getElementById(chart['id']), chart['config']);
    });
}

function pushData(data, index, key, value) {
    if (data[index] == null) {
        data[index] = {key: value};
    } else {
        data[index][key] = value;
    }
}

function calculateRate(flavorInventory) {
    if (flavorInventory == null) {
        return 0.0;
    }
    var total = flavorInventory['totalAmount'];
    var vendible = flavorInventory['vendibleAmount'];
    var rate = (vendible + 0.000001) / total;
    if (rate > 1.0) { return 1.0; }
    else if (rate < 0.0) { return 0.0; }
    else { return rate; }
}

/* 生成flavor，lflavor，mac，publicIp，privateIp维度的雷达图的配置 */
function renderRadarConfig(zoneNo, network, resourceInventory) {
    var labels = [resourceInventory.flavor.flavor, resourceInventory.lflavor.flavor, 'mac'];
    var data = [calculateRate(resourceInventory.flavor), calculateRate(resourceInventory.lflavor), calculateRate(resourceInventory.mac)];

    network = network.toLowerCase();
    if (network.search("classic") >= 0) {
        labels.push('公网ip'); labels.push('私网ip');
        data.push(calculateRate(resourceInventory.publicIp)); data.push(calculateRate(resourceInventory.privateIp));
    }

    if (network.search("vpc") >= 0) {
        labels.push('vpc公网ip'); labels.push('vpc私网ip');
        data.push(calculateRate(resourceInventory.vpcPublicIp)); data.push(calculateRate(resourceInventory.vpcPrivateIp));
    }

    var color = Chart.helpers.color;
    var config = {
        type: 'radar',
        data: {
            labels: labels,
            datasets: [{
                label: zoneNo,
                backgroundColor: color(window.chartColors.red).alpha(0.2).rgbString(),
                borderColor: window.chartColors.red,
                pointBackgroundColor: window.chartColors.red,
                data: data
            }]
        },
        options: {
            legend: {
                display: false // 不显示图例
            },
            title: {
                display: false, // 不显示标题
                text: 'resource radar chart of ' + zoneNo,
            },
            scale: {
              ticks: {
                beginAtZero: true
              }
            }
        }
    };
    return config;
}

/* 生成变化曲线图的配置 */
function renderLineConfig(refer, line) {
    var labels = [];
    var data = [];
    $.each(line, function(key, value) {
        var time = new Date(parseInt(key));
        labels.push(time.getMonth() + '/' + time.getDate());
        data.push(value);
    })
    var config = {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: refer,
                backgroundColor: window.chartColors.red,
                borderColor: window.chartColors.red,
                data: data,
                fill: false,
            }]
        },
        options: {
            legend: {
                display: false
            },
            title:{
                display:false,
                text:'flavor chart for ' + refer
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: '日期'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: '可售量'
                    }
                }]
            }
        }
    };
    return config;
}