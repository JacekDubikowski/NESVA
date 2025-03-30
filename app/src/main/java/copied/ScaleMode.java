package copied;

public enum ScaleMode {
    UNINITIALIZED(true, false, -1),
    NONE(false, false, 1),
    NORMAL(true, false, 2),
    HW2X(true, true, 2),
    HW3X(true, true, 3),
    SCANLINE(true, false, 2),
    RASTER(true, false, 2);

    private static final int BASE_WIDTH = 256;
    private static final int BASE_HEIGHT = 240;
    private final boolean scalingEnabled;
    private final boolean useHWScaling;
    private final int scale;

    ScaleMode(boolean scalingEnabled, boolean useHWScaling, int scale) {
        this.scalingEnabled = scalingEnabled;
        this.useHWScaling = useHWScaling;
        this.scale = scale;
    }

    public boolean isScalingEnabled() {
        return scalingEnabled;
    }

    public boolean useHWScaling() {
        return useHWScaling;
    }

    public int getWidth() {
        return BASE_WIDTH * scale;
    }

    public int getHeight() {
        return BASE_HEIGHT * scale;
    }

    public int getBaseWidth() {
        return BASE_WIDTH;
    }

    public int getBaseHeight() {
        return BASE_HEIGHT;
    }
}
