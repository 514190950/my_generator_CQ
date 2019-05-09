/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.cqElements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class UpdateSqlElementGenerator extends
        AbstractXmlElementGenerator {

    public UpdateSqlElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", "update_sql")); //$NON-NLS-1$
        answer.addElement(new TextElement("UPDATE <include refid=\"tableName\"/>"));
        //     context.getCommentGenerator().addComment(answer);
        XmlElement setElement = new XmlElement("set");
        answer.addElement(setElement);

        for (IntrospectedColumn baseColumn : introspectedTable.getBaseColumns()) {
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(baseColumn);
            String cqParameterClause = MyBatis3FormattingUtilities.getCQParameterClause(baseColumn);
            XmlElement ifElement = new XmlElement("if");
            ifElement.addAttribute(new Attribute("test", columnName+" !=null"));
            ifElement.addElement(new TextElement(columnName+" = "+cqParameterClause+","));
            setElement.addElement(ifElement);
        }



        if (context.getPlugins()
                .sqlMapUpdateByExampleSelectiveElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
