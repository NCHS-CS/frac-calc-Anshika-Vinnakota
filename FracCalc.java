// Anshika Vinnakota
// Period 6
// Fraction Calculator Project
import java.util.*;

// Description: This program takes the string line input from the user and prints it out in the console.
// If the user types in "quit" then the program will end and the console wil;l print "GoodBye!". 
public class FracCalc {

   // It is best if we have only one console object for input
   public static Scanner console = new Scanner(System.in);
   
   // This main method will loop through user input and then call the
   // correct method to execute the user's request for help, test, or
   // the mathematical operation on fractions. or, quit.   
   public static void main(String[] args) {   
      // initialize to false so that we start our loop
      boolean done = false;
      
      // When the user types in "quit", we are done.
      while (!done) {
         // prompt the user for input
         String input = getInput();
         
         // special case the "quit" command
         if (input.equalsIgnoreCase("quit")) {
            done = true;
         } else if (!UnitTestRunner.processCommand(input, FracCalc::processCommand)) {
        	   // We allowed the UnitTestRunner to handle the command first.
            // If the UnitTestRunner didn't handled the command, process normally.
            String result = processCommand(input);
            
            // print the result of processing the command
            System.out.println(result);
         }
      }
      
      System.out.println("Goodbye!");
      console.close();
   }

   // Prompt the user with a simple, "Enter: " and get the line of input.
   // Return the full line that the user typed in.
   public static String getInput() {      
      System.out.println("Enter: ");
      String input = console.nextLine();
      return input;
   }
   
   // processCommand will process every user command except for "quit".
   // It will return the String that should be printed to the console.
   // Parameter: String input
   public static String processCommand(String input) {
      if (input.equalsIgnoreCase("help")) {
         return provideHelp();
      }
      
      // if the command is not "help", it should be an expression.
      // Of course, this is only if the user is being nice.
      return processExpression(input);
   }
   
   // Parameter: String input
   public static String processExpression(String input) {
      // Determine which operator is being used
      String op = getOperator(input);

      // Separate the expression into two operands
      String first = getFirstOperand(input, op);
      String second = getSecondOperand(input, op);

      // Parse the first operand
      int w1 = getWhole(first);
      int n1 = getNumerator(first);
      int d1 = getDenominator(first);
      
      // Parse the second operand
      int w2 = getWhole(second);
      int n2 = getNumerator(second);
      int d2 = getDenominator(second);

      // Convert both operands to improper fractions
      int improperNum1 = toImproperNumerator(w1, n1, d1);
      int improperNum2 = toImproperNumerator(w2, n2, d2);

      // Declaring variables
      int resultNum = 0;
      int resultDen = 0;

      if (op.equals("+")) {
         resultNum = improperNum1 * d2 + improperNum2 * d1;
         resultDen = d1 * d2;
      }
      else if (op.equals("-")) {
         resultNum = improperNum1 * d2 - improperNum2 * d1;
         resultDen = d1 * d2;
      }
      else if (op.equals("*")) {
         resultNum = improperNum1 * improperNum2;
         resultDen = d1 * d2;
      }
      else if (op.equals("/")) {
         resultNum = improperNum1 * d2;
         resultDen = d1 * improperNum2;
      }

      // Normalize signs so the denominator is always positive
      resultNum = normalizeNumerator(resultNum, resultDen);
      resultDen = normalizeDenominator(resultDen);

      // If the users input of the fractions include a 0 as a denomintor the console log will print some guidance. 
      if (d1 == 0 || d2 ==0)
      {
         String zero = "Please enter a fraction does not have 0 as the denominator!";
         zero += "\nYou cannot divide by zero.";
         return zero;
      }

      // Reduce the fraction using the greatest common divisor
      int gcd = gcd(Math.abs(resultNum), resultDen);
      resultNum /= gcd;
      resultDen /= gcd;
     
      // Convert the final fraction into the correct output format
      return formatResult(resultNum, resultDen);
   }

