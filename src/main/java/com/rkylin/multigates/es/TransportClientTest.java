package com.rkylin.multigates.es;

import com.rkylin.multigates.utils.PrintBean;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by 嘉玮 on 2016-8-25.
 */
public class TransportClientTest {

    public static void main(String[] args) {
        Client client = null;
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));


            /**
             * settings 可以指定创建Client时的配置参数
             * 如 cluster_name 、ping_timeout 等等，集群链接方式可以参照官网
             * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html
             */
//            Settings settings = Settings.settingsBuilder()
//                    .put("client.transport.sniff", true).build();
//            client = TransportClient.builder().settings(settings).build();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        try {
//          createIndex(client);
//          getAPI(client);
//          getAPIAsync(client);
//          deleteAPI(client);
//          updateRequest(client);
//          updateScript(client);
//          multiget(client);
            bulkAPI(client);




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /**
             * 关闭连接，否则ES服务端会报一堆链接错误
             */
            client.close();
        }

    }

    /**
     * 创建索引
     * @param client
     */
    public static void createIndex(Client client){
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        /**
         *  prepareIndex的三个参数，第一个是index，第二个是type，第三个是id，
         *
         *  id不输入的话，随机生成，几乎不可能重复
         *
         *  setSource方法，可以传入各种类型的参数，
         *  有一个Elasticsearch helpers，可以辅助建立数据，不过推测实际运用中，并不常用
         *  参照官网 https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
         *
         *  默认情况下，是使用另外的线程来执行创建索引这一过程
         *  By default, operationThreaded is set to true which means
         *  the operation is executed on a different thread.
         */
        IndexResponse response = client.prepareIndex("twitter", "tweet","1")
                .setSource(json)
                .get();


        /**
         * IndexResponse类重写了toString方法，神奇的是，ES的包，有的重写了这个方法，有的没有
         */
        System.out.println(response);
    }

    /**
     * get方法
     * @param client
     */
    public static void getAPI(Client client){
        GetResponse response = client.prepareGet("twitter", "tweet", "1").get();

        PrintBean.print(response);

        try {
            /**
             * GetResponse类中有一个GetResult私有类，然后用各种get方法来获取这个类的属性
             * 这里直接将这个类打印了
             *
             */
            Field field = GetResponse.class.getDeclaredField("getResult");
            field.setAccessible(true);
            GetResult gr = (GetResult) field.get(response);

            PrintBean.print(gr);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        /**
         * GetResult类中，直接打印是没法获取到具体的值的
         *
         * 只能用response封装的getSourceAs...() 方法来输出存的值
         */
        String sourceAsString = response.getSourceAsString();
        System.out.print(sourceAsString);

    }

    /**
     * get方法(同步)
     * @param client
     */
    public static void getAPIAsync(Client client){
        /**
         * 方法默认由额外的线程执行，下面的设置可调整为主线程同步执行
         *
         * 但是没觉得两种执行策略有明显的差异，多半是因为数据量太小，所以体现不出来吧
         *
         * 创建和其他操作，基本都可以选择同步还是异步执行，后面的操作不再重复写代码验证
         *
         * 个人推测，正常情况下还是异步的好用些，可以让主线程撇开无用的等待时间
         *
         * By default, operationThreaded is set to true which means
         * the operation is executed on a different thread.
         */
        GetResponse response = client.prepareGet("twitter", "tweet", "1")
                .setOperationThreaded(false)
                .get();

        PrintBean.print(response);

        try {
            Field field = GetResponse.class.getDeclaredField("getResult");
            field.setAccessible(true);
            GetResult gr = (GetResult) field.get(response);

            PrintBean.print(gr);

            String sourceAsString = response.getSourceAsString();
            System.out.print(sourceAsString);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * Delete方法
     */
    public static void deleteAPI(Client client){
        DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
                .get();

        PrintBean.print(response);
    }

    /**
     * update方法
     */
    public static void updateRequest(Client client){
        try {
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index("twitter");
            updateRequest.type("tweet");
            updateRequest.id("1");
            updateRequest.doc(jsonBuilder()
                    .startObject()
                    .field("user", "Sauron")
                    .endObject());
            UpdateResponse updateResponse = client.update(updateRequest).get();

            PrintBean.print(updateResponse);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * update方法，传入一段脚本，具体脚本定义需要查阅官网
     *
     * 直接运行是不行的，需要ES服务端配置文件支持
     *
     * 就从这一点来看，个人认为update语法不应该是常用语法，而且实际考虑一下，一个全文检索为底层的noSql库，
     * 在索引反复建立和查询为主的操作过程中，更新操作并没有显得那么的必要，本来就是为了大量数据而存在的库，
     * 现在要针对某一条进行修改，反而显得杀鸡用牛刀了，如果真的有错，要么在分析的时候滤过这条，
     * 或者就是给一个status标记0或1，终归有很多方式，但是删和改都不是一个很好的选择
     *
     * 当然，更新带来的效率问题，并没有深度的挖掘，推测应该不会用的很多
     *
     * 另外吐槽一下这个更新语法啊，传一段script进去是什么鬼啊。。。真心是要怎么纠结就怎么纠结
     *
     *
     * ————————————————————————————————————————————————
     * 来自samebug的回复
     * which means you need to add this:
     *
     * script.engine.groovy.inline.update: on
     * to elasticsearch.yml file and restart the nodes.
     *
     * @param client
     */
    public static void updateScript(Client client){
        UpdateResponse updateResponse = client.prepareUpdate("twitter", "tweet", "1")
                .setScript(new Script("ctx._source.user = \"Sauron\"", ScriptService.ScriptType.INLINE, null, null))
                .get();

        PrintBean.print(updateResponse);
    }

    /**
     * 一次获取多个值
     *
     *
     * @param client
     */
    public static void multiget(Client client){
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "tweet", "1")                               //get by a single id
                .add("twitter", "tweet", "2", "3", "4")                     //or by a list of ids for the same index/type
                .add("another", "type", "foo")                              //you can also get from another index
                .get();

        for (MultiGetItemResponse itemResponse: multiGetItemResponses){      //MuliGetResponse 类可以直接for遍历
            GetResponse response = itemResponse.getResponse();                  //封装了GetResponse对象
//            try {
//                Field field = GetResponse.class.getDeclaredField("getResult");
//                field.setAccessible(true);
//                GetResult gr = (GetResult) field.get(response);
//                PrintBean.print(gr);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            boolean failed = itemResponse.isFailed();                           //用来判断是否获取成功
            if(failed){
                MultiGetResponse.Failure failure = itemResponse.getFailure();   //如果没成功，可以获取到错误对象
                PrintBean.print(failure);                                       //Failure对象里面会包含index，type，id和throwable四个属性
                                                                                //throwable属性里面有具体报错的原因
                continue;
            }

            if(response.isExists()){
                String json = response.getSourceAsString();
                System.out.println(json);
            }
            PrintBean.print(response);
        }
    }

    /**
     * ES-api中提供的管道操作
     * @param client
     */
    public static void bulkAPI(Client client) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

// either use client#prepare, or use Requests# to directly build index/delete requests
        try {
            bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "kimchy")
                            .field("postDate", new Date())
                            .field("message", "trying out Elasticsearch")
                            .endObject()
                    )
            );

            bulkRequest.add(client.prepareIndex("twitter", "tweet", "3")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "kimchy")
                            .field("postDate", new Date())
                            .field("message", "another post")
                            .endObject()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            System.out.println(bulkResponse.buildFailureMessage());             //打印管道错误
        }


        //BulkResponse类包含 BulkItemResponse子类
        //也可以用for循环直接遍历
        //BulkItemResponse类中，和MultiGetItemResponse设计是一样的，也有Failure，这俩多半是一个人设计的。。。
    }
}
