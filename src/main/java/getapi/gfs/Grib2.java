package getapi.gfs;

import getapi.dao.Dao;
import getapi.models.Nooa;
import getapi.models.Station;
import ucar.ma2.Array;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Grib2 {



    public static void main(String[] args) {
        String testfile = "data/forecastSMS/file/gfs.2018061406/1";
        try {
            new Grib2().gribPlugin(testfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gribPlugin(String gribFile) throws IOException {

        Dao dao = new Dao();
        List<Station> listStation = dao.getStationEnalbeApi();
        List<Nooa> listNooa = new ArrayList<Nooa>();
        Nooa nooa = new Nooa();

        GridDataset gds = GridDataset.open(gribFile);
        List<GridDatatype> types = gds.getGrids();

        for (Station station : listStation) {

            GridDatatype grid = gds.findGridDatatype(types.get(0).getFullName());
            GridCoordSystem gcs = grid.getCoordinateSystem();
            double lat = station.getLat();
            double lon = station.getLon();
            int[] xy = gcs.findXYindexFromLatLon(lat, lon, null); // xy[0] = x, xy[1] = y

            for (GridDatatype type : types) {
                String fullname = type.getFullName();
                String unit = type.getUnitsString();
                grid = gds.findGridDatatype(fullname);

                Array data = grid.readDataSlice(0, 0, xy[1], xy[0]); // note order is t, z, y, x
                double val = data.getDouble(0); // we know its a scalar

                if(unit.equals("K")){
                    val = val -273.15;
                }
               nooa.setObject(fullname,val);
            }
            String stationCode = station.getStation_code();
            nooa.setStationCode(stationCode);

            listNooa.add(nooa);

        }
    }
}
