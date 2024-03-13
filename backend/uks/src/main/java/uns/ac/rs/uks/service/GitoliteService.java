package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.response.*;
import uns.ac.rs.uks.util.DateUtil;
import uns.ac.rs.uks.util.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GitoliteService {
    @Value("${app.gitolite.keydir}")
    private String keyDirPath;
    @Value("${app.gitolite.workingDirectory}")
    private String scriptWorkingDirectory;
    @Value("${app.gitolite.bashLocation}")
    private String bashLocation;
    @Value("${app.gitolite.configFile}")
    private String configFile;


    @Value("${app.gitolite.script}")
    private String commitAndPushScript;
    @Value("${app.gitolite.readCommitsScript}")
    private String readCommitsScript;
    @Value("${app.gitolite.getDifferencesScript}")
    private String getDifferencesScript;
    @Value("${app.gitolite.mergeScript}")
    private String mergeScript;
    @Value("${app.gitolite.deleteBranchScript}")
    private String deleteBranchScript;
    @Value("${app.gitolite.newBranchScript}")
    private String newBranchScript;
    @Value("${app.gitolite.initialCommitScript}")
    private String initialCommitScript;
    @Value("${app.gitolite.renameBranchScript}")
    private String renameBranchScript;
    @Value("${app.gitolite.readBranchesScript}")
    private String readBranchesScript;
    @Value("${app.gitolite.removeClonedRepoScript}")
    private String removeClonedRepoScript;
    @Value("${app.gitolite.cloneRepoScriptFirstLevelSingleBranch}")
    private String cloneRepoScript_firstLevelSingleBranch;
    @Value("${app.gitolite.cloneRepoScriptSpecificFolderAndBranch}")
    private String cloneRepoScript_specificFolderAndBranch;

    private final String commitsDelimiter = "Commits";

    private final String differencesDelimiter = "Differences";

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
        pushInitialCommit(repoName);
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

    public List<CommitsResponseDto> getBranchCommits(String repo, String branch) {
        String script = readCommitsScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo, branch);
        List<CommitsResponseDto> commits = execScriptWithCommitHistory(processBuilder, script);
        removeClonedRepo(repo);
        return commits;
    }
    public List<CommitsResponseDto> getFileCommits(String repo, String branch, String path) {
        String script = readCommitsScript;
        ProcessBuilder processBuilder;
        if (Objects.equals(path, "")){
            processBuilder = new ProcessBuilder(bashLocation, script, repo, branch);
        } else {
            processBuilder = new ProcessBuilder(bashLocation, script, repo, branch, path);
        }
        return execScriptWithCommitHistory(processBuilder, script);


    }

    private ArrayList<CommitsResponseDto> parseCommits(Process process) throws IOException {
        var commits = new ArrayList<CommitsResponseDto>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        var commitsStarted = false;
        while ((line = reader.readLine()) != null) {
            if (commitsStarted) {
                parseAndAddCommit(commits, line);
            } else {
                commitsStarted = line.equals(commitsDelimiter);
            }
        }
        return commits;
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

    public List<BranchDTO> readRepoBranches(String repo) {
        try {
            var branches = new ArrayList<BranchDTO>();

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

    private void parseAndAddBranchOutput(List<BranchDTO> list, String output) {

        String[] parts = output.split("  ");
        if (parts.length == 3) {
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setUpdatedAt(DateUtil.parseGitoliteDate(parts[0]));
            branchDTO.setName(parts[1]);
            branchDTO.setUpdatedBy(parts[2]);
            list.add(branchDTO);
        }
    }

    public String getDifferences(String repo, String originBranch, String destinationBranch) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, getDifferencesScript, repo, destinationBranch, originBranch);
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            StringBuilder changes = parseGitDiff(process);

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", getDifferencesScript));
            } else {
                logger.error("Script execution failed with exit code: " + exitCode);
            }
            return changes.toString();
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return "";
    }

    private StringBuilder parseGitDiff(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder changes = new StringBuilder();
        var differencesStarted = false;
        while ((line = reader.readLine()) != null) {
            if (differencesStarted) {
                changes.append(parseGitDiffOutput(line));
            } else {
                differencesStarted = line.equals(differencesDelimiter);
            }
        }
        return changes;
    }

    private String parseGitDiffOutput(String line) {
        line = line.replaceAll("\u001B\\[[;\\d]*m", "");

        if (line.startsWith("diff --git")) {
            return "";
        }

        String substring = line.substring(2, line.length() - 2);
        if (line.startsWith("{+")) {
            return ("+ " + substring) + "\n";
        } else if (line.startsWith("{-")) {
            return "- " + substring + "\n";
        }else if (line.startsWith("+++")){
            return "File " + line.substring(6) + "\n";
        }
        return "";
    }

    private void commitGitoliteAdmin(String message) {
        String script = commitAndPushScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, message);
        execScript(processBuilder, script);
    }

    public void mergeBranches(String repo, String originBranch, String destinationBranch) {
        String script = mergeScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo, originBranch, destinationBranch);
        execScript(processBuilder, script);
    }

    public void deleteBranch(String repo, String branch) {
        String script = deleteBranchScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo, branch);
        execScript(processBuilder, script);
    }

    public void newBranch(String repo, String originBranch, String newBranch) {
        String script = newBranchScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo, originBranch, newBranch);
        execScript(processBuilder, script);
    }
    public void renameBranch(String repo, String oldName, String newName) {
        String script = renameBranchScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo, oldName, newName);
        execScript(processBuilder, script);
    }

    private void pushInitialCommit(String repo) {
        String script = initialCommitScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo);
        execScript(processBuilder, script);
    }

    public void partialRepoClone(String repo, String branch, String folderPath) {
        String script;
        ProcessBuilder processBuilder;

        if (!Objects.equals(folderPath, "")){
            script = cloneRepoScript_specificFolderAndBranch;
            processBuilder = new ProcessBuilder(bashLocation, script, repo, branch, folderPath.replace('\\', '/'));
        } else {
            script = cloneRepoScript_firstLevelSingleBranch;
            processBuilder = new ProcessBuilder(bashLocation, script, repo, branch);
        }
        execScript(processBuilder, script);
    }

    public void removeClonedRepo(String repo) {
        String script = removeClonedRepoScript;
        ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, script, repo);
        execScript(processBuilder, script);
    }

    private void execScript(ProcessBuilder processBuilder, String script){
        try {
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", script));
            } else {
                logger.error("Script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private ArrayList<CommitsResponseDto> execScriptWithCommitHistory(ProcessBuilder processBuilder, String script){
        try {
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            ArrayList<CommitsResponseDto> commits = parseCommits(process);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", script));
            } else {
                logger.error("Script execution failed with exit code: " + exitCode);
            }
            return commits;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<FileDTO> getFiles(String repoName, String branchName, String path) {
        String filePath = path.replaceFirst(Pattern.quote(repoName + "\\"), "");
        Path fullPath = Objects.equals(filePath, "") ?
                Paths.get(scriptWorkingDirectory, repoName) :
                Paths.get(scriptWorkingDirectory, repoName, filePath);

       partialRepoClone(repoName, branchName, filePath);
        try {
            List<FileDTO> files = Files.walk(fullPath, 1)
                    .filter(p -> shouldParse(p, filePath, fullPath.toString()))
                    .map(p -> fileToDTO(p, getFileCommits(repoName, branchName, filePath), repoName))
                    .toList();
            removeClonedRepo(repoName);
            return files;
        } catch (IOException e) {
            e.printStackTrace();
            removeClonedRepo(repoName);
            return null;
        }
    }

    private FileDTO fileToDTO(Path p, List<CommitsResponseDto> commits, String repoName) {
        FileDTO dto = new FileDTO();
        String fileName = p.getName(p.getNameCount() - 1).toString();

        Path pathWithRepo = p.subpath(2, p.getNameCount());
        Path parent = pathWithRepo.getParent() != null ? pathWithRepo.getParent() : null;
        Path grandParent = parent != null && parent.getParent() != null ? parent.getParent() : null;

        dto.setParentPath(grandParent != null ? grandParent.toString(): "");
        dto.setName(fileName);
        dto.setIsFolder(Files.isDirectory(p));
        dto.setCommitHistory(commits);
        dto.setPath(pathWithRepo.toString());

        if(!Files.isDirectory(p)) {
            dto.setContent(FileUtil.readContent(p));
        }
        return dto;
    }

    private boolean shouldParse(Path p, String filePath, String fullPath) {
//        String lastPartOfPath = p.getName(p.getNameCount() - 1).toString();
        return p.getNameCount() > 3 &&
                !p.getName(3).toString().equals(".git") &&
//                !lastPartOfPath.equals(filePath) &&
            (!p.toString().equals(fullPath)  || !Files.isDirectory(p) );
    }

//    public byte[] zipFilesFromGitolite(String repoName) {
//        return FileUtil.zipFiles(scriptWorkingDirectory  + File.separator + repoName);
//    }
}
