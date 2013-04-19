/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package scrum.client.collaboration;

import ilarkesto.gwt.client.TableBuilder;
import ilarkesto.gwt.client.editor.RichtextEditorWidget;
import scrum.client.ScrumGwt;
import scrum.client.common.ABlockWidget;
import scrum.client.common.BlockHeaderWidget;
import scrum.client.common.BlockWidgetFactory;
import scrum.client.journal.ActivateChangeHistoryAction;
import scrum.client.journal.ChangeHistoryWidget;

import com.google.gwt.user.client.ui.Widget;

public class WikipageBlock extends ABlockWidget<Wikipage> {

	@Override
	protected void onInitializationHeader(BlockHeaderWidget header) {
		Wikipage page = getObject();
		header.addText(page.getLabelModel());
		header.appendOuterCell(new EmoticonsWidget(page), null, true);
		header.addMenuAction(new ActivateChangeHistoryAction(page));
		header.addMenuAction(new DeleteWikipageAction(page));
	}

	@Override
	protected void onUpdateHeader(BlockHeaderWidget header) {
		Wikipage page = getObject();
		header.setDragHandle(page.getReference());
	}

	@Override
	protected Widget onExtendedInitialization() {
		return TableBuilder.row(20, createLeftColumn(), ScrumGwt.createEmoticonsAndComments(getObject()));
	}

	private Widget createLeftColumn() {
		Wikipage page = getObject();
		TableBuilder tb = ScrumGwt.createFieldTable();
		tb.addRow(new RichtextEditorWidget(page.getTextModel()), 2);
		tb.addFieldRow("Name", page.getNameModel());
		tb.addRow(new ChangeHistoryWidget(page), 2);
		return tb.createTable();
	}

	public static final BlockWidgetFactory<Wikipage> FACTORY = new BlockWidgetFactory<Wikipage>() {

		@Override
		public WikipageBlock createBlock() {
			return new WikipageBlock();
		}

	};

}
