package mappers;

public class MapperFactory {
    private static final String[] mapperName;
    private static final boolean[] mapperSupported;

    static {
        mapperName = new String[255];
        mapperSupported = new boolean[255];
        for (int i = 0; i < 255; i++) {
            mapperName[i] = "Unknown Mapper";
        }

        mapperName[0] = "NROM";
        mapperName[1] = "Nintendo MMC1";
        mapperName[2] = "UxROM";
        mapperName[3] = "CNROM";
        mapperName[4] = "Nintendo MMC3";
        mapperName[5] = "Nintendo MMC5";
        mapperName[6] = "FFE F4xxx";
        mapperName[7] = "AxROM";
        mapperName[8] = "FFE F3xxx";
        mapperName[9] = "Nintendo MMC2";
        mapperName[10] = "Nintendo MMC4";
        mapperName[11] = "Color Dreams";
        mapperName[12] = "FFE F6xxx";
        mapperName[13] = "CPROM";
        mapperName[15] = "iNES Mapper #015";
        mapperName[16] = "Bandai";
        mapperName[17] = "FFE F8xxx";
        mapperName[18] = "Jaleco SS8806";
        mapperName[19] = "Namcot 106";
        mapperName[20] = "(Hardware) Famicom Disk System";
        mapperName[21] = "Konami VRC4a, VRC4c";
        mapperName[22] = "Konami VRC2a";
        mapperName[23] = "Konami VRC2b, VRC4e, VRC4f";
        mapperName[24] = "Konami VRC6a";
        mapperName[25] = "Konami VRC4b, VRC4d";
        mapperName[26] = "Konami VRC6b";
        mapperName[32] = "Irem G-101";
        mapperName[33] = "Taito TC0190, TC0350";
        mapperName[34] = "BxROM, NINA-001";
        mapperName[41] = "Caltron 6-in-1";
        mapperName[46] = "Rumblestation 15-in-1";
        mapperName[47] = "Nintendo MMC3 Multicart (Super Spike V'Ball + Nintendo World Cup)";
        mapperName[48] = "iNES Mapper #048";
        mapperName[64] = "Tengen RAMBO-1";
        mapperName[65] = "Irem H-3001";
        mapperName[66] = "GxROM";
        mapperName[67] = "Sunsoft 3";
        mapperName[68] = "Sunsoft 4";
        mapperName[69] = "Sunsoft FME-7";
        mapperName[70] = "iNES Mapper #070";
        mapperName[71] = "Camerica";
        mapperName[72] = "iNES Mapper #072";
        mapperName[73] = "Konami VRC3";
        mapperName[75] = "Konami VRC1";
        mapperName[76] = "iNES Mapper #076 (Digital Devil Monogatari - Megami Tensei)";
        mapperName[77] = "iNES Mapper #077 (Napoleon Senki)";
        mapperName[78] = "Irem 74HC161/32";
        mapperName[79] = "American Game Cartridges";
        mapperName[80] = "iNES Mapper #080";
        mapperName[82] = "iNES Mapper #082";
        mapperName[85] = "Konami VRC7a, VRC7b";
        mapperName[86] = "iNES Mapper #086 (Moero!! Pro Yakyuu)";
        mapperName[87] = "iNES Mapper #087";
        mapperName[88] = "iNES Mapper #088";
        mapperName[89] = "iNES Mapper #087 (Mito Koumon)";
        mapperName[92] = "iNES Mapper #092";
        mapperName[93] = "iNES Mapper #093 (Fantasy Zone)";
        mapperName[94] = "iNES Mapper #094 (Senjou no Ookami)";
        mapperName[95] = "iNES Mapper #095 (Dragon Buster) [MMC3 Derived]";
        mapperName[96] = "(Hardware) Oeka Kids Tablet";
        mapperName[97] = "iNES Mapper #097 (Kaiketsu Yanchamaru)";
        mapperName[105] = "NES-EVENT [MMC1 Derived]";
        mapperName[113] = "iNES Mapper #113";
        mapperName[115] = "iNES Mapper #115 (Yuu Yuu Hakusho Final) [MMC3 Derived]";
        mapperName[118] = "iNES Mapper #118 [MMC3 Derived]";
        mapperName[119] = "TQROM";
        mapperName[140] = "iNES Mapper #140 (Bio Senshi Dan)";
        mapperName[152] = "iNES Mapper #152";
        mapperName[154] = "iNES Mapper #152 (Devil Man)";
        mapperName[159] = "Bandai (Alternate of #016)";
        mapperName[180] = "(Hardware) Crazy Climber Controller";
        mapperName[182] = "iNES Mapper #182";
        mapperName[184] = "iNES Mapper #184";
        mapperName[185] = "iNES Mapper #185";
        mapperName[207] = "iNES Mapper #185 (Fudou Myouou Den)";
        mapperName[228] = "Active Enterprises";
        mapperName[232] = "Camerica (Quattro series)";

        // The mappers supported:
        mapperSupported[0] = true; // No Mapper
        mapperSupported[1] = true; // MMC1
        mapperSupported[2] = true; // UNROM
        mapperSupported[3] = true; // CNROM
        mapperSupported[4] = true; // MMC3
        mapperSupported[7] = true; // AOROM
        mapperSupported[9] = true; // MMC2
        mapperSupported[10] = true; // MMC4
        mapperSupported[11] = true; // ColorDreams
        mapperSupported[15] = true;
        mapperSupported[18] = true;
        mapperSupported[21] = true;
        mapperSupported[22] = true;
        mapperSupported[23] = true;
        mapperSupported[32] = true;
        mapperSupported[33] = true;
        mapperSupported[34] = true; // BxROM
        mapperSupported[48] = true;
        mapperSupported[64] = true;
        mapperSupported[66] = true; // GNROM
        mapperSupported[68] = true; // SunSoft4 chip
        mapperSupported[71] = true; // Camerica
        mapperSupported[72] = true;
        mapperSupported[75] = true;
        mapperSupported[78] = true;
        mapperSupported[79] = true;
        mapperSupported[87] = true;
        mapperSupported[94] = true;
        mapperSupported[105] = true;
        mapperSupported[140] = true;
        mapperSupported[182] = true;
        mapperSupported[232] = true; // Camerica /Quattro
    }

