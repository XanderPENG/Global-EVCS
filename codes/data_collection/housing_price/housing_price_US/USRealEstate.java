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
import org.springframework.util.StringUtils;

class USRealEstate {
	public static String jsonFilePath = "";


	public static ArrayList<String> readUSJsonFileToArray(String outputFile) {
		ArrayList<String> staionList = new ArrayList();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < outputFile.length(); i++) {

			sb.append(outputFile.substring(i, i + 1));
			String writeLine = "";
			if (sb.toString().contains("\"mapResults\":[")||sb.toString().contains("\"listResults\":[")
					||sb.toString().contains("\"relaxedResults\":[")) {
				sb.delete(0, sb.length());// start from first result
			}
			
			if (sb.toString().contains(",{\"zpid\"")
					|| (sb.toString().contains("}],") && sb.toString().contains("zpid"))
					|| sb.toString().contains(",{\"buildingId\"")) {

				int minorToJson = 0;
				if (sb.toString().contains("\"carouselPhotos\":[")&&sb.toString().contains("}],")) {
					System.out.println(sb.toString());
					int head = sb.toString().indexOf("\"carouselPhotos");
					if(head>-1) {
						sb.delete(head, sb.length() );		
						continue;
					}
					
				}
				if (sb.toString().contains(",{\"zpid\"")) {
					if (sb.toString().contains("{\"buildingId\"")) {
						sb.delete(0, sb.length() - 7);
						continue;
					}
					minorToJson = 8;
				} else if (sb.toString().contains("}],")) {
					if (sb.toString().contains("{\"buildingId\"")) {
						sb.delete(0, sb.length() - 1);
						continue;
					}
					minorToJson = 2;
				} else if (sb.toString().contains("{\"buildingId\"")) {
					minorToJson = 14;
				} 
				
				
				String param = sb.toString();

				int indexofId=param.toString().indexOf("{\"zpid\"");
				if(indexofId>0) {
					param = param.substring(indexofId,param.length());
				}
				
				param = param.substring(0, param.length() - minorToJson);
				if(param.toString().contains(",{\"price\"")) {
					int endIndex=param.toString().indexOf(",{\"price\"");
					param=param.substring(0,endIndex);
				}
				System.out.println(param);
				JSONObject parse = (JSONObject) JSONObject.parse(param);
				JSONObject variableData = new JSONObject();
				if (String.valueOf(parse.get("variableData")).isEmpty()
						|| String.valueOf(parse.get("variableData")) == "null") {
					variableData.put("type", "null");
					variableData.put("text", "null");

				} else {
					variableData = parse.getJSONObject("variableData");
				}
				JSONObject hdpData = parse.getJSONObject("hdpData");		
				JSONObject homeInfo = new JSONObject();
				JSONObject listing_sub_type = new JSONObject();
				boolean isHomeInfoEmpty=false;
				String datePriceChanged="";
				if(String.valueOf(parse.get("hdpData")).isEmpty()
						|| String.valueOf(parse.get("hdpData")) == "null") {
					isHomeInfoEmpty=true;
				}else {
					homeInfo = hdpData.getJSONObject("homeInfo");
					listing_sub_type = homeInfo.getJSONObject("listing_sub_type");
					if(!String.valueOf(homeInfo.get("datePriceChanged")).isEmpty()
							&& String.valueOf(homeInfo.get("datePriceChanged")) != "null") {
						datePriceChanged=homeInfo.get("datePriceChanged").toString();
					}
				}
				JSONObject latLong = parse.getJSONObject("latLong");
				
				 
				if(isHomeInfoEmpty) {
					writeLine += parse.get("zpid") + "," + String.valueOf(parse.get("price")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
				+ String.valueOf(parse.get("priceLabel")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ parse.get("beds")  + ","
				+String.valueOf(parse.get("baths")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
				+ String.valueOf(parse.get("area")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ latLong.get("latitude") + "," + latLong.get("longitude") + "," + parse.get("statusType") + ","
							+ String.valueOf(parse.get("statusText")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," + parse.get("isFavorite") + ","
							+ parse.get("isUserClaimingOwner") + "," + parse.get("isUserConfirmedClaim") + ","
							+ String.valueOf(parse.get("imgSrc")).replace(",", "").replace("\r", " ").replace("\n"," ") + " " + "," + parse.get("hasImage") + "," + parse.get("visited") + ","
							+ parse.get("listingType") + "," + String.valueOf(variableData.get("type")) + ","
							+ String.valueOf(variableData.get("text")).replace(",", " ").replace("\r", " ").replace("\n",
									" ")
							+ "," + "null" + "," + "null" + "," + "null" + ","
							+ "null" + "," + "null" + "," + "null" + ","
							+ "null" + "," + "null" + "," + "null" + ","
							+ "null" + "," + "null" + "," + "null"+ ","+"null"
							+ "," + "null" + "," +"null" + ","+"null"+ ","+"null"+ ","
							+ "null" + "," + "null" + ","

							+ "null" + "," + "null" + ","
							+ "null"+ "," + "null" + ","
							+ "null" + "," + "null" + ","
							+ "null" + "," + "null" + "," +"null"
							+ "," + "null" + "," + "null" + ","
							+ "null" + ","

							+ parse.get("detailUrl") + "," + parse.get("pgapt") + "," + parse.get("sgapt") + ","
							+ parse.get("has3DModel") + "," + parse.get("hasVideo") + "," + parse.get("isHomeRec") + ","
							+ parse.get("address") + "," + parse.get("hasAdditionalAttributions") + ","
							+ parse.get("isFeaturedListing") + "," + parse.get("availabilityDate");
				}else {
					writeLine += parse.get("zpid") + "," + String.valueOf(parse.get("price")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
				+ String.valueOf(parse.get("priceLabel")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ parse.get("beds") + "," +  String.valueOf(parse.get("baths")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
				+ String.valueOf(parse.get("area")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ latLong.get("latitude") + "," + latLong.get("longitude") + "," + parse.get("statusType") + ","
							+ String.valueOf(parse.get("statusText")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," + parse.get("isFavorite") + ","
							+ parse.get("isUserClaimingOwner") + "," + parse.get("isUserConfirmedClaim") + ","
							+ String.valueOf(parse.get("imgSrc")).replace(",", "").replace("\r", " ").replace("\n"," ") + " " + "," + parse.get("hasImage") + "," + parse.get("visited") + ","
							+ parse.get("listingType") + "," + String.valueOf(variableData.get("type")) + ","
							+ String.valueOf(variableData.get("text")).replace(",", " ").replace("\r", " ").replace("\n",
									" ")
							+ "," + homeInfo.get("zpid") + "," + homeInfo.get("zipcode") + "," + homeInfo.get("city") + ","
							+ homeInfo.get("state") + "," + homeInfo.get("latitude") + "," + homeInfo.get("longitude") + ","
							+ String.valueOf(homeInfo.get("price")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ datePriceChanged + ","
							+ String.valueOf(homeInfo.get("bathrooms")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ String.valueOf(homeInfo.get("bedrooms")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ String.valueOf(homeInfo.get("livingArea")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ String.valueOf(homeInfo.get("homeType")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ homeInfo.get("homeStatus")
							+ "," + homeInfo.get("daysOnZillow") + "," + homeInfo.get("isFeatured") + ","
							+ homeInfo.get("shouldHighlight") + "," + listing_sub_type.get("is_bankOwned") + ","
							+ String.valueOf(homeInfo.get("priceReduction")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ homeInfo.get("isUnmappable") + "," + homeInfo.get("isPreforeclosureAuction") + ","
							+ String.valueOf(homeInfo.get("homeStatusForHDP")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ String.valueOf(homeInfo.get("priceForHDP")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ String.valueOf(homeInfo.get("priceChange")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ homeInfo.get("isNonOwnerOccupied") + "," + homeInfo.get("isPremierBuilder") + ","
							+ homeInfo.get("isZillowOwned") + "," 
							+ String.valueOf(homeInfo.get("currency")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ homeInfo.get("country")+ "," 
							+ String.valueOf(homeInfo.get("taxAssessedValue")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ String.valueOf(homeInfo.get("lotAreaValue")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","
							+ String.valueOf(homeInfo.get("lotAreaUnit")).replace(",", "").replace("\r", " ").replace("\n"," ") + ","

							+ String.valueOf(parse.get("detailUrl")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ parse.get("pgapt") + "," + parse.get("sgapt") + ","
							+ parse.get("has3DModel") + "," + parse.get("hasVideo") + "," + parse.get("isHomeRec") + ","
							+ String.valueOf(parse.get("address")).replace(",", "").replace("\r", " ").replace("\n"," ") + "," 
							+ parse.get("hasAdditionalAttributions") + ","
							+ parse.get("isFeaturedListing") + "," + parse.get("availabilityDate");
				}
			
				staionList.add(writeLine);


				sb.delete(0, sb.length() - minorToJson + 1);
			}
		}

		return staionList;

	}

	

	public static void main(String[] args) throws IOException, InterruptedException {

		/**
		 * ��γ��ת�� test
		 */

		/**
		 * ��ȡԭ������
		 */
		File OutputFile = new File("USRealEstateAll1.csv");
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
			stationExist.add(sList[0]);
		}

		/**
		 * ������ȡ
		 */

		Random r = new Random();

		/**
		 * State
		 * 
		 * Alaska Alabama Arkansas American Samoa Arizona California Colorado
		 * Connecticut Delaware Florida Georgia Guam Hawaii Iowa Idaho Illinois Indiana
		 * Kansas Kentucky Louisiana Massachusetts Maryland Maine Michigan Minnesota
		 * Missouri Northern Mariana Islands Mississippi Montana North Carolina North
		 * Dakota Nebraska New Hampshire New Jersey New Mexico Nevada NewYork Ohio
		 * Oklahoma Oregon Pennsylvania Puerto Rico Rhode Island SouthCarolina SouthDakota 
		 * Tennessee Texas Utah Virginia Virgin Islands Vermont Washington
		 * Washington,DC Wisconsin West Virginia Wyoming
		 * 
		 **/
	//Los Angeles: -118.60 ~ -117.60; 33.7 ~ 34.3
		/**
		 * test ���ݽ�����ȡ
		 */

		int requestId=0;
		int numberThisTime=0;
		String stopAt = "";
		/*
		 * �޸�������к���
		 */
		for(int round=1;round<11;round++) {
			int numberThisRound=0;
			
			for(int roundy=0;roundy<40;roundy++) {
				 
				//���ڴ�ĳһ��ָ�����п�ʼ
//				if(round==49&&roundy==0) {
//					roundy=116;
//				}
				//�������ֿհ�����


				/**
				 * Jiaxing 2023/03/07
				 * 
				 * ��ȡ��������
				 * 1.��һ��
				 * ��west��-117.05000000001 ��-117.05000000001 + 21.8 = -95.25
				 * ��south��32.55310590000001 �� 32.55310590000001 + 16.5 = 49.0531
				 * 
				 * 2.�ڶ���
				 * ��west��-117.05000000001 ��ʼ��ÿ��ѭ����0.1 �� - 124.80�� ��78��
				 * ��south��49.0531 ��ʼ��ÿ��ѭ����0.1����32.55����165��
				 * 
				 * 3.������ - ���ܵ�һ�β�������
				 * ��west��-112.89000000001 �� -117.05000000001 + 21.8 = -95.25 ��177��
				 * ��south��32.55310590000001  �� 32.55310590000001 + 16.5 = 49.0531 ��165��
				 * 
				 * 4.���Ĵ�
				 * ��west��-95.25000000001 �� -95.25000000001 + 14.5 = -80.75 ��145��
				 * ��south��28.97000000000001  �� 28.97000000000001 + 20.1 = 49.0531 ��201��
				 * 
				 * 5.����� ��ȡ���Ҳ�δ������
				 * ��west��-80.75000000001 �� -80.75000000001 + 13.9 = -66.85 ��139��
				 * ��south��32.12000000000001  �� 32.12000000000001 + 15.3 = 47.42 ��153��
				 * 
				 * 6.�����Σ�����ʣ�ಿ��
				 * ��Ϊ���飬
				 * һ�龭�ȷ�Χλ��-95.25000000001 �� -112.89000000001��γ����32.55310590000001����
				 * 
				 * ��ȡ ����-95.25�� -112.89��γ�ȴ�32.55 �� 31.28 ���� x �� 177�Σ�y �� 13��
				 *  �� -105.90 ��ʼ����ȡγ�ȴ�32.55 �� 25.78 ���� �� �� x ���� 69��ʼ ,y �� 68��
				 *  �� -97.14 ��-97.14��25.78����ʼ����-94.99��29.16�� - - 1.57 ������ÿ����1��γ�ȵ���������1.57, y= 1.57* x + 178.2898����157��ʼ
				 * 
				 * �ڶ��飬γ����28.97000000000001���� ��25.08046�����ȷ�ΧΪ-80.75 �� -82.85
				 *  �� -82.85 ��ʼ�� �� -80.75�� �� 21��
				 *  �� 25.08046 ��ʼ �� 28.97�� �� 39��
				 *  ���У��� xΪ0ʱ������24�Σ�xΪ16ʱ����ʼ���������ʵ�xС��16ʱ������ y = -1.5*x + 24 �� 
				 * 
				 * ����1�� 
				 * ����-71.7 �� -70.6���� �� 11��
				 * ��south��41.10000000000001  �� 45.10000000000001  ��40��
				 * 
				 */
		
				double west = -71.70 + round * 0.1;// 
				double east = west+0.101;
				
				double south = 41.12000000000001 + roundy * 0.1;//
				double north = south+0.101;

				requestId+=1;
				if(requestId>300) {
					requestId=1;
				}
				//Url-1
				String dataUrl="https://www.zillow.com/search/GetSearchPageState.htm?searchQueryState=%7B%22"
						+ "usersSearchTerm%22%3A%22New%20York%2C%20NY%22%2C%22mapBounds%22%3A%7B%22west%22%3A"
						+ west//-117.54509639395776
						+"%2C%22east%22%3A"
						+ east//-117.19456386221948
						+"%2C%22south%22%3A"
						+ south//33.879242433771616
						+"%2C%22north%22%3A"
						+ north//34.233935172914336
						+ "%7D%2C%22isMapVisible%22%3Atrue%2C%22filterState%22%3A%7B%22sortSelection%22%3A%7B%22value%22%3A%22"
						+ "days%22%7D%2C%22isAllHomes%22%3A%7B%22value%22%3Atrue%7D%7D%2C%22isListVisible%22%3Atrue%2C%22"
						+ "mapZoom%22%3A15%7D&wants={%22cat1%22:[%22listResults%22,%22mapResults%22],%22cat2%22:[%22total%22],%22regionResults%22:[%22regionResults%22]}&requestId="
						+ requestId;

				System.out.println("round:"+round+",roundy:"+roundy+",west:"+west+",south:"+south);
				String resultUS = sendGetUSRealEstate(dataUrl);
				if (resultUS.contains("zpid")) {// ˵��������
					if(!stopAt.equals("")) {
						stopAt="";
					}
					System.out.println("�¶�ȡ..");
					ArrayList<String> listFromUS = readUSJsonFileToArray(resultUS);
					int numberThisRoundy=0;
					for (int i = 0; i < listFromUS.size(); i++) {
						if (isFirst == 1) {
							isFirst = 0;
							// �����ļ�ͷ
							continue;
						}
						// System.out.println(s);
						String[] sList = listFromUS.get(i).split(",");
						// System.out.println(sList[4]+","+sList[0]);
						if (!stationExist.contains(sList[0])) {
							stationExist.add(sList[0]);
							bw.write(listFromUS.get(i) + "\r\n");
							numberThisRoundy+=1;
							numberThisTime+=1;
							numberThisRound+=1;
						} else {
						}
						bw.flush();
						
					}
					System.out.println("��һroundy�����"+numberThisRoundy+"�������ݣ���һround����ӣ�"+numberThisRound+"�����ݣ��ܹ�����ˣ�"+numberThisTime+"������");
					stopAt=" StopAt:Round:"+round+",Roundy:"+roundy;
				}
				else {
					
					System.out.println("round:"+round+",roundy:"+roundy+",������. "+"��һround����ӣ�"+numberThisRound+"�����ݣ��ܹ�����ˣ�"+numberThisTime+"������"+stopAt);
				}

				Thread.sleep(3000);
				if(round%10==0) {
					Thread.sleep(10000);
				}
			}
			System.out.println("End of round"+round);
			Thread.sleep(20000);
		}
		System.out.println("������г������"+numberThisTime+"��������");

		bw.close();
		osw.close();
		out.close();
		/**
		 * ���ݺϲ�
		 */


	}

	public static String sendGetUSRealEstate(String url) {
		String result = "";
		BufferedReader in = null;
		System.out.println("Start request data:"+url);
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();

			connection.addRequestProperty("Cookie",
					"x-amz-continuous-deployment-state=AYABeII4vijqDZRjUotH9Pqon6EAPgACAAFEAB1kM2Jsa2Q0azB3azlvai5jbG91ZGZyb250Lm5ldAABRwAVRzAxNTk1MzcxVEJNNTJaWDdPU09PAAEAAkNEABpDb29raWUAAACAAAAADPe/DPm74Clm5iWURwAw4xey/aBOoprLTZ0EIYPydFzJZQ2d5KtMKEgK5hAEY00ptVFk/MLRPotRfutT2ulmAgAAAAAMAAQAAAAAAAAAAAAAAAAAAFS07UeWA+WZFZ/v2ndN8rr/////AAAAAQAAAAAAAAAAAAAAAQAAAAxIVDxyz6HBk10940TAVIr00dcpZ8bpH3MUVWHG; AWSALB=U1L5Yegn0va0eiP2VvrBQYvwemdz50lUPqDyrdddSFORT2Hoa1t+wFGkTET4W7qZ2H20Velov5VevNklMO0xwTvIm81wOr+57W/aTywMNDfYrpWAmSewQCfOTUv/; AWSALBCORS=U1L5Yegn0va0eiP2VvrBQYvwemdz50lUPqDyrdddSFORT2Hoa1t+wFGkTET4W7qZ2H20Velov5VevNklMO0xwTvIm81wOr+57W/aTywMNDfYrpWAmSewQCfOTUv/; JSESSIONID=67F9687385C0701B39DEF834862F568C; search=6|1684805975021|rect=32.22100000000001%2C-80.64900000001%2C32.12000000000001%2C-80.75000000001&disp=map&mdm=auto&p=1&sort=days&z=1&listPriceActive=1&fs=1&fr=0&mmm=0&rs=0&ah=0&singlestory=0&housing-connector=0&abo=0&garage=0&pool=0&ac=0&waterfront=0&finished=0&unfinished=0&cityview=0&mountainview=0&parkview=0&waterview=0&hoadata=1&zillow-owned=0&3dhome=0&featuredMultiFamilyBuilding=0&commuteMode=driving&commuteTimeOfDay=now								; zguid=24|$2988b544-5fae-43bd-8a79-dd9210ec3c38; zgsession=1|d3953133-10fe-44c0-ba11-257c17dadd49; x-amz-continuous-deployment-state=AYABeKlL0F5+AvWgkzkMCaVqZjEAPgACAAFEAB1kM2Jsa2Q0azB3azlvai5jbG91ZGZyb250Lm5ldAABRwAVRzAxNTk1MzcxVEJNNTJaWDdPU09PAAEAAkNEABpDb29raWUAAACAAAAADJhAGAuaq1fKNrAT0QAwZ1Tr6+A69MmRTvMmmMedv3+RMKBlbN5SYgf5yPg27PVPa/x9I1Znthc75stTuAx6AgAAAAAMAAQAAAAAAAAAAAAAAAAAADipbnT4fM7WlhVyVk4yE8n/////AAAAAQAAAAAAAAAAAAAAAQAAAAzuVzF6xNONxdQs4bm1di85p4sK5IAL8ouXuWM1");
			//Cookie

			connection.setRequestProperty("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 Edg/107.0.1418.56");
			connection.connect();			
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if (result.contains("mapResults")) {
				System.out.println("��ȡ�����");
				if(!result.contains("zpid")) {
					System.out.println("����в�������");
				}
				return result;
			}else {
				System.out.println(result);
				System.out.println("�������MapResults");
			}

		} catch (Exception e) {
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
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

		boolean flag = true;
		try {
			File file = new File(filePath);
			if (file.exists()) { 
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

			URL realUrl = new URL(urlNameString);

			URLConnection connection = realUrl.openConnection();
					
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.124 Safari/537.36 Edg/102.0.1245.44");

			connection.connect();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if (result.contains("layerId")) {
				return result;

			}

		} catch (Exception e) {
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
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
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
