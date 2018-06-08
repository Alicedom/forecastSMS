import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

public class AsposeConvertType {

    public static void main(String[] args) throws Exception {
        String dir = "src/main/resources/Bản tin thời tiết tự động ";

        Workbook workbook = new Workbook(dir + ".xlsx");

        // save in different formats
        workbook.save(dir + ".pdf", SaveFormat.PDF);
        workbook.save(dir + ".xps", SaveFormat.XPS);
        workbook.save(dir + ".html", SaveFormat.HTML);
    }
}
