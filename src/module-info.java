module library_module {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    requires com.jfoenix;
	requires org.apache.logging.log4j;
	requires java.sql;
	requires java.desktop;
	requires  commons.dbcp2;
 	
	
	requires com.google.gson;
	requires jdk.rmic;
	requires org.apache.commons.codec;
	requires org.controlsfx.controls;
//	requires sun.tools.java;
    
    opens library.assistant.ui.addbook to javafx.fxml;
    opens library.assistant.ui.booklist to javafx.fxml;
    opens library.assistant.ui.addmember to javafx.fxml;
    opens library.assistant.ui.memberlist to javafx.fxml;
    opens library.assistant.ui.main to javafx.fxml;
    opens library.assistant.ui.settings to javafx.fxml;
    opens library.assistant.ui.login to javafx.fxml;
//    exports library.assistant.ui.addbook;
//    exports library.assistant.ui.booklist;
    exports library.assistant.ui.addmember;
    exports library.assistant.ui.memberlist;
    exports library.assistant.ui.main;
    exports library.assistant.ui.settings;
//    exports library.assistant.ui;
}