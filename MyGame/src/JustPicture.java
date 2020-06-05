import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import loot.*;
import loot.graphics.DrawableObject;

@SuppressWarnings("serial")
public class JustPicture extends GameFrame
{
	enum PlayerState
	{
		Normal,		//아무 버튼도 안 누름
		Left,	//왼쪽
		Right	//오른쪽
	}
	enum GameState {
		Started, // 게임이 아직 시작되지 않은 상태
		Ready, // 스페이스바를 누른 상태
		Running, // 게임이 시작된 상태
		Finished // 게임 종료	
		}

   class Player extends DrawableObject
   {
	  public PlayerState state;
      public Player() // 사용자 캐릭터입니다.
      {
    	 state = PlayerState.Normal;
         x = 300;
         y = 500;
         width = 50;
         height = 50;
         image = images.GetImage("player");
         
      }
   }
   class Enemy extends DrawableObject
	{
		public Enemy()
		{
			x = 300;
			y = 400;
			width = 40;
			height = 80;
			
			image = images.GetImage("enemy");
		}
	}
   
   public boolean checkCrash(Player p, Enemy e) {  // 충돌을 확인하는 함수입니다.
		if (p.x + p.width > e.x && p.x < e.x + e.width)
			if (p.y + p.height > e.y && p.y < e.y + e.height)
				return true;
	   	return false;
	}
   public Player resetPlayer(Player p) {  // 플레이어 위치의 초기화를 담당하는 함수입니다.
	    p.x = 300;
		p.y = 500;
		p.width = 50;
		p.height = 50;
	   	return p;
	}

   	Player p;
	Enemy e;
	GameState state = GameState.Started;
	long startTime_pressing;								//마우스 스페이스바를 누르기 시작한 시각
	long timeStamp_firstFrame = 0;							//첫 프레임의 timeStamp -> 실행 이후로 경과된 시간 계산에 사용
	long timeStamp_lastFrame = 0;							//직전 프레임의 timeStamp -> 물리량 계산에 사용
   public JustPicture(GameFrameSettings settings)
   {
      super(settings);
      
      images.LoadImage("Images/ball.png", "player");
      images.LoadImage("Images/car.png", "enemy");
      inputs.BindKey(KeyEvent.VK_A, 0);
      inputs.BindKey(KeyEvent.VK_D, 1);
      inputs.BindKey(KeyEvent.VK_SPACE, 2);
      inputs.BindKey(KeyEvent.VK_R, 3);

      p= new Player(); 
      e= new Enemy();
   }
   

   public boolean Update(long timeStamp) {
		inputs.AcceptInputs();
		Random random = new Random();
		
		
		
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
			if (inputs.buttons[3].isPressed == true) {        // r을 누르면 시작하기 전으로 돌아가기
				state = GameState.Started;
				p=resetPlayer(p);
				break;
			}
			break;

		}
		
		int speed = 10; // 좌 우로 움직이는 속도 조절
		int rightMax = 350; // 우측으로의 최대 창 가로 크기 - 공 크기
		int enemySpeed=10; // 적이 내려오는 속도
		if(state == GameState.Running)                       //게임을 시작하면 장애물이 내려옴
		{e.y += enemySpeed; // 적이 내려오는 속도
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

   public void Draw(long timeStamp)
   {
      BeginDraw();
      ClearScreen();
      DrawString(10, 50, " 공이 좌우로 움직입니다  ");
      if (checkCrash(p,e)) DrawString(10, 200, " 충돌했어요 "); // 충돌시 잠시 충돌 했다고 출력
      p.Draw(g);
      e.Draw(g);
      EndDraw();
   }

   @Override
   public boolean Initialize() {
      // TODO Auto-generated method stub
      return true;
   }

}
