package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mvc.database.DBConnection;
import mvc.database.DBConnectionOracle;

//싱글톤 
public class ReviewBbsDAO {
  //1.자신타입의 static 필드 선언
  private static ReviewBbsDAO instance;
  //2.default생성자를 private로 선언
  private ReviewBbsDAO() {}
  //3. public 접근제어타입의 getInstance()메소드 선언,getInstance()로만 접근 
  public static ReviewBbsDAO getInstance() {
	  if(instance==null) instance = new ReviewBbsDAO();
	return instance;
  }
  
  //Mysql의 회원정보 얻기
  public String getLoginNameById(String id) {
	  Connection conn=null;
	  PreparedStatement pstmt=null;
	  ResultSet rs=null;
	  
	  String name=null;
	  String sql="select * from member where id=?"; //mysql - ok
	  
	  try {
		    //Mysql 접속용 DBConnection객체 
		    conn=DBConnection.getConnection();
		    pstmt=conn.prepareStatement(sql);
		    pstmt.setString(1, id); 
		
		    rs=pstmt.executeQuery();
		    
		    if(rs.next()) {
		    	name=rs.getString("name");//rs.getString(칼럼명)
		    }
		    return name;//값을 db에서 얻어왔으면 name을 리턴, 아니면 null값 그대로 리턴.
	  }catch(Exception e) {
		  System.out.println("에러:"+e);
	  }finally {
		  try {
			    if(rs!=null) rs.close(); if(pstmt!=null) pstmt.close();
			    if(conn!=null)conn.close();
		  }catch(Exception e) {
			  throw new RuntimeException(e.getMessage());
		  }
	  }
	return null;  
  }
  
  //db에 저장하는 메소드
  public void insertReviewBbs(ReviewBbsDTO reviewBbs) {
	  Connection conn=null;
	  PreparedStatement pstmt=null;
	  String sql ="insert into reviewBbs values(reviewBbs_seq.nextval,?,?,?,?,?,?,?,?,?)";
	  try {
		     //1.Oracle dbconnection 맺기
		     conn =DBConnectionOracle.getConnection();
		     //2. sql 전달객체 생성
		     pstmt = conn.prepareStatement(sql);
		     //3. sql문의 바인딩 변수 세팅
		     pstmt.setString(1, reviewBbs.getId());
		     pstmt.setString(2, reviewBbs.getName());
		     pstmt.setString(3, reviewBbs.getSubject());
		     pstmt.setString(4, reviewBbs.getContent());
		     pstmt.setString(5, reviewBbs.getRegist_day());
		     pstmt.setInt(6, reviewBbs.getHit());
		     pstmt.setString(7,reviewBbs.getIp());
		     pstmt.setString(8,reviewBbs.getStar());
		     pstmt.setString(8,reviewBbs.getP_id());
		     pstmt.setString(9, reviewBbs.getAttachFile());
		     
		     //4. 쿼리 객체 전달 및 실행
		     pstmt.executeUpdate();
	  }catch(Exception e) {
		  System.out.println("에러:"+e.getMessage());
	  }finally {
		  try {
			    if(pstmt!=null) pstmt.close();
			    if(conn!=null)conn.close();
		  }catch(Exception e) {
			  throw new RuntimeException(e.getMessage());
		  }
	  }
  }//insertreviewBbs() 끝.
  
//ReviewBbs테이블의 레코드 가져오기
	public List<ReviewBbsDTO> getReviewBbsList(int pageNum, int limit, String items, String text) {
		List<ReviewBbsDTO> reviewBbslist = new ArrayList<ReviewBbsDTO>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql="";
		
		if((items==null && text==null)||( items.length()==0 || text.length()==0)) {//검색 조건이 파라미터로 넘어오지 않은 경우
			sql = "select * from "
				+ " (select rownum rn, reviewBbs.* "
				+ " from reviewBbs "
				+ " order by num desc) " //최근 등록글이 먼저 나오도록 처리
				+ " where rn between ? and ?";	
		}else { //검색 조건이 파라미터로 넘어온 경우 
			sql = "select * from "
				+ " (select rownum rn, reviewBbs.* "
				+ " from reviewBbs "
				+ " where "+items+" like '%'||?||'%' " //|| : 결합 연산자
				+ " order by num desc) "
				+ " where rn between ? and ?";
		 }
		System.out.println("sql:"+sql);
		
		//페이지 번호로 해당 페이지의 시작 글번호와 끝 글번호 구하기
		int start = (pageNum-1)*limit; //예)3페이지 -> (3-1)*10=20, 1페이지 ->0
		int index = start +1; //예)index=21, 1
		int end = index +9; //예)21+9=30, 1+9=10
		
		try {
			//1.OracleDB 연결객체 생성
			conn = DBConnectionOracle.getConnection();
			if((items==null && text==null)||( items.length()==0 || text.length()==0)) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, index);
				pstmt.setInt(2, end);
			}else {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, text);
				pstmt.setInt(2, index);
				pstmt.setInt(3, end);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				//DB로부터 결과 레코드를 하나씩 가져와서 ReviewBbsDTO에 담은 후 리스트에 저장하기
				ReviewBbsDTO reviewBbs = new ReviewBbsDTO();
				reviewBbs.setNum(rs.getInt(2));
				reviewBbs.setId(rs.getString(3));
				reviewBbs.setName(rs.getString(4));
				reviewBbs.setSubject(rs.getString(5));
				reviewBbs.setContent(rs.getString(6));
				reviewBbs.setRegist_day(rs.getString(7));
				reviewBbs.setHit(rs.getInt(8));
				reviewBbs.setIp(rs.getString(9));
				reviewBbs.setP_id(rs.getString(10));
				
