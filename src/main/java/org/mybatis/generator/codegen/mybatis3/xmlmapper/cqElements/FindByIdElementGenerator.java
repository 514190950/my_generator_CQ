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
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class FindByIdElementGenerator extends
        AbstractXmlElementGenerator {

    public FindByIdElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                "findById"));

        context.getCommentGenerator().addComment(answer);
        FullyQualifiedJavaType resultType;

        resultType = introspectedTable.getRules().calculateAllFieldsClass();

        answer.addAttribute(new Attribute("resultType",resultType.getFullyQualifiedName()));
        answer.addElement(new TextElement("<include refid=\"selectColumns\"/>")); //$NON-NLS-1$
        IntrospectedColumn primaryKeyColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        answer.addElement(new TextElement("WHERE "+MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumn)+" = "+MyBatis3FormattingUtilities.getCQParameterClause(primaryKeyColumn))); //$NON-NLS-1$



        if (context.getPlugins()
                .sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
