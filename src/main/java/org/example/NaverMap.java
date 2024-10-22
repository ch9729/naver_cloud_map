package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 네이버맵 API 요청해서 이미지 가져오기
 */
public class NaverMap {
    
    MainFrame mainFrame;    //화면에 표시되고 있는 윈도우 창
    
    public NaverMap(MainFrame mainFrame) throws IOException, ParseException {
        this.mainFrame = mainFrame;

        String address = mainFrame.addressTxt.getText();    //주소창의 주소를 가져옴

        StringBuilder urlString = new StringBuilder("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=");
        //입력받은 주소는 한글 및 특수기호가 들어가므로 http 주소 요청시 인코딩 필요
        urlString.append(URLEncoder.encode(address, "UTF-8"));
        System.out.println(urlString);

        URL url = new URL(urlString.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");    //보낼때 제이슨으로 요청
        conn.setRequestProperty("Accept", "application/json");  //받을때 제이슨으로 요청
        conn.setRequestProperty("x-ncp-apigw-api-key-id", Key.id);
        conn.setRequestProperty("x-ncp-apigw-api-key", Key.key);
        System.out.println("Response Code: " + conn.getResponseCode());

        BufferedReader br;
        if (conn.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
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

            AddressVO vo = new AddressVO();
            vo.setRoadAddress(obj.get("roadAddress").toString());
            vo.setJibunAddress(obj.get("jibunAddress").toString());
            vo.setX(obj.get("x").toString());
            vo.setY(obj.get("y").toString());

            mapService(vo);
        }
    }

    private void mapService(AddressVO vo) throws IOException {
        //네이버 Static Map서비스로 이미지 가져오기
        String mapUrl = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
        String pos = URLEncoder.encode(vo.getX()+ " " + vo.getY(),"UTF-8");
        mapUrl += "w=300&h=300";    //이미지 크기
        mapUrl += "&center=" + vo.getX() + "," + vo.getY() + "&level=16";    //x,y 좌표값 입력 및 줌 크기설정
        mapUrl += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(vo.getRoadAddress(),"UTF-8");
        System.out.println(mapUrl);

        URL url = new URL(mapUrl.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");    //보낼때 제이슨으로 요청
        conn.setRequestProperty("Accept", "application/json");  //받을때 제이슨으로 요청
        conn.setRequestProperty("x-ncp-apigw-api-key-id", Key.id);
        conn.setRequestProperty("x-ncp-apigw-api-key", Key.key);
        System.out.println("Response Code: " + conn.getResponseCode());

        if(conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            Image image = ImageIO.read(is);
            is.close();
            ImageIcon imageIcon = new ImageIcon(image);
            mainFrame.imageLabel.setIcon(imageIcon);
            mainFrame.resAddress.setText(vo.getRoadAddress());
            mainFrame.jibunAddress.setText(vo.getJibunAddress());
            mainFrame.resX.setText(vo.getX());
            mainFrame.resY.setText(vo.getY());
        }

        conn.disconnect();  //연결종료
    }
}
