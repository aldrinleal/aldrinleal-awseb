package com.soaexpert;

import static com.jayway.restassured.RestAssured.given;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.soaexpert.fixture.ServerRule;

public class SentlyTest {
	@Rule
	public ServerRule serverRule = new ServerRule(7999);

	@Before
	public void before() {
		RestAssured.baseURI = "http://127.0.0.1:7999/services/api/v1";
	}

	@Test
	@Ignore
	public void sentlyTest() {
		Map<String, String> args = new LinkedHashMap<String, String>() {{
			put("modelname", "(u'GT-N8000',)");
			put("operatorname", "(u'VIVO',)");
			put("hardwareid", "(u'352078050858786',)");
			put("from=(u'+5511942373430',)", "id=(u'1',)");
			put("mccmnc=(u'72411',)", "text=(u'CMD Ola, mundo!',)");			
		}};
		
		given().
			queryParams(args)
		.expect()
			.log()
			.all()
			.statusCode(200)
		.when()
				.get("/sms/sently");
	}
}
