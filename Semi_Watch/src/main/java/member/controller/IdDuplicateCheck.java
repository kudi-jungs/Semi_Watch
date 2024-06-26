package member.controller;

import org.json.JSONObject;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.model.ky_1_MemberDAO;
import member.model.ky_1_MemberDAO_imple;

public class IdDuplicateCheck extends AbstractController {
	
	private ky_1_MemberDAO mdao = null;
	
	public IdDuplicateCheck() {
		
		mdao = new ky_1_MemberDAO_imple();
		
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) {
		
			String userid = request.getParameter("userid");
			
			boolean isExists = mdao.idDuplicateCheck(userid);
			
			JSONObject jsonObj = new JSONObject(); // {}
			jsonObj.put("isExists", isExists);// {"isExists":true} 또는 {"isExists":false} 
			
			String json = jsonObj.toString(); // 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어준다.
			// System.out.println(">>> 확인용 json => " + json);
			// >>> 확인용 json => {"isExists":true}
			
			request.setAttribute("json", json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/jsonview.jsp");
		}
		
	}

}
