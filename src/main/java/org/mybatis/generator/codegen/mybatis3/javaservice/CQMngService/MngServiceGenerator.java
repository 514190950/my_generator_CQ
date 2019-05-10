package org.mybatis.generator.codegen.mybatis3.javaservice.CQMngService;

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
 * 功能描述:长护险MngService实体类创建模板
 * @auther: gxz
 * @date:
 */
public class MngServiceGenerator extends AbstractJavaGenerator {
    private static final String[] IMPORT_ARRAY = new String[]{"java.util.Date","org.springframework.beans.BeanUtils",
    "org.springframework.stereotype.Service","com.wondersgroup.healthins.cms.base.CmsBaseServiceImpl","com.wondersgroup.healthins.plugin.api.dto.ResultDto",
            "com.wondersgroup.healthins.plugin.db.pager.Pager","com.wondersgroup.ltcins.intact.core.dto.rsp.ResultDTO"};
     String   COMMON_SERVICE_NAME = "assessQuestionPicCommonService";

    public MngServiceGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.8", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();
        //获取实体类包的地址
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3MngServiceType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        for (String s : IMPORT_ARRAY) {
            topLevelClass.addImportedType(s);
        }
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.setSuperClass("CmsBaseServiceImpl<"+introspectedTable.getMyBatis3ModelQueryFileName()+","+introspectedTable.getMyBatis3ModelInputFileName()+">");
        topLevelClass.addAnnotation("@Service");
        commentGenerator.addJavaFileComment(topLevelClass);

        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        Method setServiceMethod = getQuery();
        topLevelClass.addMethod(setServiceMethod);
        setServiceMethod=getSave();
        topLevelClass.addMethod(setServiceMethod);
        setServiceMethod=getLoadForEdit();
        topLevelClass.addMethod(setServiceMethod);
        setServiceMethod=getUpdate();
        topLevelClass.addMethod(setServiceMethod);
        setServiceMethod=getRemove();
        topLevelClass.addMethod(setServiceMethod);

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
    private Method getQuery() {
        String pojoType = introspectedTable.getTableConfiguration().getDomainObjectName();
        String pojoName = formatTypeNameToParamName(pojoType);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("query");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"),"pageSize"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("int"),"pageNo"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getMyBatis3ModelQueryFileName()),"query"));
        method.setReturnType(new FullyQualifiedJavaType("Pager<"+introspectedTable.getTableConfiguration().getDomainObjectName()+">"));
        StringBuilder sb = getNewObjString();
        method.addBodyLine(sb.toString());
        sb=getCopyParamString();
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("return assessQuestionPicCommonService.queryPager(pageSize, pageNo, "+pojoName+");"); //$NON-NLS-1$
        method.addBodyLine(sb.toString());
        return method;
    }

