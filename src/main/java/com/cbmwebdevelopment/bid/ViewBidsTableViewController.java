/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.bid;

import com.cbmwebdevelopment.items.ItemMain;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author cmeehan
 */
public class ViewBidsTableViewController {

    public ObservableList<Bids> data = FXCollections.observableArrayList();

    public void setTableView(TableView tableView){
        TableColumn<Bids, String> idColumn = new TableColumn<>("ID");
        TableColumn<Bids, String> itemNameColumn = new TableColumn<>("Item Name");
        TableColumn<Bids, String> bidderNameColumn = new TableColumn<>("Winner");
        TableColumn<Bids, String> amountColumn = new TableColumn<>("Winning Bid");
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        idColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemNameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.35));
        
        bidderNameColumn.setCellValueFactory(new PropertyValueFactory<>("bidderName"));
        bidderNameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.35));
        
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        
        
        // Set the columns
        tableView.getColumns().setAll(idColumn, itemNameColumn, bidderNameColumn, amountColumn);
        
        // Set the items
        tableView.getItems().setAll(data);
    }
    
    public static class Bids {
        SimpleStringProperty itemId, itemName, bidderName, amount;
        public Bids(String itemId, String itemName, String bidderName, String amount){
            this.itemId = new SimpleStringProperty(itemId);
            this.itemName = new SimpleStringProperty(itemName);
            this.bidderName = new SimpleStringProperty(bidderName);
            this.amount = new SimpleStringProperty(amount);
        }
        
        public void setItemId(String itemId){
            this.itemId.set(itemId);
        }
        
        public String getItemId(){
            return this.itemId.getValue();
        }
        
        public void setItemName(String itemName){
            this.itemId.set(itemName);
        }
        
        public String getItemName(){
            return this.itemName.getValue();
        }
        
        public void setBidderName(String bidderName){
            this.bidderName.set(bidderName);
        }
        
        public String getBidderName(){
            return this.bidderName.getValue();
        }
        
        public void setAmount(String amount){
            this.amount.set(amount);
        }
        
        public String getAmount(){
            return this.amount.getValue();
        }
        
        
    }
}
