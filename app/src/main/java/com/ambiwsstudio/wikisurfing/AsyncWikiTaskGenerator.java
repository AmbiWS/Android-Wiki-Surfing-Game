package com.ambiwsstudio.wikisurfing;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AsyncWikiTaskGenerator {

    public static final int linksToFinish = 3;
    private static final String WIKI_RANDOM_PAGE = "https://en.m.wikipedia.org/wiki/Special:Random#/random";

    private Request request;
    private final OkHttpClient client = new OkHttpClient();
    private final Stack<String> wikiLinks = new Stack<>();

    public Stack<String> generateGoal() {

        buildRequest(WIKI_RANDOM_PAGE);
        buildCallback();

        synchronized (this) {

            try {

                this.wait();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        return wikiLinks;

    }

    private void buildCallback() {

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                e.printStackTrace();

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    if (wikiLinks.empty()) {

                        String init = response.request().url().toString().substring(response.request().url().toString().lastIndexOf('/') + 1);
                        wikiLinks.push(init);

                    }

                    String next = nextLink(Objects.requireNonNull(responseBody).string());

                    if (next == null) {

                        wikiLinks.clear();
                        buildRequest(WIKI_RANDOM_PAGE);
                        buildCallback();
                        return;

                    }

                    wikiLinks.push(next);

                    if (wikiLinks.size() < linksToFinish + 1) {

                        buildRequest("https://en.m.wikipedia.org/wiki/" + next);
                        buildCallback();

                    } else {

                        synchronized (AsyncWikiTaskGenerator.this) {

                            AsyncWikiTaskGenerator.this.notify();

                        }

                    }

                }

            }

        });

    }

    private void buildRequest(String url) {

        request = new Request.Builder()
                .url(url)
                .build();

    }

    private String nextLink(String body) {

        Pattern pattern = Pattern.compile("<a href=\"/wiki/[a-zA-Z_]+\"");
        Matcher matcher = pattern.matcher(body);

        int links = 0;
        while (matcher.find()) {

            links++;

        }

        String match = null;
        int linkCounter = 0;

        do {

            if (linkCounter >= 10) {

                return null;

            }

            int random = new Random().nextInt(links - 2) + 2;

            matcher.reset();
            while (random > 1 && matcher.find()) {

                match = matcher.group(0);
                random--;

            }

            linkCounter++;

        } while (Objects.requireNonNull(match).contains("Main_Page"));

        return match.substring(match.lastIndexOf('/') + 1, match.length() - 1);

    }

}
