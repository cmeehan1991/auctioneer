/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bidder;

import com.cbmwebdevelopment.alerts.Alerts;
import com.cbmwebdevelopment.connections.DBConnection;
import com.mysql.cj.api.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author cmeehan
 */
public class Bidder {

    /**
     * Save a new bidder
     *
     * @param args
     * @param controller
     */
    protected void saveBidder(HashMap<String, String> args, BidderFXMLController controller) {
        Connection conn = new DBConnection().connect();
        String sql = "INSERT INTO BIDDERS(PREFIX, FIRST_NAME, LAST_NAME, SUFFIX, TYPE, TELEPHONE, EMAIL, PRIMARY_ADDRESS, SECONDARY_ADDRESS, CITY, STATE, POSTAL_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, args.get("prefix"));
            ps.setString(2, args.get("firstName"));
            ps.setString(3, args.get("lastName"));
            ps.setString(4, args.get("suffix"));
            ps.setString(5, "BIDDER");
            ps.setString(6, args.get("telephone"));
            ps.setString(7, args.get("email"));
            ps.setString(8, args.get("primaryAddress"));
            ps.setString(9, args.get("secondaryAddress"));
            ps.setString(10, args.get("city"));
            ps.setString(11, args.get("state"));
            ps.setString(12, args.get("postalCode"));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                controller.bidderIdTextField.setText(rs.getString(1));
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Bidder Saved");
                alert.setHeaderText("Bidder saved to database");
                alert.setContentText("Bidder " + rs.getString(1) + " has been added.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Update an existing bidder
     *
     * @param args
     */
    protected void updateBidder(HashMap<String, String> args, BidderFXMLController controller) {
        Connection conn = new DBConnection().connect();
        String sql = "UPDATE BIDDERS SET PREFIX = ?, FIRST_NAME = ?, LAST_NAME = ?, SUFFIX = ?, TYPE = ?, TELEPHONE = ?, EMAIL = ?, PRIMARY_ADDRESS = ?, SECONDARY_ADDRESS = ?, CITY = ?, STATE = ?, POSTAL_CODE = ? WHERE ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, args.get("prefix"));
            ps.setString(2, args.get("firstName"));
            ps.setString(3, args.get("lastName"));
            ps.setString(4, args.get("suffix"));
            ps.setString(5, "BIDDER");
            ps.setString(6, args.get("telephone"));
            ps.setString(7, args.get("email"));
            ps.setString(8, args.get("primaryAddress"));
            ps.setString(9, args.get("secondaryAddress"));
            ps.setString(10, args.get("city"));
            ps.setString(11, args.get("state"));
            ps.setString(12, args.get("postalCode"));
            ps.setString(13, args.get("id"));
            int update = ps.executeUpdate();
            if(update > 0){
                new Alerts().informationAlert("Bidder Updated", "Bidder " + args.get("id") + " updated", "The bidder has been successfully updated.").showAndWait();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Get an existing bidder
     *
     * @param id
     * @return
     */
    public HashMap<String, String> getUser(String id) {
        HashMap<String, String> user = new HashMap<>();

        Connection conn = new DBConnection().connect();

        String sql = "SELECT ID, PREFIX, FIRST_NAME, LAST_NAME, SUFFIX, TYPE, TELEPHONE, EMAIL, PRIMARY_ADDRESS, SECONDARY_ADDRESS, CITY, STATE, POSTAL_CODE FROM BIDDERS WHERE ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.put("id", rs.getString("ID"));
                user.put("prefix", rs.getString("PREFIX"));
                user.put("firstName", rs.getString("FIRST_NAME"));
                user.put("lastName", rs.getString("LAST_NAME"));
                user.put("suffix", rs.getString("SUFFIX"));
                user.put("type", rs.getString("TYPE"));
                user.put("telephone", rs.getString("TELEPHONE"));
                user.put("email", rs.getString("EMAIL"));
                user.put("primaryAddress", rs.getString("PRIMARY_ADDRESS"));
                user.put("secondaryAddress", rs.getString("SECONDARY_ADDRESS"));
                user.put("city", rs.getString("CITY"));
                user.put("state", rs.getString("STATE"));
                user.put("postalCode", rs.getString("POSTAL_CODE"));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return user;
    }
    
    /**
     * Get the bidder's email address. 
     * @param id
     * @return 
     */
    public String getEmail(String id){
        String userEmail = null;
        if(id == null){
            Alert alert = new Alerts().errorAlert("Invalid ID", "The Bidder ID is not valid.", "Please check the ", null);
            alert.showAndWait();
            return null;
        }
        
        Connection conn = new DBConnection().connect();
        String sql = "SELECT EMAIL FROM BIDDERS WHERE ID = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                userEmail = rs.getString("EMAIL");
            }
        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }finally{
            try{
                conn.close();
            }catch(SQLException ex){
                System.err.println(ex.getMessage());
            }
        }
        
        return userEmail;
        
    }
}
