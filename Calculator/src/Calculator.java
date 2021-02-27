import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator extends JFrame {
	private static final long serialVersionUID = 3530753792523670817L;
	static String[] signs = {
			"7",
			"8",
			"9",
			"/",
			"4",
			"5",
			"6",
			"*",
			"1",
			"2",
			"3",
			"-",
			".",
			"0",
			"C",
			"+"
	};
	static String exp = "";
	
	public Calculator() {
		super("Calculator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		JTextField calcField = new JTextField(exp, 10);
		calcField.setEditable(false);
		calcField.setFont(new Font(null, Font.PLAIN, 25));
		
		JPanel calcPanel = new JPanel();
		calcPanel.setLayout(new GridLayout(5, 4));
		
		for (int i = 0; i < signs.length; i++) {
			JButton button = new JButton(signs[i]);
			button.setFont(new Font("Verdana", Font.PLAIN, 16));
			button.addActionListener(e -> {
				if (calcField.getText().contains("="))
					exp = "";
				if (button.getText().matches("[0-9]") || button.getText().equals("."))
					exp += button.getText();
				else if (button.getText().equals("C"))
					exp = "";
				else
					exp += " " + button.getText() + " ";
				calcField.setText(exp);
			});
			calcPanel.add(button);
		}
		
		JButton eqButton = new JButton("=");
		eqButton.addActionListener(e -> {
			ArrayList<String> res = new ArrayList<String>();
			for (String s : exp.split(" ")) {
				res.add(s);
			}
			if (!exp.contains("="))
				exp += " = " + ExpressionParser.calculate(res);
			calcField.setText(exp);
		});
		calcPanel.add(eqButton);
		
		this.getContentPane().add(calcField, BorderLayout.NORTH);
		this.getContentPane().add(calcPanel);
	}
	
	public void start() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		Calculator calc = new Calculator();
		calc.start();
	}
}
