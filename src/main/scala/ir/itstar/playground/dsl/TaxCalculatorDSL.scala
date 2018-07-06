package ir.itstar.playground.dsl

import ir.itstar.playground.dsl.Cart.CartField
import ir.itstar.playground.dsl.Country.{CartPredict, CountryToTaxCalculation, England, France, Iran, TaxCalculator}

import scala.language.implicitConversions


case class Cart(
                 total: BigDecimal,
                 shipping: BigDecimal,
                 discount: BigDecimal,
                 country: Country
               )

object Cart {

  type CartField = Cart => BigDecimal

  val shipping: CartField = (c: Cart) => c.shipping
  val discount: CartField = (c: Cart) => c.discount
}


trait Country

object Country {

  case object France extends Country

  case object England extends Country

  case object Iran extends Country

  type CartPredict = Cart => Boolean

  type TaxCalculator = Cart => BigDecimal

  type CountryToTaxCalculation = (Country, TaxCalculator)

  implicit def taxAndCountryToTaxCalculation(tc: TaxAndCountryContainer): CountryToTaxCalculation = {
    (tc.country, (cart: Cart) => cart.total * tc.taxRate)
  }




}

case class CartCombinator(cartField: CartField) {
  self =>

  def and(comb: CartCombinator): CartCombinator =
    CartCombinator((cart: Cart) => cartField(cart) + comb.cartField(cart))

  def &(comb: CartCombinator): CartCombinator = and(comb)

  def If(p: CartPredict): CartCombinator =
    CartCombinator((cart: Cart) => if (p(cart)) cartField(cart) else 0)


  def >(value: BigDecimal): CartPredict = {
    cart: Cart => cartField(cart) > value
  }
}


case class CountryContainer(country: Country) {

  def take(taxRate: BigDecimal): TaxAndCountryContainer = {
    TaxAndCountryContainer(country, taxRate)
  }
}

object CountryContainer{
  def For(country:Country) : CountryContainer = CountryContainer(country)
}

case class TaxAndCountryContainer(country: Country, taxRate: BigDecimal) {

  def ignore(cartFields: CartField*): CountryToTaxCalculation = {
    (country, (cart: Cart) =>
      (cart.total - cartFields.foldLeft(BigDecimal(0))((acc, field) => acc + field(cart))) * taxRate)
  }

  def addTouristPrice(f: Cart => BigDecimal): CountryToTaxCalculation =
    addCustomValue(f)

  def addCustomValue(f: CartField): CountryToTaxCalculation =
    (country, (cart: Cart) => (cart.total + f(cart)) * taxRate)

}


object SampleTaxRules {
  import CountryContainer._


  For(France) take 1.5
  For(England) take 2.5 ignore Cart.shipping
  For(England) take 2.5 ignore Cart.shipping If(Cart.discount > 10)
  For(Iran) take 8 addTouristPrice (c => c.total * 0.3)

}
















