package OCRread;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import utils.OCRReadUtil;
import utils.QRUtils;

public class OCRGui extends JFrame implements ActionListener {
	// 单张识别，连续识别
	public JPanel jp1, jp2;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5;
	public JButton button1, button2, button3, button4;
	public JTextArea jta1, jta2;
	public JTextField tt1, tt2, tt3, tt4;
	public JFileChooser jfc1, jfc2;
	public JRadioButton jrb1, jrb2;
	public ButtonGroup bg1;
	public QRUtils qru = new QRUtils();
	public BufferedImage bi1, bi2;
	public static int numImage = 0, numImage2 = 0;
	public File file;
	public JProgressBar jpbProcessLoading;
	public String imageToText = null;
	// 滚动条
	public JScrollPane jsp1;
	public SwingWorker<String, String> sw;

	public OCRGui() {
		initTheme();
		init();
	}

	private void initTheme() {
		// 国人牛逼主题，值得学习
		// 初始化字体
		InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 13));
		// 设置本属性将改变窗口边框样式定义
		// 系统默认样式 osLookAndFeelDecorated
		// 强立体半透明 translucencyAppleLike
		// 弱立体感半透明 translucencySmallShadow
		// 普通不透明 generalNoTranslucencyShadow
		BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
		// 设置主题为BeautyEye
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 隐藏“设置”按钮
		UIManager.put("RootPane.setupButtonVisible", false);
		// 开启/关闭窗口在不活动时的半透明效果
		// 设置此开关量为false即表示关闭之，BeautyEye LNF中默认是true
		BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
		// 设置BeantuEye外观下JTabbedPane的左缩进
		// 改变InsetsUIResource参数的值即可实现
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(3, 20, 2, 20));
		//

		// 切换主题，此主题在圆形窗口有标题栏

		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// font
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

	// 主方法
	public static void main(String[] args) {
		// 线程启动截图主程序
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new OCRGui();
			}
		});
	}

	private void init() {
		// 单张解析的面板
		jp1 = new JPanel();
		// 连续识别的面板
		jp2 = new JPanel();
		jp1.setLayout(null);
		jp2.setLayout(null);

		jlb1 = new JLabel("打开图片：");
		jlb1.setBounds(20, 10, 100, 25);
		jp1.add(jlb1);
		tt1 = new JTextField();// TextField 目录的路径
		tt1.setText("");
		tt1.setEditable(false);
		button1 = new JButton("文件夹");// 选择
		jfc1 = new JFileChooser();// 文件选择器
		jfc1.setCurrentDirectory(new File("C:/"));// 文件选择器的初始目录定为c盘
		tt1.setBounds(90, 10, 295, 25);
		button1.setBounds(400, 10, 65, 25);
		button1.addActionListener(this); // 添加事件处理
		jp1.add(tt1);
		jp1.add(button1);
		jp1.add(jfc1);

		jlb4 = new JLabel("选择引擎：");
		jlb4.setBounds(20, 40, 100, 25);
		jp1.add(jlb4);

		jrb1 = new JRadioButton("Tesseract");
		jrb1.setBounds(85, 40, 100, 25);
		jp1.add(jrb1);
		jrb2 = new JRadioButton("BaiDuOCR（需网络 500次/天）");
		jrb2.setBounds(220, 40, 250, 25);
		jrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb2.isSelected()) {
					// 弹窗设置
					BaiDuOCRSet bdos = new BaiDuOCRSet();
				}
			}
		});
		jp1.add(jrb2);
		bg1 = new ButtonGroup();
		bg1.add(jrb1);
		bg1.add(jrb2);
		jrb1.setSelected(true);

		jlb2 = new JLabel();
		jlb2.setBounds(20, 70, 460, 400);
		jp1.add(jlb2);

		// 进度条
		jpbProcessLoading = new JProgressBar();
		jpbProcessLoading.setStringPainted(true); // 呈现字符串
		jpbProcessLoading.setBounds(100, 475, 300, 15);
		jpbProcessLoading.setVisible(false);
		jp1.add(jpbProcessLoading);

		jlb3 = new JLabel("文本：");
		jlb3.setBounds(20, 495, 40, 25);
		jp1.add(jlb3);
		jlb3.setVisible(false);

		jta1 = new JTextArea();
		jsp1 = new JScrollPane(jta1);
		jsp1.setBounds(65, 495, 415, 130);
		jp1.add(jsp1);
		jta1.setEditable(false);
		jta1.setVisible(false);
		jsp1.setVisible(false);

		button2 = new JButton("开始识别");
		button2.setBounds(210, 630, 80, 25);
		button2.addActionListener(this);
		jp1.add(button2);
		button2.setEnabled(false);

		button3 = new JButton("复制到剪切板");
		button3.setBounds(300, 630, 125, 25);
		button3.addActionListener(this);
		jp1.add(button3);
		button3.setVisible(false);

		this.add(jp1);
