package de.uniba.georacer.model.json;

/**
 * Holds the information for a position, instance gets created from GSON
 *
 * @author Pio
 */
public class Position {
		private double longitude;
		private double latitude;

		public Position() { }

		public Position(double longitude, double latitude) {
			this.longitude = longitude;
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
	}