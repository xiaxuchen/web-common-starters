package com.originit.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.originit.common.exception.NoTokenFoundException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xxc
 */
public class JwtHolder {

    private static final String PAYLOAD_KEY = "payload";
    private final int expireTime;
    private final int refreshExpireTime;

    private Algorithm algorithm;

    private String issuer;

    private final String refreshIdKey;

    private final String userIdKey;

    public JwtHolder(Algorithm algorithm, String issuer) {
        this.algorithm = algorithm;
        this.issuer = issuer;
        this.userIdKey = "userId";
        this.refreshIdKey = "refreshId";
        this.expireTime = 60*60;
        this.refreshExpireTime = 7*24*60*60;
    }

    public JwtHolder(Algorithm algorithm, String issuer,String userIdKey,String refreshIdKey,int expireTime,int refreshExpireTime) {
        this.algorithm = algorithm;
        this.issuer = issuer;
        this.refreshIdKey = refreshIdKey;
        this.userIdKey = userIdKey;
        this.expireTime = expireTime;
        this.refreshExpireTime = refreshExpireTime;
    }

    /**
     * 生成token
     * @param claims payload
     * @param expireTime 过期时间
     * @return jwtToken
     */
    public String generateToken(Map<String,Object> claims, int expireTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,expireTime);
        return JWT.create()
                .withClaim(PAYLOAD_KEY, claims)
                .withIssuer(this.issuer)
                .withExpiresAt(calendar.getTime())
                .sign(this.algorithm);
    }

    /**
     * 生成token
     * @param key payload的键
     * @param value payload的值
     * @param expireTime 过期时间
     * @return jwtToken
     */
    public String generateToken(String key,Object value, int expireTime) {
        Map<String,Object> claims = new HashMap<>();
        claims.put(key, value);
        return generateToken(claims,expireTime);
    }



    /**
     * 解码jwt
     * @throws SignatureVerificationException 签名不一致
     * @throws TokenExpiredException token过期
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     * @return JWT相关信息封装
     */
    public DecodedJWT decodedJWT(String token) throws NoTokenFoundException,SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException  {
        final JWTVerifier verifier = JWT.require(this.algorithm).build();
        return verifier.verify(token);
    }

    /**
     * 验证失败抛出异常
     * @throws SignatureVerificationException 签名不一致
     * @throws TokenExpiredException token过期
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     */
    public void verify(String token) throws SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException {
        decodedJWT(token);
    }

    /**
     * 获取负载信息
     * @throws SignatureVerificationException 签名不一致
     * @throws TokenExpiredException token过期
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     * @return 负载信息
     */
    public Map<String,Object> getPayload(String token) throws SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException {
        return decodedJWT(token).getClaim(PAYLOAD_KEY).asMap();
    }


    /**
     * 不校验直接拿取
     * @throws SignatureVerificationException 签名不一致
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     * @param token
     */
    public DecodedJWT decodedJWTWithoutVerify(String token) {
        return JWT.decode(token);
    }

    /**
     * 不校验直接拿取
     * @throws SignatureVerificationException 签名不一致
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     * @param token
     */
    public Map<String,Object> justPayload(String token) throws SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException {
        return decodedJWTWithoutVerify(token).getClaim(PAYLOAD_KEY).asMap();
    }

    /**
     * 快速创建token
     * @param userId 用户id
     * @return access token
     */
    public String generateToken(String userId) {
        return generateToken(userIdKey,userId,expireTime);
    }

    /**
     * 快速生成refresh token
     * @param userId 用户id
     * @param refreshId refresh id
     * @return refresh token
     */
    public String generateRefreshToken(String userId,String refreshId) {
        final Map<String, Object> payload = new HashMap<>(2);
        payload.put(userIdKey,userId);
        payload.put(refreshIdKey,refreshId);
        return generateToken(payload,refreshExpireTime);
    }

}