    public static MemoryMapper mapperForType(int mapperType) {
        if (mapperSupported(mapperType)) {
            switch (mapperType) {
                case 0: {
                    return new MapperDefault();
                }
                case 1: {
                    return new Mapper001();
                }
                case 2: {
                    return new Mapper002();
                }
                case 3: {
                    return new Mapper003();
                }
                case 4: {
                    return new Mapper004();
                }
                case 7: {
                    return new Mapper007();
                }
                case 9: {
                    return new Mapper009();
                }
                case 10: {
                    return new Mapper010();
                }
                case 11: {
                    return new Mapper011();
                }
                case 15: {
                    return new Mapper015();
                }
                case 18: {
                    return new Mapper018();
                }
                case 21: {
                    return new Mapper021();
                }
                case 22: {
                    return new Mapper022();
                }
                case 23: {
                    return new Mapper023();
                }
                case 32: {
                    return new Mapper032();
                }
                case 33: {
                    return new Mapper033();
                }
                case 34: {
                    return new Mapper034();
                }
                case 48: {
                    return new Mapper048();
                }
                case 64: {
                    return new Mapper064();
                }
                case 66: {
                    return new Mapper066();
                }
                case 68: {
                    return new Mapper068();
                }
                case 71: {
                    return new Mapper071();
                }
                case 72: {
                    return new Mapper072();
                }
                case 75: {
                    return new Mapper075();
                }
                case 78: {
                    return new Mapper078();
                }
                case 79: {
                    return new Mapper079();
                }
                case 87: {
                    return new Mapper087();
                }
                case 94: {
                    return new Mapper094();
                }
                case 105: {
                    return new Mapper105();
                }
                case 140: {
                    return new Mapper140();
                }
                case 182: {
                    return new Mapper182();
                }

            }
        }
        return new MapperDefault();
    }

    private static boolean mapperSupported(int mapperType) {
        if (mapperType < mapperSupported.length && mapperType >= 0) {
            return mapperSupported[mapperType];
        }
        return false;
    }
}
