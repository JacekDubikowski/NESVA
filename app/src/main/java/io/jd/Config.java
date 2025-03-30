package io.jd;

import copied.BufferView;

public record Config(
        String rom,
        boolean sound,
        boolean stereo,
        boolean fps,
        boolean timeemulation,
        boolean showsoundbuffer,
        PlayerControls player1Controls,
        PlayerControls player2Controls,
        int romSize,
        BufferView.ScaleMode scaleMode
) {
    public static Config get(String rom) {
        return new Config(
                rom,
                true,
                true,
                false,
                true,
                false,
                PlayerControls.PLAYER1_DEFAULT,
                PlayerControls.PLAYER2_DEFAULT,
                -1,
                BufferView.ScaleMode.NORMAL
        );
    }
}

record PlayerControls(
        String up,
        String down,
        String left,
        String right,
        String buttonA,
        String buttonB,
        String start,
        String select
) {
    public static final PlayerControls PLAYER1_DEFAULT = new PlayerControls(
            "VK_UP",
            "VK_DOWN",
            "VK_LEFT",
            "VK_RIGHT",
            "VK_X",
            "VK_Z",
            "VK_ENTER",
            "VK_CONTROL"
    );

    public static final PlayerControls PLAYER2_DEFAULT = new PlayerControls(
            "VK_NUMPAD8",
            "VK_NUMPAD2",
            "VK_NUMPAD4",
            "VK_NUMPAD6",
            "VK_NUMPAD7",
            "VK_NUMPAD9",
            "VK_NUMPAD1",
            "VK_NUMPAD3"
    );
}