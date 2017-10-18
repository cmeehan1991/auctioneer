/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import com.cbmwebdevelopment.bid.ViewBidsTableViewController.Bids;
import com.cbmwebdevelopment.connections.DBConnection;
import static com.cbmwebdevelopment.main.MainApp.CURRENCY_FORMAT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author cmeehan
 */
public class Bid {

    /**
     * Submit the winning bid to the database.
     *
     * @param controller
     */
    protected void submitBid(BidFXMLController controller) {
        Connection conn = new DBConnection().connect();
        String sql;
        if (bidExists(controller.itemNumber)) {
            sql = "UPDATE BIDS SET AMOUNT = ?, USER_ID = ? WHERE ITEM_ID = ?";
        } else {
            sql = "INSERT INTO BIDS (AMOUNT, USER_ID, ITEM_ID) VALUES (?,?,?)";
        }
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, controller.bidAmount);
            ps.setString(2, controller.bidderNumber);
            ps.setString(3, controller.itemNumber);
            ps.executeUpdate();
            
            ps.close();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Bid Submitted");
            alert.setHeaderText("The bid has been successfully submitted");
            alert.setContentText("The bid was successfully submitted.");
            alert.showAndWait();
        } catch (SQLException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Databae Error");
            alert.setHeaderText("Error submitting the bid to the database");
            alert.setContentText("Error Message:\n" + ex.getMessage());
            alert.showAndWait();
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
     * Check if the item has already been won. Returns true if the item has been
     * won.
     *
     * @param id - Item ID Number
     * @return
     */
    private boolean bidExists(String id) {
        boolean exists = false;
        Connection conn = new DBConnection().connect();
        String sql = "SELECT ID FROM BIDS WHERE ITEM_ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
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
        return exists;
    }

    /**
     *
     * @return
     */
    protected ObservableList<Bids> getBids() {
        ObservableList<Bids> data = FXCollections.observableArrayList();
        Connection conn = new DBConnection().connect();
        String sql = "SELECT BIDS.ID AS 'ID', AUCTION_ITEMS.NAME AS 'ITEM_NAME', CONCAT(IF(BIDDERS.PREFIX != '-', CONCAT(BIDDERS.PREFIX, ' '), ''), BIDDERS.FIRST_NAME, ' ', BIDDERS.LAST_NAME, IF(BIDDERS.SUFFIX != '-', CONCAT(', ', BIDDERS.SUFFIX), '')) AS 'NAME', BIDS.AMOUNT AS 'AMOUNT' FROM BIDS INNER JOIN BIDDERS ON BIDDERS.ID = BIDS.USER_ID INNER JOIN AUCTION_ITEMS ON AUCTION_ITEMS.ID = BIDS.ITEM_ID";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                do {
                    data.addAll(new Bids(rs.getString("ID"), rs.getString("ITEM_NAME"), rs.getString("NAME"), CURRENCY_FORMAT.format(rs.getDouble("AMOUNT"))));
                } while (rs.next());
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
        return data;
    }
    
        
    public HashMap<Integer, List<String>> winningBidInformation(){
        HashMap<Integer, List<String>> list = new HashMap<>();
        Connection conn = new DBConnection().connect();
        String sql = "SELECT AUCTION_ITEMS.NAME AS 'ITEM NAME', AUCTION_ITEMS.DESCRIPTION AS 'ITEM DESCRIPTION', BIDS.AMOUNT AS 'WINNING AMOUNT', CONCAT(BIDDERS.FIRST_NAME, ' ', BIDDERS.LAST_NAME) AS 'WINNER NAME', BIDDERS.ID AS 'BIDDER ID' FROM BIDS INNER JOIN AUCTION_ITEMS ON BIDS.ITEM_ID = AUCTION_ITEMS.ID INNER JOIN BIDDERS ON BIDS.USER_ID = BIDDERS.ID";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Integer integer = 1;
                do{
                    list.put(integer, new ArrayList<String>(){{add(rs.getString("ITEM NAME")); add(rs.getString("ITEM DESCRIPTION")); add(CURRENCY_FORMAT.format(rs.getDouble("WINNING AMOUNT"))); add(rs.getString("WINNER NAME")); add(rs.getString("BIDDER ID"));}});
                    integer++;
                }while(rs.next());
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
        return list;
    }
}
