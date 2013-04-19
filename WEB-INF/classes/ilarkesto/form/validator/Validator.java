/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package ilarkesto.form.validator;

import ilarkesto.form.ValidationException;

/**
 * TODO Comment type Validator
 * 
 * @author <A href="mailto:wi@koczewski.de">Witoslaw Koczewski </A> <BR>
 *         <code>
 * 			$Author: wko $
 * 		</code>
 * @version <code>
 * 			$Revision: 1.1 $
 *			$Date: 2006/09/13 19:07:31 $
 *		</code>
 */
public interface Validator {

    public String validate(String text) throws ValidationException;

}
