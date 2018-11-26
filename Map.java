// 네이버 지도 API 예제 - 주소좌표변환
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Map {
	String clientId = "5O_V3ediiMSW6RioQ_j0";// 애플리케이션 클라이언트 아이디값";
	String clientSecret = "EAaJtRRB5W";// 애플리케이션 클라이언트 시크릿값";

	public double get_longitude(String address) {
		try
		{
			String addr = URLEncoder.encode(address, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; // json
			// String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr;

			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();

			// 경도 파싱 부분
			String str = response.toString(); // 출력문을 문자열 str에 저장
			String linesplit_str[] = str.split(" "); // 문자열을 " " 단위로 나누기
			// linesplit_str.length - 62값에 해당하는 인덱스에 경도가 들어있지만 경도 옆에 ,이 붙어있어서 제거가 필요
			String linesplit_str2[] = linesplit_str[linesplit_str.length - 62].split(","); // 문자열을 "," 단위로 나누기
			String xString = linesplit_str2[0]; // string으로 된 경도값
			double longitude = Double.parseDouble(xString); // string 경도값을 Double로 형변환

			return longitude; // 경도 반환
		}
		catch(Exception e)
		{
			System.out.println("예외 처리");
			return -1.0;
		}
	}

	public double get_latitude(String address) {
		try
		{
			String addr = URLEncoder.encode(address, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; // json
			// String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr;
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			// 위도 파싱 부분
			String str = response.toString(); // 출력문을 문자열 str에 저장
			String linesplit_str[] = str.split(" "); // 문자열을 " " 단위로 나눈다.
			String yString = linesplit_str[linesplit_str.length - 41];
			// linesplit_str.length - 41에 해당하는 값의 인덱스에 위도가 문자열로 저장되어있음. 그것을 yString에 저장
			double latitude = Double.parseDouble(yString); // yString을 Double형으로 형변환하여 latitude에 저장

			return latitude;
		}
		catch(Exception e)
		{
			System.out.println("예외 처리");
			return -1.0;
		}
	}
}
