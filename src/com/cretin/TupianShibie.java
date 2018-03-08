package com.cretin;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

/**
 * <p>
 * Title: TupianShibie
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.cretin.com
 * </p>
 * 
 * @author cretin
 * @date 2018年2月2日
 */
public class TupianShibie extends JFrame {
	private static final long serialVersionUID = 1L;
	// 设置APPID/AK/SK
	public static final String APP_ID = "YOUR APP_ID";
	public static final String API_KEY = "YOUR API_KEY";
	public static final String SECRET_KEY = "YOUR SECRET_KEY";

	// 设置宽高
	private static int mWidth = 1000;
	private static int mHeight = 800;

	// 当前选择的文件
	private String currFilePath = "";

	// 控件
	private JPanel mainPanel;

	public TupianShibie() {
		setTitle("图片识别与文字对比助手");

		// 获取屏幕宽度
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		mWidth = (int) ((int) screensize.getWidth() * 0.9);
		mHeight = (int) ((int) screensize.getHeight() * 0.9);
		setSize(mWidth, mHeight);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		// 标准内容区域
		JLabel label1 = new JLabel("请输入标准内容");
		label1.setBounds(10, 20, 100, 20);
		mainPanel.add(label1);
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);// 设置文本区的换行策略
//		textArea.setText(
//				"新春佳节过后，万象更新，人们饱含着新的希望朝着新的目标，整装待发、再启征程。狗年伊始，网贷行业的发展现状是怎么样的？房融界又有哪些规划呢？2018网贷备案年正式开启，随着监管政策一步一步的落实，各网贷平台加速合规，全力迎接4月份的终极“大考”。同时，在备案关键期，平台对资产资质审核也将越来越严格，投资人将面临继年关过后的又一段“投标荒”时期。\n"
//						+ "		顺利完成备案登记将是房融界接下来工作的重中之重。2018年1月份，房融界已经向前海管理局提交了全部自查资料，并遵循政策的指引，根据律所和会计所出具的建议书加速合规整改，力争今年备案成功。同时，为解决当前投资人抢标困难的情况，房融界已经对接了车贷、消费分期等符合监管规定的优质的资产，并计划于近期上线。进行合规建设的同时，房融界也一直不敢放松对风控体系和用户体验的打造，今年房融界会继续借助科技的力量完善风控、打磨产品，提升平台服务品质。厚积“搏8”，“犬力”以赴。2018，房融界将在合规的路上继续砥砺前行，努力为投资人构建更安全、更可靠的投资环境。");
		JScrollPane jsp0 = new JScrollPane(textArea);
		jsp0.setBounds(116, 10, mWidth - 10 - 120 - 100, 100);
		jsp0.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(jsp0);

		// 两个按钮
		JButton jButton1 = new JButton("打开图片");
		JButton jButton2 = new JButton("开始比较");
		JButton jButton3 = new JButton("清空数据");
		jButton1.setBounds(mWidth - 100, 15, 90, 30);
		jButton2.setBounds(mWidth - 100, 45, 90, 30);
		jButton3.setBounds(mWidth - 100, 75, 90, 30);
		mainPanel.add(jButton1);
		mainPanel.add(jButton2);
		mainPanel.add(jButton3);
		add(mainPanel);

		// 目标标准内容
		JLabel label2 = new JLabel("目标标准内容");
		label2.setBounds(10, 130, 100, 20);
		JTextField jTextField = new JTextField();
		JScrollPane jsp = new JScrollPane(jTextField);
		jsp.setBounds(116, 120, mWidth - 10 - 120, 60);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.getHorizontalScrollBar().setEnabled(false);
		mainPanel.add(label2);
		mainPanel.add(jsp);

		// 识别图片内容
		JLabel label3 = new JLabel("识别图片内容");
		label3.setBounds(10, 200, 100, 20);
		JTextField jTextField1 = new JTextField();
		jTextField1.setEditable(false);
		JScrollPane jsp1 = new JScrollPane(jTextField1);
		jsp1.setBounds(116, 190, mWidth - 10 - 120, 60);
		jsp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(label3);
		mainPanel.add(jsp1);

		// 所选图片预览
		JLabel label4 = new JLabel("所选图片预览");
		label4.setBounds(10, 270, 100, 20);
		mainPanel.add(label4);
		JLabel label = new JLabel();
		JScrollPane jsp2 = new JScrollPane(label);
		int picHeight = mHeight - 260;
		jsp2.setBounds(116, 260, mWidth - 20 - 110, picHeight - 130);
		mainPanel.add(jsp2);

		// 对比结果预览
		JLabel label5 = new JLabel("操作控制台");
		label5.setBounds(10, 270 + picHeight - 130 + 10, 100, 20);
		mainPanel.add(label5);
		JTextArea textArea1 = new JTextArea();
		JScrollPane jsp3 = new JScrollPane(textArea1);
		jsp3.setBounds(116, 260 + picHeight - 130 + 10, mWidth - 20 - 110, mHeight - (260 + picHeight - 130 + 10 + 30));
		jsp3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(jsp3);

