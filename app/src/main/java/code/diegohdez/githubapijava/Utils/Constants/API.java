package code.diegohdez.githubapijava.Utils.Constants;

public class API {
    public static final String BASE_URL = "https://api.github.com/";
    public static final String USERS = "users/";
    public static final String REPOS = "/repos";
    public static final String USER_REPOS = "repos/";
    public static final String USER_ISSUES = "/issues";
    public static final String USER = "user";
    public static final String WATCH_REPO = "/subscription";
    public static final String STAR_REPO = "/starred";
    public static final String FORK_REPO = "/forks";

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCEPT = "Accept";

    public static final String APPLICATION_VND_GITHUB = "application/vnd.github.symmetra-preview+json";
    public static final int PAGE_SIZE = 30;

    public static final String UN_WATCH = "Unwatch";
    public static final String UN_STAR = "Unstar";

    public static final String WATCH = "Watch";
    public static final String STAR = "Star";

    public static final int WATCH_REPO_SUCCESS = 200;
    public static final int UNWATCH_REPO_SUCCESS = 204;

    public static final String GET_TOKEN = BASE_URL + USER;

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_REPO_FORMAT = "M dd',' yyyy";

    public static final String getRepos(String username) {
        return BASE_URL + USERS + username + REPOS;
    }
}
