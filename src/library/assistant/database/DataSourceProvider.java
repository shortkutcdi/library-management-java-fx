package library.assistant.database;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceProvider {
	
	private static BasicDataSource singleDataSource;
	
    private static String URL = "jdbc:mysql://localhost:3308/library_assistant?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=Europe/Paris";
    
    private static String USER = "root";
    private static String PASS = "root";
	
	public static DataSource getSingleDataSourceInstance() {
		if (singleDataSource == null) {
			singleDataSource = new BasicDataSource();
			singleDataSource.setUrl(URL);
			singleDataSource.setUsername(USER);
			singleDataSource.setPassword(PASS);
		}
		return singleDataSource;
	}

}