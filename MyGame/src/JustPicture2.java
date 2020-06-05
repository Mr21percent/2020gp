import java.awt.event.KeyEvent;
import java.util.Random;

import loot.*;
import loot.graphics.*;

@SuppressWarnings("serial")
public class JustPicture2 extends GameFrame {

	enum GameState {
		Started, // 게임이 아직 시작되지 않은 상태
		Ready, // 스페이스바를 누른 상태
		Running, // 게임이 시작된 상태
		Finished // 게임 종료
	}

	enum PlayerState {
		Normal, // 아무 버튼도 안 누름
		Left, // 왼쪽
		Right // 오른쪽

	}

	class Player extends DrawableObject {
		public PlayerState state;

		public Player() // 사용자 캐릭터입니다.
		{
			state = PlayerState.Normal;
			x = 295;
			y = 500;
			width = 50;
			height = 50;
			image = images.GetImage("player");

		}
	}

	class Enemy extends DrawableObject {
		public Enemy() { // 장애물 입니다.
			x = 300;
			y = 100;
			width = 100;
			height = 30;

			image = images.GetImage("player");
		}
	}

	public boolean checkCrash(Player p, Enemy e) {          // 충돌을 확인하는 함수입니다.
		if (p.x + p.width > e.x && p.x < e.x + e.width)
			if (p.y + p.height > e.y && p.y < e.y + e.height)
				return true;
		return false;
	}

	Player p;
	Enemy e;
	GameState state = GameState.Started;
	long startTime_pressing;								//마우스 스페이스바를 누르기 시작한 시각
	long timeStamp_firstFrame = 0;							//첫 프레임의 timeStamp -> 실행 이후로 경과된 시간 계산에 사용
	long timeStamp_lastFrame = 0;							//직전 프레임의 timeStamp -> 물리량 계산에 사용

	public JustPicture2(GameFrameSettings settings) {
		super(settings);

		images.LoadImage("Images/ball.png", "player");
		inputs.BindKey(KeyEvent.VK_A, 0);
		inputs.BindKey(KeyEvent.VK_D, 1);
		inputs.BindKey(KeyEvent.VK_SPACE, 2);
		p = new Player();
		e = new Enemy();
	}

	@Override
	public boolean Initialize() {                            // 글씨 옵션을 지정할 메소드입니다.
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Update(long timeStamp) {
		inputs.AcceptInputs();
		Random random = new Random();
		
		int speed = 10; // 좌 우로 움직이는 속도 조절
		int rightMax = 350; // 우측으로의 최대 창 가로 크기 - 공 크기
		
		if(state == GameState.Running)                       //게임을 시작하면 장애물이 내려옴
		{e.y += 1; // 적이 내려오는 속도
		if (e.y > 600) {
			e.y = 0;
			e.x = random.nextInt(4) * 100;
		}
		}
		
		switch (state) {
		case Started:
			
		case Ready:
			
			if (inputs.buttons[2].IsPressedNow() == true)    // space 를 누르면 게임시작
				state = GameState.Running;
			break;

		case Running:

			p.state = PlayerState.Normal;

			if (inputs.buttons[0].isPressed == true) {        // a를 누르면 왼쪽으로 이동
				p.state = PlayerState.Left;
				break;
			}
			if (inputs.buttons[1].isPressed == true) {        // d를 누르면 오른쪽으로 이동
				p.state = PlayerState.Right;
				break;
			}
			if (checkCrash(p, e) == true)                     // 장애물에 부딪히면 게임 종료
				state = GameState.Finished;
			break;

		case Finished:
			break;

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

	public void Draw(long timeStamp)   {
	      BeginDraw();
	      ClearScreen();
	      DrawString(10, 30, " Space를 눌러 게임을 시작합니다  ");
	      DrawString(10, 50, " 공이 좌우로 움직입니다  ");
	      if (checkCrash(p,e)) DrawString(10, 200, " 충돌했어요 "); // 충돌시 잠시 충돌 했다고 출력
	      p.Draw(g);
	      e.Draw(g);
	      EndDraw();
	   }

}