    private Method getSave() {
        String pojoType = introspectedTable.getTableConfiguration().getDomainObjectName();
        String pojoName = formatTypeNameToParamName(pojoType);
        String primaryKeyType=introspectedTable.getPrimaryKeyColumns().
                size() == 0 ? "Integer" : formatTypeNameToFirstUp(introspectedTable.getPrimaryKeyColumns().get(0).getJdbcTypeName());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("save");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getMyBatis3ModelInputFileName()),"t"));
        method.setReturnType(new FullyQualifiedJavaType("ResultDto<"+primaryKeyType+">"));
        StringBuilder sb =getNewResultDto();
        method.addBodyLine(sb.toString());
        sb=getNewObjString();
        method.addBodyLine(sb.toString());
        sb=getCopyParamString();
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append(pojoName+".setCreatedTime(new Date());");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("ResultDTO<Serializable> resultDTO = "+COMMON_SERVICE_NAME+".save("+pojoName+");");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("resultDto.setParamObj(("+primaryKeyType+")resultDTO.getReturnObj());");
        method.addBodyLine(sb.toString());
        sb=getResultString();
        method.addBodyLine(sb.toString());
        return method;
    }
    private Method getLoadForEdit() {
        String pojoType = introspectedTable.getTableConfiguration().getDomainObjectName();
        String pojoName = formatTypeNameToParamName(pojoType);
        String primaryKeyType=introspectedTable.getPrimaryKeyColumns().
                size() == 0 ? "Integer" : formatTypeNameToFirstUp(introspectedTable.getPrimaryKeyColumns().get(0).getJdbcTypeName());
        String pojoInputType=introspectedTable.getMyBatis3ModelInputFileName();
        String pojoInputName = formatTypeNameToParamName(pojoInputType);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Serializable"),"id"));
        method.setName("loadForEdit");
        method.setReturnType(new FullyQualifiedJavaType(introspectedTable.getMyBatis3ModelInputFileName()));
        StringBuilder sb = new StringBuilder();
        sb.append(pojoType+" "+pojoName+" = "+COMMON_SERVICE_NAME+".findById("+primaryKeyType+".valueOf(id.toString()));");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append(pojoInputType+" "+pojoInputName+" = new "+pojoInputType+"();");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("BeanUtils.copyProperties("+pojoName+", "+pojoInputName+");");
        method.addBodyLine(sb.toString());
        sb=getResultString();
        method.addBodyLine(sb.toString());
        return method;
    }
    private Method getRemove() {
        String pojoType = introspectedTable.getTableConfiguration().getDomainObjectName();
        String pojoName = formatTypeNameToParamName(pojoType);
        String primaryKeyType=introspectedTable.getPrimaryKeyColumns().
                size() == 0 ? "Integer" : formatTypeNameToFirstUp(introspectedTable.getPrimaryKeyColumns().get(0).getJdbcTypeName());
        String pojoInputType=introspectedTable.getMyBatis3ModelInputFileName();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("remove");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Serializable"),"id"));
        method.setReturnType(new FullyQualifiedJavaType("ResultDto<"+primaryKeyType+">"));
        method.addException(new FullyQualifiedJavaType(("RuntimeException")));
        StringBuilder sb=getNewResultDto();
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("ResultDTO<"+primaryKeyType+"> resultDTO = assessQuestionPicCommonService.delete("+primaryKeyType+".valueOf(id.toString()));");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("resultDto.setParamObj(resultDTO.getReturnObj());");
        method.addBodyLine(sb.toString());
        sb=getResultString();
        method.addBodyLine(sb.toString());
        return method;
    }

    private Method getUpdate() {
        String pojoType = introspectedTable.getTableConfiguration().getDomainObjectName();
        String pojoName = formatTypeNameToParamName(pojoType);
        String primaryKeyType=introspectedTable.getPrimaryKeyColumns().
                size() == 0 ? "Integer" : formatTypeNameToFirstUp(introspectedTable.getPrimaryKeyColumns().get(0).getJdbcTypeName());
        String pojoInputType=introspectedTable.getMyBatis3ModelInputFileName();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setName("update");
        method.addParameter(0,new Parameter(new FullyQualifiedJavaType("Serializable"),"id"));
        method.addParameter(1,new Parameter(new FullyQualifiedJavaType(pojoInputType),"t"));
        method.setReturnType(new FullyQualifiedJavaType("ResultDto<"+primaryKeyType+">"));
        method.addException(new FullyQualifiedJavaType(("RuntimeException")));
        StringBuilder sb=getNewResultDto();
        method.addBodyLine(sb.toString());
        sb=getNewObjString();
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("BeanUtils.copyProperties(t, "+pojoName+");");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append(pojoName+".setId("+primaryKeyType+".valueOf(id.toString()));");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("ResultDTO<"+primaryKeyType+"> resultDTO =  assessQuestionPicCommonService.update("+pojoName+");");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("resultDto.setParamObj(resultDTO.getReturnObj());");
        method.addBodyLine(sb.toString());
        sb=getResultString();
        method.addBodyLine(sb.toString());
        return method;
    }
    /**
     * 功能描述:把全大写的变成首字母大写
     * 例:  INTEGER-->Integer
     * @auther: gxz
     * @param:
     * @return:
     * @date: 2019/5/10 13:38
     */
    private String formatTypeNameToFirstUp(String typeName){
        if(typeName==null)return null;
        String first=typeName.substring(0,1);
        return first+typeName.substring(1).toLowerCase();

    }
    /**
     * 功能描述:把类名首字母小写
     * 例:StringBuff--> stringBuff
     * @auther: gxz
     * @param:
     * @return:
     * @date: 2019/5/10 13:38
     */
    private String formatTypeNameToParamName(String typeName){
        if(typeName==null)return null;
        String first=typeName.substring(0,1);
        return first.toLowerCase()+typeName.substring(1);

    }
/**
 * 功能描述:获取当前表的一行创建对象的字符串
 * 例:T t=new T();
 * @auther: gxz
 * @param:
 * @return:
 * @date: 2019/5/10 14:32
 */
private StringBuilder getNewObjString(){
    StringBuilder sb=new StringBuilder();
    String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
    String domainOjbectParamName = formatTypeNameToParamName(domainObjectName);
    sb.append(domainObjectName+" "+domainOjbectParamName+" = new "+domainObjectName+"();");
    return sb;
}
    /**
     * 功能描述:获取当前表通过input对象给实体类对象赋值的语句
     * 例:BeanUtils.copyProperties(t, obj);
     * @auther: gxz
     * @param:
     * @return:
     * @date: 2019/5/10 14:32
     */
    private StringBuilder getCopyParamString(){
        StringBuilder sb=new StringBuilder();
        sb.append("BeanUtils.copyProperties(query, "+formatTypeNameToParamName(introspectedTable.getTableConfiguration().getDomainObjectName())+");");
        return sb;
    }
    /**
     * 功能描述:获取当前表创建返回值对象的语句
     * 例:ResultDto<T> resultDto = new ResultDto<>();
     * @auther: gxz
     * @param:
     * @return:
     * @date: 2019/5/10 14:32
     */
    private StringBuilder getNewResultDto(){
        String primaryKeyType=introspectedTable.getPrimaryKeyColumns().
                size() == 0 ? "Integer" : formatTypeNameToFirstUp(introspectedTable.getPrimaryKeyColumns().get(0).getJdbcTypeName());
        StringBuilder sb = new StringBuilder();
            sb.append("ResultDto<"+primaryKeyType+"> resultDto = new ResultDto<>();");
        return sb;
    }
    /**
     * 功能描述:获取返回对象语句
     * 例:return resultDto;
     * @auther: gxz
     * @param:
     * @return:
     * @date: 2019/5/10 14:32
     */
    private StringBuilder getResultString(){
        StringBuilder sb = new StringBuilder();
        sb.append("return resultDto;");
        return sb;
    }

}
