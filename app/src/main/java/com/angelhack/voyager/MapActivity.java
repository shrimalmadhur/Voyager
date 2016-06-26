package com.angelhack.voyager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelhack.voyager.util.ItBean;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity {
    MapView mMapView;
    GraphicsLayer graphicsLayer;
    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    List<ItBean> mplaces;
    private TextView title;
    private TextView desc;

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
            SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 30, SimpleMarkerSymbol.STYLE.TRIANGLE);
            SpatialReference spatialReference1 = SpatialReference.create(102100);
            GeometryEngine geometryEngine = new GeometryEngine();

            int[] graphicIDs = graphicsLayer.getGraphicIDs(x, y, 10);
            if (graphicIDs != null && graphicIDs.length > 0) {
                Graphic gr = graphicsLayer.getGraphic(graphicIDs[0]);
                Map<String,Object> attr = gr.getAttributes();
                Double x_val = (Double)gr.getAttributeValue("X");
                Double y_val = (Double)gr.getAttributeValue("Y");

                if(x_val !=null && y_val!= null) {
                    graphicsLayer.removeAll();
                    addPoints(x_val,y_val);
                    addLines();
                    changeImage((String)attr.get("Title"));
                    changeTitle((String)attr.get("Title"));
                    changeDesc((String)attr.get("Desc"));

                    for (String key : gr.getAttributeNames()) {
                        System.out.println("Key " + key + " value :" + gr.getAttributeValue(key));
                    }
                }
            }
        }
    };

    private HashMap<String,Object> createAttributeMap(Double x, Double y, String title,String desc)
    {
        HashMap<String, Object> attrMap = new
                HashMap<String, Object>();

        attrMap.put("Title", title);
        attrMap.put("X", x);
        attrMap.put("Y", y);
        attrMap.put("Desc", desc);
        return attrMap;
    }

    private void changeTitle(String title)
    {
        this.title = (TextView) findViewById(R.id.name);
        this.title.setText(title);

    }

    private void changeDesc(String desc)
    {
        this.desc = (TextView) findViewById(R.id.description);
        this.desc.setText(desc);
    }

    private void addPoints(Double x, Double y)
    {
        GeometryEngine geometryEngine = new GeometryEngine();
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.BLACK, 30, SimpleMarkerSymbol.STYLE.TRIANGLE);
        SimpleMarkerSymbol simpleMarkerRed = new SimpleMarkerSymbol(Color.RED, 30, SimpleMarkerSymbol.STYLE.TRIANGLE);
        SpatialReference spatialReference1 = SpatialReference.create(102100);

        List<Point> points = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();


        for(ItBean bean : mplaces)
        {
            points.add(geometryEngine.project(bean.x, bean.y, spatialReference1));
        }

        for(int i=0; i< mplaces.size();i++)
        {
            ItBean bean = mplaces.get(i);
            System.out.println("X :" + x + "Bean X :" + bean.x );
            graphics.add(new Graphic(points.get(i), (bean.x.equals(x))?simpleMarkerRed:simpleMarker, createAttributeMap(bean.x, bean.y,bean.title, bean.desc)));
        }

        for (Graphic graphic : graphics)
        {
            graphicsLayer.addGraphic(graphic);
        }

