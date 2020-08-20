### 《SpringBoot国际化插件》

### 一、简介

#### 1.1 概述

该插件的作用是方便SpringBoot项目的国际化开发，不仅可以生成前端与移动端国际化开发所需的语言包，还支持对后端抛出的异常、甚至数据库中存储的数据进行国际化。并且支持使用百度翻译对需要国际化的字段进行自动翻译。

### 二、初始化工作

#### 2.1 初始化“多语言数据库”

“多语言数据库初始化SQL脚本”位置为：
/multilingual-spring-boot-starter/doc/db/multilingual.sql

```sql
CREATE TABLE `multilingual` (
  `id` bigint(64) unsigned NOT NULL,
  `word_key` varchar(255) NOT NULL COMMENT 'key',
  `word_source_value` varchar(255) DEFAULT NULL COMMENT '源语言值',
  `word_target_value` varchar(255) DEFAULT NULL COMMENT '目标语言值',
  `word_target_type` varchar(255) NOT NULL COMMENT '目标语言类型',
  `client_type` int(2) NOT NULL COMMENT '终端类型 1:前端;2:APP-IOS;3:APP-Android;4:UNI-APP;5:后端-异常；6：后端-数据库中数据',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='多语言表';
```

#### 2.2 引入依赖

```xml
<dependency>
  <groupId>com.runlion</groupId>
  <artifactId>multilingual-spring-boot-starter</artifactId>
  <version>{最新Release版本}</version>
</dependency>
```

#### 2.3 系统配置

目前系统支持108种语言，每种语言对应的编码参考下方《语言编码对照表》

```properties
### 这里配置了英语、法语，意大利语
language.system.used = en,fra,it

### 是否开启自动翻译，默认关闭。如果开启自动翻译，必须配置appId与securityKey
language.autoTranslate.enable = true
### 开启自动翻译的情况下，需要配置。需要去百度翻译开放平台申请账号
### 百度翻译开放平台地址：https://api.fanyi.baidu.com/product/11
baidu.translate.appId = *************
baidu.translte.securityKey = ****************

### 异常枚举类所在的包路径,如果不配置会扫描所有类，影响启动时间，多个包可用逗号分割
language.scanner.packages = com.sixj.multilingual.enums
```

#### 2.4 请求头添加Language标示

前端在请求接口的时候，需要在headers中添加一个语言标示，key为`Language`,value为《语言编码对照表》中的语言编码，后端才能根据语言标示，切换到相对应的语言返回给前端。如果不传的话，默认为中文（zh）。

### 三、后端抛出的异常国际化

#### 3.1 异常枚举加注解`@MultiLanguageEnum(fieldName = "message")`

fieldName：需要进行国际化语言转换的字段名

例如：

```java
@MultiLanguageEnum(fieldName = "message")
public enum RespStatusEnum{
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(10000, "系统异常"),
    PARAM_ERROR(10001, "请求参数错误"),
    BIZ_ERROR(10002, "业务异常"),
  	;

    private Integer status;
    private String message;
}

```

#### 3.2 替换异常枚举中的异常信息

该插件提供了一个转换异常枚举中异常信息的方法

```java
<T extends Enum> T getMultilingualEnum(T eEnum);
```

可以在统一异常处理器中，对捕获的异常进行转换。

使用方法：

```java

@Autowired
private MultilingualService multilingualService;

private String test(){
  // 转换异常，返回的异常信息会根据系统语言的类型进行转换
  RespStatusEnum multilingualEnum = multilingualService.getMultilingualEnum(RespStatusEnum.SYSTEM_ERROR);
  return multilingualEnum.getException();
}

```

### 四、后端数据国际化

比如数据库中维护了新闻类别的数据，要求新闻类别支持国际化。下面以新闻类别为例

#### 4.1 @MultiLanguage注解

在新闻类别vo类上添加`@MultiLanguage`注解，在需要支持国际化的字段上也添加这个注解。

例如：

```java
@MultiLanguage
public class NoticeCategoryVO implements Serializable {
    private Long id;

    /** 栏目名称 */
    @MultiLanguage
    private String name;

    /** 创建时间 */
    private Date gmtCreate;

    /** 更新时间 */
    private Date gmtModified;

    /** 是否删除 */
    private Boolean deleted;
}
```

