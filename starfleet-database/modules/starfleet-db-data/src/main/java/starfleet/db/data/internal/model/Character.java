package starfleet.db.data.internal.model;

import java.util.List;

public record Character(String uid, String name, List<Episode> episodes, List<Title> titles) {
}
