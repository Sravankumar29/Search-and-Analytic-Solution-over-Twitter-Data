<%@ page import="java.util.Map" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html><head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">


        google.charts.load('current', {
            'packages':['corechart','geochart'],
            // Note: you will need to get a mapsApiKey for your project.
            // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
            'mapsApiKey': 'AIzaSyAA4aNFh4DZX9qjJiUbN5bS6dEWK8gnHeE'
        });
        google.charts.setOnLoadCallback(drawChart);
        google.charts.setOnLoadCallback(drawRegionsMap);


        google.setOnLoadCallback(drawChart1);
        google.setOnLoadCallback(drawChart2);

        function drawChart() {


            var data = new google.visualization.arrayToDataTable([
                ['Country','Area(square km)'],
                <c:forEach items="${sessionScope.hashtags}" var="entry">
                ['${entry.key}'  , ${entry.value}],
                </c:forEach>
            ]);

            // Set chart options
            var options = {
                'title' : 'Hashtag Volume', //title which will be shown right above the Google Pie Chart
                is3D : true, //render Google Pie Chart as 3D
                pieSliceText: 'label', //on mouse hover show label or name of the Country
                tooltip :  {showColorCode: true}, // whether to display color code for a Country on mouse hover
                'width' : 500, //width of the Google Pie Chart
                'height' : 500 //height of the Google Pie Chart
            };

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        }

        function drawRegionsMap() {
            var data =new  google.visualization.arrayToDataTable([
                ['City','numberOfTweets'],
                ['delhi',${cities['Delhi']}],
                ['paris',${cities['Paris']}],
                ['Bangkok',${cities['Bangkok']}],
                ['New York',${cities['NYC']}],
                ['mexico'  ,${cities['Mexico City']}]


            ]);

            var options = {

                displayMode: 'markers',
                colorAxis: {
                    colors: ['blue', 'green', 'red'],
                },
				width : 500, //width of the Google Pie Chart
                height : 500 //height of the Google Pie Chart
            };

            var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));

            chart.draw(data, options);
        }

        function drawChart1() {

            // Create the data table.
            //var data = new google.visualization.DataTable();
            //data.addColumn('string', 'Topping');
            //data.addColumn('number', 'Slices');
            /*data.addRows([

             */

            var data = new google.visualization.arrayToDataTable([
                ['Topics','Number of Tweets'],
                ['Social Unrest', 34],
                ['Politics', 120],
                ['Infrastructure', 12],
                ['Crime', 20],
                ['Environment', 37],
            ]);

            // Set chart options
            var options = {
                'title' : 'Histogram: Number of Topics', //title which will be shown right above the Google Pie Chart
                //is3D : true, //render Google Pie Chart as 3D
                pieSliceText: 'label', //on mouse hover show label or name of the Country
                tooltip :  {showColorCode: true}, // whether to display color code for a Country on mouse hover
                'width' : 500, //width of the Google Pie Chart
                'height' : 500 //height of the Google Pie Chart
            };

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.Histogram(document.getElementById('chart_div1'));
            chart.draw(data, options);
        }

        function drawChart2() {

            // Create the data table.
            //var data = new google.visualization.DataTable();
            //data.addColumn('string', 'Topping');
            //data.addColumn('number', 'Slices');
            /*data.addRows([

             */

            var data = new google.visualization.arrayToDataTable([
                ['Mood','Number of Tweets'],
                ['Happy', 34],
                ['Sad', 120],
                ['Neutral', 12],

            ]);

            // Set chart options
            var options = {
                'title' : 'Scatter Chart: Sentiment Analysis', //title which will be shown right above the Google Pie Chart
                //is3D : true, //render Google Pie Chart as 3D
                pieSliceText: 'label', //on mouse hover show label or name of the Country
                tooltip :  {showColorCode: true}, // whether to display color code for a Country on mouse hover
                
				  'width':500,
                       'height':500
            };

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.ScatterChart(document.getElementById('chart_div2'));
            chart.draw(data, options);
        }
    </script>
</head>



<body>

<table>
  <tr>
        <td><div id="chart_div" style="border: 1px solid #ccc"></div></td>
        <td><div id="chart_div1" style="border: 1px solid #ccc"></div></td>
      </tr>
	  <br>
	  <tr>
	  <td><div id="regions_div" style="border: 1px solid #ccc"></div></td>
        <td><div id="chart_div2" style="border: 1px solid #ccc"></div></td>
	  </tr>
	  <br>
	
    </table>
</body>

</html>