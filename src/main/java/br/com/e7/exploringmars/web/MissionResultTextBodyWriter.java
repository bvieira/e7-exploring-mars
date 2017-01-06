package br.com.e7.exploringmars.web;

import static br.com.e7.exploringmars.util.ConfigProperties.DEFAULT_ENCODING;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import br.com.e7.exploringmars.model.MissionResult;


@Provider
@Produces(MediaType.TEXT_PLAIN)
public class MissionResultTextBodyWriter implements MessageBodyWriter<MissionResult>{

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		System.out.println(type);
		return type == MissionResult.class;
	}

	@Override
	@Deprecated
	public long getSize(MissionResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(MissionResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		try(final DataOutputStream outputStream = new DataOutputStream(entityStream)) {
			outputStream.write(parseMissionResult(t));
		}
	}
	
	byte[] parseMissionResult(final MissionResult result) {
		return result.rovers().stream().map(r -> r.x() + " " + r.y() + " " + r.direction().value()).collect(Collectors.joining("\n")).getBytes(Charset.forName(DEFAULT_ENCODING.value()));
	}
}
