const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        reloadData(data);
    });
}

function checkEnabled(id, enabled) {
    $.ajax({
        url: ctx.ajaxUrl + id + "/enable?isEnabled=" + enabled,
        type: "PATCH",
        success: function () {
            document.getElementById(id).setAttribute("data-user-enabled",enabled);
            successNoty(enabled ? "Enabled" : "Disabled");
        },
        error: function () {
            updateTable();
        }
    });
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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