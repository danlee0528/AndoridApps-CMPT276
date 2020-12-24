package com.example.daniellee.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Daniel Lee on 2017-02-03.

 */
public class PotTest {
    private Pot pot_1 = new Pot("name",5);

    ///*Test cases for correct inputs
    // * should give 4 passes
    @Test
    public void getWeightInG() throws Exception {
        assertEquals(5,pot_1.getWeightInG());
    }

    @Test
    public void setWeightInG() throws Exception {
        pot_1.setWeightInG(6);
        assertEquals(6, pot_1.getWeightInG());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name",pot_1.getName());
    }

    @Test
    public void setName() throws Exception {
        pot_1.setName("name123");
        assertEquals("name123",pot_1.getName());
    }
    //*/

    /* Test cases for incorrect inputs
     * should give 4 test failed errors

    @Test
    public void getWeightInG() throws Exception {
        assertEquals(6,pot_1.getWeightInG());
    }

    @Test
    public void setWeightInG() throws Exception {
        pot_1.setWeightInG(4);
        assertEquals(6, pot_1.getWeightInG());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name123",pot_1.getName());
    }

    @Test
    public void setName() throws Exception {
        pot_1.setName("name123");
        assertEquals("name1234",pot_1.getName());
    }
    */

}