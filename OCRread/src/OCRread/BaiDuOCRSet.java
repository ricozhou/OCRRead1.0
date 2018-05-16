package OCRread;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.OCRUtils;

//自定义弹窗
public class BaiDuOCRSet extends JDialog implements ActionListener {
	public JTextField appid, apikey, secretkey;
	public JButton button1, button2, button3;
	public JLabel jlp1, jlp2, jlp3, jlp4;
	public OCRUtils ocru = new OCRUtils();

	public BaiDuOCRSet() {
		init();
	}

	private void init() {
		// 读取配置
		BaiDuKey bdy = new BaiDuKey();
		try {
			bdy = ocru.getBaiDuOCRConfig();
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null,
			// "读取配置失败！\n请检查baiduaipconfig.properties是否存在！", "提示消息",
			// JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
//			return;
		}
		this.setLayout(null);
		jlp1 = new JLabel("请   输   入  APP_ID：");
		jlp1.setBounds(20, 20, 150, 25);
		appid = new JTextField();
		appid.setText(bdy.getAPP_ID() != null ? bdy.getAPP_ID() : "");
		appid.setBounds(165, 20, 200, 25);
		jlp2 = new JLabel("请  输  入  API_KEY：");
		jlp2.setBounds(20, 50, 150, 25);
		apikey = new JTextField();
		apikey.setText(bdy.getAPI_KEY() != null ? bdy.getAPI_KEY() : "");
		apikey.setBounds(165, 50, 200, 25);
		jlp3 = new JLabel("请输入SECRET_KEY：");
		jlp3.setBounds(20, 80, 150, 25);
		secretkey = new JTextField();
		secretkey.setText(bdy.getSECRET_KEY() != null ? bdy.getSECRET_KEY() : "");
		secretkey.setBounds(165, 80, 200, 25);

		button1 = new JButton("保存");
		button1.setBounds(30, 140, 70, 25);
		button2 = new JButton("取消");
		button2.setBounds(170, 140, 70, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3 = new JButton("关于");
		button3.setBounds(310, 140, 70, 25);
		button3.addActionListener(this);

		this.add(jlp1);
		this.add(appid);
		this.add(jlp2);
		this.add(apikey);
		this.add(jlp3);
		this.add(secretkey);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("OCR.jpg"));
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setTitle("百度OCR设置");
		this.setSize(410, 240);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听保存按钮
		if (e.getSource().equals(button1)) {
			// 获取参数
			BaiDuKey bdy = getConfigFromJp();
			// 校验参数
			if (!checkParams(bdy)) {
				JOptionPane.showMessageDialog(null, "参数设置格式不正确！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				// 保存到文件
				try {
					ocru.saveToConfigFile(bdy);
					dispose();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "保存配置失败！\n请检查baiduaipconfig.properties是否存在！", "提示消息",
							JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					return;
				}
			}

		}

		// 监听取消按钮
		if (e.getSource().equals(button2)) {
			dispose();
		}
		// 监听关于按钮
		if (e.getSource().equals(button3)) {
			JOptionPane.showMessageDialog(null, "百度OCR每天500次免费！需要网络！\n请到百度开发者中心注册登录并创建应用即可获取接口！", "提示消息",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	// 获取参数
	private BaiDuKey getConfigFromJp() {
		BaiDuKey bdy = new BaiDuKey();
		// 获取参数
		bdy.setAPP_ID(appid.getText().trim());
		bdy.setAPI_KEY(apikey.getText().trim());
		bdy.setSECRET_KEY(secretkey.getText().trim());
		return bdy;
	}

	// 初步校验修改信息
	private boolean checkParams(BaiDuKey bdy) {

		return true;
	}
}
