package io.nlytx.commons.privacy

/**
  * Created by andrew@andrewresearch.net on 07/08/2016.
  * Based on original anonymous user generation for GoingOK
  */

import scala.util.Random

object Anonymiser {

  private val startChar = List[Char]('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'w', 'z')
  private val scl = startChar.length
  private val midVowel = List[Char]('a', 'e', 'i', 'o', 'u')
  private val mvl = midVowel.length
  private val endChar = List[Char]('b', 'c', 'd', 'f', 'g', 'k', 'l', 'm', 'n', 'p', 's', 't', 'v', 'x', 'z')
  private val ecl = endChar.length

  private val exclude = List[String]("but", "coc", "cok", "cox", "dic", "dik", "dix", "dum", "fag", "fuc", "fuk", "fux", "god", "koc", "kok", "lez", "lic", "lik", "lix", "pig", "put", "sex", "suk", "sux", "tit", "was", "wiz")

  private val r = Random

  private def syllable:String = {
    val syl = ""+startChar(r.nextInt(scl)) + midVowel(r.nextInt(mvl)) + endChar(r.nextInt(ecl))
    if (exclude.contains(syl)) syllable
    else syl
  }

  def word = syllable + syllable + r.nextInt(100).formatted("%02d")

  def generate(num:Int,startSet:Set[String] = Set[String]()):Set[String] = if (startSet.size >= num) startSet else generate(num,startSet.+(word))


}