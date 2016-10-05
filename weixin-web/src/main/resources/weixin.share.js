wx.ready(function () {
    var title = '_TITLE_';
    var desc = '_DESC_';
    var link = '_LINK_';
    var imgUrl = '_IMAGE_URL_';
    var success = _SUCCESS_;
    var cancel = _CANCEL_;

    if (title.length == 0) {
        title = document.title;
    }
    if (desc.length == 0) {
        desc = title;
    }
    if (link.length == 0) {
        link = location.href + '#fromWX';
    }
    if (imgUrl.length == 0)
        imgUrl = null;

    wx.onMenuShareTimeline({
        title: title,
        link: link,
        imgUrl: imgUrl,
        success: function () {
            if (success)
                success('timeLine', arguments);
        },
        cancel: function () {
            if (cancel)
                cancel('timeLine', arguments);
        }
    });

    wx.onMenuShareAppMessage({
        title: title,
        desc: desc,
        link: link,
        imgUrl: imgUrl,
        success: function () {
            if (success)
                success('message', arguments);
        },
        cancel: function () {
            if (cancel)
                cancel('message', arguments);
        }
    });

    wx.onMenuShareQQ({
        title: title,
        desc: desc,
        link: link,
        imgUrl: imgUrl,
        success: function () {
            if (success)
                success('qq', arguments);
        },
        cancel: function () {
            if (cancel)
                cancel('qq', arguments);
        }
    });

    wx.onMenuShareWeibo({
        title: title,
        desc: desc,
        link: link,
        imgUrl: imgUrl,
        success: function () {
            if (success)
                success('weibo', arguments);
        },
        cancel: function () {
            if (cancel)
                cancel('weibo', arguments);
        }
    });

    wx.onMenuShareQZone({
        title: title,
        desc: desc,
        link: link,
        imgUrl: imgUrl,
        success: function () {
            if (success)
                success('qqZone', arguments);
        },
        cancel: function () {
            if (success)
                success('qqZone', arguments);
        }
    });
});