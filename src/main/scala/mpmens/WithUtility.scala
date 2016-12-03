package mpmens

import org.chocosolver.solver.variables.IntVar

trait WithUtility {
  private var _utility: Integer = null

  def utility(cst: Integer) {
    _utility = cst
  }

  def utility: Integer = _utility

  def solutionUtility: Int = _utility.solutionValue
}