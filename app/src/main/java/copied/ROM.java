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

import mappers.MemoryMapper;
import mappers.MirroringType;

import static mappers.MapperFactory.mapperForType;

public class ROM {
    private static final String EXPECTED_FCODE = "NES" + new String(new byte[]{0x1A});

    private final short[] saveRam = new short[0];
    private final short[] header;
    private final short[][] rom;
    private final short[][] vrom;
    private final Tile[][] vromTile;
    private final int romCount;
    private final int vromCount;
    private final int mirroring;
    private final boolean batteryRam;
    private final boolean trainer;
    private final boolean fourScreen;
    private final MemoryMapper memoryMapper;

    private ROM(short[] header, short[][] rom, short[][] vrom, Tile[][] vromTile, int romCount, int vromCount, int mirroring, boolean batteryRam, boolean trainer, boolean fourScreen, MemoryMapper memoryMapper) {
        this.header = header;
        this.rom = rom;
        this.vrom = vrom;
        this.vromTile = vromTile;
        this.romCount = romCount;
        this.vromCount = vromCount;
        this.mirroring = mirroring;
        this.batteryRam = batteryRam;
        this.trainer = trainer;
        this.fourScreen = fourScreen;
        this.memoryMapper = memoryMapper;
    }

    public static ROM load(String fileName, FileLoader loader) {
        short[] romContent = loader.loadFile(fileName);

        if (romContent == null || romContent.length == 0) {
            throw new IllegalArgumentException("Unable to load ROM file.");
        }

        // Read header:
        var header = new short[16];
        System.arraycopy(romContent, 0, header, 0, 16);

        // Check first four bytes:
        String fcode = new String(new byte[]{(byte) romContent[0], (byte) romContent[1], (byte) romContent[2], (byte) romContent[3]});
        if (!fcode.equals(EXPECTED_FCODE)) {
            throw new IllegalArgumentException("Incorrect header of rom file '%s'".formatted(fileName));
        }

        // Read header:
        var romCount = header[4];
        var vromCount = header[5] * 2; // Get the number of 4kB banks, not 8kB
        var mirroring = ((header[6] & 1) != 0 ? 1 : 0);
        var batteryRam = (header[6] & 2) != 0;
        var trainer = (header[6] & 4) != 0;
        var fourScreen = (header[6] & 8) != 0;
        var mapperType = (header[6] >> 4) | (header[7] & 0xF0);

        // Battery RAM?
//        if (batteryRam) {
//            loadBatteryRam();
//        }

        // Check whether byte 8-15 are zero's:
        boolean foundError = false;
        for (int i = 8; i < 16; i++) {
            if (header[i] != 0) {
                foundError = true;
                break;
            }
        }
        if (foundError) {
            // Ignore byte 7.
            mapperType &= 0xF;
        }

        var rom = new short[romCount][16384];
        var vrom = new short[vromCount][4096];
        var vromTile = new Tile[vromCount][256];

        //try{

        // Load PRG-ROM banks:
        int offset = 16;
        for (int i = 0; i < romCount; i++) {
            for (int j = 0; j < 16384; j++) {
                if (offset + j >= romContent.length) {
                    break;
                }
                rom[i][j] = romContent[offset + j];
            }
            offset += 16384;
        }

        // Load CHR-ROM banks:
        for (int i = 0; i < vromCount; i++) {
            for (int j = 0; j < 4096; j++) {
                if (offset + j >= romContent.length) {
                    break;
                }
                vrom[i][j] = romContent[offset + j];
            }
            offset += 4096;
        }

        // Create VROM tiles:
        for (int i = 0; i < vromCount; i++) {
            for (int j = 0; j < 256; j++) {
                vromTile[i][j] = new Tile();
            }
        }

        // Convert CHR-ROM banks to tiles:
        //System.out.println("Converting CHR-ROM image data..");
        //System.out.println("VROM bank count: "+vromCount);
        int tileIndex;
        int leftOver;
        for (int v = 0; v < vromCount; v++) {
            for (int i = 0; i < 4096; i++) {
                tileIndex = i >> 4;
                leftOver = i % 16;
                if (leftOver < 8) {
                    vromTile[v][tileIndex].setScanline(leftOver, vrom[v][i], vrom[v][i + 8]);
                } else {
                    vromTile[v][tileIndex].setScanline(leftOver - 8, vrom[v][i - 8], vrom[v][i]);
                }
            }
        }

        var memoryMapper = mapperForType(mapperType);
        return new ROM(header, rom, vrom, vromTile, romCount, vromCount, mirroring, batteryRam, trainer, fourScreen, memoryMapper);
    }

