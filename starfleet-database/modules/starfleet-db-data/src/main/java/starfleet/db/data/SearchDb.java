package starfleet.db.data;

import module java.net.http;
import module starfleet.db.model;
import module tools.jackson.databind;

import starfleet.db.data.internal.model.ApiResultGetDetails;
import starfleet.db.data.internal.model.ApiResultSearch;
import starfleet.db.data.internal.model.Character;
import starfleet.db.data.internal.model.Episode;
import starfleet.db.data.internal.model.Series;
import starfleet.db.data.internal.model.Title;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class SearchDb {

    private static final String SEARCH_API =
            "https://stapi.co/api/v1/rest/character/search?pageSize=8&alternateReality=false&mirror=false&name=";
    private static final String GET_API =
            "https://stapi.co/api/v1/rest/character?uid=";

    public static List<Member> searchByName(String nameQuery) {
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder().uri(URI.create(SEARCH_API + nameQuery))
                    .POST(HttpRequest.BodyPublishers.noBody()).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parse(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Member> parse(String body) {
        var mapper = new ObjectMapper();
        var result = mapper.readValue(body, ApiResultSearch.class);
        var fullList = result.characters().stream()
                .map(id -> mapper.readValue(getById(id), ApiResultGetDetails.class).character()).toList();
        return fullList.stream()
                .filter(c -> !c.episodes().isEmpty())
                .filter(c -> c.episodes().size() != 1 || // clean out some weird duplications in API
                        fullList.stream().noneMatch(other -> other != c && other.name().equals(c.name())))
                .sorted(comparing(Character::name))
                .map(c -> new Member(c.name(),
                        c.titles().stream().findFirst().orElse(new Title("Civilian")).name(),
                        c.episodes().stream().sorted(comparing(Episode::usAirDate)).map(Episode::series).map(Series::title).distinct().toList(),
                        c.episodes().stream().sorted(comparing(Episode::usAirDate)).map(e ->
                                "%s (%s - S%dE%d)".formatted(e.title(), e.season().title().split(" ")[0], e.seasonNumber(), e.episodeNumber())).toList())).toList();
    }

    private static String getById(Character id) {
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder().uri(URI.create(GET_API + id.uid())).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
