package t4j.http;

import t4j.TBlogException;
import t4j.org.json.JSONException;
import t4j.org.json.JSONObject;

public class OAuth2AccessToken {

	private long expires_in;
	private String refresh_token;
	private String access_token;
	
	public OAuth2AccessToken(Response response) throws TBlogException {
		JSONObject json = response.asJSONObject();
		try {
			this.expires_in = json.getLong("expires_in");
			this.refresh_token = json.getString("refresh_token");
			this.access_token = json.getString("access_token");
		}catch(JSONException je) {
			throw new TBlogException(je.getMessage() + ":" + json.toString(), je);
		}
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	public String toString() {
		return "expires_in=" + expires_in + " refresh_token=" + refresh_token + " access_token=" + access_token;
	}
}
