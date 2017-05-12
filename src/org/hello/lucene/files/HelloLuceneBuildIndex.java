package org.hello.lucene.files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class HelloLuceneBuildIndex {

	private static StandardAnalyzer analyzer = new StandardAnalyzer();

	private IndexWriter writer;

	/**
	 * Constructor
	 * 
	 * @param indexDir
	 *            the name of the folder in which the index should be created
	 * @throws java.io.IOException
	 *             when exception creating index.
	 */
	HelloLuceneBuildIndex(String indexDir) throws IOException {
		// the boolean true parameter means to create a new index everytime,
		// potentially overwriting any existing files there.
		FSDirectory dir = FSDirectory.open(new File(indexDir).toPath());

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		writer = new IndexWriter(dir, config);
	}

	public void indexFile(File fileToIndex) throws IOException {
		FileReader fr = null;
		try {
			Document doc = new Document();

			// ===================================================
			// add contents of file
			// ===================================================
			fr = new FileReader(fileToIndex);
			doc.add(new TextField("contents", fr));
			doc.add(new StringField("path", fileToIndex.getPath(),
					Field.Store.YES));
			doc.add(new StringField("filename", fileToIndex.getName(),
					Field.Store.YES));

			writer.addDocument(doc);
			System.out.println("Added: " + fileToIndex);
		} catch (Exception e) {
			System.out.println("Could not add: " + fileToIndex);
		} finally {
			fr.close();
		}
	}

	/**
	 * Close the index.
	 * 
	 * @throws java.io.IOException
	 *             when exception closing
	 */
	public void closeIndex() throws IOException {
		writer.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		HelloLuceneBuildIndex indexer = null;
		String indexLocation = "C:/Users/borislav.rangelov/Documents/Corpora/Index";
		try {
			indexer = new HelloLuceneBuildIndex(
					indexLocation);
		} catch (Exception ex) {
			System.out.println("Cannot create index..." + ex.getMessage());
			System.exit(-1);
		}

		File folderToIndex = new File(
				"C:/Users/borislav.rangelov/Documents/Corpora");
		File[] filesToIndex = folderToIndex.listFiles();

		for (File nextFile : filesToIndex) {
			if (nextFile.isFile()) {
				System.out.println("Trying to index file: "
						+ nextFile.getPath());
				indexer.indexFile(nextFile);
			}
		}

		// ===================================================
		// after adding, we always have to call the
		// closeIndex, otherwise the index is not created
		// ===================================================
		indexer.closeIndex();

	}

}
