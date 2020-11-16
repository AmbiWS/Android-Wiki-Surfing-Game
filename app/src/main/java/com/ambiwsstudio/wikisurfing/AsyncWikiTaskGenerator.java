package com.ambiwsstudio.wikisurfing;

import android.util.Pair;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

    private Request request;
    private final OkHttpClient client = new OkHttpClient();
    private String wikiFrom;
    private Stack<String> wikiLinks = new Stack<>();
    //private Pair<String, String> goal;

    public void generateGoal() {

        buildRequest();
        buildCallback();

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

                    System.out.println("=============================================================");

                    wikiFrom = response.request().url().toString().substring(response.request().url().toString().lastIndexOf('/') + 1).replaceAll("_", " ");
                    System.out.println(wikiFrom);

                    if (wikiLinks.size() < 5) {

                        String next = nextLink(responseBody.string());
                        System.out.println(next + " (PUSHED)");
                        wikiLinks.push(next);

                    }

                }

            }

        });

    }

    private void buildRequest() {

        String WIKI_RANDOM_PAGE = "https://en.m.wikipedia.org/wiki/Special:Random#/random";
        request = new Request.Builder()
                .url(WIKI_RANDOM_PAGE)
                .build();

    }

    private String nextLink(String body) {

        Pattern pattern = Pattern.compile("<a href=\"/wiki/[a-zA-Z_]+\"");
        Matcher matcher = pattern.matcher(body);

        int links = 0;
        while (matcher.find()) {

            links++;

        }

        System.out.println("Count: " + links);

        int random = new Random().nextInt(links - 2) + 2;
        System.out.println("Random: " + random);

        String match = null;

        do {

            matcher.reset();
            while (random > 1 && matcher.find()) {

                match = matcher.group(0);
                random--;

            }

        } while (match.contains("Main_Page"));

        return match.substring(match.lastIndexOf('/') + 1, match.length() - 1);

    }


}
