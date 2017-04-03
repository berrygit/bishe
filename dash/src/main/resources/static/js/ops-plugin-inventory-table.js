/**
 *
 */

function InventoryTable(selector, level) {
    this.data = [];
    this.selector = selector; // #inventory-table
    this.level = level;
    this.dataLoadedHandlers = [];

    this.loadData();
};

InventoryTable.prototype.columnsMapping = {
    BigRegion: {
        child: {'NcInit': 'ncInit', 'PublicIp': 'publicIp', 'PrivateIp': 'privateIp', 'Mac': 'mac', 'Generation': 'generation'},
    },
    AvailableZone: {
        child: {'Region': 'region', 'NcInit': 'ncInit', 'PublicIp': 'publicIp', 'PrivateIp': 'privateIp', 'Mac': 'mac', 'Generation': 'generation'},
    },
    Cluster: {
        child: {'AvailableZone': 'availableZone', 'Region': 'region', 'NcInit': 'ncInit', 'PublicIp': 'publicIp', 'PrivateIp': 'privateIp', 'Mac': 'mac', 'Generation': 'generation'},
    },
    Zone: {
        child: {'规格': 'flavor', '系列': 'generation', 'cpu/mem': 'cpuMem', '可售量': 'vendibleAmount', '余量': 'availableAmount', '总量': 'totalAmount','低/高水位':'lowHighLevel', 'io优化': 'ioOptimized', '网络': 'network', '存储':'storage', '磁盘':'io'},
    }
};

InventoryTable.prototype.columns=
[
  [
    { field: 'zone', title: "zone",width:"18%", colspan: 1, rowspan: 2},
    { field: 'lflavorInfo', title: "最大规格",width:"12%", colspan: 1, rowspan: 2 },
    { field: 'flavorInfo', title: "最小规格",width:"12%", colspan: 1, rowspan: 2 },
    { field: 'publicIp', title: "公网ip",width:"10%", colspan: 1, rowspan: 2 },
    { field: 'privateIp', title: "私网ip",width:"10%", colspan: 1, rowspan: 2 },
    { field: 'mac', title: "mac",width:"4%", colspan: 1, rowspan: 2 },
    { title: "库存概要", width:"24%", colspan: 2, rowspan: 1 },
    { field: 'generation', title: "系列",width:"6%", colspan: 1, rowspan: 2 },
    { field: 'detail', title: "详情",width:"4%", colspan: 1, rowspan: 2 }
  ],
  [
    { field: 'maxInventory', title: '最大库存'},
    { field: 'minInventory', title: '最小库存'}
  ]
]

