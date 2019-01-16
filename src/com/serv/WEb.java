package com.serv;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.google.gson.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.font.FontFamily;


import javax.management.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(
        name = "WEb",
        urlPatterns = "/tweet-data")
public class WEb extends HttpServlet {

    private static final  Font RED_NORMAL = new Font("Helvetica",Font.BOLD, 12);
    static final  String characterFilter = "[\\p{N}\\p{P}\\p{S}\\p{Cf}]";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Map<String, String> details = null;
        Map<String, Map<String, String>> tweetData = new HashMap<>();

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        String query = req.getParameter("txt");
        if (query != null) {
            req.setAttribute("txt", query);
        } else {
            if (req.getAttribute("txt") != null) {
                query = req.getAttribute("txt").toString();
            } else {
                query = "";
            }

        }


        String dataFil[] = null;
        if (req.getParameterValues("check") != null) {
            dataFil = req.getParameterValues("check");
        }
        String sd = "";
        if (dataFil != null) {

            for (int i = 0; i < dataFil.length; i++) {
                if (i == dataFil.length - 1) {

                    sd = sd + dataFil[i];
                } else {
                    sd = sd + dataFil[i] + " AND ";
                }


            }
        }


// http://localhost:8983/solr/test/select?fq=city:delhi&fq=topic:crime&indent=on&q=:&rows=1&wt=json
        query = query.replace(":", "\\:");
        //  query=URLEncoder.encode(query, "UTF-8");
        if (query.equals("")) {
            query = "*:*";
        }
        int page = 1;
        int recordsPerPage = 10;
        int currPage = 0;

        if (req.getParameter("page") != null)
            page = Integer.parseInt(req.getParameter("page"));
        currPage = page;
        if (page == 1) {
        } else {
            page = page - 1;
            page = page * recordsPerPage + 1;
        }


        int filterquery = 0;
        if (sd.length() > 1) {

            filterquery = 1;
        }

        String inurl = "";
        String inurl_wf = "";
        if (sd.length() > 1) {
            query = query + " AND " + sd;
        }
        query = URLEncoder.encode(query, "UTF-8");
        sd = URLEncoder.encode(sd, "UTF-8");
    /*    if(filterquery==1){

            inurl_wf="http://localhost:8983/solr/test/select?indent=on&"+sd+"&q="+query+"&wt=json&rows=1100&start=1";
             inurl = "http://localhost:8983/solr/test/select?indent=on&"+sd+"&q="+query+"&wt=json&rows="+recordsPerPage+"&start="+page;


        } else {

             inurl_wf="http://localhost:8983/solr/test/select?indent=on&q="+query+"&wt=json&rows=1100&start=1";
             inurl = "http://localhost:8983/solr/test/select?indent=on&q="+query+"&wt=json&rows="+recordsPerPage+"&start="+page;

        }*/


        if (filterquery == 1) {


            inurl_wf = "http://ec2-3-17-66-121.us-east-2.compute.amazonaws.com:8983/solr/test/select?indent=on&" + sd + "&q=" + query + "&wt=json&rows=1100&start=1";
            inurl = "http://ec2-3-17-66-121.us-east-2.compute.amazonaws.com:8983/solr/test/select?indent=on&" + sd + "&q=" + query + "&wt=json&rows=" + recordsPerPage + "&start=" + page;


        } else {

            inurl_wf = "http://ec2-3-17-66-121.us-east-2.compute.amazonaws.com:8983/solr/test/select?indent=on&q=" + query + "&wt=json&rows=1100&start=1";
            inurl = "http://ec2-3-17-66-121.us-east-2.compute.amazonaws.com:8983/solr/test/select?indent=on&q=" + query + "&wt=json&rows=" + recordsPerPage + "&start=" + page;

        }


