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
 * @author Jeff Butler
 */
public class DeleteByIdElementGenerator extends
        AbstractXmlElementGenerator {

    private boolean isSimple;

    public DeleteByIdElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "id", "deleteById")); //$NON-NLS-1$

        answer.addAttribute(new Attribute("parameterType", MyBatis3FormattingUtilities.getPrimaryKeyType(introspectedTable))); //$NON-NLS-1$

        /* context.getCommentGenerator().addComment(answer);*/

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM "); //$NON-NLS-1$
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        sb.append("<include refid=\"tableName\"/>");
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                answer.addElement(new TextElement("  AND "));//$NON-NLS-1$
            } else {
                answer.addElement(new TextElement("WHERE"));//$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getCQParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins()
                .sqlMapDeleteByPrimaryKeyElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
