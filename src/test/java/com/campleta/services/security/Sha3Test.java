/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.services.security;

import javax.ws.rs.BadRequestException;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vixo
 */
public class Sha3Test {
    
    private Sha3 sha3;
    private static String EMPTYSTR224 = "f71837502ba8e10837bdd8d365adb85591895602fc552b48b7390abd";
    private static String EMPTYSTR256 = "c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
    private static String EMPTYSTR384 = "2c23146a63a29acf99e73b88f8c24eaa7dc60aa771780ccc006afbfa8fe2479b2dd2b21362337441ac12b515911957ff";
    private static String EMPTYSTR512 = "0eab42de4c3ceb9235fc91acffe746b29c29a8c366b7c60e4e67c466f36a4304c00fa9caf9d87976ba469bcbe06713b435f091ef2769fb160cdab33d3670680e";
    
    
    public Sha3Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void sha3EncodeSuccessTest() {
        String s = "1234";
        String expected = "b0f3dc043a9c5c05f67651a8c9108b4c2b98e7246b2eea14cb204295";
        String result = Sha3.encode(s);
        
        assertThat(result, is(expected));
    }
    
    @Test (expected = BadRequestException.class)
    public void doEMPTYSTR224() {
        String s = "";
        String result = Sha3.encode(s);
    }
}