function mergeRow(row1, row2) {
    if ([row1['status'], row2['status']].includes('ONLINE')) {
        row1['status'] = 'ONLINE';
    } else {
        row1['status'] = 'OFFLINE';
    }

    var allStorage = row1['storage'].split('/').concat(row2['storage'].split('/')).sort();
    var outStorage = [];
    if (allStorage.includes('local')) outStorage.push('local');
    if (allStorage.includes('cloud')) outStorage.push('cloud');
    while (allStorage.length > 0) {
        var storage = allStorage.shift();
        if (!outStorage.includes(storage)) {
            outStorage.push(storage);
        }
    }
    row1['storage'] = outStorage.join('/');

    var allNetwork = row1['network'].split('/').concat(row2['network'].split('/'));
    var outNetwork = [];
    if (allNetwork.includes('classic')) outNetwork.push('classic');
    if (allNetwork.includes('vpc')) outNetwork.push('vpc');
    row1['network'] = outNetwork.join('/');

    var allNcType = row1['ncType'].split('/').concat(row2['ncType'].split('/')).sort();
    var outNcType = [];
    while (allNcType.length > 0) {
        var ncType = allNcType.shift();
        if (!outNcType.includes(ncType)) {
            outNcType.push(ncType);
        }
    }
    row1['ncType'] = outNcType.join('/');

    var allNcInit = row1['ncInit'].split(':').concat(row2['ncInit'].split(':'));
    var outNcInit = [];
    while (allNcInit.length > 0) {
        var ncInit = allNcInit.shift();
        if (!outNcInit.includes(ncInit)) {
            outNcInit.push(ncInit);
        }
    }
    row1['ncInit'] = outNcType.join(':');

    var allGeneration = row1['generation'].split('/').concat(row2['generation'].split('/')).sort();
    var outGeneration = [];
    while (allGeneration.length > 0) {
        var generation = allGeneration.shift();
        if (!outGeneration.includes(generation)) {
            outGeneration.push(generation);
        }
    }
    row1['generation'] = outGeneration.join('/');

    row1['cpuFragment'] = row1['cpuFragment'] + row2['cpuFragment'];
    row1['memoryFragment'] = row1['memoryFragment'] + row2['memoryFragment'];
    row1['publicIp'] = row1['publicIp'] + row2['publicIp'];
    row1['privateIp'] = row1['privateIp'] + row2['privateIp'];
    row1['mac'] = row1['mac'] + row2['mac'];

    var mapInventory = {};
    var inventories = row1['inventory'];
    for (var i = 0; i < inventories.length; ++i) {
        var inventory = inventories[i];
        mapInventory[inventory['specification']] = inventory;
    }
    var inventories = row2['inventory'];
    for (var i = 0; i < inventories.length; ++i) {
        var inventory = inventories[i];

        var tp = mapInventory[inventory['specification']];
        if (tp == null) {
            mapInventory[inventory['specification']] = inventory;
        } else {
            tp['cpuFragment'] = tp['cpuFragment'] + inventory['cpuFragment'];
            tp['memoryFragment'] = tp['memoryFragment'] + inventory['memoryFragment'];
            tp['inventory'] = tp['inventory'] + inventory['inventory'];
            tp['total'] = tp['total'] + inventory['total'];
        }
    }
    var outInventory = [];
    for (var key in mapInventory) {
        outInventory.push(mapInventory[key]);
    }
    row1['inventory'] = outInventory;

    return row1;
}

/* 将columns编码转化为bootstrap-table.columns的形式 */
InventoryTable.prototype.$translate = function(raw) {
    var ret = [];
    for (var key in raw) {
        ret.push({title: key, field: raw[key]});
    }
    return ret;
}

/* 获取指定level的父表展示数据columns */
InventoryTable.prototype.$getColumns = function() {
    // 指定了单元格的对齐属性
    $.each(this.columns, function(index, value, array){
        $.each(value, function(index, innerValue, array){
            innerValue.valign="middle";
            innerValue.align="center";
        });
    });
    
    return this.columns;
}

/* 获取指定level的子表展示数据columns */
InventoryTable.prototype.$getChildColumns = function(level) {
    return this.$translate(this.columnsMapping[level]['child']);
}

var mapData = {};

/*
 * 库存数据展示预处理：按照level合并原数据（服务器返回）
 * 数据格式{zone: 'zzz', cluster: 'ccc', availableZone: 'aaa', bigRegion: 'bbb', ...}
 * 就是通过zone，cluster，availableZone，bigRegion选择聚合数据的。
 */
InventoryTable.prototype.$dataPreProcessor = function(data, level, $this) {
    var key = $this.$levelToVariable[level];
    
    for (var i = 0; i < data.length; ++i) {
        var row = data[i];
        var id = row[key];

        var mRow = mapData[id];
        if (mRow == null) {
            mapData[id] = row;
        } else {
            mergeRow(mRow, row);
        }
    }

    data = [];
    
    for(var key in mapData) {
        var item = mapData[key];
        
        var row = {};
        
        $this.prepareTableShowInfo(row, item, $this);

        data.push(row);
    }
    return data;
}


