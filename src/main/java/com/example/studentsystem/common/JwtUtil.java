package com.example.studentsystem.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 极简版 JWT 工具类
 * 使用了 HMAC-SHA256 算法对头部和载荷进行签名，防止 Token 被篡改。
 */
public class JwtUtil{
    //签名用的密钥
    private static final String SECRET_KEY = "MySecretKeyForStudentSystem";

    /**
     * 生成 Token
     * @param userId 用户ID
     * @param role 用户角色（STUDENT/TEACHER）
     * @return 生成好的 JWT 字符串
     */
    public static String generateToken(Long userId, String role){
        //1.构建 Header
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        //2.构建 Payload (载荷)，存放用户ID和角色
        String payload = "{\"userId\":" + userId + ",\"role\":\"" + role + "\"}";

        //3.Base64 URL 编码（Java 自带的 Base64.getUrlEncoder()）
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        //4.拼接前两部分
        String content = encodedHeader + "." + encodedPayload;

        //5.计算签名 (使用 HMAC-SHA256)
        String signature = hmacSha256(content, SECRET_KEY);

        //6.返回最终的 Token
        return content + "." + signature;
    }

    /**
     * HMAC-SHA256 签名算法
     */
    private static String hmacSha256(String data, String key){
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        }catch(Exception e){
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * 解析 Token，获取用户ID
     * @param token 客户端传来的 Token
     * @return 用户ID (如果解析失败，返回 null)
     */
    public static Long getUserIdFromToken(String token){
        try{
            //按点号分割成三部分：Header, Payload, Signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            //解码 Payload 部分
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            //从 JSON 字符串里提取 userId(字符串切割）
            String userIdStr = payload.substring(payload.indexOf("\"userId\":") + 9, payload.indexOf(","));
            return Long.parseLong(userIdStr);
        }catch(Exception e){
            return null; //解析失败
        }
    }
}