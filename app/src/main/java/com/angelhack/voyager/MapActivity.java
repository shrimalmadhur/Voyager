package com.angelhack.voyager;

import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity {
    MapView mMapView;
    GraphicsLayer graphicsLayer;



    final OnSingleTapListener mapTapCallback = new OnSingleTapListener() {
        @Override
        public void onSingleTap(float x, float y) {
            System.out.println(x + " Lattitude : " + y);
//            int[] allIDs = graphicsLayer.getGraphicIDs();
//
//            Double minDistance = Double.MAX_VALUE;
//            Graphic selGraphic = null;
//            for (int id : allIDs)
//            {
//                Graphic graphic = graphicsLayer.getGraphic(id);
//                Double x_val = (Double)graphic.getAttributeValue("X");
//                Double y_val = (Double)graphic.getAttributeValue("Y");
//
//
//                if(x_val !=null && y_val!= null) {
//                    Double distance = (x - x_val) * (x - x_val) + (y - y_val) * (y - y_val);
//
//                    if (minDistance > distance) {
//                        minDistance = distance;
//                        selGraphic = graphic;
//                    }
//                }
//            }
//
//            for (String key : selGraphic.getAttributeNames()) {
//                    System.out.println("Key " + key + " value :" + selGraphic.getAttributeValue(key));
//                }


            int[] graphicIDs = graphicsLayer.getGraphicIDs(x, y, 10);
            if (graphicIDs != null && graphicIDs.length > 0) {
                Graphic gr = graphicsLayer.getGraphic(graphicIDs[0]);
                for (String key : gr.getAttributeNames()) {
                    System.out.println("Key " + key + " value :" + gr.getAttributeValue(key));
                }
            }
        }
    };

    private HashMap<String,Object> createAttributeMap(Double x, Double y, String title)
    {
        HashMap<String, Object> attrMap = new
                HashMap<String, Object>();

        attrMap.put("Title", title);
        attrMap.put("X", x);
        attrMap.put("Y", y);
        attrMap.put("Description", "Its pretty good");
        return attrMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // after the content of this activity is set
        // the map can be accessed from the layout
        mMapView = (MapView) findViewById(R.id.map);
        graphicsLayer = new GraphicsLayer();
        mMapView.setOnSingleTapListener(mapTapCallback);

        mMapView.addLayer(graphicsLayer);

        SpatialReference spatialReference1 = SpatialReference.create(102100);
        SpatialReference spatialReference2 = SpatialReference.create(4326);

        // create a geometry engine
        GeometryEngine geometryEngine = new GeometryEngine();

        // create a point marker symbol (red, size 10, of type circle)
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.BLACK, 30, SimpleMarkerSymbol.STYLE.TRIANGLE);
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLUE, 2, SimpleLineSymbol.STYLE.DOT);

        // create a point
        Point point1 = geometryEngine.project(-122.507787, 37.78522, spatialReference1);
        Point point2 = geometryEngine.project(-122.40994, 37.806381, spatialReference1);
        Point point3 = geometryEngine.project(-122.477918, 37.857359, spatialReference1);
        Point point4 = geometryEngine.project(-122.582974, 37.861967, spatialReference1);
        Point point5 = geometryEngine.project(-122.596021, 37.924553, spatialReference1);



        // create a graphic with the geometry and marker symbol
        Graphic pointGraphic1 = new Graphic(point1, simpleMarker, createAttributeMap(-122.507787, 37.78522,"Lands End"));
        Graphic pointGraphic2 = new Graphic(point2, simpleMarker, createAttributeMap(-122.40994, 37.806381,"Fisherman's Wharf"));
        Graphic pointGraphic3 = new Graphic(point3, simpleMarker, createAttributeMap(-122.477918, 37.857359,"Sausalito"));
        Graphic pointGraphic4 = new Graphic(point4, simpleMarker, createAttributeMap(-122.582974, 37.861967,"Muir Beach"));
        Graphic pointGraphic5 = new Graphic(point5, simpleMarker, createAttributeMap(-122.596021, 37.924553,"Mount Tamalpais"));
        // add the graphic to the graphics layer
        graphicsLayer.addGraphic(pointGraphic1);
        graphicsLayer.addGraphic(pointGraphic2);
        graphicsLayer.addGraphic(pointGraphic3);
        graphicsLayer.addGraphic(pointGraphic4);
        graphicsLayer.addGraphic(pointGraphic5);

        Polyline polyline = new Polyline();
        polyline.startPath(-122.507787, 37.78522);
        polyline.lineTo(-122.40994, 37.806381);
        polyline.lineTo(-122.477918, 37.857359);
        polyline.lineTo(-122.582974, 37.861967);
        polyline.lineTo(-122.596021, 37.924553);
        Geometry geometryLine = geometryEngine.project(polyline, spatialReference2, spatialReference1);

        // create the graphic using the geometry and the symbol
        Graphic lineGraphic = new Graphic(geometryLine, lineSymbol);
        // add the graphic to the graphics layer
        graphicsLayer.addGraphic(lineGraphic);
    }
}
