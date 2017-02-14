/**
 * 
 */
package fr.epita.iam.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.epita.iam.datamodel.Identity;

/**
 * @author tbrou
 *
 */
public class JDBCIdentityDAO {

	
	
	private Connection connection;
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public JDBCIdentityDAO() throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:derby://localhost:1527/IAM;create=true","PRATEEK","PRATEEK");
		System.out.println(connection.getSchema());
	}
	
	
	public void writeIdentity(Identity identity) throws SQLException {
		String insertStatement = "insert into IDENTITIES (IDENTITY_DISPLAYNAME, IDENTITY_EMAIL, IDENTITY_BIRTHDATE) "
				+ "values(?, ?, ?)";
		PreparedStatement pstmt_insert = connection.prepareStatement(insertStatement);
		pstmt_insert.setString(1, identity.getDisplayName());
		pstmt_insert.setString(2, identity.getEmail());
		Date now = new Date();
		pstmt_insert.setDate(3, new java.sql.Date(now.getTime()));

		pstmt_insert.execute();

	}
	
	public List<Identity> showidentity (String credentials )throws SQLException {
		List<Identity> user = new ArrayList<Identity>();
		PreparedStatement pstmtSelect = connection.prepareStatement("select * from IDENTITIES where IDENTITY_DISPLAYNAME =?");
		pstmtSelect.setString(1, credentials);
		
		ResultSet output = pstmtSelect.executeQuery();
		while (output.next()){
			String showname = output.getString("IDENTITY_DISPLAYNAME");
			String uid = String.valueOf(output.getString("IDENTITY_ID"));
			String email = output.getString("IDENTITY_EMAIL");
			
			Identity identity = new Identity(uid,showname, email);
			user.add(identity);
			
			
		} System.out.println(user);
		return user;
	}
	
	public boolean authentication(String username,String password) throws SQLException {
		String user=null;
		String pwd=null;
		PreparedStatement pstmtSelect = connection.prepareStatement("select LOGIN, PASSWORD from IDENTITIES where LOGIN =?");
		pstmtSelect.setString(1, username);
		
		ResultSet out = pstmtSelect.executeQuery();
		while (out.next()) {
			 user = out.getString("LOGIN");
			 pwd = out.getString("PASSWORD");

		}
		
		
		return username.equals(user) && password.equals(pwd);
		
	}
	
	public void QueueIdentities( Identity modifyname) throws SQLException{
		String modify =  "UPDATE IDENTITIES set IDENTITY_DISPLAYNAME=?, IDENTITY_EMAIL=? WHERE IDENTITY_ID=?";
				PreparedStatement pstmtmodify = connection.prepareStatement(modify);
		pstmtmodify.setString(1, modifyname.getDisplayName());
		pstmtmodify.setString(2, modifyname.getEmail());
		pstmtmodify.setString(3, modifyname.getUid());
		
		pstmtmodify.execute();
		
	}
	
	public void deleterow( Identity renewid) throws SQLException{
		String deleterow = "DELETE from IDENTITIES where IDENTITY_DISPLAYNAME=?";
	PreparedStatement pstmtrm = connection.prepareStatement(deleterow);
	pstmtrm.setString(1, renewid.getEmail());
	pstmtrm.execute();
	pstmtrm.close();
	
	}

	public List<Identity> readAll() throws SQLException {
		List<Identity> identities = new ArrayList<Identity>();

		PreparedStatement pstmt_select = connection.prepareStatement("select * from IDENTITIES");
		ResultSet rs = pstmt_select.executeQuery();
		while (rs.next()) {
			String displayName = rs.getString("IDENTITY_DISPLAYNAME");
			String uid = String.valueOf(rs.getString("IDENTITY_ID"));
			String email = rs.getString("IDENTITY_EMAIL");
			Date birthDate = rs.getDate("IDENTITY_BIRTHDATE");
			Identity identity = new Identity(uid, displayName, email);
			identities.add(identity);
		}
		return identities;

	}

}
