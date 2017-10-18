/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbmwebdevelopment.menus;

import com.cbmwebdevelopment.bidder.BidderMain;
import com.cbmwebdevelopment.items.ItemMain;
import com.cbmwebdevelopment.output.WinningBids;
import com.cbmwebdevelopment.user.UserInformationController;
import com.cbmwebdevelopment.user.UserInformationMain;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;


/**
 *
 * @author cmeehan
 */
public class CustomMenuBar {
    private final SeparatorMenuItem SEPARATOR = new SeparatorMenuItem();
    public Group group;
    
    public MenuBar menuBar(){
        MenuBar menuBar = new MenuBar();
        
        // Create the primary menus
        Menu menuFile = new Menu("File");
        Menu menuData = new Menu("Data");
        Menu menuUsers = new Menu("Users");
        
        // Create the sub menus
        Menu subMenuNew = new Menu("New");
        Menu subMenuExport = new Menu("Export");
        
        
        // Create the menu items
        // File Menu Items
        MenuItem newItem = new MenuItem("Item");
        MenuItem newBidder = new MenuItem("Bidder");
        MenuItem close = new MenuItem("Close");
        
        // Data Menu Items
        MenuItem viewBidders = new MenuItem("View Bidders");
        MenuItem viewItems = new MenuItem("View Items");
        MenuItem bidderList = new MenuItem("Bidder List");
        MenuItem itemList = new MenuItem("Auction Items");
        MenuItem winnerList = new MenuItem("Winners");
        
        // Users Menu Items
        MenuItem newUser = new MenuItem("New");
        MenuItem editUser = new MenuItem("Edit");
        MenuItem viewUsers = new MenuItem("View All");
        
        // Set the item actions
        setActions(newItem, newBidder, close, viewBidders, viewItems, bidderList, itemList, winnerList, newUser, editUser, viewUsers);
        
        
        // Add the sub menus
        subMenuNew.getItems().setAll(newItem, SEPARATOR, newBidder);
        subMenuExport.getItems().setAll(bidderList, itemList, winnerList);
        
        // Add the items to the menus
        menuFile.getItems().addAll(subMenuNew, close);
        menuData.getItems().addAll(viewBidders, viewItems, subMenuExport);
        menuUsers.getItems().addAll(newUser, editUser, viewUsers);
        
        
        // Add the menus
        menuBar.getMenus().addAll(menuFile, menuData, menuUsers);
     
        return menuBar;
    }
    
    /**
     * Menu item order - newItem, newBidder, close, viewBidders, viewItems, 
     *                   bidderList, itemList, winnerList, newUser, editUser, viewUsers
     * @param args 
     */
    private void setActions(MenuItem...args){
        
        // Create a new item
        args[0].setOnAction((event)->{
            ItemMain itemMain = new ItemMain();
            itemMain.isNew = true;
            itemMain.group = group;
            try {
                itemMain.start(new Stage());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        });
        
        // Create a new bidder
        args[1].setOnAction((event)->{
            BidderMain bidder = new BidderMain();
            bidder.isNew = true;
            bidder.group = group;
            try{
                bidder.start(new Stage());
            }catch(IOException ex){
                System.err.println(ex.getMessage());
            }
        });
        
        // Close the application
        args[2].setOnAction((event)->{
            Platform.exit();
        });
        
        // View the bidders in the application
        args[3].setOnAction((event)->{
            
        });
        
        // View the items in the application
        args[4].setOnAction((event)->{
            
        });
        
        // Export a list of the bidders
        args[5].setOnAction((event)->{
            
        });
        
        // Export a list of the items
        args[6].setOnAction((event)->{
            
        });
        
        // Export a list of the winners
        args[7].setOnAction((event)->{
            WinningBids winningBids = new WinningBids();
            winningBids.exportWinningBidsToExcel();
        });
        
        // Create a new user
        args[8].setOnAction((event)->{
            UserInformationMain uMain = new UserInformationMain();
            uMain.newUser = true;
            uMain.group = group;
            try{
                uMain.start(new Stage());
            }catch(IOException ex){
                System.err.println(ex.getMessage());
            }
            
        });
        
        // Edit an existing user
        args[9].setOnAction((event)->{
            
        });
        
        // View a list of all the users
        args[10].setOnAction((event)->{
            
        });
        
        
    }
}
