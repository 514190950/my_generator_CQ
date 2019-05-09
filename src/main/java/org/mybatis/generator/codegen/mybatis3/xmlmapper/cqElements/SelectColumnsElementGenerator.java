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

import java.util.Iterator;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class SelectColumnsElementGenerator extends
        AbstractXmlElementGenerator {

    public SelectColumnsElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                "selectColumns"));
        //  context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("SELECT")); //$NON-NLS-1$
        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();
        int columnsNo = MyBatis3FormattingUtilities.getSelectColumnsNo(introspectedTable);
        int index=1;
        while (iter.hasNext()){
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }
            if(index==columnsNo){
                answer.addElement(new TextElement("t."+introspectedColumn.getActualColumnName()+"  "+introspectedColumn.getJavaProperty()));
            }else{
                answer.addElement(new TextElement("t."+introspectedColumn.getActualColumnName()+"  "+introspectedColumn.getJavaProperty()+","));
                index++;
            }
        }
        answer.addElement(new TextElement("FROM"));
        answer.addElement(new TextElement(" <include refid=\"tableName\"/> t"));

        if (context.getPlugins()
                .sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
