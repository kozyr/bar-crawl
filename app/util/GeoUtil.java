package util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import play.Logger;

public class GeoUtil {
    private final static int DEFAULT_GEOM = 4326;
    private final static int TARGET_GEOM = 3857;

    public static final Point fromLatLon(double lat, double lon)  {
        Point result = null;
        try {
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + DEFAULT_GEOM);
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + TARGET_GEOM);
            // Logger.info(targetCRS.toWKT());
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), DEFAULT_GEOM);
            Point point = geometryFactory.createPoint(
                    new Coordinate(lat, lon));
            result = (Point) JTS.transform(point, transform);
            result.setSRID(TARGET_GEOM);
        } catch (FactoryException e) {
            Logger.error("Could not get CRS for EPSG_3857", e);
        } catch (TransformException e) {
            Logger.error("Could not transform (" + lat + ", " + lon + ") into EPSG_3857");
        }

        return result;
    }
}
