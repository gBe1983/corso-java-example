<%@page import="it.ecommerce.util.constants.Common"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@include file="../../common/props.jsp"%>
<%
List<HashMap<String,Object>>brandList=
(List<HashMap<String,Object>>)
	request.getAttribute("brandList");
%>
<table class=list>
<% 
for(HashMap<String,Object> brand: brandList){		
%>
	<tr>
		<td>
			<%=(String)brand.get("NAME")%>
		</td>
		<td>
			<a href="<%=(String)brand.get("URL")%>"><%=rb.getString("manage.brand.url")%></a>
		</td>
		<td>
			<a href="<%=(String)brand.get("LOGO_URL")%>"><%=rb.getString("manage.brand.logoUrl")%></a>
		</td>
		<td>
			<button onclick="loadAll('./ManageBrands','<%=Common.DETAIL%>','<%=(String)brand.get("ID_BRAND")%>')">
			<%=rb.getString("manage.brand.page.detail")%>
			</button>
		</td>
		<td>
			<button onclick="loadAll('./ManageBrands','<%=Common.MODIFY%>','<%=(String)brand.get("ID_BRAND")%>')">
			<%=rb.getString("manage.brand.page.modify")%>
			</button>
		</td>
	</tr>
<%
}
%>
</table>