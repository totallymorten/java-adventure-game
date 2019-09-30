package adventure.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import adventure.engine.AdventureGame;
import adventure.types.RenderPriority;

public class LoginScreen implements Renderable
{
	public LoginScreen()
	{
		
	}
	
	
	public LoginScreen(String username, String server, String port)
	{
		this.username = new StringBuffer(username);
		this.server = new StringBuffer(server);
		this.port = new StringBuffer(port);
	}
	
	RenderPriority renderPriority = RenderPriority.FOREGROUND;

	@Override
	public RenderPriority getRenderPriority()
	{
		return renderPriority;
	}

	int x = AdventureGame.WIDTH / 3;
	int y = AdventureGame.HEIGHT / 3;
	int ySpacing = 40;
	int yUsername = y + ySpacing*2;
	int yServer = yUsername + ySpacing;
	int yPort = yServer + ySpacing;
	int boxWidth = 150;
	int boxHeight = 20;
	int boxOffsetX = 100;
	int boxOffsetY = -15;
	int textOffsetX = 5;
	int textOffsetY = 3;
	int activeBoxY = yUsername;
	int activeBorderSize = 2;
	
	public StringBuffer username = new StringBuffer("Player 1");
	public StringBuffer server = new StringBuffer("localhost");
	public StringBuffer port = new StringBuffer("5555");
	
	StringBuffer activeText = username;
	
	private enum LoginState { USERNAME, SERVER, PORT};
	
	private LoginState state = LoginState.USERNAME;
	
	public void handleKeyEvent(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			if (activeText.length() > 0)
				activeText.deleteCharAt(activeText.length() - 1); // delete last letter
		}
		
		else if (event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_TAB)
		{
			if (state == LoginState.USERNAME)
			{
				activeText = server;
				state = LoginState.SERVER;
				activeBoxY = yServer;
			}
			else if (state == LoginState.SERVER)
			{
				activeText = port;
				state = LoginState.PORT;
				activeBoxY = yPort;
			}
			else if (state == LoginState.PORT)
			{
				activeText = username;
				state = LoginState.USERNAME;
				activeBoxY = yUsername;
			}
		}

		else if (event.getKeyCode() == KeyEvent.VK_UP)
		{
			if (state == LoginState.USERNAME)
			{
				activeText = port;
				state = LoginState.PORT;
				activeBoxY = yPort;
			}
			else if (state == LoginState.SERVER)
			{
				activeText = username;
				state = LoginState.USERNAME;
				activeBoxY = yUsername;
			}
			else if (state == LoginState.PORT)
			{
				activeText = server;
				state = LoginState.SERVER;
				activeBoxY = yServer;
			}
		}

		else if ((event.getKeyCode() != KeyEvent.VK_SHIFT) && (event.getKeyCode() != KeyEvent.VK_ESCAPE))
		{
			activeText.append(event.getKeyChar());
		}

	}
	
	@Override
	public void render(Graphics g)
	{
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, AdventureGame.WIDTH, AdventureGame.HEIGHT);
		
		g.setFont(new Font("Arial", Font.BOLD, 16));

		g.setColor(Color.white);
		g.drawString("[ESC]", 10, 20);

		// selected color
		g.setColor(Color.cyan);
		g.fillRect(x + boxOffsetX - activeBorderSize, activeBoxY + boxOffsetY - activeBorderSize, boxWidth + 2 * activeBorderSize, boxHeight + 2 * activeBorderSize);			

		// Username
		g.setColor(Color.black);
		g.drawString("Username: ", x, yUsername);
		g.setColor(Color.white);
		g.fillRect(x + boxOffsetX, yUsername + boxOffsetY, boxWidth, boxHeight);
		
		g.setColor(Color.black);
		g.drawString(username.toString(), x + boxOffsetX + textOffsetX, yUsername + textOffsetY);
		
		// Server
		g.setColor(Color.black);
		g.drawString("Server: ", x, yServer);
		g.setColor(Color.white);
		g.fillRect(x + boxOffsetX, yServer + boxOffsetY, boxWidth, boxHeight);
		g.setColor(Color.black);
		g.drawString(server.toString(), x + + boxOffsetX + textOffsetX, yServer + textOffsetY);

		// Port
		g.setColor(Color.black);
		g.drawString("Port: ", x, yPort);
		g.setColor(Color.white);
		g.fillRect(x + boxOffsetX, yPort + boxOffsetY, boxWidth, boxHeight);
		g.setColor(Color.black);
		g.drawString(port.toString(), x + + boxOffsetX + textOffsetX, yPort + textOffsetY);

	}

}
