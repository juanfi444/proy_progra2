
package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author juanf
 */
public class QueryTable {
    private Connection cn;
    
    private ArrayList<String> colsUserName;
    private ArrayList<String> colsAsientoName;
    private ArrayList<String> colsPeliculaName;
    private ArrayList<String> colsSalaName;
    private ArrayList<String> colsTicketName;
    
    public QueryTable(Connection cn){
        
        this.cn = cn;
        
        colsUserName = new ArrayList<String>();
        colsUserName.add("`id_usuario`");
        colsUserName.add("`nombre`");
        colsUserName.add("`apellido`");
        colsUserName.add("`edad`");
        colsUserName.add("`telefono`");
        
        colsAsientoName = new ArrayList<String>();
        colsAsientoName.add("`id_asiento`");
        colsAsientoName.add("`nombre_asiento`");
        colsAsientoName.add("`disponibilidad_asiento`");
        colsAsientoName.add("`color_asiento`");
        
        colsPeliculaName = new ArrayList<String>();
        colsPeliculaName.add("`id_pelicula`");
        colsPeliculaName.add("`nombre_pelicula`");
        
        colsSalaName = new ArrayList<String>();
        colsSalaName.add("`id_sala`");
        colsSalaName.add("`nombre_sala`");
        
        colsTicketName = new ArrayList<String>();
        colsTicketName.add("`id_ticket`");
        colsTicketName.add("`precio_tck`");
        colsTicketName.add("`semana`");  
    }
    
