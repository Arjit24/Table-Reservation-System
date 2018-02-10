import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame implements Serializable, Runnable {

	private static final long serialVersionUID = 1L;
	static Map<String, User> usersMap;
	static File inputFile;
	static String file = System.getProperty("user.dir").concat("//dataFile.ser");
	static List<String> serverTemp;
	static int seq;
	private JLabel screenLabel;
	private JLabel comboLabel;
	private String[] userList;
	private JComboBox userListDropdown;
	private JButton logoutButton;
	private JButton resetAllButton;
	private JTextArea textArea;
	private JButton userTables;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private ArrayList<String> reservedTables;
	private JButton refreshButton;
	private User user;
	private ServerSocket serverSock;
	private JLabel password;
	private JFrame frame1;
	private JPanel panel1;
	private JButton loginButton;
	private JTextField emailId1;
	private JPasswordField password1;
	private JPanel panel2;
	private JLabel welcomeLabel;
	JTable tables[] = new JTable[372];

	@SuppressWarnings("unchecked")
	public Server() {

		usersMap = new HashMap<String, User>();
		inputFile = new File(file);
		serverTemp = new ArrayList<String>();
		try {

			if (inputFile.exists()) {

				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(inputFile));
				usersMap = (HashMap<String, User>) objectInputStream.readObject();
				seq = (int) objectInputStream.readObject();
				serverTemp = (ArrayList<String>) objectInputStream.readObject();
				objectInputStream.close();
			} else {
				usersMap.put("admin@uic.edu", new User("admin@uic.edu", "Admin", "Admin", "adminUser", "admin123"));
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(file)));
				objectOutputStream.writeObject(usersMap);
				seq = 123456;
				objectOutputStream.writeObject(seq);
				objectOutputStream.writeObject(serverTemp);
				objectOutputStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) throws IOException {

		Server server = new Server();
		Thread serverThread = new Thread(server);
		serverThread.start();
		server.loginDisplay();
	}

	private void loginDisplay() {

		frame1 = new JFrame();
		panel1 = new javax.swing.JPanel();
		frame1.getContentPane().removeAll();
		frame1.setSize(600, 300);
		frame1.setLocation(700, 300);
		frame1.setTitle("Login");
		panel1.setLayout(null);
		JLabel jLabelId = new javax.swing.JLabel("EmailId");
		password = new javax.swing.JLabel("Password");
		loginButton = new javax.swing.JButton("Login");
		emailId1 = new javax.swing.JTextField(12);
		emailId1.setText("");
		password1 = new javax.swing.JPasswordField(12);
		password1.setText("");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jLabelId.setBounds(70, 30, 100, 20);
		password.setBounds(70, 65, 100, 20);
		emailId1.setBounds(140, 30, 200, 20);
		password1.setBounds(140, 65, 200, 20);
		loginButton.setBounds(140, 100, 90, 20);

		loginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				actionPerformedLogin(evt);
			}
		});

		panel1.add(jLabelId);
		panel1.add(password);
		panel1.add(loginButton);
		panel1.add(emailId1);
		panel1.add(password1);
		frame1.getContentPane().add(panel1);
		frame1.setVisible(true);
	}

	private void actionPerformedLogin(ActionEvent evt) {
		String emailId = emailId1.getText().trim();
		String userPass = new String(password1.getPassword()).trim();
		if (emailId == null || "".equals(emailId)) {
			JOptionPane.showMessageDialog(null, "Enter Valid Email Id!");
			emailId1.requestFocus();
			return;
		}
		if (userPass == null || "".equals(userPass)) {
			JOptionPane.showMessageDialog(null, "Enter Valid Password!");
			password1.requestFocus();
			return;
		}
		if (emailId.equals("admin@uic.edu") && userPass.equals("admin123")) {
			user = usersMap.get(emailId);
			homeScreenDisp();
		} else {
			JOptionPane.showMessageDialog(null, "Invalid! Check User Id/Password");
			return;
		}

	}

	class JTable extends JButton {
		private static final long serialVersionUID = 1L;
		boolean isBooked = false;
		boolean isSelected = false;
	}

	private void homeScreenDisp() {
		Font font = new Font("Times New Roman", Font.BOLD, 18);
		Font font1 = new Font("Times New Roman", Font.BOLD, 14);
		Font font2 = new Font("Times New Roman", Font.ITALIC, 14);

		frame1.getContentPane().removeAll();
		frame1.setSize(1500, 800);
		frame1.setLocation(100, 100);
		frame1.setTitle("Admin Screen");
		panel2 = new javax.swing.JPanel();
		panel2.setLayout(null);

		welcomeLabel = new javax.swing.JLabel();
		screenLabel = new javax.swing.JLabel();

		screenLabel.setText("Please Select Action:");
		welcomeLabel.setText("User Details: ");
		welcomeLabel.setFont(font);

		logoutButton = new javax.swing.JButton();
		logoutButton.setText("Logout");

		welcomeLabel.setBounds(screenSize.width - 700, 70, 350, 20);
		screenLabel.setBounds(100, 100, 200, 20);
		screenLabel.setFont(font);

		logoutButton.setBounds(500, 100, 100, 20);
		logoutButton.setFont(font);

		panel2.add(welcomeLabel);
		panel2.add(screenLabel);
		panel2.add(logoutButton);

		if (user.getUserId().equals("adminUser")) {
			frame1.setSize(screenSize.width - 100, screenSize.height - 100);
			frame1.setLocation(0, 0);
			resetAllButton = new javax.swing.JButton("Reset All Reservations");
			refreshButton = new javax.swing.JButton("Refresh Page");
			textArea = new javax.swing.JTextArea();
			userTables = new javax.swing.JButton("List All User Reservation");
			comboLabel = new javax.swing.JLabel("Select User");

			userList = new String[usersMap.size() + 1];
			userList[0] = "";
			int i = 1;
			for (String key : Server.usersMap.keySet()) {
				userList[i] = key;
				i++;
			}

			userListDropdown = new javax.swing.JComboBox(userList);
			textArea.setBounds(screenSize.width - 700, 100, 400, 600);
			textArea.setFont(font2);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			resetAllButton.setBounds(100, 150, 200, 20);
			resetAllButton.setFont(font1);
			refreshButton.setBounds(100, 200, 200, 20);
			refreshButton.setFont(font1);
			userTables.setBounds(100, 250, 200, 20);
			userTables.setFont(font1);
			comboLabel.setBounds(100, 300, 300, 20);
			comboLabel.setFont(font1);
			userListDropdown.setBounds(180, 300, 190, 20);
			logoutButton.setBounds(screenSize.width - 300, 10, 100, 20);
			logoutButton.setFont(font1);
			panel2.add(resetAllButton);
			panel2.add(textArea);
			panel2.add(refreshButton);
			panel2.add(userTables);
			panel2.add(userListDropdown);
			panel2.add(comboLabel);

			resetAllButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					resetAllAction(evt);
				}
			});
			refreshButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					refreshActionPerformed(evt);
				}
			});

			userTables.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					userListAction(evt);
				}
			});

			userListDropdown.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					userListCActionPerformed(evt);
				}
			});

		}

		reservedTables = new ArrayList<String>();
		for (String key : Server.usersMap.keySet()) {

			for (String table : Server.usersMap.get(key).getTables()) {
				reservedTables.add(table);
			}

		}

		frame1.add(panel2);
		frame1.setVisible(true);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jLogoutButtonAction(evt);
			}
		});

	}

	protected void jLogoutButtonAction(ActionEvent evt) {
		SwingUtilities.windowForComponent(((JButton) evt.getSource())).dispose();
		loginDisplay();

	}

	protected void refreshActionPerformed(ActionEvent evt) {
		homeScreenDisp();

	}

	private void listUserDetail(ActionEvent evt) {
		textArea.setText("");
		JButton tempButton = (JButton) evt.getSource();
		User tempUser = new User();

		for (String key : usersMap.keySet()) {

			for (String table : usersMap.get(key).getTables()) {
				if (table.equals(tempButton.getText())) {
					tempUser = usersMap.get(key);
					textArea.append("Email ID: ".concat(tempUser.getEmailId()).concat("\n"));
					textArea.append("First Name: ".concat(tempUser.getfName()).concat("\n"));
					textArea.append("Last Name: ".concat(tempUser.getlName()).concat("\n"));
					textArea.append("User Id: ".concat(tempUser.getUserId()).concat("\n"));
					textArea.append("Booked Table: "
							.concat(tempUser.getTables().toString().replace("[", "").replace("]", "")).concat("\n"));
					break;
				}

			}

		}

	}

	private void userListAction(ActionEvent evt) {
		textArea.setText("");
		String textFormat = String.format("%1$-20s %2$-20s %3$-20s", "Email Id", "  First Name", "  Tables Selected");
		textArea.append(textFormat.concat("\n"));
		for (String key : usersMap.keySet()) {
			if (!usersMap.get(key).getfName().equals("Admin") && usersMap.get(key).getTables().size() > 0) {
				textFormat = String.format("%1$-20s %2$-20s %3$-20s", usersMap.get(key).getEmailId(),
						"  " + usersMap.get(key).getfName(),
						"  " + usersMap.get(key).getTables().toString().replace("[", "").replace("]", ""));

				textArea.append(textFormat.concat("\n"));
			}

		}

	}

	private void userListCActionPerformed(ActionEvent evt) {

		JComboBox comboBox = (JComboBox) evt.getSource();
		String userMail = (String) comboBox.getSelectedItem();
		User userTemp = usersMap.get(userMail);
		if (null != userTemp) {
			textArea.setText("");
			textArea.append("Email ID: ".concat(userTemp.getEmailId()).concat("\n"));
			textArea.append("First Name: ".concat(userTemp.getfName()).concat("\n"));
			textArea.append("Last Name: ".concat(userTemp.getlName()).concat("\n"));
			textArea.append("User Id: ".concat(userTemp.getUserId()).concat("\n"));
			textArea.append("Booked Tables: ".concat(userTemp.getTables().toString().replace("[", "").replace("]", ""))
					.replace("@", " Oct 2017-Table "));

		}
	}

	private void resetAllAction(ActionEvent evt) {
		for (String key : usersMap.keySet()) {
			usersMap.get(key).setTables(new ArrayList<String>());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(file)));
				oos.writeObject(usersMap);
				seq = 123456;
				oos.writeObject(seq);
				oos.writeObject(serverTemp);
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		homeScreenDisp();

	}

	@Override
	public void run() {
		try {
			this.serverSock = new ServerSocket(4568);

			while (true) {

				Socket client = this.serverSock.accept();
				Thread serverTask = new Thread(new ServerTasks(client, usersMap, file, serverTemp, seq));
				serverTask.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}