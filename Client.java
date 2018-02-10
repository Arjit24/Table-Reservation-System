import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.*;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private User user;
	private JFrame jFrame = new JFrame();
	private JLabel jfName1;
	private JTextField jlName2;
	private JPanel register2;
	private JLabel loginId;
	private JButton jexit1;
	private JPasswordField password11;
	private JButton register1;
	private JScrollPane scroll;
	private JLabel jPassl;
	private JPasswordField jPass3;
	private JTextField jfName2;
	private JLabel jlName1;
	private JTextField jEmail4;
	private JButton jLogB;
	private JPanel jHomeP;
	private JButton jResTableB;
	private JLabel jexit11;
	private JLabel jScL;
	private JTextArea jUArea;
	private JScrollPane scroll1;
	private JLabel jnewL;
	private JComboBox jbox;
	private JLabel jTime1;
	private JLabel jTime2;
	private Timer jTime3;
	private Timer jTime4;
	private JLabel labelPassword;
	private JTextField emailId1;
	private JButton jUser1;
	private JPanel panel1;
	private JButton loginb;
	private ArrayList<String> bookedTables;
	static private ArrayList<String> reservedTables = new ArrayList<String>();
	int seconds = 60;
	int port = 4568;
	private String ip = "10.4.211.79"; // "10.107.239.93";
	private Socket socket;
	private BufferedWriter buffWriter;
	private BufferedReader buffReader;
	private String split = "::";
	private ArrayList<String> tmpSelectedTables;
	public int tempdate = 0;
	JTable tables[] = new JTable[372];

	public Client() {
		try {
			socket = new Socket(InetAddress.getByName(ip), port);
			buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loginDisplay() {

		jFrame = new JFrame();
		jFrame.getContentPane().removeAll();
		jFrame.setSize(600, 300);
		jFrame.setLocation(700, 300);
		panel1 = new JPanel();
		jFrame.setTitle("Login");
		panel1.setLayout(null);
		loginId = new JLabel("Email Id");
		labelPassword = new JLabel("Password");
		loginb = new JButton("Login");
		register1 = new JButton("Register");
		emailId1 = new JTextField(12);
		emailId1.setText("");
		password11 = new JPasswordField(12);
		password11.setText("");
		emailId1.setBounds(140, 30, 200, 20);
		loginId.setBounds(70, 30, 100, 20);
		loginb.setBounds(140, 100, 90, 20);
		register1.setBounds(140, 140, 90, 20);
		labelPassword.setBounds(70, 65, 100, 20);

		password11.setBounds(140, 65, 200, 20);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		panel1.add(password11);
		panel1.add(labelPassword);
		panel1.add(loginb);
		panel1.add(emailId1);
		panel1.add(register1);
		panel1.add(loginId);

		jFrame.getContentPane().add(panel1);
		jFrame.setVisible(true);

		register1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				registerButtonAction(evt);
			}
		});

		loginb.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loginButtonAction(evt);
			}
		});

	}

	

	private void loginResponse(String response) {

		user = null;
		if (null != response) {
			System.out.println("response:" + response);
			String[] values = response.split(split);
			bookedTables = new ArrayList<String>();
			if (values.length >= 7 && values[1].equals("Success")) {
				user = new User(values[2], values[3], values[4], values[5], values[6]);
				if (values.length > 7) {
					bookedTables = new ArrayList<String>();
					for (String temp : values[7].split(",")) {
						bookedTables.add(temp.trim());
					}
					user.setTables(bookedTables);
					System.out.println("login:" + bookedTables.toString());
				}

				tmpSelectedTables = new ArrayList<String>();
				displayHomeScreen();
			} else if (values[1].equals("Fail")) {
				JOptionPane.showMessageDialog(null, "Invalid Email Id or Password");
				return;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Invalid Email Id or Password");
			return;
		}
	}

	private void loginButtonAction(ActionEvent evt) {
		String x = emailId1.getText().trim();
		String y = new String(password11.getPassword()).trim();
		if (x == null || "".equals(x)) {
			JOptionPane.showMessageDialog(null, "Enter your Email ID --> ");
			emailId1.requestFocus();
			return;
		}
		if (y == null || "".equals(y)) {
			JOptionPane.showMessageDialog(null, "Enter your Password -->");
			password11.requestFocus();
			return;
		}
		StringBuffer sb = new StringBuffer("Login").append(split).append(x).append(split).append(y);
		try {
			buffWriter.write(sb.toString() + "\n");
			buffWriter.flush();
			String response = buffReader.readLine();
			if (null != response && !response.equals("")) {
				String[] values = response.split(split);
				if (values[0].equals("Login")) {
					loginResponse(response);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void registerResponse(String response) {
		user = null;
		if (null != response && !response.equals("")) {
			System.out.println("response:" + response);
			String[] values = response.split(split);
			if (!values[1].equals("Success")) {

				JOptionPane.showMessageDialog(null,
						"This email id already exists.\nPlease Login or use different id to register");
				return;
			} else {

				JOptionPane.showMessageDialog(null,
						"Registration Successfull.\nPlease login to continue with reservation");
				jFrame.getContentPane().removeAll();
				jFrame.dispose();
				loginDisplay();
			}
		}
	}

	private void registerButtonAction(java.awt.event.ActionEvent evt) {
		registerUserDisplay();
	}

	private void registerUserDisplay() {

		jFrame.getContentPane().removeAll();
		jFrame.setSize(600, 300);
		jFrame.setLocation(700, 300);
		jFrame.setTitle("Register User");
		jfName2 = new JTextField(30);
		jfName2.setText("");
		register2 = new JPanel();
		register2.setLayout(null);
		jexit11 = new JLabel("Email Id");
		jfName1 = new JLabel("First Name");
		jlName1 = new JLabel("Last Name");
		jUser1 = new JButton("Register");
		jexit1 = new JButton("Exit");
		jEmail4 = new JTextField(12);
		jEmail4.setText("");
		jlName2 = new JTextField(30);
		jlName2.setText("");

		jPass3 = new JPasswordField(12);
		jPass3.setText("");
		jPassl = new JLabel("Password");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jPassl.setBounds(70, 135, 100, 20);
		jEmail4.setBounds(140, 30, 200, 20);
		jfName2.setBounds(140, 65, 200, 20);
		jlName2.setBounds(140, 100, 200, 20);
		jexit11.setBounds(70, 30, 100, 20);
		jfName1.setBounds(70, 65, 100, 20);
		jlName1.setBounds(70, 100, 100, 20);
		jPass3.setBounds(140, 135, 200, 20);
		jUser1.setBounds(100, 180, 90, 20);
		jexit1.setBounds(250, 180, 90, 20);
		register2.add(jexit11);
		register2.add(jlName2);
		register2.add(jPass3);
		register2.add(jUser1);
		register2.add(jexit1);
		register2.add(jfName1);
		register2.add(jlName1);
		register2.add(jPassl);
		register2.add(jEmail4);
		register2.add(jfName2);
		jFrame.getContentPane().add(register2);
		jFrame.setVisible(true);

		jUser1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				userRegisterButtonAction(evt);
			}

		});

		jexit1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				userExitButtonAction(evt);
			}
		});

	}

	private void userExitButtonAction(ActionEvent evt) {
		jFrame.getContentPane().removeAll();
		jFrame.dispose();
		loginDisplay();

	}

	private void userRegisterButtonAction(ActionEvent evt) {

		String f1 = jfName2.getText().trim();
		String l1 = jlName2.getText().trim();
		String p1 = new String(jPass3.getPassword()).trim();
		String e1 = jEmail4.getText().trim();

		if (e1 == null || "".equals(e1) || e1.length() < 6 || e1.length() > 12) {
			JOptionPane.showMessageDialog(null, "Enter Valid Email Id(6 to 12 Characters) -->");
			jEmail4.requestFocus();
			return;
		}
		if (f1 == null || "".equals(f1)) {
			JOptionPane.showMessageDialog(null, "Enter First Name -->");
			jfName2.requestFocus();
			return;
		}
		if (l1 == null || "".equals(l1)) {
			JOptionPane.showMessageDialog(null, "Enter Last Name -->");
			jlName2.requestFocus();
			return;
		}
		if (p1 == null || "".equals(p1) || p1.length() < 6 || p1.length() > 12) {
			JOptionPane.showMessageDialog(null, "Invalid Password (6 to 12 Characters) --> ");
			jPass3.requestFocus();
			return;
		}

		StringBuffer sb = new StringBuffer("Register").append(split).append(e1).append(split).append(f1).append(split)
				.append(l1).append(split).append(p1);
		try {
			buffWriter.write(sb.toString() + "\n");
			buffWriter.flush();
			String r1 = buffReader.readLine();
			if (null != r1 && !r1.equals("")) {
				String[] values = r1.split(split);
				if (values[0].equals("Register")) {
					registerResponse(r1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class JTable extends JButton {
		private static final long serialVersionUID = 1L;
		boolean isBooked = false;
		boolean isSelected = false;
	}

	private void jTimerRefreshActionPerformed(ActionEvent evt) {

		displayHomeScreen();

	}

	private void modifyReservation(ActionEvent evt) {
		JTable temp = (JTable) evt.getSource();
		temp.isSelected = !temp.isSelected;
		if (temp.isSelected && (bookedTables.size() + tmpSelectedTables.size()) < 3) {
			JOptionPane.showMessageDialog(null, "Table Added" + "\n Reserve in 60 Seconds");

			if (jTime3 == null) {
				jTime3 = new Timer(1000, new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jTimerActionPerformed(evt);
					}
				});
				jTime3.start();
			}
			jTime1.setVisible(true);
			jTime2.setVisible(true);
			jTime2.setText("60");

			seconds = 60;
			temp.setBackground(Color.GREEN);
			if (!tmpSelectedTables.contains(temp.getName())) {
				tmpSelectedTables.add(temp.getName());
			}
			jUArea.setText("");
			jUArea.append("Email ID: ".concat(user.getEmailId()).concat("\n"));
			jUArea.append("First Name: ".concat(user.getfName()).concat("\n"));
			jUArea.append("Last Name: ".concat(user.getlName()).concat("\n"));
			jUArea.append("Reserved Tables: ".concat(
					user.getTables().toString().replace("[", "").replace("]", "").replace("@", " Oct 2017-Table "))
					.concat("\n"));
			jUArea.append("Tables in Cart: ".concat(
					tmpSelectedTables.toString().replace("[", "").replace("]", "").replace("@", " Oct 2017-Table "))
					.concat("\n"));

			StringBuffer sb = new StringBuffer("AddCart").append(split).append(temp.getName());
			try {
				buffWriter.write(sb.toString() + "\n");
				buffWriter.flush();
				String r4 = buffReader.readLine();
				if (null != r4 && !r4.equals("")) {
					String[] values = r4.split(split);
					if (values[0].equals("AddCart") && values[1].equals("Success")) {

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (!temp.isSelected) {
			if (bookedTables.contains(temp.getName())) {
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
						"Table already reserved, select Yes to cancel", "", JOptionPane.YES_NO_OPTION)) {
					StringBuffer sb = new StringBuffer("RemoveTables").append(split).append(user.getEmailId())
							.append(split).append(temp.getName());
					try {
						buffWriter.write(sb.toString() + "\n");
						buffWriter.flush();
						String r5 = buffReader.readLine();
						if (null != r5 && !r5.equals("")) {
							String[] values = r5.split(split);
							if (values[0].equals("RemoveTables") && values[1].equals("Success")) {

								temp.setBackground(null);
								bookedTables.remove(temp.getName());
								user.setTables(bookedTables);
								reservedTables.remove(temp.getName());

							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					temp.isSelected = !temp.isSelected;
					return;
				}

			} else {
				StringBuffer sb = new StringBuffer("RemoveCart").append(split).append(temp.getName());
				try {
					buffWriter.write(sb.toString() + "\n");
					buffWriter.flush();
					String r6 = buffReader.readLine();
					if (null != r6 && !r6.equals("")) {
						String[] values = r6.split(split);
						if (values[0].equals("RemoveCart") && values[1].equals("Success")) {
							tmpSelectedTables.remove(temp.getName());
							temp.setBackground(null);
							if (tmpSelectedTables.size() == 0) {
								if (jTime3 != null) {
									jTime3.stop();
									jTime3 = null;
									jTime1.setVisible(false);
									jTime2.setVisible(false);
									jTime2.setText("60");
								}
							}
							jUArea.setText("");
							jUArea.append("Email ID: ".concat(user.getEmailId()).concat("\n"));
							jUArea.append("First Name: ".concat(user.getfName()).concat("\n"));
							jUArea.append("Last Name: ".concat(user.getlName()).concat("\n"));
							jUArea.append("Reserved Tables: ".concat(user.getTables().toString().replace("[", "")
									.replace("]", "").replace("@", " Oct 2017-Table ")).concat("\n"));
							jUArea.append("Tables in Cart: ".concat(tmpSelectedTables.toString().replace("[", "")
									.replace("]", "").replace("@", " Oct 2017-Table ")).concat("\n"));

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {

			temp.isSelected = false;
			JOptionPane.showMessageDialog(null, "Maximum Reservation Reached");
		}

	}

	private void jTimerActionPerformed(ActionEvent evt) {
		if (seconds > 0) {
			seconds--;
			jTime2.setText(Integer.toString(seconds));
		} else {
			jTime3.stop();
			jTime3 = null;
			StringBuffer sb;
			for (String temp : tmpSelectedTables) {
				sb = new StringBuffer("RemoveCart").append(split).append(temp.trim());
				try {
					buffWriter.write(sb.toString() + "\n");
					buffWriter.flush();
					buffReader.readLine();
				} catch (Exception e) {

				}
			}
			tmpSelectedTables.clear();
			JOptionPane.showMessageDialog(null, "Time Over ~Cart Cleared~ Select Again-->");
			displayHomeScreen();
		}
	}

	private void reserveTableButtonAction(java.awt.event.ActionEvent evt) {
		if (tmpSelectedTables.size() > 0) {
			if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "YES to Reserve Selected Tables", "",
					JOptionPane.YES_NO_OPTION)) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.addAll(tmpSelectedTables);

				StringBuffer sb = new StringBuffer("BookTables").append(split).append(user.getEmailId()).append(split)
						.append(temp.toString().replaceAll("\\[|\\]", ""));
				try {
					buffWriter.write(sb.toString() + "\n");
					buffWriter.flush();
					String r7 = buffReader.readLine();
					if (null != r7 && !r7.equals("")) {
						String[] values = r7.split(split);
						if (values[0].equals("BookTables") && values[1].equals("Fail")) {
							JOptionPane.showMessageDialog(null, "Tables already Reserved.\n Try other tables");
						} else if (values[1].equals("Success")) {
							temp.addAll(bookedTables);
							user.setTables(temp);
							bookedTables = user.getTables();
						}
						tmpSelectedTables.clear();
						if (jTime3 != null) {
							jTime3.stop();
							jTime3 = null;
							jTime1.setVisible(false);
							jTime2.setVisible(false);
							jTime2.setText("60");
						}
						displayHomeScreen();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			JOptionPane.showMessageDialog(null, "Select Tables to Reserve");
		}
	}

	private void jLogoutBActionPerformed(java.awt.event.ActionEvent evt) {
		SwingUtilities.windowForComponent(((JButton) evt.getSource())).dispose();
		if (jTime4 != null) {
			jTime4.stop();
			jTime4 = null;
		}
		if (jTime3 != null) {
			jTime3.stop();
			jTime3 = null;
		}
		StringBuffer sb;
		for (String temp : tmpSelectedTables) {
			sb = new StringBuffer("RemoveCart").append(split).append(temp.trim());
			try {
				buffWriter.write(sb.toString() + "\n");
				buffWriter.flush();
				buffReader.readLine();
			} catch (Exception e) {

			}
		}

		tmpSelectedTables.clear();
		loginDisplay();

	}

	private void setReservedTables(String response) {
		reservedTables = new ArrayList<String>();
		if (response.split(split).length >= 2) {
			ArrayList<String> tables1 = new ArrayList<String>(Arrays.asList(response.split(split)[1].split(",")));
			for (String temp : tables1) {
				reservedTables.add(temp.trim());
			}
		}
	}

	private void setUserTables(String r8) {
		int t = tempdate + 1;
		ArrayList<String> userTables = new ArrayList<String>();
		if (r8.split(split).length > 1) {
			ArrayList<String> userLT = new ArrayList<String>(Arrays.asList(r8.split(split)[1].split(",")));
			for (String temp : userLT) {
				userTables.add(temp.trim());
			}
		}
		bookedTables = new ArrayList<String>(userTables);
		user.setTables(userTables);

	}



	private void displayHomeScreen() {
		Font font = new Font("Times New Roman", Font.BOLD, 18);
		Font font1 = new Font("Times New Roman", Font.BOLD, 14);
		Font font2 = new Font("Times New Roman", Font.ITALIC, 14);
		jFrame.getContentPane().removeAll();
		jFrame.setSize(1450, 1300);
		jFrame.setLocation(0, 0);
		jFrame.setTitle("Table Reservation Screen");
		jHomeP = new JPanel();
		jHomeP.setLayout(null);

		jnewL = new JLabel();
		//jnewL.setText(user.getfName() + ", ");
		jnewL.setFont(font);
		jScL = new JLabel();
		jScL.setText("Pick a Date: ");
		jScL.setFont(font1);

		jLogB = new JButton();
		jLogB.setText("Logout");
		jLogB.setFont(font1);

		jnewL.setBounds(10, 10, 350, 20);
		jScL.setBounds(540, 100, 100, 20);

		jLogB.setBounds(1300, 10, 100, 20);
		if (jTime4 == null) {
			jTime4 = new Timer(5000, new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jTimerRefreshActionPerformed(evt);

				}
			});
			jTime4.setInitialDelay(10000);
			jTime4.start();
		}
		try {

			buffWriter.write("GetUserTables::" + user.getEmailId() + "\n");
			buffWriter.flush();
			String r2 = buffReader.readLine();

			if (null != r2 && !r2.equals("")) {
				String[] values = r2.split(split);
				if (values[0].equals("GetUserTables")) {
					setUserTables(r2);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		jbox = new JComboBox();
		jbox.setBounds(635, 100, 250, 20);
		for (int k = 1; k <= 31; k++) {
			jbox.addItem(k);
		}

		jHomeP.add(jnewL);
		jHomeP.add(jScL);
		jHomeP.add(jLogB);
		int y = 150;

		
		jUArea = new JTextArea();
		jUArea.setFont(font2);
		jUArea.setLineWrap(true);
		jUArea.setWrapStyleWord(true);
		jResTableB = new JButton("Reserve Tables");
		jResTableB.setFont(font1);
		jUArea.append("Email ID: ".concat(user.getEmailId()).concat("\n"));
		jUArea.append("First Name: ".concat(user.getfName()).concat("\n"));
		jUArea.append("Last Name: ".concat(user.getlName()).concat("\n"));
		jUArea.append("\n");
		jUArea.append("Reserved Tables: "
				.concat(user.getTables().toString().replace("[", "").replace("]", "").replace("@", " Oct 2017-Table "))
				.concat("\n"));
		jUArea.append("\n");
		jUArea.append("Tables in Cart: "
				.concat(tmpSelectedTables.toString().replace("[", "").replace("]", "").replace("@", " Oct 2017-Table "))
				.concat("\n"));
		jUArea.setBounds(550, 420, 500, 200);
		jResTableB.setBounds(635, 400, 250, 20);
		jHomeP.add(jUArea);
		jHomeP.add(jResTableB);
		if (jTime3 == null) {
			jTime1 = new JLabel("Seconds remaining");
			jTime2 = new JLabel("60");
			jTime1.setVisible(false);
			jTime2.setVisible(false);

		}
		jTime1.setBounds(1100, 10, 200, 20);
		jTime2.setBounds(1100, 30, 200, 20);
		jHomeP.add(jTime1);
		jHomeP.add(jTime2);
		jResTableB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				reserveTableButtonAction(evt);
			}
		});

		try {

			String temp = "GetReservedTables::";
			if (user.getUserId().equals("adminUser")) {
				temp = temp + "Admin";
			} else {
				temp = temp + "User";
			}
			buffWriter.write(temp + "\n");
			buffWriter.flush();

			String r3 = buffReader.readLine();
			if (null != r3 && !r3.equals("")) {
				String[] values = r3.split(split);
				if (values[0].equals("ReservedTables")) {
					setReservedTables(r3);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		jbox.setSelectedItem(tempdate + 1);
		jbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tempdate = jbox.getSelectedIndex();
				String p = String.valueOf(tempdate + 1);
				String name;
				String[] arr;

				for (int i = 0; i < 372; i++) {
					name = (tables[i].getName());
					arr = name.split("@");
					System.out.println(p + "@" + arr[0] + "@" + arr[1]);

					if (arr[0].equals(p)) {
						tables[i].setVisible(true);
					} else {
						tables[i].setVisible(false);
					}
				}
			}
		});

		int s = 0;
		int u = 0;
		String l = "";
		for (int j = 0; j < 31; j++) {
			int x = 10;
			l = Integer.toString(j + 1) + "@";
			for (int i = 0; i < 12; i++) {
				String label = new String(l + (i + 1));
				tables[s] = new JTable();
				tables[s].setVisible(false);
				tables[s].setText("Table " + (i + 1));
				tables[s].setFont(font1);
				tables[s].setName(label);
				tables[s].setBounds(x, 300, 100, 80);
				x += 120;

				u = u + 1;

				if (user.getTables().contains(label) || tmpSelectedTables.contains(label)) {
					tables[s].setBackground(Color.GREEN);
					tables[s].isSelected = true;

					tables[s].addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							modifyReservation(evt);
						}

					});
				} else if (reservedTables.contains(label)) {
					tables[s].setBackground(Color.RED);
					tables[s].isBooked = true;
				} else {
					tables[s].addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							modifyReservation(evt);
						}

					});
				}

				jHomeP.add(jbox);
				jHomeP.add(tables[s]);
				s++;
			}
			x = 60;
			y += 60;
		}

		tempdate = jbox.getSelectedIndex();
		String p = String.valueOf(tempdate + 1);
		String name;
		String[] arr;
		for (int i = 0; i < 372; i++) {
			name = (tables[i].getName());
			arr = name.split("@");
			if (p.equals(arr[0])) {
				tables[i].setVisible(true);
			} else {
				tables[i].setVisible(false);
			}
		}

		jFrame.add(jHomeP);
		jFrame.setVisible(true);

	

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		jLogB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jLogoutBActionPerformed(evt);
			}
		});

		jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				StringBuffer sb;
				for (String temp : tmpSelectedTables) {
					sb = new StringBuffer("RemoveCart").append(split).append(temp.trim());
					try {
						buffWriter.write(sb.toString() + "\n");
						buffWriter.flush();
						buffReader.readLine();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				tmpSelectedTables.clear();
				JOptionPane.showMessageDialog(null, "Signing Off");
				windowEvent.getWindow().dispose();
				System.exit(0);

			}
		});

	}
	public static void main(String[] args) {

		Client client = new Client();
		client.loginDisplay();

	}
}
