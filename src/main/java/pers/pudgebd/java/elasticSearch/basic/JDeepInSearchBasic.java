package pers.pudgebd.java.elasticSearch.basic;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import pers.pudgebd.java.utils.EsUtils;
import pers.pudgebd.java.utils.constants.Key;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class JDeepInSearchBasic {

    protected static void scrollSearch(TransportClient client) {
        QueryBuilder qb = termQuery(Key.RAITING(), "31");

        SearchResponse scrollResp = client.prepareSearch(Key.GOODS())
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(4).get(); //max of 100 hits will be returned for each scroll
        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println(hit.getId());
            }
            System.err.println("-------------------------------------");
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(60000)).execute().actionGet();
        }
        while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop
    }


    protected static void termSearch(TransportClient client) {
        TermQueryBuilder tqb = QueryBuilders.termQuery("price", 20);

        SearchRequestBuilder srb = client.prepareSearch(Key.MY_STORE_())
                .setTypes(Key.PRODUCTS())
                .setQuery(tqb);

        SearchResponse resp = srb.get();
        EsUtils.printHits(resp.getHits());
    }


    protected static void boolSearch(TransportClient client) {
        BoolQueryBuilder bqb = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("title", "brown"))
                .should(QueryBuilders.termQuery("title", "fox"))
                .should(QueryBuilders.termQuery("title", "quick"))
                .minimumShouldMatch(2);

//        BoolQueryBuilder bqb2 = QueryBuilders.

        //不能这样写
//        BoolQueryBuilder bqb = QueryBuilders.boolQuery()
//                .should(QueryBuilders.termsQuery("title", "brown", "fox", "quick"))
//                .minimumShouldMatch(2);

        SearchRequestBuilder srb = client.prepareSearch(Key.MY_INDEX_())
                .setTypes(Key.MY_TYPE_())
                .setQuery(bqb);

        SearchResponse resp = srb.get();
        EsUtils.printHits(resp.getHits());
    }




}
