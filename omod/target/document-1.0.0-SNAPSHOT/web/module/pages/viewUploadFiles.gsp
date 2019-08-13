<%
 ui.decorateWith("appui", "standardEmrPage")

ui.includeJavascript("uicommons", "datatables/jquery.dataTables.min.js")
ui.includeCss("uicommons", "datatables/dataTables_jui.css")
%>



<script type="text/javascript">
            //var OPENMRS_CONTEXT_PATH = 'openmrs';
            window.sessionContext = window.sessionContext || {
                locale: "en_GB"
            };
            window.translations = window.translations || {};
</script>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label:"${patient.familyName}, ${patient.givenName}",
          link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/clinicianfacing/patient.page?patientId=${patient.patientId}&app=pih.app.clinicianDashboard' },
        { label: "View Files"}
    ];
</script>

<script>
var tbl;
var rowToDelete;
var previewTableBody;
var xhrDelete;
var deleteDocId;
var deletePatientId;

function callbackDeleteDoc(shouldDelete) {
    if (!shouldDelete) { return; }
        var deleteformData = new FormData();
            deleteformData.append("docId", deleteDocId);
            deleteformData.append("patientId", deletePatientId);
            
        xhrDelete = new XMLHttpRequest();
        var uriDelete = "delete.form";
        xhrDelete.open("POST", uriDelete);
        
            var result = xhrDelete.send(deleteformData);
location.reload(true)

   //     previewTableBody.removeChild(rowToDelete);

};

jq(function() { 

     tbl = jq('#tableid').dataTable({
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });

        
jq( "#dialog-confirm-deleteDoc" ).dialog({
    autoOpen: false,
      resizable: false,
      height: "auto",
      width: 400,
      modal: true,
      buttons: {
        "Delete Document": function() {
            jq(this).dialog("close");
            callbackDeleteDoc(true);
        },
        Cancel: function() {
            jq(this).dialog("close");
            callbackDeleteDoc(false);
        }
      }
    });

});

            function trashDoc(event,em,docId,patientId) {
                event.stopPropagation();
                rowToDelete = em.closest("tr");
                previewTableBody = em.closest("tbody");
                deleteDocId = docId;
                deletePatientId = patientId;
jq( "#dialog-confirm-deleteDoc" ).dialog( "open" );
            }
function getDate(date1) {
date = new Date(date1);

var day = date.getDate();
if (day < 10) {
    day = "0" + day;
}
var month = date.getMonth() + 1
if (month < 10) { month = "0" + month; }
var year = date.getFullYear();
var hours = date.getHours();
if (hours < 10) { hours = "0" + hours; }
var minutes = date.getMinutes();
if (minutes < 10) { minutes = "0" + minutes; }
var seconds = date.getSeconds();
if (seconds < 10) { seconds = "0" + seconds; }
return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds + ".0";
}

 

</script>

<table id="tableid" border="1" class="display" cellspacing="0" width="50%">
    <thead>
        <tr> 
                <th></th><th>Files to Preview</th><th>Doc Type</th><th>Description</th><th>Date</th>
        </tr>
    </thead>
    <tbody>

<% if (docs) { %>
     <% docs.each { %>
      <tr>
<td><em class="icon-trash delete-action" title="Delete Doc"  onclick="trashDoc(event,this,$it.id,$it.patientId)"></em></td>
<td><a target="_blank" href="preview.page?file=${ ui.format(it.serverFileName) }">${ ui.format(it.originalFileName) }</a></td>
<td>${it.docType}</td><td>${it.description}</td><td>${it.dateCreated}</td>
      </tr>

    <% } %>
  <% } else { %>
  <tr>
    <td colspan="2">${ ui.message("general.none") }</td><td></td><td></td><td></td><td></td>
  </tr>
  <% } %>
    </tbody>
</table>


<style>
    #progress-bar {
        margin: 8px;
        width: auto;
        background-color: lightgray;
    }

    #progress {
        width: 1%;
        height: 30px;
        background-color: green;
        text-align: center;
        color: white;
    }
</style>

<div id="dialog-confirm-deleteDoc" title="Delete Document?">
  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:12px 12px 20px 0;"></span>This document will be permanently deleted and cannot be recovered. Are you sure?</p>
</div>

<h2>UPLOAD FILE(S)</h2></br></br>
<b><em>Document Type:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<select id="selectId">
<option selected value="Test Result">Test Result</option>
<% if (types) { %>
 <% types.each { type->%>
    <option value="${type}">${type}</option>
     <% } %>

  <% } %>
     <option value="Other">Other</option>
   </select>
