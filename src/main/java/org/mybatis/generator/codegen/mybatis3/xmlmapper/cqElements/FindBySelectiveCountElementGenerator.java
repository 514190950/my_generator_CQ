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

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
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
public class FindBySelectiveCountElementGenerator extends
        AbstractXmlElementGenerator {

    public FindBySelectiveCountElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);
        answer.addAttribute(new Attribute("id", "findBySelective_COUNT"));
        FullyQualifiedJavaType resultType;
        resultType = introspectedTable.getRules().calculateAllFieldsClass();
        answer.addAttribute(new Attribute("resultType", MyBatis3FormattingUtilities.getPrimaryKeyType(introspectedTable)));
        answer.addAttribute(new Attribute("parameterType",resultType.getFullyQualifiedName()));
        answer.addElement(new TextElement("SELECT COUNT(1) FROM"));
        answer.addElement(new TextElement("<include refid=\"tableName\" /> t"));
        answer.addElement(new TextElement("<include refid=\"pageConditions\"/>"));

        if (context.getPlugins()
                .sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
