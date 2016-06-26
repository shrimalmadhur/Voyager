package com.angelhack.voyager;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

public class MapActivity extends AppCompatActivity {
    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // after the content of this activity is set
        // the map can be accessed from the layout
        mMapView = (MapView) findViewById(R.id.map);
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(graphicsLayer);

        SpatialReference spatialReference1 = SpatialReference.create(102100);
        SpatialReference spatialReference2 = SpatialReference.create(4326);

        // create a point marker symbol (red, size 10, of type circle)
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.BLACK, 20, SimpleMarkerSymbol.STYLE.TRIANGLE);

        // create a geometry engine
        GeometryEngine geometryEngine = new GeometryEngine();


        // create a point
        Point point1 = geometryEngine.project(-122.507787, 37.78522, spatialReference1);
        Point point2 = geometryEngine.project(-122.40994, 37.806381, spatialReference1);
        Point point3 = geometryEngine.project(-122.477918, 37.857359, spatialReference1);
        Point point4 = geometryEngine.project(-122.582974, 37.861967, spatialReference1);
        Point point5 = geometryEngine.project(-122.596021, 37.924553, spatialReference1);

        // create a graphic with the geometry and marker symbol
        Graphic pointGraphic1 = new Graphic(point1, simpleMarker);
        Graphic pointGraphic2 = new Graphic(point2, simpleMarker);
        Graphic pointGraphic3 = new Graphic(point3, simpleMarker);
        Graphic pointGraphic4 = new Graphic(point4, simpleMarker);
        Graphic pointGraphic5 = new Graphic(point5, simpleMarker);

        // add the graphic to the graphics layer
        graphicsLayer.addGraphic(pointGraphic1);
        graphicsLayer.addGraphic(pointGraphic2);
        graphicsLayer.addGraphic(pointGraphic3);
        graphicsLayer.addGraphic(pointGraphic4);
        graphicsLayer.addGraphic(pointGraphic5);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLUE, 2, SimpleLineSymbol.STYLE.DOT);

        Polyline lineGeometry = new Polyline();
        lineGeometry.startPath(-122.507787, 37.78522);
        lineGeometry.lineTo(-122.40994, 37.806381);
        lineGeometry.lineTo(-122.477918, 37.857359);
        lineGeometry.lineTo(-122.582974, 37.861967);
        lineGeometry.lineTo(-122.596021, 37.924553);
        Geometry geometry = geometryEngine.project(lineGeometry, spatialReference2, spatialReference1);

        // create the graphic using the geometry and the symbol
        Graphic lineGraphic = new Graphic(geometry, lineSymbol);
        // add the graphic to the graphics layer
        graphicsLayer.addGraphic(lineGraphic);
    }
}
