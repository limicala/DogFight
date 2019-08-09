package com.limicala.dogfight.message;

public class ConsoleMessage extends NLS {
    private static final String BUNDLE_NAME = "com.limicala.dogfight.message.messages"; //$NON-NLS-1$

    public static String INVALID_ENTER;
    public static String INVALID_OPTION;
    public static String INVALID_SETTING;
    public static String ENTER_ROOMID;
    public static String CONNECT_SUCCESS;
    public static String CLIENT_EXIT;
    public static String CLIENT_KICK;
    public static String NICKNAME_SET;
    public static String NICKNAME_INVALID;
    public static String GAME_OVER;
    public static String GAME_STARTING;

    public static String OPTIONS;
    public static String PLAY_ENTER_CARD;
    public static String PLAY_YOUR_TURN;
    public static String PLAY_CANT_PASS;
    public static String PLAY_HAS_BIGGER;
    public static String PLAY_INVALID;
    public static String PLAY_LESS;
    public static String PLAY_MISMATCH;
    public static String PLAY_MISMATCH_MIN;
    public static String PLAY_ORDER_ERROR;
    public static String PLAY_PASS;
    public static String PLAY_REDIRECT_NEXT_PLAY;
    public static String PLAY_FAIL_BY_INEXIST;
    public static String PVE_DIFFICULTY_NOT_SUPPORT;

    public static String PVE;
    public static String PVP;

    public static String SHOW_POKERS_PLAY;
    public static String SHOW_POKERS_NEXT_PLAY;

    public static String ROOM_CREATE_SUCCESS;
    public static String ROOM_JOIN_SUCCESS;
    public static String ROOM_JOIN_SUCCESS2;
    public static String ROOM_JOIN_FAIL_BY_FULL;
    public static String ROOM_JOIN_FAIL_BY_INEXIST;
    public static String SETTING;

    static {
        NLS.initializeMessages(BUNDLE_NAME, ConsoleMessage.class);
    }
}
