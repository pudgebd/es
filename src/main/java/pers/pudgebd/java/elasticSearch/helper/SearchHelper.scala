package pers.pudgebd.java.elasticSearch.helper

import java.util

import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.aggregations.{Aggregation, AggregationBuilders}
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval
import org.elasticsearch.search.aggregations.bucket.terms.{StringTerms, Terms}
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket
import org.elasticsearch.search.aggregations.metrics.max.Max
import org.elasticsearch.search.aggregations.metrics.min.Min
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles
import org.elasticsearch.search.aggregations.metrics.stats.Stats
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits
import org.elasticsearch.search.sort.{FieldSortBuilder, SortOrder}
import pers.pudgebd.java.utils.constants.{EsCnts, Key}
import pers.pudgebd.java.utils.{EsUtils, HttpUtils}

/**
  * Created by kartty on 17-4-2.
  */
class SearchHelper {

    //表达 SQL
    def bool() = {
        val content = """{
                        |   "query" : {
                        |      "filtered" : {
                        |         "filter" : {
                        |            "bool" : {
                        |              "should" : [
                        |                 { "term" : {"price" : 20}},
                        |                 { "term" : {"productID" : "XHDK-A-1293-#fJ3"}}
                        |              ],
                        |              "must_not" : {
                        |                 "term" : {"price" : 30}
                        |              }
                        |           }
                        |         }
                        |      }
                        |   }
                        |}""".stripMargin
//        val content = "{\"query\":{\"filtered\":{\"filter\":{\"bool\":{\"should\":[{\"term\":{\"price\":20}},{\"term\":{\"productID\":\"XHDK-A-1293-#fJ3\"}}],\"must_not\":{\"term\":{\"price\":30}}}}}}}"
        println(content)
//        val resp = HttpUtils.reqGet(EsCnts.MY_STORE_BASIC_URL + Key._SEARCH, content)
//        println(resp)


        val content2 = """{
                         |   "query" : {
                         |      "filtered" : {
                         |         "filter" : {
                         |            "bool" : {
                         |              "should" : [
                         |              // 因为 term 和 bool 在第一个 should 分句中是平级的，至少需要匹配其中的一个过滤器。
                         |                { "term" : {"productID" : "KDKE-B-9947-#kL5"}},
                         |                { "bool" : {
                         |                  "must" : [
                         |                    { "term" : {"productID" : "JODL-X-1937-#pV7"}},
                         |                    { "term" : {"price" : 30}}
                         |                  ]
                         |                }}
                         |              ]
                         |           }
                         |         }
                         |      }
                         |   }
                         |}""".stripMargin
//        val resp2 = HttpUtils.reqGet(EsCnts.MY_STORE_BASIC_URL + Key._SEARCH, content2)
//        println(resp2)
    }


    //The filtered query has been deprecated and removed in ES 5.0
    def term() = {
        //    <1> filtered 查询同时接受 query 与 filter。+
        //    <2> match_all 用来匹配所有文档，这是默认行为，所以在以后的例子中我们将省略掉 query 部分。
        //    <3> 这是我们上面见过的 term 过滤器。注意它在 filter 分句中的位置。
        val content = """{
                        |    "query" : {
                        |        "filtered" : {
                        |            "query" : {
                        |                "match_all" : {}
                        |            },
                        |            "filter" : {
                        |                "term" : {
                        |                    "price" : 20
                        |                }
                        |            }
                        |        }
                        |    }
                        |}""".stripMargin
//        val resp = HttpUtils.reqGet(EsCnts.MY_STORE_BASIC_URL + Key._SEARCH, content)
//        println(resp)


        //无法搜索到，没有一个 正确的映射索引
        //见 https://es.xiaoleilu.com/080_Structured_Search/05_term.html
        //“用于文本的 term 过滤器”
        val content2 = """{
                         |    "query" : {
                         |        "filtered" : {
                         |            "filter" : {
                         |                "term" : {
                         |                    "productID" : "XHDK-A-1293-#fJ3"
                         |                }
                         |            }
                         |        }
                         |    }
                         |}""".stripMargin
//        val resp2 = HttpUtils.reqGet(EsCnts.MY_STORE_BASIC_URL + Key._SEARCH, content2)
//        println(resp2)


        val content3 = """{
                         |    "query" : {
                         |        "filtered" : {
                         |            "filter" : {
                         |                "terms" : {
                         |                    "price" : [20, 30]
                         |                }
                         |            }
                         |        }
                         |    }
                         |}""".stripMargin
//        val resp3 = HttpUtils.reqGet(EsCnts.MY_STORE_BASIC_URL + Key._SEARCH, content3)
//        println(resp3)
    }


