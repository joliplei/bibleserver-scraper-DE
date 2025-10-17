package org.zephyrsoft.bibleserverscraper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyrsoft.bibleserverscraper.model.Book;
import org.zephyrsoft.bibleserverscraper.model.BookChapter;
import org.zephyrsoft.bibleserverscraper.model.ChapterScrapeResult;
import org.zephyrsoft.bibleserverscraper.model.Translation;

import org.htmlunit.BrowserVersion;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlPage;

public class Scraper {
	private static final Logger LOG = LoggerFactory.getLogger(Scraper.class);

	private Random random = new Random();

	public void scrape(String directory) {
		try (WebClient client = new WebClient(BrowserVersion.CHROME)) {
			client.getOptions().setCssEnabled(false);
			client.getOptions().setJavaScriptEnabled(true);
			client.getOptions().setHistoryPageCacheLimit(1);
			client.getOptions().setThrowExceptionOnScriptError(false);

			AtomicBoolean allScrapedSuccessfully = new AtomicBoolean(true);
			do {
				Translation.forEach(translation -> Book.books()
					.flatMap(book -> book.bookChapters())
					.forEach(bookChapter -> {
						ChapterScrapeResult chapterScrapeResult = scrapeChapter(directory, client, translation, bookChapter);
						if (!chapterScrapeResult.wasSuccessful()) {
							allScrapedSuccessfully.set(false);
						}
						if (chapterScrapeResult.shouldWait()) {
							sleepRandomTime();
						}
					}));
			} while (!allScrapedSuccessfully.get());
		}
	}

	private ChapterScrapeResult scrapeChapter(String directory, WebClient client, Translation translation,
			BookChapter bookChapter) {
		String translationAbbreviation = translation.getAbbreviation();
		String bookChapterName = bookChapter.getPrintNameGerman();

		File targetFile = new File(directory + File.separator + translationAbbreviation + "-"
				+ bookChapter.getBook().getOrdinal() + "-" + bookChapter.getNameGerman() + ".txt"); // files always named
																									// in German
		if (targetFile.exists()) {
			LOG.debug("not fetching {} in {}, file {} exists", bookChapterName, translationAbbreviation, targetFile);
			return new ChapterScrapeResult(false, true);
		} else {
			try {
				LOG.debug("fetching {} in {}", bookChapterName, translationAbbreviation);
				String searchUrl = "https://www.die-bibel.de/bibel/" + translation.getAbbreviation() + "/"
						+ bookChapter.getBook().getNameForUrl() + "." + bookChapter.getChapter();
				HtmlPage page = client.getPage(searchUrl);

				if (page.isHtmlPage()) {
					handleChapter(targetFile, (HtmlPage) page);
					return new ChapterScrapeResult(true, true);
				} else {
					LOG.warn("result was not HTML: {}", page.getWebResponse().getContentAsString(StandardCharsets.UTF_8));
					return new ChapterScrapeResult(true, false);
				}
			} catch (Exception e) {
				LOG.warn("error fetching " + bookChapterName + " in " + translationAbbreviation, e);
				return new ChapterScrapeResult(true, false);
			}
		}
	}

	private void handleChapter(File targetFile, HtmlPage page) throws IOException {
		List<DomNode> paragraphs = page.querySelectorAll("p.m");
		if (paragraphs.isEmpty()) {
			throw new IllegalStateException("no content found");
		} else {
			List<String> verses = new ArrayList<>();
			StringBuilder currentVerse = new StringBuilder();

			for (DomNode p : paragraphs) {
				boolean newVerse = !p.querySelectorAll("bible-v > sub.v").isEmpty();
				if (newVerse && currentVerse.length() > 0) {
					String text = currentVerse.toString();
					text = text.replaceAll("\\s+", " ").replaceAll(" ([,.:;!?])", "$1").trim();
					text = text.replaceAll("^\\d+\\s*", "");
					if (!text.isEmpty()) {
						verses.add(text);
					}
					currentVerse.setLength(0);
				}
				currentVerse.append(p.getTextContent()).append(" ");
			}

			// Add the last verse
			if (currentVerse.length() > 0) {
				String text = currentVerse.toString();
				text = text.replaceAll("\\s+", " ").replaceAll(" ([,.:;!?])", "$1").trim();
				text = text.replaceAll("^\\d+\\s*", "");
				if (!text.isEmpty()) {
					verses.add(text);
				}
			}

			Files.write(targetFile.toPath(), verses, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
		}
	}

	private void sleepRandomTime() {
		try {
			int seconds = random.nextInt(2) + 1;
			LOG.debug("waiting for {} seconds", seconds);
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// do nothing
		}
	}

}
