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

    private Algorithm algorithm;

    private String issuer;

    /**
     * token存储的头
     */
    private String tokenHeader;

    public JwtHolder(Algorithm algorithm, String issuer,String tokenHeader) {
        this.algorithm = algorithm;
        this.issuer = issuer;
        this.tokenHeader = tokenHeader;
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
     * @throws NoTokenFoundException 在tokenHeader指定的header中找不到token
     * @throws SignatureVerificationException 签名不一致
     * @throws TokenExpiredException token过期
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     * @return JWT相关信息封装
     */
    public DecodedJWT decodedJWT() throws NoTokenFoundException,SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException  {
        final String token = RequestContextHolderUtil.getRequest().getHeader(this.tokenHeader);
        if (token == null) {
            throw new NoTokenFoundException();
        }
        final JWTVerifier verifier = JWT.require(this.algorithm).build();
        final DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    /**
     * 验证失败抛出异常
     * @throws NoTokenFoundException 在tokenHeader指定的header中找不到token
     * @throws SignatureVerificationException 签名不一致
     * @throws TokenExpiredException token过期
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     */
    public void verify() throws SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException {
        decodedJWT();
    }

    /**
     * 获取负载信息
     * @throws NoTokenFoundException 在tokenHeader指定的header中找不到token
     * @throws SignatureVerificationException 签名不一致
     * @throws TokenExpiredException token过期
     * @throws AlgorithmMismatchException 算法不一致
     * @throws InvalidClaimException 失效的payload
     * @return 负载信息
     */
    public Map<String,Object> getPayload() throws SignatureVerificationException, TokenExpiredException, AlgorithmMismatchException, InvalidClaimException {
        return decodedJWT().getClaim(PAYLOAD_KEY).asMap();
    }


}
