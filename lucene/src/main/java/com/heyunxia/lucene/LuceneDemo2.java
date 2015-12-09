package com.heyunxia.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.apache.lucene.util.Version;

import java.io.*;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-12-07 上午9:40
 */
public class LuceneDemo2 {

    public static final String sourcePath = "/data/work/git/java/lucene/src/main/resources/luceneSource/f";
    public static final String indexPath = "/data/work/git/java/lucene/src/main/resources/luceneIndex";

    public static final Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_36);


    public static void main(String[] args) throws IOException, ParseException {

        //createIndex();
        indexSearch();
    }

    public static void createIndex() throws IOException {

        File file = new File(sourcePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        Directory directory = FSDirectory.open(new File(indexPath));
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, writerConfig);


        Document doc;
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            //buffer.append(line).append("\n");
            doc = new Document();
            doc.add(new Field("name", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("content", line, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("size", String.valueOf(file.length()), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("path", file.getPath(), Field.Store.YES, Field.Index.NO));
            indexWriter.addDocument(doc);
        }
        //System.out.println(buffer);


        indexWriter.close();


    }

    public static void indexSearch() throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(indexPath));
        IndexReader indexReader = IndexReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new QueryParser(Version.LUCENE_36, "content", analyzer).parse("lucene");

        //query.add(new Term("content", "dictionary"));
        Filter filter = null;
        TopFieldDocs topFieldDocs = indexSearcher.search(query, filter, 100, Sort.INDEXORDER);
        System.out.println("查中的行数：" + topFieldDocs.totalHits);
        SortField[] sortFields = topFieldDocs.fields;
        ScoreDoc[] scoreDocs = topFieldDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docSn =  scoreDoc.doc;
            Document document = indexSearcher.doc(docSn);
            System.out.println(document.get("content"));
        }

        indexSearcher.close();
    }
}
