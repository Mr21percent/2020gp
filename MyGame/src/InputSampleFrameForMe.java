import java.awt.event.KeyEvent;

import loot.*;
import loot.graphics.DrawableObject;

@SuppressWarnings("serial")
public class InputSampleFrameForMe extends GameFrame
{
   enum GameState
   {
      Started,   //게임이 아직 시작되지 않은 상태
      Ready,      //스페이스 바를 누른 상태, 이제 떼면 게임이 시작됨
      Running,   //게임이 시작된 상태
      Finished,   //충돌로 게임이 끝난 상태 
   }
   enum PlayerState
   {
      Normal,      //아무 버튼도 안 누름
      left,   //왼쪽 a 버튼을 누름
      right   //오른쪽 d 버튼을누름
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
   GameState state = GameState.Started;   //정상적인 경우 Started -> Ready -> Running -> Finished -> Ready -> ...의 순서로 전환됨

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
      DrawString(10, 50, " 이거는 나와야 되는거 아닌가?  ");
      p.Draw(g);

      switch ( state )
      {
      case Started:
         DrawString(10, 72, "다른 키 말고 스페이스 바만 누르면 시작한다.");
         break;
      case Ready:
         DrawString(10, 50, "       스페이스 바를 떼면 시작한다.       ");
         break;
      case Running:
         DrawString(10, 72, "                 시작했다.                 ");
         break;
      case Finished:
         DrawString(10, 72, "     스페이스 바를 누르면 다시 시작한다.   ");
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