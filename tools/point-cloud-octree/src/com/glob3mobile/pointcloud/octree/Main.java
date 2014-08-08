

package com.glob3mobile.pointcloud.octree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic3D;

import com.glob3mobile.pointcloud.octree.berkeleydb.BerkeleyDBOctree;

import es.igosoftware.euclid.projection.GProjection;
import es.igosoftware.euclid.vector.GVector2D;
import es.igosoftware.euclid.vector.IVector2;
import es.igosoftware.util.GUndeterminateProgress;
import es.igosoftware.util.XStringTokenizer;


public class Main {

   private static final String CLOUD_NAME = "Loudoun-VA";


   private static Geodetic3D fromRadians(final double latitudeInRadians,
                                         final double longitudeInRadians,
                                         final double height) {
      return new Geodetic3D( //
               Angle.fromRadians(latitudeInRadians), //
               Angle.fromRadians(longitudeInRadians), //
               height);
   }


   private static void load(final PersistentOctree octree,
                            final String fileName) throws IOException, FileNotFoundException {
      final GUndeterminateProgress progress = new GUndeterminateProgress(5, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final long elapsed) {
            System.out.println("Loading \"" + fileName + "\" " + progressString(stepsDone, elapsed));
         }
      };

      final GProjection projection = GProjection.EPSG_26918;
      final GProjection targetProjection = GProjection.EPSG_4326;

      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(
               fileName))))) {
         String line;
         while ((line = reader.readLine()) != null) {
            final String[] tokens = XStringTokenizer.getAllTokens(line, ",");

            final double x = Double.valueOf(tokens[0]);
            final double y = Double.valueOf(tokens[1]);
            final double z = Double.valueOf(tokens[2]);
            // final double intensity = Double.valueOf(tokens[2]);

            final IVector2 sourcePoint = new GVector2D(x, y);
            final IVector2 projectedPointInRadians = projection.transformPoint(targetProjection, sourcePoint);


            final Geodetic3D point = fromRadians(projectedPointInRadians.y(), projectedPointInRadians.x(), z);
            octree.addPoint(point);

            progress.stepDone();
         }
      }

      progress.finish();

      octree.optimize();
   }


   public static void main(final String[] args) throws IOException {
      System.out.println("PointClout OcTree 0.1");
      System.out.println("---------------------\n");


      final boolean createOT = false;
      final boolean visitOT = true;

      // 5813329 steps [Finished in 24s] 265.7kB/sec (avr=233.7kB/sec)
      // ** Visited 89 nodes with 5813329 points in 548ms


      if (createOT) {
         BerkeleyDBOctree.delete(CLOUD_NAME);

         final boolean loadPoints = true;
         if (loadPoints) {
            final String fileName = "18STJ6448.txt.gz";
            final boolean createIfNotExists = true;


            try (final PersistentOctree octree = BerkeleyDBOctree.open(CLOUD_NAME, createIfNotExists)) {
               final long start = System.currentTimeMillis();
               load(octree, fileName);
               final long elapsed = System.currentTimeMillis() - start;
               System.out.println("\n- loaded in " + elapsed + "ms");
            }
         }
      }


      if (visitOT) {
         final boolean createIfNotExists = false;
         try (final PersistentOctree octree = BerkeleyDBOctree.open(CLOUD_NAME, createIfNotExists);) {
            octree.acceptVisitor(new PersistentOctree.Visitor() {
               private int  _counter;
               private long _started;
               private long _totalPoints;


               @Override
               public void start() {
                  _counter = 0;
                  _started = System.currentTimeMillis();
                  _totalPoints = 0;
               }


               @Override
               public boolean visit(final PersistentOctree.Node node) {
                  // System.out.println(node);
                  final int pointsCount = node.getPoints().size();
                  System.out.println(node.getID() + ", points=" + pointsCount);
                  _counter++;
                  _totalPoints += pointsCount;
                  return true;
               }


               @Override
               public void stop() {
                  final long elapsed = System.currentTimeMillis() - _started;
                  System.out.println("** Visited " + _counter + " nodes with " + _totalPoints + " points in " + elapsed + "ms");
               }
            });
         }
      }

   }


}
