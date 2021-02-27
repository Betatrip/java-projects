import java.math.BigDecimal;
import java.util.ArrayList;

public class ExpressionParser {
	public static String calculate(ArrayList<String> exp) {
		String part = "";
		BigDecimal a = new BigDecimal(exp.get(0));
		BigDecimal b = new BigDecimal(exp.get(2));
		BigDecimal result = new BigDecimal("0");
		String operation = exp.get(1);
		
		if (operation.equals("+"))
			result = a.add(b);
		else if (operation.equals("-"))
			result = a.subtract(b);
		else if (operation.equals("*"))
			result = a.multiply(b);
		else if (operation.equals("/"))
			result = a.divide(b);
		
		if (result.doubleValue() % 1 != 0)
			part = Double.toString(result.doubleValue());
		else
			part = Integer.toString((int) result.doubleValue());
		
		for (int i = 0; i < 3; i++) {
			exp.remove(0);
		}

		exp.add(0, part);
		
		if (exp.size() > 1)
			calculate(exp);
		return exp.get(0);
	}
}
