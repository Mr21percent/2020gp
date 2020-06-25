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
		Normal, // �ƹ� ��ư�� �� ����
		Left, // ����
		Right // ������
	}

	enum GameState {
		Started, // ������ ���� ���۵��� ���� ����
		Ready, // �����̽��ٸ� ���� ����
		Running, // ������ ���۵� ����
		Finished // ���� ����
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

		public Player() // ����� ĳ�����Դϴ�.
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
		public Item() // ����� ĳ�����Դϴ�.
		{	state = ItemState.none;
			x = 0;
			y = 0;
			width = 30;
			height = 30;
			image = images.GetImage("player");

		}
	}

	static final int Enemy_width = 50; // ��ֹ��� ���� ����(������ �ȼ�)
	static final int Enemy_heigth = 100; // ��ֹ��� ���� ����(������ �ȼ�)

	class Enemy extends DrawableObject {

		public double e_x;
		public double e_y;

		public Enemy(int x, int y) { // ��ֹ� ������
			super(x, y, Enemy_width, Enemy_heigth, images.GetImage("enemy"));
			e_x = x;
			e_y = y;
		}
	}

	class BackgroundRoad extends DrawableObject { // ����� �������� ���� �߰���
		public BackgroundRoad() {
			x = 0;
			y = 0;
			width = 400;
			height = 620;
			image = images.GetImage("road");
		}
	}

	public boolean checkCrash(Player p, Enemy e) { // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
		if (p.x + p.width >= e.x && p.x <= e.x + e.width)
			if (p.y + p.height >= e.y && p.y <= e.y + e.height)
				return true;
		return false;
	}
	public boolean checkGetItem(Player p, Item i) { // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
		if (p.x + p.width >= i.x && p.x <= i.x + i.width)
			if (p.y + p.height >= i.y && p.y <= i.y + i.height)
				return true;
		return false;
	}

	public Player resetPlayer(Player p) { // �÷��̾� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�.
		p.x = 150;
		p.y = 500;
		p.width = 50;
		p.height = 50;
		return p;
	}
	
	public BackgroundRoad resetBackgroundRoad(BackgroundRoad e, int ynum) { // ��� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�. 
		 e.x = 0; e.y = ynum;
		 return e; }
	
	 

	public void OneGamePlayTime(long time) { // �̹� ������ �÷��� �ð� ����� ����ϴ� �Լ��Դϴ�.
		if (timeStamp_firstFrame == 0) // �̹��� ù �������̾��ٸ� ���� �ð� ���
			timeStamp_firstFrame = time;
		timeStamp_lastFrame = time; // ���� '���� ������'�� �� �̹� �������� ���� �ð� ���
		PlayTime = (int) (timeStamp_lastFrame - timeStamp_firstFrame) / 1000; // �÷��� �ð� ����

		if (PlayTime % 60 == 0 && OneSec != 0 && PlayTime != 0) { // ��,�� ���
			OneMin++;
			OneSec = 0;
		} else {
			OneSec = PlayTime % 60;
		}
	}

	
	public void BestGamePlayTime() { // BestGame�� �����ϴ� �޼ҵ��Դϴ�.
		System.out.println("������ �ְ� �ð��� : "+BestTime);
		if (BestTime < PlayTime) {
			
			BestTime = PlayTime;
			
			if (BestTime >= 60) { // ��,�� ��� 
				BestMin = BestTime / 60;
				BestSec = BestTime % 60;
			} else {
				BestSec = BestTime % 60;
			}
		}
	}

	String filename_save = "besttime.txt"; // BestGame�� �ð��� �����ϴ� �����Դϴ�.

	public void SaveBestGamePlayTime() // BestGame�� �ð��� �����ϴ� �޼ҵ��Դϴ�. �� �޼ҵ�� Running�� ����Ǳ� ������ �ҷ��ɴϴ�.
	{
		PrintStream ps;
		try {
			ps = new PrintStream(filename_save);
			ps.println(BestTime);
			ps.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void LoadBestGamePlayTime() // ���� ������ BestPlayTime�� �ҷ����� �޼ҵ��Դϴ�. �� �޼ҵ�� ������ ���۵Ǳ� ��, ���α׷��� ���۵� �� �ѹ��� �ҷ��ɴϴ�.
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
		if (BestTime >= 60) { // ��,�� ��� 
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
	long timeStamp_firstFrame = 0; // ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0; // ���� �������� timeStamp -> ������ ��꿡 ���
	int PlayTime = 0; // �÷��� �ð��� ���ϰ� �ֽ��ϴ�
	int OneSec = 0; // �÷��� �ð� �ʴ���
	int OneMin = 0; // �÷��� �ð� �д���
	int BestTime; // �ְ� ��� �� �ǹ��մϴ�
	int BestSec; // �ְ� ��� �ʸ� �ǹ��մϴ�
	int BestMin; // �ְ� ��� ���� �ǹ��մϴ�
	int enemySpeed=10; // ��ֹ��� ���ǵ带 �ǹ��մϴ�.
	int numberOfEnemys = 5; // ��ֹ��� ����
	int timeForNando=0;
	public BeforeNine state1=BeforeNine.no;
	Enemy[] enemys = new Enemy[numberOfEnemys]; // ��ֹ��� ����ִ� �迭

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
			enemys[i] = new Enemy(x, -300 * i); // ��ֹ��� x�ڸ� ����, y�ڸ� -100���� �����˴ϴ�.

		}
		
		return true;
	}

	public boolean Update(long timeStamp) {
		inputs.AcceptInputs();
		Random random = new Random();

		switch (state) {
		case Started:

		case Ready:
			if (inputs.buttons[2].IsPressedNow() == true) // space �� ������ ���ӽ���
				state = GameState.Running;
			break;

		case Running:
			OneGamePlayTime(timeStamp);
			p.state = PlayerState.Normal;
			for (int i = 0; i < numberOfEnemys; ++i) {
				if (checkCrash(p, enemys[i]) == true) // ��ֹ��� �ε����� ���� ����
				{
					BestGamePlayTime(); // ����Ʈ �÷��̸� �����մϴ�.
					SaveBestGamePlayTime(); // ����Ʈ �÷��̸� �����մϴ�.
					audios.Play("sample");
					state = GameState.Finished;
					item.state=ItemState.none;
				}
			}
			if ((inputs.buttons[0].isPressed == true) && (inputs.buttons[1].isPressed == true)){ // a�� ������ �������� �̵�
				p.state = PlayerState.Normal;
				break;
			}
			
			if (inputs.buttons[0].isPressed == true) { // a�� ������ �������� �̵�
				p.state = PlayerState.Left;
				break;
			}
			else if (inputs.buttons[1].isPressed == true) { // d�� ������ ���������� �̵�
				p.state = PlayerState.Right;
				break;
			}
			else {
				p.state=PlayerState.Normal;
				break;
			}
			
		case Finished:
			p.state = PlayerState.Normal; // ������ ������ ���� ����
			if (inputs.buttons[4].isPressed == true) {  //f�� ������  ����ƮŸ���� �����մϴ�.
				BestTime = 0;
				BestMin = 0;
				BestSec = 0;
				SaveBestGamePlayTime(); // ����Ʈ �÷��̸� �����մϴ�.
			}
			if (inputs.buttons[3].isPressed == true) { // r�� ������ �����ϱ� ������ ���ư���
				state = GameState.Started;
				p = resetPlayer(p);
				bg1=resetBackgroundRoad(bg1, 10);
				bg2=resetBackgroundRoad(bg2, -590);
				Initialize();
				timeStamp_firstFrame = 0; // �ð� �ʱ�ȭ
				timeStamp_lastFrame = 0;
				OneMin = 0;
				OneSec = 0;
				enemySpeed=10;
				break;
			}
			break;

		}

		int speed = 10; // �� ��� �����̴� �ӵ� ����
		int rightMax = 350; // ���������� �ִ� â ���� ũ�� - �� ũ��
		
		
		if ((item.state==ItemState.exsist) && (checkGetItem(p, item)==true)) {
			if (enemySpeed>4)
				enemySpeed-=1;
			item.state=ItemState.none;
		}
		if (state == GameState.Running) // ������ �����ϸ� ��ֹ��� ������s
		{	if (PlayTime%10==9)
				state1=BeforeNine.yes;
			else if ((state1==BeforeNine.yes) && (PlayTime % 10 == 0)){
				enemySpeed+=2;
				state1=BeforeNine.no;
			}
			 // 10�ʿ� �ӵ� nando�� ����
			
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

				// �� ��ֹ��� 150 �̻� ������ ������ �ִ��� Ȯ���� �� ���ο� ��ֹ��� ����մϴ�.
				if (i == 0 && enemys[i].e_y <= enemys[numberOfEnemys - 1].e_y && enemys[numberOfEnemys - 1].e_y < 150)
					enemys[0].e_y = -100;
				else if (i != 0 && enemys[i].e_y <= enemys[i - 1].e_y && enemys[i - 1].e_y < 150)
					enemys[i].e_y = -100;
				else
					enemys[i].e_y += enemySpeed; // ��ֹ��� �������� �ӵ��� ���̵��� ����մϴ�.

				enemys[i].x = (int) enemys[i].e_x; // ����� ���� x�� �����մϴ�.
				enemys[i].y = (int) enemys[i].e_y; // ����� ���� y�� �����մϴ�.
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

		switch (p.state) { // ĳ������ �������� ����մϴ�.
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
			DrawString(10, 30, "Space�� ���� ������ �����մϴ�  ");
			DrawString(10, 50, "a, d�� ���� �¿�� �����̼���");
			DrawString(10, 70, "BestPlay : %4d: %4d", BestMin, BestSec);
			break;
		case Running:
			bg1.Draw(g);
			bg2.Draw(g);
			if (PlayTime != 0 && PlayTime % 10 == 0) { // 10�ʸ��� ���� �� ǥ��
				DrawString(130, 90, "(Level Up!)");
			}
			DrawString(50, 30, "�ִ��� ���� ���ߺ�����  ");
			DrawString(50, 50, "���ӽ���");
			DrawString(50, 70, "Time %4d: %4d", OneMin, OneSec);
			DrawString(50, 90, "Speed : %d", enemySpeed);
			if(item.state==ItemState.exsist)
				item.Draw(g);
			p.Draw(g);
			for (int i = 0; i < numberOfEnemys; ++i) { // ��� ��ֹ��� ����մϴ�.
				enemys[i].Draw(g);
			}

			break;
		case Finished:
			bg1.Draw(g);
			bg2.Draw(g);
			p.Draw(g);
			for (int i = 0; i < numberOfEnemys; ++i) { // ��� ��ֹ��� ����մϴ�.
				enemys[i].Draw(g);
			}  
			DrawString(50, 30, "�浹�߾�� "); // �浹�� ��� �浹 �ߴٰ� ���
			DrawString(50, 70, "Time %2d : %2d", OneMin, OneSec);
			DrawString(50, 90, "BestPlay : %2d: %2d", BestMin, BestSec);
			DrawString(50, 150, "F�� ������ BestPlay�� �ʱ�ȭ �˴ϴ�.");
			DrawString(50, 170, "R�� ���� �ٽ� ����  ");   
			break;
		}
		EndDraw();
	}

}

