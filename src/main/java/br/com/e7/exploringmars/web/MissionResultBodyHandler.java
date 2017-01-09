package br.com.e7.exploringmars.web;

import static br.com.e7.exploringmars.util.ConfigProperties.DEFAULT_ENCODING;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import br.com.e7.exploringmars.model.Direction;
import br.com.e7.exploringmars.model.MissionResult;

@Provider
@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
public class MissionResultBodyHandler implements MessageBodyWriter<MissionResult>{
	
	private final Gson gson;
	
	public MissionResultBodyHandler() {
		gson = new GsonBuilder().create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == MissionResult.class;
	}


	@Override
	@Deprecated
	public long getSize(MissionResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return 0;
	}


	@Override
	public void writeTo(MissionResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		if(MediaType.APPLICATION_JSON_TYPE.equals(mediaType))
			writeOutputJson(t, entityStream);
		else
			writeOutputText(t, entityStream);
	}
	
	// ------- parse text
	private void writeOutputText(MissionResult t, OutputStream entityStream) {
		try(final DataOutputStream outputStream = new DataOutputStream(entityStream)) {
			outputStream.write(toMissionOutputText(t));
		} catch (IOException e) {
			throw new WebApplicationException(e.getMessage(), e, Response.Status.BAD_REQUEST);
		}
	}
	
	byte[] toMissionOutputText(final MissionResult result) {
		return result.rovers().stream().map(r -> r.x() + " " + r.y() + " " + r.direction().value()).collect(Collectors.joining("\n")).getBytes(Charset.forName(DEFAULT_ENCODING.value()));
	}
	
	// ------- parse json
	private void writeOutputJson(MissionResult t, OutputStream entityStream) {
		try (final OutputStreamWriter writer = new OutputStreamWriter(entityStream, Charset.forName(DEFAULT_ENCODING.value()))) {
			gson.toJson(toMissionOutput(t), writer);
		} catch (final JsonSyntaxException | IOException e) {
            throw new WebApplicationException(e.getMessage(), e, Response.Status.BAD_REQUEST);
        }
	}
	
	List<RoverOutput> toMissionOutput(final MissionResult result) {
		return result.rovers().stream().map(r -> new RoverOutput(r.x(), r.y(), r.direction())).collect(Collectors.toList());
	}

	static class RoverOutput {
		private int x;
		private int y;
		private Direction direction;
		
		public RoverOutput(final int x, final int y, final Direction direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
		
	}
	
}
