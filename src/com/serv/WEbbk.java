package com.serv;

import com.google.gson.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@WebServlet(
        name = "WEb",
        urlPatterns = "/tweet-data")
public class WEbbk extends HttpServlet {

    private static final  Font RED_NORMAL = new Font("Helvetica",Font.BOLD, 12);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String,String> details=null;
        Map<String,Map<String,String>> tweetData=new HashMap<>();

        resp.setContentType("text/html");
        PrintWriter pw=resp.getWriter();
        String query=req.getParameter("txt");


        query=query.replace(":","\\:");
        query=URLEncoder.encode(query, "UTF-8");
        if(query.equals("")){
            query=":";
        }
        String inurl = "http://localhost:8983/solr/gettingstarted/select?indent=on&q=*"+query+"*&wt=json&rows=10";
        System.out.print(inurl);
        URL obj = new URL(inurl);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " );
        System.out.println("Response Code : " + responseCode);
        BufferedReader in =new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        } in .close();



        //print in String
        // System.out.println(response.toString());



        JsonParser parser = new JsonParser();
        JsonElement data = parser.parse(response.toString());
        JsonObject jobject = data.getAsJsonObject();
        JsonElement respo = jobject.getAsJsonObject("response");
        jobject = respo.getAsJsonObject();
        JsonArray docs=jobject.get("docs").getAsJsonArray();
        pw.println(docs.size());
        Map<String, Integer> hashtags = new TreeMap<>();
        Map<String,Integer> all_topics = new TreeMap<>();
        all_topics.put("Environment",0);
        all_topics.put("Crime",0);
        all_topics.put("Politics",0);
        all_topics.put("Social Unrest",0);
        all_topics.put("Infrastructure",0);
        all_topics.put("General",0);

        Map<String,Integer> cities = new TreeMap<>();
        cities.put("Delhi",0);
        cities.put("New York City (NYC)",0);
        cities.put("Paris",0);
        cities.put("Bangkok",0);
        cities.put("Mexico City",0);

       // URL url=null;
        pw.print("<body bgcoolor=\"red\">");
        for(int i=0;i<docs.size();i++){
            details=new TreeMap<>();
            jobject= docs.get(i).getAsJsonObject();
            String id=jobject.get("id").getAsString();
            String tweet_text="";
            String tweet_topic="";
            String tweet_date="";
            String related_articles="";
            if(jobject.has("text_en")){
                 tweet_text=jobject.get("text_en").getAsString();
            } else if(jobject.has("text_es")){
                 tweet_text=jobject.get("text_es").getAsString();
            } else if(jobject.has("text_hi")){
                 tweet_text=jobject.get("text_hi").getAsString();
            } else if(jobject.has("text_th")){
                 tweet_text=jobject.get("text_th").getAsString();
            } else if(jobject.has("text_fr")){
                 tweet_text=jobject.get("text_fr").getAsString();
            }


        // topic categorixzation
            if (jobject.has("topic")) {

                tweet_topic = jobject.get("topic").getAsString();
                all_topics = insertInMap(all_topics, tweet_topic);
            } else {
                tweet_topic = "General";
                all_topics = insertInMap(all_topics, tweet_topic);
            }


            details.put("tweet_topic",tweet_topic);

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
            String characterFilter = "[\\p{N}\\p{P}\\p{S}\\p{Cf}]";
            if(jobject.has("user.name")){
                userName=jobject.get("user.name").getAsString();
                userName=userName.replaceAll(characterFilter,"");
            }

            if(jobject.has("user.screen_name")){
                userScreenName=jobject.get("user.screen_name").getAsString();
            }

            details.put("userName",userName);
            details.put("userScreenName",userScreenName);


            // HAshtag extraction
            String hash="";

            if (jobject.has("entities.hashtags.text")) {
                hash = jobject.get("entities.hashtags.text").toString();


                List<String> hashes = Arrays.asList(hash.split(","));

                for (String hasht : hashes) {
                    if (hashtags.get(hasht) != null) {
                        hasht=hasht.replaceAll(characterFilter,"");
                        hashtags.put(hasht, hashtags.get(hash) + 1);
                    } else {
                        hasht=hasht.replaceAll(characterFilter,"");
                        hashtags.put(hasht, 1);
                    }
                }

            }


            // getting cities out
            // topic categorixzation
            if (jobject.has("city")) {

                tweet_topic = jobject.get("city").getAsString();
                cities = insertInMap(cities, tweet_topic);
            }


            //Sentiment analysis

            Map<String,String> sentiments=new HashMap<>();
            String dir = System.getProperty("user.dir");
            System.out.println(dir);
            String pythonScriptPath = dir+"/senti.py";
            System.out.println(pythonScriptPath);
            String[] cmd = new String[2];
            cmd[0] = "python"; // check version of installed python: python -V
            cmd[1] = pythonScriptPath;

// create runtime to execute external command
           /* Runtime rt = Runtime.getRuntime();

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
                }*/




            pw.println(id+"&nbsp &nbsp&nbsp<a href=\""+url+"\">tweet</a><br>");
           // pw.print("</body></html>");
        }
        req.setAttribute("tweetData",tweetData);
        req.setAttribute("hashtags",hashtags);
        req.setAttribute("all_topics",all_topics);
        req.setAttribute("cities",cities);
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
                    tweet_topic="Infrastructure";
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

}
