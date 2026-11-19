package starfleet.db.model;

import java.util.List;

public record Member(String name, String title, List<String> shows, List<String> episodes) {
}
