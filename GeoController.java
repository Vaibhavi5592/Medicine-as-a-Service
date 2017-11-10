package com.test;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("geoController")
public class GeoController {

	private Log log = LogFactory.getLog(GeoController.class);

	@GET
	@Path("/getdata/{addr}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<pharmacy> main(@PathParam("addr") String addr) throws ParseException,
			ClassNotFoundException, SQLException {

		log.info("Excuting main() ... ");
		ArrayList<pharmacy> t = new ArrayList<>();
		InputStream inputStream = null;
		String json = "";
		
     	try {
			HttpClient client0 = new DefaultHttpClient();
			HttpGet request0 = new HttpGet(
					"http://maps.google.com/maps/api/geocode/json?sensor=false&address="+addr);
             
		
			HttpResponse response0 = client0.execute(request0);
			// HttpEntity entity = response.getEntity();
			// inputStream = entity.getContent();

			String respStr0 = EntityUtils
					.toString(response0.getEntity(), "UTF-8");
			System.out.println("Response String -> " + respStr0);
			JSONParser parser0 = new JSONParser();
			JSONObject jsonObj0 = (JSONObject) parser0.parse(respStr0);

			JSONArray jsonObject0 = (JSONArray) jsonObj0.get("results");
			JSONObject jsonObj2 = (JSONObject)jsonObject0.get(0);
            JSONObject jsonObject3 = (JSONObject)jsonObj2.get("geometry");
            JSONObject location = (JSONObject) jsonObject3.get("location");
			
            System.out.println( "Lat = "+location.get("lat"));
            System.out.println( "Lng = "+location.get("lng"));
			
            double lat=(double) location.get("lat");
            double lng=(double) location.get("lng");
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&rankby=distance&types=pharmacy&key=AIzaSyA0Q4hB8WxSpX5HEVs63HyLtdGSbXgEQig");
		
			HttpResponse response = client.execute(request);
			// HttpEntity entity = response.getEntity();
			// inputStream = entity.getContent();

			String respStr = EntityUtils
					.toString(response.getEntity(), "UTF-8");
			System.out.println("Response String -> " + respStr);
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(respStr);

			JSONArray jsonObject1 = (JSONArray) jsonObj.get("results");
			for (int i = 0; i < 5; i++) {
				pharmacy p = new pharmacy();

				JSONObject jsonObject2 = (JSONObject) jsonObject1.get(i);
				p.setName((String) jsonObject2.get("name"));
				p.setAddress((String) jsonObject2.get("vicinity"));
				t.add(p);

			}
			return t;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

		/*
		 * try { BufferedReader reader = new BufferedReader(new
		 * InputStreamReader(inputStream,"utf-8"),8); StringBuilder sbuild = new
		 * StringBuilder(); String line = null; while ((line =
		 * reader.readLine()) != null) { sbuild.append(line); }
		 * inputStream.close(); json = sbuild.toString(); } catch(Exception e) {
		 * System.out.println(e); }
		 */
		// System.out.println(json);
		// now parse
		/*
		 * JSONParser parser = new JSONParser(); Object obj =
		 * parser.parse(json); JSONObject jb = (JSONObject) obj;
		 */

		// now read

	}
}