        System.out.print(inurl);
        URL obj = new URL(inurl);
        URL obj1 = new URL(inurl_wf);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : ");
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        BufferedReader in1 = new BufferedReader(
                new InputStreamReader(con1.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        StringBuffer responseall = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        while ((inputLine = in1.readLine()) != null) {
            responseall.append(inputLine);
        }
        in.close();

        //print in String
        // System.out.println(response.toString());

        JsonParser parser = new JsonParser();
        JsonElement data = parser.parse(response.toString());
        JsonElement data1 = parser.parse(responseall.toString());
        JsonObject jobject = data.getAsJsonObject();
        JsonObject jobject1 = data1.getAsJsonObject();
        JsonElement respo = jobject.getAsJsonObject("response");
        JsonElement respoall = jobject1.getAsJsonObject("response");
        jobject1 = respoall.getAsJsonObject();
        jobject = respo.getAsJsonObject();
        JsonArray docs = jobject.get("docs").getAsJsonArray();
        JsonArray docs1 = jobject1.get("docs").getAsJsonArray();
        JsonElement docsall = jobject1.get("numFound");
        pw.println(docs.size());
        pw.println(docs.size());
        int docc=jobject1.get("numFound").getAsInt();
        int doccount = jobject.get("numFound").getAsInt();
        if (doccount == 0) {

            resp.sendRedirect("../../nores.jsp");
            return;
        }
        int noOfRecords = jobject1.get("numFound").getAsInt();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        Map<String, Integer> hashtags = new TreeMap<>();
        Map<String, Integer> all_topics = new TreeMap<>();
        SortedSet<Map.Entry<String, Integer>> hashtags1 = null;
        List<Map.Entry<String, Integer>> hs = new ArrayList<>();
        all_topics.put("Environment", 0);
        all_topics.put("Crime", 0);
        all_topics.put("Politics", 0);
        all_topics.put("Social Unrest", 0);
        all_topics.put("Infra", 0);
        all_topics.put("General", 0);

        Map<String, Integer> cities = new TreeMap<>();
        cities.put("Delhi", 0);
        cities.put("NYC", 0);
        cities.put("Paris", 0);
        cities.put("Bangkok", 0);
        cities.put("Mexico City", 0);


        int hashcount = 0;
        for (int i = 0; i < docs1.size(); i++) {
            String tweet_topic = "";
            jobject1 = docs1.get(i).getAsJsonObject();
            if (jobject1.has("topic")) {

                tweet_topic = jobject1.get("topic").getAsString();
                if (tweet_topic.equalsIgnoreCase("social unrest")) {

                    tweet_topic = tweet_topic.substring(0, 1).toUpperCase() + tweet_topic.substring(1, 7) + tweet_topic.substring(7, 8).toUpperCase() + tweet_topic.substring(8);
                } else {
                    tweet_topic = tweet_topic.substring(0, 1).toUpperCase() + tweet_topic.substring(1);
                }

                all_topics = insertInMap(all_topics, tweet_topic);
            } else {
                tweet_topic = "General";
                all_topics = insertInMap(all_topics, tweet_topic);
            }


            if (jobject1.has("city")) {

                tweet_topic = jobject1.get("city").getAsString();
                cities = insertCityInMap(cities, tweet_topic);
            }


            // HAshtag extraction
            String hash = "";

            if (jobject1.has("hashtags")) {
                hash = jobject1.get("hashtags").toString();


                List<String> hashes = Arrays.asList(hash.split(","));

                for (String hasht : hashes) {
                    hasht = hasht.replaceAll(characterFilter, "");
                    if (hashtags.get(hasht) != null) {

                        hashtags.put(hasht, hashtags.get(hasht) + 1);
                    } else {
                        hashtags.put(hasht, 1);
                    }
                }
                hashtags1 = entriesSortedByValues(hashtags);

            }


        }


        if (hashtags1.size() > 0) {


        Iterator<Map.Entry<String, Integer>> it = hashtags1.iterator();


        while (it.hasNext() && hashcount < 10) {
            hs.add(it.next());
            hashcount++;
        }
    }

        // URL url=null;
        pw.print("<body bgcoolor=\"red\">");
        for(int i=0;i<docs.size();i++){
            details=new TreeMap<>();
            jobject= docs.get(i).getAsJsonObject();

            String id=jobject.get("id").getAsString();
            String tweet_text="";
            String tweet_topic="";
            String tweet_date="";
            String city="";
            String related_articles="";
            if(jobject.has("text")){
                 tweet_text=jobject.get("text").getAsString();
            } /*else if(jobject.has("text_es")){
                 tweet_text=jobject.get("text_es").getAsString();
            } else if(jobject.has("text_hi")){
                 tweet_text=jobject.get("text_hi").getAsString();
            } else if(jobject.has("text_th")){
                 tweet_text=jobject.get("text_th").getAsString();
            } else if(jobject.has("text_fr")){
                 tweet_text=jobject.get("text_fr").getAsString();
            }*/


        // topic categorixzation
           if (jobject.has("topic")) {

                tweet_topic = jobject.get("topic").getAsString();
                if(tweet_topic.equalsIgnoreCase("social unrest")){

                    tweet_topic=tweet_topic.substring(0,1).toUpperCase()+tweet_topic.substring(1,7)+tweet_topic.substring(7,8).toUpperCase()+tweet_topic.substring(8);
                } else {
                    tweet_topic=tweet_topic.substring(0,1).toUpperCase()+tweet_topic.substring(1);
                }

                all_topics = insertInMap(all_topics, tweet_topic);
            } else {
                tweet_topic = "General";
                all_topics = insertInMap(all_topics, tweet_topic);
            }


            details.put("tweet_topic",tweet_topic);


            if (jobject.has("city")) {

                city = jobject1.get("city").getAsString();
                cities = insertCityInMap(cities, city);
            }

            details.put("city",city);

            // tweet url
            String url= "https://twitter.com/" + jobject.get("user.screen_name").getAsString()
                    + "/status/" + id;


            details.put("tweet_url",url);
          //  actual tweet stored here
            if(tweet_text.equals("")){

                details.put("tweet_text","");
                tweetData.put(id,details);
            } else {
                details.put("tweet_text",tweet_text);
                tweetData.put(tweet_text,details);
            }


            // tweet summarization
            long ttLenght=tweet_text.length();
            long ttsumlen=0;
            String tweet_sumr_init="";
            String tweet_sumr_end="";
            if(ttLenght>0){
                ttsumlen=Math.round(ttLenght*(0.4));
               tweet_sumr_init=tweet_text.substring(0,(int)ttsumlen);
               tweet_sumr_end=tweet_text.substring((int)ttsumlen);
            } else {
                 tweet_sumr_init="Text missing";
                tweet_sumr_end="please open link for tweet";
            }
            details.put("tweet_sumr_init",tweet_sumr_init);
            details.put("tweet_sumr_end",tweet_sumr_end);


            // create date for tweet

            if(jobject.has("created_at")){ ;
                tweet_date=jobject.get("created_at").getAsString();
                try {

                    DateFormat recievedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00'Z'");
                    Date parsedRecDate = recievedDateFormat.parse(tweet_date);
                    //      System.out.print("Created: "+s.getCreatedAt().toString());
                    DateFormat solrDateForm = new SimpleDateFormat("dd MMMM yyyy");
                     tweet_date = solrDateForm.format(parsedRecDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            details.put("tweet_date",tweet_date);

            // user name extraction
            String userName="";
            String userScreenName="";

            if(jobject.has("user.name")){
                userName=jobject.get("user.name").getAsString();
                userName=userName.replaceAll(characterFilter,"");
            }

            if(jobject.has("user.screen_name")){
                userScreenName=jobject.get("user.screen_name").getAsString();
            }

            details.put("userName",userName);
            details.put("userScreenName",userScreenName);





            // getting cities out
            // topic categorixzation

            //Sentiment analysis

        /*    Map<String,String> sentiments=new HashMap<>();
            String dir = System.getProperty("user.dir");
            System.out.println(dir);
            String pythonScriptPath = dir+"/senti.py";
            System.out.println(pythonScriptPath);
            String[] cmd = new String[2];
            cmd[0] = "python"; // check version of installed python: python -V
            cmd[1] = pythonScriptPath;*/

// create runtime to execute external command
            /*Runtime rt = Runtime.getRuntime();

                Process pr = rt.exec(new String[]{cmd[0],cmd[1], tweet_text});

                BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = "";

                while ((line = bfr.readLine()) != null) {
                    String senti="";
                   if(Double.parseDouble(line)>0){
                       senti="Positive";
                   } else if(Double.parseDouble(line)<0){
                       senti="Negative";
                   } else {
                       senti="Neutral";
                    }
                    sentiments.put(tweet_text,line);
                    details.put("sentiment",senti);
                }

*/


            pw.println(id+"&nbsp &nbsp&nbsp<a href=\""+url+"\">tweet</a><br>");
           // pw.print("</body></html>");
        }
        req.setAttribute("tweetData",tweetData);
        req.setAttribute("hashtags",hs);
        req.setAttribute("all_topics",all_topics);
        req.setAttribute("cities",cities);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("page", page);
        req.setAttribute("recordsPerPage", recordsPerPage);
        req.setAttribute("currPage", currPage);
        RequestDispatcher dispatcher = req.getRequestDispatcher(
                "/WEB-INF/jsp/tweet-data.jsp");

        dispatcher.forward(req, resp);

       // jobject = docs.get(0).getAsJsonObject();
       // String id=jobject.get("id").getAsString();

        Gson gson = new Gson();
      //  String finalJson = gson.toJson(id);

/*


JsonParser parser = new JsonParser();
        JsonElement data = parser.parse(response.toString());
        JsonObject jobject = data.getAsJsonObject();
        JsonElement respo = jobject.get("response");
        Gson gson = new Gson();
        String finalJson = gson.toJson(respo);
 */

      //  pw.write(query);
        System.out.print("\n\n");
       // pw.write("\n\n"+finalJson);
      //  pw.write(myresponse.toString());


    }

    private Map<String,Integer> insertInMap(Map<String, Integer> all_topics, String tweet_topic) {

        for(String top:all_topics.keySet()){

            if(top.equalsIgnoreCase(tweet_topic) || tweet_topic.equalsIgnoreCase("infra")){
                tweet_topic=tweet_topic.toLowerCase();
                if(tweet_topic.equalsIgnoreCase("infra")){
                    tweet_topic="Infra";
                }  if(tweet_topic.equalsIgnoreCase("social unrest")){

                    tweet_topic=tweet_topic.substring(0,1).toUpperCase()+tweet_topic.substring(1,7)+tweet_topic.substring(7,8).toUpperCase()+tweet_topic.substring(8);
                }
                tweet_topic=tweet_topic.substring(0,1).toUpperCase()+tweet_topic.substring(1);
                if(all_topics.get(tweet_topic)!=null){
                    all_topics.put(tweet_topic,all_topics.get(tweet_topic)+1);
                } else {
                    all_topics.put(tweet_topic,1);
                }
            }



        }


        return all_topics;
    }

    private Map<String,Integer> insertCityInMap(Map<String, Integer> all_topics, String tweet_topic) {

        for(String top:all_topics.keySet()){


            if(top.equalsIgnoreCase(tweet_topic)){
                tweet_topic=tweet_topic.toLowerCase();
                if(tweet_topic.equalsIgnoreCase("mexico city")){

                    tweet_topic=tweet_topic.substring(0,1).toUpperCase()+tweet_topic.substring(1,7)+tweet_topic.substring(7,8).toUpperCase()+tweet_topic.substring(8);
                }
                if(tweet_topic.equalsIgnoreCase("nyc")){
                    tweet_topic="NYC";
                }
                tweet_topic=tweet_topic.substring(0,1).toUpperCase()+tweet_topic.substring(1);
                if(all_topics.get(tweet_topic)!=null){
                    all_topics.put(tweet_topic,all_topics.get(tweet_topic)+1);
                } else {
                    all_topics.put(tweet_topic,1);
                }
            }

        }


        return all_topics;
    }

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());

        return sortedEntries;
    }

}
