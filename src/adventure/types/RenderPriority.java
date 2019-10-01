package adventure.types;

public enum RenderPriority
{
	BACKGROUND,
	ACTOR,
	EFFECT,
	FOREGROUND,
	UI;
	
	public static boolean leq(RenderPriority r1, RenderPriority r2)
	{
		boolean b = (  r1 == r2
			||	  r1 == RenderPriority.BACKGROUND
			||   (r1 == RenderPriority.ACTOR && r2 != RenderPriority.BACKGROUND)
			||   (r1 == RenderPriority.EFFECT && (r2 != RenderPriority.BACKGROUND && r2 != RenderPriority.ACTOR))
			||   (r1 == RenderPriority.FOREGROUND && (r2 == RenderPriority.UI))
			);
		
	    return b;
	}
}
