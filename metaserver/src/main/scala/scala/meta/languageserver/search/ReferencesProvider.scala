package scala.meta.languageserver.search

import com.typesafe.scalalogging.LazyLogging
import org.langmeta.io.AbsolutePath
import langserver.{types => l}
import langserver.messages.ReferencesResult
import scala.meta.languageserver.ScalametaEnrichments._

object ReferencesProvider extends LazyLogging {

  def references(
      symbolIndex: SymbolIndex,
      path: AbsolutePath,
      position: l.Position,
      context: l.ReferenceContext
  ): ReferencesResult = {
    logger.info("ReferencesProvider.references started")
    val symbolOpt = symbolIndex
      .findSymbol(path, position.line, position.character)
      .toList
    logger.info("symbolIndex.findSymbol done")
    val data = symbolOpt.flatMap(symbol => symbolIndex.referencesData(symbol))
    logger.info("symbolIndex.referencesData done")
    val poses = data.flatMap(data => data.referencePositions(context.includeDeclaration))
    logger.info("data.referencePositions done")
    val locations = poses.map(_.toLocation)
    logger.info("toLocation done")
    ReferencesResult(locations)
  }

}