// 详情页面信息
function transferForDetail(zone){
    
    var obj = mapData[zone];
    
    // 基本信息
    $('#zoneInfo').text(zone);
    $('#clusterInfo').text(obj.cluster);
    $('#aZoneInfo').text(obj.availableZone);
    $('#regionInfo').text(obj.bigRegion);
    
    if (obj.ioOptimized == 'false'){
        $('#ioOptimizedInfo').text('否');
    }else{
        $('#ioOptimizedInfo').text('是');

    }
    
    $('#networkInfo').text(obj.network);
    $('#storageInfo').text(obj.storage);
    $('#ioInfo').text(obj.io);
    $('#generationInfo').text(obj.generation);
    
    // 状态信息
    $('#finalStatusInfo').text(obj.finalStatus);
    $('#classicStatusInfo').text(obj.classicStatus);
    $('#vpcStatusInfo').text(obj.vpcStatus);
    
    $('#autoStatusInfo').text(obj.autoStatus);
    $('#locationStatusInfo').text(obj.locationStatus);
    
    var manual = obj.manual;
    
    if (manual == false){
        $('#manualStatus').text("OFFLINE");
    }else{
        $('#manualStatus').text("ONLINE");
    }
    
    data = [];
    
    $.each(obj.resourceInventory, function(index, value, array) {
        
        // 不存在的资源直接略过（classic vpc 不是都有，后台返回的是null）
        if (value == null){
            return;
        }
        
        map = {};
        
        if(index == 'flavor'){
            map.resouce_detail = '最小规格 ' + value.flavor;
        }else if(index == 'lflavor'){
            map.resouce_detail = '最大规格 ' + value.flavor;
        }else if(index == 'privateIp'){
            map.resouce_detail = 'classic私网ip';
        }else if(index == 'publicIp'){
            map.resouce_detail = 'classic公网ip';
        }else if(index == 'vpcPrivateIp'){
            map.resouce_detail = 'vpc私网ip';
        }else if(index == 'vpcPublicIp'){
            map.resouce_detail = 'vpc公网ip';
        }else{
            map.resouce_detail = value.flavor;
        }
        
        map.status_detail = value.status;
        map.available_amount_detail = value.availableAmount;
        map.vendible_amount_detail = value.vendibleAmount;
        map.total_amount_detail = value.totalAmount;
        map.low_high_level_detail = value.lowLevel + '/' +value.highLevel;
        
        data.push(map);
    });
    
    // 资源信息
    $('#inventory-resource-table').bootstrapTable({
        columns: [{
            field: 'resouce_detail',
            title: '资源',
            width: '20%'
        }, {
            field: 'status_detail',
            title: '状态'
        }, {
            field: 'available_amount_detail',
            title: '可售卖量'
        }, {
            field: 'vendible_amount_detail',
            title: '余量'
        }, {
            field: 'total_amount_detail',
            title: '总量'
        }, {
            field: 'low_high_level_detail',
            title: '低/高水位'
        }],
        data: data,
        striped: true
    });
    
}

// warning级别
InventoryTable.prototype.$tenseLevel = 0.1;

InventoryTable.prototype.$checkResouceTenseStatus = function(resource, status){
    if (resource.availableAmount/resource.totalAmount < this.$tenseLevel){
        status.state = 'tense';
    }
}

