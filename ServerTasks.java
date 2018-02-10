import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ServerTasks implements Runnable {

	Socket client;
	static Map<String, User> usersMap;
	static List<String> serverTemp;
	static int seq;
	static String file;
	String split = "::";

	@SuppressWarnings("static-access")
	public ServerTasks(Socket client, Map<String, User> usersMap, String file, List<String> serverTemp, int seq) {
		this.client = client;
		this.usersMap = usersMap;
		this.file = file;
		this.serverTemp = serverTemp;
		this.seq = seq;
		
	}

	public ServerTasks() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("client connected");
		try {
			String request = null;
			String response = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			while (true) {
				request = bufferedReader.readLine();
				

				String[] values = request.split("::");
				if (values[0].equals("Login")) {
					response = validateLogin(values);
				} else if (values[0].equals("Register")) {
					response = validateRegisterUser(values);
				} else if (values[0].equals("GetReservedTables")) {
					response = sendReservedTables(values);
				} else if (values[0].equals("GetUserTables")) {
					response = sendUserTables(values);
				} 
				else if (values[0].equals("BookTables")) {
					response = bookTables(values);
				} else if (values[0].equals("RemoveTables")) {
					response = removeTables(values);
				} else if (values[0].equals("AddCart")) {
					response = addTempTables(values);
				} else if (values[0].equals("RemoveCart")) {
					response = removeTempTables(values);
				} else {
					response = "Fail";
				}
				System.out.println("Serverresponse:" + response);
				bufferedWriter.write(response + "\n");
				bufferedWriter.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String removeTempTables(String[] values) {
		System.out.println("Before:"+serverTemp.toString());
		System.out.println("Before1:"+values[1].trim());
		if (serverTemp.contains(values[1].trim())) {
			
			serverTemp.remove(values[1].trim());
			System.out.println("After:"+serverTemp.toString());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Server.file)));
				oos.writeObject(usersMap);
				oos.writeObject(seq);
				oos.writeObject(serverTemp);
				oos.close();
			} catch (Exception e) {

			}
			return "RemoveCart::Success";
		}
		return "RemoveCart::Fail";
	}

	private String addTempTables(String[] values) {
		System.out.println("Before:"+serverTemp.toString());
		System.out.println("Before1:"+values[1].trim());
		if (!serverTemp.contains(values[1].trim())) {
			
			serverTemp.add(values[1].trim());
			System.out.println("After:"+serverTemp.toString());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Server.file)));
				oos.writeObject(usersMap);
				oos.writeObject(seq);
				oos.writeObject(serverTemp);
				oos.close();
			} catch (Exception e) {

			}
			return "AddCart::Success";
		}
		return "AddCart::Fail";
	}

	private String removeTables(String[] values) {
		User user = usersMap.get(values[1]);
		ArrayList<String> tempTables = user.getTables();
		tempTables.remove(values[2].trim());
		user.setTables(tempTables);
		usersMap.put(values[1], user);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Server.file)));
			oos.writeObject(usersMap);
			oos.writeObject(seq);
			oos.writeObject(serverTemp);
			oos.close();
		} catch (Exception e) {

		}
		return "RemoveTables::Success";
	}

	private String bookTables(String[] values) {

		List<String> tempList = new ArrayList<String>();
		for (String key : usersMap.keySet()) {

			for (String Tables : usersMap.get(key).getTables()) {
				tempList.add(Tables.trim());
			}

		}

		List<String> temp = new ArrayList<String>(Arrays.asList(values[2].split(",")));
		for (String Tables : temp) {
			if (tempList.contains(Tables.trim())) {
				return "BookTables::Fail";
			}
		}
		User user = usersMap.get(values[1]);
		ArrayList<String> tempTables = user.getTables();
		for (String temp1 : temp) {
			tempTables.add(temp1.trim());
			serverTemp.remove(temp1.trim());
		}
		user.setTables(tempTables);
		usersMap.put(values[1], user);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Server.file)));
			oos.writeObject(usersMap);
			oos.writeObject(seq);
			oos.writeObject(serverTemp);
			oos.close();
		} catch (Exception e) {

		}
		return "BookTables::Success";
	}

	private String sendReservedTables(String[] values) { 
		List<String> tempList = new ArrayList<String>();
		for (String key : usersMap.keySet()) {

			for (String tables : usersMap.get(key).getTables()) {
				tempList.add(tables);
			}

		}
		if(values.length>=2 && !values[1].equals("Admin")){
			for (String tables : serverTemp) {
				tempList.add(tables.trim());
			}
		}
		StringBuffer sb = new StringBuffer("ReservedTables::").append(tempList.toString().replaceAll("\\[|\\]", ""));
		return sb.toString();
	}

	private String sendUserTables(String[] values) {
		ArrayList<String> userTables = new ArrayList<String>();
		userTables = usersMap.get(values[1].trim()).getTables();

		
		System.out.println(userTables.toString());
		StringBuffer sb = new StringBuffer("GetUserTables::").append(userTables.toString().replaceAll("\\[|\\]", ""));
		return sb.toString();
	}

	private String validateRegisterUser(String[] values) {
		if (usersMap.containsKey(values[1])) {
			return "Register::Fail";
		}
		usersMap.put(values[1], new User(values[1], values[2], values[3], Integer.toString(++Server.seq), values[4]));

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Server.file)));
			oos.writeObject(usersMap);
			oos.writeObject(seq);
			oos.writeObject(serverTemp);
			oos.close();
		} catch (Exception e) {

		}
		return "Register::Success";

	}

	private String validateLogin(String[] req) {
		String res = "Login::Fail";
		User user = usersMap.get(req[1]);
		if (user != null && user.getPassword().equals(req[2])) {

			StringBuffer sb = new StringBuffer("Login::Success").append(split).append(user.getEmailId()).append(split)
					.append(user.getfName()).append(split).append(user.getlName()).append(split)
					.append(user.getUserId()).append(split).append(user.getPassword()).append(split);
			sb.append(user.getTables().toString().replaceAll("\\[|\\]", ""));

			res = sb.toString();
			
		}
		return res;
	}

}
