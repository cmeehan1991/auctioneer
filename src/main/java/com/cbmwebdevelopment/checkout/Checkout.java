/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.checkout;

import com.cbmwebdevelopment.tablecontrollers.CheckoutItemTableViewController.ItemData;
import com.cbmwebdevelopment.connections.DBConnection;
import static com.cbmwebdevelopment.main.MainApp.CURRENCY_FORMAT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author cmeehan
 */
public class Checkout {

    private Double amountDue = 0.0;

    public void getUserInformation(String userId, CheckoutFXMLController controller) {
        System.out.println(userId);
        Connection conn = new DBConnection().connect();
        String sql = "SELECT ID, IF(PREFIX != '-', PREFIX, '') AS 'PREFIX', CONCAT(FIRST_NAME, ' ', LAST_NAME) AS 'NAME', IF(SUFFIX != '-', SUFFIX, '') AS 'SUFFIX', PRIMARY_ADDRESS, SECONDARY_ADDRESS, CITY, STATE, POSTAL_CODE FROM BIDDERS WHERE ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                controller.bidderIdTextField.setText(rs.getString("ID"));

                String name = rs.getString("PREFIX") + " " + rs.getString("NAME") + " " + rs.getString("SUFFIX");
                controller.bidderNameTextField.setText(name.trim());

                String billingAddress = rs.getString("PRIMARY_ADDRESS") + "\n";
                if (rs.getString("SECONDARY_ADDRESS") != null) {
                    billingAddress += rs.getString("SECONDARY_ADDRESS") + "\n";
                }
                billingAddress += rs.getString("CITY") + ", " + rs.getString("STATE") + " " + rs.getString("POSTAL_CODE");
                controller.billingAddressTextArea.setText(billingAddress);
                controller.bidderItemsTableView.setItems(getItems(userId));
                controller.totalAmountTextField.setText(totalAmountDue(userId));
                controller.totalItemsTextField.setText(totalItems(userId));
            } else {
                controller.bidderNameTextField.clear();
                controller.billingAddressTextArea.clear();
                controller.totalItemsTextField.clear();
                controller.totalAmountTextField.clear();
                controller.bidderItemsTableView.getItems().clear();
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            } finally {
                controller.progressIndicator.setVisible(false);
            }

        }
    }

    public String totalItems(String id) {
        String totalItems = null;
        Connection conn = new DBConnection().connect();
        String sql = "SELECT COUNT(DISTINCT BIDS.ITEM_ID) AS 'COUNT' FROM BIDS WHERE BIDS.USER_ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalItems = rs.getString("COUNT");
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
        return totalItems;
    }

    public String totalAmountDue(String id) {
        String totalAmountDue = null;
        Connection conn = new DBConnection().connect();
        String sql = "SELECT SUM(BIDS.AMOUNT) AS 'TOTAL_AMOUNT' FROM BIDS WHERE BIDS.USER_ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalAmountDue = CURRENCY_FORMAT.format(rs.getDouble("TOTAL_AMOUNT"));
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
        return totalAmountDue;
    }

    /**
     * Get the items that the user won. Returns an observable list to populate
     * the table with.
     *
     * @param id
     * @return
     */
    public ObservableList getItems(String id) {
        ObservableList<ItemData> data = FXCollections.observableArrayList();
        Connection conn = new DBConnection().connect();
        String sql = "SELECT BIDS.ITEM_ID AS 'ITEM_ID', BIDS.AMOUNT AS 'ITEM_AMOUNT', AUCTION_ITEMS.NAME AS 'ITEM_NAME', AUCTION_ITEMS.DESCRIPTION AS'ITEM_DESCRIPTION' FROM BIDS JOIN AUCTION_ITEMS ON AUCTION_ITEMS.ID = BIDS.ITEM_ID WHERE BIDS.USER_ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //controller.totalAmountTextField.setText(CURRENCY_FORMAT.format(Double.parseDouble(rs.getString("TOTAL_AMOUNT"))));
                do {
                    data.addAll(new ItemData(rs.getString("ITEM_ID"), rs.getString("ITEM_NAME"), rs.getString("ITEM_DESCRIPTION"), CURRENCY_FORMAT.format(Double.parseDouble(rs.getString("ITEM_AMOUNT")))));
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
}
