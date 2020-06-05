import loot.GameFrame;
import loot.GameFrameSettings;


public class Program
{

	public static void main(String[] args)
	{
		int mode = 1;
		
		if ( mode == 1 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "공을 화면에 띄움";
			settings.canvas_width = 400;
			settings.canvas_height = 600;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS에 해당 - '동시에 키를 입력'하는 상황이 상대적으로 적게 연출됨
			settings.gameLoop_interval_ns = 16666666;		//약 60FPS에 해당
			//settings.gameLoop_interval_ns = 100000000;	//10FPS에 해당 - '동시에 키를 입력'하는 상황이 꽤 자주 연출됨
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 3;
			
			GameFrame window = new JustPicture(settings);
			window.setVisible(true);
		}
		
		if ( mode == 3 )
		{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "공이 왼쪽 오른쪽 움직임";
			settings.canvas_width = 400;
			settings.canvas_height = 600;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS에 해당 - '동시에 키를 입력'하는 상황이 상대적으로 적게 연출됨
			//settings.gameLoop_interval_ns = 16666666;		//약 60FPS에 해당
			settings.gameLoop_interval_ns = 1000;	//1FPS에 해당 - '동시에 키를 입력'하는 상황이 꽤 자주 연출됨
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 3;
			
			GameFrame window = new InputSampleFrameForMe(settings);
			window.setVisible(true);
		}
	}
}
