package org.zephyrsoft.bibleserverscraper.model;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum Book {

	_01("1.Mose", "Genesis", "Génesis", "GEN", 50),
	_02("2.Mose", "Exodus", "Éxodo", "EXO", 40),
	_03("3.Mose", "Leviticus", "Levitico", "LEV", 27),
	_04("4.Mose", "Numbers", "Números", "NUM", 36),
	_05("5.Mose", "Deuteronomy", "Deuteronomio", "DEU", 34),
	_06("Josua", "Joshua", "Josué", "JOS", 24),
	_07("Richter", "Judges", "Jueces", "JDG", 21),
	_08("Rut", "Ruth", "Rut", "RUT", 4),
	_09("1.Samuel", "1 Samuel", "1 Samuel", "1SA", 31),
	_10("2.Samuel", "2 Samuel", "2 Samuel", "2SA", 24),
	_11("1.Könige", "1 Kings", "1 Reyes", "1KI", 22),
	_12("2.Könige", "2 Kings", "2 Reyes", "2KI", 25),
	_13("1.Chronik", "1 Chronicles", "1 Crónicas", "1CH", 29),
	_14("2.Chronik", "2 Chronicles", "2 Crónicas", "2CH", 36),
	_15("Esra", "Ezra", "Esdras", "EZR", 10),
	_16("Nehemia", "Nehemiah", "Nehemías", "NEH", 13),
	_17("Esther", "Esther", "Ester", "EST", 10),
	_18("Hiob", "Job", "Job", "JOB", 42),
	_19("Psalmen", "Psalms", "Salmos", "PSA", 150),
	_20("Sprüche", "Proverbs", "Proverbios", "PRO", 31),
	_21("Prediger", "Ecclesiastes", "Eclesiastés", "ECC", 12),
	_22("Hoheslied", "Song of Solomon", "Cantares", "SNG", 8),
	_23("Jesaja", "Isaiah", "Isaías", "ISA", 66),
	_24("Jeremia", "Jeremiah", "Jeremías", "JER", 52),
	_25("Klagelieder", "Lamentations", "Lamentaciones", "LAM", 5),
	_26("Hesekiel", "Ezekiel", "Ezequiel", "EZK", 48),
	_27("Daniel", "Daniel", "Daniel", "DAN", 14),
	_28("Hosea", "Hosea", "Oseas", "HOS", 14),
	_29("Joel", "Joel", "Joel", "JOL", 4),
	_30("Amos", "Amos", "Amós", "AMO", 9),
	_31("Obadja", "Obadiah", "Abdías", "OBA", 1),
	_32("Jona", "Jonah", "Jonás", "JON", 4),
	_33("Micha", "Micah", "Miqueas", "MIC", 7),
	_34("Nahum", "Nahum", "Nahum", "NAH", 3),
	_35("Habakuk", "Habbakuk", "Habacuc", "HAB", 3),
	_36("Zefanja", "Zephaniah", "Sofonías", "ZEP", 3),
	_37("Haggai", "Haggai", "Hageo", "HAG", 2),
	_38("Sacharja", "Zechariah", "Zacarías", "ZEC", 14),
	_39("Maleachi", "Malachi", "Malaquías", "MAL", 3),
	_40("Matthäus", "Matthew", "Mateo", "MAT", 28),
	_41("Markus", "Mark", "Marcos", "MRK", 16),
	_42("Lukas", "Luke", "Lucas", "LUK", 24),
	_43("Johannes", "John", "Juan", "JHN", 21),
	_44("Apostelgeschichte", "Acts", "Hechos", "ACT", 28),
	_45("Römer", "Romans", "Romanos", "ROM", 16),
	_46("1.Korinther", "1 Corinthians", "1 Corintios", "1CO", 16),
	_47("2.Korinther", "2 Corinthians", "2 Corintios", "2CO", 13),
	_48("Galater", "Galatians", "Gálatas", "GAL", 6),
	_49("Epheser", "Ephesians", "Efesios", "EPH", 6),
	_50("Philipper", "Philippians", "Filipenses", "PHP", 4),
	_51("Kolosser", "Colossians", "Colosenses", "COL", 4),
	_52("1.Thessalonicher", "1 Thessalonians", "1 Tesalonicenses", "1TH", 5),
	_53("2.Thessalonicher", "2 Thessalonians", "2 Tesalonicenses", "2TH", 3),
	_54("1.Timotheus", "1 Timothy", "1 Timoteo", "1TI", 6),
	_55("2.Timotheus", "2 Timothy", "2 Timoteo", "2TI", 4),
	_56("Titus", "Titus", "Tito", "TIT", 3),
	_57("Philemon", "Philemon", "Filemón", "PHM", 1),
	_58("Hebräer", "Hebrews", "Hebreos", "HEB", 13),
	_59("Jakobus", "James", "Santiago", "JAS", 5),
	_60("1.Petrus", "1 Peter", "1 Pedro", "1PE", 5),
	_61("2.Petrus", "2 Peter", "2 Pedro", "2PE", 3),
	_62("1.Johannes", "1 John", "1 Juan", "1JN", 5),
	_63("2.Johannes", "2 John", "2 Juan", "2JN", 1),
	_64("3.Johannes", "3 John", "3 Juan", "3JN", 1),
	_65("Judas", "Jude", "Judas", "JUD", 1),
	_66("Offenbarung", "Revelation", "Apocalipsis", "REV", 22);

	public static Stream<Book> books() {
		return Stream.of(values());
	}

	private String nameGerman;
	private String nameEnglish;
	private String nameSpanish;
	private String urlAbbreviation;
	private int chapters;

	private Book(String nameGerman, String nameEnglish, String nameSpanish, String urlAbbreviation, int chapters) {
		this.nameGerman = nameGerman;
		this.nameEnglish = nameEnglish;
		this.nameSpanish = nameSpanish;
		this.urlAbbreviation = urlAbbreviation;
		this.chapters = chapters;
	}

	public String getNameGerman() {
		return nameGerman;
	}

	public String getPrintNameGerman() {
		return getNameGerman()
			.replaceAll("^([1-5]\\.)([A-Z])", "$1 $2");
	}

	public String getNameForUrl() {
		return urlAbbreviation;
	}

	public String getOrdinal() {
		return name().replace("_", "");
	}

	public int getOrdinalNumber() {
		return Integer.parseInt(getOrdinal());
	}

	public Stream<BookChapter> bookChapters() {
		if (chapters == 1) {
			return Stream.of(new BookChapter(this, 1, nameGerman, nameEnglish, nameSpanish));
		} else {
			return IntStream.range(1, chapters + 1)
				.mapToObj(chapter -> new BookChapter(this, chapter, nameGerman + chapter, nameEnglish + chapter, nameSpanish + chapter));
		}
	}

}