		add(mainPanel);

		// 添加事件
		// 选择文件夹
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 检查内容
				String tag = textArea.getText();
				if (tag == null || tag.equals("")) {
					JOptionPane.showMessageDialog(getContentPane(), "请先输入标准内容再操作", "系统信息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "选择文件夹");
				File file = jfc.getSelectedFile();
				if (file != null) {
					textArea1.append("已选择文件：" + file.getAbsolutePath() + "\n");
					ImageIcon img = new ImageIcon(file.getAbsolutePath());// 创建图片对象
					label.setIcon(img);
					currFilePath = file.getAbsolutePath();
				} else {
					textArea1.append("取消选择文件......\n");
				}
			}
		});

		// 开始对比
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currFilePath != "" && !currFilePath.equals("")) {
					String tag = textArea.getText();
					doInt(tag, currFilePath, textArea1, jTextField, jTextField1,jButton2);
				} else {
					JOptionPane.showMessageDialog(getContentPane(), "请先选择图片再操作", "系统信息", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		jButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 清空所有数据
				textArea1.append("清空所有数据，准备进行下一次操作！！！\n");
				jTextField.setText("");
				jTextField1.setText("");
				textArea.setText("");
				//清空图片
				label.setIcon(null);
				currFilePath = "";
				jButton2.setEnabled(true);
			}
		});
		// 对滑动条进行监听
		JScrollBar bar = jsp1.getHorizontalScrollBar();
		bar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				jsp.getHorizontalScrollBar().setValue(e.getValue());
			}
		});
		// 显示
		setVisible(true);
	}

	public static void main(String[] args) {
		new TupianShibie();
	}

	private void doInt(String aimStr, String path, JTextArea textArea1, 
			JTextField jTextField, JTextField jTextField1, JButton jButton2) {
		String aimStr1 = "";
		String content = jTextField.getText();
		if (content == "" || content.equals("")) {
			aimStr1 = formatStr(aimStr);
			jTextField.setText(aimStr1);
		}else {
			aimStr1 = content;
		}
		final String aimStr2 = aimStr1;
		jButton2.setEnabled(false);
		textArea1.append("正在对比，请等待...\n");
		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		new Thread() {
			public void run() {
				// 调用接口
				JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
				JSONArray array = res.getJSONArray("words_result");
				Iterator<Object> it = array.iterator();
				StringBuffer stringBuffer = new StringBuffer();
				while (it.hasNext()) {
					JSONObject ob = (JSONObject) it.next();
					String ss = ob.getString("words");
					stringBuffer.append(ss);
				}
				String endStr = formatStr(stringBuffer.toString());
				jTextField1.setText(endStr);
				textArea1.append("对比完成！！！\n");
				if (endStr.equals(aimStr2)) {
					textArea1.append("对比结果：匹配成功\n");
					JOptionPane.showMessageDialog(getContentPane(), "恭喜，匹配成功", "系统信息", JOptionPane.INFORMATION_MESSAGE);
				} else {
					textArea1.append("对比结果：匹配不成功,请检查\n");
					JOptionPane.showMessageDialog(getContentPane(), "匹配不成功，请注意", "系统信息", JOptionPane.WARNING_MESSAGE);
				}
				jButton2.setEnabled(true);
			};
		}.start();

	}

	// 去除标点符号 去除换行和空格
	private static String formatStr(String aimStr) {
		aimStr = aimStr.trim();
		// 对aimStr 去除标点符号 去除换行和空格
		aimStr = aimStr.replaceAll("\\n", "");
		aimStr = aimStr.replaceAll("\\r", "");
		aimStr = aimStr.replaceAll("\\t", "");
		aimStr = aimStr.replaceAll(",", "");
		aimStr = aimStr.replaceAll("。", "");
		aimStr = aimStr.replaceAll("，", "");
		aimStr = aimStr.replaceAll("、", "");
		aimStr = aimStr.replaceAll("\\.", "");
		aimStr = aimStr.replaceAll("（", "");
		aimStr = aimStr.replaceAll("）", "");
		aimStr = aimStr.replaceAll("\\)", "");
		aimStr = aimStr.replaceAll("\\(", "");
		aimStr = aimStr.replaceAll("!", "");
		aimStr = aimStr.replaceAll("！", "");
		aimStr = aimStr.replaceAll("\\?", "");
		aimStr = aimStr.replaceAll("？", "");
		aimStr = aimStr.replaceAll("“", "");
		aimStr = aimStr.replaceAll("\\:", "");
		aimStr = aimStr.replaceAll("：", "");
		aimStr = aimStr.replaceAll("；", "");
		aimStr = aimStr.replaceAll("\\;", "");
		aimStr = aimStr.replaceAll("”", "");
		aimStr = aimStr.replaceAll("\"", "");
		aimStr = aimStr.replaceAll(" ", "");
		return aimStr;
	}
}
