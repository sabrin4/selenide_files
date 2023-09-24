package parsing;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;

public class FileParsingTest {
    private ClassLoader cl = FileParsingTest.class.getClassLoader();

    @Test
    void pdfParseTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfFile = $("a[href*='junit-user-guide-5.10.0.pdf']").download();
        PDF pdf = new PDF(pdfFile);
        Assertions.assertEquals(
                "Stefan Bechtold, Sam Brannen, Johannes Link, Matthias Merdes, Marc Philipp, Juliette de Rancourt, Christian Stein",
                pdf.author
        );
        System.out.println();
    }

    @Test
    void xlsParseTest() throws Exception {
        Selenide.open("https://excelvba.ru/programmes/Teachers");
        File download = $("a[href='https://ExcelVBA.ru/sites/default/files/teachers.xls']").download();
        XLS xls = new XLS(download);
        Assertions.assertTrue(xls.excel.getSheetAt(0).getRow(3).getCell(2).getStringCellValue().startsWith("1. Суммарное количество часов"));
    }

    @Test
    void csvParseTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("csvfile.csv");
             InputStreamReader isr = new InputStreamReader(is)) {
            CSVReader csvReader = new CSVReader(isr);
            List<String[]> content = csvReader.readAll();
            Assertions.assertArrayEquals(new String[]{"Samara", "5"}, content.get(1));
        }
    }

    @Test
    void csvEqualFilesTest() throws Exception {
        Selenide.open("https://github.com/sabrin4/qa_std/blob/main/src/test/resources/testFile.csv");
        File downloadedFile = $("a[href=\"/sabrin4/qa_std/raw/main/src/test/resources/testFile.csv\"]").download();
        try (InputStream isExpectedFile = cl.getResourceAsStream("csvfile.csv");
             InputStream isActual = new FileInputStream(downloadedFile)) {
            Assertions.assertArrayEquals(isExpectedFile.readAllBytes(), isActual.readAllBytes());
        }
    }

    @Test
    void zipFileTest() throws Exception {
        String[] zipFiles = new String[]{"File1.txt", "File2.txt"};
        try (InputStream is = cl.getResourceAsStream("archFile.rar");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            int i = 0;
            while ((entry = zis.getNextEntry()) != null) {
                Assertions.assertEquals(zipFiles[i], entry.getName());
                i++;
            }
        }
    }

    @Test
    void jsonTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = cl.getResourceAsStream("person.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            JsonObject jsonObject = gson.fromJson(isr, JsonObject.class);
            Assertions.assertEquals(jsonObject.get("name").getAsString(), "Sergey");
            Assertions.assertFalse(jsonObject.get("hasJobOffer").getAsBoolean());
        }
    }

    @Test
    void jsonParseFromClassTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = cl.getResourceAsStream("person.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            Person person = gson.fromJson(isr, Person.class);
            Assertions.assertEquals(person.name, "Sergey");
            Assertions.assertFalse(person.hasJobOffer);
        }
    }
}
