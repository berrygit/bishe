/**
 * 页面层级选择器
 * 将<nav>标签扩展为层级选择器。
 * 注册选择器click事件
 */

(function ($) {
    'use strict';

    var invokeOnClickHandler = function(handler, fromLevel, toLevel) {
        if (handler == null || typeof handler === 'undefined') {
            return;
        }
        if (typeof handler === 'object') {
            var _handler = handler['onLevelSelectorClickHandler'];
            if (typeof _handler === 'function') {
                _handler(fromLevel, toLevel);
            }
        } else if (typeof handler === 'function') {
            handler(fromLevel, toLevel);
        }
    };

    var LevelSelector = function (container) {
        this.container = $(container);
        this.selectorId = 'level-selector-' + (new Date().getTime() % 1000) + '-' + parseInt(Math.random() * 1000);
        this.selector = '#' + this.selectorId;
        this.onClickHandlers = [];

        this.init();
    };

    /* 初始化selector的container（父容器）；注册click事件响应 */
    LevelSelector.prototype.init = function() {
        this.container.addClass('navbar').addClass('navbar-default').attr('role', 'navigation');
        this.container.html(
            '<div class="container-fluid">' +
                '<div class="navbar-collapse collapse">' +
                    '<ul id="' + this.selectorId + '" class="nav navbar-nav">' +
                        '<li><a href="javascript:void(0);">BigRegion</a></li>' +
                        '<li><a href="javascript:void(0);">AvailableZone</a></li>' +
                        '<li><a href="javascript:void(0);">Cluster</a></li>' +
                        '<li class="active"><a href="javascript:void(0);">Zone</a></li>' +
                    '</ul>' +
                '</div>' +
            '</div>'
        );

        var $this = this;
        /* 为selector子项注册click事件响应 */
        $(this.selector).children().click(function(event) {
            var fromLevel = $this.getLevel();
            $($this.selector).children().removeClass('active');

            var li = $(event.currentTarget);
            li.addClass('active');
            var toLevel = li.find('a').text();

            $.each($this.onClickHandlers, function(index, onClickHandler, array) {
                invokeOnClickHandler(onClickHandler, fromLevel, toLevel);
            });
        });
    };

    LevelSelector.prototype.destroy = function() {
        this.container.removeClass('navbar').removeClass('navbar-default').removeAttr('role');
        this.container.html('');
    };

    /* 获取当前层级 */
    LevelSelector.prototype.getLevel = function() {
        return $(this.selector).find('.active').text();
    };

    /* 注册层级选择器click事件响应函数 */
    LevelSelector.prototype.registerOnClickEvent = function(handler) {
        this.onClickHandlers.push(handler);
    };

    var allowedMethods = [
        'getLevel', 'registerOnClickEvent', 'destroy'
    ];

    $.fn.levelSelector = function (option) {
        var value;
        var args = Array.prototype.slice.call(arguments, 1);

        this.each(function () {
            var $this = $(this);
            var _levelSelector = $this.data('levelSelector');

            if (!_levelSelector) {
                $this.data('levelSelector', (_levelSelector = new LevelSelector(this)));
            }
            else if (typeof option === 'string') {
                if ($.inArray(option, allowedMethods) < 0) {
                    throw new Error("Unknown method: " + option);
                }
                if (!_levelSelector) {
                    throw new Error("LevelSelector uninitialized. " + option);
                }

                value = _levelSelector[option].apply(_levelSelector, args);

                if (option === 'destroy') {
                    $this.removeData('levelSelector');
                }
            }
        });

        return typeof value === 'undefined' ? this : value;
    };

    $.fn.levelSelector.Constructor = LevelSelector;

})(jQuery);
