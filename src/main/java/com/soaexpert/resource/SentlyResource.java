package com.soaexpert.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.google.inject.Singleton;

@Path("/sms/sently")
@Singleton
public class SentlyResource extends BaseResource {
	private final AmazonSNS sns;

	private String topicArn;

	@Inject
	public SentlyResource(AmazonSNS sns) {
		this.sns = sns;

		init();
	}

	private void init() {
		logger.debug("Inicializando");

		for (Topic t : sns.listTopics().getTopics()) {
			logger.info("Avaliando fila com arn: {}", t.getTopicArn());
			if (t.getTopicArn().endsWith("-sently")) {
				this.topicArn = t.getTopicArn();

				logger.info("Utilizando o topic arn {}", topicArn);
			}
		}
	}

	/**
	 * Exemplo de chamada get:
	 * 
	 * <pre>
	 * modelname=(u'GT-N8000',)&operatorname=(u'VIVO',)&hardwareid=(u'352078050858786',)&from=(u'+5511942373430',)&id=(u'1',)&mccmnc=(u'72411',)&text=(u'CMD Ola, mundo!',)
	 * </pre>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Response sendMessage(@QueryParam("from") String from,
			@QueryParam("text") String text) {
		from = filtrarPython(from);
		text = filtrarPython(text);

		PublishResult resultado = sns.publish(new PublishRequest()
				.withMessage(text).withSubject("CMD").withTopicArn(topicArn));

		return Response.ok().entity(resultado).build();
	}

	private static String filtrarPython(String from) {
		return from.replaceAll("\\(u\\'CMD (.*)\\'\\,\\)", "$1");
	}

	/**
	 * Apenas para descargo de consciência e um dia eu me lembrar pra que isso
	 * :)
	 * 
	 * @param args
	 *            meh
	 */
	public static void main(String[] args) {
		System.out.println(filtrarPython("(u'CMD Ola, mundo!',)"));
	}

}
