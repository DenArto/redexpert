/*
 * RecordDataItemFactory.java
 *
 * Copyright (C) 2002-2017 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.executequery.gui.resultset;

import java.sql.Types;


public class RecordDataItemFactory {

    public RecordDataItem create(ResultSetColumnHeader header) {

        return create(header.getLabel(), header.getDataType(), header.getDataTypeName());
    }

    public RecordDataItem create(String name, int dataType, String dataTypeName) {

        switch (dataType) {
            case Types.LONGVARCHAR:
            case Types.CLOB:
                return new ClobRecordDataItem(name, dataType, dataTypeName);

            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
            case Types.BLOB:
                return new BlobRecordDataItem(name, dataType, dataTypeName);

            case Types.DATE:
            //case Types.TIME:
            case Types.TIMESTAMP:
                //case Types.TIME_WITH_TIMEZONE:
                return new DateRecordDataItem(name, dataType, dataTypeName);

            case Types.ARRAY:
                return new ArrayRecordDataItem(name, dataType, dataTypeName);

            default:
                return new SimpleRecordDataItem(name, dataType, dataTypeName);

        }

    }

}






