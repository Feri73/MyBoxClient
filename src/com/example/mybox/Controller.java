package com.example.mybox;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import android.util.SparseIntArray;

public class Controller
{
	static String webAddress = "http://10.0.2.2:3000/mobile";
	static JSONObject cookie;

	static JSONObject getResponse(JSONObject object) throws CustomException, Exception
	{
		HttpPost request = new HttpPost(webAddress);
		StringEntity entity = new StringEntity(object.toString());
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		request.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		HttpEntity result = response.getEntity();
		InputStream is = result.getContent();
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader buffer = new BufferedReader(reader, 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = buffer.readLine()) != null)
			sb.append(line + "\n");
		reader.close();
		JSONObject obj = null;
		obj = new JSONObject(sb.toString());
		String type = obj.getString("Exception");
		if (!type.equals("null"))
			throw new CustomException(type);
		return obj;
	}

	static String fromFile(String address) throws Exception
	{
		FileInputStream stream = new FileInputStream(address);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader, 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = buffer.readLine()) != null)
			sb.append(line + "\n");
		reader.close();
		return sb.toString();
	}

	static Suggestion[] GetSuggestions(String task, int orgID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", task);
			param.put("cookie", cookie);
			param.put("orgID", orgID);
			JSONObject result = getResponse(param);
			JSONArray suggests = result.getJSONArray("suggestions");
			Suggestion[] ret = new Suggestion[suggests.length()];
			for (int i = 0; i < suggests.length(); i++)
			{
				JSONObject sug = suggests.getJSONObject(i);
				ret[i] = new Suggestion(sug.getInt("ID"), sug.getString("text"), SuggestionState.valueOf(sug.getString("state")));
			}
			return ret;
		}
		catch (Exception e)
		{
			// throw new CustomException(e.getMessage());
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Boolean Login(String username, String password) throws CustomException
	{
		try
		{
			JSONObject param = new JSONObject();
			param.put("task", "login");
			param.put("username", username);
			param.put("password", password);
			JSONObject response = getResponse(param);
			if (!response.getBoolean("ok"))
			{
				cookie = null;
				return false;
			}
			cookie = response.getJSONObject("cookie");
			return true;
		}
		catch (Exception e)
		{
			cookie = null;
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Boolean SignUp(String username, String password) throws CustomException
	{
		try
		{
			JSONObject param = new JSONObject();
			param.put("task", "signup");
			param.put("username", username);
			param.put("password", password);
			JSONObject response = getResponse(param);
			if (!response.getBoolean("ok"))
			{
				cookie = null;
				return false;
			}
			cookie = response.getJSONObject("cookie");
			return true;
		}
		catch (Exception e)
		{
			cookie = null;
			throw new CustomException("ConnectionFailed");
		}
	}

	// static public void Logout() throws CustomException
	// {
	// try
	// {
	// if (cookie == null)
	// throw new CustomException("NotLoggedInException");
	// JSONObject param = new JSONObject();
	// param.put("task", "logout");
	// param.put("cookie", cookie);
	// getResponse(param);
	// cookie=null;
	// }
	// catch (Exception e)
	// {
	// throw new CustomException("ConnectionFailed");
	// }
	// }

	// /OK
	static public Boolean CreateOrg(Organization org) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			JSONObject organization = new JSONObject();
			organization.put("name", org.name);
			organization.put("picture", org.picAddress.equals("") ? "" : fromFile(org.picAddress));
			organization.put("desc", org.description);
			organization.put("regType", org.regType.toString());
			// if (org.regType == RegisterType.FromFile)
			organization.put("fileAddress", org.regFileAddress.equals("") ? "" : fromFile(org.regFileAddress));
			param.put("task", "createOrg");
			param.put("cookie", cookie);
			param.put("org", organization);
			JSONObject response = getResponse(param);
			return response.getBoolean("ok");
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	static public String RegisterInOrg(int orgID, String key, String password) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "enrollInOrg");
			param.put("cookie", cookie);
			param.put("orgID", orgID);
			param.put("key", key);
			param.put("password", password);
			JSONObject response = getResponse(param);
			return response.getString("state");
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	static public void AnswerRequest(String username, int orgID, Boolean answer) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("cookie", cookie);
			param.put("task", "answerRequest");
			param.put("orgID", orgID);
			param.put("username", username);
			param.put("answer", answer);
			getResponse(param);
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Suggestion[] GetAskedSuggestions(int orgID) throws CustomException
	{
		return GetSuggestions("getAskedSuggestions", orgID);
	}

	// /OK
	static public void AnswerSuggestion(int sugID, Boolean answer) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "answerSuggestion");
			param.put("cookie", cookie);
			param.put("sugID", sugID);
			param.put("answer", answer);
			getResponse(param);
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}

	}

	static public Request[] GetAnsweredRequests() throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getAnsweredRequests");
			param.put("cookie", cookie);
			JSONObject result = getResponse(param);
			JSONArray requests = result.getJSONArray("requests");
			Request[] ret = new Request[requests.length()];
			for (int i = 0; i < requests.length(); i++)
			{
				JSONObject req = requests.getJSONObject(i);
				ret[i] = new Request(req.getString("orgName"), req.getInt("orgID"), req.getBoolean("accepted"));
			}
			return ret;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	static public Request[] GetEnrollRequests(int orgID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getEnrollRequests");
			param.put("cookie", cookie);
			param.put("orgID", orgID);
			JSONObject result = getResponse(param);
			JSONArray requests = result.getJSONArray("requests");
			Request[] ret = new Request[requests.length()];
			for (int i = 0; i < requests.length(); i++)
			{
				JSONObject req = requests.getJSONObject(i);
				ret[i] = new Request(req.getString("username"), req.getString("orgName"), req.getInt("orgID"));
			}
			return ret;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public void Suggest(String suggestion, int orgID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "suggest");
			param.put("cookie", cookie);
			param.put("suggestion", suggestion);
			param.put("orgID", orgID);
			getResponse(param);
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public void CreateSurvey(String subject, Vector<Question> questions, int orgID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "createSurvey");
			param.put("cookie", cookie);
			param.put("subject", subject);
			param.put("orgID", orgID);
			JSONArray qs = new JSONArray();
			for (int i = 0; i < questions.size(); i++)
			{
				JSONObject q = questions.get(i).ToJSONObject();
				qs.put(q);
			}
			param.put("questions", qs);
			getResponse(param);
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public void Vote(long surveyID, SparseIntArray questionOptions) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "vote");
			param.put("cookie", cookie);
			param.put("surveyID", surveyID);
			JSONArray qs = new JSONArray();
			JSONArray ops = new JSONArray();
			// Integer[] keys = null;
			// keys = questionOptions.keySet().toArray(keys);
			for (int i = 0; i < questionOptions.size(); i++)
			{
				int key = questionOptions.keyAt(i);
				qs.put(key);
				ops.put(questionOptions.get(key));
			}
			param.put("questions", qs);
			param.put("options", ops);
			getResponse(param);
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public int[] GetQuestionResult(int questionID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getQuestionResult");
			param.put("questionID", questionID);
			param.put("cookie", cookie);
			JSONObject info = getResponse(param);
			JSONArray ratios = info.getJSONArray("options");
			int[] result = new int[ratios.length()];
			for (int j = 0; j < ratios.length(); j++)
				result[j] = ratios.getInt(j);
			return result;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}

	}

	static public User GetUserInfo() throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getUserInfo");
			param.put("cookie", cookie);
			JSONObject info = getResponse(param);
			User user = new User(info.getString("name"), info.getString("email"), info.getInt("rep"));
			return user;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public int[] GetUsersOrgs() throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getUserOrgs");
			param.put("cookie", cookie);
			JSONObject result = getResponse(param);
			JSONArray orgs = result.getJSONArray("orgs");
			int[] ret = new int[orgs.length()];
			for (int i = 0; i < orgs.length(); i++)
				ret[i] = orgs.getInt(i);

			return ret;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Organization GetOrgInfo(int orgID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getOrgInfo");
			param.put("cookie", cookie);
			param.put("orgID", orgID);
			JSONObject result = getResponse(param);
			Organization org = new Organization(orgID, result.getString("name"), result.getString("picAddress"), result.getString("desc"), result.getBoolean("asManager"), result.getInt("notifCount"), RegisterType.valueOf(result.getString("registerType")));
			return org;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public int[] GetOrgsSurveys(int orgID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getOrgsSurveys");
			param.put("cookie", cookie);
			param.put("orgID", orgID);
			JSONObject result = getResponse(param);
			JSONArray serveys = result.getJSONArray("surveys");
			int[] ret = new int[serveys.length()];
			for (int i = 0; i < serveys.length(); i++)
				ret[i] = serveys.getInt(i);
			return ret;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Survey GetSurveyInfo(int surveyID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getSurveyInfo");
			param.put("cookie", cookie);
			param.put("surveyID", surveyID);
			JSONObject result = getResponse(param);
			Survey survey = new Survey(surveyID, result.getInt("orgID"), new Date(), result.getString("text"));
			survey.endDate.setTime(result.getLong("date"));
			JSONArray questions = result.getJSONArray("questions");
			survey.questionsID = new int[questions.length()];
			for (int i = 0; i < questions.length(); i++)
				survey.questionsID[i] = questions.getInt(i);
			return survey;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Question GetQuestionInfo(int questionID) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "getQuestionInfo");
			param.put("cookie", cookie);
			param.put("questionID", questionID);
			JSONObject result = getResponse(param);
			Question question = new Question(questionID, result.getString("text"), result.getInt("chosen"));
			JSONArray options = result.getJSONArray("options");
			question.options = new Vector<String>();
			for (int i = 0; i < options.length(); i++)
				question.options.add(options.getString(i));
			return question;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public int[] SearchOrgs(String expression) throws CustomException
	{
		try
		{
			if (cookie == null)
				throw new CustomException("NotLoggedInException");
			JSONObject param = new JSONObject();
			param.put("task", "searchOrgs");
			param.put("cookie", cookie);
			param.put("expression", expression);
			JSONObject result = getResponse(param);
			JSONArray orgs = result.getJSONArray("orgs");
			int[] ret = new int[orgs.length()];
			for (int i = 0; i < orgs.length(); i++)
				ret[i] = orgs.getInt(i);
			return ret;
		}
		catch (Exception e)
		{
			throw new CustomException("ConnectionFailed");
		}
	}

	// /OK
	static public Suggestion[] GetMySuggestions(int orgID) throws CustomException
	{
		return GetSuggestions("getMySuggestion", orgID);
	}
}
