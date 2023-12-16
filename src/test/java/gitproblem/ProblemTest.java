package gitproblem;

import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class ProblemTest {

    @Test
    void workingOnPublicRepository() {
        GHRepository gh = null;
        try {
            gh = GitHub.connectAnonymously()
                    .getRepository("Assamir/QAMaster");
            URL url = gh.getPullRequest(1).getDiffUrl();

            var con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            var content = fetchPRDiff(url.toString());
            System.out.println("content = " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void notWorkingOnPrivateRepository() {
        GHRepository gh = null;
        try {
            gh = GitHub.connectUsingOAuth("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")
                    .getRepository("Assamir/LettersMania");
            URL url = gh.getPullRequest(1).getDiffUrl();

            var con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            var content = fetchPRDiffHTTPS(url.toString());
            System.out.println("content = " + content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fetchPRDiff(String urlString) throws IOException {
        var con = (HttpURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("GET");

        var responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            try (Scanner scanner = new Scanner(con.getInputStream(), StandardCharsets.UTF_8)) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            throw new IOException("HTTP request not successful: HTTP Error code " + responseCode);
        }
    }

    public static String fetchPRDiffHTTPS(String urlString) throws IOException {
        var con = (HttpsURLConnection) new URL(urlString).openConnection();
        con.setRequestMethod("GET");

        var responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) { // success
            try (Scanner scanner = new Scanner(con.getInputStream(), StandardCharsets.UTF_8)) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            throw new IOException("HTTP request not successful: HTTP Error code " + responseCode);
        }
    }
}
