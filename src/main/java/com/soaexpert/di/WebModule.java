package com.soaexpert.di;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.soaexpert.resource.RootResource;
import com.soaexpert.resource.SNSResource;
import com.soaexpert.resource.SampleSQSDResource;
import com.soaexpert.resource.SentlyResource;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class WebModule extends ServletModule {
	@Override
	protected void configureServlets() {
		bind(GuiceContainer.class);
		
		bind(JacksonJaxbJsonProvider.class).in(Scopes.SINGLETON);
		
		serve("/services/api/v1/*").with(GuiceContainer.class);
		
		bind(RootResource.class);
		
		bind(SNSResource.class);

        bind(SampleSQSDResource.class);
        
        bind(SentlyResource.class);
	}
}
