package com.example.VNPay_Api_Integration.Util;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class VNPayUtil {
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request){
        String ipAddress;
        try{
            ipAddress = request.getHeader("X-Forwarded-For"); //Lấy địa chỉ IP của client gửi request, Xử lý cả trường hợp client đứng sau proxy/load balancer
            if(ipAddress == null){
                ipAddress = request.getRemoteAddr();
            }
        }catch (Exception e){
            ipAddress = "Invalid IP: " + e.getMessage();
        }
        return ipAddress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++){
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) { //entry = 1 cặp key-value
        return paramsMap.entrySet().stream() //Chuyển Map thành Stream để xử lý
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) //Loại bỏ các entry có value là null hoặc rỗng
                .sorted(Map.Entry.comparingByKey()) //Sắp xếp các entry theo thứ tự alphabet của key
                .map(entry ->
                        (encodeKey ? URLEncoder.encode(entry.getKey(), //mã hóa bằng URLEncoder.encode với bộ mã hóa US_ASCII, giúp mã hóa key để tuân thủ chuẩn URL encoding (ví dụ: các ký tự đặc biệt trong URL cần được mã hóa).
                                StandardCharsets.US_ASCII)
                                : entry.getKey()) + "=" +
                                URLEncoder.encode(entry.getValue()
                                        , StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }
}
