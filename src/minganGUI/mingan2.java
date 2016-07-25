package minganGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.ScrollPaneConstants;

public class mingan2 extends JFrame {

	private JPanel contentPane;
	private JButton buttonFind;
	private JButton buttonExport;
	private JTextArea textOutput;
	private JTextArea textInput;
	private JCheckBox checkBaidu;
	private JCheckBox checkWeixin;
	private JCheckBox checkGoogle;
	private JButton button;
	private JLabel inputCount;
	private JLabel outputCount;
	private JLabel pictureLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mingan frame = new mingan();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mingan2() {
		setTitle("敏感词查找");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1174, 757);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel label = new JLabel("输入候选词：");
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		sl_contentPane.putConstraint(SpringLayout.NORTH, label, 31, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, label, 61, SpringLayout.WEST, contentPane);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("发现敏感词：");
		sl_contentPane.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, label);
		label_1.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(label_1);
		
		buttonFind = new JButton("查找");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, buttonFind, -90, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonFind, -839, SpringLayout.EAST, contentPane);
		buttonFind.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LinkedList<Boolean> findList = new LinkedList<Boolean>();
				if (checkBaidu.isSelected())
					findList.add(true);
				else findList.add(false);
				if (checkWeixin.isSelected())
					findList.add(true);
				else findList.add(false);
				if (checkGoogle.isSelected())
					findList.add(true);
				else findList.add(false);
				//第二个参数是添加的
				String outputResult = minganData.findData(findList,"");
				textOutput.setText(outputResult.split("::::")[1]);
				outputCount.setText("共" + outputResult.split("::::")[0] + "个");
			}
		});
		buttonFind.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(buttonFind);
		
		buttonExport = new JButton("导出");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, buttonExport, -5, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonExport, -839, SpringLayout.EAST, contentPane);
		buttonExport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.showDialog(new JLabel(), "选择");
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				try{
				FileWriter fileWriter = new FileWriter(file, true);
				BufferedWriter buffer = new BufferedWriter(fileWriter);
				for (String word : textOutput.getText().split("；"))
					buffer.write(word + "\r\n");
				buffer.close();
				fileWriter.close();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		});
		buttonExport.setFont(new Font("宋体", Font.PLAIN, 18));
		buttonExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(buttonExport);
		
		checkBaidu = new JCheckBox("百度");
		sl_contentPane.putConstraint(SpringLayout.NORTH, buttonFind, -16, SpringLayout.NORTH, checkBaidu);
		sl_contentPane.putConstraint(SpringLayout.WEST, buttonFind, 45, SpringLayout.EAST, checkBaidu);
		sl_contentPane.putConstraint(SpringLayout.NORTH, checkBaidu, 570, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, checkBaidu, 0, SpringLayout.WEST, label);
		checkBaidu.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(checkBaidu);
		
		checkWeixin = new JCheckBox("微信");
		sl_contentPane.putConstraint(SpringLayout.NORTH, checkWeixin, 5, SpringLayout.SOUTH, checkBaidu);
		sl_contentPane.putConstraint(SpringLayout.WEST, checkWeixin, 0, SpringLayout.WEST, label);
		checkWeixin.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(checkWeixin);
		
		checkGoogle = new JCheckBox("谷歌");
		sl_contentPane.putConstraint(SpringLayout.NORTH, buttonExport, 0, SpringLayout.NORTH, checkGoogle);
		sl_contentPane.putConstraint(SpringLayout.WEST, buttonExport, 45, SpringLayout.EAST, checkGoogle);
		sl_contentPane.putConstraint(SpringLayout.NORTH, checkGoogle, 6, SpringLayout.SOUTH, checkWeixin);
		sl_contentPane.putConstraint(SpringLayout.EAST, checkGoogle, 0, SpringLayout.EAST, checkBaidu);
		checkGoogle.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(checkGoogle);
		
		JScrollPane scrollPaneInput = new JScrollPane();
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPaneInput, 9, SpringLayout.SOUTH, label);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPaneInput, 61, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPaneInput, -438, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPaneInput, -773, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, label_1, 22, SpringLayout.SOUTH, scrollPaneInput);
		contentPane.add(scrollPaneInput);
		
		JScrollPane scrollPaneOutput = new JScrollPane();
		scrollPaneOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPaneOutput, 20, SpringLayout.SOUTH, label_1);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPaneOutput, -166, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPaneOutput, 61, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPaneOutput, -773, SpringLayout.EAST, contentPane);
		contentPane.add(scrollPaneOutput);
		
		textOutput = new JTextArea();
		scrollPaneOutput.setViewportView(textOutput);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textOutput, -50, SpringLayout.NORTH, buttonExport);
		sl_contentPane.putConstraint(SpringLayout.EAST, textOutput, -265, SpringLayout.EAST, contentPane);
		textOutput.setFont(new Font("Monospaced", Font.PLAIN, 15));
		
		textInput = new JTextArea();
		scrollPaneInput.setViewportView(textInput);
		sl_contentPane.putConstraint(SpringLayout.WEST, textInput, 409, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textInput, -68, SpringLayout.NORTH, buttonFind);
		textInput.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textInput.setLineWrap(true);
		
		button = new JButton("获取候选词");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, button, -51, SpringLayout.NORTH, scrollPaneInput);
		sl_contentPane.putConstraint(SpringLayout.WEST, button, 102, SpringLayout.EAST, label);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, button, -4, SpringLayout.NORTH, scrollPaneInput);
		button.setFont(new Font("宋体", Font.PLAIN, 18));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//pictureLabel.setText("图谱生成中...");
				Integer number = 0;
				String text = minganData.getKeywords();
				textInput.setText(text.split("::::")[1]);
				inputCount.setText("共" + text.split("::::")[0] + "个");
				//label.setText("输入候选词：" + "（共" + text.split("::::")[0]));
				
				ImageIcon icon = new ImageIcon("kg-xi.png");
				//icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT));
				//pictureLabel.setIcon(icon);
				int w = pictureLabel.getWidth();
				int h = pictureLabel.getHeight();
				Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_FAST);
				pictureLabel.setIcon(new ImageIcon(img));
				
				
			}
		});
		contentPane.add(button);
		
		inputCount = new JLabel("  ");
		inputCount.setFont(new Font("宋体", Font.PLAIN, 18));
		sl_contentPane.putConstraint(SpringLayout.WEST, inputCount, 4, SpringLayout.EAST, label);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, inputCount, 0, SpringLayout.SOUTH, label);
		contentPane.add(inputCount);
		
		outputCount = new JLabel("  ");
		outputCount.setFont(new Font("宋体", Font.PLAIN, 18));
		sl_contentPane.putConstraint(SpringLayout.WEST, outputCount, 6, SpringLayout.EAST, label_1);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, outputCount, 0, SpringLayout.SOUTH, label_1);
		contentPane.add(outputCount);
		
		pictureLabel = new JLabel("    ");
		sl_contentPane.putConstraint(SpringLayout.NORTH, pictureLabel, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, pictureLabel, 6, SpringLayout.EAST, button);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, pictureLabel, 704, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, pictureLabel, 736, SpringLayout.EAST, button);
		contentPane.add(pictureLabel);
		
	}
}
