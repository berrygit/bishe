
function loadPageInstance() {
	
    $('#instance-table').bootstrapTable({
        columns: [{
            field: 'name',
            title: '工作流名称',
        }, {
            field: 'requestid',
            title: 'request id'
        }, {
            field: 'workflowid',
            title: '工作流ID'
        },{
            field: 'status',
            title: '状态'
        }, {
            field: 'node',
            title: '节点'
        }, {
            field: 'begin',
            title: '开始时间'
        }, {
            field: 'end',
            title: '结束时间'
        }, {
            field: 'operation',
            title: '操作'
        }],
        data: [{
        	name: 'Request_A',requestid: '2837',status:'成功',node:'10.182.27.31',begin:'2017-02-07 12:33:24',
        	workflowid: '112',end:'2017-02-07 12:34:24',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_B',requestid: '2877',status:'超时',node:'10.182.27.33',begin:'2017-02-07 12:23:54',
        	workflowid: '111',end:'2017-02-07 12:24:34',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_C',requestid: '2827',status:'成功',node:'10.182.27.33',begin:'2017-02-07 12:13:35',
        	workflowid: '110',end:'2017-02-07 12:14:23',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_A',requestid: '2237',status:'失败',node:'10.182.27.31',begin:'2017-02-07 12:12:22',
        	workflowid: '109',end:'2017-02-07 12:13:15',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_C',requestid: '1237',status:'成功',node:'10.182.27.33',begin:'2017-02-07 12:12:17',
        	workflowid: '108',end:'2017-02-07 12:13:14',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_B',requestid: '8337',status:'成功',node:'10.182.27.31',begin:'2017-02-07 12:12:15',
        	workflowid: '107',end:'2017-02-07 12:13:16',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_A',requestid: '3237',status:'失败',node:'10.182.27.31',begin:'2017-02-07 12:12:14',
        	workflowid: '106',end:'2017-02-07 12:12:24',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_B',requestid: '9837',status:'成功',node:'10.182.27.33',begin:'2017-02-07 12:12:14',
        	workflowid: '105',end:'2017-02-07 12:12:23',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_C',requestid: '2854',status:'成功',node:'10.182.27.31',begin:'2017-02-07 12:12:13',
        	workflowid: '104',end:'2017-02-07 12:12:19',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_A',requestid: '1437',status:'成功',node:'10.182.27.33',begin:'2017-02-07 12:12:13',
        	workflowid: '103',end:'2017-02-07 12:12:17',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_C',requestid: '2384',status:'成功',node:'10.182.27.31',begin:'2017-02-07 12:12:13',
        	workflowid: '102',end:'2017-02-07 12:12:17',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_B',requestid: '8463',status:'失败',node:'10.182.27.33',begin:'2017-02-07 12:12:13',
        	workflowid: '101',end:'2017-02-07 12:12:17',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }, {
        	name: 'Request_A',requestid: '1927',status:'成功',node:'10.182.27.31',begin:'2017-02-07 12:12:13',
        	workflowid: '100',end:'2017-02-07 12:12:16',operation:'<a>详情</a>&nbsp;&nbsp;<a>重试</a>&nbsp;&nbsp;<a>回滚</a>'
        }],
        striped: true,
        pagination: true
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