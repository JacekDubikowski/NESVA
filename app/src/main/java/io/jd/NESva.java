package io.jd;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;

import copied.AppletUI;
import copied.BufferView;
import copied.Globals;
import copied.NES;
import copied.ScreenView;

public class NESva extends Frame {
    private final Color bgColor = Color.black.darker().darker();
    private NES nes;
    private AppletUI gui;
    int progress = 0;
    volatile boolean started = false;
    Font progressFont = new Font("Tahoma", Font.BOLD, 12);

    private final Config config;

    public NESva(Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        var nesva = new NESva(Config.get(args[0]));
        try {
            nesva.setSize(512, 480);
            nesva.setVisible(true);
            nesva.init();
            nesva.start();
            nesva.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws Exception {
        initGlobals();
        this.gui = new AppletUI(this);
        gui.init(false);
        nes = gui.getNES();
        nes.enableSound(config.sound());
        nes.reset();
    }

    public void start() {

        // Can start painting:
        started = true;

        // Load ROM file:
        try (var exec = Executors.newSingleThreadExecutor()) {
            exec.submit(this::run);
        }
    }

    private void run() {
        System.out.println("vNES 2.16 \u00A9 2006-2013 Open Emulation Project");
        System.out.println("For updates, visit www.openemulation.com");
        System.out.println("Use of this program subject to GNU GPL, Version 3.");

        nes.loadRom(config.rom());

        if (nes.rom.isValid()) {

            // Add the screen buffer:
            addScreenView();

            // Set some properties:
            Globals.timeEmulation = config.timeemulation();
            nes.ppu.showSoundBuffer = config.showsoundbuffer();

            // Start emulation:
            System.out.println("vNES is now starting the processor.");
            nes.getCpu().beginExecution();

        } else {
            // ROM file was invalid.
            System.out.println("vNES was unable to find (" + config.rom() + ").");
        }
    }

    public void addScreenView() {
        var panelScreen = (ScreenView) gui.getScreenView();
        panelScreen.setFPSEnabled(config.fps());

        if (config.scale()) {

            if (config.scanlines()) {
                panelScreen.setScaleMode(BufferView.ScaleMode.SCANLINE);
            } else {
                panelScreen.setScaleMode(BufferView.ScaleMode.NORMAL);
            }

            this.setSize(512, 480);
            this.setBounds(0, 0, 512, 480);
            panelScreen.setBounds(0, 0, 512, 480);

        } else {

            panelScreen.setBounds(0, 0, 256, 240);

        }

        this.setIgnoreRepaint(true);
        this.add(panelScreen);
        this.repaint();
    }

    private void initGlobals() {
        Globals.initMemoryFlushValue();
        Globals.controls.put("p1_a", config.player1Controls().buttonA());
        Globals.controls.put("p1_b", config.player1Controls().buttonB());
        Globals.controls.put("p1_start", config.player1Controls().start());
        Globals.controls.put("p1_select", config.player1Controls().select());
        Globals.controls.put("p1_up", config.player1Controls().up());
        Globals.controls.put("p1_down", config.player1Controls().down());
        Globals.controls.put("p1_left", config.player1Controls().left());
        Globals.controls.put("p1_right", config.player1Controls().right());

        Globals.controls.put("p2_a", config.player2Controls().buttonA());
        Globals.controls.put("p2_b", config.player2Controls().buttonB());
        Globals.controls.put("p2_start", config.player2Controls().start());
        Globals.controls.put("p2_select", config.player2Controls().select());
        Globals.controls.put("p2_up", config.player2Controls().up());
        Globals.controls.put("p2_down", config.player2Controls().down());
        Globals.controls.put("p2_left", config.player2Controls().left());
        Globals.controls.put("p2_right", config.player2Controls().right());
    }


    public void showLoadProgress(int percentComplete) {
        progress = percentComplete;
        paint(getGraphics());

    }

    // Show the progress graphically.
    @Override
    public void paint(Graphics g) {
        String pad;
        String disp;
        int scrw, scrh;
        int txtw, txth;

        if (!started) {
            return;
        }

        // Get screen size:
        if (config.scale()) {
            scrw = 512;
            scrh = 480;
        } else {
            scrw = 256;
            scrh = 240;
        }

        // Fill background:
        g.setColor(bgColor);
        g.fillRect(0, 0, scrw, scrh);

        // Prepare text:
        if (progress < 10) {
            pad = "  ";
        } else if (progress < 100) {
            pad = " ";
        } else {
            pad = "";
        }
        disp = "vNES is Loading Game... " + pad + progress + "%";

        // Measure text:
        g.setFont(progressFont);
        txtw = g.getFontMetrics(progressFont).stringWidth(disp);
        txth = g.getFontMetrics(progressFont).getHeight();

        // Display text:
        g.setFont(progressFont);
        g.setColor(Color.white);
        g.drawString(disp, scrw / 2 - txtw / 2, scrh / 2 - txth / 2);
        g.drawString(disp, scrw / 2 - txtw / 2, scrh / 2 - txth / 2);
        g.drawString("vNES \u00A9 2006-2013 Open Emulation Project", 12, 464);
    }


    public Color getBgColor() {
        return bgColor;
    }

    public int getRomSize() {
        return config.romSize();
    }
}
