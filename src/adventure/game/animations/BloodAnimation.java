package adventure.game.animations;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import adventure.game.Game;
import adventure.game.GameView;
import adventure.game.Renderable;
import adventure.game.entities.Animating;
import adventure.game.entities.BaseEntity;
import adventure.server.AdventureServer;
import adventure.types.RenderPriority;
import engine.exception.JavaEngineException;
import particle.ParticleConfiguration;
import particle.ParticleSystem;

public class BloodAnimation extends BaseEntity implements Animating {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4096904361079083797L;
	
	ParticleSystem p;
	
	public BloodAnimation(int tilex, int tiley) {
		
		this.tileX = tilex;
		this.tileY = tiley;
		
		p = new ParticleSystem(6000, tilex*Game.g.tileWidth + (Game.g.tileWidth / 2), tiley*Game.g.tileHeight + (Game.g.tileHeight / 4), ParticleConfiguration.BLOOD_SPATTER1, Color.red);
	}

	@Override
	public void updateAnimation(double ms) {
		
		if (p.finished)
		{
			Game.g.scheduleRemoveAnimating(this);
			return;
		}
		
		p.update(ms*1000000); // calling with ns

	}

	@Override
	public RenderPriority getRenderPriority() {
		return RenderPriority.EFFECT;
	}

	Point renderTmpPoint;
	
	@Override
	public void render(Graphics g, GameView view) {
		try {
			
			p.render((Graphics2D) g, new Point(view.getXInt(), view.getYInt()));
		} catch (JavaEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics g) {
		try {
			
			p.render((Graphics2D) g, null);
		} catch (JavaEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
