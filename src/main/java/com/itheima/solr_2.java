package com.itheima;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class solr_2 {

    //删除指定的索引

    @Test
    public void deleteIndex()throws Exception{
        FSDirectory directory = FSDirectory.open(new File("D:\\lucene\\index"));
        IndexWriterConfig indexWriterConfig =  new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
     //   indexWriter.deleteDocuments(new Term("id","20"));
        indexWriter.deleteAll();
        indexWriter.close();


    }

    @Test
    public void updateIndex()throws Exception{
        FSDirectory directory = FSDirectory.open(new File("D:\\lucene\\index"));
        IndexWriterConfig indexWriterConfig =  new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        Document document = new Document();
        document.add(new TextField("id", "1002", Field.Store.YES));
        document.add(new TextField("title", "lucene测试test 002", Field.Store.YES));
        //更新会先查找后删除新增
        indexWriter.updateDocument(new Term("title","test"),document);
        indexWriter.close();
    }
}
