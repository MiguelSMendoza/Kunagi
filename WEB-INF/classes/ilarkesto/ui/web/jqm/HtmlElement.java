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

public class HtmlElement extends AElement {

	private HtmlRenderer renderer;

	public HtmlElement(JqmHtmlPage htmlPage) {
		super(htmlPage);
		renderer = new HtmlRenderer();
		// renderer.setIndentationDepth(indentationDepth);
	}

	public HtmlRenderer getRenderer() {
		return renderer;
	}

	@Override
	protected void render(HtmlRenderer html, String id) {
		if (id == null) {
			html.html(getRenderer().toString());
			return;
		}
		html.startSPAN(null).setId(id);
		html.html(getRenderer().toString());
		html.endSPAN();
	}

}
