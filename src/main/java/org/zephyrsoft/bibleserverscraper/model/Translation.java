package org.zephyrsoft.bibleserverscraper.model;

import java.util.function.Consumer;
import java.util.stream.Stream;

public enum Translation {

	BB("BB", "BasisBibel"),
	NGUE("NGUE", "Neue Genfer Übersetzung"),
	LU84("LU84", "Lutherbibel 1984");

	private String abbreviation;
	private String name;

	private Translation(String abbreviation, String name) {
		this.abbreviation = abbreviation;
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getName() {
		return name;
	}

	public String fileNameOf(BookChapter bookChapter) {
		return abbreviation + "-" + bookChapter.getBook().getOrdinal() + "-" + bookChapter.getNameGerman() + ".txt";
	}

	public static Stream<Translation> stream() {
		return Stream.of(values());
	}

	public static void forEach(Consumer<Translation> action) {
		stream().forEach(action);
	}
}
