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
			height = 1200;
			image = images.GetImage("road");
		}
	}

	public boolean checkCrash(Player p, Enemy e) { // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
		if (p.x + p.width >= e.x && p.x <= e.x + e.width)
			if (p.y + p.height >= e.y && p.y <= e.y + e.height)
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

	 public BackgroundRoad resetBackgroundRoad(BackgroundRoad e) { // �÷��̾� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�. 
	 e.x = 0; e.y = -600; e.width = 400; e.height = 1200;
	 return e; }
	 

	public void OneGamePlayTime(long time) { // �̹� ������ �÷��� �ð� ����� ����ϴ� �Լ��Դϴ�.
		if (timeStamp_firstFrame == 0) // �̹��� ù �������̾��ٸ� ���� �ð� ���
			timeStamp_firstFrame = time;
		timeStamp_lastFrame = time; // ���� '���� ������'�� �� �̹� �������� ���� �ð� ���
		PlayTime = (int) (timeStamp_lastFrame - timeStamp_firstFrame) / 1000; // �÷��� �ð� ����

		if (PlayTime % 10 == 0 && OneSec != 0 && PlayTime != 0) { // ��,�� ��� // OneMin�� �����ϴ� ���� Ȯ���ϱ� ���� ���Ƿ� 10������
																	// ǥ���߽��ϴ�.=> �����Ҷ� 60 ������ ����
			OneMin++;
			OneSec = 0;
		} else {
			OneSec = PlayTime % 10;
		}
	}

	public void BestGamePlayTime() { // BestGame�� �����ϴ� �޼ҵ��Դϴ�.
		System.out.println("������ �ְ� �ð��� : "+BestTime);
		if (BestTime < PlayTime) {
			
			BestTime = PlayTime;
			
			if (BestTime >= 10) { // ��,�� ��� // BestMin�� �����ϴ� ���� Ȯ���ϱ� ���� ���Ƿ� 10������ ǥ���߽��ϴ�.=> ���������Ҷ� 60 ������ ����
				BestMin = PlayTime / 10;
				BestSec = PlayTime % 10;
			} else {
				BestSec = BestTime % 10;
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
		if (BestTime >= 10) { // ��,�� ��� // BestMin�� �����ϴ� ���� Ȯ���ϱ� ���� ���Ƿ� 10������ ǥ���߽��ϴ�.=> ���������Ҷ� 60 ������ ����
			BestMin = BestTime / 10;
			BestSec = BestTime % 10;
		} else {
			BestSec = BestTime % 10;
		}
	}

	Player p;
	Enemy e;
	BackgroundRoad bg;
//	Bullet b;
	GameState state = GameState.Started;
	long timeStamp_firstFrame = 0; // ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0; // ���� �������� timeStamp -> ������ ��꿡 ���
	int PlayTime = 0; // �÷��� �ð��� ���ϰ� �ֽ��ϴ�
	int OneSec = 0; // �÷��� �ð� �ʴ���
	int OneMin = 0; // �÷��� �ð� �д���
	int BestTime; // �ְ� ��� �� �ǹ��մϴ�
	int BestSec; // �ְ� ��� �ʸ� �ǹ��մϴ�
	int BestMin; // �ְ� ��� ���� �ǹ��մϴ�
	int enemySpeed; // ��ֹ��� ���ǵ带 �ǹ��մϴ�.
	int numberOfEnemys = 5; // ��ֹ��� ����
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
		bg = new BackgroundRoad();
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
				}
			}
			if (inputs.buttons[0].isPressed == true) { // a�� ������ �������� �̵�
				p.state = PlayerState.Left;
				break;
			}
			if (inputs.buttons[1].isPressed == true) { // d�� ������ ���������� �̵�
				p.state = PlayerState.Right;
				break;
			}

			break;

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
				Initialize();
				timeStamp_firstFrame = 0; // �ð� �ʱ�ȭ
				timeStamp_lastFrame = 0;
				OneMin = 0;
				OneSec = 0;
				break;
			}
			break;

		}

		int speed = 10; // �� ��� �����̴� �ӵ� ����
		int rightMax = 350; // ���������� �ִ� â ���� ũ�� - �� ũ��
		enemySpeed = 10; // ���� �������� �ӵ�
		int nando = 1; // 1�ʿ� �����ϴ� enemySpeed
		if (state == GameState.Running) // ������ �����ϸ� ��ֹ��� ������s
		{
			enemySpeed += nando * PlayTime / 10; // 10�ʿ� �ӵ� nando�� ����

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

			bg.y += enemySpeed * 2;
			if (bg.y > 0) {
				bg.y = -600;
			}
		}

		switch (p.state) { // ĳ������ �������� ����մϴ�.
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

		if (PlayTime != 0 && PlayTime % 10 == 0) { // 10�ʸ��� ���� �� ǥ��
			DrawString(80, 90, "(Level Up!)");
		}

		switch (state) {
		case Started:
		case Ready:
			DrawString(10, 30, " Space�� ���� ������ �����մϴ�  ");
			DrawString(10, 50, " a, d�� ���� �¿�� �����̼���");
			DrawString(10, 70, "BestPlay : %4d: %4d", BestMin, BestSec);
			break;
		case Running:
			DrawString(10, 30, " �ִ��� ���� ���ߺ�����  ");
			DrawString(10, 50, "   ���ӽ���");
			DrawString(10, 70, "Time %4d: %4d(minute�� �����ϴ� ���� Ȯ���ϱ� ���� �Ͻ��� 10���� ǥ��)", OneMin, OneSec);
			DrawString(10, 90, "Speed : %d", enemySpeed);
			bg.Draw(g);
			p.Draw(g);
			for (int i = 0; i < numberOfEnemys; ++i) { // ��� ��ֹ��� ����մϴ�.
				enemys[i].Draw(g);
			}

			break;
		case Finished:
			
			p.Draw(g);
			for (int i = 0; i < numberOfEnemys; ++i) { // ��� ��ֹ��� ����մϴ�.
				enemys[i].Draw(g);
			}
			DrawString(10, 70, " �浹�߾�� "); // �浹�� ��� �浹 �ߴٰ� ���
			DrawString(10, 140, "Time %2d : %2d", OneMin, OneSec);
			DrawString(10, 160, "BestPlay : %2d: %2d", BestMin, BestSec);
			DrawString(10, 180, "F�� ������ BestPlay�� �ʱ�ȭ �˴ϴ�.");
			DrawString(10, 200, "R�� ���� �ٽ� ����  ");   
			break;
		}
		EndDraw();
	}

}
