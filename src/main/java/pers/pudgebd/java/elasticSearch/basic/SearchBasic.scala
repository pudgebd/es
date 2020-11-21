package pers.pudgebd.java.elasticSearch.basic

import pers.pudgebd.java.elasticSearch.helper.SearchHelper
import pers.pudgebd.java.utils.HttpUtils
import pers.pudgebd.java.utils.constants.EsCnts

/**
  * Created by kartty on 17-3-15.
  */
class SearchBasic extends SearchHelper {

    def aXXX() = {
        //        aggJson()
        //        aggStructuring()
        //        aggMetrics1()
        //        aggMetrics2()
        //Scripted Metric Aggregation 见 https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.2/_metrics_aggregations.html#java-aggs-metrics-percentile

        //看到
        //https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.2/_bucket_aggregations.html#_use_aggregation_response_14
    }


    def bxxx() = {
        bool()
    }


    //我们通过HTTP方法GET来检索文档，同样的，我们可以使用DELETE方法删除文档，
    //使用HEAD方法检查某文档是否存在。如果想更新已存在的文档，我们只需再PUT一次。
    def getXXX() = {
//        val resp = HttpClientUtils.requestGet(EsCnts.MEGACORP_BASIC_URL + "1")
//        println(resp)
    }


    def sXXX() = {
        searchXXX()
//        scroll()
//        multiSearch()
    }


    def txxx() = {
        term()
    }


}
