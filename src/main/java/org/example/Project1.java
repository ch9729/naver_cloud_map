package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.awt.Image;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Scanner;


public class Project1 {

    public static void main(String[] args) throws IOException, ParseException {

        Key key = new Key();
        //주소를 입력받기
        Scanner sc = new Scanner(System.in);
        System.out.println("주소를 입력해주세요");
        String address = sc.nextLine();  //주소를 받을때까지 대기함
        sc.close();
        System.out.println("받은주소는 " + address + "입니다.");

        StringBuilder urlString = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=");
        //입력받은 주소는 한글 및 특수기호가 들어가므로 http 주소 요청시 인코딩 필요
        urlString.append(URLEncoder.encode(address, "UTF-8"));
        System.out.println(urlString);

        URL url = new URL(urlString.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");    //보낼때 제이슨으로 요청
        conn.setRequestProperty("Accept", "application/json");  //받을때 제이슨으로 요청
        conn.setRequestProperty("x-ncp-apigw-api-key-id", key.id);
        conn.setRequestProperty("x-ncp-apigw-api-key", key.key);
        System.out.println("Response Code: " + conn.getResponseCode());

        BufferedReader br;
        if (conn.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        }else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        String result = br.readLine();

        br.close();
        conn.disconnect();

        JSONParser js = new JSONParser();
        JSONObject jsonObject = (JSONObject) js.parse(result);
        //System.out.println(jsonObject);

        JSONArray jsonArray = (JSONArray) jsonObject.get("addresses");

        for (Object object : jsonArray) {
            JSONObject obj = (JSONObject) object;
            System.out.println("도로명주소 : " + obj.get("roadAddress"));
            System.out.println("지번주소 : " + obj.get("jibunAddress"));
            System.out.println("경도 : " + obj.get("x"));
            System.out.println("위도 : " + obj.get("y"));

            String x = obj.get("x").toString();
            String y = obj.get("y").toString();
            String z = obj.get("roadAddress").toString();

            mapService(x,y,z);
        }
    }

    static void mapService(String x, String y, String z) throws IOException {
        Key key = new Key();
        //네이버 Static Map서비스로 이미지 가져오기
        String mapUrl = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
        String pos = URLEncoder.encode(x+ " " + y,"UTF-8");
        mapUrl += "w=300&h=300";    //이미지 크기
        mapUrl += "&center=" + x + "," + y + "&level=16";    //x,y 좌표값 입력 및 줌 크기설정
        // mapUrl += "center=" + x + "," + y;
        // mapUrl += "&level=16&w=300&h=300";
        mapUrl += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(z,"UTF-8");
        System.out.println(mapUrl);

        URL url = new URL(mapUrl.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");    //보낼때 제이슨으로 요청
        conn.setRequestProperty("Accept", "application/json");  //받을때 제이슨으로 요청
        conn.setRequestProperty("x-ncp-apigw-api-key-id", key.id);
        conn.setRequestProperty("x-ncp-apigw-api-key", key.key);
        System.out.println("Response Code: " + conn.getResponseCode());

        if(conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            Image image = ImageIO.read(is);
            //이미지 파일의 이름을 현재시간을 0.001초 단위로 만듬 (겹치지 않는다)
            String tempname = Long.valueOf(new Date().getTime()).toString();
            File f = new File(tempname + ".jpg");
            f.createNewFile();
            ImageIO.write((RenderedImage) image, "jpg", f);
            is.close();
        }

        conn.disconnect();  //연결종료
    }
}
