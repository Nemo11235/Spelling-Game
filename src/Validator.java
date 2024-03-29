/* 
  * Brief Description: 
  * This is a validator that varifies players' input of their names. Exception will be thrown if the player:
  * 1. didn't enter anything;
  * 2. entered more or less than 3 letters;
  * 3. entered something other than letters.
  * After informing the exception type, player will be required so enter again until they enter their names with
  *  the correct format.
  */ 

import javax.swing.JOptionPane;
import java.util.regex.Pattern;

public class Validator {
	
	public static String validateUserName(String prompt) {
		boolean isValid = false;
		String name = "";
		
		while(!isValid) {
			try {
				name = JOptionPane.showInputDialog(prompt);
//				System.out.print(prompt);
//				name = sc.next();
				if (name.isEmpty())					//Illegal integer
					throw new ClassCastException();	//Throw the exception
				else if (name.length() != 3)
					throw new StringIndexOutOfBoundsException();
				if(!Pattern.matches("[a-zA-Z]+",name))
					throw new IncompatibleClassChangeError();
				
				isValid = true;
		
			}								//End of try
			catch ( ClassCastException e) {
				JOptionPane.showMessageDialog( null, "\nInput data error! Name Cannot Be Empty...",
				         "Naming Error",
				         JOptionPane.INFORMATION_MESSAGE );
			}							//End of catch
			catch ( StringIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog( null, "\nInput data error! Name Must Contain 3 Characters Only...",
				         "Naming Error",
				         JOptionPane.INFORMATION_MESSAGE );
			}
			catch (IncompatibleClassChangeError e) {
				JOptionPane.showMessageDialog( null, "\nInput data error! Name Can Only Contain Letters...",
				         "Naming Error",
				         JOptionPane.INFORMATION_MESSAGE );
			}
		} 	//End of while
		return name;
	}

}