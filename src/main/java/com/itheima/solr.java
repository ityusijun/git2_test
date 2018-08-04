package com.itheima;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class solr {

    @Test
    public void createIndex() throws IOException {

        String path = "D:\\lucene\\index";
        FSDirectory directory = FSDirectory.open(new File(path));
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory,writerConfig);

        for (int i = 0; i < 30; i++) {
            Document document = new Document();
            document.add(new StringField("id",""+i,Field.Store.YES));
             TextField title= new TextField("title","lucene入门",Field.Store.YES);

            if(i ==22){
                title.setBoost(222f);
            }
            document.add(title);
            document.add(new TextField("content","lucene入门就随便写点内容吧",Field.Store.YES));
            document.add(new TextField("desc","lucene入门很简单",Field.Store.YES));


            indexWriter.addDocument(document);


        }
        indexWriter.commit();
        indexWriter.close();


    }


    //只能对一个字段查询
    @Test
    public void queryIndex() throws Exception {
        String text = "lucene入门";
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse(text);
        executeAndPrintResult(query);

    }
    //对多个字段进行检索
    @Test
    public void multityFiledQuery() throws Exception {
        String text = "lucene入门";
        //QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new 
                String[]{"content", "desc"}, new IKAnalyzer());
        Query query = queryParser.parse(text);
        executeAndPrintResult(query);

    }

    //根据词条进行检索不会再分词，词条为最小单位
    @Test
    public void termQuery() throws Exception {
       // String text = "lucene入门";
        String text = "lucene";
        TermQuery query = new TermQuery(new Term("content",text));
        executeAndPrintResult(query);

    }

    //模糊查询
   /* 关键词+”*”    表示是以什么开始  * 表示所有
    ”*”+关键词    以什么结尾
    ”*”+关键词+”*” 表示包含关键词查询
    ”?”+关键词*/

    @Test
    public void wildcardQuery() throws Exception {
        String text = "入门";

        WildcardQuery query = new WildcardQuery(new Term("content",text+"*"));
        executeAndPrintResult(query);

    }
    //相似度检索 最多只用两个不一样的字符
    @Test
    public void FuzzyQuery() throws Exception {
        String text = "lucPne";

        Query query = new FuzzyQuery(new Term("content",text));
        executeAndPrintResult(query);

    }
    //NumericQuery 数字范围查询
    @Test
    public void NumericRangeQuery() throws Exception {

        NumericRangeQuery query = NumericRangeQuery.newIntRange("id",1,10,true,true);
        executeAndPrintResult(query);

    }
    //查询所用
    @Test
    public void MatchAllDocdQuery() throws Exception {

        MatchAllDocsQuery query = new MatchAllDocsQuery();
        executeAndPrintResult(query);

    }

           /*  1、MUST和MUST表示“与”的关系，即“交集”，相当与AND。
                2、MUST和MUST_NOT前者包含后者不包含。
                3、MUST_NOT和MUST_NOT没意义
                4、SHOULD与MUST表示MUST，SHOULD失去意义；
                5、SHOULD与MUST_NOT相当于MUST与MUST_NOT。
                6、SHOULD与SHOULD表示“或”的关系，相当与OR。
            */

    @Test
    public void booleanQuery()throws Exception{
        BooleanQuery booleanQuery = new BooleanQuery();
        NumericRangeQuery query = NumericRangeQuery.newIntRange("id",1,10,true,true);
        MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
        booleanQuery.add(query,BooleanClause.Occur.MUST);
       // booleanQuery.add(matchAllDocsQuery,BooleanClause.Occur.MUST_NOT);
        booleanQuery.add(matchAllDocsQuery,BooleanClause.Occur.MUST);
        executeAndPrintResult(booleanQuery);
    }
    @Test
    public void setBoot() throws Exception {
        String text = "lucene入门";
        QueryParser queryParser = new QueryParser("title",new IKAnalyzer());
        Query query = queryParser.parse(text);
        executeAndPrintResult(query);

    }
    public void executeAndPrintResult(Query query)throws Exception{
        FSDirectory directory = FSDirectory.open(new File("D:\\lucene\\index"));
        IndexReader indexReader= DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs search = indexSearcher.search(query, Integer.MAX_VALUE);
        System.out.println("总记录数是："+search.totalHits);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc s: scoreDocs) {
            System.out.println("得分"+s.score);
            Document doc = indexSearcher.doc(s.doc);
            System.out.println("id:"+doc.get("id"));
            System.out.println("title:"+doc.get("title"));
            System.out.println("content:"+doc.get("content"));
            System.out.println("desc:"+doc.get("desc"));
        }

    }
}