   // This method determines which operator (+, -, *, /) is in the expression
   public static String getOperator(String input) {
      if (input.indexOf(" + ") != -1) return "+";
      if (input.indexOf(" - ") != -1) return "-";
      if (input.indexOf(" * ") != -1) return "*";
      return "/";
   }

   // This method returns the first operand from the input expression
   // Parameter: String input and String op
   public static String getFirstOperand(String input, String op) {
      return input.substring(0, input.indexOf(op) - 1);
   }

   // This method returns the second operand from the input expression
   // Parameter: String input and String op
   public static String getSecondOperand(String input, String op) {
      return input.substring(input.indexOf(op) + 2);
   }

   // Extracts the whole number portion of an operand
   // Parameter: String operand
   public static int getWhole(String operand) {
      if (operand.indexOf("_") != -1) {
         return Integer.parseInt(operand.substring(0, operand.indexOf("_")));
      }
      if (operand.indexOf("/") == -1) {
         return Integer.parseInt(operand);
      }
      return 0;
   }

   // Extracts the numerator from an operand
   // Parameter: String operand
   public static int getNumerator(String operand) {
      if (operand.indexOf("/") != -1) {
         int slash = operand.indexOf("/");
         if (operand.indexOf("_") != -1) {
            return Integer.parseInt(operand.substring(operand.indexOf("_") + 1, slash));
         }
         return Integer.parseInt(operand.substring(0, slash));
      }
      return 0;
   }

    // Extracts the denominator from an operand
    // Parameter: String operand
   public static int getDenominator(String operand) {
      if (operand.indexOf("/") != -1) {
         return Integer.parseInt(operand.substring(operand.indexOf("/") + 1));
      }
      return 1;
   }

   // Converts a mixed number into an improper numerator
   // Parameter: Int whole, int num, and int den
   public static int toImproperNumerator(int whole, int num, int den) {
      if (whole < 0) {
         return whole * den - num;
      }
      return whole * den + num;
   }

   // Ensures the numerator has the correct sign after normalization
   // Parameter: Int num and int den
   public static int normalizeNumerator(int num, int den) {
      if (num < 0 && den < 0) return -num;
      if (den < 0) return -num;
      return num;
   }

   // Ensures the denominator is always positive
   // Parameter: Int den
   public static int normalizeDenominator(int den) {
      if (den < 0) return -den;
      return den;
   }

   // Finds the greatest common divisor using the Euclidean algorithm or GCD
   // Source: https://youtu.be/JUzYl1TYMcU?si=USkMtT94tJzIJGTA
   // Source: https://youtu.be/yHwneN6zJmU?si=08TUSL-TtauEXUZ9
   public static int gcd(int a, int b) {
      while (b != 0) {
         int temp = b;
         b = a % b;
         a = temp;
      }
      return a;
   }

   // Formats the final result as a whole number, fraction, or mixed number
   // Parameters: Int num and int den
   public static String formatResult(int num, int den) {
      if (num == 0) {
         return "0";
      }

      int whole = num / den;
      int remainder = Math.abs(num % den);

      // If there is no fractional part, return just the whole number
      if (remainder == 0) {
         return "" + whole;
      }

      // If there is no whole number, return just the fraction
      if (whole == 0) {
            if (num < 0) {
            return "-" + remainder + "/" + den;
         }
         return remainder + "/" + den;
      }
      // return a mixed number
      return whole + " " + remainder + "/" + den;
   }
   
   // Returns a string that is helpful to the user about how
   // to use the program. These are instructions to the user.
   public static String provideHelp() {      
      String help = "Enter numbers using spaces and underscores between the operators!\n";
      help += "Here are some examples:\n";
      help += "1/2 + 1/2\n";
      help += "2_2/3 - 1/4\n";
      help += "Type 'quit' to exit the program!";
      return help;
   }
}
