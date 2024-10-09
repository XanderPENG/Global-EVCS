package github;

//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.BufferedReader;

//import net.sf.json.JSONArray;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.springframework.core.io.ClassPathResource;

class EUCharging {
	public static String jsonFilePath = "";

	public static String readJsonFile(String Filename, String outputFilename) {
		String jsonStr = "";
		System.out.println("��ʼ����" + Filename);
		try {
			// reader
			File jsonFile = new File(Filename);
			FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
			// writer
			File OutputFile = new File(outputFilename);
			FileOutputStream out = null;
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;

			out = new FileOutputStream(OutputFile);
			osw = new OutputStreamWriter(out, "UTF-8");
			bw = new BufferedWriter(osw);
			bw.write(
					"layerId,layerName,displayFieldName,value,OBJECTID,Shape,TARGET_FID,location_unique_id,location_type,"
							+ "location_name,location_address,location_city,location_postal_code,location_country,"
							+ "location_lat,location_lng,location_operator,location_timezone,"
							+ "location_last_updated,location_accessibility,evse_uid,evse_id,evse_status,evse_physical_reference,"
							+ "evse_last_updated,connector_format,connector_standard,connector_amperage,connector_voltage,"
							+ "connector_powertype,connector_power,connector_last_updated,ID,COUNTRY_CODE,MEMBER_STATE,"
							+ "CORE_NETWORK,CORRIDORS,GEO-LENGTH,BUFFER_2,BUFFER_1,Standard_Cat,Power_Cat,"
							+ "geometryType,geometry-x,geometry-y,spatialReference-wkid,"
							+ "spatialReference-latestWkid" + "\r\n");
			int ch = 0;
			StringBuffer sb = new StringBuffer();

			int count = 0;
			while ((ch = reader.read()) != -1) {
				if (count < 12) {
					count += 1;
					continue;// ����ǰ�����ַ�
				}
				count += 1;

				String writeLine = "";
				sb.append((char) ch);
				if (sb.toString().contains("}}},") || sb.toString().contains("}}}]")) {
					String param = sb.toString();
					param = param.substring(0, param.length() - 1);
					JSONObject parse = (JSONObject) JSONObject.parse(param);
					JSONObject features = parse.getJSONObject("attributes");
					JSONObject geometry = parse.getJSONObject("geometry");
					JSONObject spatialReference = geometry.getJSONObject("spatialReference");
					writeLine += parse.get("layerId") + "," + parse.get("layerName") + ","
							+ parse.get("displayFieldName") + "," + parse.get("value").toString().replace(",", " ")
							+ "," + features.get("OBJECTID") + "," + features.get("Shape") + ","
							+ features.get("TARGET_FID") + "," + features.get("location_unique_id") + ","
							+ features.get("location_type") + ","
							+ features.get("location_name").toString().replace(",", " ") + ","
							+ features.get("location_address").toString().replace(",", " ").replace("\r", " ").replace(
									"\n", " ")
							+ "," + features.get("location_city").toString().replace(",", " ") + ","
							+ features.get("location_postal_code") + ","
							+ features.get("location_country").toString().replace(",", " ") + ","
							+ features.get("location_lat") + "," + features.get("location_lng") + ","
							+ features.get("location_operator").toString().replace(",", " ").replace("\r", " ")
									.replace("\n", " ")
							+ "," + features.get("location_timezone") + "," + features.get("location_last_updated")
							+ "," + features.get("location_accessibility") + ","
							+ features.get("evse_uid").toString().replace(",", " ").replace("\r", " ").replace("\n",
									" ")
							+ ","
							+ features.get("evse_id").toString().replace(",", " ").replace("\r", " ").replace("\n", " ")
							+ "," + features.get("evse_status") + "," + features.get("evse_physical_reference") + ","
							+ features.get("evse_last_updated") + "," + features.get("connector_format") + ","
							+ features.get("connector_standard") + "," + features.get("connector_amperage") + ","
							+ features.get("connector_voltage") + "," + features.get("connector_powertype") + ","
							+ features.get("connector_power") + "," + features.get("connector_last_updated") + ","
							+ features.get("ID") + "," + features.get("COUNTRY_CODE") + ","
							+ features.get("MEMBER_STATE") + "," + features.get("CORE_NETWORK") + ","
							+ features.get("CORRIDORS") + "," + features.get("GEO-LENGTH") + ","
							+ features.get("BUFFER_2") + "," + features.get("BUFFER_1") + ","
							+ features.get("Standard_Cat") + "," + features.get("Power_Cat") + ","
							+ parse.get("geometryType") + "," + geometry.get("x") + "," + geometry.get("y") + ","
							+ spatialReference.get("wkid") + "," + spatialReference.get("latestWkid");

					bw.write(writeLine + "\r\n");
					bw.flush();
					sb.delete(0, sb.length());

				}
			}
			bw.close();
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			// System.out.println(jsonStr.getClass().getName());
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<String> readJsonFileToArray(String outputFile) {
		ArrayList<String> staionList = new ArrayList();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < outputFile.length(); i++) {

			if (i < 12) {
				continue;// ����ǰ�����ַ�
			}
			sb.append(outputFile.substring(i, i + 1));
			String writeLine = "";
			if (sb.toString().contains("}}},") || sb.toString().contains("}}}]")) {
				String param = sb.toString();
				param = param.substring(0, param.length() - 1);
				JSONObject parse = (JSONObject) JSONObject.parse(param);
				JSONObject features = parse.getJSONObject("attributes");
				JSONObject geometry = parse.getJSONObject("geometry");
				JSONObject spatialReference = geometry.getJSONObject("spatialReference");
				writeLine += parse.get("layerId") + "," + parse.get("layerName") + "," + parse.get("displayFieldName")
						+ "," + parse.get("value").toString().replace(",", " ") + "," + features.get("OBJECTID") + ","
						+ features.get("Shape") + "," + features.get("TARGET_FID") + ","
						+ features.get("location_unique_id") + "," + features.get("location_type") + ","
						+ features.get("location_name").toString().replace(",", " ") + ","
						+ features.get("location_address").toString().replace(",", " ").replace("\r", " ").replace("\n",
								" ")
						+ "," + features.get("location_city").toString().replace(",", " ") + ","
						+ features.get("location_postal_code") + ","
						+ features.get("location_country").toString().replace(",", " ") + ","
						+ features.get("location_lat") + "," + features.get("location_lng") + ","
						+ features.get("location_operator").toString().replace(",", " ").replace("\r", " ")
								.replace("\n", " ")
						+ "," + features.get("location_timezone") + "," + features.get("location_last_updated") + ","
						+ features.get("location_accessibility") + ","
						+ features.get("evse_uid").toString().replace(",", " ").replace("\r", " ").replace("\n", " ")
						+ ","
						+ features.get("evse_id").toString().replace(",", " ").replace("\r", " ").replace("\n", " ")
						+ "," + features.get("evse_status") + "," + features.get("evse_physical_reference") + ","
						+ features.get("evse_last_updated") + "," + features.get("connector_format") + ","
						+ features.get("connector_standard") + "," + features.get("connector_amperage") + ","
						+ features.get("connector_voltage") + "," + features.get("connector_powertype") + ","
						+ features.get("connector_power") + "," + features.get("connector_last_updated") + ","
						+ features.get("ID") + "," + features.get("COUNTRY_CODE") + "," + features.get("MEMBER_STATE")
						+ "," + features.get("CORE_NETWORK") + "," + features.get("CORRIDORS") + ","
						+ features.get("GEO-LENGTH") + "," + features.get("BUFFER_2") + "," + features.get("BUFFER_1")
						+ "," + features.get("Standard_Cat") + "," + features.get("Power_Cat") + ","
						+ parse.get("geometryType") + "," + geometry.get("x") + "," + geometry.get("y") + ","
						+ spatialReference.get("wkid") + "," + spatialReference.get("latestWkid");
				// System.out.println(writeLine);
				staionList.add(writeLine);


				sb.delete(0, sb.length());
			}
		}

		return staionList;

	}

	public static void main(String[] args) throws IOException {


		/**
		 * ��ȡԭ������
		 * 
		 * �������޸ģ����� �޸�1��������ļ������ļ�·�����Ը�����Ҫ�޸�
		 */
		File OutputFile = new File("C:\\Users\\jiaxing\\eclipse-workspace\\EU\\EU-all.csv");
		// Writter
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		FileReader fileReader = new FileReader(OutputFile);
		DataInputStream in = new DataInputStream(new FileInputStream(OutputFile));
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		// �����ļ�����ʽ������
		out = new FileOutputStream(OutputFile, true);
		osw = new OutputStreamWriter(out, "UTF-8");
		bw = new BufferedWriter(osw);
		ArrayList<String> stationExist = new ArrayList<String>();
		int isFirst = 1;
		String s = null;
		while ((s = br.readLine()) != null) {
			if (isFirst == 1) {
				isFirst = 0;
				// �����ļ�ͷ
				continue;
			}
			String[] sList = s.split(",");
			stationExist.add(sList[4]);
		}
		/**
		 * ������ȡ
		 */
		/*
		 * 
		 *
		 */
		Random r = new Random();

		double x = 0.0;
		double y = 0.0;

		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {

				/**
				 * �������޸ģ�����
				 */
				double before_x = -11.55 + i * 43.55 / 100;// 32
				double before_y = 36.00 + j * 34 / 100;// 70

				x = Double.parseDouble(lonAndLatToWGS84Web(before_x, before_y).split(",")[0]);
				y = Double.parseDouble(lonAndLatToWGS84Web(before_x, before_y).split(",")[1]);

				
				/**
				 * �������޸ģ�����
				 */
				
				double min_x = Double.parseDouble(lonAndLatToWGS84Web(before_x - 2, before_y - 2).split(",")[0]);
				double min_y = Double.parseDouble(lonAndLatToWGS84Web(before_x - 2, before_y - 2).split(",")[1]);
				double max_x = Double.parseDouble(lonAndLatToWGS84Web(before_x + 2, before_y + 2).split(",")[0]);
				double max_y = Double.parseDouble(lonAndLatToWGS84Web(before_x + 2, before_y + 2).split(",")[1]);
				System.out.println("WGS84_x_y:" + x + "," + y);
				String paramEC = "f=json&tolerance=12&returnGeometry=true&returnFieldName=false&returnUnformattedValues=false"
						+ "&imageDisplay=1280%2C688%2C96" + "&geometry=%7B%22x%22%3A" + x// x
						+ "%2C%22y%22%3A" + y// y
						+ "%7D" + "&geometryType=esriGeometryPoint&sr=102100" + "&mapExtent=" 
						+ min_x// X-min
						+ "%2C" + min_y// Y-min
						+ "%2C" + max_x// X-max
						+ "%2C" + max_y// Y-max
						/*
						 * +
						 * "&mapExtent=-11127803.860765422%2C-3429304.368670989%2C12431922.745398639%2C19152028.27544307"
						 */
						+ "&layers=all%3A15";
				jsonFilePath = "EU001/" + i + "-" + j + ".json";
//	        System.out.println("��ȡjson�ı�");
				String resultFromEC = sendGetEC(
						"https://webgate.ec.europa.eu/getis/rest/services/TENTec/tentec_public_services_v4/MapServer/identify",
						paramEC);
				if (resultFromEC.contains("layerId")) {
					createJsonFile(resultFromEC,jsonFilePath);
					readJsonFile(jsonFilePath, jsonFilePath + ".csv");
					// Reader
					File file = new File(jsonFilePath + ".csv");
					fileReader = new FileReader(file);
					in = new DataInputStream(new FileInputStream(file));
					br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					isFirst = 1;
					while ((s = br.readLine()) != null) {

						if (isFirst == 1) {
							isFirst = 0;

							continue;
						}
						// System.out.println(s);
						String[] sList = s.split(",");
						if (sList.length > 40 && !stationExist.contains(sList[4]) && !sList[0].contains("layerId")) {
							System.out.println("����Ӳ�������" + sList[4] + ",from" + file.getName());
							stationExist.add(sList[4]);
							bw.write(s + "\r\n");
							bw.flush();
						}
					}
				} else {
					System.out.println("���ؽ��Ϊ�գ�i="+i+",j="+j+",coord:"+x+","+y);
				}
			}
		}



		bw.close();
		osw.close();
		out.close();
		System.exit(0);
		/**
		 * ���ݺϲ�
		 * ����ƽʱ�����У��������һ�����Ժϲ�����
		 */


		List<File> CSVFile = getCSV("EU001");
		for (int i = 0; i < CSVFile.size(); i++) {
			isFirst = 1;
			File file = CSVFile.get(i);
			if (file.toString().endsWith(".csv")) {
				// Reader
				fileReader = new FileReader(file);
				in = new DataInputStream(new FileInputStream(file));
				br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				while ((s = br.readLine()) != null) {
					if (isFirst == 1) {
						isFirst = 0;
						// �����ļ�ͷ
						continue;
					}
					String[] sList = s.split(",");
					if (sList.length > 40 && !stationExist.contains(sList[4]) && !sList[0].contains("layerId")) {
						System.out.println("����Ӳ�������" + sList[4] + ",from" + file.getName());
						stationExist.add(sList[4]);
						bw.write(s + "\r\n");
						bw.flush();
					}
				}
			}
		}
		bw.close();
		osw.close();
		out.close();

		String paramChargingMap = "client=ocm.app.ionic.8.2.0&verbose=false&output=json&includecomments=true&maxresults=500&"
				+ "compact=true&boundingbox=(49.929677757109744,-2.989878384040651),(53.24319169944968,2.5482302066625095)"
				+ "&key=9bb03e5b-0fb2-4916-9b2b-26c6bd27a56a";// For Open chargingMap Website

	}

