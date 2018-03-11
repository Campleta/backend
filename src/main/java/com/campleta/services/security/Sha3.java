/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services.security;

import java.io.UnsupportedEncodingException;
import javax.ws.rs.BadRequestException;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;

/**
 *
 * @author Vixo
 */
public class Sha3 {

    private static Size DEFAULT = Size.S224;
    
    public static String encode(String s) {
        if(s == null || s.equals("")) throw new BadRequestException();
        DigestSHA3 md = new DigestSHA3(DEFAULT.getValue());
        
        try {
            md.update(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            
        }
        
        byte[] digest = md.digest();
        return encodeToString(digest);
    }
    
    private static String encodeToString(byte[] bytes) {
        return Hex.toHexString(bytes);
    }
    
    protected enum Size {
        S224(224),
        S256(256),
        S384(384),
        S512(512);
        
        int bits = 0;
        
        Size(int bits) {
            this.bits = bits;
        }
        
        public int getValue() {
            return this.bits;
        }
    }
}
