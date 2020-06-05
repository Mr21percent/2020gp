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
		Normal,		//�ƹ� ��ư�� �� ����
		Left,	//����
		Right	//������
	}
	enum GameState {
		Started, // ������ ���� ���۵��� ���� ����
		Ready, // �����̽��ٸ� ���� ����
		Running, // ������ ���۵� ����
		Finished // ���� ����	
		}

   class Player extends DrawableObject
   {
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
   
   public boolean checkCrash(Player p, Enemy e) {  // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
		if (p.x + p.width > e.x && p.x < e.x + e.width)
			if (p.y + p.height > e.y && p.y < e.y + e.height)
				return true;
	   	return false;
	}
   public Player resetPlayer(Player p) {  // �÷��̾� ��ġ�� �ʱ�ȭ�� ����ϴ� �Լ��Դϴ�.
	    p.x = 300;
		p.y = 500;
		p.width = 50;
		p.height = 50;
	   	return p;
	}

   	Player p;
	Enemy e;
	GameState state = GameState.Started;
	long startTime_pressing;								//���콺 �����̽��ٸ� ������ ������ �ð�
	long timeStamp_firstFrame = 0;							//ù �������� timeStamp -> ���� ���ķ� ����� �ð� ��꿡 ���
	long timeStamp_lastFrame = 0;							//���� �������� timeStamp -> ������ ��꿡 ���
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
			if (inputs.buttons[3].isPressed == true) {        // r�� ������ �����ϱ� ������ ���ư���
				state = GameState.Started;
				p=resetPlayer(p);
				break;
			}
			break;

		}
		
		int speed = 10; // �� ��� �����̴� �ӵ� ����
		int rightMax = 350; // ���������� �ִ� â ���� ũ�� - �� ũ��
		int enemySpeed=10; // ���� �������� �ӵ�
		if(state == GameState.Running)                       //������ �����ϸ� ��ֹ��� ������
		{e.y += enemySpeed; // ���� �������� �ӵ�
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

   public void Draw(long timeStamp)
   {
      BeginDraw();
      ClearScreen();
      DrawString(10, 50, " ���� �¿�� �����Դϴ�  ");
      if (checkCrash(p,e)) DrawString(10, 200, " �浹�߾�� "); // �浹�� ��� �浹 �ߴٰ� ���
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