</br></br>


<b><em>Document Description:&nbsp&nbsp</em></b><input type="text" id="docDescription" size="60">
</br></br>
<label for="file-input">File (pdf,jpg,gif,png):</label>
<input id="file-input" multiple type="file" accept=".pdf, .gif, .jpg, .png, .pdf" oninput="clearProgressBar()">

<button id="send">Send</button>

<div id="progress-bar">
    <div id="progress"></div>
</div>

<script>
    const sendButton = document.querySelector('#send');
    sendButton.addEventListener(
        'click',
        function (e) {
            console.log("Send button clicked");
            if (document.getElementById("docDescription").value == "") {
                alert("Please provide description");
                return;
                }
            uploadFiles();
        },
        false);

    const fileInput = document.querySelector('#file-input');
    function clearProgressBar() {
            updateProgress(0);
    }

    function uploadFiles() {
        // var file = fileInput.files[0];
        for (var i = 0; i < fileInput.files.length; i++) {
            //alert("file: " + fileInput.files[i].name);
            file = fileInput.files[i];
            if(file){
                console.log(file.name, " selected.");
                initUpload(file);
            } else {
                console.log("Please select the file.")
            }
        }
    }

    function initUpload(file) {
        var xhr = new XMLHttpRequest();
        var uri = "upload.form";
        xhr.open("POST", uri);
        xhr.overrideMimeType('text/plain; charset=x-user-defined-binary');

        xhr.upload.onprogress = function(e) {
            var percentComplete = Math.ceil((e.loaded / e.total) * 100);
            console.log(percentComplete);
            updateProgress(percentComplete);
        };

        xhr.onreadystatechange = function() {
            console.log("----- readyState changed -----");
            console.log("readyState :", xhr.readyState);
            console.log("Response :", xhr.responseText);
            if (xhr.readyState === 4) {
                console.log("Upload finish");

                var jsonResponse = this.responseText;
                location.reload(true)


/*
                var serverFileName =    JSON.parse(jsonResponse).serverFileName;
                var originalFileName =  JSON.parse(jsonResponse).originalFileName;
                patientId = JSON.parse(jsonResponse).patientId;
                docId = JSON.parse(jsonResponse).docId;
                var docDescription =    document.getElementById("docDescription").value;
                var date = getDate(new Date().toString());
                var fileLink = "<a target=_blank href=preview.page?file=" + serverFileName + " }>" + originalFileName + "</a>";

                var trash = "<em class='icon-trash delete-action' title='Delete Doc'  onclick='trashDoc(event,this,docId,patientId)'></em>";

                tbl.fnAddData( [trash,fileLink,docType,docDescription,date]);
                tbl.fnDraw();
                document.getElementById("file-input").value = "";
*/
            }
        };

var parts = location.search.substring(1).split('&');
var patientId = parts[0].split('=')[1];

        var formData = new FormData();
        var reader = new FileReader();
        reader.onload = (function (e) {
            var previewFile = e.target.result;
            formData.append("filename", file.name);
            formData.append("file", previewFile);
            formData.append("patientId", patientId);
var docType = jq('#selectId').val();
if (docType == "") { docType = "Test Result"; }
var descript = document.getElementById("docDescription").value;
formData.append("docDescription", descript);
formData.append("docType", docType);
            var result = xhr.send(formData);
        });
        reader.readAsDataURL(file);

    }

    var elem = document.getElementById("progress");

    function updateProgress(progress) {
        elem.style.width = progress + '%';
        elem.innerHTML = progress + '%';
    }

</script>

<script id="breadcrumb-template" type="text/template">
    <li>
        {{ if (!first) { }}
        <i class="icon-chevron-right link"></i>
        {{ } }}
        {{ if (!last && breadcrumb.link) { }}
        <a href="{{= breadcrumb.link }}">
        {{ } }}
        {{ if (breadcrumb.icon) { }}
        <i class="{{= breadcrumb.icon }} small"></i>
        {{ } }}
        {{ if (breadcrumb.label) { }}
        {{= breadcrumb.label }}
        {{ } }}
        {{ if (!last && breadcrumb.link) { }}
        </a>
        {{ } }}
    </li>
</script> 








<script type="text/javascript">
    jq(function() {
        emr.updateBreadcrumbs();
    });
</script>




