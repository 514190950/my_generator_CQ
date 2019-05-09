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
public class PageConditionsElementGenerator extends
        AbstractXmlElementGenerator {

    public PageConditionsElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", "pageConditions")); //$NON-NLS-1$

        //  context.getCommentGenerator().addComment(answer);
        XmlElement whereElement = new XmlElement("where");
        answer.addElement(whereElement);

        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn primaryKeyColumn : introspectedTable.getPrimaryKeyColumns()) {
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumn);
            String cqParameterClause = MyBatis3FormattingUtilities.getCQParameterClause(primaryKeyColumn);
            XmlElement ifElement = new XmlElement("if");
            ifElement.addAttribute(new Attribute("test",columnName+" !=null"));
            ifElement.addElement(new TextElement("AND t."+columnName+" = "+cqParameterClause));
            whereElement.addElement(ifElement);
        }


        for (IntrospectedColumn baseColumn : introspectedTable.getBaseColumns()) {
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(baseColumn);
            String cqParameterClause = MyBatis3FormattingUtilities.getCQParameterClause(baseColumn);
            XmlElement ifElement = new XmlElement("if");
            ifElement.addAttribute(new Attribute("test",columnName+" != '' and "+columnName+" !=null"));
            ifElement.addElement(new TextElement("AND t."+columnName+" = "+cqParameterClause));
            whereElement.addElement(ifElement);
        }



        if (context.getPlugins()
                .sqlMapUpdateByExampleSelectiveElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
