/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package ilarkesto.integration.gesetzeiminternet;

import ilarkesto.core.base.Parser;
import ilarkesto.core.base.Str;
import ilarkesto.law.Book;
import ilarkesto.law.Norm;
import ilarkesto.law.NormRef;
import ilarkesto.law.Section;

public class GiiBookXmlParser extends Parser {

	private Book book;
	private Section section;
	private int currentDepth;

	public GiiBookXmlParser(String data) {
		super(data);
	}

	public void parseInto(Book book) throws ParseException {
		this.book = book;
		this.section = null;
		this.currentDepth = 0;
		gotoAfter("<norm");
		gotoAfter("</norm");
		while (gotoAfterIf("<norm")) {
			parseNorm();
		}
	}

	public void parseNorm() throws ParseException {
		gotoAfter("</jurabk>");
		if (isNext("<gliederungseinheit>")) {
			gotoAfter("<gliederungskennzahl>");
			String gliederungskennzahl = getUntilAndGotoAfter("</gliederungskennzahl>");
			int depth = gliederungskennzahl.length() / 3;
			gotoAfter("<gliederungsbez>");
			String gliederungsbez = getUntilAndGotoAfter("</gliederungsbez>");
			Section s = new Section(gliederungsbez);
			// Log.TEST("Gliederung:", depth, gliederungskennzahl, gliederungsbez);
			if (depth > currentDepth) {
				if (section == null) {
					book.addSection(s);
				} else {
					section.addSection(s);
				}
				currentDepth++;
			} else if (depth == currentDepth) {
				Section parentSection = section.getParentSection();
				if (parentSection == null) {
					section.getBook().addSection(s);
				} else {
					parentSection.addSection(s);
				}
			} else if (depth < currentDepth) {
				while (depth < currentDepth) {
					section = section.getParentSection();
					currentDepth--;
				}
				Section parentSection = section.getParentSection();
				if (parentSection == null) {
					section.getBook().addSection(s);
				} else {
					parentSection.addSection(s);
				}
			}
			section = s;
			// TODO sections
			return;
		}
		gotoAfter("<enbez>");
		String enbez = getUntilAndGotoAfter("</enbez>");
		enbez = Str.removePrefix(enbez, "(XXXX) ");
		String titel = null;
		if (isBefore("<titel", "</metadaten>")) {
			gotoAfter("<titel");
			gotoAfter(">");
			titel = getUntil("</titel");
			titel = titel.replace("\n<BR/>\n", "\n");
			titel = titel.replace("<BR/>\n", "\n");
			titel = titel.replace("<BR/>", "\n");
		}
		NormRef ref = new NormRef(book.getRef().getCode(), enbez);
		Norm norm = new Norm(ref, titel);

		gotoAfter("<textdaten");
		gotoAfter("<Content>");
		String content = getUntil("</Content>");
		content = xmlToHtml(content);
		norm.setTextAsHtml(content);

		if (section == null) {
			book.addNorm(norm);
		} else {
			section.addNorm(norm);
		}
		gotoAfter("</norm>");
	}

	private String xmlToHtml(String s) {
		if (s == null) return null;
		s = s.replace("<row", "<tr");
		s = s.replace("</row>", "</tr>");

		s = s.replace("<entry", "<td");
		s = s.replace("</entry>", "</td>");

		s = s.replace("nameend=\"col2\"", "colspan=\"2\"");
		s = s.replace("nameend=\"col3\"", "colspan=\"3\"");
		s = s.replace("nameend=\"col4\"", "colspan=\"4\"");

		s = s.replace(" Type=\"TIF\"", "");
		s = s.replace(" Units=\"", "width=\"");

		return s;
	}
}
