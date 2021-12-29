package mvc.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import mvc.model.ReviewBbsDAO;
import mvc.model.ReviewBbsDTO;

public class ReviewBbsUpdateAction implements CommandReviewBbs{
	@Override
	public String action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//글 수정 처리
		//DB억세스 객체 생성
		ReviewBbsDAO dao = ReviewBbsDAO.getInstance();
		
		//upload처리
		String filename="";
		String realFolder = "c:/upload";//웹 어플리케이션상의 절대 경로
		int maxSize = 10 * 1024 * 1024;//5mb - 전송될 파일의 최대 크기
		String encType = "utf-8";
		
		//MultipartRequest객체 생성
		MultipartRequest multi 
		= new MultipartRequest(request,
				realFolder,
				maxSize, 
				encType, 
				new DefaultFileRenamePolicy());
		
		 //파라미터로 넘어온 값 얻기
		 int num = Integer.parseInt(multi.getParameter("num"));
		 int pageNum =Integer.parseInt(multi.getParameter("pageNum"));
		 //검색조회 파라미터 얻기
		 String items =multi.getParameter("items");
		 String text = multi.getParameter("text");
		 
		 
		 //ReviewBbsDTO객체 생성
		 ReviewBbsDTO reviewBbs = new ReviewBbsDTO();
		 reviewBbs.setId(multi.getParameter("id"));
		 reviewBbs.setNum(num);
		 reviewBbs.setName(multi.getParameter("name"));
		 reviewBbs.setSubject(multi.getParameter("subject"));
		 reviewBbs.setContent(multi.getParameter("content"));
		 
		 //등록(수정)일자 변경
		 SimpleDateFormat formatter =new SimpleDateFormat("yyyy/MM/dd(HH:mm:ss)");
		 String regist_day = formatter.format(new Date());
		 
		 reviewBbs.setRegist_day(regist_day);
		 reviewBbs.setIp(request.getRemoteAddr());
		
		//전송된 파일정보 얻기
		Enumeration files = multi.getFileNames();
		String fname =(String)files.nextElement();
		String fileName = multi.getFilesystemName(fname);//전송되어서 서버로 넘어온파일명	
		//파일명 추가 처리
	     reviewBbs.setAttachFile(fileName);
		
		 //수정 메소드 호출
		 dao.updateReviewBbs(reviewBbs);
		 
			//상세 글정보를 상세 페이지로 전달 위해 request에 세팅
			request.setAttribute("num", num);//글번호-autoBoxing(기본타입-래퍼객체로 자동형변환)
			request.setAttribute("page", pageNum);//페이지 번호
			request.setAttribute("reviewBbs", reviewBbs);//글 정보
			request.setAttribute("items", items);//검색 타입
			request.setAttribute("text", text);//검색어
		
		return "/ReviewBbsListAction.to";
	}

}