	public static String lonAndLatToWGS84Web(double lon, double lat) {

		double x = lon * 20037508.34 / 180;
		double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
		y = y * 20037508.34 / 180;
		return (x + "," + y);

	}

	public static String WGS84WebToLonAndLat(double x, double y) {
		double lon = x / 20037508.34 * 180;
		double lat = y / 20037508.34 * 180;
		lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
		return (lon + "," + lat);

	}

	public static String readJsonFileOpenchargeByFile(String Filename, String outputFilename) {
		String jsonStr = "";
		try {
			File jsonFile = new File(Filename);
			// FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
			// writer
			File OutputFile = new File(outputFilename);
			FileOutputStream out = null;
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;

			// �����ļ�����ʽ������
			out = new FileOutputStream(OutputFile);
			osw = new OutputStreamWriter(out, "UTF-8");
			bw = new BufferedWriter(osw);
			osw.write(
					"IsRecentlyVerified,DateLastVerified,ID,UUID," + "DataProviderID,DataProvidersReference,OperatorID,"
							+ "UsageTypeIDUsageTypeID,AddressInfo-Id,AddressInfo-Title,AddressLine1,Town,"
							+ "Postcode,CountryID,Latitude,Longitude," + "AccessComments,RelatedURL,DistanceUnit,"
							+ "Connections-ID,Connections-ConnectionTypeID,Connections-Reference,"
							+ "Connections-StatusTypeID,Connections-LevelID,Connections-Amps,"
							+ "Connections-Voltage,Connections-PowerKW,Connections-CurrentTypeID,"
							+ "MetadataValues-ID,MetadataValues-MetadataFieldID,MetadataValues-ItemValue,"
							+ "DataQualityLevel,DateCreated,SubmissionStatusTypeID" + "\r\n");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			boolean isNearEnd = false;
			int count = 1;
			while ((ch = reader.read()) != -1) {
				count += 1;
				String writeLine = "";
				sb.append((char) ch);
				if (sb.toString().contains("SubmissionStatusTypeID")) {
					isNearEnd = true;
				}
				if (isNearEnd && ch == ',') {
					isNearEnd = false;
					String param = sb.toString();
					param = param.substring(0, param.length() - 1);
					JSONObject parse = (JSONObject) JSONObject.parse(param);
					JSONObject AddressInfo = parse.getJSONObject("AddressInfo");
					JSONArray Connections = parse.getJSONArray("Connections");
					JSONArray MetadataValues = parse.getJSONArray("MetadataValues");
					for (Object Connection : Connections) {
						String Town = "", AccessComments = "";
						if (AddressInfo.get("AccessComments") != null) {
							AccessComments = AddressInfo.get("AccessComments").toString().replace("\r\n", " ")
									.replace(",", " ");
						}
						if (AddressInfo.get("Town") != null) {
							Town = AddressInfo.get("Town").toString().replace(",", " ");
						}
						JSONObject featureObject = (JSONObject) Connection;
						if (MetadataValues == null) {

							writeLine = parse.get("IsRecentlyVerified") + "," + parse.get("DateLastVerified") + ","
									+ parse.get("ID") + "," + parse.get("UUID") + "," + parse.get("DataProviderID")
									+ "," + parse.get("DataProvidersReference") + "," + parse.get("OperatorID") + ","
									+ parse.get("UsageTypeID") + "," + AddressInfo.get("ID") + ","
									+ AddressInfo.get("Title") + "," + AddressInfo.get("AddressLine1") + "," + Town
									+ "," + AddressInfo.get("Postcode") + "," + AddressInfo.get("CountryID") + ","
									+ AddressInfo.get("Latitude") + "," + AddressInfo.get("Longitude") + ","
									+ AccessComments + "," + AddressInfo.get("RelatedURL") + ","
									+ AddressInfo.get("DistanceUnit") + "," + featureObject.get("ID") + ","
									+ featureObject.get("ConnectionTypeID") + "," + featureObject.get("Reference") + ","
									+ featureObject.get("StatusTypeID") + "," + featureObject.get("LevelID") + ","
									+ featureObject.get("Amps") + "," + featureObject.get("Voltage") + ","
									+ featureObject.get("PowerKW") + "," + featureObject.get("CurrentTypeID") + ","
									+ "null" + "," + "null" + "," + "null" + "," + parse.get("DataQualityLevel") + ","
									+ parse.get("DateCreated") + "," + parse.get("SubmissionStatusTypeID");
							// System.out.println(writeLine);
							bw.write(writeLine + "\r\n");

						} else {
							for (Object MetadataValue : MetadataValues) {
								JSONObject MetadataObject = (JSONObject) MetadataValue;
								writeLine = parse.get("IsRecentlyVerified") + "," + parse.get("DateLastVerified") + ","
										+ parse.get("ID") + "," + parse.get("UUID") + "," + parse.get("DataProviderID")
										+ "," + parse.get("DataProvidersReference") + "," + parse.get("OperatorID")
										+ "," + parse.get("UsageTypeID") + "," + AddressInfo.get("Id") + ","
										+ AddressInfo.get("Title") + "," + AddressInfo.get("AddressLine1") + "," + Town
										+ "," + AddressInfo.get("Postcode") + "," + AddressInfo.get("CountryID") + ","
										+ AddressInfo.get("Latitude") + "," + AddressInfo.get("Longitude") + ","
										+ AccessComments + "," + AddressInfo.get("RelatedURL") + ","
										+ AddressInfo.get("DistanceUnit") + "," + featureObject.get("ID") + ","
										+ featureObject.get("ConnectionTypeID") + "," + featureObject.get("Reference")
										+ "," + featureObject.get("StatusTypeID") + "," + featureObject.get("LevelID")
										+ "," + featureObject.get("Amps") + "," + featureObject.get("Voltage") + ","
										+ featureObject.get("PowerKW") + "," + featureObject.get("CurrentTypeID") + ","
										+ MetadataObject.get("ID") + "," + MetadataObject.get("MetadataFieldID") + ","
										+ MetadataObject.get("ItemValue") + "," + parse.get("DataQualityLevel") + ","
										+ parse.get("DateCreated") + "," + parse.get("SubmissionStatusTypeID");
								// System.out.println(writeLine);
								bw.write(writeLine + "\r\n");
							}
						}

					}

					sb.delete(0, sb.length());
				}
			}
			bw.close();
			reader.close();
			// System.out.println(jsonStr.getClass().getName());
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readJsonFileOpencharge(String jsonStr, String outputFilename) {
		try {
			Reader reader = new InputStreamReader(new FileInputStream(jsonStr), "utf-8");
			// writer
			File OutputFile = new File(outputFilename);
			FileOutputStream out = null;
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;

			// �����ļ�����ʽ������
			out = new FileOutputStream(OutputFile);
			osw = new OutputStreamWriter(out, "UTF-8");
			bw = new BufferedWriter(osw);
			osw.write(
					"layerId,layerName,displayFieldName,value,OBJECTID,Shape,TARGET_FID,location_unique_id,location_type,"
							+ "location_name,location_address,location_city,location_postal_code,location_country,"
							+ "location_lat,location_lng,location_operator,location_timezone,"
							+ "location_last_updated,location_accessibility,evse_uid,evse_id,evse_status,evse_physical_reference,"
							+ "evse_last_updated,connector_format,connector_standard,connector_amperage,connector_voltage,"
							+ "connector_powertype,connector_power,connector_last_updated,ID,COUNTRY_CODE,MEMBER_STATE,"
							+ "CORE_NETWORK,CORRIDORS,GEO-LENGTH,BUFFER_2,BUFFER_1,Standard_Cat,Power_Cat,"
							+ "geometryType,geometry-x,geometry-y,spatialReference-wkid,"
							+ "spatialReference-latestWkid" + "\r\n");
			int ch = 0;
			StringBuffer sb = new StringBuffer();

			int count = 1;
			while ((ch = reader.read()) != -1) {
				count += 1;
				String writeLine = "";
				sb.append((char) ch);
				if (sb.toString().contains("SubmissionStatusTypeID")) {
					System.out.println("SB" + sb);
					String param = sb.toString();
					param = param.substring(0, param.length() - 1);
					JSONObject parse = (JSONObject) JSONObject.parse(param);
					JSONObject features = parse.getJSONObject("attributes");
					JSONObject geometry = parse.getJSONObject("geometry");
					JSONObject spatialReference = geometry.getJSONObject("spatialReference");
					writeLine += parse.get("layerId") + "," + parse.get("layerName") + ","
							+ parse.get("displayFieldName") + "," + parse.get("value").toString().replace(",", " ")
							+ "," + features.get("OBJECTID") + "," + features.get("Shape") + ","
							+ features.get("TARGET_FID") + "," + features.get("location_unique_id") + ","
							+ features.get("location_type") + ","
							+ features.get("location_name").toString().replace(",", " ") + ","
							+ features.get("location_address") + "," + features.get("location_city") + ","
							+ features.get("location_postal_code") + "," + features.get("location_country") + ","
							+ features.get("location_lat") + "," + features.get("location_lng") + ","
							+ features.get("location_operator") + "," + features.get("location_timezone") + ","
							+ features.get("location_last_updated") + "," + features.get("location_accessibility") + ","
							+ features.get("evse_uid") + "," + features.get("evse_id") + ","
							+ features.get("evse_status") + "," + features.get("evse_physical_reference") + ","
							+ features.get("evse_last_updated") + "," + features.get("connector_format") + ","
							+ features.get("connector_standard") + "," + features.get("connector_amperage") + ","
							+ features.get("connector_voltage") + "," + features.get("connector_powertype") + ","
							+ features.get("connector_power") + "," + features.get("connector_last_updated") + ","
							+ features.get("ID") + "," + features.get("COUNTRY_CODE") + ","
							+ features.get("MEMBER_STATE") + "," + features.get("CORE_NETWORK") + ","
							+ features.get("CORRIDORS") + "," + features.get("GEO-LENGTH") + ","
							+ features.get("BUFFER_2") + "," + features.get("BUFFER_1") + ","
							+ features.get("Standard_Cat") + "," + features.get("Power_Cat") + ","
							+ parse.get("geometryType") + "," + geometry.get("x") + "," + geometry.get("y") + ","
							+ spatialReference.get("wkid") + "," + spatialReference.get("latestWkid");

					System.out.println(writeLine);
					bw.write(writeLine + "\r\n");
					sb.delete(0, sb.length());
				}
			}
			System.out.println(sb);
			reader.close();
			jsonStr = sb.toString();
			System.out.println(jsonStr);
			// System.out.println(jsonStr.getClass().getName());
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isValid(String s) {// ����ƥ��һ������
		Stack<Character> st = new Stack<>();
		for (char c : s.toCharArray()) {
			if (c == '(')
				st.push(')');
			else if (c == '[')
				st.push(']');
			else if (c == '{')
				st.push('}');
			else if (st.isEmpty() || c != st.pop())
				return false;
		}
		return st.isEmpty();
	}

	public static boolean createJsonFile(String jsonData, String filePath) {

		// ����ļ������Ƿ�ɹ�
		boolean flag = true;
		// ����json��ʽ�ļ�
		try {
			// ��֤����һ�����ļ�
			File file = new File(filePath);

			if (file.exists()) { // ����Ѵ���,ɾ�����ļ�
				file.delete();
			}
			boolean t = file.createNewFile();

			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonData);
			write.flush();
			write.close();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public static String sendGetEC(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			// System.out.println(urlNameString);
			URL realUrl = new URL(urlNameString);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("Cookie",
					"eu_cookie_consent=%7B%22a%22%3A%7B%22europa%22%3A%5B%22europa-analytics%22%2C%22load-balancers%22%2C%22authentication%22%5D%7D%2C%22r%22%3A%7B%7D%7D; cck1=%7B%22cm%22%3Atrue%2C%22all1st%22%3Atrue%2C%22closed%22%3Afalse%7D; GIS_GetisP_SESSIONID=SERVER_3");
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.124 Safari/537.36 Edg/102.0.1245.44");
			// ����ʵ�ʵ�����
			connection.connect();
			// ���� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if (result.contains("layerId")) {
				return result;
				// createJsonFile(result,jsonFilePath);//˵��������
			}

		} catch (Exception e) {
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {

				result += line;
			}
		} catch (Exception e) {
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ��ָ�� URL ����POST����������
	 * 
	 * @param url   ��������� URL
	 * @param param ����������������Ӧ���� JSONObject.toJSONString(param) ����ʽ��
	 * @return ������Զ����Դ����Ӧ���
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			// �����������
			out.print(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("���� POST ��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPostEC(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			String urlNameString = url + "?" + param;
			System.out.println(urlNameString);
			URL realUrl = new URL(urlNameString);

			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			// �����������
			out.print(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			// createJsonFile(result,"EC0624-1.json");
		} catch (Exception e) {
			System.out.println("���� POST ��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static List<File> getCSV(String DirPath) throws IOException {

		// ��ȡ����ļ���Ŀ¼
		File dirpath = new File(DirPath);
		// ����Ŀ¼�µ������ļ�������һ��File���͵�������
		File[] filelist = dirpath.listFiles();

		// ����һ���յ�file���͵�list���ϣ����ڴ�ŷ����������ļ�
		List<File> newfilelist = new ArrayList<>();
		// ����filelist��������ļ���洢���½���list������
		for (File file : filelist) {
			if (file.isFile()) {
				newfilelist.add(file);
			}
		}
		return newfilelist;


	}

}