#### 4.2 @MultiLanguageMethod注解

在查询新闻类别列表的方法上，添加`@MultiLanguageMethod`注解。该插件会对添加了此注解的方法做切面，对返回结果做处理，将添加了`MultiLanguage`注解的属性值做国际化处理。

例如：

```java
// 添加@MultiLanguageMethod注解，会对返回结果（NoticeCategoryVO）做处理，将添加了@MultiLanguage注解的属性值（name属性）做国际化处理。
@MultiLanguageMethod
public List<NoticeCategoryVO> listCategory() {
  // 查询新闻类别列表
}
```

#### 4.3 注解扩展

##### 4.3.1  MultiDataGetter接口

对于`@MultiLanguageMethod`注解，如果返回结果是简单的vo对象，或者是vo对象集合，该插件可以直接对返回结果进行遍历，获取到vo对象，并对vo对象做国际化处理，但是，如果返回结果不是这种简单类型，可能是一个Page对象，或者是一个Map，或者是更复杂的嵌套数据结构，那么该插件就获取不到需要国际化处理的vo对象。例如返回结果是一个分页对象：

```java
public IPage<NoticeCategoryVO> pageList(){
  Page<NoticeCategoryVO> page = new Page<>(1,20);
  // 查询新闻类别列表
  // page.setRecords(noticeCategoryVos);
  return page;
}
```

为了解决这个问题，对于复杂的数据结构，可以实现`MultiDataGetter`接口，自定义获取到vo对象的方式，该插件会按照定义的方式获取vo对象，进行国际化处理。例如获取分页对象中的vo对象：

```java
public class MyPageDataGetter implements MultiDataGetter<IPage> {
    @Override
    public Object getData(IPage page) {
        return page.getRecords();
    }
}
```

定义获取vo对象的方式之后，在`@MultiLanguageMethod`注解中加入自定义的获取数据的方式。

```java
@MultiLanguageMethod(multiDataGetter = MyPageDataGetter.class)
```

### 五、生成前端、移动端语言包

目的是辅助前端、移动端生成他们所需的语言包（如果他们需要的话），他们需要的语言包文件格式不一样：

> 前端：JSON格式文件
>
> Android：<string name="key">value</string>
>
> IOS：key = "value";

步骤：

1. **下载多语言配置Excel**
2. **前端或移动端人员将需要国际化的字段维护到Excel中**
3. **将填写好的Excel上传到系统，写入数据库中**
4. **生成语言包，下载**

#### 3.1 下载多语言配置Excel

- 地址：/language/downloadTemplate
- 请求方式：get

#### 3.2  前端或移动端人员将需要国际化的字段维护到Excel中

终端类型枚举：`PC`、`APP_IOS`、`APP-Android`、`UNI-APP`。需要主要的是，终端类型必须严格使用这几个枚举，不然系统识别不了。

如果系统开启了自动翻译，那么会在上传的时候，对上传的中文自动进行翻译。

#### 3.3  将填写好的Excel上传到系统，写入数据库中

- 地址：/language/uploadTemplate
- 请求方式：post
- 参数名：file

#### 3.4  生成语言包，下载

- 地址：/language/downloadLangPackage?clientType=PC&languageType=en
- 请求方式：GET
- 参数：clientType，终端类型枚举：`PC`、`APP_IOS`、`APP-Android`、`UNI-APP`。
- 参数：languageType，语言编码参照《语言编码对照表》

------

------

《语言编码对照表》

