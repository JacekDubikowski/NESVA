package copied;
/*
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

import java.util.HashMap;

public class Globals {
    public static final boolean debug = true;
    public static final boolean fsdebug = true;
    public static double CPU_FREQ_NTSC = 1789772.5d;
    public static double CPU_FREQ_PAL = 1773447.4d;
    public static int preferredFrameRate = 60;
    // Microseconds per frame:
    public static int frameTime = 1000000 / preferredFrameRate;
    // What value to flush memory with on power-up:
    public static short memoryFlushValue = 0xFF;
    public static boolean disableSprites = false;
    public static boolean timeEmulation = true;
    public static boolean palEmulation;
    public static boolean enableSound = true;
    public static boolean focused = false;
    public static HashMap<String, String> controls = new HashMap<>(); //vNES controls codes

    public static HashMap<String, Integer> keycodes; //Java key codes

    public static void initMemoryFlushValue() {
        Globals.memoryFlushValue = 0x00;
    }

    static {
        keycodes = new HashMap<>();
        keycodes.put("VK_SPACE", 32);
        keycodes.put("VK_PAGE_UP", 33);
        keycodes.put("VK_PAGE_DOWN", 34);
        keycodes.put("VK_END", 35);
        keycodes.put("VK_HOME", 36);
        keycodes.put("VK_DELETE", 127);
        keycodes.put("VK_INSERT", 155);
        keycodes.put("VK_LEFT", 37);
        keycodes.put("VK_UP", 38);
        keycodes.put("VK_RIGHT", 39);
        keycodes.put("VK_DOWN", 40);
        keycodes.put("VK_0", 48);
        keycodes.put("VK_1", 49);
        keycodes.put("VK_2", 50);
        keycodes.put("VK_3", 51);
        keycodes.put("VK_4", 52);
        keycodes.put("VK_5", 53);
        keycodes.put("VK_6", 54);
        keycodes.put("VK_7", 55);
        keycodes.put("VK_8", 56);
        keycodes.put("VK_9", 57);
        keycodes.put("VK_A", 65);
        keycodes.put("VK_B", 66);
        keycodes.put("VK_C", 67);
        keycodes.put("VK_D", 68);
        keycodes.put("VK_E", 69);
        keycodes.put("VK_F", 70);
        keycodes.put("VK_G", 71);
        keycodes.put("VK_H", 72);
        keycodes.put("VK_I", 73);
        keycodes.put("VK_J", 74);
        keycodes.put("VK_K", 75);
        keycodes.put("VK_L", 76);
        keycodes.put("VK_M", 77);
        keycodes.put("VK_N", 78);
        keycodes.put("VK_O", 79);
        keycodes.put("VK_P", 80);
        keycodes.put("VK_Q", 81);
        keycodes.put("VK_R", 82);
        keycodes.put("VK_S", 83);
        keycodes.put("VK_T", 84);
        keycodes.put("VK_U", 85);
        keycodes.put("VK_V", 86);
        keycodes.put("VK_W", 87);
        keycodes.put("VK_X", 88);
        keycodes.put("VK_Y", 89);
        keycodes.put("VK_Z", 90);
        keycodes.put("VK_NUMPAD0", 96);
        keycodes.put("VK_NUMPAD1", 97);
        keycodes.put("VK_NUMPAD2", 98);
        keycodes.put("VK_NUMPAD3", 99);
        keycodes.put("VK_NUMPAD4", 100);
        keycodes.put("VK_NUMPAD5", 101);
        keycodes.put("VK_NUMPAD6", 102);
        keycodes.put("VK_NUMPAD7", 103);
        keycodes.put("VK_NUMPAD8", 104);
        keycodes.put("VK_NUMPAD9", 105);
        keycodes.put("VK_MULTIPLY", 106);
        keycodes.put("VK_ADD", 107);
        keycodes.put("VK_SUBTRACT", 109);
        keycodes.put("VK_DECIMAL", 110);
        keycodes.put("VK_DIVIDE", 111);
        keycodes.put("VK_BACK_SPACE", 8);
        keycodes.put("VK_TAB", 9);
        keycodes.put("VK_ENTER", 10);
        keycodes.put("VK_SHIFT", 16);
        keycodes.put("VK_CONTROL", 17);
        keycodes.put("VK_ALT", 18);
        keycodes.put("VK_PAUSE", 19);
        keycodes.put("VK_ESCAPE", 27);
        keycodes.put("VK_OPEN_BRACKET", 91);
        keycodes.put("VK_BACK_SLASH", 92);
        keycodes.put("VK_CLOSE_BRACKET", 93);
        keycodes.put("VK_SEMICOLON", 59);
        keycodes.put("VK_QUOTE", 222);
        keycodes.put("VK_COMMA", 44);
        keycodes.put("VK_MINUS", 45);
        keycodes.put("VK_PERIOD", 46);
        keycodes.put("VK_SLASH", 47);
    }
}