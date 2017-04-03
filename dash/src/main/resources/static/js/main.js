/*
 * 注册和处理index页面菜单（#navbar）的click事件。
 * 行为：选择菜单，加载对应子页面。
 *
 * 实现：
 *   1，捕获当前click事件，解析出目标页面路径nextPath；
 *   2，读取当前为active的tab，解析出上一次页面路径lastPath；
 *   3，如果nextPath == lastPath，不做操作；
 *   4，否则，清空active标记，重新为nextPath节点加active标记；
 *   5，加载nextPath对应的页面。如果页面已经加载过，则显示上次加载过的页面；
 *   6，否则，ajax加载nextPath页面，并在加载成功后，执行相应的响应函数。
 *
 * 页面路径 - 子页面ID - 加载函数 映射关系
 *   eg. inventory/by_location - page-inventory-by_location - loadPageInventoryByLocation
 *   子页面ID = 'page-' + 页面路径.replace('/', '-')
 *   加载函数 = 'loadPage' + 页面路径 '/'和'-'移除 各单词首字母大写
 */

/*
 * 将'页面路径'转化为'加载函数'
 * 加载函数 = 'loadPage' + 页面路径 '/'和'-'移除 各单词首字母大写
 * 需要为每个子页面添加loadPage函数，如'inventory -> by_location'页面，loadPageInventoryByLocation
 */
 function toLodePageHandler(path) {
     var list = path.split(/\/|_/);
     for (var i = 0; i < list.length; ++i) {
         list[i] = list[i].slice(0, 1).toUpperCase() + list[i].slice(1);
     }
     return window["load" + list.join('')];
 }

// 触发函数执行
 function invokeFunction(theFunction, params) {
     if (theFunction = null) { return; }

     var type = typeof(theFunction);
     if ('function' == type) {
         theFunction(params);
     } else if ('string' == type) {
         theFunction = window[theFunction];
         if (theFunction == null) { return; }
         theFunction(params);
     }
 }

/*
 * 加载页面
 * @param targetPage 目标'页面路径'
 * @param onLoadFailHandler 加载页面失败响应函数
 */
function loadPage(targetPage, onLoadFailHandler) {
    var container = $('#main-container');
    var subPageId = '#' + targetPage.replace('/', '-');

    // 隐藏全部子页面
    container.find('>.row').addClass('hide');

    // 只显示目标子页面（事实上是css为'.row'的div）
    var subPage = container.find(subPageId);
    if (subPage.length > 0) {
        subPage.removeClass('hide');
        
        var afterPageLoadHandler = toLodePageHandler(targetPage);
        if (afterPageLoadHandler == null) { return; }
        afterPageLoadHandler();
        
        return;
    }

    // 如果子页面不在本地，重加载
    $.get("/" + targetPage, function(data, status) {
        if (status != 'success') {
            invokeFunction(onLoadFailHandler, {'data': data, 'status': status});
            return;
        }

        // 重新加载页面
        $('#main-container').append(data);

        var afterPageLoadHandler = toLodePageHandler(targetPage);
        if (afterPageLoadHandler == null) { return; }
        afterPageLoadHandler();
    });
}

/*
 * 该函数在启动时执行：为index.html页面的菜单栏（'#navbar'）添加click事件响应
 */
$('#navbar').children().click(function(event) {
    var a = $(event.target);
    var page = a.attr('href').substring(1); // <a>标签的href属性值，'#'+目标子页面标识。这里移除'#'以得到"子页面标识"
    if (page.length == 0) { return; }

    var navbar = $(event.currentTarget);

    var lastActiveA = navbar.find('.active:last>a');
    var lastPage = lastActiveA.attr('href').substring(1);
    if (lastPage == page) { return; }

    navbar.find('li').removeClass('active');

    a.parent().addClass('active');

    loadPage(page, function() {
        loadPage(lastPage);
    });
});

