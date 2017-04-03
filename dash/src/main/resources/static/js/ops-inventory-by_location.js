// 保存inventory->by_location页面当前的level
global_inventory_by_location_level = 'zone';

/* 比较两个level的大小 */
var levelMap = {
    'BigRegion' : 3,
    'AvailableZone' : 2,
    'Cluster' : 1,
    'Zone' : 0
};
function compareLevel(level1, level2) {
    return levelMap[level1] - levelMap[level2];
}

/* 索引树数据加载事件处理器 onTreeNodeLoaded(treeData) */
var onTreeNodeLoadedHandlers = [];
function registerOnTreeNodeLoadedEvent(handler) {
    onTreeNodeLoadedHandlers.push(handler);
}

/*
 * 将/ops/json/tree/region的返回值解析为索引树节点 bootstrap-tree的数据结构{text: 展示名字, nodes:
 * {递归的树结构}, tags:[标签]} 为方便调用，这里tags[0] = bigRegionId, tags[1] = level
 * (BigRegion, AvailableZone, Cluster, Zone, 这与index页面的#levelSelector一致的)
 */
function renderTree(data) {

    for (var i = 0; i < onTreeNodeLoadedHandlers.length; ++i) {
        onTreeNodeLoadedHandlers[i](data);
    }

    var treeData = [];
    for (var i = 0; i < data.length; ++i) {
        var bigRegionObj = data[i];

        var availableZoneNodes = [];
        var bigRegion = {
            'text' : bigRegionObj.bigRegion,
            'nodes' : availableZoneNodes,
            'tags' : [ bigRegionObj.bigRegion, 'BigRegion' ]
        };
        treeData.push(bigRegion);

        var azList = bigRegionObj.availableZones;
        for (var j = 0; j < azList.length; ++j) {
            var azObj = azList[j];

            var clusterNodes = [];
            var availableZone = {
                'text' : azObj.availableZone,
                'nodes' : clusterNodes,
                'tags' : [ bigRegionObj.bigRegion, 'AvailableZone' ],
                'state' : {
                    'expanded' : true
                }
            };
            availableZoneNodes.push(availableZone);

            var clusterList = azObj.clusters;
            for (var k = 0; k < clusterList.length; ++k) {
                var clusterObj = clusterList[k];

                var zoneNodes = [];
                var cluster = {
                    'text' : clusterObj.cluster,
                    'nodes' : zoneNodes,
                    'tags' : [ bigRegionObj.bigRegion, 'Cluster' ]
                };
                clusterNodes.push(cluster);

                var zoneList = clusterObj.zones;
                for (var n = 0; n < zoneList.length; ++n) {
                    var zone = zoneList[n];

                    zoneNodes.push({
                        'text' : zone,
                        'tags' : [ bigRegionObj.bigRegion, 'Zone' ]
                    });
                }
            }
        }
    }

    return treeData;
}

/* 索引树节点被选中 onTreeNodeSelected(idsToExpand, level) */
var onTreeNodeSelectedHandlers = [];
function registerOnTreeNodeSelectedEvent(handler) {
    onTreeNodeSelectedHandlers.push(handler);
}

/*
 * 索引树节点被选中事件处理 展开全部关联节点
 */
var onTreeNodeSelected = function(event, node) {

    var level = $('#location-level-selector').levelSelector('getLevel');
    var selectedLevel = node.tags[1];

    var idsToExpand = [];
    var workQueue = [ node ];
    while (workQueue.length > 0) {
        var cNode = workQueue.shift();
        var cLevel = cNode.tags[1];

        var flag = compareLevel(cLevel, level);
        if (flag == 0) {
            idsToExpand.push(cNode.text);
        } else if (flag > 0) {
            var children = cNode.nodes;
            for (var i = 0; i < children.length; ++i) {
                workQueue.push(children[i]);
            }
        } else {
            var parent = $('#region-tree').treeview('getParent', cNode);
            workQueue.push(parent);
        }
    }

    for (var i = 0; i < onTreeNodeSelectedHandlers.length; ++i) {
        onTreeNodeSelectedHandlers[i](idsToExpand, level);
    }
}

/* 初始化索引树节点 */
function initRegionTree() {
    $.get("/ops/json/tree/region", function(data, status) {
        if (status != 'success')
            return;

        $('#region-tree').treeview({
            showIcon : true,
            data : renderTree(data),
            highlightSearchResults: false
        }).on('nodeSelected', onTreeNodeSelected);
    });
}

/*
 * 获取搜索树的相关信息
 */
function getSearchInfo(data) {

    var searchInfo = {};

    if (data == null) {
        searchInfo.value = ""
    } else {
        var infos = [];
        for (var i = 0; i < data.length; i++) {

            var bigRegionObj = data[i];
            var azs = bigRegionObj.availableZones;

            for (var j = 0; j < azs.length; ++j) {

                var azObj = azs[j];
                var clusters = azObj.clusters;

                for (var k = 0; k < clusters.length; ++k) {
                    var clusterObj = clusters[k];

                    var zones = clusterObj.zones;
                    for (var n = 0; n < zones.length; ++n) {
                        var zone = zones[n];

                        var info = {};
                        info.BigRegion = bigRegionObj.bigRegion;
                        info.AZone = azObj.availableZone;
                        info.Cluster = clusterObj.cluster;
                        info.Zone = zone;

                        infos.push(info);
                    }
                }
            }
        }

        searchInfo.value = infos;
    }

    return searchInfo;
}

function collapseAll(){
    $('#region-tree').treeview('collapseAll', { silent: true });
}

/* inventory -> by_location 页面加载时触发的函数 */
function loadPageInventoryByLocation() {
    registerOnTreeNodeLoadedEvent(function(treeData){

          $("#region-tree-search").bsSuggest({
              data: getSearchInfo(treeData),
              ignorecase: true,
              showHeader: true,
              showBtn: false,     // 不显示下拉按钮
              getDataMethod: "data",
              idField: "Zone",
              keyField: "Zone",
              clearable: true
          }).on('onSetSelectValue', function (event, keyword, data) {

              var node = $('#region-tree').treeview('search', [ data.Zone, {
                  ignoreCase: false,     // case insensitive
                  exactMatch: true,      // like or equals
                  revealResults: false,  // reveal matching nodes
                }]);
              $('#region-tree').treeview('revealNode', [ node, { silent: true } ]);
              $('#region-tree').treeview('selectNode', [ node, { silent: false } ]);
          });
    });

    // 初始化levelSelector，并注册click响应事件
    $('#location-level-selector').levelSelector();
    $('#location-level-selector').levelSelector('registerOnClickEvent', tableNodeSwitchLevelHandler);

    // tableNodesSelectedHandler: ops-plugin-inventory-table.js
    registerOnTreeNodeSelectedEvent(tableNodesSelectedHandler);

    initRegionTree();    // ops-inventory-by_location.js

    // ops-plugin-inventory-table.js
    var inventoryTable = new InventoryTable('#inventory-table', 'Zone');
}

