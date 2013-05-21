<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%

%>
<html>
    <meta charset="utf-8">
    <title>Akka-Spring</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="<c:url value="/css/bootstrap.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/bootstrap-responsive.css"/>" rel="stylesheet">

    <head>
        <script src="<c:url value="/js/jquery.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-transition.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-transition.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-alert.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-modal.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-dropdown.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-scrollspy.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-tab.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-tooltip.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-popover.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-button.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-collapse.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-carousel.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-typeahead.js"/>"></script>
        <script src="<c:url value="/scripts/d3/d3.js"/>"></script>
        <script src="<c:url value="/scripts/d3/d3.layout.cloud.js"/>"></script>
        <script type="text/javascript">
            var fill = d3.scale.category20();

            function draw(words) {
                d3.select("#aggregate-word-cloud").append("svg")
                        .attr("width", 300)
                        .attr("height", 300)
                        .append("g")
                        .attr("transform", "translate(150,150)")
                        .selectAll("text")
                        .data(words)
                        .enter().append("text")
                        .style("font-size", function(d) { return d.size + "px"; })
                        .style("font-family", "Impact")
                        .style("fill", function(d, i) { return fill(i); })
                        .attr("text-anchor", "middle")
                        .attr("transform", function(d) {
                            return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                        })
                        .text(function(d) { return d.text; });
            }

            if (!window.WebSocket){
                alert("WebSockets are not supported within this browser");
            }
            var ws = new WebSocket("ws://localhost:8080/Akka-Spring/ws/websocket");
            ws.onopen = function() {
                $("#wsInfo").append("Websocket connected!");
                ws.send("Websocket is open");
            };
            ws.onclose = function() {
                $("#wsInfo").append("Websocket disconnected!");
                ws = null;
            }
            ws.onmessage = function(message) {
                var appMessage = JSON.parse(message.data);

                if(appMessage.type == "IMAGE"){
                    var random = Math.floor(Math.random() * 8) + 1;
                    $("#img-c" + random).attr("src", "data:image/jpeg;base64, " + appMessage.data);
                }else if (appMessage.type == "AGGREGATE"){
                    $("#aggregate-word-cloud").html("");
                    var keys = [];
                    var total = 0;
                    for (var key in appMessage.data){
                        keys.push(key);
                        total = total + appMessage.data[key];
                    }

                    d3.layout.cloud().size([300, 300])
                            .words(keys.map(function(d) {
                                        return {text: d, size: (appMessage.data[d]/total) * 300};
                                    }))
                            .rotate(function() { return ~~(Math.random() * 2) * 90; })
                            .font("Impact")
                            .fontSize(function(d) { return d.size; })
                            .on("end", draw)
                            .start();


                }
            }
        </script>
    </head>
<body>
<script>

</script>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                <button class="btn btn-large btn-primary" type="button">Fetch</button>
                <textarea rows="3" id="wsInfo"></textarea>
            </div>
            <div class="span9">
                <div class="row-fluid" id="aggregate-word-cloud">
                </div>
                <div class="row-fluid">
                    <div class="span3">
                        <img style="height: 200px; width:200px;" id="img-c1"/>
                    </div>
                    <div class="span3" id="c2">
                        <img style="height: 200px; width:200px;" id="img-c2"/>
                    </div>
                    <div class="span3" id="c3">
                        <img style="height: 200px; width:200px;" id="img-c3"/>
                    </div>
                    <div class="span3" id="c4">
                        <img style="height: 200px; width:200px;" id="img-c4"/>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span3" id="c5">
                        <img style="height: 200px; width:200px;" id="img-c5"/>
                    </div>
                    <div class="span3" id="c6">
                        <img style="height: 200px; width:200px;" id="img-c6"/>
                    </div>
                    <div class="span3" id="c7">
                        <img style="height: 200px; width:200px;" id="img-c7"/>
                    </div>
                    <div class="span3" id="c8">
                        <img style="height: 200px; width:200px;" id="img-c8"/>
                    </div>
                </div>
            </div>
        </div>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->


</body>
</html>