//        // create a point
//        Point point1 = geometryEngine.project(-122.507787, 37.78522, spatialReference1);
//        Point point2 = geometryEngine.project(-122.40994, 37.806381, spatialReference1);
//        Point point3 = geometryEngine.project(-122.477918, 37.857359, spatialReference1);
//        Point point4 = geometryEngine.project(-122.582974, 37.861967, spatialReference1);
//        Point point5 = geometryEngine.project(-122.596021, 37.924553, spatialReference1);
//
//        // create a graphic with the geometry and marker symbol
//        Graphic pointGraphic1 = new Graphic(point1, (-122.507787==x)?simpleMarkerRed:simpleMarker, createAttributeMap(-122.507787, 37.78522,"Lands End"));
//        Graphic pointGraphic2 = new Graphic(point2, (-122.40994==x)?simpleMarkerRed:simpleMarker, createAttributeMap(-122.40994, 37.806381,"Fisherman's Wharf"));
//        Graphic pointGraphic3 = new Graphic(point3, (-122.477918==x)?simpleMarkerRed:simpleMarker, createAttributeMap(-122.477918, 37.857359,"Sausalito"));
//        Graphic pointGraphic4 = new Graphic(point4, (-122.582974==x)?simpleMarkerRed:simpleMarker, createAttributeMap(-122.582974, 37.861967,"Muir Beach"));
//        Graphic pointGraphic5 = new Graphic(point5, (-122.596021==x)?simpleMarkerRed:simpleMarker, createAttributeMap(-122.596021, 37.924553,"Mount Tamalpais"));
//        // add the graphic to the graphics layer
//        graphicsLayer.addGraphic(pointGraphic1);
//        graphicsLayer.addGraphic(pointGraphic2);
//        graphicsLayer.addGraphic(pointGraphic3);
//        graphicsLayer.addGraphic(pointGraphic4);
//        graphicsLayer.addGraphic(pointGraphic5);
    }


    private void addLines()
    {
        GeometryEngine geometryEngine = new GeometryEngine();
        SpatialReference spatialReference2 = SpatialReference.create(4326);
        SpatialReference spatialReference1 = SpatialReference.create(102100);

        Polyline polyline = new Polyline();
        polyline.startPath(-122.507787, 37.78522);
        polyline.lineTo(-122.40994, 37.806381);
        polyline.lineTo(-122.477918, 37.857359);
        polyline.lineTo(-122.582974, 37.861967);
        polyline.lineTo(-122.596021, 37.924553);
        Geometry geometryLine = geometryEngine.project(polyline, spatialReference2, spatialReference1);
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLUE, 2, SimpleLineSymbol.STYLE.DOT);

        // create the graphic using the geometry and the symbol
        Graphic lineGraphic = new Graphic(geometryLine, lineSymbol);
        // add the graphic to the graphics layer
        graphicsLayer.addGraphic(lineGraphic);
    }

    private void changeImage(String name)
    {
        name = name.replace(' ', '_');

        InputStream ims1 = null;
        try {
            ims1 = this.getApplicationContext().getAssets().open("images/"+name.toLowerCase()+".jpg");
            ImageView iv1 = (ImageView) findViewById(R.id.imagePlace);
            Drawable d1 = Drawable.createFromStream(ims1, null);
            iv1.setImageDrawable(d1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ItBean> createEntities()
    {
        List<ItBean> places = new ArrayList<>();

        places.add(new ItBean("Lands end","Lands End is a park in San Francisco within the Golden Gate National Recreation Area which is a good spot to get pictures of the windswept shore. ", -122.507787, 37.78522));
        places.add(new ItBean("SF Wharf","Fisherman's Wharf is a neighborhood and popular tourist attraction in San Francisco. Pier 39 is the perfect place to eat some famous Bubba Gump shrimp and hang out with the sea lions.", -122.40994, 37.806381));
        places.add(new ItBean("Sausalito","Sausalito is a small city on the other side of the Golden gate Bridge which has ana amzing bunch of restaurants and photography spots. There are beautiful houses all around and you get a good view of the Bay.,", -122.477918, 37.857359));
        places.add(new ItBean("Muir Beach","Muir Beach is quite a gem of a place that is located in a pretty secluded neighborhood on the tip of North Bay. This is a beautiful beach with hiking, fishing, beaching, and picnicking options.", -122.582974, 37.861967));
        places.add(new ItBean("Tamalpais","For someone who wants a good variety of hikes, this is the perfect place. Sometimes the fog is below you when you ascend the peak and you feel like you're swimming above a sea of rolling fog. One thing however is that you make sure you know how to read a map and you know your route, because there's no service up here.", -122.596021, 37.924553));

        return places;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // after the content of this activity is set
        // the map can be accessed from the layout

        mplaces = createEntities();
        mMapView = (MapView) findViewById(R.id.map);
        graphicsLayer = new GraphicsLayer();
        mMapView.setOnSingleTapListener(mapTapCallback);

        mMapView.addLayer(graphicsLayer);

        addPoints(mplaces.get(4).x,mplaces.get(4).y);
        addLines();
        changeImage(mplaces.get(4).title);
        changeTitle(mplaces.get(4).title);
        changeDesc(mplaces.get(4).desc);
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setBackground(getDrawable(R.drawable.stop));
                } else {
                    setBackground(getDrawable(R.drawable.play));
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
//            setText("Start playing");
            setBackground(getDrawable(R.drawable.play));
            setOnClickListener(clicker);
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
}