InventoryTable.prototype.prepareTableShowInfo = function(row, item, $this) {
    
    var status = new String('');

    row.detail = '<a onclick = "transferForDetail(\'' +item.zone+ '\')" data-toggle="modal" data-target="#detailPage" type="button" class="btn bt-xs btn-success">详情<a>';

    var splitLine = '<div style="margin:0 auto;height:1px;width:50%;background-color:grey;overflow:hidden;"></div>';

    // 小规格
    var flavor = item.resourceInventory.flavor;
    var flavorName = flavor.flavor;
    var flavorNum = flavor.vendibleAmount;
    row.flavorInfo = flavorName + '<br/></br>' + flavorNum;
    
    // 大规格
    var lflavor = item.resourceInventory.lflavor;
    var lflavorName = lflavor.flavor;
    var lflavorNum = lflavor.vendibleAmount;
    row.lflavorInfo = lflavorName + '<br/></br>' + lflavorNum;

    // 私网ip
    var privateIp = item.resourceInventory.privateIp;
    if (privateIp != null) {
        var classicPriIP = 'classic<br/>'
                + $this.initStatus(privateIp.vendibleAmount, privateIp.status);
        $this.$checkResouceTenseStatus(privateIp, status);
    }

    var vpcPrivateIp = item.resourceInventory.vpcPrivateIp;
    if (vpcPrivateIp != null) {
        var vpcPriIP = 'vpc<br/>'
                + $this.initStatus(vpcPrivateIp.vendibleAmount,
                        vpcPrivateIp.status);
        $this.$checkResouceTenseStatus(vpcPrivateIp, status);
    }

    if (classicPriIP != null && vpcPriIP != null) {
        row.privateIp = classicPriIP + splitLine + vpcPriIP;
    } else if (classicPriIP != null) {
        row.privateIp = classicPriIP;
    } else {
        row.privateIp = vpcPriIP;
    }

    // 公网ip
    var publicIp = item.resourceInventory.publicIp;
    if (publicIp != null) {
        var classicPubIP = 'classic<br/>'
                + $this.initStatus(publicIp.vendibleAmount, publicIp.status);
        $this.$checkResouceTenseStatus(publicIp, status);
    }

    var vpcPublicIp = item.resourceInventory.vpcPublicIp;
    if (vpcPublicIp != null) {
        var vpcPubIP = 'vpc<br/>'
                + $this.initStatus(vpcPublicIp.vendibleAmount,
                        vpcPublicIp.status);
        $this.$checkResouceTenseStatus(vpcPublicIp, status);
    }

    if (classicPubIP != null && vpcPubIP != null) {
        row.publicIp = classicPubIP + splitLine + vpcPubIP;
    } else if (classicPubIP != null) {
        row.publicIp = classicPubIP;
    } else {
        row.publicIp = vpcPubIP;
    }

    var mac = item.resourceInventory.mac;
    row.mac = $this.initStatus(mac.vendibleAmount, mac.status);
    $this.$checkResouceTenseStatus(mac, status);


    // 最大最小库存
    var max = Number.NEGATIVE_INFINITY;
    var maxInventory;
    var min = Number.POSITIVE_INFINITY;
    var minInventory;

    $.each(item.flavorInventory, function(index, value, array) {

        if (value.availableAmount > max) {
            max = value.availableAmount;
            maxInventory = value.flavor;
        }

        if (value.availableAmount < min) {
            min = value.availableAmount;
            minInventory = value.flavor;
        }
        
        if (value.ioOptimized == true){
            value.ioOptimized = '是';
        }else{
            value.ioOptimized = '否';
        }
        
        value.cpuMem = value.cpu + '_' + value.memory;
        value.lowHighLevel = value.lowLevel + '/' + value.highLevel;
        
        $this.initStatus(value.flavor, value.status);
        
        $this.$checkResouceTenseStatus(value, status);

    });

    row.maxInventory = maxInventory + '</br></br>' + max;
    row.minInventory = minInventory + '</br></br>' + min;
    
    row.inventory = item.flavorInventory;

    var generation = item.generation;
    if (generation != null) {
        generation = generation.replace(',', '</br>');
    }
    row.generation = generation;
    
    if (item.finalStatus != null && item.finalStatus.toLowerCase() != 'offline'){
        if (status.state == 'tense'){
            row.zone = '<font color=orange >' + item.zone + '</font>';
        }else{
            row.zone = item.zone;
        }
        
    }else{
        row.zone = $this.initStatus(item.zone, item.finalStatus);
    }

}

InventoryTable.prototype.initStatus = function(raw, status) {
    
    var data = '';
    
    if (status != null && status.toLowerCase() == 'offline'){
        data = '<font color=red>' + raw + '</font>';
    }else{
        data = raw;
    }
    
    return data;
}


InventoryTable.prototype.$alertHtml = '<div class="alert alert-warning"><strong>Warning!</strong>&nbsp;load inventory data failed.</div>';

InventoryTable.prototype.loadData = function() {
    var $this = this;
    $(this.selector).bootstrapTable('showLoading');

    $.get("/ops/json/inventory", function(data, status) {
        $($this.selector).bootstrapTable('hideLoading');

        if (status != 'success') {
            $($this.selector).html($this.$alertHtml);
            return;
        }

        $this.renderView(data, $this);
        
        // 将数据加载事件广播出去
        for (var i = 0; i < $this.dataLoadedHandlers.length; ++i) {
            $this.dataLoadedHandlers[i](data);
        }
    });
}

InventoryTable.prototype.renderView = function(data, $this) {
    // 将访问服务器得到的库存信息缓存到本地
    $this.data = data.inventories;
    var copiedData = JSON.parse(JSON.stringify(data.inventories));
    copiedData = $this.$dataPreProcessor(copiedData, $this.level, $this);
    $this.onDataLoaded(copiedData, $this);
}

