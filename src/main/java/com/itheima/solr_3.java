package com.itheima;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class solr_3 {
    @Test
    public void seacherIndex()throws Exception{
        String url = "http://localhost:8080/solr/products/";
        HttpSolrServer solrServer = new HttpSolrServer(url);
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        QueryResponse response = solrServer.query(solrQuery);
        SolrDocumentList results = response.getResults();
        System.out.println("命中的文档总数是："+results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("product_name"));
            System.out.println(result.get("product_price"));
        }


    }

    @Test
    public void queryIndex()throws Exception{
        String url = "http://localhost:8080/solr/products/";
        HttpSolrServer solrServer = new HttpSolrServer(url);
        SolrQuery solrQuery = new SolrQuery();
         solrQuery.setQuery("浴巾");
         solrQuery.addFilterQuery("product_catalog_name:时尚卫浴");
         solrQuery.addFilterQuery("product_price:[1 TO 20]");
         solrQuery.setSort("product_price",SolrQuery.ORDER.desc);
         solrQuery.setStart(1);
         solrQuery.setRows(6);

         solrQuery.setHighlight(true);
         solrQuery.addHighlightField("product_name");
         solrQuery.setHighlightSimplePre("<font color = 'red'>");
         solrQuery.setHighlightSimplePost("</font>");

         solrQuery.set("df","product_keywords");

        QueryResponse query = solrServer.query(solrQuery);
        SolrDocumentList results = query.getResults();
        long numFound = results.getNumFound();
        System.out.println("命中的总记录数是："+numFound);

        for (SolrDocument result : results) {
            String  id = (String)result.get("id");
            System.out.println("文档的id是："+id);
            String  product_name = (String)result.get("product_name");
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            Map<String, List<String>> map = highlighting.get(id);
            List<String> list = map.get(product_name);
            if(list!=null && list.size()>0){
                product_name = list.get(0);

            }
            System.out.println("商品的名称是："+product_name);
            float product_price = (Float) result.get("product_price");
            System.out.println("商品的价格是："+product_price);
            String product_description = (String)result.get("product_description");
            System.out.println("商品描述是："+product_description);
            String product_picture =(String) result.get("product_picture");
            System.out.println("商品图片"+product_picture);
            String product_catalog_name = (String)result.get("product_catalog_name");
            System.out.println("商品的类别是："+product_catalog_name);


        }


    }
}
