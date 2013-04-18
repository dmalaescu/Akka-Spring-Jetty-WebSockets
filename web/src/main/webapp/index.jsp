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
        <script type="text/javascript">
            if (!window.WebSocket){
                alert("WebSockets are not supported within this browser");
            }
            var ws = new WebSocket("ws://localhost:8080/Akka-Spring/ws/websocket");
            ws.onopen = function() {
                alert("Websocket connected");
                ws.send("Websocket is open");
            };
            ws.onclose = function() {
                alert("socket is cloesed");
                ws = null;
            }
            ws.onmessage = function(message) {
                $("#wsInfo").append(message.data);
            }

        </script>
    </head>
<body>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                <button class="btn btn-large btn-primary" type="button">Fetch</button>
                <textarea rows="3" id="wsInfo"></textarea>
            </div>
            <div class="span9">
                <div class="row-fluid" id="c1">
                    <div class="span3">
                        Container
                    </div>
                    <div class="span3" id="c2">
                        Container
                    </div>
                    <div class="span3" id="c3">
                        Container
                    </div>
                    <div class="span3" id="c4">
                        Container
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span3" id="c5">
                        Container
                    </div>
                    <div class="span3" id="c6">
                        Container
                    </div>
                    <div class="span3" id="c7">
                        Container
                    </div>
                    <div class="span3" id="c8">
                        Container
                    </div>
                </div>
            </div>
        </div>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<c:url value="/js/jquery.js"/>"></script>
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
</body>
</html>