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
		Normal,		//아무 버튼도 안 누름
		Left,	//왼쪽
		Right	//오른쪽
	}


   class Player extends DrawableObject
   {
	  public PlayerState state;
      public Player() // 사용자 캐릭터입니다.
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
   
   public boolean checkCrash(Player p, Enemy e) {  // 충돌을 확인하는 함수입니다.
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
      e.y+=5; // 적이 내려오는 속도
      if(e.y>600) {
    	  e.y=0;
    	  e.x=random.nextInt(4)*100;
      }
      int speed=10; // 좌 우로 움직이는 속도 조절
      int rightMax=350; // 우측으로의 최대 창 가로 크기 - 공 크기
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
      DrawString(10, 50, " 공이 좌우로 움직입니다  ");
      if (checkCrash(p,e)) DrawString(10, 200, " 충돌했어요 "); // 충돌시 잠시 충돌 했다고 출력
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
