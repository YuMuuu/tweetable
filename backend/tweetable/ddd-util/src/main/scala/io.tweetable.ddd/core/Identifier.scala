package io.tweetable.ddd.core

trait Identifier[A]:
  def value: A