InventoryTable.prototype.updateView = function(level, $this) {
    $this.level = level;
    var copiedData = JSON.parse(JSON.stringify($this.data));
    copiedData = $this.$dataPreProcessor(copiedData, $this.level, $this);

    $($this.selector).bootstrapTable('destroy');
    $this.onDataLoaded(copiedData, $this);
}

/* 注册数据加载完成事件的响应handler: onDataLoaded(data); */
InventoryTable.prototype.registerDataLoadedHandler = function(handler) {
    this.dataLoadedHandlers.push(handler);
}

InventoryTable.prototype.$levelToVariable = {'BigRegion': 'region', 'AvailableZone': 'availableZone', 'Cluster': 'cluster', 'Zone': 'zone'};
InventoryTable.prototype.$levelToTitle = {'BigRegion': 'Region', 'AvailableZone': 'AvailableZone', 'Cluster': 'Cluster', 'Zone': 'Zone'};
InventoryTable.prototype.$levelToOrder = {'BigRegion': 3, 'AvailableZone': 2, 'Cluster': 1, 'Zone': 0};

InventoryTable.prototype.onDataLoaded = function(data, $this) {
    
    $(this.selector).bootstrapTable({
        data: data,
        uniqueId: $this.$levelToVariable[$this.level],
        
        columns: $this.$getColumns(),
        striped: $this,
        cache: true,
        pagination: true,
        sortable: false,
        search: true,
        showColumns: true,
        showRefresh: true,
        clickToSelect: true,
        showToggle:true,
        cardView: false,
        detailView: true,
        onExpandRow: $this.onRowExpand,
        onClickCell: $this.onRowClick,
        inventoryTable: $this, // 为计算方便，将inventoryTable对象装入bootstrap-table的options中
    });
}

/* 展开库存表格的一行展示更多库存信息 */
InventoryTable.prototype.onRowExpand = function(index, row, $detail) {
    var subTable = $detail.html('<table></table>').find('table');

    var bootstrapTable = $($detail.closest('table'));
    var options = bootstrapTable.bootstrapTable('getOptions');

    var $this = options.inventoryTable;
    $(subTable).bootstrapTable({
        data: row.inventory,
        uniqueId: $this.$levelToVariable[$this.level],
        columns: $this.$getChildColumns($this.level),
        clickToSelect: true,
        detailView: false,
    });
}

/* hack代码将bootstrap-table的onClickRow转化为onExpandRow */
InventoryTable.prototype.onRowClick = function(filed, title, row, $element) {
    
    // 禁止detail列的点击事件
    if (filed == 'detail'){
        return;
    }
    
    var $element =  $element.parent();
    
    var index = $element.data('index');

    // tr元素最近的父table节点就是bootstrap-table
    var bootstrapTable = $($element.closest('table'));
    if ($element.next().is('tr.detail-view')) {
          bootstrapTable.bootstrapTable('collapseRow', index);
    } else {
        bootstrapTable.bootstrapTable('collapseAllRows');
        bootstrapTable.bootstrapTable('expandRow', index);
    }
}

function tableNodeSwitchLevelHandler(fromLevel, toLevel) {
    if (fromLevel == toLevel) return;

    var options = $('#inventory-table').bootstrapTable('getOptions');
    var inventoryTable = options['inventoryTable'];
    inventoryTable.updateView(toLevel, inventoryTable);
}

function tableNodesSelectedHandler(idsToExpand, level) {
    if (idsToExpand.length <= 0) return;

    var indexToExpand = [];
    var variableName = InventoryTable.prototype.$levelToVariable[level];

    var data = $('#inventory-table').bootstrapTable('getData', false);
    for (var i = 0; i < data.length; ++i) {
        if (idsToExpand.includes(data[i][variableName])) {
            indexToExpand.push(i);
        }
    }

    if (indexToExpand.length <= 0) return;

    $('#inventory-table').bootstrapTable('collapseAllRows');
    for (var i = 0; i < indexToExpand.length; ++i) {
        $('#inventory-table').bootstrapTable('expandRow', indexToExpand[i]);
    }
}

