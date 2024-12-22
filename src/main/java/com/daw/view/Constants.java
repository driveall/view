package com.daw.view;

public class Constants {
    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ATTRIBUTE_SESSIONS = "sessions";

    public static final String INDEX_PAGE_PATH = "/index";
    public static final String SHOP_PAGE_PATH = "/shop";
    public static final String WEAR_PAGE_PATH = "/wear";
    public static final String SUCCESS_PAGE_PATH = "/success";

    public static final String API_GET_URL = "http://localhost:8081/api/get?login=%s";
    public static final String API_GET_ITEM_URL = "http://localhost:8081/api/get-item?itemId=%s";
    public static final String API_CREATE_URL = "http://localhost:8081/api/create";
    public static final String API_UPDATE_URL = "http://localhost:8081/api/update";
    public static final String API_DELETE_URL = "http://localhost:8081/api/delete?login=%s";
    public static final String BATTLE_START_WITH_BOT_URL = "http://localhost:8082/battle/bot/start?login=%s";
    public static final String BATTLE_MOVE_URL = "http://localhost:8082/battle/move?login=%s&attack=%s&defence=%s&opponent=%s";

    public static final String DEFAULT_REGION = "UA";
    public static final Integer ITEMS_COUNT = 8;

}
