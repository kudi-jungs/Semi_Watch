
package order.model;

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

import member.domain.MemberVO;
import order.domain.OrderVO;
import shop.domain.ImageVO;

public class sw_4_OrderDAO_imple implements sw_4_OrderDAO {

	// DB에 사용되는 객체
	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.
	// import javax.sql.DataSource;
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// DB Connection Pool.txt 파일내용을 복붙한 내용
	// 생성자
	public sw_4_OrderDAO_imple() {
		
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

	// *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 페이징 처리하여 조회를 해오고, 
	//     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 페이징 처리하여 조회해온다.
	@Override
	public List<Map<String, String>> getOrderList(String userid)throws SQLException {
		
		List<Map<String,String>> order_map_List = new ArrayList<>();
		
		try {
    		conn = ds.getConnection();
    		
    		String sql = " select ordercode, E.fk_userid as fk_userid , total_price, total_orderdate, fk_pdno, color, pdname "
    				   + " from "
    				   + " ( "
    				   + "    select ordercode, C.fk_userid, total_price, total_orderdate, fk_pdno, color "
    				   + "    from "
    				   + "    ( "
    				   + "    select ordercode, A.fk_userid, total_price, total_orderdate, fk_pd_detailno "
    				   + "    from "
    				   + "    ( "
    				   + "        select ordercode, fk_userid, total_price, total_orderdate "
    				   + "        from tbl_order ";
    		
    		if(!"admin".equals(userid)) {
    			// 관리자가 아닌 일반사용자로 로그인한 경우
    			sql += " where fk_userid = ? "; 
    		}
    		
    		sql		   += "    ) A "
    				   + "    join tbl_orderdetail B "
    				   + "    on A.ordercode = B.fk_ordercode "
    				   + " ) C "
    				   + " join tbl_pd_detail D "
    				   + " on C.fk_pd_detailno = D.pd_detailno "
    				   + " ) E "
    				   + " join tbl_product F "
    				   + " on E.fk_pdno = F.pdno ";
    	
    		
    		pstmt = conn.prepareStatement(sql);
    		
    		if(!"admin".equals(userid)) {
    			// 관리자가 아닌 일반사용자로 로그인한 경우
    			pstmt.setString(1, userid);
    		}
    		rs = pstmt.executeQuery();
    		
			while(rs.next()) {
				
				String ordercode = rs.getString("ordercode");
				// String fk_userid = rs.getString("fk_userid");
				String total_price = rs.getString("total_price");
				String total_orderdate = rs.getString("total_orderdate");
				String fk_pdno = rs.getString("fk_pdno");
				String color = rs.getString("color");
				String pdname = rs.getString("pdname");
				
				Map<String,String> odrmap = new HashMap<>();
				odrmap.put("ordercode", ordercode);
				// odrmap.put("fk_userid", fk_userid);
				odrmap.put("total_price", total_price);
				odrmap.put("total_orderdate", total_orderdate);
				odrmap.put("fk_pdno", fk_pdno);
				odrmap.put("color", color);
				odrmap.put("pdname", pdname);
				
				order_map_List.add(odrmap);				
			
			} // end of while(rs.next()) {}----------------
		}finally {
			close();
		}	
		return order_map_List;
	}
	
	
	// 로그인한 유저 아이디 가져오기(주문현황 시작 파트에서 들어가야함. 관리자 제외)
	@Override
	public String getorderName(String userid) throws SQLException {
		
		String getorderName = "";
		
		try {
    		conn = ds.getConnection();
    		
    	    String sql = " select username "
    				   + " from tbl_member "
    				   + " where userid = ? "; 
		
    	    pstmt = conn.prepareStatement(sql);
    	    pstmt.setString(1, userid);
    	    
    	    rs = pstmt.executeQuery();
    	    
    	    if(rs.next()) {
    	    	getorderName = new String();
    	    }
    	    String username = rs.getString("username");
    	    
    	    getorderName = username;
    	 // System.out.println("확인용 : " + getorderName);
		}finally {
			close();
		}
		return getorderName;
	}

	// 관리자가 전 회원 주문내역 보기 
	@Override
	public List<Map<String, String>> getOdrListAdmin(String userid) throws SQLException {
		
		List<Map<String,String>> order_list_admin = new ArrayList<>();
		
		try {
    		conn = ds.getConnection();
    		
    		String sql = " select A.ordercode, A.fk_userid, total_price, to_char(total_orderdate,'yyyy-mm-dd') as total_orderdate "
	 				   + "		, pdname"
	 				   + "		, case delivery_status "
	 				   + "			when 1 then '주문완료' "
	 				   + "			when 2 then '배송중' "
	 				   + "			when 3 then '배송완료' "
	 				   + "			end as delivery_status "
	    			   + " from "
	    			   + " ( "
	    			   + " select * "
	    			   + " from tbl_order "
	    			   + " )A "
	    			   + " join tbl_orderdetail B "
	    			   + " on A.ordercode = B.fk_ordercode "
	    			   + " join tbl_pd_detail C "
	    			   + " on B.fk_pd_detailno = C.pd_detailno "
	    		       + " join tbl_product D "
	    			   + " on C.fk_pdno = D.pdno ";
 			
 			if("admin".equals(userid)) {
 				// 관리자가 전 회원의 주문내역을 볼 때
 				sql += " where A.fk_userid != 'admin' ";
 				
 			}
 			
 			pstmt = conn.prepareStatement(sql);
		
 			rs = pstmt.executeQuery();
 			
 				while(rs.next()) {
 					
 					String ordercode = rs.getString("ordercode");
 					String fk_userid = rs.getString("fk_userid");
 					String total_price = rs.getString("total_price");
 					String total_odrdate = rs.getString("total_orderdate");
 					String pdname = rs.getString("pdname");
 					String delivery_status = rs.getString("delivery_status");
 					
 					
 					Map<String, String> odradminList = new HashMap<>();
 					
 					odradminList.put("ordercode", ordercode);
 					odradminList.put("fk_userid",fk_userid);
 					odradminList.put("total_price", total_price);
 					odradminList.put("total_orderdate", total_odrdate);
 					odradminList.put("pdname", pdname);
 					odradminList.put("delivery_status", delivery_status);
 					
 					order_list_admin.add(odradminList);
 					
 				} // end of while()--------------------------------------------------
 				
			}finally {
				close();
			}
		
		return order_list_admin;
	} // end of public List<Map<String, String>> getOdrListAdmin(String userid) {----------------------------

}
