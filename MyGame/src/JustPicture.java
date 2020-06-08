import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

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

	class Enemy extends DrawableObject {
		public Enemy() {
			x = 180;
			y = -80;
			width = 50;
			height = 100;

			image = images.GetImage("enemy");
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

	public Enemy resetEnemy(Enemy e) { // �÷��̾� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�.
		e.x = 0;
		e.y = 0;
		e.width = 50;
		e.height = 100;
		return e;
	}
	public void OneGamePlayTime(long time) { // �÷��� �ð� ����� ����ϴ� �Լ��Դϴ�.
		if (timeStamp_firstFrame == 0) // �̹��� ù �������̾��ٸ� ���� �ð� ���
			timeStamp_firstFrame = time;
		timeStamp_lastFrame = time; // ���� '���� ������'�� �� �̹� �������� ���� �ð� ���
		PlayTime = (int) (timeStamp_lastFrame - timeStamp_firstFrame) / 1000; // �÷��� �ð� ����

		if (PlayTime % 60 == 0 && OneSec != 0 && PlayTime != 0) { // ��,�� ���
			OneMit++;
			OneSec = 0;
		} else {
			OneSec = PlayTime % 60;
		}
	}
	public void BestGamePlayTime() { // ���� �������� ��Ƴ��� ������ �ð��� �����մϴ�. 
		if (BestTime < PlayTime)
			BestTime = PlayTime;
		if (BestTime % 60 == 0 && BestSec != 0 && BestTime != 0) { // ��,�� ���
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
	long timeStamp_firstFrame = 0; // ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0; // ���� �������� timeStamp -> ������ ��꿡 ���
	int PlayTime = 0; //�÷��� �ð��� ���ϰ� �ֽ��ϴ�
	int OneSec = 0; //�÷��� �ð� �ʴ���
	int OneMit = 0; //�÷��� �ð� �д���
	int BestTime; //�ְ� ��� �� �ǹ��մϴ�
	int BestSec; //�ְ� ��� �ʸ� �ǹ��մϴ�
	int BestMit; //�ְ� ��� ���� �ǹ��մϴ�
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

			if (inputs.buttons[2].IsPressedNow() == true) // space �� ������ ���ӽ���
				state = GameState.Running;
			break;

		case Running:
			OneGamePlayTime(timeStamp);
			p.state = PlayerState.Normal;
			if (checkCrash(p, e) == true) // ��ֹ��� �ε����� ���� ����
				state = GameState.Finished;
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
			BestGamePlayTime(); //����Ʈ �÷��� ��
			if (inputs.buttons[3].isPressed == true) { // r�� ������ �����ϱ� ������ ���ư���
				state = GameState.Started;
				p = resetPlayer(p);
				e = resetEnemy(e);
				timeStamp_firstFrame = 0; // �ð� �ʱ�ȭ
				timeStamp_lastFrame = 0;
				OneMit = 0;
				OneSec = 0;
				break;
			}
			break;

		}

		int speed = 10; // �� ��� �����̴� �ӵ� ����
		int rightMax = 350; // ���������� �ִ� â ���� ũ�� - �� ũ��
		int enemySpeed = 10; // ���� �������� �ӵ�
		int nando=1; //1�ʿ� �����ϴ� enemySpeed
		if (state == GameState.Running) // ������ �����ϸ� ��ֹ��� ������
		{	enemySpeed+=nando*(timeStamp_lastFrame-timeStamp_firstFrame)/1000; //1�ʿ� �ӵ� nando�� ����
			e.y += enemySpeed; // ���� �������� �ӵ�
			if (e.y > 600) {
				e.y = 0;
				e.x = random.nextInt(10) * e.width;
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
		switch (state) {
		case Started:
		case Ready:
			DrawString(10, 30, " Space�� ���� ������ �����մϴ�  ");
			DrawString(10, 50, " a, d�� ���� �¿�� �����̼���");
			break;
		case Running:
			DrawString(10, 30, " �ִ��� ���� ���ߺ�����  ");
			DrawString(10, 50, "   ���ӽ���");
			DrawString(10, 70, "Time %4d: %4d", OneMit, OneSec);
			p.Draw(g);
			e.Draw(g);
			break;
		case Finished:
			DrawString(10, 70, " �浹�߾�� "); // �浹�� ��� �浹 �ߴٰ� ���
			DrawString(150, 140, "Time %4d : %4d", OneMit, OneSec);
			DrawString(150, 160, "BestPlay : %4d: %4d", BestMit, BestSec);
			DrawString(150, 180, "R�� ���� �ٽ� ����  ");
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
