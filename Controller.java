import java.util.Scanner;

public class Controller {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int cmd = 0;
		String id = new String();
		String pw = new String();
		User user = new User();
		int authorization = 0;

		while (true) {
			System.out.println("[1] : 로그인 , [2] : 회원가입 , [-1] : 종료");
			System.out.print("> 수행할 작업을 선택하세요 : ");
			cmd = sc.nextInt();
			sc.nextLine();
			switch (cmd) {
			case 1:
				while (authorization < 1) {
					System.out.println("===== 로그인을 시작합니다. =====");
					System.out.print("> ID를 입력하세요 : ");
					id = sc.nextLine();
					System.out.print("> Password를 입력하세요 : ");
					pw = sc.nextLine();
					authorization = user.login(id, pw);

					if(authorization == -1)
						System.out.println("> ID와 PW를 다시 입력하세요.");
					}
				user.work(authorization);
				authorization = 0;
				break;
			case 2:
				break;
			case -1:
				System.out.println("@ [안내] 시스템을 종료합니다.");
				return;
			default:
				System.out.println("[Error] 잘못된 입력입니다.");
				break;
			}


		}
	}
}
