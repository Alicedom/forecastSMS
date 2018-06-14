package getapi.gfs;

import ucar.nc2.VariableSimpleIF;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;

import java.io.IOException;
import java.util.List;

public class Grib2 {

    String testfile = "src/test/file/Download/2";

    public static void main(String[] args) {
        try {
            new Grib2().gribPlugin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gribPlugin() throws IOException {

        GridDataset grid = GridDataset.open(testfile);
        List<VariableSimpleIF> list = grid.getDataVariables();
        list.forEach(var -> {
            System.out.println("\nvar.getFullName() = " + var.getFullName());
            System.out.println("var.getUnitsString() = " + var.getUnitsString());
            System.out.println("var.getDataType() = " + var.getDataType());
            System.out.println("var.getRank() = " + var.getRank());
            System.out.println("var.getShape().toString() = " + var.getShape().length);
        });

        List<GridDatatype> types = grid.getGrids();
        for (GridDatatype type : types) {
            System.out.println("\ntype.getFullName() = " + type.getFullName());
            System.out.println("type.getInfo() = " + type.getInfo());

        }

    }
}
