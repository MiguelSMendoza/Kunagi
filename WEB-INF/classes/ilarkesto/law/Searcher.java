package ilarkesto.law;

import ilarkesto.core.logging.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Searcher implements Runnable {

	private static Log log = Log.get(Searcher.class);

	private SearchResultConsumer consumer;
	private BookCacheCollection bookCaches;
	private List<String> searchStrings;

	private boolean stopRequested;
	private BookRef restrictToBook;

	public Searcher(String query, SearchResultConsumer consumer, BookCacheCollection bookCaches) {
		super();
		this.searchStrings = parseQuery(query);
		this.consumer = consumer;
		this.bookCaches = bookCaches;
	}

	@Override
	public void run() {
		searchRestrictedBook();
		if (stopRequested) return;
		if (searchStrings.isEmpty()) return;
		if (restrictToBook != null) {
			log.info("Searching for norms in identified book:", restrictToBook, "->", searchStrings);
			searchForNorms(restrictToBook);
		} else {
			log.info("Searching for books:", searchStrings);
			searchForBooks();
		}

		// if (!searchStrings.isEmpty()) {
		// log.info("Searching for norms in identified books", booksIdentifiedByCode, "->", searchStrings);
		// searchForNorms(booksIdentifiedByCode);
		// }

		consumer.onSearchFinished();
		log.info("Search finished:", searchStrings);
	}

	private void searchRestrictedBook() {
		BookIndex index = bookCaches.getBookIndexCache().getPayload();
		for (String code : searchStrings) {
			restrictToBook = index.getBookByCode(code);
			if (restrictToBook != null) {
				searchStrings.remove(code);
				consumer.onBookFound(restrictToBook);
				return;
			}
		}
	}

	private void searchForNorms(Collection<BookRef> bookRefs) {
		List<BookCache> unloadedCaches = new LinkedList<BookCache>();
		for (BookRef bookRef : bookRefs) {
			if (stopRequested) return;
			BookCache cache = bookCaches.getBookCache(bookRef);
			Book book = cache.getPayload();
			if (book == null) {
				unloadedCaches.add(cache);
			} else {
				searchForNorms(book);
			}
			cache.unload();
		}
		// for (BookCache cache : unloadedCaches) {
		// if (stopRequested) return;
		// Book book = cache.getPayload_ButUpdateIfNull();
		// if (book != null) searchForNorms(book);
		// cache.unload();
		// }
	}

	private void searchForNorms(BookRef bookRef) {
		Book book = bookCaches.getBookCache(bookRef).getPayload_ButUpdateIfNull();
		searchForNorms(book);
	}

	private void searchForNorms(Book book) {
		Set<String> alreadyFoundCodes = new HashSet<String>();
		List<Norm> norms = book.getAllNorms();
		for (Norm norm : norms) {
			if (stopRequested) return;
			if (searchStrings.size() == 1 && norm.getRef().isCodeNumber(searchStrings.get(0))) {
				consumer.onNormFound(norm);
				String code = norm.getRef().getCode();
				alreadyFoundCodes.add(code);
				continue;
			}
		}
		for (Norm norm : norms) {
			if (stopRequested) return;
			String code = norm.getRef().getCode();
			if (alreadyFoundCodes.contains(code)) continue;
			if (matchesSearch(code) || matchesSearch(norm.getTextAsString())) {
				consumer.onNormFound(norm);
				alreadyFoundCodes.add(code);
				continue;
			}
		}
	}

	private void searchForBooks() {
		BookIndex index = bookCaches.getBookIndexCache().getPayload();
		if (index == null) return;
		for (BookRef book : index.getBooks()) {
			if (stopRequested) return;
			checkBook(book);
		}
	}

	private void checkBook(BookRef book) {
		if (matchesSearch(book.getCode()) || matchesSearch(book.getTitle())) {
			consumer.onBookFound(book);
			return;
		}
	}

	private boolean matchesSearch(String test) {
		if (test == null) return false;
		if (searchStrings.isEmpty()) return false;
		test = test.toLowerCase();
		for (String s : searchStrings) {
			if (!test.contains(s)) return false;
		}
		return true;
	}

	public void setStopRequested(boolean stopRequested) {
		this.stopRequested = stopRequested;
	}

	private List<String> parseQuery(String searchString) {
		List<String> ret = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(searchString, " \t\n\r,.|§");
		while (tokenizer.hasMoreTokens()) {
			ret.add(tokenizer.nextToken().toLowerCase());
		}
		return ret;
	}

	public static interface SearchResultConsumer {

		void onBookFound(BookRef book);

		void onNormFound(Norm norm);

		void onSearchFinished();

	}

}
