import java.util.*;
import java.util.Scanner;
public class BinaryConvert{
	public static void main(String[]args){
		Scanner s = new Scanner(System.in);
		System.out.print("Enter a Decimal to convert to Binary: ");
		int decimal = s.nextInt();
		Stack<Integer> stk= new Stack<Integer>();
		int ph=0;
		while(decimal>0){
			stk.push(decimal%2);
			decimal/=2;
			ph++;
		}
		for(int i = 0; i < stk.size()%4; i++){
			stk.push(0);
		}
		System.out.println("Binary Conversion: "+stk.toString().replace("[","").replace(" ","").replace(",","").replace("]",""));
	}
}