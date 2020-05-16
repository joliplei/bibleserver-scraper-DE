package org.zephyrsoft.bibleserverscraper;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyrsoft.bibleserverscraper.model.Book;
import org.zephyrsoft.bibleserverscraper.model.BookChapter;
import org.zephyrsoft.bibleserverscraper.model.Translation;

public class OpenLPExporter {
	private static final Logger LOG = LoggerFactory.getLogger(OpenLPExporter.class);

	public void export(String rawDirectory, String openlpDirectory) {
		Translation.forEach(translation -> {
			String filename = sqliteFileName(openlpDirectory, translation);
			try (Connection connection = open(filename)) {
				if (connection == null) {
					LOG.debug("not writing {}, file {} exists", translation.getAbbreviation(),
						sqliteFileName(openlpDirectory, translation));
				} else {
					init(connection, translation);
					writeContent(connection, translation, rawDirectory);
				}
			} catch (Exception e) {
				new File(filename).delete();
				LOG.warn(
					"error writing " + translation.getAbbreviation() + ", deleted partly-finished file " + filename, e);
			}
		});
	}

	private Connection open(String filename) throws SQLException {
		File file = new File(filename);
		if (file.exists()) {
			return null;
		}
		return DriverManager.getConnection("jdbc:sqlite:" + filename);
	}

	private String sqliteFileName(String directory, Translation translation) {
		return directory + File.separator + translation.getAbbreviation() + ".sqlite";
	}

	private void init(Connection connection, Translation translation) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			// create tables
			statement.addBatch(
				"CREATE TABLE book ( id INTEGER NOT NULL, book_reference_id INTEGER, testament_reference_id INTEGER, name VARCHAR(50), PRIMARY KEY (id) );");
			statement.addBatch(
				"CREATE INDEX ix_book_book_reference_id ON book (book_reference_id);");
			statement.addBatch(
				"CREATE INDEX ix_book_name ON book (name);");
			statement.addBatch(
				"CREATE TABLE metadata ( \"key\" VARCHAR(255) NOT NULL, value VARCHAR(255), PRIMARY KEY (\"key\") );");
			statement.addBatch(
				"CREATE INDEX ix_metadata_key ON metadata (\"key\");");
			statement.addBatch(
				"CREATE TABLE verse ( id INTEGER NOT NULL, book_id INTEGER, chapter INTEGER, verse INTEGER, text TEXT, PRIMARY KEY (id), FOREIGN KEY(book_id) REFERENCES book (id) );");
			statement.addBatch(
				"CREATE INDEX ix_verse_book_id ON verse (book_id);");
			statement.addBatch(
				"CREATE INDEX ix_verse_chapter ON verse (chapter);");
			statement.addBatch(
				"CREATE INDEX ix_verse_id ON verse (id);");
			statement.addBatch(
				"CREATE INDEX ix_verse_text ON verse (text);");
			statement.addBatch(
				"CREATE INDEX ix_verse_verse ON verse (verse);");
			
			// write table 'book'
			int testament = 1;
			for (int id = 1; id <= 66; id++) {
				if (id == 40) testament++;
				statement.addBatch(
					"INSERT INTO book (id,book_reference_id,testament_reference_id,name) "
						+ "VALUES ('" + id + "', '" + id + "', '" + testament + "', '" + Book.valueOf("_" + String.format("%02d", id)).getPrintNameGerman() + "');");
			}
			
			// write table 'metadata'
			statement.addBatch(
				"INSERT INTO metadata (key,value) VALUES ('language_id', '40');");
			statement.addBatch(
				"INSERT INTO metadata (key,value) VALUES ('version', '1');");
			statement.addBatch(
				"INSERT INTO metadata (key,value) VALUES ('name', '" + translation.getName() + "');");
			statement.addBatch(
				"INSERT INTO metadata (key,value) VALUES ('copyright', '');");
			statement.addBatch(
				"INSERT INTO metadata (key,value) VALUES ('permissions', '');");
			
			// commit
			statement.executeBatch();
			
		}
	}

	private void writeContent(Connection connection, Translation translation, String rawDirectory)
		throws SQLException, IOException {
		try (PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO verse (id,book_id,chapter,verse,text) VALUES (?,?,?,?,?);")) {
			int id = 0;
			for (Book book : Book.values()) {
				for (BookChapter bookChapter : book.bookChapters().collect(toList())) {
					File versesFile = new File(rawDirectory + File.separator + translation.fileNameOf(bookChapter));
					List<String> verses = Files.readAllLines(versesFile.toPath(), StandardCharsets.UTF_8);

					int verseNumber = 0;
					for (String verse : verses) {
						if (verse == null || verse.trim().equals("")) {
							continue;
						}
						verseNumber++;
						id++;
						statement.setInt(1, id);
						statement.setInt(2, bookChapter.getBook().getOrdinalNumber());
						statement.setInt(3, bookChapter.getChapter());
						statement.setInt(4, verseNumber);
						statement.setString(5, verse);
						statement.addBatch();
					}
				}
				statement.executeBatch();
				LOG.debug("wrote {}, {}", translation.getAbbreviation(), book.getNameGerman());
			}
		}
	}

}
