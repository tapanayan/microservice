package com.msn.poc.user.bm;

import java.io.IOException;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msn.poc.user.exception.TokenException;
import com.msn.poc.user.model.CustomClaim;
import com.msn.poc.user.model.UserDetail;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTTokenUtility {
	private final String secretKey="MicroServiceHackathon";
	private final String algorithm="AES";
	private static final Logger log = LoggerFactory.getLogger(JWTTokenUtility.class);
	public String generateToken(UserDetail userDetails) throws TokenException {
        try {
            final SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
            final String payload = getPayload(userDetails);
                       
            return Jwts.builder()
            		.setPayload(payload).signWith(SignatureAlgorithm.HS512, skeySpec).compact();
        }catch (JwtException ex) {
            throw new TokenException(ex.getMessage(), ex);
        }catch(IllegalStateException ex){
        	throw new TokenException(ex.getMessage(), ex);
        }
    }
	
	public UserDetail getUserDetail(String token) throws TokenException {
        try {
            UserDetail userDetail = new UserDetail();
            userDetail.setUserName(getValueFromToken(token, "userId"));
            userDetail.setUserRole(getValueFromToken(token, "role"));
            return userDetail;
        }catch (JwtException ex) {
            throw new TokenException(ex.getMessage(), ex);
        }catch(IllegalStateException ex){
        	throw new TokenException(ex.getMessage(), ex);
        }
    }
	
	private String getValueFromToken(final String token, String fieldName){
        return String.valueOf(Jwts.parser().setSigningKey(new SecretKeySpec(secretKey.getBytes(), algorithm)).parseClaimsJws(token).getBody().get(fieldName));
    }

	private String getPayload(UserDetail userDetails) throws TokenException {
		Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.HOUR, 10); 
        CustomClaim customClaim = new CustomClaim();
        customClaim.setRole(userDetails.getUserRole());
        customClaim.setUserId(userDetails.getUserName());
        customClaim.setExp(calendar.getTime());
        return objectToJSON(customClaim);
    }
	
	public String objectToJSON(final Object obj) {
        String json = null;
        final ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(obj);
        }catch (IOException ex) {
        	log.error("Error while converting to Json"+ex);
        }
        return json;
    }
}
