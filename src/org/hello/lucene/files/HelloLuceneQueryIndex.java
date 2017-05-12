package org.hello.lucene.files;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

public class HelloLuceneQueryIndex {
	private static StandardAnalyzer analyzer = new StandardAnalyzer();

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		String indexLocation = "C:/Users/borislav.rangelov/Documents/Corpora/Index";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
				indexLocation).toPath()));
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(5);

		//String searchQuery = "madafaka";
		String searchQuery = "TrayIcon";
		Query q = new QueryParser("contents", analyzer).parse(searchQuery);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("path") + " score="
					+ hits[i].score);
		}
	}

}
