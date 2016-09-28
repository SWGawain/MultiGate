package com.rkylin.multigates.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 嘉玮 on 2016-8-30.
 */
public class ESSearchAPI {

    Client client ;

    public ESSearchAPI(){
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testQuery(){

        try {
            SearchResponse response = client.prepareSearch("twitter", "index2")         //可以不赋值，查全部的index
                    .setTypes("tweet", "type2")                                          //查任意type
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    //Query
                    .setQuery(QueryBuilders.termQuery("user", "kimchy"))
                    //Filter
                    .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))
                    .setFrom(0).setSize(60).setExplain(true)
                    .execute()
                    .actionGet();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }
}
