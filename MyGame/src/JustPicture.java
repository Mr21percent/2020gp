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
			x = 300;
			y = 500;
			width = 50;
			height = 50;
			image = images.GetImage("player");

		}
	}

	class Enemy extends DrawableObject {
		public Enemy() {
			x = 200;
			y = 0;
			width = 40;
			height = 80;
			// public int hp=3; ���� ü���� ���ؼ� �ӽ÷� ������ ���Դϴ�.

			image = images.GetImage("enemy");
		}
	}

	/*
	 * class Bullet extends DrawableObject { public Bullet() { x = 300; y = 400;
	 * width = 10; height = 10;
	 * 
	 * image = images.GetImage("bullet"); }
	 * 
	 * }
	 */

	public boolean checkCrash(Player p, Enemy e) { // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
		if (p.x + p.width > e.x && p.x < e.x + e.width)
			if (p.y + p.height > e.y && p.y < e.y + e.height)
				return true;
		return false;
	}

	/*
	 * public boolean checkhit(Bullet b, Enemy e) { // źȯ ������ Ȯ���ϴ� �Լ��Դϴ�. if (b.x +
	 * b.width > e.x && b.x < e.x + e.width) if (b.y + b.height > e.y && b.y < e.y +
	 * e.height) return true; return false; }
	 */
	public Player resetPlayer(Player p) { // �÷��̾� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�.
		p.x = 300;
		p.y = 500;
		p.width = 50;
		p.height = 50;
		return p;
	}

	public Enemy resetEnemy(Enemy e) { // �÷��̾� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�.
		e.x = -40;
		e.y = -80;
		e.width = 40;
		e.height = 80;
		return e;
	}

	Player p;
	Enemy e;
//	Bullet b;
	GameState state = GameState.Started;
	long startTime_pressing; // ���콺 �����̽��ٸ� ������ ������ �ð�
	long timeStamp_firstFrame = 0; // ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0; // ���� �������� timeStamp -> ������ ��꿡 ���
	long endTime_crashed; //�浹�� ������ ���� �ð�
	long nowTime;
	public JustPicture(GameFrameSettings settings) {
		super(settings);

		images.LoadImage("Images/ball.png", "player");
		images.LoadImage("Images/ball.png", "bullet");
		images.LoadImage("Images/car.png", "enemy");
		inputs.BindKey(KeyEvent.VK_A, 0);
		inputs.BindKey(KeyEvent.VK_D, 1);
		inputs.BindKey(KeyEvent.VK_SPACE, 2);
		inputs.BindKey(KeyEvent.VK_R, 3);
		inputs.BindKey(KeyEvent.VK_W, 4);

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
			    startTime_pressing= timeStamp;
			break;

		case Running:
			nowTime=timeStamp;
			p.state = PlayerState.Normal;
			
			if (inputs.buttons[0].isPressed == true) { // a�� ������ �������� �̵�
				p.state = PlayerState.Left;
				break;
			}
			if (inputs.buttons[1].isPressed == true) { // d�� ������ ���������� �̵�
				p.state = PlayerState.Right;
				break;
			}
			/*
			 * if (inputs.buttons[4].isPressed == true) { // w�� ������ �Ѿ��� �߻� b=new Bullet();
			 * b.x=p.x; b.y=p.y; break; }
			 */
			if (checkCrash(p, e) == true) { // ��ֹ��� �ε����� ���� ����
				state = GameState.Finished;
				endTime_crashed= timeStamp;
				}
			break;

		case Finished:
			if (inputs.buttons[3].isPressed == true) { // r�� ������ �����ϱ� ������ ���ư���
				state = GameState.Started;
				p = resetPlayer(p);
				e = resetEnemy(e);
				break;
			}
			break;

		}

		int speed = 10; // �� ��� �����̴� �ӵ� ����
		int rightMax = 350; // ���������� �ִ� â ���� ũ�� - �� ũ��
		int enemySpeed = 10; // ���� �������� �ӵ�
		int nando=1; //1�ʿ� �����ϴ� enemySpeed
		if (state == GameState.Running) // ������ �����ϸ� ��ֹ��� ������
		{	enemySpeed+=nando*(nowTime-startTime_pressing)/1000; //1�ʿ� �ӵ� nando�� ����
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
			DrawString(10, 50, " %d �и��� ��Ƽ�� ��ó׿�",nowTime-startTime_pressing);
			p.Draw(g);
			e.Draw(g);
			break;
		case Finished:
			DrawString(10, 70, " �浹�߾�� "); // �浹�� ��� �浹 �ߴٰ� ���
			DrawString(10,90, "%d �и��� ��Ƽ�̳׿�",endTime_crashed-startTime_pressing); //��ƾ �ð� ���
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
