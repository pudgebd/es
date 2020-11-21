package pers.pudgebd.java.utils.constants

/**
  * Created by kartty on 17-2-28.
  */
object Key {

    val APP_NAME = "FaceMainScala_58"

    val NEW_LINE = "\n"
    val SEP_ = "_"

    //-----  for hbase below  ---------------------------------

    val STATUS_0 = 0
    val FIXED_POOL_SIZE = 50

    val FLAG_1 = "1"
    val FLAG_2 = "1"
    val UTF8 = "utf8"
    val T_US = "t_us"
    val PRIORITY_COMMENT = 3
    val PRIORITY_POST = 2
    val PRIORITY_LIKE = 1


    //-----  for mysql  ---------------------------------------

    val SQL_REGEX = "[;:.,'\" ]"
    val MYSQL_PKG = "com.mysql.jdbc.Driver"
    val T_SEARCH_BASIC_WORD_STATISTICS = "t_search_basic_word_statistics"
    val GMT_CREATE_STR = "gmt_create"
    val GMT_MODIFY_STR = "gmt_modify"
    val BASIC_WORD_STR = "basic_word"
    val TYPE_STR = "type"
    val FREQUENCY_STR = "frequency"

    //-----  for es  ---------------------------------------

    val _INDEX = "_index"
    val _TYPE = "_type"
    val _ID = "_id"
    val _SEARCH = "_search?pretty"
    val _MGET = "_mget?pretty"
    val _BULK = "_bulk?pretty"
    val _SOURCE = "_source"
    val TERM = "term"
    val CREATE = "create"
    val UPSERT = "upsert"
    val INDEX = "index" //add a new doc
    val UPDATE = "update"
    val PRESEARCH = "presearch"
    val COMPLETION = "completion"
    val DOC = "doc"
    val INPUT_STR = "input"
    val OUTPUT_STR = "output"
    val PAYLOAD_STR = "payload"
    val WEIGHT_STR = "weight"
    val SUGGEST_WORDS_ = "suggest-words-"
    val IDS = "ids"
    val NUM = "num"
    val MYSQL_BATCH_SIZE = 10000
    val MYSQL_INIT_COLL_SIZE = 15000
    val BATCH_SIZE = 50000
    val INIT_COLL_SIZE = 70000


    //-----  for spark below  ---------------------------------

    val SPARK = "spark"
    val SEARCH = "search"
    val SEARCH_TRIGGER = "searchtrigger"

    //-----  for redis below  ---------------------------------
    val LIST_PREFIX = "recommend_uid_"
    val REDIS_MASTER_NAME = "system"
    val REDIS_MASTER_PWD = "xxxyyy"
    val REDIS_NODE_1 = "192.168.1.251:27600"
    val REDIS_NODE_2 = "192.168.1.251:28600"
    val REDIS_NODE_3 = "192.168.1.251:29600"


    val KAFKA_SEP = ":"
    val GOODS_ID_ = "goods_id"
    val USER_ID_ = "user_id"
    val RAITING = "raiting"
    val VIEW_COUNTS_ = "view_counts"
    val STAY_MS_ = "stay_ms"
    val IS_STAR_ = "is_star"
    val BUY_COUNTS_ = "buy_counts"
    val AT_TIMESTAMP = "@timestamp"
    val YEAR_MONTH_ = "year_month"
    val MYTOPIC = "mytopic"
    val CLICK = "click"
    val GOODS = "goods"
    val GOODSREC = "goodsrec"
    val LATEST = "latest"
    val TRUE = "true"
    val FALSE = "false"
    val REMARK = "remark"
    val CONCERN_SCORE = "concernScore"
    val ES_TYPE = "esType"
    val RECOMMEND = "recommend"
    val MY_STORE_ = "my_store"
    val PRODUCTS = "products"
    val MY_INDEX_ = "my_index"
    val MY_TYPE_ = "my_type"


}
