/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

/**
 *
 * @author bastin
 */
public class Item {
    
    private String key;
    private String value;
    
    public Item(String k, String v)
    {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        return this.value;
    }
}
