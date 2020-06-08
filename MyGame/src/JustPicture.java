import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import loot.*;
import loot.graphics.DrawableObject;

@SuppressWarnings("serial")
public class JustPicture extends GameFrame {
	enum PlayerState {
		Normal, // 아무 버튼도 안 누름
		Left, // 왼쪽
		Right // 오른쪽
	}

	enum GameState {
		Started, // 게임이 아직 시작되지 않은 상태
		Ready, // 스페이스바를 누른 상태
		Running, // 게임이 시작된 상태
		Finished // 게임 종료
	}

	class Player extends DrawableObject {
		public PlayerState state;

		public Player() // 사용자 캐릭터입니다.
		{
			state = PlayerState.Normal;
			x = 150;
			y = 500;
			width = 50;
			height = 50;
			image = images.GetImage("player");

		}
	}

	class Enemy extends DrawableObject {
		public Enemy() {
			x = 180;
			y = -80;
			width = 50;
			height = 100;

			image = images.GetImage("enemy");
		}
	}


	public boolean checkCrash(Player p, Enemy e) { // 충돌을 확인하는 함수입니다.
		if (p.x + p.width >= e.x && p.x <= e.x + e.width)
			if (p.y + p.height >= e.y && p.y <= e.y + e.height)
				return true;
		return false;
	}


	public Player resetPlayer(Player p) { // 플레이어 위치의 초기화를 담당하는 함수입니다.
		p.x = 150;
		p.y = 500;
		p.width = 50;
		p.height = 50;
		return p;
	}

	public Enemy resetEnemy(Enemy e) { // 플레이어 위치의 초기화를 담당하는 함수입니다.
		e.x = 0;
		e.y = 0;
		e.width = 50;
		e.height = 100;
		return e;
	}
	public void OneGamePlayTime(long time) { // 플레이 시간 계산을 담당하는 함수입니다.
		if (timeStamp_firstFrame == 0) // 이번이 첫 프레임이었다면 시작 시각 기록
			timeStamp_firstFrame = time;
		timeStamp_lastFrame = time; // 이제 '직전 프레임'이 될 이번 프레임의 시작 시각 기록
		PlayTime = (int) (timeStamp_lastFrame - timeStamp_firstFrame) / 1000; // 플레이 시간 저장

		if (PlayTime % 60 == 0 && OneSec != 0 && PlayTime != 0) { // 분,초 계산
			OneMit++;
			OneSec = 0;
		} else {
			OneSec = PlayTime % 60;
		}
	}
	public void BestGamePlayTime() { // 가장 오랫동안 살아남은 게임의 시간을 저장합니다. 
		if (BestTime < PlayTime)
			BestTime = PlayTime;
		if (BestTime % 60 == 0 && BestSec != 0 && BestTime != 0) { // 분,초 계산
			BestMit++;
			BestSec = 0;
		} else {
			BestSec = BestTime % 60;
		}

	}

	Player p;
	Enemy e;
//	Bullet b;
	GameState state = GameState.Started;
	long timeStamp_firstFrame = 0; // 첫 프레임의 timeStamp -> 실행 이후로 경과된 시간 계산에 사용
	long timeStamp_lastFrame = 0; // 직전 프레임의 timeStamp -> 물리량 계산에 사용
	int PlayTime = 0; //플레이 시간을 말하고 있습니다
	int OneSec = 0; //플레이 시간 초단위
	int OneMit = 0; //플레이 시간 분단위
	int BestTime; //최고 기록 을 의미합니다
	int BestSec; //최고 기록 초를 의미합니다
	int BestMit; //최고 기록 분을 의미합니다
	public JustPicture(GameFrameSettings settings) {
		super(settings);

		images.LoadImage("Images/ball.png", "player");
		images.LoadImage("Images/car.png", "enemy");
		inputs.BindKey(KeyEvent.VK_A, 0);
		inputs.BindKey(KeyEvent.VK_D, 1);
		inputs.BindKey(KeyEvent.VK_SPACE, 2);
		inputs.BindKey(KeyEvent.VK_R, 3);


		p = new Player();
		e = new Enemy();
	}

	public boolean Update(long timeStamp) {
		inputs.AcceptInputs();
		Random random = new Random();

		switch (state) {
		case Started:

		case Ready:

			if (inputs.buttons[2].IsPressedNow() == true) // space 를 누르면 게임시작
				state = GameState.Running;
			break;

		case Running:
			OneGamePlayTime(timeStamp);
			p.state = PlayerState.Normal;
			if (checkCrash(p, e) == true) // 장애물에 부딪히면 게임 종료
				state = GameState.Finished;
			if (inputs.buttons[0].isPressed == true) { // a를 누르면 왼쪽으로 이동
				p.state = PlayerState.Left;
				break;
			}
			if (inputs.buttons[1].isPressed == true) { // d를 누르면 오른쪽으로 이동
				p.state = PlayerState.Right;
				break;
			}
			
			break;

		case Finished:
			p.state = PlayerState.Normal; // 게임이 끝나면 공을 멈춤
			BestGamePlayTime(); //베스트 플레이 갱
			if (inputs.buttons[3].isPressed == true) { // r을 누르면 시작하기 전으로 돌아가기
				state = GameState.Started;
				p = resetPlayer(p);
				e = resetEnemy(e);
				timeStamp_firstFrame = 0; // 시간 초기화
				timeStamp_lastFrame = 0;
				OneMit = 0;
				OneSec = 0;
				break;
			}
			break;

		}

		int speed = 10; // 좌 우로 움직이는 속도 조절
		int rightMax = 350; // 우측으로의 최대 창 가로 크기 - 공 크기
		int enemySpeed = 10; // 적이 내려오는 속도
		int nando=1; //1초에 증가하는 enemySpeed
		if (state == GameState.Running) // 게임을 시작하면 장애물이 내려옴
		{	enemySpeed+=nando*(timeStamp_lastFrame-timeStamp_firstFrame)/1000; //1초에 속도 nando씩 증가
			e.y += enemySpeed; // 적이 내려오는 속도
			if (e.y > 600) {
				e.y = 0;
				e.x = random.nextInt(10) * e.width;
			}
		}

		switch (p.state) { // 캐릭터의 움직임을 출력합니다.
		case Normal:
			break;
		case Left:
			p.x -= speed;
			if (p.x < 0)
				p.x += speed;
			break;
		case Right:
			p.x += speed;
			if (p.x > rightMax)
				p.x -= speed;
			break;
		}

		return true;
	}

	public void Draw(long timeStamp) {
		BeginDraw();
		ClearScreen();
		switch (state) {
		case Started:
		case Ready:
			DrawString(10, 30, " Space를 눌러 게임을 시작합니다  ");
			DrawString(10, 50, " a, d를 눌러 좌우로 움직이세요");
			break;
		case Running:
			DrawString(10, 30, " 최대한 오래 버텨보세요  ");
			DrawString(10, 50, "   게임시작");
			DrawString(10, 70, "Time %4d: %4d", OneMit, OneSec);
			p.Draw(g);
			e.Draw(g);
			break;
		case Finished:
			DrawString(10, 70, " 충돌했어요 "); // 충돌시 잠시 충돌 했다고 출력
			DrawString(150, 140, "Time %4d : %4d", OneMit, OneSec);
			DrawString(150, 160, "BestPlay : %4d: %4d", BestMit, BestSec);
			DrawString(150, 180, "R을 눌러 다시 시작  ");
			p.Draw(g);
			e.Draw(g);
			break;
		}
		EndDraw();
	}

	public boolean Initialize() {
		// TODO Auto-generated method stub
		return true;
	}

}
