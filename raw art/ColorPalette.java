import com.badlogic.gdx.graphics.Color;

public class ColorPalette {
	// https://lospec.com/palette-list/rebirth
	
    // Define colors as constants
    public static final Color COLOR_1 = Color.valueOf("0d0812");
    public static final Color COLOR_2 = Color.valueOf("162c54");
    public static final Color COLOR_3 = Color.valueOf("1c4c62");
    public static final Color COLOR_4 = Color.valueOf("3b6166");
    public static final Color COLOR_5 = Color.valueOf("46857b");
    public static final Color COLOR_6 = Color.valueOf("8f154f");
    public static final Color COLOR_7 = Color.valueOf("b82d46");
    public static final Color COLOR_8 = Color.valueOf("b82d46");
    public static final Color COLOR_9 = Color.valueOf("e2a560");
    public static final Color COLOR_10 = Color.valueOf("edce5e");
    
    // Private constructor to prevent instantiation
    private ColorPalette() {
        throw new AssertionError("ColorPalette class cannot be instantiated.");
    }
}
