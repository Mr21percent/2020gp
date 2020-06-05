import java.awt.event.KeyEvent;

import loot.*;
import loot.graphics.DrawableObject;

@SuppressWarnings("serial")
public class InputSampleFrameForMe extends GameFrame
{
   enum GameState
   {
      Started,   //������ ���� ���۵��� ���� ����
      Ready,      //�����̽� �ٸ� ���� ����, ���� ���� ������ ���۵�
      Running,   //������ ���۵� ����
      Finished,   //�浹�� ������ ���� ���� 
   }
   enum PlayerState
   {
      Normal,      //�ƹ� ��ư�� �� ����
      left,   //���� a ��ư�� ����
      right   //������ d ��ư������
   }

   class Player extends DrawableObject
   {
      public PlayerState state;
      public Player()
      {
         state = PlayerState.Normal;
         x = 175;
         y = 25;
         width = 50;
         height = 50;
         image = images.GetImage("player");
      }
   }
   Player p;
   GameState state = GameState.Started;   //�������� ��� Started -> Ready -> Running -> Finished -> Ready -> ...�� ������ ��ȯ��

   public InputSampleFrameForMe(GameFrameSettings settings)
   {
      super(settings);
      
      images.LoadImage("Images/ball.png", "player");
      inputs.BindKey(KeyEvent.VK_A, 0);
      inputs.BindKey(KeyEvent.VK_D, 1);
      inputs.BindKey(KeyEvent.VK_SPACE, 2);
   }

   @Override
   public boolean Update(long timeStamp)
   {
      inputs.AcceptInputs();
      
      switch ( state )
      {
      case Ready:
         if ( inputs.buttons[4].IsReleasedNow() == true )
            state = GameState.Running;
         break;
      case Running:
         p.state = PlayerState.Normal;
         if ( inputs.buttons[0].isPressed == true) {
            p.state = PlayerState.left;
            p.x=p.x-100;
         }
         if ( inputs.buttons[1].isPressed == true ) {
            p.state = PlayerState.right;
            p.y=p.y+100;
         }
         break;
      case Started:
      case Finished:
      }
      return true;
   }

   @Override
   public void Draw(long timeStamp)
   {
      BeginDraw();
      ClearScreen();
      DrawString(10, 50, " �̰Ŵ� ���;� �Ǵ°� �ƴѰ�?  ");
      p.Draw(g);

      switch ( state )
      {
      case Started:
         DrawString(10, 72, "�ٸ� Ű ���� �����̽� �ٸ� ������ �����Ѵ�.");
         break;
      case Ready:
         DrawString(10, 50, "       �����̽� �ٸ� ���� �����Ѵ�.       ");
         break;
      case Running:
         DrawString(10, 72, "                 �����ߴ�.                 ");
         break;
      case Finished:
         DrawString(10, 72, "     �����̽� �ٸ� ������ �ٽ� �����Ѵ�.   ");
         break;
      }

      EndDraw();
   }

   @Override
   public boolean Initialize() {
      // TODO Auto-generated method stub
      return true;
   }

}