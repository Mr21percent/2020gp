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
	static final int Enemy_width = 50;					//��ֹ��� ���� ����(������ �ȼ�)
	static final int Enemy_height = 100;                //��ֹ��� ���� ����(������ �ȼ�)
	class Enemy extends DrawableObject {                //��ֹ� ��ü
		public double e_x;
		public double e_y;

		public Enemy(int x,int y) {                     //��ֹ� ������
			super(x, y,	Enemy_width, Enemy_height, images.GetImage("enemy"));
			e_x = x;
			e_y = y;
		
		}
	}

	
	
	int numberOfEnemys = 5; //��ֹ��� ����
	GameState state = GameState.Started;
	int enemySpeed; //��ֹ��� �ӵ�
	Enemy[] enemys = new Enemy[numberOfEnemys]; //��ֹ��� ����ִ� �迭

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
	public boolean Initialize() {  //��ֹ��� ó�� ��ġ�� �����մϴ�.
		Random random = new Random();
		for (int i = 0; i < enemys.length; ++i) {
			int x = random.nextInt(10)*Enemy_width;
			enemys[i] = new Enemy(x,-200*i);  //��ֹ��� x�ڸ��� ��������, y�ڸ��� 120�������� �����˴ϴ�.

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

			break;

		case Finished:

			break;
		}
			

		enemySpeed = 10; // ��ֹ��� �������� �ӵ�
		int nando = 1; // 1�ʿ� �����ϴ� enemySpeed
		if (state == GameState.Running) // ������ �����ϸ� ��ֹ��� ������s
		{
			enemySpeed += nando *( timeStamp / 10000); // 10�ʿ� �ӵ� nando�� ����
		
			for (int i = 0; i < numberOfEnemys; ++i) {  //��� ��ֹ��� ���Ͽ� �����մϴ�.
				enemys[i].e_y += enemySpeed; // ��ֹ��� �������� �ӵ��� ���̵��� ����մϴ�.
				if (enemys[i].e_y > 600) {
					enemys[i].e_y = 0;
					enemys[i].e_x = random.nextInt(10) * Enemy_width;
				}
				enemys[i].x = (int)enemys[i].e_x; //����� ���� x�� �����մϴ�.
				enemys[i].y = (int)enemys[i].e_y; //����� ���� y�� �����մϴ�.
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
			DrawString(10, 30, " Space�� ���� ������ �����մϴ�  ");
			break;
		case Running:
			DrawString(10, 50, "   ���ӽ���");
			for (int i = 0; i < numberOfEnemys; ++i) { //��� ��ֹ��� ����մϴ�.
				enemys[i].Draw(g);
			}
			break;
		case Finished:

			break;
		}
		EndDraw();
	}



}
