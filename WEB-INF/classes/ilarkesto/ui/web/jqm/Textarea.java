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
package ilarkesto.ui.web.jqm;

import ilarkesto.ui.web.HtmlRenderer;

public class Textarea extends AFieldElement {

	private String value;

	public Textarea(JqmHtmlPage htmlPage, String id, String label) {
		super(htmlPage, id, label);
	}

	@Override
	protected void renderElement(HtmlRenderer html) {
		html.TEXTAREA(name, id, value);
	}

	public Textarea setValue(String value) {
		this.value = value;
		return this;
	}

}
