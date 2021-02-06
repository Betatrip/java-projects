import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Piano {
	static JLabel[] notes = new JLabel[17];
	static int octave = 0;
	static int note = 60;
	static int instrument = 1;
	static int strOctave = 3;
	static String[] strNotes = {"C3",
								"C3#",
								"D3",
								"D3#",
								"E3",
								"F3",
								"F3#",
								"G3",
								"G3#",
								"A3",
								"A3#",
								"B3",
								"C4",
								"C4#",
								"D4",
								"D4#",
								"E4"
								};
	public Piano() {
		JFrame frame = new JFrame("Piano");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(1100, 200);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLayout(new GridLayout(1, 2, 10, 0));
		
		InstrumentPanel leftPanel = new InstrumentPanel();
		leftPanel.setLayout(new GridLayout(1, 2, 10, 10));
		leftPanel.setPreferredSize(new Dimension(200, 200));
		leftPanel.setBackground(Color.WHITE);
		
		
		JButton next = new JButton("next instrument");
		next.addActionListener(e -> {
			instrument += 1;
			if (instrument > 100)
				instrument = 1;
			leftPanel.grabFocus();
		});
		
		JButton prev = new JButton("previous instrument");
		prev.addActionListener(e -> {
			instrument -= 1;
			if (instrument < 1)
				instrument = 1;
			leftPanel.grabFocus();
		});
		
		leftPanel.add(prev);
		leftPanel.add(next);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(500, 220));
		rightPanel.setLayout(new GridLayout(1, 10, 1, 0));
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 10, 0, Color.WHITE));
		rightPanel.setBackground(Color.WHITE);

		for (int i = 0; i < 17; i++) {
			JLabel label = new JLabel();
			label.setOpaque(true);
			label.setBackground(Color.WHITE);
			label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
			label.setText(strNotes[i]);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.BOTTOM);
			switch(i) {
			case 1:
			case 3:
			case 6:
			case 8:
			case 10:
			case 13:
			case 15:
				label.setForeground(Color.LIGHT_GRAY);
				label.setBackground(Color.BLACK);
				label.setBorder(BorderFactory.createMatteBorder(0, 2, 40, 2, Color.WHITE));
			}
			notes[i] = label;
		}
		
		for (int i = 0; i < 17; i++) {
			rightPanel.add(notes[i]);
		}
		
		
		
		
		leftPanel.setFocusable(true);
		leftPanel.addKeyListener(new NoteListener());
		
		frame.getContentPane().add(leftPanel);
		frame.getContentPane().add(rightPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Piano();
	}
}

class InstrumentPanel extends JPanel {
	@Override
	public void paintComponent(Graphics g) {
		this.setBorder(BorderFactory.createTitledBorder("Èíñòðóìåíò ¹ " + Piano.instrument));
		super.paintComponent(g);
	}
}

class NoteListener implements KeyListener {
	int index = 0;
	boolean playing = false;
	int[] keys = {
			  KeyEvent.VK_Q,
			  KeyEvent.VK_2,
			  KeyEvent.VK_W,
			  KeyEvent.VK_3,
			  KeyEvent.VK_E,
			  KeyEvent.VK_R,
			  KeyEvent.VK_5,
			  KeyEvent.VK_T,
			  KeyEvent.VK_6,
			  KeyEvent.VK_Y,
			  KeyEvent.VK_7,
			  KeyEvent.VK_U,
			  KeyEvent.VK_I,
			  KeyEvent.VK_9,
			  KeyEvent.VK_O,
			  KeyEvent.VK_0,
			  KeyEvent.VK_P
			};
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for (int i = 0; i < keys.length; i++) {
			if (key == keys[i]) {
				Piano.notes[i].setBackground(Color.LIGHT_GRAY);
				switch(i) {
				case 1:
				case 3:
				case 6:
				case 8:
				case 10:
				case 13:
				case 15:
					Piano.notes[i].setBackground(Color.DARK_GRAY);
				}
				index = i;
				playing = true;
				}
			}
			if (key == KeyEvent.VK_X) {
				if (Piano.octave < 36)
					Piano.octave += 12;
				Piano.strOctave += 1;
				playing = false;
				for (int i = 0; i < Piano.strNotes.length; i++) {
					char[] c = Piano.strNotes[i].toCharArray();
					int j = Character.getNumericValue(c[1]);
					if (j <= 6)
						j++;
					c[1] = Character.forDigit(j, 10);
					Piano.strNotes[i] = new String(c);
					Piano.notes[i].setText(Piano.strNotes[i]);
				}
			}
			if (key == KeyEvent.VK_Z) {
				if (Piano.octave > -36)
					Piano.octave -= 12;
				Piano.strOctave -= 1;
				playing = false;
				for (int i = 0; i < Piano.strNotes.length; i++) {
					char[] c = Piano.strNotes[i].toCharArray();
					int j = Character.getNumericValue(c[1]);
					if (j > 1)
						j--;
					c[1] = Character.forDigit(j, 10);
					Piano.strNotes[i] = new String(c);
					Piano.notes[i].setText(Piano.strNotes[i]);
				}
			}
		if (playing)
			playNote(Piano.note, index, Piano.octave);
	}
	
	public void playNote(int note, int index, int octave) {
		new Thread(new Runnable() {
			public void run() {
			try {
				 Synthesizer synth = MidiSystem.getSynthesizer();
				 synth.open();		         
				 MidiChannel[] channels = synth.getChannels();
			     channels[0].programChange(Piano.instrument);
			     channels[0].noteOn(note + index + octave, 80);
			     Thread.sleep(500);
			     channels[0].noteOff(65);
			     synth.close();
			     } catch (Exception e1) {
			    	 e1.printStackTrace();
				}
			}
			}).start();
	}
 
	@Override
    public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < this.keys.length; i++) {
			if (key == keys[i]) {
				playing = false;
				Piano.notes[i].setBackground(Color.WHITE);
				switch(i) {
				case 1:
				case 3:
				case 6:
				case 8:
				case 10:
				case 13:
				case 15:
					Piano.notes[i].setBackground(Color.BLACK);
				}
			}
		}
    }
	
	@Override
    public void keyTyped(KeyEvent e) {
    }
}
