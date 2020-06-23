import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import loot.GameFrame;
import loot.GameFrameSettings;
import loot.graphics.DrawableObject;

public class PracticeEnemy extends GameFrame {
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
	static final int Enemy_width = 50;					//장애물의 가로 길이(단위는 픽셀)
	static final int Enemy_height = 100;                //장애물의 세로 길이(단위는 픽셀)
	class Enemy extends DrawableObject {                //장애물 본체
		public double e_x;
		public double e_y;

		public Enemy(int x,int y) {                     //장애물 생성자
			super(x, y,	Enemy_width, Enemy_height, images.GetImage("enemy"));
			e_x = x;
			e_y = y;
		
		}
	}

	
	
	int numberOfEnemys = 5; //장애물의 숫자
	GameState state = GameState.Started;
	int enemySpeed; //장애물의 속도
	Enemy[] enemys = new Enemy[numberOfEnemys]; //장애물이 들어있는 배열

	public PracticeEnemy(GameFrameSettings settings) {
		super(settings);

		images.LoadImage("Images/ball.png", "player");
		images.LoadImage("Images/car.png", "enemy");
		inputs.BindKey(KeyEvent.VK_A, 0);
		inputs.BindKey(KeyEvent.VK_D, 1);
		inputs.BindKey(KeyEvent.VK_SPACE, 2);
		inputs.BindKey(KeyEvent.VK_R, 3);
		inputs.BindKey(KeyEvent.VK_F, 4);

	}
	public boolean Initialize() {  //장애물의 처음 위치를 설정합니다.
		Random random = new Random();
		for (int i = 0; i < enemys.length; ++i) {
			int x = random.nextInt(10)*Enemy_width;
			enemys[i] = new Enemy(x,-200*i);  //장애물의 x자리는 랜덤으로, y자리는 120간격으로 생성됩니다.

		}
		return true;
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

			break;

		case Finished:

			break;
		}
			

		enemySpeed = 10; // 장애물이 내려오는 속도
		int nando = 1; // 1초에 증가하는 enemySpeed
		if (state == GameState.Running) // 게임을 시작하면 장애물이 내려옴s
		{
			enemySpeed += nando *( timeStamp / 10000); // 10초에 속도 nando씩 증가
		
			for (int i = 0; i < numberOfEnemys; ++i) {  //모든 장애물에 대하여 적용합니다.
				enemys[i].e_y += enemySpeed; // 장애물이 내려오는 속도는 난이도를 고려합니다.
				if (enemys[i].e_y > 600) {
					enemys[i].e_y = 0;
					enemys[i].e_x = random.nextInt(10) * Enemy_width;
				}
				enemys[i].x = (int)enemys[i].e_x; //계산한 값을 x에 대입합니다.
				enemys[i].y = (int)enemys[i].e_y; //계산한 값을 y에 대입합니다.
			}
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
			break;
		case Running:
			DrawString(10, 50, "   게임시작");
			for (int i = 0; i < numberOfEnemys; ++i) { //모든 장애물을 출력합니다.
				enemys[i].Draw(g);
			}
			break;
		case Finished:

			break;
		}
		EndDraw();
	}



}