    private boolean isInt(String c){
        try{
            Integer.parseInt(c);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    private String parseActualizeCondition(String[] fields, ArrayList<String> colsName){
        
        String condition = "";
        
        for (int i = 0; i<fields.length; i++){
            condition += "".equals(fields[i])? "": colsName.get(i+1)+" = "+(isInt(fields[i])?fields[i]:("\""+fields[i]+"\""))+" , ";
        }
        condition = condition.substring(0, condition.length()-2);
        return condition;
    }
    
    private String parseFindCondition(String[] fields, ArrayList<String> colsName){
        String condition = "";
        for (int i = 0; i<fields.length; i++){
            condition += "".equals(fields[i])? "": colsName.get(i+1)+" = "+(isInt(fields[i])?fields[i]:("\""+fields[i]+"\""))+"and";
        }
        
        condition = condition.substring(0, condition.length()-3);   
        
        return condition;
    }
    
    public int makeRequest(ArrayList<String> values, ArrayList<String> colsName, String tableName){
        
        String requestQuery = "INSERT INTO ";
        
        requestQuery += tableName;
        
        requestQuery += " (";
        for (String auxColName: colsName){
            requestQuery += auxColName+",";
        }
        requestQuery = requestQuery.substring(0, requestQuery.length()-1)+")";
        
        requestQuery += " VALUES(";
        for (String auxValue : values){
           requestQuery += "?,";
        }
        requestQuery = requestQuery.substring(0, requestQuery.length()-1)+");";
        
        try{
            PreparedStatement pps = cn.prepareStatement(requestQuery);

            int auxIndex = 1;
            for(String auxValue: values){
                if(isInt(auxValue)){
                    pps.setInt(auxIndex, Integer.parseInt(auxValue));
                }else{
                    pps.setString(auxIndex, auxValue);
                }
                auxIndex++;
            }
            pps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
            return 1;
        }

        return 0;
    }
    
    private DefaultTableModel makeTable(String tableName, ArrayList<String> colsName, String conditions){
        DefaultTableModel tabla = new DefaultTableModel();
        
        try{
            int numCols = colsName.size();

            for (String auxColName: colsName){
                tabla.addColumn(auxColName);
            }
            
            String queryGetData = "SELECT * FROM "+tableName+" "+conditions+";";
            
            String[] dato=new String[numCols];
            
            Statement st;
            ResultSet rs;
            
            st=cn.createStatement();
            rs=st.executeQuery(queryGetData);

            while(rs.next()){
                for (int i = 0; i<numCols; i++){
                    dato[i] = rs.getString(i+1);
                }
                tabla.addRow(dato);
            }
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }
        
        return tabla;
        
    }
    
    private DefaultTableModel actualizeTable(String namedID,String tableName, String conditions, int id, ArrayList<String> colsName){
        String requestQuery = "UPDATE "+tableName+" SET ";
        
        requestQuery += conditions+" ";
        requestQuery += "WHERE "+"`"+namedID+"`"+" = "+id;
                        
        try{
            PreparedStatement pps = cn.prepareStatement(requestQuery);
            pps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }
        
        return makeTable(tableName, colsName, "");
    }
    
    private DefaultTableModel deleteTable(String namedID, String tableName, int id, ArrayList<String> colsName){
        String deleteQuery = "DELETE FROM "+tableName+" WHERE "+"`"+namedID+"`"+" = "+id;
        try{
            PreparedStatement pps = cn.prepareStatement(deleteQuery);
            pps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }
        
        return makeTable(tableName, colsName, "");
        
    }
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
    
    public DefaultTableModel makeUserTable (){
        ArrayList<String> colsName = colsUserName; 
        String tableName = "usuario";
        
        return makeTable(tableName, colsName,"");
    }
    
    public int makeUserRequest(ArrayList<String> values){
        ArrayList<String> colsName = new ArrayList<String>(colsUserName.subList(1, colsUserName.size()));
        String tableName = "usuario";
        
        return makeRequest(values, colsName, tableName);
    }
    
    public DefaultTableModel makeUserTableConditions(String[] fields){
        
        String condition = "WHERE "+parseFindCondition(fields, colsUserName);
        String tableName = "usuario";
             
        return makeTable(tableName, colsUserName, condition);
    }
    
    public DefaultTableModel actualizeUser(int id, String[] fields){
        
        String condition = parseActualizeCondition(fields, colsUserName);
        String tableName = "usuario";
        String namedID = "id_usuario";
        
        return actualizeTable( namedID, tableName, condition, id, colsUserName); 

    }
    
    public DefaultTableModel deleteUser(int id){
        String namedID = "id_usuario";
        String tableName = "usuario";
        
        return deleteTable(namedID, tableName, id, colsUserName);
    }
    
    
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
    
    public DefaultTableModel makeAsientoTable (){
        ArrayList<String> colsName = colsAsientoName; 
        String tableName = "asiento";
        
        return makeTable(tableName, colsName,"");
    }
    
    public int makeAsientoRequest(ArrayList<String> values){
        ArrayList<String> colsName = new ArrayList<String>(colsAsientoName.subList(1, colsAsientoName.size()));
        String tableName = "asiento";
        
        return makeRequest(values, colsName, tableName);
    }
    
    public DefaultTableModel makeAsientoTableConditions(String[] fields){
        ArrayList<String> colsName = colsAsientoName; 
        String condition = "WHERE "+parseFindCondition(fields, colsName);
        String tableName = "asiento";
             
        return makeTable(tableName, colsName, condition);
    }
    
    public DefaultTableModel actualizeAsiento(int id, String[] fields){
        ArrayList<String> colsName = colsAsientoName;
        String condition = parseActualizeCondition(fields, colsName);
        String tableName = "asiento";
        String namedID = "id_asiento";
        
        return actualizeTable( namedID, tableName, condition, id, colsName); 

    }
    
    public DefaultTableModel deleteAsiento(int id){
        ArrayList<String> colsName = colsAsientoName;
        String tableName = "asiento";
        String namedID = "id_asiento";
        
        return deleteTable(namedID, tableName, id, colsName);
    }
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
    
    public DefaultTableModel makePeliculaTable (){
        ArrayList<String> colsName = colsPeliculaName; 
        String tableName = "pelicula";
        
        return makeTable(tableName, colsName,"");
    }
    
    public DefaultTableModel makeAsientoPeliculaTable (){
        ArrayList<String> colsName = colsPeliculaName; 
        String tableName = "pelicula";
        
        return makeTable(tableName, colsName,"");
    }
    
    public int makePeliculaRequest(ArrayList<String> values){
        ArrayList<String> colsName = new ArrayList<String>(colsPeliculaName.subList(1, colsPeliculaName.size()));
        String tableName = "pelicula";
        
        return makeRequest(values, colsName, tableName);
    }
    
    public DefaultTableModel makePeliculaTableConditions(String[] fields){
        ArrayList<String> colsName = colsPeliculaName; 
        String condition = "WHERE "+parseFindCondition(fields, colsName);
        String tableName = "pelicula";
             
        return makeTable(tableName, colsName, condition);
    }
    
    public DefaultTableModel actualizePelicula(int id, String[] fields){
        ArrayList<String> colsName = colsPeliculaName;
        String condition = parseActualizeCondition(fields, colsName);
        String tableName = "pelicula";
        String namedID = "id_pelicula";
        
        return actualizeTable( namedID, tableName, condition, id, colsName); 

    }
    
    public DefaultTableModel deletePelicula(int id){
        ArrayList<String> colsName = colsPeliculaName;
        String tableName = "pelicula";
        String namedID = "id_pelicula";
        
        return deleteTable(namedID, tableName, id, colsName);
    }
    
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
    
    public DefaultTableModel makeSalaTable (){
        ArrayList<String> colsName = colsSalaName; 
        String tableName = "sala";
        
        return makeTable(tableName, colsName,"");
    }
    
    
    public int makeSalaRequest(ArrayList<String> values){
        ArrayList<String> colsName = new ArrayList<String>(colsSalaName.subList(1, colsSalaName.size()));
        String tableName = "sala";
        
        return makeRequest(values, colsName, tableName);
    }
    
    public DefaultTableModel makeSalaTableConditions(String[] fields){
        ArrayList<String> colsName = colsSalaName; 
        String condition = "WHERE "+parseFindCondition(fields, colsName);
        String tableName = "sala";
             
        return makeTable(tableName, colsName, condition);
    }
    
    public DefaultTableModel actualizeSala(int id, String[] fields){
        ArrayList<String> colsName = colsSalaName;
        String condition = parseActualizeCondition(fields, colsName);
        String tableName = "sala";
        String namedID = "id_sala";
        
        return actualizeTable( namedID, tableName, condition, id, colsName); 

    }
    
    public DefaultTableModel deleteSala(int id){
        ArrayList<String> colsName = colsSalaName;
        String tableName = "sala";
        String namedID = "id_sala";
        
        return deleteTable(namedID, tableName, id, colsName);
    }
    
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
//    ----------------------------------------------------------------------------
    
    public DefaultTableModel makeTicketTable (){
        ArrayList<String> colsName = colsTicketName; 
        String tableName = "ticket";
        
        return makeTable(tableName, colsName,"");
    }
    
    
    public int makeTicketRequest(ArrayList<String> values){
        ArrayList<String> colsName = new ArrayList<String>(colsTicketName.subList(1, colsTicketName.size()));
        String tableName = "ticket";
        
        return makeRequest(values, colsName, tableName);
    }
    
    public DefaultTableModel makeTicketTableConditions(String[] fields){
        ArrayList<String> colsName = colsTicketName; 
        String condition = "WHERE "+parseFindCondition(fields, colsName);
        String tableName = "ticket";
             
        return makeTable(tableName, colsName, condition);
    }
    
    public DefaultTableModel actualizeTicket(int id, String[] fields){
        ArrayList<String> colsName = colsTicketName;
        String condition = parseActualizeCondition(fields, colsName);
        String tableName = "ticket";
        String namedID = "id_ticket";
        
        return actualizeTable( namedID, tableName, condition, id, colsName); 

    }
    
    public DefaultTableModel deleteTicket(int id){
        ArrayList<String> colsName = colsTicketName;
        String tableName = "ticket";
        String namedID = "id_ticket";
        
        return deleteTable(namedID, tableName, id, colsName);
    }
}
