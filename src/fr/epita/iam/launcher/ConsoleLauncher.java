/**
 * 
 */
package fr.epita.iam.launcher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.JDBCIdentityDAO;

/**
 * @author tbrou
 *
 */
public class ConsoleLauncher {
	
	private static JDBCIdentityDAO dao;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		System.out.println("Hello, welcome to the IAM application");
		Scanner scanner = new Scanner(System.in);
		dao = new JDBCIdentityDAO();
		
		
		
		//authentication
		System.out.println("Please enter your login");
		String login = scanner.nextLine();
		System.out.println("Please enter your password");
		String password = scanner.nextLine();
		
		if(!authenticate(login, password)){
			scanner.close();
			return;
		}
		
		// menu
		String answer = menu(scanner);
		
		switch (answer) {
		case "a":
			// creation
			createIdentity(scanner);
			break;
		case "b":
			prat (scanner);
			
			
			break;
		case "c":
			removeidentity (scanner);
			
			break;
		case "d":
			listIdentities();
			break;
		default:
			System.out.println("This option is not recognized ("+ answer + ")");
			break;
		}
		
		scanner.close();

	}

	/**
	 * @throws SQLException 
	 * 
	 */
	private static void listIdentities() throws SQLException {
		System.out.println("This is the list of all identities in the system");
		List<Identity> list = dao.readAll();
		int size = list.size();
		for(int i = 0; i < size; i++){
	
		System.out.println( i+ "." + list.get(i));
		
		}
		
	}
	
	private static void prat(Scanner scanner) throws SQLException {
	System.out.println("please enter the name you want to modify");
	String displayName = scanner.nextLine();
	List<Identity> list = dao.showidentity(displayName);
	System.out.println( list.get(0));
	System.out.println("Please enter the Identity display name");
	
	String displayname = scanner.nextLine();
	System.out.println("Please enter the Identity email");
	
	String email = scanner.nextLine();
	Identity newIdentity = new Identity(list.get(0).getUid(), displayname, email);
	dao.QueueIdentities(newIdentity);
	System.out.println("you succesfully modify this identity :" + newIdentity);
	
	
	}
	
	/**
	 * @param scanner
	 * @throws SQLException 
	 */
	private static void createIdentity(Scanner scanner) throws SQLException {
		System.out.println("You've selected : Identity Creation");
		System.out.println("Please enter the Identity display name");
		String displayName = scanner.nextLine();
		System.out.println("Please enter the Identity email");
		String email = scanner.nextLine();
		Identity newIdentity = new Identity(null, displayName, email);
		dao.writeIdentity(newIdentity);
		System.out.println("you succesfully created this identity :" + newIdentity);
	
	}
	
	
	private static void removeidentity(Scanner scanner) throws SQLException{
		System.out.println("You've selected : Identity Deletion");
		System.out.println("Please enter the Identity display name");
		String displayName = scanner.nextLine();
	
		List<Identity> list = dao.showidentity(displayName);
			
		dao.deleterow(list.get(0));
		
		System.out.println("you have succesfully deleted this identity ");
		
	}
	

	/**
	 * @param scanner
	 * @return
	 */
	private static String menu(Scanner scanner) {
		System.out.println("You're authenticated");
		System.out.println("Here are the actions you can perform :");
		System.out.println("a. Create an Identity");
		System.out.println("b. Modify an Identity");
		System.out.println("c. Delete an Identity");
		System.out.println("d. List Identities");
		System.out.println("e. quit");
		System.out.println("your choice (a|b|c|d|e) ? : ");
		String answer = scanner.nextLine();
		return answer;
	}

	/**
	 * @param login
	 * @param password
	 */
	private static boolean authenticate(String login, String password) {

		// TODO replace this hardcoded check by the real authentication method
		return "prateek".equals(login) && "khajuria".equals(password);
	}

}