| 编码 | 语言           | 是否支持百度翻译 | 编码 | 语言                | 是否支持百度翻译 |
| ---- | -------------- | ---------------- | ---- | ------------------- | ---------------- |
| af   | 阿非利堪斯语   | 0                | mi   | 毛利语              | 0                |
| am   | 阿姆哈拉语     | 0                | mk   | 马其顿语            | 0                |
| ara  | 阿拉伯语       | 1                | ml   | 马拉亚拉姆语        | 0                |
| az   | 阿塞拜疆语     | 0                | mn   | 蒙古语              | 0                |
| be   | 白俄罗斯语     | 0                | mo   | 摩尔达维亚语        | 0                |
| bul  | 保加利亚语     | 1                | mr   | 马拉提语            | 0                |
| bn   | 孟加拉语       | 0                | ms   | 马来语              | 0                |
| bs   | 波斯尼亚语     | 0                | mt   | 马耳他语            | 0                |
| ca   | 加泰隆语       | 0                | my   | 缅甸语              | 0                |
| co   | 科西嘉语       | 0                | nb   | 书面挪威语          | 0                |
| cs   | 捷克语         | 1                | ne   | 尼泊尔语            | 0                |
| cy   | 威尔士语       | 0                | nl   | 荷兰语              | 1                |
| dan  | 丹麦语         | 1                | no   | 挪威语              | 0                |
| de   | 德语           | 1                | ny   | 尼扬贾语            | 0                |
| el   | 希腊语         | 1                | or   | 奥利亚语            | 0                |
| en   | 英语           | 1                | pa   | 旁遮普语            | 0                |
| eo   | 世界语         | 0                | pl   | 波兰语              | 1                |
| spa  | 西班牙语       | 1                | ps   | 普什图语            | 0                |
| est  | 爱沙尼亚语     | 1                | pt   | 葡萄牙语            | 1                |
| eu   | 巴斯克语       | 0                | rom  | 罗马尼亚语          | 1                |
| fa   | 波斯语         | 0                | ru   | 俄语                | 1                |
| fin  | 芬兰语         | 1                | rw   | 基尼阿万达语        | 0                |
| fra  | 法语           | 1                | sd   | 信德语              | 0                |
| fy   | 弗里西亚语     | 0                | sh   | 塞尔维亚-克罗地亚语 | 0                |
| ga   | 爱尔兰语       | 0                | si   | 僧加罗语            | 0                |
| gd   | 苏格兰盖尔语   | 0                | sk   | 斯洛伐克语          | 0                |
| gl   | 加利西亚语     | 0                | slo  | 斯洛文尼亚语        | 1                |
| gu   | 古吉拉特语     | 0                | sm   | 萨摩亚语            | 0                |
| ha   | 豪萨语         | 0                | sn   | 绍纳语              | 0                |
| hi   | 印地语         | 0                | so   | 索马里语            | 0                |
| hr   | 克罗地亚语     | 0                | sq   | 阿尔巴尼亚语        | 0                |
| ht   | 海地克里奥尔语 | 0                | sr   | 塞尔维亚语          | 0                |
| hu   | 匈牙利语       | 1                | st   | 南索托语            | 0                |
| hy   | 亚美尼亚语     | 0                | su   | 巽他语              | 0                |
| id   | 印尼语         | 0                | swe  | 瑞典语              | 1                |
| ig   | 伊博语         | 0                | sw   | 斯瓦希里语          | 0                |
| is   | 冰岛语         | 0                | ta   | 泰米尔语            | 0                |
| it   | 意大利语       | 1                | te   | 泰卢固语            | 0                |
| jp   | 日语           | 1                | tg   | 塔吉克语            | 0                |
| jv   | 爪哇语         | 0                | th   | 泰语                | 1                |
| ka   | 格鲁吉亚语     | 0                | tk   | 土库曼语            | 0                |
| kg   | 刚果语         | 0                | tl   | 塔加洛语            | 0                |
| kk   | 哈萨克语       | 0                | tr   | 土耳其语            | 0                |
| km   | 高棉语         | 0                | tt   | 塔塔尔语            | 0                |
| kn   | 卡纳达语       | 0                | ug   | 维吾尔语            | 0                |
| kor  | 韩语           | 1                | uk   | 乌克兰语            | 0                |
| ku   | 库尔德语       | 0                | ur   | 乌尔都语            | 0                |
| ky   | 吉尔吉斯语     | 0                | uz   | 乌兹别克语          | 0                |
| la   | 拉丁语         | 0                | vie  | 越南语              | 1                |
| lb   | 卢森堡语       | 0                | xh   | 科萨语              | 0                |
| lo   | 老挝语         | 0                | yi   | 依地语              | 0                |
| lt   | 立陶宛语       | 0                | yo   | 约鲁巴语            | 0                |
| lv   | 拉脱维亚语     | 0                | zh   | 中文                | 1                |
| mg   | 马达加斯加语   | 0                | zu   | 祖鲁语              | 0                |