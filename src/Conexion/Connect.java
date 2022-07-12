package Conexion;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author juanf
 */
public class Connect {
        
    private String usuario;
    private String pass;
    private String ip;
    private String port;
    private String bd;
    private String con;
    
    private Connection connect;
        
    public Connect(){
        
        usuario = "";
        pass = "";
        ip = "localhost";
        port = "3306";
        bd = "cine";
        
        con = "jdbc:mysql://"+ip+":"+port+"/"+bd;
        
    }
    
    public Connect(String usuario, String pass){
        
        this.usuario = usuario;
        this.pass = pass;
        ip = "localhost";
        port = "3306";
        bd = "cine";
        
        con = "jdbc:mysql://"+ip+":"+port+"/"+bd;
        
    }
    
//    public Connect(String usuario, String pass){
//        
//        this.usuario = usuario;
//        this.pass = pass;
//        ip = "containers-us-west-81.railway.app";
//        port = "6513";
//        bd = "railway";
//        
//        con = "jdbc:mysql://"+ip+":"+port+"/"+bd;
//        
//    }
    
    public Connection realizarConexion(){
        try{
            connect = DriverManager.getConnection(this.con, this.usuario, this.pass);
        }catch(Exception e){
            connect = null;
        }
        
        return connect;
    }
    
}
