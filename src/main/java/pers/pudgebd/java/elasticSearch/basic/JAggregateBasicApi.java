package pers.pudgebd.java.elasticSearch.basic;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import pers.pudgebd.java.elasticSearch.helper.JAggregateApiHelper;
import pers.pudgebd.java.utils.constants.Key;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class JAggregateBasicApi extends JAggregateApiHelper {


    protected static void usingAggregations(TransportClient client) {
        SearchResponse sr = client.prepareSearch(Key.GOODS())
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.terms("agg1").field("raiting")
                )
                .addAggregation(
                        AggregationBuilders.dateHistogram("agg2")
                                .field("@timestamp")
                                .dateHistogramInterval(DateHistogramInterval.YEAR)
                )
                .get();

        // Get your facet results
        Terms agg1 = sr.getAggregations().get("agg1");
        Histogram agg2 = sr.getAggregations().get("agg2");

        System.out.println(agg1.getDocCountError());
        System.out.println(agg2.toString());
    }


    protected static void searchTemplate(TransportClient client) {
//        searchTempFromFile(client);
//        searchTempFromClusterState(client);
        //还有 ScriptType.INLINE
    }


    /**
     * The term query finds documents that cont *.
     * 3ain the exact term specified in the inverted index.
     * 详见 personal_doc 的 es-term-query.txt
     */
    protected static void structuringAggregations(TransportClient client) {
        SearchResponse sr = client.prepareSearch()
                .addAggregation(
                        AggregationBuilders.terms("by_colors").field("color")
                                .subAggregation(
                                        AggregationBuilders.avg("avg_price").field("price")
                                )
                                .subAggregation(
                                        AggregationBuilders.terms("make_terms").field("make")
                                                .subAggregation(AggregationBuilders.min("min_price").field("price"))
                                                .subAggregation(AggregationBuilders.max("max_price").field("price"))
                                )
                )
//                .addAggregation( //官网不详细例子
//                        AggregationBuilders.terms("by_ev").field("exact_value").subAggregation(
//                                AggregationBuilders.dateHistogram("by_year").field("dateOfBirth").dateHistogramInterval(DateHistogramInterval.YEAR).subAggregation(
//                                        AggregationBuilders.avg("avg_children").field("children")
//                                )
//                        )
//                )
                .execute().actionGet();

        Terms terms = sr.getAggregations().get("by_colors");
        for (Terms.Bucket colorBucket : terms.getBuckets()) {

            for (Aggregation agg1 : colorBucket.getAggregations()) {
                String colorName = colorBucket.getKeyAsString();

                if (agg1 instanceof InternalAvg) {
                    InternalAvg ia = (InternalAvg) agg1;
//                    System.out.println(StringUtils.joinStr(
//                            colorName, ", ", ia.getName(), ", ", ia.getValue()
//                    ));

                } else {
                    StringTerms st = (StringTerms) agg1;
                    for (Terms.Bucket priceBucket : st.getBuckets()) {
                        String makerName = priceBucket.getKeyAsString();

                        for (Aggregation agg2 : priceBucket.getAggregations()) {
                            String minOrMax = null;
                            double val = 0D;

                            if (agg2 instanceof InternalMin) {
                                InternalMin im = (InternalMin) agg2;
                                minOrMax = "min";
                                val = im.getValue();

                            } else if (agg2 instanceof InternalMax) {
                                InternalMax im = (InternalMax) agg2;
                                minOrMax = "max";
                                val = im.getValue();
                            }
//                            System.err.println(StringUtils.joinStr(
//                                    colorName, ", ", makerName, ", ", minOrMax, ":", val
//                            ));
                        }
                    }
                }
            }

        }

    }



}
