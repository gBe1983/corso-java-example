package it.eCommerce.servlet.common.language;

import it.eCommerce.log.MyLogger;
import it.eCommerce.servlet.RootServlet;
import it.eCommerce.util.constants.Common;
import it.eCommerce.util.constants.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * Servlet implementation class ManageLanguages
 */
public class ManageLanguages extends RootServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageLanguages() {
		super();
		log=new MyLogger(this.getClass());
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doGet";
		log.start(metodo);
		process(request, response);
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doPost";
		log.start(metodo);
		process(request, response);
		log.end(metodo);
	}

    protected void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		final String metodo="process";
		log.start(metodo);
		initProcess(request);
		String action = request.getParameter(Common.ACTION);
		if (!StringUtils.isEmpty(action)) {
			updateLanguages(request, response);
		}

		// 3 carico la lista delle lingue gestite

		SqlSession sql = sqlSessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> managedLanguages = 
			(List<HashMap<String, Object>>)
				(List<?>)
					sql.selectList("Language.list");
		sql.close();
		request.setAttribute(Request.MANAGED_LANGUAGES, managedLanguages);
		request.getSession().setAttribute(Request.MANAGED_LANGUAGES, managedLanguages);
		// 4 carico la lista delle lingue da gestire

		List<Locale> toManage = new ArrayList<Locale>();
		boolean find;
		for (String locale : Locale.getISOLanguages()) {
			find = false;
			for (HashMap<String, Object> managedLanguage : managedLanguages) {
				if (locale.equals((String) managedLanguage.get("ID_LANGUAGE"))) {
					find = true;
					break;
				}
			}
			if (!find) {
				toManage.add(new Locale(locale));
			}
		}
		request.setAttribute(Request.TO_MANAGE_LANGUAGES, toManage);

		dispatch(request, response);
		log.end(metodo);
	}


	private synchronized void updateLanguages(HttpServletRequest request,
			HttpServletResponse response) {
		final String metodo="updateLanguages";
		log.start(metodo);
		SqlSession sql = sqlSessionFactory.openSession(
									TransactionIsolationLevel.READ_COMMITTED);
		// 1 se hanno aggiunto una lingua la va a salvare nel db
		String id_language = request.getParameter("toManage");
		try {
			if (!"0000".equals(id_language)) {
				HashMap<String, Object> toManage = new HashMap<String, Object>();
				toManage.put("ID_LANGUAGE", id_language);
				toManage.put("IS_VISIBLE", true);
				sql.insert("Language.add", toManage);
			}
			// 2 aggiorna lo stato di visibilit� delle lingue gestite
			List<HashMap<String, Object>> managedLanguages=
					(List<HashMap<String, Object>>)
						request.getSession().getAttribute(
							Request.MANAGED_LANGUAGES);
			for (HashMap<String, Object> managedLanguage :managedLanguages) {
				if(request.getParameter(//nella pagina la checkbox associata � fleggata
						(String)managedLanguage.get("ID_LANGUAGE"))!=null){
					if(((BigDecimal)//devo fare update solo se la lingua era invisibile
							managedLanguage.get("IS_VISIBLE")).intValue()==0){
						managedLanguage.put("IS_VISIBLE",true);
						sql.update("Language.update", managedLanguage);
					}
				}else{//nella pagina la checkbox associata non � fleggata 
					if(((BigDecimal)//devo fare update solo se la lingua era visibile
							managedLanguage.get("IS_VISIBLE")).intValue()==1){
						managedLanguage.put("IS_VISIBLE",false);
						sql.update("Language.update", managedLanguage);
					}
				}
			}
			sql.commit();
		} catch (Exception e) {
			sql.rollback();
			log.error(metodo,request.getSession().getId(), e);
		}
		finally {
			sql.close();
			log.end(metodo);
		}
		
	}
}
