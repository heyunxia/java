package com.heyunxia.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKTokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-12-07 上午9:40
 */
public class LuceneDemo {

    public static final String sourcePath = "/data/work/git/java/lucene/src/main/resources/luceneSource/f";
    public static final String indexPath = "/data/work/git/java/lucene/src/main/resources/luceneIndex";

    public static final Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_36);


    public static void main(String[] args) throws Exception {

        //createIndex();
        //indexSearch();

        //Analyzer enAnalyzer = new SimpleAnalyzer(Version.LUCENE_36);
        Analyzer enAnalyzer = new StandardAnalyzer(Version.LUCENE_36);

        String enText = "Finagle is an extensible RPC system for the JVM, used to construct high-concurrency servers. Finagle implements uniform client and server APIs for several protocols, and is designed for high performance and concurrency. Most of Finagle’s code is protocol agnostic, simplifying the implementation of new protocols.";
        //analyzer(enAnalyzer, enText);
        String zhText = "原型模式虽然是创建型的模式，但是与工程模式没有关系，从名字即可看出，该模式的思想就是将一个对象作为原型，对其进行复制、克隆，产生一个和原对象类似的新对象。本小结会通过对象的复制，进行讲解。在Java中，复制对象是通过clone()实现的，先创建一个原型类";

        //analyzer(new CJKAnalyzer(Version.LUCENE_36), zhText);
        analyzer(new IKAnalyzer(true), zhText);
    }

    public static void analyzer(Analyzer analyzer, String text) throws Exception {

        TokenStream tokenStream =  analyzer.tokenStream("content", new StringReader(text));
        CharTermAttribute charTerm = tokenStream.addAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.println(charTerm);
        }
    }

    public static void createIndex() throws IOException {

        File file = new File(sourcePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        //Directory directory = new SimpleFSDirectory(new File(indexPath), new SingleInstanceLockFactory());
        Directory directory = FSDirectory.open(new File(indexPath), new SingleInstanceLockFactory());
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter indexWriter = new IndexWriter(directory, writerConfig);

        Document doc = null;
        String line;
        while ((line = reader.readLine()) != null) {
            doc = new Document();
            doc.add(new Field("name", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("content", line, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("size", String.valueOf(file.length()), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("path", file.getPath(), Field.Store.YES, Field.Index.NO));
            indexWriter.addDocument(doc, analyzer);
        }


        indexWriter.close();


    }

    public static void indexSearch() throws IOException, ParseException {
        IndexReader indexReader = IndexReader.open(FSDirectory.open(new File(indexPath)));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new QueryParser(Version.LUCENE_36, "content", analyzer).parse("lucene");

        Filter filter = null;
        TopFieldDocs topFieldDocs = indexSearcher.search(query, filter, 100, Sort.INDEXORDER);
        System.out.println("查中的行数：" + topFieldDocs.totalHits);
        ScoreDoc[] scoreDocs = topFieldDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docSn = scoreDoc.doc;
            Document document = indexSearcher.doc(docSn);
            System.out.println(document.get("content"));
        }

        indexSearcher.close();
    }
}
