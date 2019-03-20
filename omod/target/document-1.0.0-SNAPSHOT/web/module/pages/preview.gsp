
<% ui.decorateWith("appui", "standardEmrPage") %>

<style>
    .frame{
        width: 100%;
        height: 768px;
    }
</style>

<% if (filename) { %>
    <strong>File : ${filename} </strong>
    <iframe src="document.form?file=${filename}" class="frame">
        <p style="font-size: 110%;"><em><strong>ERROR: </strong>
            An &#105;frame should be displayed here but your browser version does not support &#105;frames. </em>Please update your browser to its most recent version and try again.</p>
    </iframe>
<% } else { %>
    <p>Please provide the filename in url as : <em>preview.page?file={filename} </em></p>
<% } %>

