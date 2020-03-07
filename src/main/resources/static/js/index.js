$(document).ready(function() {

    var $sendParseRequestButtons = $('[role=sendParseRequest]')
    var $trainModelButtons = $('[role=trainModel]')

    function refreshProductInfo(data) {
        console.log(data);
        $sendParseRequestButtons.prop("disabled", true);
    }

    function refreshTrainInfoDone(data) {
        console.log(data);
        $trainModelButtons.html("Процесс запущен");
        $trainModelButtons.prop("value", "Процесс запущен");
    }

    function refreshTrainInfoFail(data) {
            console.log(data);
            $trainModelButtons.html("Процесс уже запущен");
            $trainModelButtons.prop("value", "Процесс уже запущен");
        }

    $trainModelButtons.each(function() {
        var alias = $(this).attr('data-alias')
        console.log(alias)
        $(this).on('click', function () {
            $.ajax({
                type: "POST",
                url: "/api/v1/gate/product-determination/train/" + alias,
            }).done(function(data) {
                refreshTrainInfoDone.call(this, data);
            }).fail(function(data) {
                refreshTrainInfoFail.call(this, data);
            })
        });
    });


    $sendParseRequestButtons.each(function() {
        var productId = $(this).attr('data-productId')
        $(this).on('click', function () {
            $.ajax({
                type: "POST",
                url: "/api/v1/gate/product-info/products/parse",
                contentType: "application/json",
                data: productId,
            }).done(function(data) {
                refreshProductInfo.call(this, data);
            })
        });
    })
})