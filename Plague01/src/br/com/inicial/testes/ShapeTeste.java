package br.com.inicial.testes;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.oracle.sdo.Coordinates;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.primefaces.model.map.LatLng;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;

public class ShapeTeste {

	public static void main(String[] args) throws IOException {

//		File file = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Santo Antonio\\Shapes\\Shapes\\FazStoAn.shp");
		File file = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Santo Antonio\\Shapes\\Shapes\\FazStoRe.shp");
		Map<String, Serializable> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource source = dataStore.getFeatureSource(typeName);

		FeatureCollection collection = source.getFeatures();
		FeatureIterator<SimpleFeature> results = collection.features();
		try {
			int x = 1;
			while (results.hasNext()) {
				System.out.println("Coodenadas=" +x);
				x++;
				org.primefaces.model.map.Polygon talhaoPolygon = new org.primefaces.model.map.Polygon();
				SimpleFeature feature = (SimpleFeature) results.next();
				MultiLineString mult = (MultiLineString) feature.getAttribute(0);
				Coordinate[] coordenadas = mult.getCoordinates();
				for (Coordinate coordinate : coordenadas) {
					double lat = coordinate.y;
					double lon = coordinate.x;
					LatLng coord = new LatLng(lat,lon);
					talhaoPolygon.getPaths().add(coord);
					System.out.println(coordinate.y);
					System.out.println(coordinate.x);
					System.out.println(coordinate.z);
				}
				talhaoPolygon.setStrokeColor("#ff0000");
	        	talhaoPolygon.setFillColor("#ff0000");
	        	talhaoPolygon.setStrokeOpacity(0.7);
	        	talhaoPolygon.setFillOpacity(0.7);
//		        polygonModel.addOverlay(talhaoPolygon);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			results.close();
			dataStore.dispose();
		}

	}
}
