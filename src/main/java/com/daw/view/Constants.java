package com.daw.view;

public class Constants {
    private static final String STORE_SERVICE_URL = "http://localhost:8081";
    private static final String BATTLE_SERVICE_URL = "http://localhost:8082";

    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ATTRIBUTE_SESSIONS = "sessions";

    public static final String INDEX_PAGE_PATH = "/index";
    public static final String SHOP_PAGE_PATH = "/shop";
    public static final String WEAR_PAGE_PATH = "/wear";
    public static final String SUCCESS_PAGE_PATH = "/success";

    public static final String API_GET_URL = STORE_SERVICE_URL + "/api/get?login=%s";
    public static final String API_GET_ALL_URL = STORE_SERVICE_URL + "/api/get-all";
    public static final String API_GET_ITEM_URL = STORE_SERVICE_URL + "/api/get-item?itemId=%s";
    public static final String API_GET_ALL_ITEMS_URL = STORE_SERVICE_URL + "/api/get-all-items";
    public static final String API_CREATE_URL = STORE_SERVICE_URL + "/api/create";
    public static final String API_UPDATE_URL = STORE_SERVICE_URL + "/api/update";
    public static final String API_LOGIN_URL = STORE_SERVICE_URL + "/api/login?login=%s&password=%s";
    public static final String API_DELETE_URL = STORE_SERVICE_URL + "/api/delete?login=%s";
    public static final String API_BUY_URL = STORE_SERVICE_URL + "/api/buy?itemId=%s&login=%s";
    public static final String API_SELL_URL = STORE_SERVICE_URL + "/api/sell?itemId=%s&login=%s";
    public static final String API_WEAR_URL = STORE_SERVICE_URL + "/api/wear?itemId=%s&login=%s";
    public static final String API_UNWEAR_URL = STORE_SERVICE_URL + "/api/unwear?itemId=%s&login=%s";
    public static final String BATTLE_START_WITH_BOT_URL = BATTLE_SERVICE_URL + "/battle/bot/start?login=%s";
    public static final String BATTLE_START_URL = BATTLE_SERVICE_URL + "/battle/start?login=%s";
    public static final String BATTLE_CANCEL_URL = BATTLE_SERVICE_URL + "/battle/cancel?login=%s";
    public static final String BATTLE_MOVE_URL = BATTLE_SERVICE_URL + "/battle/move?login=%s&attack=%s&defence=%s&opponent=%s";

}
