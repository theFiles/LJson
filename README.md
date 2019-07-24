# ljson使用说明
**自己写着玩的json操作类**

---

> ###简述
ljson 是一个简单的 json 数据处理类，基于`List`和`Map`作为转换对象封装而成
内置：`Json`父类、`JsonMap`子类、`JsonList`子类、`LJson`对象、`ILJson`接口

### Json父类
    public abstract class Json

* `Json`是一个抽象类，平时不直接使用。

### JsonMap子类
    public class JsonMap extends Json

* `JsonMap`用于处理`Map`对象的转换和解析
```java
// map对象转换为json字符串
public static String encode(Map map)

// json字符串转为Map对象
public static LJson decode(String json)
```

### JsonList子类
    public class JsonList extends Json
    
* `JsonMap`用于处理`List`对象的转换和解析
```java
// list对象转换为json字符串
public static String encode(List list)

// json字符串转为List对象
public static LJson decode(String json)
```

### LJson对象
    public class LJson<T>
    
* `LJson`是两个子类在`decode()`时返回的结果对象

> 基本用法
1. `public LJson(T obj)` 通过 List/Map 转换成 LJson 对象
2. `public T getObj()` 取结果对象的源对象 List/Map
3. `public Object get(Object... keys)` 通过传入多个参数，依次深入（维度）取值

```java
// 暂时不支持自定义对象取值（实现 ILJson 的对象）
// 第一维 list
List list = new ArrayList();
// 第二维 map
Map mao = new HashMao();
map.put("a","array"); // 二维的值

// 复制个一维 list
list.add(map);

// 转换成 LJson
LJson<List> ljson = new LJson<>(list);

// 用 get 方法取值
System.out.println(ljson.get(0,"a")); // array

```

### ILJson接口
    public interface ILJson
    
* `ILJson`用于调用`encode()`时传入自定义对象必须实现的接口

```java
// 自定义规范
public class User implements ILJson{ // 必须实现 ILJson
    private String name;
    private int age;
    private char sex;
    
    public User(name,age,sex){
        this.name = name;
        this.age  = age;
        this.sex  = sex;
    }
    
    //必须有 get和set 方法
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    
    public int getAge(){return age;}
    public void setAge(int age){this.age = age;}
    
    public char getSex(){return sex;}
    public void setSex(char sex){this.sex = sex;}
}
```
**注意：自定义对象除了需要实现`ILJson`之外，还需要是一个规范的带有`getxxx()`方法的类，否则无法读取**

> ILJson接口拓展
1. `default Map getParam()` 获取实现接口的对象的所有属性和值
2. `default boolean set(String,String)` 修改指定属性的值

``` java
 // 一个规范实现了ILJson接口的类
 User user = new User("李大爷",25,"男");
 
 // 通过 set() 修改属性值
 user.set("age","18");
 
 // 通过 getParam() 取所有的属性
 Map params = user.getParam();
 
 System.out.println(params);
 // {name=李大爷,age=18,sex=男}
```

---
### demo调用实例
#### 创建一个自定义对象
```java
import ljson.ILJson;

public class testMap implements ILJson {
    private String a;
    private int b;
    private String c;

    public testMap(String a, int b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public String getC() {
        return c;
    }
}
```
#### List 转 json
```java
List list = new ArrayList(); // 创建一个List对象
list.add(new testMap("1",2,"3")); // 添加自定义对象
list.add(666); // 添加值
list.add("哈哈哈"); // 添加值

// 调用 JsonList.encode 转换
String json = JsonList.encode(list);

System.out.println(json);
// [{"a":"1","b":2,"c":"3"},666,"\u54c8\u54c8\u54c8"]
```

#### Map 转 json
```java
Map map = new HashMap(); // 创建一个Map对象
map.put("self",new testMap("1",2,"3")); // 添加自定义对象
map.put("name","李大爷"); //添加值
map.put("age",18); // 添加值

// 调用 JsonMap.encode 转换
String json = JsonMap.encode(map);

System.out.println(json);
// {"name":"\u674e\u5927\u7237","self":{"a":"1","b":2,"c":"3"},"age":18}
```

#### json 转 List
```java
// json字符串
String json = "[{\"a\":\"1\",\"b\":2,\"c\":\"3\"},666,\"\u54c8\u54c8\u54c8\"]";

// 调用 JsonList.decode 转换
LJson ljson = JsonList.decode(json);

// 通过 LJson.get 取值
int num = (int)ljson.get(1); // 下标为1
String str = (String)ljson.get(0,"a"); // 下标为0里key为a

System.out.println(num); // 666
System.out.println(str); // 1
System.out.println(ljson);
// [{a=1, b=2, c=3}, 666, 哈哈哈]
```

#### json 转 Map
```java
// json 字符串
String json = "{\"name\":\"\u674e\u5927\u7237\",\"self\":{\"a\":\"1\",\"b\":2,\"c\":\"3\"},\"age\":18}";

// 调用 JsonMap.decode 转换
LJson ljson = JsonMap.decode(json);

// 通过 LJson.get 取值
int num = (int)ljson.get("age"); // key为age
String str = (String)ljson.get("self","c"); // key为self里key为c

System.out.println(num); // 18
System.out.println(str); // 3
System.out.println(ljson);
// {name=李大爷, self={a=1, b=2, c=3}, age=18}
```

