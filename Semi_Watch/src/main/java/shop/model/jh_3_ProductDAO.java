package shop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import shop.domain.ProductVO;

public interface jh_3_ProductDAO {
	
	// 장바구니에 있는 제품 정보(제품명, 이미지, 가격, 옵션명 등)
	List<ProductVO> select_odrProductInfo(Map<String, String[]> paraMap) throws SQLException;
	
	
	// 주문코드 시퀀스값 가져오기
	int get_seq_tbl_order_ordercode() throws SQLException;

	
	
	// 주문하기 정보 처리(주문테이블 추가, 주문배송지테이블 추가, 주문상세 추가, 장바구니 삭제, 상품상세 변경, 유저 변경)
	int checkOutUpdate(Map<String, Object> paraMap) throws SQLException;

	
	// 주문한 제품에 대해 email 보내기시 email 내용에 넣을 주문한 제품번호들에 대한 제품정보를 얻어오는 것.
	List<ProductVO> getordProductList(String pnums) throws SQLException;
	
	
	
	
	
	
}
