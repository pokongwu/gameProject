package de.uniba.georacer.model;

public class Landmark {

		private String name;
		private Position position;

		public Landmark() {
		}


		public Landmark(String name, Position position) {
			this.name = name;
			this.position = position;
		}
		
		public String getName() {
			return name;
		}


		public Position getPosition() {
			return this.position;
		}

		public void setPosition(Position position) {
			this.position = position;
		}

	}