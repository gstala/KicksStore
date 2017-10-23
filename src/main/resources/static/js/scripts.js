
function checkPasswordMatch() {
    var password = $("#txtNewPassword").val();
    var confirmPassword = $("#txtConfirmPassword").val();

    if(password == "" && confirmPassword =="") {
        $("#checkPasswordMatch").html("");
        $("#updateUserInfoButton").prop('disabled', false);
    } else {
        if(password != confirmPassword) {
            $("#checkPasswordMatch").html("Passwords do not match!");
            $("#updateUserInfoButton").prop('disabled', true);
        } else {
            $("#checkPasswordMatch").html("Passwords match");
            $("#updateUserInfoButton").prop('disabled', false);
        }
    }
}

function setOrderTotal() {

    var shippingMethodCostValue = parseFloat($('#shippingMethodCost').text());
    var grandTotalCostValue = parseFloat($('#grandTotalCost').text());
    var orderTotalCost = $('#orderTotalCost');
    orderTotalCost.html(grandTotalCostValue + shippingMethodCostValue);
}

$(document).ready(function(){


    $('#priorityParcel').on('click',function () {
       $('#shippingMethodCost').html('20.00');
        setOrderTotal();
    });
    $('#ecoParcel').on('click',function () {
        $('#shippingMethodCost').html('10.00');
        setOrderTotal();
    });

    $(".cartItemQty").on('change', function(){
        var id=this.id;

        $('#update-item-'+id).css('display', 'inline-block');
    });
    $("#txtConfirmPassword").keyup(checkPasswordMatch);
    $("#txtNewPassword").keyup(checkPasswordMatch);

    //////

    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    });

    $('.delete-product').on('click', function (){
        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'remove';
        /*]]>*/
        var id=$(this).attr('id');

        bootbox.confirm({
            message: "Are you sure to remove this product? It can't be undone.",
            buttons: {
                cancel: {
                    label:'<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label:'<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function(confirmed) {
                if(confirmed) {
                    $.post(path, {'id':id}, function(res) {
                        location.reload();
                    });
                }
            }
        });
    });


    $('#deleteSelected').click(function() {
        var idList= $('.checkboxProduct');
        var productIdList=[];
        for (var i = 0; i < idList.length; i++) {
            if(idList[i].checked==true) {
                productIdList.push(idList[i]['id'])
            }
        }

        console.log(productIdList);

        /*<![CDATA[*/
        var path = /*[[@{/}]]*/'removeList';
        /*]]>*/

        bootbox.confirm({
            message: "Are you sure to remove all selected products? It can't be undone.",
            buttons: {
                cancel: {
                    label:'<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label:'<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function(confirmed) {
                if(confirmed) {
                    $.ajax({
                        type: 'POST',
                        url: path,
                        data: JSON.stringify(productIdList),
                        contentType: "application/json",
                        success: function(res) {
                            console.log(res);
                            location.reload()
                        },
                        error: function(res){
                            console.log(res);
                            location.reload();
                        }
                    });
                }
            }
        });
    });

    $("#selectAllProducts").click(function() {
        if($(this).prop("checked")==true) {
            $(".checkboxProduct").prop("checked",true);
        } else if ($(this).prop("checked")==false) {
            $(".checkboxProduct").prop("checked",false);
        }
    })



});