//		jtp.add("单张图片识别", jp1);
//		jtp.add("连续图片识别", jp2);

		this.setTitle("OCR识别文字");
		this.setSize(500, 700);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("OCR.jpg"));
		this.setIconImage(imageIcon.getImage());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听选择文件夹按钮
		if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
			jfc1.setFileSelectionMode(0);// 设定选择到文件
			jfc1.setFileFilter(new FileCanChoose());
			int state = jfc1.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				file = jfc1.getSelectedFile();// f为选择到的文件
				tt1.setText(file.getAbsolutePath());
				// 缩放图片
//				bi1 = qru.zoomImage(file.getAbsolutePath(), 360, 260);
				bi1 = qru.zoomImage2(file.getAbsolutePath(), 460, 400,null);
				if (bi1 != null) {
					if (numImage != 0) {
						// 移除之前的图片
						jlb2.setText("");
					}
					numImage++;
					jlb2.setIcon(new ImageIcon(bi1));
					button2.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "加载图片失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		// 监听开始识别
		if (e.getSource().equals(button2)) {
			// 设置进度条
			jpbProcessLoading.setVisible(true);
			jpbProcessLoading.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
			jpbProcessLoading.setString("正识别中...请稍等...");
			button2.setEnabled(false);
			// 获取图片路径
			String imagePath = tt1.getText();
			// 获取文件格式
			String imageFormat = imagePath.substring(imagePath.lastIndexOf(".") + 1, imagePath.length());
			// 调用核心识别方法

			// 较复杂，耗时线程swingworker
			sw = new SwingWorker<String, String>() {

				// 此方法处理耗时任务
				@Override
				protected String doInBackground() throws Exception {
					if (jrb1.isSelected()) {
						return new OCRReadUtil().recognizeText(new File(imagePath), imageFormat);
					} else {
						return new BaiDuOCRReadUtil().recognizeText(new File(imagePath));
					}
				}

				// 此方法是耗时任务完成后的操作
				protected void done() {
					try {
						imageToText = get();
					} catch (Exception e) {
						jpbProcessLoading.setVisible(true);
						jpbProcessLoading.setIndeterminate(false);
						jpbProcessLoading.setString("识别失败！请重试！");
						button2.setEnabled(true);
						JOptionPane.showMessageDialog(null, "识别失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
						e.printStackTrace();
						return;
					}
					if (imageToText == null) {
						jpbProcessLoading.setVisible(true);
						jpbProcessLoading.setIndeterminate(false);
						jpbProcessLoading.setString("识别失败！请重试！");
						button2.setEnabled(true);
						JOptionPane.showMessageDialog(null, "识别失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					} else {
						jpbProcessLoading.setVisible(true);
						jpbProcessLoading.setIndeterminate(false);
						jpbProcessLoading.setString("识别完成！");
						button2.setEnabled(true);
						jlb3.setVisible(true);
						jta1.setVisible(true);
						jta1.setEditable(true);
						jsp1.setVisible(true);
						jta1.setText("");
						jta1.setText(imageToText);
						button3.setVisible(true);
					}
				}
			};
			sw.execute();
		}

		// 复制到剪切板
		if (e.getSource().equals(button3)) {
			// 获取剪切板
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable qrtext = new StringSelection(jta1.getText().trim().toString());
			clip.setContents(qrtext, null);
		}
	}

	// 文件过滤器
	class FileCanChoose extends FileFilter {
		public boolean accept(File file) {
			String name = file.getName();
			return (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".bmp")
					|| name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpeg")
					|| file.isDirectory());
		}

		@Override
		public String getDescription() {
			return "图片文件：*.jpg、 *.bmp、 *.png、 *.jpeg";
		}
	}
}
