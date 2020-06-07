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
			settings.window_title = "���� ȭ�鿡 ���";
			settings.canvas_width = 400;
			settings.canvas_height = 600;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - '���ÿ� Ű�� �Է�'�ϴ� ��Ȳ�� ��������� ���� �����
			settings.gameLoop_interval_ns = 166666666;		//�� 60FPS�� �ش�
			//settings.gameLoop_interval_ns = 100000000;	//10FPS�� �ش� - '���ÿ� Ű�� �Է�'�ϴ� ��Ȳ�� �� ���� �����
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 5;
			
			GameFrame window = new JustPicture(settings);
			window.setVisible(true);
		}
		
	}
}
