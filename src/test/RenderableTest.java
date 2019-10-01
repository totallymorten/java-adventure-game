package test;

import adventure.types.RenderPriority;
import junit.framework.TestCase;

public class RenderableTest extends TestCase 
{
	
	public void testRenderable()
	{
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.BACKGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.ACTOR));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.EFFECT));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.FOREGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.UI));
		
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.ACTOR, RenderPriority.BACKGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.ACTOR, RenderPriority.ACTOR));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.ACTOR, RenderPriority.EFFECT));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.ACTOR, RenderPriority.FOREGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.ACTOR, RenderPriority.UI));

		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.EFFECT, RenderPriority.BACKGROUND));
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.EFFECT, RenderPriority.ACTOR));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.EFFECT, RenderPriority.EFFECT));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.EFFECT, RenderPriority.FOREGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.EFFECT, RenderPriority.UI));

		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.FOREGROUND, RenderPriority.BACKGROUND));
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.FOREGROUND, RenderPriority.ACTOR));
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.FOREGROUND, RenderPriority.EFFECT));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.FOREGROUND, RenderPriority.FOREGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.FOREGROUND, RenderPriority.UI));
		
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.UI, RenderPriority.BACKGROUND));
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.UI, RenderPriority.ACTOR));
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.UI, RenderPriority.EFFECT));
		assertFalse("this is not true!", RenderPriority.leq(RenderPriority.UI, RenderPriority.FOREGROUND));
		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.UI, RenderPriority.UI));

//		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.BACKGROUND));
//		assertTrue("this is not true!", RenderPriority.leq(RenderPriority.BACKGROUND, RenderPriority.BACKGROUND));
	}
}
