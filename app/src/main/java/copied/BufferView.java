package copied;/*
vNES
Copyright © 2006-2013 Open Emulation Project

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;

public class BufferView extends JPanel {

    protected NES nes;
    private BufferedImage img;
    private VolatileImage vimg;
    private boolean usingMenu = false;
    private Graphics gfx;
    private int[] pix;
    private int[] pix_scaled;
    private ScaleMode scaleMode;
    // FPS counter variables:
    private boolean showFPS = false;
    private long prevFrameTime;

    private String fps;

    private int fpsCounter;
    private final Font fpsFont = new Font("Verdana", Font.BOLD, 10);
    private int bgColor = Color.white.darker().getRGB();
    // Constructor

    public BufferView(NES nes) {

        super(false);
        this.nes = nes;
        this.scaleMode = ScaleMode.NONE;
        createView();
    }

    public boolean useHWScaling() {
        return scaleMode.useHWScaling();
    }

    public void setBgColor(int color) {
        bgColor = color;
    }

    private void createView() {
        if (!scaleMode.useHWScaling()) {
            // Create new BufferedImage with scaled width & height:
            img = new BufferedImage(scaleMode.getWidth(), scaleMode.getHeight(), BufferedImage.TYPE_INT_RGB);
        } else {

            // Create new BufferedImage with normal width & height:
            img = new BufferedImage(scaleMode.getBaseWidth(), scaleMode.getBaseHeight(), BufferedImage.TYPE_INT_RGB);

            // Create graphics object to use for FPS display:
            gfx = img.createGraphics();
            gfx.setFont(fpsFont);


            // Set rendering hints:
            Graphics2D g2d = (Graphics2D) gfx;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            try {

                // Create hardware accellerated image:
                vimg = createVolatileImage(scaleMode.getBaseWidth(), scaleMode.getBaseHeight(), new ImageCapabilities(true));

            } catch (Exception e) {

                // Unable to create image. Fall back to software scaling:
                System.out.println("Unable to create HW accellerated image.");
                scaleMode = ScaleMode.NORMAL;
                img = new BufferedImage(scaleMode.getWidth(), scaleMode.getHeight(), BufferedImage.TYPE_INT_RGB);

            }

        }


        // Create graphics object to use for FPS display:
        gfx = img.createGraphics();
        gfx.setFont(fpsFont);


        // Set rendering hints:
        Graphics2D g2d = (Graphics2D) gfx;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);


        // Retrieve raster from image:
        DataBufferInt dbi = (DataBufferInt) img.getRaster().getDataBuffer();
        int[] raster = dbi.getData();


        // Replace current rasters with the one used by the image:
        if (scaleMode == ScaleMode.NONE || scaleMode == ScaleMode.HW2X || scaleMode == ScaleMode.HW3X) {

            pix = raster;
            nes.ppu.buffer = raster;

        } else {

            pix_scaled = raster;

        }


        // Set background color:
        for (int i = 0; i < raster.length; i++) {
            raster[i] = bgColor;
        }


        // Set component size & bounds:
        setSize(scaleMode.getWidth(), scaleMode.getHeight());
        setBounds(getX(), getY(), scaleMode.getWidth(), scaleMode.getHeight());


        // Repaint component:
        this.invalidate();
        repaint();


    }

    public void imageReady(boolean skipFrame) {
        // Skip image drawing if minimized or frameskipping:
        if (!skipFrame) {
            switch (scaleMode) {
                case NORMAL -> Scale.doNormalScaling(pix, pix_scaled, nes.ppu.scanlineChanged);
                case SCANLINE -> Scale.doScanlineScaling(pix, pix_scaled, nes.ppu.scanlineChanged);
                case RASTER -> Scale.doRasterScaling(pix, pix_scaled, nes.ppu.scanlineChanged);
            }

            nes.ppu.requestRenderAll = false;
            paint(getGraphics());
        }
    }

    public int[] getBuffer() {
        return pix;
    }

    public void update(Graphics g) {
    }

    public boolean scalingEnabled() {
        return scaleMode.isScalingEnabled();
    }

    public void setScaleMode(ScaleMode newMode) {
        // Check differences:
        // Change scale mode:
        this.scaleMode = newMode;
        setBounds(0, 0, scaleMode.getWidth(), scaleMode.getHeight());
        createView();
    }

    public void paint(Graphics g) {

        // Skip if not needed:
        if (usingMenu) {
            return;
        }

        if (scaleMode != ScaleMode.NONE) {

            // Scaled drawing:
            paintFPS(0, 14, g);
            paintScaled(g);

        } else if (img != null && g != null) {

            // Normal draw:
            paintFPS(0, 14, g);
            g.drawImage(img, 0, 0, null);

        }

    }

    public void paintScaled(Graphics g) {

        // Skip if not needed:
        if (usingMenu) {
            return;
        }

        if (scaleMode.useHWScaling()) {

            // 2X Hardware accellerated scaling.
            if (g != null && img != null && vimg != null) {

                // Draw BufferedImage into accellerated image:
                vimg.getGraphics().drawImage(img, 0, 0, null);

                // Draw accellerated image scaled:
                g.drawImage(vimg, 0, 0, scaleMode.getWidth(), scaleMode.getHeight(), null);

            }

        } else {
            // 2X Software scaling.
            if (g != null && img != null) {
                // Draw big BufferedImage directly:
                g.drawImage(img, 0, 0, scaleMode.getWidth(), scaleMode.getHeight(), null);

            }

        }

    }

    public void setFPSEnabled(boolean val) {

        // Whether to show FPS count.
        showFPS = val;

    }

    public void paintFPS(int x, int y, Graphics g) {

        // Skip if not needed:
        if (usingMenu) {
            return;
        }

        if (showFPS) {

            // Update FPS count:
            if (--fpsCounter <= 0) {

                long ct = nes.getGui().getTimer().currentMicros();
                long frameT = (ct - prevFrameTime) / 45;
                if (frameT == 0) {
                    fps = "FPS: -";
                } else {
                    fps = "FPS: " + (1000000 / frameT);
                }
                fpsCounter = 45;
                prevFrameTime = ct;

            }

            // Draw FPS.
            gfx.setColor(Color.black);
            gfx.fillRect(x, y - gfx.getFontMetrics().getAscent(), gfx.getFontMetrics().stringWidth(fps) + 3, gfx.getFontMetrics().getHeight());
            gfx.setColor(Color.cyan);
            gfx.drawString(fps, x, y);

        }

    }

    public void setUsingMenu(boolean val) {
        usingMenu = val;
    }

    public void destroy() {

        nes = null;
        img = null;

    }
}