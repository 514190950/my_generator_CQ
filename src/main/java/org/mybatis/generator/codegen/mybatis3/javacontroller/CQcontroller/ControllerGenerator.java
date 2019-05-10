package org.mybatis.generator.codegen.mybatis3.model.CQmodel;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 功能描述:长护险Query实体类创建模板
 * @auther: gxz
 * @date:
 */
public class ControllerGenerator extends AbstractJavaGenerator {

    private static final String[] IMPORT_ARRAY = new String[]{"org.springframework.beans.factory.annotation.Value","org.springframework.stereotype.Controller"
            ,"org.springframework.web.bind.annotation.RequestMapping","com.wondersgroup.healthins.cms.base.controller.CmsBaseController",
            "com.wondersgroup.healthins.plugin.api.dto.ResultDto","com.wondersgroup.healthins.plugin.api.dtoutil.ValidResult",
            "com.wondersgroup.healthins.plugin.api.dtoutil.ValidUtils"};

    public ControllerGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.8", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();
        //获取实体类包的地址
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ControllerType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        for (String s : IMPORT_ARRAY) {
            topLevelClass.addImportedType(s);
        }
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.setSuperClass("CmsBaseController<"+introspectedTable.getMyBatis3ModelQueryFileName()
        +","+introspectedTable.getMyBatis3ModelQueryFileName()+","+introspectedTable.getMyBatis3ModelInputFileName()+">");
        topLevelClass.addAnnotation("@Controller");
        topLevelClass.addAnnotation("@RequestMapping(value = \"/core/"+table.toString()+".htm\")");
        commentGenerator.addJavaFileComment(topLevelClass);

        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        Method setService = getSetService();
        topLevelClass.addMethod(setService);
        setService=getSetMainView();
        topLevelClass.addMethod(setService);
        setService=getIdClass();
        topLevelClass.addMethod(setService);

        if (introspectedTable.isConstructorBased()) {
            addParameterizedConstructor(topLevelClass);

            if (!introspectedTable.isImmutable()) {
                addDefaultConstructor(topLevelClass);
            }
        }

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().modelBaseRecordClassGenerated(
                topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            superClass = new FullyQualifiedJavaType(introspectedTable
                    .getPrimaryKeyType());
        } else {
            String rootClass = getRootClass();
            if (rootClass != null) {
                superClass = new FullyQualifiedJavaType(rootClass);
            } else {
                superClass = null;
            }
        }

        return superClass;
    }

    private boolean includePrimaryKeyColumns() {
        return !introspectedTable.getRules().generatePrimaryKeyClass()
                && introspectedTable.hasPrimaryKeyColumns();
    }

    private boolean includeBLOBColumns() {
        return !introspectedTable.getRules().generateRecordWithBLOBsClass()
                && introspectedTable.hasBLOBColumns();
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> constructorColumns =
                includeBLOBColumns() ? introspectedTable.getAllColumns() :
                        introspectedTable.getNonBLOBColumns();

        for (IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                    introspectedColumn.getJavaProperty()));
        }

        StringBuilder sb = new StringBuilder();
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            boolean comma = false;
            sb.append("super("); //$NON-NLS-1$
            for (IntrospectedColumn introspectedColumn : introspectedTable
                    .getPrimaryKeyColumns()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }
                sb.append(introspectedColumn.getJavaProperty());
            }
            sb.append(");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        }
        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            sb.setLength(0);
            sb.append("this."); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        topLevelClass.addMethod(method);
    }

    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns;
        if (includePrimaryKeyColumns()) {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable.getAllColumns();
            } else {
                introspectedColumns = introspectedTable.getNonBLOBColumns();
            }
        } else {
            if (includeBLOBColumns()) {
                introspectedColumns = introspectedTable
                        .getNonPrimaryKeyColumns();
            } else {
                introspectedColumns = introspectedTable.getBaseColumns();
            }
        }

        return introspectedColumns;
    }
    private Method getSetService() {

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Resource");
        method.setName("setService");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getMyBatis3ModelQueryFileName()),introspectedTable.getMyBatis3ModelQueryFileName()));
        StringBuilder sb = new StringBuilder();
        sb.append("this.cmsService = cmsService; "); //$NON-NLS-1$
        method.addBodyLine(sb.toString());

        return method;
    }

    private Method getSetMainView() {

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Value(value = \"core/assess_basic_info\")");
        method.setName("setMainView");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"),"mainView"));
        StringBuilder sb = new StringBuilder();
        sb.append("\tthis.mainView = mainView;"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        return method;
    }
    private Method getIdClass() {

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("getIdClass");
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        StringBuilder sb = new StringBuilder();
        sb.append("return ");
        if(primaryKeyColumns.size()!=0){
            method.setReturnType(new FullyQualifiedJavaType("Class<"+formatTypeName(primaryKeyColumns.get(0).getJdbcTypeName())+">"));
            sb.append(formatTypeName(primaryKeyColumns.get(0).getJdbcTypeName()));
        }else{
            method.setReturnType(new FullyQualifiedJavaType("Class<Integer>"));
            sb.append("Integer");
        }
        sb.append(".class;");
        method.addBodyLine(sb.toString());

        return method;
    }
    /**
     * 功能描述:把全大写的变成首字母大写
     * @auther: gxz
     * @param:
     * @return:
     * @date: 2019/5/10 13:38
     */
    private String formatTypeName(String typeName){
        if(typeName==null)return null;
         String first=typeName.substring(0,1);
         return first+typeName.substring(1).toLowerCase();

    }

}
