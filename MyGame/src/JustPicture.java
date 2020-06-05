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
			width = 100;
			height = 30;
			
			image = images.GetImage("player");
		}
	}
   
   public boolean checkCrash(Player p, Enemy e) {  // �浹�� Ȯ���ϴ� �Լ��Դϴ�.
	   	if (p.x+p.width/2<e.x+e.width && p.x+p.width/2>e.x)
	   		if (p.y+p.height/2<e.y+e.height && p.y+p.height/2>e.y)
	   				return true;
	   	return false;
	}
   Player p;
   Enemy e;
   public JustPicture(GameFrameSettings settings)
   {
      super(settings);
      
      images.LoadImage("Images/ball.png", "player");
      inputs.BindKey(KeyEvent.VK_A, 0);
      inputs.BindKey(KeyEvent.VK_D, 1);
      inputs.BindKey(KeyEvent.VK_SPACE, 2);
      p= new Player(); 
      e= new Enemy();
   }
   

   @Override
   public boolean Update(long timeStamp)
   {
      inputs.AcceptInputs();
      if ( inputs.buttons[0].isPressed == true)
			p.state = PlayerState.Left;
		
      else if ( inputs.buttons[1].isPressed == true )
			p.state = PlayerState.Right;
      Random random=new Random();
      e.y+=5; // ���� �������� �ӵ�
      if(e.y>600) {
    	  e.y=0;
    	  e.x=random.nextInt(4)*100;
      }
      int speed=10; // �� ��� �����̴� �ӵ� ����
      int rightMax=350; // ���������� �ִ� â ���� ũ�� - �� ũ��
      switch ( p.state )
		{
		case Normal:
			break;
		case Left:
			p.x-=speed; 
			if (p.x<0) p.x+=speed;
			break;
		case Right:
			p.x+=speed;
			if (p.x>rightMax) p.x-=speed; 
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
