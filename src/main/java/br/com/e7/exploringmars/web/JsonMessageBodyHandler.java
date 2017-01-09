package br.com.e7.exploringmars.web;

import static br.com.e7.exploringmars.util.ConfigProperties.DEFAULT_ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import br.com.e7.exploringmars.exception.ParserException;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

	private final Gson gson;

	public JsonMessageBodyHandler() {
		gson = new GsonBuilder().create();
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		try (final InputStreamReader streamReader = new InputStreamReader(entityStream, Charset.forName(DEFAULT_ENCODING.value()))) {
			return gson.fromJson(streamReader, genericType == null ? type : genericType);
		} catch (JsonSyntaxException e) {
			throw new ParserException(e.getMessage());
		}
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	@Deprecated
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		try (final OutputStreamWriter writer = new OutputStreamWriter(entityStream, Charset.forName(DEFAULT_ENCODING.value()))) {
			gson.toJson(t, genericType == null ? type : genericType, writer);
		} catch (final JsonSyntaxException e) {
            throw new WebApplicationException(e.getMessage(), e, Response.Status.BAD_REQUEST);
        }
	}

}
