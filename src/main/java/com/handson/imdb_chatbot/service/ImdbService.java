package com.handson.imdb_chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handson.imdb_chatbot.model.ImdbSearchResponseObject;
import com.handson.imdb_chatbot.model.ImdbMovieObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImdbService {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public static final Pattern MOVIE_PATTERN = Pattern.compile("</script><title>([^<]+)</title><meta name=\"description\" content=\"([^\"]+).*\"aggregateRating\":\\{\"@type\":\"AggregateRating\",\"ratingCount\":([0-9]+).*\"ratingValue\":([0-9.]+).*,\"datePublished\":\"([^\"]+)");


    public static final Pattern MOVIE_NAME_AND_DESCRIPTION_ONLY_PATTERN = Pattern.compile("</script><title>([^<]+)</title><meta name=\"description\" content=\"([^\"]+)");

    @Autowired
    private ObjectMapper om;

    private final String failure = "Couldn't find the requested movie.";


    public String searchMovie(String movieName) throws IOException {
        ImdbMovieObject mo = getMovieObject(movieName);
        if (mo == null)
            return failure;
        String html = getMovieHtml(mo.getId(), mo.getL());
        return parseMovieHtml(html);
    }

    /***
     * group 1 -title
     * group 2 - description
     * group 3 - number of ratings
     * group 4 - rating out of 10
     * group 5 - publish date
     * @param html the html in the response from imdb's api
     * @return string of movie details
     */
    public String parseMovieHtml(String html){
        StringBuilder res = new StringBuilder();
        Matcher matcher = MOVIE_PATTERN.matcher(html);
        while (matcher.find()){
            res
                    .append("Movie name: ").append(matcher.group(1)).append("\n")
                    .append("Description: ").append(matcher.group(2)).append("\n")
                    .append("Rating: ").append(matcher.group(4)).append("/10 (").append(matcher.group(3)).append(" votes)\n")
                    .append("Publish date: ").append(matcher.group(5)).append("\n");
        }
        if (res.toString().isEmpty()){  // newer movies may not have ratings
            matcher = MOVIE_NAME_AND_DESCRIPTION_ONLY_PATTERN.matcher(html);
            while (matcher.find()){
                res
                        .append("Movie name: ").append(matcher.group(1)).append("\n")
                        .append("Description: ").append(matcher.group(2)).append("\n");
            }
        }
        if (res.toString().isEmpty())
            res.append(failure);
        return res.toString();
    }

    public String getMovieHtml(String movieId, String movieName) throws IOException {
        Request request = new Request.Builder()
                .url("https://www.imdb.com/title/" + movieId + "/?ref_=nv_sr_srsg_0_tt_8_nm_0_q_" + movieName)
                .method("GET", null)
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "en-GB,en;q=0.9")
                .addHeader("cookie", "session-id=147-0907892-3648302; session-id-time=2082787201l; ad-oo=0; ci=e30; ubid-main=131-2914084-3146625; session-token=VpRKQ/hj/xeOzd43dUsKJvHILXqfSFr2rRwP9BKnnduP1pg8a58wNYP2aQgvSvCmTfe9QhUZHsbznN9SyCn+J57+HXZrf5YW4lKVFRXDtElCUmaiOd09XTb1LWrdqm9ljM6IPxlzanOnTq8FzUfjrdYIfggbsRlZY0P/plXrFmRrKYJ0b9EkQp8ns31uEBGIU94IDrO7AI0L/zsyeMilHRGF3f/aVI0H5XQITLREwR9BX+GRmAwkEPF+7uyv7YznTbByXKDRSU1GgZunvvQwAVeTe2iAWEjtVwDa/poT3jN82cRcK9i0ndttLm62m07UQqWxHBkLR3BDKq9CStSAADbzwku3Q7qH; csm-hit=tb:21HY6XQ78K58JQC83BXA+s-SMN972VH6XXVVYFZA5MF|1718203932362&t:1718203932362&adb:adblk_yes; session-id=147-0907892-3648302; session-id-time=2082787201l; session-token=VpRKQ/hj/xeOzd43dUsKJvHILXqfSFr2rRwP9BKnnduP1pg8a58wNYP2aQgvSvCmTfe9QhUZHsbznN9SyCn+J57+HXZrf5YW4lKVFRXDtElCUmaiOd09XTb1LWrdqm9ljM6IPxlzanOnTq8FzUfjrdYIfggbsRlZY0P/plXrFmRrKYJ0b9EkQp8ns31uEBGIU94IDrO7AI0L/zsyeMilHRGF3f/aVI0H5XQITLREwR9BX+GRmAwkEPF+7uyv7YznTbByXKDRSU1GgZunvvQwAVeTe2iAWEjtVwDa/poT3jN82cRcK9i0ndttLm62m07UQqWxHBkLR3BDKq9CStSAADbzwku3Q7qH")
                .addHeader("priority", "u=0, i")
                .addHeader("referer", "https://www.imdb.com/name/nm0000288/?ref_=nv_sr_srsg_0_tt_3_nm_5_q_christian%2520bale")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .build();
        return client.newCall(request).execute().body().string();
    }

    public ImdbMovieObject getMovieObject(String movieName) throws IOException {
        Request request = new Request.Builder()
                .url("https://v3.sg.media-imdb.com/suggestion/x/" + movieName + ".json?includeVideos=0")
                .method("GET", null)
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader("accept-language", "en-GB,en;q=0.9")
                .addHeader("origin", "https://www.imdb.com")
                .addHeader("priority", "u=1, i")
                .addHeader("referer", "https://www.imdb.com/")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "cross-site")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        ImdbSearchResponseObject imdbSearchResponseObject = om.readValue(response.body().string(), ImdbSearchResponseObject.class);
        for (int i = 0; i < imdbSearchResponseObject.getD().size(); i++) {
            if (imdbSearchResponseObject.getD().get(i).getId().startsWith("tt"))
                return new ImdbMovieObject(imdbSearchResponseObject.getD().get(i).getL(), imdbSearchResponseObject.getD().get(i).getId());
        }
        return null;
    }

}
