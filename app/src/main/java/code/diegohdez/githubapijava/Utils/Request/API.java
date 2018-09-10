package code.diegohdez.githubapijava.Utils.Request;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;

import code.diegohdez.githubapijava.Manager.AppManager;

public class API {
    public static ANRequest getRepos (String url) {
        ANRequest.GetRequestBuilder builder =  AndroidNetworking
                .get(url);
        String token = AppManager.getOurInstance().getToken();
        if (token.length() > 0) builder.addHeaders("Authorization", token);
        return builder.build();
     }
}
