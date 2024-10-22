package org.example;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainFrame extends JFrame {

    //지역변수를 벗어나서 다른곳에서도 사용할수 있도록 변수 선언
    public JTextField addressTxt;
    public JLabel resAddress, jibunAddress, resX, resY, imageLabel;

    public MainFrame(String title) {
        super(title);
        //위쪽패널에 주소입력, 클릭버튼 추가
        JPanel panel = new JPanel();
        JLabel addressLbl = new JLabel("주소입력");
        addressTxt = new JTextField(50);
        JButton btn = new JButton("클릭");
        panel.add(addressLbl);
        panel.add(addressTxt);
        panel.add(btn);
        //가운데 이미지라벨
        imageLabel = new JLabel("지도보기");
        //아래의 정보 패널
        JPanel pan1 = new JPanel();
        pan1.setLayout(new GridLayout(4, 1));   //4줄
        resAddress = new JLabel("도로명");
        jibunAddress = new JLabel("지번주소");
        resX = new JLabel("경도");
        resY = new JLabel("위도");
        pan1.add(resAddress);
        pan1.add(jibunAddress);
        pan1.add(resX);
        pan1.add(resY);

        //버튼 눌렀을때 이벤트
        btn.addActionListener(e -> {
            try {
                new NaverMap(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //레이아웃 설정
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH); //위의 패널을 윈도우창에 붙임
        add(imageLabel, BorderLayout.CENTER);   //이미지라벨
        add(pan1, BorderLayout.SOUTH);  //정보
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //윈도우창 x 닫기
        setSize(730,660);               //윈도우창 크기
        setVisible(true);                           //창 보이기



    }
}

