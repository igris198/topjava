const userAjaxUrl = "ajax/meals/";

const ctx = {
    ajaxUrl: userAjaxUrl
};

function clearFilter() {
    $('#filter').find(":input").val("");
    updateTable();
    successNoty("Cleared");
}

function updateTable() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: $('#filter').serialize()
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});