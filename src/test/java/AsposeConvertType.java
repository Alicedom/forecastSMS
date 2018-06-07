import com.aspose.cells.Chart;
import com.aspose.cells.ImageFormat;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

public class AsposeConvertType {

    public static void main(String[] args) throws Exception {
        String dir = "src/main/resources/";
        // load spreadsheet containing the chart
        Workbook workbook = new Workbook(dir + "Mẫu tin thời tiết tự động template.xlsx");
// get the chart present in first worksheet
//        Chart chart = book.getWorksheets().get(0).getCharts().get(0);
// render chart as PNG
//        chart.toImage(dir + "output.png", ImageFormat.getPng());
// render chart as PDF
//        chart.toPdf(dir + "output.pdf");

        // save in different formats
        workbook.save(dir + "output.pdf", SaveFormat.PDF);
        workbook.save(dir + "output.xps", SaveFormat.XPS);
        workbook.save(dir + "output.html", SaveFormat.HTML);
    }
}
