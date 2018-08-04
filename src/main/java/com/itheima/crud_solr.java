package com.itheima;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;


public class crud_solr {
    @Test
    public void testCreateIndexBySolr() throws Exception{

        String baseURl="http://localhost:8080/solr/article";
        HttpSolrServer solrServer = new HttpSolrServer(baseURl);
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","1");
        document.addField("title","华为手机");
        document.addField("description","国产的骄傲。太牛了哈哈哈");
        solrServer.add(document);
        solrServer.commit();
    }
    @Test
    public void deleteIndexBySolr()throws Exception {
        String baseURl="http://localhost:8080/solr";
        HttpSolrServer solrServer = new HttpSolrServer(baseURl);

        solrServer.deleteById("1");
        solrServer.commit();
    }
}
