package shop.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import shop.domain.ImageVO;
import shop.domain.ProductVO;

public class ky_1_ProductDAO_imple implements ky_1_ProductDAO {

	// DB에 사용되는 객체
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	// import javax.sql.DataSource;
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// DB Connection Pool.txt 파일내용을 복붙한 내용
	// 생성자
	public ky_1_ProductDAO_imple() {
		
		try {
		Context initContext = new InitialContext();
	    Context envContext  = (Context)initContext.lookup("java:/comp/env");
	    ds = (DataSource)envContext.lookup("jdbc/semioracle");

		}catch(NamingException e) {
			
		}
		
	}// end of public void ProductDAO_imple() {} 
	
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기 
   private void close() {
      try {
         if(rs != null)    {rs.close();    rs=null;}
         if(pstmt != null) {pstmt.close(); pstmt=null;}
         if(conn != null)  {conn.close();  conn=null;}
      } catch(SQLException e) {
         e.printStackTrace();
      }
   } // end of private void close() {} 

	// 최신 등록순으로 6개의 상품 이미지를 가져오기
	@Override
	public List<Map<String, String>> selectByRegiDate() throws SQLException {
		
		List<Map<String, String>> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT rno, pdno, pdimg1 "
					+ " FROM "
					+ " (select row_number() over(order by pdinputdate desc) AS rno, pdno, pdimg1 "
					+ " from tbl_product) "
					+ " where rno between 1 and 6 ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				Map<String, String> paraMap = new HashMap<>(); 
				
				
				paraMap.put("pdno", rs.getString("pdno"));
				paraMap.put("pdimg1", rs.getString("pdimg1"));
				
				productList.add(paraMap);
				
			}// end of while(rs.next()) 
			
			// System.out.println("확인용 리스트 사이즈 : "+productList.size());
			
		} finally {
			close();
		}
		
		return productList;
		
	}// end of public List<ProductVO> selectByRegiDate(Map<String, String> paraMap) throws SQLException 

	
	// tbl_map(위, 경도) 테이블에 있는 정보를 가져오기(select)
	@Override
	public List<Map<String, String>> selectCenterMap() throws SQLException {
		
		List<Map<String, String>> centerMapList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select storeID, storeName, storeUrl, storeImg, storeAddress, lat, lng, zindex " +
						 " from tbl_map " +
						 " order by zindex asc ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, String> centerMap = new HashMap<>();
				centerMap.put("STOREID", rs.getString("STOREID"));
				centerMap.put("STORENAME", rs.getString("STORENAME"));
				centerMap.put("STOREURL", rs.getString("STOREURL"));
				centerMap.put("STOREIMG", rs.getString("STOREIMG"));
				centerMap.put("STOREADDRESS", rs.getString("STOREADDRESS"));
				centerMap.put("LAT", rs.getString("LAT"));
				centerMap.put("LNG", rs.getString("LNG"));
				centerMap.put("ZINDEX", rs.getString("ZINDEX"));
				
				centerMapList.add(centerMap);
			}
					
		} finally {
			close();
		}
				
		return centerMapList;
	}// end of public List<Map<String, String>> selectCenterMap() throws SQLException 
	
	
	

}
