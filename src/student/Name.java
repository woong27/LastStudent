package student;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Name {
	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, OUTPUT = 2, UPDATE = 3, SEARCH = 4, DELETE = 5, SORT = 6, STATS = 7, EXIT = 8;

	public static void main(String[] args) {

		DBConnection dbConn = new DBConnection();
		// database 연결
		dbConn.connect();

		boolean loopFlag = false;
		while (!loopFlag) {
			int num = displayMenu();

			switch (num) {
			case INPUT: // 입력
				studentInputdata();
				break;
			case UPDATE:// 수정
				studentUpdate();
				break;
			case SEARCH:// 검색
				studentSearch();
				break;
			case DELETE: // 삭제
				studentDelete();
				break;
			case OUTPUT:// 출력
				studentOutput();
				break;
			case SORT:// 정렬
				studentSort();
				break;
			case STATS:// 통계
				studentStats();
				break;
			case EXIT:// 종료
				loopFlag = true;
				break;
			default:
				System.out.println("1~8번 입력바랍니다.");
				break;
			}
		}
		System.out.println("종료");
	}

	// 통계
	private static void studentStats() {
		List<Student> list = new ArrayList<Student>();

		try {
			System.out.println("|1.최고점수|2.최저점수|\n입력 >>");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 5);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			dbc.connect();

			list = dbc.selectMaxMin(type);

			if (list.size() <= 0) {
				System.out.println("검색한 학생정보가 없습니다" + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("InputMismatchException eror" + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("database stats error" + e.getMessage());
		}

	}

	// 정렬
	private static void studentSort() {
		List<Student> list = new ArrayList<Student>();
		try {
			// database 연결
			DBConnection dbConn = new DBConnection();

			dbConn.connect();
			// 정렬방식 입력
			System.out.print("정렬방식 입력(1:no,2:name,3:total) 입력 >> ");
			int type = sc.nextInt();
			// 패턴번호 입력
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if (!value)
				return;

			list = dbConn.selectOrderBy(type);

			if (list.size() <= 0) {
				System.out.println("보여줄 리스트가 없습니다" + list.size());
				return;
			}

			for (Student student : list) {
				System.out.println(student);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("database error" + e.getMessage());
		}
		return;
	}

	// 수정
	public static void studentUpdate() {

		List<Student> list = new ArrayList<Student>();
		try {
			// 수정할 회원번호 입력
			System.out.print("수정할 회원번호 입력 >> ");
			String no = sc.nextLine();
			// 패턴 검색
			boolean value = checkInputPattern(no, 1);
			if (!value)
				return;

			DBConnection dbc = new DBConnection();
			// Database connection
			dbc.connect();
			// Entering article table data
			list = dbc.selectSearch(no, 1);

			if (list.size() <= 0) {
				System.out.println("입력된 정보가 없습니다.");
			}

			// 리스트 내용 보여주기
			for (Student student : list) {
				System.out.println(student);
			}

			Student imsiStudent = list.get(0);
			System.out.print("국어점수 입력 >>");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;
			imsiStudent.setKor(kor);

			System.out.print("영어점수 입력 >>");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;
			imsiStudent.setEng(eng);

			System.out.print("수학점수 입력 >>");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;
			imsiStudent.setMath(math);

			// 데이터베이스 수정할 부분을 update
			int returnUpdateValue = dbc.update(imsiStudent);
			if (returnUpdateValue == -1) {
				System.out.println("수정할 정보가 없습니다.");
				return;
			}
			System.out.println("수정이 완료되었습니다.");

			dbc.close();

		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지않습니다.");
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("Exception error");
			return;
		}
	}

	// 검색
	private static void studentSearch() {
		List<Student> list = new ArrayList<Student>();

		try {

			System.out.print("검색할 학생 이름 >>");
			String name = sc.nextLine();

			boolean value = checkInputPattern(name, 2);
			if (!value) {
				return;
			}
			// 데이터배이스 연결
			DBConnection dbConn = new DBConnection();

			dbConn.connect();
			list = dbConn.selectSearch(name, 2);
			if (list.size() <= 0) {
				System.out.println("검색할 학생정보가 없습니다. " + list.size());
				return;
			}

			for (Student student : list) {
				System.out.println(student);
			}
			dbConn.close();

		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지않습니다." + e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println("database search error" + e.getStackTrace());
		}
	}

	// 출력
	private static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try {
			// 데이터베이스 연결
			DBConnection dbConn = new DBConnection();

			dbConn.connect();
			list = dbConn.select();

			if (list.size() <= 0) {
				System.out.println("보여줄 리스트가 없습니다." + list.size());
				return;
			}

			for (Student student : list) {
				System.out.println(student);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("Exception error " + e.getMessage());
		}
		return;
	}

	// 삭제
	private static void studentDelete() {
		try {
			System.out.println("삭제할 회원번호 입력 >>");
			String no = sc.nextLine();

			boolean value = checkInputPattern(no, 1);
			if (!value) {
				return;
			}

			DBConnection dbConn = new DBConnection();

			dbConn.connect();
			int deleteReturnValue = dbConn.delete(no);
			if (deleteReturnValue == -1) {
				System.out.println("삭제 실패입니다. " + deleteReturnValue);
				return;
			}
			if (deleteReturnValue == 0) {
				System.out.println("삭제할 번호가 없습니다. " + deleteReturnValue);
			} else {
				System.out.println("삭제성공 리턴값 = " + deleteReturnValue);
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지않습니다.");
			return;
		}
	}

	// 입력
	private static void studentInputdata() {

		try {

			System.out.println("학번 입력 (예 : 010101)>>");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 1);
			if (!value) {
				return;
			}

			// 이름
			System.out.println("이름 입력 >>");
			String name = sc.nextLine();

			value = checkInputPattern(name, 2);
			if (!value)
				return;
			// 국어점수
			System.out.println("국어점수 입력 >>");
			int kor = sc.nextInt();
			value = checkInputPattern(String.valueOf(kor), 3);
			if (!value)
				return;

			// 영어
			System.out.println("영어점수 입력 >>");
			int eng = sc.nextInt();
			value = checkInputPattern(String.valueOf(eng), 3);
			if (!value)
				return;

			// 수학
			System.out.println("수학점수 입력 >>");
			int math = sc.nextInt();
			value = checkInputPattern(String.valueOf(math), 3);
			if (!value)
				return;

			Student student = new Student(no, name, kor, eng, math);

			DBConnection dbConn = new DBConnection();
			// 데이터베이스 연결
			dbConn.connect();
			int insertReturnValue = dbConn.insert(student);
			if (insertReturnValue == -1) {
				System.out.println("삽입 성공입니다. ");
			} else {
				System.out.println("삽입 성공 리턴값 = " + insertReturnValue);
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지않습니다.");
			return;
		} finally {
			sc.nextLine();
		}

	}

	// 메뉴
	private static int displayMenu() {
		int num = -1;
		try {
			
			System.out.println("1.입력 | 2.출력 | 3.수정 | 4.검색 | 5.삭제 | 6.정렬 | 7.통계 | 8.종료 | \n입력 >>");
			num = sc.nextInt();

			String pattern = "^[1-8]*$";
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
			System.out.println(regex);
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력되었습니다.");
			num = -1;
		} finally {
			sc.nextLine();
		}
		return num;
	}

	// 문자패턴검색

	private static boolean checkInputPattern(String data, int patternType) {

		String pattern = null;
		boolean regex = true;
		String message = null;
		switch (patternType) {
		case 1:
			pattern = "^0[1-3]0[1-9][0-6][0-9]$";
			message = "학생번호 재입력(예 : 010101)";
			break;
		case 2:
			pattern = "^[가-힣]{2,4}$";
			message = "이름 재입력 (예:홍길동)";
			break;
		case 3:
			pattern = "^[0-9]{1,3}$";
			message = "점수 재입력 (예 : 0~100)";
			break;
		case 4:
			pattern = "^[1-3]$";
			message = "정렬타입 재입력 (예 : 1~3)";
			break;
		case 5:
			pattern = "^[1-2]$";
			message = "통계타입 재입력 (에 : 1~2)";
			break;

		}

		regex = Pattern.matches(pattern, data);
		if (patternType == 3) {
			if (!regex || Integer.parseInt(data) < 0 || Integer.parseInt(data) > 100) {
				System.out.println("다시입력해주세요");
				return false;
			}
		} else {
			if (!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;

	}

}
