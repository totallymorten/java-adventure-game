package adventure.game.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import adventure.game.Game;
import adventure.properties.AdventureProperties;

public class Player extends Actor
{
	public String username;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6389918645753031069L;

	public Player(int initx, int inity, Image img)
	{
		super(initx, inity, img);

		friends.add(Player.class);
		
		this.maxHealth = AdventureProperties.getDouble("player_maxHealth");
		this.maxStamina = AdventureProperties.getDouble("player_maxStamina");
		this.maxMana = AdventureProperties.getDouble("player_maxMana");
		
		this.defence = AdventureProperties.getDouble("player_defence");
		this.attack = AdventureProperties.getDouble("player_attack");
		this.staminaGainPrMs = AdventureProperties.getDouble("player_staminaGainPrMs");

		// setting all stats = max
		this.resetStats();
	}
	
	public void render(Graphics g)
	{
		if (!Game.g.isVisible(this))
			return;		

		super.render(g);
		
		Point viewPos = getViewRenderPoint();
		
		g.setColor(Color.white);
		g.setFont(new Font("Courier New", Font.BOLD, 15));
		g.drawString(username, viewPos.x,viewPos.y);	
	}

}
