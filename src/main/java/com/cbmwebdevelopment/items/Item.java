/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.items;

import com.cbmwebdevelopment.bid.BidFXMLController;
import com.cbmwebdevelopment.connections.DBConnection;
import com.cbmwebdevelopment.tablecontrollers.ItemsTableController.AllItems;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author cmeehan
 */
public class Item {

    private final String imageSavePath = "http://localhost/auctioneer";
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private final DateFormat dateYear = new SimpleDateFormat("yyyy/MM/dd");
    private final DateFormat dateMonth = new SimpleDateFormat("MM");
    private final DateFormat dateDay = new SimpleDateFormat("dd");

    public void saveItem(ItemFXMLController controller) throws IOException {
        Connection conn = new DBConnection().connect();

        String sql = null;
        if (controller.itemNumber.trim().isEmpty() || controller.itemNumber == null) {
            sql = "INSERT INTO AUCTION_ITEMS(NAME, DESCRIPTION, RESERVE, SILENT_AUCTION, LIVE_AUCTION, CLOSED) VALUES(?,?,?,?,?,?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, controller.itemName);
                ps.setString(2, controller.itemDescription);
                ps.setString(3, controller.minimumBid);
                ps.setBoolean(4, controller.silentAuction);
                ps.setBoolean(5, controller.liveAuction);
                ps.setBoolean(6, false);
                ps.executeUpdate();
                ResultSet key = ps.getGeneratedKeys();
                if (key.next()) {
                    controller.itemNumberTextField.setText(String.valueOf(key.getInt(1)));
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Item Saved");
                    alert.setHeaderText("Item " + String.valueOf(key.getInt(1)) + " was successfully saved.");
                    alert.setContentText("Close this window to continue");
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
        } else {
            sql = "UPDATE AUCTION_ITEMS SET NAME = ?, DESCRIPTION = ?, RESERVE = ?, SILENT_AUCTION = ?, LIVE_AUCTION = ?, CLOSED = ? WHERE ID = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, controller.itemName);
                ps.setString(2, controller.itemDescription);
                ps.setString(3, controller.minimumBid);
                ps.setBoolean(4, controller.silentAuction);
                ps.setBoolean(5, controller.liveAuction);
                ps.setBoolean(6, false);
                ps.setString(7, controller.itemNumber);
                int rs = ps.executeUpdate();
                if (rs > 0) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Item Saved");
                    alert.setHeaderText("Item " + controller.itemNumber + " was successfully saved.");
                    alert.setContentText("Close this window to continue");
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

    }

    protected HashMap<String, String> getValues(String itemId) {
        HashMap<String, String> results = new HashMap<>();

        Connection conn = new DBConnection().connect();
        String sql = "SELECT NAME, DESCRIPTION, IMAGES, RESERVE, SILENT_AUCTION, LIVE_AUCTION, CLOSED FROM AUCTION_ITEMS WHERE ID =?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Has results");
                results.put("name", rs.getString("name"));
                results.put("description", rs.getString("DESCRIPTION"));
                results.put("imageUrl", rs.getString("IMAGES"));
                results.put("reserve", rs.getString("RESERVE"));
                results.put("silentAuction", String.valueOf(rs.getBoolean("SILENT_AUCTION")));
                results.put("liveAuction", String.valueOf(rs.getBoolean("LIVE_AUCTION")));
                results.put("closed", String.valueOf(rs.getBoolean("CLOSED")));
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
        return results;
    }

    public void getItem(String id, BidFXMLController controller) {
        Connection conn = new DBConnection().connect();
        String sql = "SELECT NAME, DESCRIPTION, CLOSED FROM AUCTION_ITEMS WHERE ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                controller.itemNameTextField.setText(rs.getString("NAME"));
                controller.itemDescriptionTextField.setText(rs.getString("DESCRIPTION"));
                if (rs.getBoolean("CLOSED")) {
                    controller.submitWinnerButton.setDisable(true);
                } else {
                    controller.submitWinnerButton.setDisable(false);
                }
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
    
    public ObservableList<AllItems> getAuctionItems(String terms, HashMap<String, String> filters){
        
        // The obeservable list to be returned
        ObservableList<AllItems> data = FXCollections.observableArrayList();
        
        Connection conn = new DBConnection().connect();
        String sql = "SELECT AUCTION_ITEMS.ID, AUCTION_ITEMS.NAME, AUCTION_ITEMS.DESCRIPTION, IF(SILENT_AUCTION = TRUE, 'Silent Auction', 'Live Auction') AS 'TYPE',  IF(CLOSED = TRUE, 'Closed', 'Open') as 'STATUS' FROM AUCTION_ITEMS JOIN BIDS ON AUCTION_ITEMS.ID = BIDS.ITEM_ID";
        
        if (terms != null || filters != null){
            sql += " WHERE ";
        }
        
        if(terms != null){
            sql += "NAME = ? OR DESCRIPTION = ? OR NAME LIKE ? OR DESCRIPTION LIKE ?";
        }
        
        if(filters != null){
            if(terms != null){
                sql += " AND ";
            }
            
            sql += " CLOSED = ? AND SILENT_AUCTION = ? AND LIVE_AUCTION = ?";
        }
        
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            if(terms != null){
                ps.setString(1, terms);
                ps.setString(2, terms);
                ps.setString(3, "%" + terms + "%");
                ps.setString(4, "%" + terms + "%");
            }
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                do{
                    data.addAll(new AllItems(rs.getString("ID"), rs.getString("NAME"), rs.getString("DESCRIPTION"), rs.getString("TYPE"), rs.getString("STATUS")));
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
        return data;        
    }
}
