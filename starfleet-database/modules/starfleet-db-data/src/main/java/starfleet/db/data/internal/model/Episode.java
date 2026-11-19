package starfleet.db.data.internal.model;

public record Episode(String title, Series series, Season season, String usAirDate, int seasonNumber, int episodeNumber) {
}
