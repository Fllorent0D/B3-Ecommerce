/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_dismap;

/**
 *
 * @author bastin
 */
public interface protocoleDISMAP {
    public final static int REQUEST_LOGIN = 1;
    public final static int REQUEST_LOGOUT = 2;
    public final static int REQUEST_SEARCH_GOODS = 3;
    public final static int REQUEST_TAKE_GOODS = 4;
    public final static int REQUEST_BUY_GOODS = 5;
    public final static int REQUEST_DELIVERY_GOODS = 6;
    public final static int REQUEST_LIST_SALES = 7;
    
    public final static int REQUEST_INTERRUPT = 9;
}
