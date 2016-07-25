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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import java.awt.List;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class mingan extends JFrame {

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
	private JComboBox comboBox;
	private static mingan frame = new mingan();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				//	mingan frame = new mingan();
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
	public mingan() {
		setResizable(false);
		setTitle("敏感词查找");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1358, 899);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("选择图谱：");
		label.setBounds(46, 20, 108, 21);
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("发现敏感词：");
		label_1.setBounds(66, 347, 108, 21);
		label_1.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(label_1);
		
		buttonFind = new JButton("开始");
		buttonFind.setBounds(145, 615, 108, 53);
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
				String table = null;
				if (comboBox.getSelectedItem().equals("习近平")){
					table = "";
				}
				else if (comboBox.getSelectedItem().equals("彭丽媛")){
					pictureLabel.setIcon(null);
					table="_peng";
				}
				//String outputResult = minganData.tempfindData(findList, table);
				String outputResult = minganData.findData(findList, table);
				textOutput.setText(outputResult);
				int count = 0;
				for(String paragraph : outputResult.split("\n\n\n"))
					count++;
				outputCount.setText("共" + count + "个");
			}
		});
		buttonFind.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(buttonFind);
		
		buttonExport = new JButton("导出");
		buttonExport.setBounds(273, 615, 108, 53);
		buttonExport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.showDialog(new JLabel(), "选择");
				//fileChooser.setName("敏感词发现.xls");
				try {
					minganData.ExportExcel(fileChooser.getSelectedFile().getAbsolutePath(), textOutput.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showInternalMessageDialog(contentPane, "文件导出完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
				
				
				/*写入txt文件
				 * File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				 * try{
				
					FileWriter fileWriter = new FileWriter(file, true);
				BufferedWriter buffer = new BufferedWriter(fileWriter);
				//for (String word : textOutput.getText().split("；"))
				//	buffer.write(word + "\r\n");
				for (String paragraph : textOutput.getText().split("\n\n\n")){
					for (String line : paragraph.split("\n")){
						buffer.write(line);
						buffer.newLine();
					}
					buffer.newLine();
					buffer.newLine();
				}
				buffer.close();
				fileWriter.close();
				}catch(IOException ex){
					ex.printStackTrace();
				}*/
			}
		});
		buttonExport.setFont(new Font("宋体", Font.PLAIN, 15));
		buttonExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(buttonExport);
		
		checkBaidu = new JCheckBox("百度");
		checkBaidu.setBounds(46, 600, 61, 29);
		checkBaidu.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(checkBaidu);
		
		checkWeixin = new JCheckBox("微信");
		checkWeixin.setBounds(46, 634, 61, 29);
		checkWeixin.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(checkWeixin);
		
		checkGoogle = new JCheckBox("谷歌");
		checkGoogle.setBounds(46, 669, 61, 29);
		checkGoogle.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(checkGoogle);
		
		JScrollPane scrollPaneInput = new JScrollPane();
		scrollPaneInput.setBounds(66, 146, 314, 169);
		contentPane.add(scrollPaneInput);
		
		textInput = new JTextArea();
		scrollPaneInput.setViewportView(textInput);
		textInput.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textInput.setLineWrap(true);
		
		JScrollPane scrollPaneOutput = new JScrollPane();
		scrollPaneOutput.setBounds(66, 378, 314, 204);
		scrollPaneOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPaneOutput);
		
		textOutput = new JTextArea();
		scrollPaneOutput.setViewportView(textOutput);
		textOutput.setFont(new Font("Monospaced", Font.PLAIN, 15));
		
		button = new JButton("获取候选词");
		button.setBounds(66, 84, 122, 40);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 15));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//pictureLabel.setText("图谱生成中...");
				Integer number = 0;
				String text = minganData.getKeywords();
				inputCount.setText("共" + text.split("::::")[0] + "个");
				textInput.setText(text.split("::::")[1]);
			}
		});
		contentPane.add(button);
		
		outputCount = new JLabel("");
		outputCount.setBounds(164, 343, 81, 29);
		outputCount.setFont(new Font("宋体", Font.PLAIN, 15));
		contentPane.add(outputCount);
		
		pictureLabel = new JLabel("    ");
		pictureLabel.setBounds(416, 41, 926, 784);
		contentPane.add(pictureLabel);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("宋体", Font.PLAIN, 15));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"习近平"}));
		comboBox.setBounds(164, 18, 108, 29);
		contentPane.add(comboBox);
		
		JButton generateGraph = new JButton("生成图谱");
		generateGraph.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ImageIcon icon = new ImageIcon("kg-xi.png");
				//icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT));
				//pictureLabel.setIcon(icon);
				int w = pictureLabel.getWidth();
				int h = pictureLabel.getHeight();
				Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_FAST);
				pictureLabel.setIcon(new ImageIcon(img));
			}
		});
		generateGraph.setFont(new Font("宋体", Font.PLAIN, 15));
		generateGraph.setBounds(293, 10, 113, 40);
		contentPane.add(generateGraph);
		
		inputCount = new JLabel("");
		inputCount.setFont(new Font("宋体", Font.PLAIN, 15));
		inputCount.setBounds(198, 84, 143, 40);
		contentPane.add(inputCount);
		
	}
}
