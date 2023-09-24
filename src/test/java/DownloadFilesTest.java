import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;

public class DownloadFilesTest {

    //для случаев, когда необходимо скачать файл без href
    static {
        Configuration.fileDownload = FileDownloadMode.PROXY;
    }

    @Disabled
    @Test
    void downloadTest() throws Exception {
        Selenide.open("https://github.com/qa-guru/niffler/blob/master/README.md");
        File downloadedFile= $("a[href*='/qa-guru/niffler/raw/master/README.md']").download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            byte[] bytes = is.readAllBytes();
            String fileAsString = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(fileAsString);
            Assertions.assertTrue(fileAsString.contains("Технологии, использованные в Niffler"));
        }
    }

    @Test
    void uploadTest() throws Exception {
        Selenide.open("https://tus.io/demo.html"); //это универсальный локатор для аплоада файлов везде
        $("input[type='file']").uploadFromClasspath("pictures/cat.jpg");
        $("._heading_gq6c0_21").shouldHave(Condition.text("The upload is complete!"));
    }
}
