import ljson.JsonMap;
import ljson.LJson;

import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        String json = "{\"name\":\"\u674e\u5927\u7237\",\"self\":{\"a\":\"1\",\"b\":2,\"c\":\"3\"},\"age\":18}";
        // json 转对象
        LJson lJson = JsonMap.decode(json);
        System.out.println(lJson);

        // 对象转json
        String newJson = JsonMap.encode((Map)lJson.get("self"));
        System.out.println(newJson);
    }
}
