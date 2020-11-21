package pers.pudgebd.java.elasticSearch.javaApi;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import pers.pudgebd.java.elasticSearch.basic.JDeepInSearchBasic;
import pers.pudgebd.java.utils.constants.EsCnts;
import pers.pudgebd.java.utils.constants.PropName;

import java.net.InetAddress;

public class JDeepInSearch extends JDeepInSearchBasic {


    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put(PropName.CLUSTER_NAME(), EsCnts.MY_DEV()).build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(EsCnts.DN6()), EsCnts.J_PORT()))
                .addTransportAddress(new TransportAddress(InetAddress.getByName(EsCnts.DN7()), EsCnts.J_PORT()))
                .addTransportAddress(new TransportAddress(InetAddress.getByName(EsCnts.DN8()), EsCnts.J_PORT()));

//        scrollSearch(client);
        termSearch(client);
//        boolSearch(client);
    }




}
