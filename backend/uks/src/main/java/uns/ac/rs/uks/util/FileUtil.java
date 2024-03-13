package uns.ac.rs.uks.util;

import uns.ac.rs.uks.dto.response.FileDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static byte[] zipFiles(String path){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            File sourceDirectory = new File(path);
            compressDirectory(sourceDirectory, sourceDirectory.getName(), zos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return baos.toByteArray();
    }

    private static void compressDirectory(File dir, String baseName, ZipOutputStream zos) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                compressDirectory(file, baseName + File.separator + file.getName(), zos);
            } else {
                ZipEntry zipEntry = new ZipEntry(baseName + File.separator + file.getName());
                zos.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                try (FileInputStream fis = new FileInputStream(file)) {
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
                zos.closeEntry();
            }
        }
    }
    public static String readContent(Path p){
        try {
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
