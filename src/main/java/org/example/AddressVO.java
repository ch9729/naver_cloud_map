package org.example;

/**
 * 필요한 데이터 전달하기 위한 객체
 */
public class AddressVO {

    private String roadAddress; //도로명주소
    private String jibunAddress;    //지번주소
    private String x;   //경도
    private String y;   //위도

    public AddressVO() {
    }

    public AddressVO(String roadAddress, String jibunAddress, String x, String y) {
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.x = x;
        this.y = y;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public void setJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "AddressVO{" +
                "roadAddress='" + roadAddress + '\'' +
                ", jibunAddress='" + jibunAddress + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
