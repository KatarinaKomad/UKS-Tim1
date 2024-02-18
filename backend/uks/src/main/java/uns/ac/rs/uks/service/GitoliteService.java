package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.response.BranchBasicInfoDTO;
import uns.ac.rs.uks.dto.response.CommitsResponseDto;
import uns.ac.rs.uks.dto.response.KeyResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GitoliteService {

    @Value("${app.gitolite.keydir}")
    private String keyDirPath;

    @Value("${app.gitolite.script}")
    private String commitAndPushScript;

    @Value("${app.gitolite.readBranchesScript}")
    private String readBranchesScript;

    @Value("${app.gitolite.readCommitsScript}")
    private String readCommitsScript;

    @Value("${app.gitolite.workingDirectory}")
    private String scriptWorkingDirectory;

    @Value("${app.gitolite.bashLocation}")
    private String bashLocation;

    @Value("${app.gitolite.configFile}")
    private String configFile;

    private final String commitsDelimiter = "Commits";

    private static final Logger logger = LoggerFactory.getLogger(GitoliteService.class);

    public KeyResponse createKey(String value, String username) {
        if (saveKey(value, username)) {
            commitGitoliteAdmin(String.format("Saved key for user %s", username));
            return new KeyResponse(value);
        }
        throw new RuntimeException("Error saving the key");
    }

    public String createRepo(String repoName, String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, true))) {
            repoName = repoName.replace(" ", "-");
            writer.newLine();
            writer.write("repo " + repoName);
            writer.newLine();
            writer.write("    RW+     =   id_rsa");
            writer.newLine();
            writer.write("    RW+     =   " + username);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "";
        }
        commitGitoliteAdmin(String.format("Created repo %s for user %s", repoName, username));
        return String.format("GIT_SSH_COMMAND='ssh -p 2222 -i <your_private_ssh>' git clone git@localhost:%s", repoName);
    }

    private boolean saveKey(String value, String username) {
        var keyFullPath = keyDirPath + "/" + username + ".pub";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(keyFullPath))) {
            logger.info(String.format("Saving public key for user %s at location: %s", username, keyFullPath));
            writer.write(value);
            return true;
        } catch (IOException e) {
            logger.info(String.format("Failed saving the public key for user %s at location: %s", username, keyFullPath));
            return false;
        }
    }

    private void commitGitoliteAdmin(String message) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, commitAndPushScript, message);
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", commitAndPushScript));
            } else {
                logger.error("Script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    public List<BranchBasicInfoDTO> readRepoBranches(String repo) {
        try {
            var branches = new ArrayList<BranchBasicInfoDTO>();

            ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, readBranchesScript, repo);
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                parseAndAddBranchOutput(branches, line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", readBranchesScript));
            } else {
                logger.error("Script execution failed with exit code: " + exitCode);
            }

            return branches;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }


    private void parseAndAddBranchOutput(List<BranchBasicInfoDTO> list, String output) {
        var parts = output.split("\\t");
        if (parts.length == 2) {
            var dto = new BranchBasicInfoDTO();
            dto.setId((long) list.size());
            dto.setCode(parts[0]);
            dto.setName(parts[1].replace("refs/heads/", ""));
            list.add(dto);
        }
    }

    public List<CommitsResponseDto> getCommits(String repo, String branch) {
        try {
            var commits = new ArrayList<CommitsResponseDto>();
            ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, readCommitsScript, repo, branch);
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            var commitsStarted = false;
            while ((line = reader.readLine()) != null) {
                if (commitsStarted) {
                    parseAndAddCommit(commits, line);
                }
                else {
                    commitsStarted = line.equals(commitsDelimiter);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", readCommitsScript));
            } else {
                logger.error("Script execution failed with exit code: " + exitCode);
            }

            return commits;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    private void parseAndAddCommit(List<CommitsResponseDto> commits, String line) {
        String regex = "(\\w+) (.+?) \\((.+?)\\) \\[(.+?)]";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            var dto = new CommitsResponseDto();
            dto.setHash(matcher.group(1));
            dto.setMessage(matcher.group(2));
            dto.setTimeAgo(matcher.group(3));
            dto.setGitUser(matcher.group(4));
            commits.add(dto);
        }
    }
}
