package starfleet.db.data.test.internal.model;

import org.junit.jupiter.api.Test;
import starfleet.db.data.internal.model.ApiResultGetDetails;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiSearchTests {

    @Test
    void can_parse_details_for_character() {
        var testResponse = """
                {
                  "character": {
                    "uid": "CHMA0000013101",
                    "name": "Aurelan Kirk",
                    "episodes": [
                      {
                        "uid": "EPMA0000000441",
                        "title": "Operation -- Annihilate!",
                        "series": {
                          "uid": "SEMA0000097474",
                          "title": "Star Trek: The Original Series"
                        },
                        "season": {
                          "uid": "SAMA0000001732",
                          "title": "TOS Season 1"
                        },
                        "seasonNumber": 1,
                        "episodeNumber": 29,
                        "usAirDate": "1967-04-13"
                      }
                    ],
                    "titles": []
                  }
                }
                """;

        var character = new ObjectMapper().readValue(testResponse, ApiResultGetDetails.class).character();

        assertEquals("Aurelan Kirk", character.name());
        assertEquals(1, character.episodes().size());
    }
}