				//리스트에 추가하기
				reviewBbslist.add(reviewBbs);
			}
		}catch (Exception e) {
			System.out.println("에러:"+e.getMessage());
		}finally {
			try {
				if(rs!=null) rs.close(); if(pstmt!=null)pstmt.close(); if(conn!=null)conn.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return reviewBbslist;
	} //getreviewBbsList()메소드 끝.


//전체 게시글 건수 가져오기
	public int getListCount(int pageNum, int limit, String items, String text) {
		//조회한 게시글 건수 변수
		int count=0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql="";
		
		if((items==null && text==null)||( items.length()==0 || text.length()==0)) {//검색 조건이 파라미터로 넘어오지 않은 경우
			sql = "select count(*) "
				+ " from reviewBbs ";	
		}else { //검색 조건이 파라미터로 넘어온 경우 
			sql = "select count(*) "
				+ " from reviewBbs "
				+ " where "+items+" like '%'||?||'%' ";
		 }
		System.out.println("sql:"+sql);
		
		try {
			//1.OracleDB 연결객체 생성
			conn = DBConnectionOracle.getConnection();
			if((items==null && text==null)||( items.length()==0 || text.length()==0)) {
				pstmt = conn.prepareStatement(sql);
			}else {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, text);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				//DB로부터 게시글 건수를 가져와서 count에 저장
				count = rs.getInt(1);
			}
		}catch (Exception e) {
			System.out.println("에러:"+e);
		}finally {
			try {
				if(rs!=null) rs.close(); if(pstmt!=null)pstmt.close(); if(conn!=null)conn.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return count;
	} //getListCount() 끝.

 //상세 페이지 뷰 메소드
public ReviewBbsDTO getReviewBbsByNum(int num,int pageNum) {
	//조회한 게시글 정보 저장 객체 생성
	ReviewBbsDTO reviewBbs=null;
	Connection conn=null;
    PreparedStatement pstmt=null;
    ResultSet rs = null;
    
    String sql="select * from reviewBbs where num=?";
    System.out.println("sql:"+sql);
   
    try {
          //1.OracleDB 연결객체 생성
    	 conn = DBConnectionOracle.getConnection();
    	 pstmt = conn.prepareStatement(sql);
    	 pstmt.setInt(1, num);//매개변수 넘어온 글번호 설정
    	 rs = pstmt.executeQuery();
    	 if(rs.next()) {
           //db로 부터 해당 글번호에 맞는 게시글 정보를 가져와서 reviewBbsDTO에 저장
    	   reviewBbs = new ReviewBbsDTO();	 
    		 reviewBbs.setNum(rs.getInt(1));
    		 reviewBbs.setId(rs.getString(2));
    		 reviewBbs.setName(rs.getString(3));
    		 reviewBbs.setSubject(rs.getString(4));
    		 reviewBbs.setContent(rs.getString(5));
    		 reviewBbs.setRegist_day(rs.getString(6));
    		 reviewBbs.setHit(rs.getInt(7));
    		 reviewBbs.setIp(rs.getString(8));
    		 reviewBbs.setStar(rs.getString(9));
    		 reviewBbs.setP_id(rs.getString(10));
    		 reviewBbs.setAttachFile(rs.getString(11));//첨부파일 정보 
    	}
    }catch(Exception e) {
		  System.out.println("에러:"+e.getMessage());
	  }finally {
		  try {
			    if(rs!=null) rs.close();
			    if(pstmt!=null) pstmt.close();
			    if(conn!=null)conn.close();
		  }catch(Exception e) {
			  throw new RuntimeException(e.getMessage());
		  }
	  } 
	return reviewBbs;//reviewBbsDTO객체 리턴.
}//getreviewBbsByNum() 끝.

//게시글 조회수 증가 메소드
public void updateHit(int num) {
		Connection conn=null;
	    PreparedStatement pstmt=null;
	    
	    String sql="update reviewBbs set hit=hit+1  where num=?";
	    System.out.println("sql:"+sql);
	   
	    try {
	          //1.OracleDB 연결객체 생성
	    	 conn = DBConnectionOracle.getConnection();
	    	 pstmt = conn.prepareStatement(sql);
	    	 pstmt.setInt(1, num);
	    	 pstmt.executeUpdate();
	    }catch(Exception e) {
			  System.out.println("에러:"+e.getMessage());
		  }finally {
			  try {
				    if(pstmt!=null) pstmt.close();
				    if(conn!=null)conn.close();
			  }catch(Exception e) {
				  throw new RuntimeException(e.getMessage());
			  }
		  } 	
}//updateHit() 끝.

//게시글 내용 수정
public void updateReviewBbs(ReviewBbsDTO reviewBbs) {
	Connection conn=null;
    PreparedStatement pstmt=null;
    String sql="";
    
    if(reviewBbs.getAttachFile()==null) {//첨부파일이 전송되지 않은 경우
    	sql="update reviewBbs set id=?,name=?,subject=?,content=?, "
    			 + " regist_day=?,ip=?  where num=?";	
    }else {//첨부파일 전송된 경우
    	sql="update reviewBbs set id=?,name=?,subject=?,content=?, "
    			 + " regist_day=?,ip=?, attachFile=?  where num=?";
    }
    System.out.println("sql:"+sql);
   
    try {
          //1.OracleDB 연결객체 생성
    	 conn = DBConnectionOracle.getConnection();
    	 pstmt = conn.prepareStatement(sql);
    	 //2. 바인딩변수 세팅
    	 pstmt.setString(1, reviewBbs.getId());
    	 pstmt.setString(2, reviewBbs.getName());
    	 pstmt.setString(3, reviewBbs.getSubject());
    	 pstmt.setString(4, reviewBbs.getContent());
    	 pstmt.setString(5, reviewBbs.getRegist_day());
    	 pstmt.setString(6, reviewBbs.getIp());
    	 pstmt.setString(7, reviewBbs.getStar());
    	 pstmt.setString(8, reviewBbs.getP_id());
    	 if(reviewBbs.getAttachFile()==null) {
    	     pstmt.setInt(9, reviewBbs.getNum());
    	 }else {
    		 pstmt.setString(9,reviewBbs.getAttachFile());
    		 pstmt.setInt(10, reviewBbs.getNum());
    	 }
    	 //update처리
    	 pstmt.executeUpdate();
    }catch(Exception e) {
		  System.out.println("에러:"+e.getMessage());
	  }finally {
		  try {
			    if(pstmt!=null) pstmt.close();
			    if(conn!=null)conn.close();
		  }catch(Exception e) {
			  throw new RuntimeException(e.getMessage());
		  }
	  } 	
}//updatereviewBbs() 끝.

//게시글 삭제 메소드
public void deleteReviewBbs(int num) {
	Connection conn=null;
    PreparedStatement pstmt=null;
    
    String sql="delete from reviewBbs where num=?";    
    System.out.println("sql:"+sql);
   
    try {
          //1.OracleDB 연결객체 생성
    	 conn = DBConnectionOracle.getConnection();
    	 pstmt = conn.prepareStatement(sql);
    	 //2. 바인딩변수 세팅
    	 pstmt.setInt(1, num);
    	 //update처리
    	 pstmt.executeUpdate();
    }catch(Exception e) {
		  System.out.println("에러:"+e.getMessage());
	  }finally {
		  try {
			    if(pstmt!=null) pstmt.close();    if(conn!=null)conn.close();
		  }catch(Exception e) {
			  throw new RuntimeException(e.getMessage());
		  }
	  } 	
}//deletereviewBbs() 끝.



}