import java.awt.event.KeyEvent;
import java.util.Random;

import loot.*;
import loot.graphics.*;

@SuppressWarnings("serial")
public class JustPicture2 extends GameFrame {

	enum GameState {
		Started, // ������ ���� ���۵��� ���� ����
		Ready, // �����̽��ٸ� ���� ����
		Running, // ������ ���۵� ����
		Finished // ���� ����
	}

	enum PlayerState {
		Normal, // �ƹ� ��ư�� �� ����
		Left, // ����
		Right // ������

	}

	class Player extends DrawableObject {
		public PlayerState state;

		public Player() // ����� ĳ�����Դϴ�.
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
		public Enemy() { // ��ֹ� �Դϴ�.
			x = 300;
			y = 100;
			width = 100;
			height = 30;

			image = images.GetImage("player");
		}
	}

	public boolean checkCrash(Player p, Enemy e) {          // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
		if (p.x + p.width > e.x && p.x < e.x + e.width)
			if (p.y + p.height > e.y && p.y < e.y + e.height)
				return true;
		return false;
	}

	Player p;
	Enemy e;
	GameState state = GameState.Started;
	long startTime_pressing;								//���콺 �����̽��ٸ� ������ ������ �ð�
	long timeStamp_firstFrame = 0;							//ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0;							//���� �������� timeStamp -> ������ ��꿡 ���

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
	public boolean Initialize() {                            // �۾� �ɼ��� ������ �޼ҵ��Դϴ�.
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean Update(long timeStamp) {
		inputs.AcceptInputs();
		Random random = new Random();
		
		int speed = 10; // �� ��� �����̴� �ӵ� ����
		int rightMax = 350; // ���������� �ִ� â ���� ũ�� - �� ũ��
		
		if(state == GameState.Running)                       //������ �����ϸ� ��ֹ��� ������
		{e.y += 1; // ���� �������� �ӵ�
		if (e.y > 600) {
			e.y = 0;
			e.x = random.nextInt(4) * 100;
		}
		}
		
		switch (state) {
		case Started:
			
		case Ready:
			
			if (inputs.buttons[2].IsPressedNow() == true)    // space �� ������ ���ӽ���
				state = GameState.Running;
			break;

		case Running:

			p.state = PlayerState.Normal;

			if (inputs.buttons[0].isPressed == true) {        // a�� ������ �������� �̵�
				p.state = PlayerState.Left;
				break;
			}
			if (inputs.buttons[1].isPressed == true) {        // d�� ������ ���������� �̵�
				p.state = PlayerState.Right;
				break;
			}
			if (checkCrash(p, e) == true)                     // ��ֹ��� �ε����� ���� ����
				state = GameState.Finished;
			break;

		case Finished:
			break;

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

	public void Draw(long timeStamp)   {
	      BeginDraw();
	      ClearScreen();
	      DrawString(10, 30, " Space�� ���� ������ �����մϴ�  ");
	      DrawString(10, 50, " ���� �¿�� �����Դϴ�  ");
	      if (checkCrash(p,e)) DrawString(10, 200, " �浹�߾�� "); // �浹�� ��� �浹 �ߴٰ� ���
	      p.Draw(g);
	      e.Draw(g);
	      EndDraw();
	   }

}
