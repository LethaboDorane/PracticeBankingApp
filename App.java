//Executes all the code found in the createTables file and myPackage folder
import myPackage.*;

public class App extends createTables {
    
    public static void main(String[] args) {
        //Main methods to create database, tables and introduce main menu of banking app
        createTables.main(args);
        i_startingMenu.main(args);
    }
}
