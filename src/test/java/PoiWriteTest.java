import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class PoiWriteTest {
    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream(new File("src/main/resources/Mẫu tin thời tiết tự động template.xlsx"));

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell = null;

            //Update the value of cell
            cell = sheet.getRow(1).getCell(2);
            cell.setCellValue("A");
            cell = sheet.getRow(2).getCell(2);
            cell.setCellValue("B");
            cell = sheet.getRow(3).getCell(2);
            cell.setCellValue("C");

            file.close();

            FileOutputStream outFile =new FileOutputStream(new File("src/main/resources/Mẫu tin thời tiết tự động template2.xlsx"));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
