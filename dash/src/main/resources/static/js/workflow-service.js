
function loadPageService() {
	$('#service-table').bootstrapTable({
        columns: [{
            field: 'name',
            title: '工作流名称',
        }, {
            field: 'total',
            title: '调用'
        }, {
            field: 'success',
            title: '成功'
        },{
            field: 'fail',
            title: '失败'
        },{
            field: 'timeout',
            title: '超时'
        },{
            field: 'rate',
            title: '成功率'
        },{
            field: 'average',
            title: '平均耗时（毫秒）'
        },{
            field: 'max',
            title: '最长耗时（毫秒）'
        },{
            field: 'min',
            title: '最短耗时（毫秒）'
        },{
            field: 'operation',
            title: '操作'
        }],
        data: [{
        	name: 'Request_A',total:'2344',success:'2344',fail:'0',timeout:'0',rate:'100.00%' 
        		,average:"1216", max:"2132", min:"1003",operation:'<a>查看时间趋势</a>'
        },{
        	name: 'Request_B',total:'234',success:'234',fail:'0',timeout:'0',rate:'100.00%' 
        		,average:"1532", max:"2043", min:"983",operation:'<a>查看时间趋势</a>'
        },{
        	name: 'Request_C',total:'1235',success:'1235',fail:'0',timeout:'0',rate:'100.00%' 
        		,average:"1598", max:"2231", min:"1234",operation:'<a>查看时间趋势</a>'
        },{
        	name: 'Request_D',total:'345',success:'345',fail:'0',timeout:'0',rate:'100.00%' 
        		,average:"1345", max:"2312", min:"1234",operation:'<a>查看时间趋势</a>'
        },{
        	name: 'Request_E',total:'876',success:'876',fail:'0',timeout:'0',rate:'100.00%' 
        		,average:"1453", max:"2341", min:"1222",operation:'<a>查看时间趋势</a>'
        }],
        striped: true,
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