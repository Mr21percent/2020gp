import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import loot.GameFrame;
import loot.GameFrameSettings;

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
	enum ItemState{
		exsist,
		none
	}
	enum BeforeNine{
		yes,
		no
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
	class Item extends DrawableObject {
		public ItemState state;
		public Item() // 사용자 캐릭터입니다.
		{	state = ItemState.none;
			x = 0;
			y = 0;
			width = 30;
			height = 30;
			image = images.GetImage("player");

		}
	}

	static final int Enemy_width = 50; // 장애물의 가로 길이(단위는 픽셀)
	static final int Enemy_heigth = 100; // 장애물의 세로 길이(단위는 픽셀)

	class Enemy extends DrawableObject {

		public double e_x;
		public double e_y;

		public Enemy(int x, int y) { // 장애물 생성자
			super(x, y, Enemy_width, Enemy_heigth, images.GetImage("enemy"));
			e_x = x;
			e_y = y;
		}
	}

	class BackgroundRoad extends DrawableObject { // 배경의 움직임을 위해 추가됨
		public BackgroundRoad() {
			x = 0;
			y = 0;
			width = 400;
			height = 620;
			image = images.GetImage("road");
		}
	}

	public boolean checkCrash(Player p, Enemy e) { // 충돌을 확인하는 함수입니다.
		if (p.x + p.width >= e.x && p.x <= e.x + e.width)
			if (p.y + p.height >= e.y && p.y <= e.y + e.height)
				return true;
		return false;
	}
	public boolean checkGetItem(Player p, Item i) { // 충돌을 확인하는 함수입니다.
		if (p.x + p.width >= i.x && p.x <= i.x + i.width)
			if (p.y + p.height >= i.y && p.y <= i.y + i.height)
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
	
	public BackgroundRoad resetBackgroundRoad(BackgroundRoad e, int ynum) { // 배경 위치의 초기화를 담당하는 함수입니다. 
		 e.x = 0; e.y = ynum;
		 return e; }
	
	 

	public void OneGamePlayTime(long time) { // 이번 게임의 플레이 시간 계산을 담당하는 함수입니다.
		if (timeStamp_firstFrame == 0) // 이번이 첫 프레임이었다면 시작 시각 기록
			timeStamp_firstFrame = time;
		timeStamp_lastFrame = time; // 이제 '직전 프레임'이 될 이번 프레임의 시작 시각 기록
		PlayTime = (int) (timeStamp_lastFrame - timeStamp_firstFrame) / 1000; // 플레이 시간 저장

		if (PlayTime % 60 == 0 && OneSec != 0 && PlayTime != 0) { // 분,초 계산
			OneMin++;
			OneSec = 0;
		} else {
			OneSec = PlayTime % 60;
		}
	}

	
	public void BestGamePlayTime() { // BestGame을 갱신하는 메소드입니다.
		System.out.println("갱신전 최고 시간은 : "+BestTime);
		if (BestTime < PlayTime) {
			
			BestTime = PlayTime;
			
			if (BestTime >= 60) { // 분,초 계산 
				BestMin = BestTime / 60;
				BestSec = BestTime % 60;
			} else {
				BestSec = BestTime % 60;
			}
		}
	}

	String filename_save = "besttime.txt"; // BestGame의 시간을 저장하는 파일입니다.

	public void SaveBestGamePlayTime() // BestGame의 시간을 저장하는 메소드입니다. 이 메소드는 Running이 종료되기 직전에 불러옵니다.
	{
		PrintStream ps;
		try {
			ps = new PrintStream(filename_save);
			ps.println(BestTime);
			ps.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void LoadBestGamePlayTime() // 지난 게임의 BestPlayTime을 불러오는 메소드입니다. 이 메소드는 게임이 시작되기 전, 프로그램이 시작된 후 한번만 불러옵니다.
	{
		FileInputStream is;
		PlayTime = 0;

		try {
			is = new FileInputStream(filename_save);
			Scanner scanner = new Scanner(is);
			BestTime = scanner.nextInt();
			scanner.close();
		} catch (FileNotFoundException e) {
			BestTime = PlayTime;
		}
		if (BestTime >= 60) { // 분,초 계산 
			BestMin = BestTime / 60;
			BestSec = BestTime % 60;
		} else {
			BestSec = BestTime % 60;
		}
	}
	
	Player p;
	Enemy e;
	BackgroundRoad bg1;
	BackgroundRoad bg2;
	Item item;
	
	GameState state = GameState.Started;
	long timeStamp_firstFrame = 0; // 첫 프레임의 timeStamp -> 실행 이후로 경과된 시간 계산에 사용
	long timeStamp_lastFrame = 0; // 직전 프레임의 timeStamp -> 물리량 계산에 사용
	int PlayTime = 0; // 플레이 시간을 말하고 있습니다
	int OneSec = 0; // 플레이 시간 초단위
	int OneMin = 0; // 플레이 시간 분단위
	int BestTime; // 최고 기록 을 의미합니다
	int BestSec; // 최고 기록 초를 의미합니다
	int BestMin; // 최고 기록 분을 의미합니다
	int enemySpeed=10; // 장애물의 스피드를 의미합니다.
	int numberOfEnemys = 5; // 장애물의 숫자
	int timeForNando=0;
	public BeforeNine state1=BeforeNine.no;
	Enemy[] enemys = new Enemy[numberOfEnemys]; // 장애물이 들어있는 배열

	public JustPicture(GameFrameSettings settings) {
		super(settings);

		images.LoadImage("Images/ball.png", "player");
		images.LoadImage("Images/car.png", "enemy");
		images.LoadImage("Images/road.png", "road");
		inputs.BindKey(KeyEvent.VK_A, 0);
		inputs.BindKey(KeyEvent.VK_D, 1);
		inputs.BindKey(KeyEvent.VK_SPACE, 2);
		inputs.BindKey(KeyEvent.VK_R, 3);
		inputs.BindKey(KeyEvent.VK_F, 4);
		audios.LoadAudio("Audios/media.io_sound.wav", "sample", 1);
		p = new Player();
		bg1 = new BackgroundRoad();
		bg2 = new BackgroundRoad();
		bg2.y=-600;
		item = new Item();
		LoadBestGamePlayTime();
	}

	public boolean Initialize() {
		Random random = new Random();
		for (int i = 0; i < enemys.length; ++i) {
			int x = random.nextInt(8) * Enemy_width;
			enemys[i] = new Enemy(x, -300 * i); // 장애물을 x자리 랜덤, y자리 -100으로 생성됩니다.

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
			OneGamePlayTime(timeStamp);
			p.state = PlayerState.Normal;
			for (int i = 0; i < numberOfEnemys; ++i) {
				if (checkCrash(p, enemys[i]) == true) // 장애물에 부딪히면 게임 종료
				{
					BestGamePlayTime(); // 베스트 플레이를 갱신합니다.
					SaveBestGamePlayTime(); // 베스트 플레이를 저장합니다.
					audios.Play("sample");
					state = GameState.Finished;
					item.state=ItemState.none;
				}
			}
			if ((inputs.buttons[0].isPressed == true) && (inputs.buttons[1].isPressed == true)){ // a를 누르면 왼쪽으로 이동
				p.state = PlayerState.Normal;
				break;
			}
			
			if (inputs.buttons[0].isPressed == true) { // a를 누르면 왼쪽으로 이동
				p.state = PlayerState.Left;
				break;
			}
			else if (inputs.buttons[1].isPressed == true) { // d를 누르면 오른쪽으로 이동
				p.state = PlayerState.Right;
				break;
			}
			else {
				p.state=PlayerState.Normal;
				break;
			}
			
		case Finished:
			p.state = PlayerState.Normal; // 게임이 끝나면 공을 멈춤
			if (inputs.buttons[4].isPressed == true) {  //f를 누르면  베스트타임을 리셋합니다.
				BestTime = 0;
				BestMin = 0;
				BestSec = 0;
				SaveBestGamePlayTime(); // 베스트 플레이를 저장합니다.
			}
			if (inputs.buttons[3].isPressed == true) { // r을 누르면 시작하기 전으로 돌아가기
				state = GameState.Started;
				p = resetPlayer(p);
				bg1=resetBackgroundRoad(bg1, 10);
				bg2=resetBackgroundRoad(bg2, -590);
				Initialize();
				timeStamp_firstFrame = 0; // 시간 초기화
				timeStamp_lastFrame = 0;
				OneMin = 0;
				OneSec = 0;
				enemySpeed=10;
				break;
			}
			break;

		}

		int speed = 10; // 좌 우로 움직이는 속도 조절
		int rightMax = 350; // 우측으로의 최대 창 가로 크기 - 공 크기
		
		
		if ((item.state==ItemState.exsist) && (checkGetItem(p, item)==true)) {
			if (enemySpeed>4)
				enemySpeed-=1;
			item.state=ItemState.none;
		}
		if (state == GameState.Running) // 게임을 시작하면 장애물이 내려옴s
		{	if (PlayTime%10==9)
				state1=BeforeNine.yes;
			else if ((state1==BeforeNine.yes) && (PlayTime % 10 == 0)){
				enemySpeed+=2;
				state1=BeforeNine.no;
			}
			 // 10초에 속도 nando씩 증가
			
			if (item.state == ItemState.none) {
				if (random.nextInt(500)==1) {
					item.y = 0;
					item.x = random.nextInt(8) * item.width;
					item.state = ItemState.exsist;
				}
			}
			
			else {
				item.y+=enemySpeed;
				if (item.y>600) item.state=ItemState.none;
				
			}
			
			
			for (int i = 0; i < numberOfEnemys; ++i) {
				if (enemys[i].e_y > 600) {
					enemys[i].e_y = -100;
					enemys[i].e_x = random.nextInt(8) * Enemy_width;
				}

				// 전 장애물과 150 이상 간격이 떨어져 있는지 확인한 후 새로운 장애물을 출력합니다.
				if (i == 0 && enemys[i].e_y <= enemys[numberOfEnemys - 1].e_y && enemys[numberOfEnemys - 1].e_y < 150)
					enemys[0].e_y = -100;
				else if (i != 0 && enemys[i].e_y <= enemys[i - 1].e_y && enemys[i - 1].e_y < 150)
					enemys[i].e_y = -100;
				else
					enemys[i].e_y += enemySpeed; // 장애물이 내려오는 속도는 난이도를 고려합니다.

				enemys[i].x = (int) enemys[i].e_x; // 계산한 값을 x에 대입합니다.
				enemys[i].y = (int) enemys[i].e_y; // 계산한 값을 y에 대입합니다.
			}

			bg1.y += enemySpeed * 2;
			if (bg1.y > 600) {
				bg1.y = -590;
			}
			bg2.y += enemySpeed * 2;
			if (bg2.y > 600) {
				bg2.y = -590;
			}
		}

		switch (p.state) { // 캐릭터의 움직임을 출력합니다.
		case Normal:
			break;
		case Left:
			p.x -= speed;
			if (p.x < 0)
				p.x =0;
			break;
		case Right:
			p.x += speed;
			if (p.x > rightMax)
				p.x=rightMax;
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
			DrawString(10, 30, "Space를 눌러 게임을 시작합니다  ");
			DrawString(10, 50, "a, d를 눌러 좌우로 움직이세요");
			DrawString(10, 70, "BestPlay : %4d: %4d", BestMin, BestSec);
			break;
		case Running:
			bg1.Draw(g);
			bg2.Draw(g);
			if (PlayTime != 0 && PlayTime % 10 == 0) { // 10초마다 레벨 업 표시
				DrawString(130, 90, "(Level Up!)");
			}
			DrawString(50, 30, "최대한 오래 버텨보세요  ");
			DrawString(50, 50, "게임시작");
			DrawString(50, 70, "Time %4d: %4d", OneMin, OneSec);
			DrawString(50, 90, "Speed : %d", enemySpeed);
			if(item.state==ItemState.exsist)
				item.Draw(g);
			p.Draw(g);
			for (int i = 0; i < numberOfEnemys; ++i) { // 모든 장애물을 출력합니다.
				enemys[i].Draw(g);
			}

			break;
		case Finished:
			bg1.Draw(g);
			bg2.Draw(g);
			p.Draw(g);
			for (int i = 0; i < numberOfEnemys; ++i) { // 모든 장애물을 출력합니다.
				enemys[i].Draw(g);
			}  
			DrawString(50, 30, "충돌했어요 "); // 충돌시 잠시 충돌 했다고 출력
			DrawString(50, 70, "Time %2d : %2d", OneMin, OneSec);
			DrawString(50, 90, "BestPlay : %2d: %2d", BestMin, BestSec);
			DrawString(50, 150, "F를 누르면 BestPlay가 초기화 됩니다.");
			DrawString(50, 170, "R을 눌러 다시 시작  ");   
			break;
		}
		EndDraw();
	}

}

