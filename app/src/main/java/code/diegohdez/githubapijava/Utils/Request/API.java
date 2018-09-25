package code.diegohdez.githubapijava.Utils.Request;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;

import code.diegohdez.githubapijava.Manager.AppManager;

import static code.diegohdez.githubapijava.Utils.Constants.API.ACCEPT;
import static code.diegohdez.githubapijava.Utils.Constants.API.APPLICATION_VND_GITHUB;
import static code.diegohdez.githubapijava.Utils.Constants.API.AUTHORIZATION;

public class API {

    public static ANRequest getToken (String url, String token) {
        return AndroidNetworking
                .get(url)
                .addHeaders("Authorization", token)
                .build();
    }

    public static ANRequest getAccount(String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest getRepos(String url, int page) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url + "?page=" + page);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest searchInRepo(String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest onRepoEvenListener (String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }
    public static ANRequest watchRepo (String url) {
        ANRequest.PutRequestBuilder builder = AndroidNetworking
                .put(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest unWatchRepo (String url) {
        ANRequest.DeleteRequestBuilder builder = AndroidNetworking
                .delete(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest starRepo (String url) {
        ANRequest.PutRequestBuilder builder = AndroidNetworking
                .put(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest unStarRepo (String url) {
        ANRequest.DeleteRequestBuilder builder = AndroidNetworking
                .delete(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest forkRepo (String url) {
        ANRequest.PostRequestBuilder builder = AndroidNetworking
                .post(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest getIssues (String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url)
                .addHeaders(ACCEPT, APPLICATION_VND_GITHUB);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest getPulls (String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url)
                .addHeaders(ACCEPT, APPLICATION_VND_GITHUB);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest getBranches (String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return builder.build();
    }

    public static ANRequest getCommits (String url ) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders(AUTHORIZATION, token);
        return  builder.build();
    }
}
