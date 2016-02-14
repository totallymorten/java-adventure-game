package adventure.types;

public enum RenderPriority
{
	BACKGROUND,
	ACTOR,
	EFFECT,
	FOREGROUND;
	
	public static boolean leq(RenderPriority r1, RenderPriority r2)
	{
		boolean b = (  r1 == r2
			||	  r1 == RenderPriority.BACKGROUND
			||   (r1 == RenderPriority.ACTOR && r2 != RenderPriority.BACKGROUND)
			||   (r1 == RenderPriority.EFFECT && (r2 == RenderPriority.EFFECT || r2 == RenderPriority.FOREGROUND)));
		
	    return b;
	}
}
