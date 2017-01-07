package br.com.e7.exploringmars.repository;

import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

import com.google.gson.annotations.SerializedName;

import br.com.e7.exploringmars.model.Mission;
import br.com.e7.exploringmars.model.MissionResult;

public interface MissionRepository {
	void add(Mission mission, MissionResult result);
	
	List<MissionRepositoryInfo> search(String query, String sort);

	public class MissionRepositoryInfo {
		
		@Field("id")
		private transient String id;
		
		@Field("name")
		private String name;
		
		@Field("created")
		private Date created;
		
		@Field("width")
		private int width;
		
		@Field("height")
		private int height;
		
		@SerializedName("qty_rover")
		@Field("qty_rover")
		private int qtyRover;
		
		@SerializedName("start_points")
		@Field("start_points")
		private List<String> startPoints;
		
		@SerializedName("end_points")
		@Field("end_points")
		private List<String> endPoints;
		
		public MissionRepositoryInfo() {}
		
		public MissionRepositoryInfo(final String id, final String name, final Date created, final int width, final int height, final int qtyRover, final List<String> startPoints, final List<String> endPoints) {
			this.id = id;
			this.name = name;
			this.created = created;
			this.width = width;
			this.height = height;
			this.qtyRover = qtyRover;
			this.startPoints = startPoints;
			this.endPoints = endPoints;
		}
	}
}
