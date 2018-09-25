package code.diegohdez.githubapijava.Utils.Constants;

public class API {
    public static final String BASE_URL = "https://api.github.com/";
    public static final String USERS = "users/";
    public static final String REPOS = "/repos";
    public static final String USER_REPOS = "repos/";
    public static final String USER_ISSUES = "/issues";
    public static final String USER_PULLS = "/pulls";
    public static final String USER = "user";
    public static final String WATCH_REPO = "/subscription";
    public static final String STAR_REPO = "/starred";
    public static final String FORK_REPO = "/forks";
    public static final String BRANCHES = "/branches";
    public static final String COMMITS = "/commits";
    public static final String SEARCH = "search/";
    public static final String REPOSITORIES = "repositories";
    public static final String QUERY = "?q=";
    public static final String EQUAL = "=";
    public static final String PLUS = "+";
    public static final String COLON = ":";

    public static final String STATE_ALL = "?state=all";
    public static final String STATE_OPEN ="?state=open";
    public static final String STATE_CLOSED = "?state=closed";
    public static final String AND_PAGE = "&page=";

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCEPT = "Accept";

    public static final String APPLICATION_VND_GITHUB = "application/vnd.github.symmetra-preview+json";
    public static final int PAGE_SIZE = 30;

    public static final String UN_WATCH = "Unwatch";
    public static final String UN_STAR = "Unstar";

    public static final String WATCH = "Watch";
    public static final String STAR = "Star";

    public static final int AUTH_SUCCESS = 200;
    public static final int WATCH_REPO_SUCCESS = 200;
    public static final int UNWATCH_REPO_SUCCESS = 204;
    public static final int WATCHED_REPO_ERROR = 404;
    public static final int STARRED_REPO_SUCCESS = 204;
    public static final int STARRED_REPO_ERROR = 404;
    public static final int FORK_REPO_SUCCESS = 202;

    public static final String OPEN = "open";
    public static final String CLOSED = "closed";
    public static final String MERGED = "merged";

    public static final String GET_TOKEN = BASE_URL + USER;

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_REPO_FORMAT = "MMMM dd',' yyyy";

    public static String getRepos(String username) {
        return BASE_URL + USERS + username + REPOS;
    }
}
