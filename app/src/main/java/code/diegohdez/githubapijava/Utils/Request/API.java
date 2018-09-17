package code.diegohdez.githubapijava.Utils.Request;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;

import code.diegohdez.githubapijava.Manager.AppManager;

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
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest getRepos(String url, int page) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url + "?page=" + page);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest onRepoEvenListener (String url) {
        ANRequest.GetRequestBuilder builder = AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
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
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest starRepo (String url) {
        ANRequest.PutRequestBuilder builder = AndroidNetworking
                .put(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest unStarRepo (String url) {
        ANRequest.DeleteRequestBuilder builder = AndroidNetworking
                .delete(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest forkRepo (String url) {
        ANRequest.PostRequestBuilder builder = AndroidNetworking
                .post(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }

    public static ANRequest getIssues (String url) {
        ANRequest.GetRequestBuilder builder= AndroidNetworking
                .get(url)
                .addHeaders("Accept", "application/vnd.github.symmetra-preview+json");
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
    }
}
