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
jq(function() { 

     tbl = jq('#tableid').dataTable({
            "sPaginationType": "full_numbers",
            "bPaginate": true,
            "bAutoWidth": false,
            "bLengthChange": true,
            "bSort": true,
            "bJQueryUI": true
        });
});
</script>

<table id="tableid" border="1" class="display" cellspacing="0" width="50%">
    <thead>
        <tr> 
                <th>Files to Preview</th>
        </tr>
    </thead>
    <tbody>

<% if (files) { %>
     <% files.each { %>
      <tr>
<td><a target="_blank" href="preview.page?file=${ ui.format(it) }">${ ui.format(it) }</a></td>
      </tr>

    <% } %>
  <% } else { %>
  <tr>
    <td colspan="2">${ ui.message("general.none") }</td>
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


<h1>UPLOAD FILE(S)</h1>
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
                var fullFilename = JSON.parse(jsonResponse).fullFilename;
                var newRow = "<a target=_blank href=preview.page?file=" + fullFilename + " }>" + fullFilename + "</a>";
                tbl.fnAddData( [newRow]);
                tbl.fnDraw();
                document.getElementById("file-input").value = "";
//location.reload();
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




