$(function () {
    // console.error('start ajax');
    $.ajax('CONFIG_URI', {
        method: 'post',
        contentType: 'text/plain',
        // location.href.split('#')[0]
        // encodeURIComponent(location.href.split('#')[0])
        data: location.href.split('#')[0],
        dataType: 'json',
        success: function (data) {
            data.jsApiList = API_LIST;
            wx.config(data);
            wx.testConfigDone = true;
        }
    });
});