    public int getRomBankCount() {
        return romCount;
    }

    // Returns number of 4kB VROM banks.
    public int getVromBankCount() {
        return vromCount;
    }

    public short[] getRomBank(int bank) {
        return rom[bank];
    }

    public short[] getVromBank(int bank) {
        return vrom[bank];
    }

    public Tile[] getVromBankTiles(int bank) {
        return vromTile[bank];
    }

    public MirroringType getMirroringType() {

        if (fourScreen) {
            return MirroringType.FOURSCREEN_MIRRORING;
        }

        if (mirroring == 0) {
            return MirroringType.HORIZONTAL_MIRRORING;
        }

        // default:
        return MirroringType.VERTICAL_MIRRORING;

    }

    public void setSaveState(boolean enableSave) {
        //this.enableSave = enableSave;
        if (enableSave && !batteryRam) {
//          loadBatteryRam();
        }
    }

    public boolean isBatteryRam() {
        return batteryRam;
    }

    public short[] getBatteryRam() {
        return saveRam;
    }

    public void destroy() {
//      closeRom();
    }

    public MemoryMapper getMemoryMapper() {
        return memoryMapper;
    }

    /*
     * Oracle broke the way this work, so most of it has been commented out.
     */

//    private void loadBatteryRam() {
//        if (batteryRam) {
//            try {
//                saveRam = new short[0x2000];
//                saveRamUpToDate = true;
//                
//                // Get hex-encoded memory string from user:
//                String encodedData = JOptionPane.showInputDialog("Returning players insert Save Code here.");
//                if (encodedData == null) {
//                    // User cancelled the dialog.
//                    return;
//                }
//
//                // Remove all garbage from encodedData:
//                encodedData = encodedData.replaceAll("[^\\p{XDigit}]", "");
//                if (encodedData.length() != saveRam.length * 2) {
//                    // Either too few or too many digits.
//                    return;
//                }
//
//                // Convert hex-encoded memory string to bytes:
//                for (int i = 0; i < saveRam.length; i++) {
//                    String hexByte = encodedData.substring(i * 2, i * 2 + 2);
//                    saveRam[i] = Short.parseShort(hexByte, 16);
//                }
//
//                //System.out.println("Battery RAM loaded.");
//                if (nes.getMemoryMapper() != null) {
//                    nes.getMemoryMapper().loadBatteryRam();
//                }
//
//            } catch (Exception e) {
//                //System.out.println("Unable to get battery RAM from user.");
//                failedSaveFile = true;
//            }
//        }
//    }

//    public void writeBatteryRam(int address, short value) {
//
//        if (!failedSaveFile && !batteryRam && enableSave) {
//            loadBatteryRam();
//        }
//
//        //System.out.println("Trying to write to battery RAM. batteryRam="+batteryRam+" enableSave="+enableSave);
//        if (batteryRam && enableSave && !failedSaveFile) {
//            saveRam[address - 0x6000] = value;
//            saveRamUpToDate = false;
//        }
//
//    }

//    public void closeRom() {
//    
//        if (batteryRam && !saveRamUpToDate) {
//            try {
//
//                // Convert bytes to hex-encoded memory string:
//                StringBuilder sb = new StringBuilder(saveRam.length * 2 + saveRam.length / 38);
//                for (int i = 0; i < saveRam.length; i++) {
//                    String hexByte = String.format("%02x", saveRam[i] & 0xFF);
//                    if (i % 38 == 0 && i != 0) {
//                        // Put spacing in so that word wrap will work.
//                        sb.append(" ");
//                    }
//                    sb.append(hexByte);
//                }
//                String encodedData = sb.toString();
//
//                // Send hex-encoded memory string to user:
//                JOptionPane.showInputDialog("Save Code for Resuming Game.", encodedData);
//
//                saveRamUpToDate = true;
//                //System.out.println("Battery RAM sent to user.");
//
//            } catch (Exception e) {
//
//                //System.out.println("Trouble sending battery RAM to user.");
//                e.printStackTrace();
//
//            }
//        }
//
//    }
}
