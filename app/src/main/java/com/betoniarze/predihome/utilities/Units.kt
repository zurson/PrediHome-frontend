package com.betoniarze.predihome.utilities

enum class Units(val symbol: String) {

    METERS("m"),
    KILOMETERS("km"),
    SQUARE_METERS("m²"),
    PLN("PLN"),
    MONTH("month"),
    NONE("")
    ;

    override fun toString(): String {
        return symbol
    }
}