    def searchXXX() = {
        //        val query = "_search"
        val query = "_search?q=last_name:Smith&pretty"
//        val resp = HttpClientUtils.requestGet(EsCnts.MEGACORP_BASIC_URL + query)
//        println(resp)
        //使用DSL语句查询
        //https://es.xiaoleilu.com/010_Intro/30_Tutorial_Search.html
    }


    def scroll() = {
//        val qb = termQuery("multi", "test");
        val qb = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("first_name", "John"))
//                .should(QueryBuilders.termQuery("first_name", "John"))

        var scrollResp = EsUtils.getClient().prepareSearch(EsCnts.IDX_MEGACORP)
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
//                .setQuery(qb)
                .setSize(2).get();

        var idx = 1
        do {
            println(s"idx: $idx -----------------------------------------")
            val hits = scrollResp.getHits().getHits().iterator;
            loopSearchHits(hits)

            scrollResp = EsUtils.getClient().prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
            idx = idx + 1
        } while(scrollResp.getHits().getHits().length != 0);
    }


    def loopSearchHits(hits: Iterator[SearchHit]) = {
        while (hits.hasNext) {
            val hit = hits.next()
            println(s"${hit.getId}, ${hit.getFields}, ${hit.getSourceAsString}")
        }
    }


    def multiSearch() = {
        val client = EsUtils.getClient()
        val srb1 = client
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);
        val srb2 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("first_name", "John")).setSize(1);

        val sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

        // You will get all individual responses from MultiSearchResponse#getResponses()
        var nbHits = 0L;
        val it = sr.getResponses().iterator
        while (it.hasNext) {
            val item = it.next()
            val resp = item.getResponse();
            val hits = resp.getHits()
            loopSearchHits(hits.getHits().iterator)
            nbHits = nbHits + hits.getTotalHits();
        }
        println(s"nbHits: $nbHits")
    }


    def loopBucketsKeyDocCount(buckets: util.List[_ <: Bucket]) = {
        val it = buckets.iterator
        while (it.hasNext) {
            val entry = it.next()
            val key = entry.getKey();
            val docCount = entry.getDocCount();
            println(s"bucket key: $key, docCount: $docCount")
        }
    }

    def aggJson() = {
        val query = "_search"
        //        val content = "{\"aggs\":{\"all_interests\":{\"terms\":{\"field\":\"interests\"}}}}"
        val content = "{\"aggs\":{\"all_interests\":{\"terms\":{\"field\":\"interests\"},\"aggs\":{\"avg_age\":{\"avg\":{\"field\":\"age\"}}}}}}"
//        val resp = HttpUtils.reqGet(EsCnts.MEGACORP_BASIC_URL + query, content)
//        println(resp)
    }

    //用不起来，需要的话直接 http 请求
    def aggStructuring() = {
        val client = EsUtils.getClient()

        val sr = client.prepareSearch(EsCnts.IDX_MEGACORP)
                .setQuery(QueryBuilders.matchAllQuery()) //matchQuery("last_name", "Smith")
                .addAggregation(
                    AggregationBuilders
                            .terms("all_interests").field("interests")
                )
                .execute().actionGet();

        val agg1: Terms = sr.getAggregations().get("all_interests");
        loopBucketsKeyDocCount(agg1.getBuckets())
        //下面的没有有效数据
//        EsUtils.loop(agg1.getBuckets)
//        println(agg1.getBucketByKey("music").getAggregations.getAsMap)
//        println(agg1.getName)
    }


    def aggMetrics1() = {
        val client = EsUtils.getClient()

//        val ab = AggregationBuilders.max("agg").field("age")
//        val ab = AggregationBuilders.stats("agg").field("age")
//        val ab = AggregationBuilders.extendedStats("agg").field("age")
        val ab = AggregationBuilders.percentiles("agg").field("age").percentiles(1.0, 5.0, 10.0, 20.0, 30.0, 75.0, 95.0, 99.0);//自定义百分比

        val sr = client.prepareSearch(EsCnts.IDX_MEGACORP)
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(ab)
                .execute().actionGet();

//        val agg: Max = sr.getAggregations().get("agg"); println(agg.getValue())
//        val agg: Stats = sr.getAggregations().get("agg");println(agg.getMax)
//        val agg: ExtendedStats = sr.getAggregations().get("agg");println(agg.getMax);println(agg.getSumOfSquares())
        val agg: Percentiles = sr.getAggregations().get("agg");println(agg.iterator())

    }

    def aggMetrics2() = {
        val client = EsUtils.getClient()
        val ab = AggregationBuilders
                .terms("agg").field("age")
                .subAggregation(
                    AggregationBuilders.topHits("top").explain(true).size(2).from(0));

        val sr = client.prepareSearch(EsCnts.IDX_MEGACORP)
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(ab)
                .execute().actionGet();

        // sr is here your SearchResponse object
        val agg: Terms = sr.getAggregations().get("agg");
        loopBucketsKeyDocCount(agg.getBuckets())
    }


}
