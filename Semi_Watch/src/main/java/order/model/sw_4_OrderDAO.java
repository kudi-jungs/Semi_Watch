package order.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import order.domain.OrderVO;
import shop.domain.ImageVO;

public interface sw_4_OrderDAO {
	
	// *** 관리자가 아닌 일반사용자로 로그인 했을 경우에는 자신이 주문한 내역만 페이징 처리하여 조회를 해오고, 
	//     관리자로 로그인을 했을 경우에는 모든 사용자들의 주문내역을 페이징 처리하여 조회해온다.
	List<Map<String, String>> getOrderList(String userid) throws SQLException;
	
	// 로그인한 아이디를 가진 사람의 이름을 가져오기
	String getorderName(String userid) throws SQLException;
	
	// 관리자가 로그인해서 전체회원 주문내역보기
	List<Map<String, String>> getOdrListAdmin(String userid) throws SQLException;
	
	;
}
