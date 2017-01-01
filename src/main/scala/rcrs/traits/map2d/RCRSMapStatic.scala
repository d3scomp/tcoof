package rcrs.traits.map2d

import java.io._

import mpmens.traits.map2d.Position
import rcrs.LineOfSight
import rescuecore2.config.Config
import rescuecore2.standard.entities.{Area, StandardWorldModel}
import rescuecore2.worldmodel.EntityID

import scala.collection.JavaConverters._
import scala.collection.mutable


object RCRSMapStatic {
  private var initialized = false

  var lineOfSight = mutable.Map.empty[EntityID, Set[EntityID]]

  def initialize(config: Config, model: StandardWorldModel): Unit = {
    val precomputeFileName = "precompute.data"

    if (!initialized) {
      // try to load map from file
      var input: ObjectInputStream = null
      try {
        input = new ObjectInputStream(new FileInputStream(precomputeFileName))
        lineOfSight = loadLineOfSight(input)
        println(s"Loaded precomputed data from '${precomputeFileName}'")
        initialized = true
      } catch {
        case e: FileNotFoundException => println(s"File with precomputed data '${precomputeFileName}' not found")
      } finally {
        if (input != null) {
          input.close()
        }
      }
    }

    if (!initialized) {
      val modelIterable = model.asScala

      val entityCount = modelIterable.size
      var entityIndex = 0

      println("Computation of lines of sight...")
      for (entity <- modelIterable) {
        entity match {
          case area: Area =>
            val los = new LineOfSight(config, model)
            val visibleEntities = los.getVisibleEntities(Position(area.getX, area.getY)).asScala.collect{ case visibleArea: Area => visibleArea.getID }.toSet
            lineOfSight += (area.getID -> visibleEntities)
          case _ =>
        }

        entityIndex = entityIndex + 1

        if (entityIndex % 100 == 0) {
          println(s"  $entityIndex / $entityCount")
        }
      }

      // try to save map to file
      var output: ObjectOutputStream = null
      try {
        val output = new ObjectOutputStream(new FileOutputStream(precomputeFileName))
        saveLineOfSight(output)
        println(s"Saved precomputed data to \'${precomputeFileName}\'")
      } finally {
        if (output != null) {
          output.close
        }
      }

      println(s"  finished")
      initialized = true
    }
  }

  private def loadLineOfSight(objectInputStream: ObjectInputStream): mutable.Map[EntityID, Set[EntityID]] = {
    val o = objectInputStream.readObject
    val loadedMap = o.asInstanceOf[mutable.Map[Int, Set[Int]]]

    // transforms Map[Int, Set[Int]] to Map[EntityID, Set[EntityID]]
    return loadedMap.map{case (key: Int, value: Set[Int]) => (new EntityID(key), value.map(valueID => new EntityID(valueID)))}
  }

  private def saveLineOfSight(objectOutputStream: ObjectOutputStream): Unit = {
    // transforms Map[EntityID, Set[EntityID]] to Map[Int, Set[Int]]
    val mapToSave = lineOfSight.map{case (key: EntityID, value: Set[EntityID]) => (key.getValue, value.map(valueID => valueID.getValue))}

    objectOutputStream.writeObject(mapToSave)
  }
}
