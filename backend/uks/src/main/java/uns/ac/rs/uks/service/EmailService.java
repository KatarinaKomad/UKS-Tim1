package uns.ac.rs.uks.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceLoader resourceLoader;
    private final String templatesLocation = "templates";


    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Async
    public void sendInviteUserEmail(Repo repo, User user, String link) {
        String content = renderTemplate("inviteUserToRepo.html",
                "fullName", user.getName(),
                "repoName", repo.getName(),
                "link", link);
        String subject = "Invitation to " + repo.getName();
        sendMail("ukstim1111+" + user.getCustomUsername() +"@gmail.com", subject, content);
    }

    @Async
    public void sendResetPasswordEmail(User user, String password) {
        String content = renderTemplate("resetPassword.html",
                "fullName", user.getName(),
                "password", password);
        String subject = "Password reset";
        sendMail("ukstim1111+" + user.getCustomUsername() +"@gmail.com", subject, content);
    }


    private void sendMail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Error sending email");
            throw new RuntimeException(e);
        }
    }
    private void sendMailWithAttachment(String to, String subject, String body, byte[] attachment, String attachmentName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);
            ByteArrayDataSource byteArrayDataSourceHtml = new ByteArrayDataSource(attachment, "application/x-x509-ca-cert");
            helper.addAttachment(attachmentName, byteArrayDataSourceHtml);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Error sending email");
            throw new RuntimeException(e);
        }
    }

    private String renderTemplate(String templateName, String... variables) {
        Map<String, String> variableMap = new HashMap<>();

        List<String> keyValueList = Arrays.stream(variables).toList();

        if (keyValueList.size() % 2 != 0)
            throw new IllegalArgumentException();

        for (int i = 0; i < keyValueList.size(); i += 2) {
            variableMap.put(keyValueList.get(i), keyValueList.get(i + 1));
        }

        return renderTemplate(templateName, variableMap);
    }

    private String renderTemplate(String templateName, Map<String, String> variables) {
        String path = Paths.get(templatesLocation).resolve(templateName).toString();
        String message = null;
        try {
            message = readFileToString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String target, renderedValue;
        for (var entry : variables.entrySet()) {
            target = "\\{\\{ " + entry.getKey() + " \\}\\}";
            renderedValue = entry.getValue();

            message = message.replaceAll(target, renderedValue);
        }

        return message;
    }

    public String readFileToString(String filePath) throws IOException {
        // Load the resource
        Resource resource = resourceLoader.getResource("classpath:" + filePath);

        // Check if the resource exists
        if (!resource.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        // Read the file content into a string
//        return FileUtils.readFileToString(resource.getFile(), "UTF-8");
        try (InputStream inputStream = resource.getInputStream();
             Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        }
    }

}
