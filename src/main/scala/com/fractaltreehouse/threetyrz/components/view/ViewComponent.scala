package com.fractaltreehouse.threetyrz.components.view

import tyrian.*

trait ViewComponent[M, S]:
  def view(state: S): Html[M]
