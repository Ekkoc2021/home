package com.yang.home.common.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.MapUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class JwtUtil {

    // 秒为单位
    public static final long SECOND = 1000;

    public static final long MINUTES = SECOND*60;

    public static final long HOURS = MINUTES*60;

    public static final long DAYS = HOURS*24;

    public static final long WEEKS = DAYS*7;

    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    // 获取 token
    public static String getToken(Map<String,Object> claims,String secret,long exp) {

        if (secret==null||secret.length()==0){
            secret=new String("TestTokenSecret");
        }
        if (claims==null){
            claims = new HashMap();
        }
        // 头部
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        // 过期时间设置
        String string = MapUtils.getString(claims, "exp", "");
        if (string.length() == 0){
            // 默认一天
            claims.put("exp",new Date( System.currentTimeMillis() + DAYS));
        }
           /*	iss: jwt签发者
                sub: jwt所面向的用户
                aud: 接收jwt的一方
                exp: jwt的过期时间，这个过期时间必须要大于签发时间
                nbf: 定义在什么时间之前，该jwt都是不可用的.
                iat: jwt的签发时间
                jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
            */

        return Jwts.builder()
                .setHeader(header)
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date( System.currentTimeMillis() + exp))
                .signWith(SIGNATURE_ALGORITHM,secret)
                .compact();
    }
    // 验证 token
    public static Map<String,Object> validateToken(String token,String secret) {
        try {
            // 自动校验签发时间,过期时间
            Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return new HashMap<>(body);
        } catch (Exception e) {
            return null; // 表示验证失败
        }
    }
}
