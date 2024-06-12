package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.response.*;
import uns.ac.rs.uks.util.DateUtil;
import uns.ac.rs.uks.util.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public List<CommitsResponseDto> getFileCommits(String repoName, String branch, String path) {
        String filePathWithoutRepo = path.replaceFirst(Pattern.quote(repoName + "\\"), "");
        String script = readCommitsScript;
        ProcessBuilder processBuilder =
                new ProcessBuilder(bashLocation, script, repoName, branch, filePathWithoutRepo);
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
                logger.error(String.format("Script %s execution failed with exit code: " + exitCode, readBranchesScript));
            }

            return branches;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    private void parseAndAddBranchOutput(List<BranchDTO> list, String output) {

        String[] parts = output.split("\\|");
        if (parts.length == 3) {
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setUpdatedAt(DateUtil.parseGitoliteDate(parts[0]));
            branchDTO.setName(parts[1]);
            branchDTO.setUpdatedBy(parts[2]);
            list.add(branchDTO);
        }
    }

    public CommitDiffResponseDTO getCommitDiff(String repo, String branchName, String commit) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(bashLocation, getDifferencesScript, repo, branchName, commit);
            processBuilder.directory(new File(scriptWorkingDirectory));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            CommitDiffResponseDTO changes = parseGitDiff(process);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info(String.format("Script %s executed successfully", getDifferencesScript));
            } else {
                logger.error(String.format("Script %s execution failed with exit code: " + exitCode, getDifferencesScript));
            }
            return changes;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private CommitDiffResponseDTO parseGitDiff(Process process) throws IOException {
        CommitDiffResponseDTO dto = new CommitDiffResponseDTO();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.contains("changed") || line.contains("insertion(+)") || line.contains("deletion(-)")) {
                dto.setStats(line);
                break;
            }
        }
        List<FileChangeResponseDTO> files = parseGitDiffOutput(reader);
        dto.setFileChanges(files);
        return dto;
    }

    private List<FileChangeResponseDTO> parseGitDiffOutput(BufferedReader reader) throws IOException {
        String line;
        List<FileChangeResponseDTO> dtoList = new ArrayList<>();
        FileChangeResponseDTO fileDTO = null;
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            // new file changes start
            if(line.startsWith("+++")){
                // set accumulated difference from previous file
                if(fileDTO != null){
                    fileDTO.setChanges(stringBuilder.toString());
                    dtoList.add(fileDTO);
                }
                // create new file dto
                fileDTO = new FileChangeResponseDTO();
                fileDTO.setFileName(line.substring(6));
                stringBuilder = new StringBuilder();
            } else if (line.startsWith("@@") && fileDTO != null) {
                fileDTO.setStats(line);
            } else if ((line.startsWith("+") || line.startsWith("-")) && !line.startsWith("---")) {
                // accumulate file changes
                stringBuilder.append(line).append("\n");
            }
        }
        if(fileDTO != null){
            fileDTO.setChanges(stringBuilder.toString());
            dtoList.add(fileDTO);
        }
        return dtoList;
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
                logger.error(String.format("Script %s execution failed with exit code: " + exitCode, script));
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
                logger.error(String.format("Script %s execution failed with exit code: " + exitCode, script));
            }
            return commits;
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<FileDTO> getFiles(String repoName, String branchName, String path) {
        String filePathWithoutRepo = path.replaceFirst(Pattern.quote(repoName + "\\"), "");
        Path fullPath = Objects.equals(filePathWithoutRepo, "") ?
                Paths.get(scriptWorkingDirectory, repoName) :
                Paths.get(scriptWorkingDirectory, repoName, filePathWithoutRepo);

        partialRepoClone(repoName, branchName, filePathWithoutRepo);
        try {
            List<FileDTO> files = Files.walk(fullPath, 1)
                    .filter(p -> shouldParse(p, filePathWithoutRepo, fullPath.toString()))
                    .map(p -> fileToDTO(p, getFileCommits(repoName, branchName, p.subpath(2, p.getNameCount()).toString())))
                    .toList();
            removeClonedRepo(repoName);
            return files;
        } catch (IOException e) {
            e.printStackTrace();
            removeClonedRepo(repoName);
            return null;
        }
    }

    private FileDTO fileToDTO(Path p, List<CommitsResponseDto> commits) {
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
