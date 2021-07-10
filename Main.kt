package converter

enum class Units(val single: String, val plural: String) {
    // length units
    METER( "meter", "meters"),
    KILOMETER("kilometer", "kilometers"),
    CENTIMETER("centimeter", "centimeters"),
    MILLIMETER("millimeter", "millimeters"),
    MILE("mile", "miles"),
    YARD("yard", "yards"),
    FOOT("foot", "feet"),
    INCH("inch", "inches"),
    // weight units
    GRAM("gram", "grams"),
    KILOGRAM("kilogram", "kilograms"),
    MILLIGRAM("milligram", "milligrams"),
    POUND("pound", "pounds"),
    OUNCE("ounce", "ounces"),
    // temperature units
    CELSIUS("degree Celsius", "degrees Celsius"),
    FAHRENHEIT("degree Fahrenheit", "degrees Fahrenheit"),
    KELVIN("kelvin", "kelvins"),

    NULL("???", "???")
}

fun getMeasure(unit: Units): String {
    return when (unit) {
        Units.METER, Units.KILOMETER, Units.MILLIMETER, Units.CENTIMETER, Units.MILE, Units.FOOT,
        Units.YARD, Units.INCH -> "length"
        Units.GRAM, Units.KILOGRAM, Units.MILLIGRAM, Units.POUND, Units.OUNCE -> "weight"
        Units.KELVIN, Units.FAHRENHEIT, Units.CELSIUS -> "temperature"
        else -> "???"
    }
}

fun getUnit(unit: String): Units {
    return when (unit) {
        "m", "meter", "meters" -> Units.METER
        "km", "kilometer", "kilometers" -> Units.KILOMETER
        "cm", "centimeter", "centimeters" -> Units.CENTIMETER
        "mm", "millimeter", "millimeters" -> Units.MILLIMETER
        "mi", "mile", "miles" -> Units.MILE
        "yd", "yard", "yards" -> Units.YARD
        "ft", "foot", "feet" -> Units.FOOT
        "in", "inch", "inches" -> Units.INCH
        "g", "gram", "grams" -> Units.GRAM
        "kg", "kilogram", "kilograms" -> Units.KILOGRAM
        "mg", "milligram", "milligrams" -> Units.MILLIGRAM
        "lb", "pound", "pounds" -> Units.POUND
        "oz", "ounce", "ounces" -> Units.OUNCE
        "celsius", "dc", "c" -> Units.CELSIUS
        "fahrenheit", "df", "f" -> Units.FAHRENHEIT
        "kelvin", "kelvins", "k" -> Units.KELVIN
        else -> Units.NULL
    }
}

fun convertLength(value: Double, unit: Units, newUnit: Units): Double {
    val bufferValue = when (unit) {
        // converting user's value to intermediate units -- meters
        Units.METER -> value
        Units.KILOMETER -> 1000 * value
        Units.CENTIMETER -> 0.01 * value
        Units.MILLIMETER -> 0.001 * value
        Units.MILE -> 1609.35 * value
        Units.YARD -> 0.9144 * value
        Units.FOOT -> 0.3048 * value
        Units.INCH -> 0.0254 * value
        else -> 0.0
    }

    return when (newUnit) {
        // Converting intermediate value to the target-units
        Units.METER -> bufferValue
        Units.KILOMETER -> bufferValue / 1000
        Units.CENTIMETER -> bufferValue * 100
        Units.MILLIMETER -> bufferValue * 1000
        Units.MILE -> bufferValue / 1609.35
        Units.YARD -> bufferValue / 0.9144
        Units.FOOT -> bufferValue / 0.3048
        Units.INCH -> bufferValue / 0.0254
        else -> 0.0
    }
}

fun convertWeight(value: Double, unit: Units, newUnit: Units): Double {
    val bufferValue = when (unit) {
        // converting user's value to intermediate units -- grams
        Units.GRAM -> value
        Units.KILOGRAM -> 1000 * value
        Units.MILLIGRAM -> 0.001 * value
        Units.POUND -> 453.592 * value
        Units.OUNCE -> 28.3495 * value
        else -> 0.0
    }

    return when (newUnit) {
        // Converting intermediate value to the target-units
        Units.GRAM -> bufferValue
        Units.KILOGRAM -> bufferValue / 1000
        Units.MILLIGRAM -> bufferValue * 1000
        Units.POUND -> bufferValue / 453.592
        Units.OUNCE -> bufferValue / 28.3495
        else -> 0.0
    }
}

fun convertTemperature(value: Double, unit: Units, newUnit: Units): Double {
    val bufferValue = when (unit) {
        // converting user's value to intermediate units -- kelvin
        Units.KELVIN -> value
        Units.CELSIUS -> value + 273.15
        Units.FAHRENHEIT -> (value + 459.67) * 5.0 / 9.0
        else -> 0.0
    }

    return when (newUnit) {
        // Converting intermediate value to the target-units
        Units.KELVIN -> bufferValue
        Units.CELSIUS -> bufferValue - 273.15
        Units.FAHRENHEIT -> bufferValue * 1.8 - 459.67
        else -> 0.0
    }
}

fun main() {
    print("Enter what you want to convert (or exit): ")
    var inputLine: String = readLine()!!.toLowerCase()
    while (inputLine != "exit") {
        val parse: List<String> = inputLine.split(" ")
        var parameters: Array<String> = emptyArray<String>()
        for (item in parse){
            if (item != "degree" && item != "degrees") {
                parameters += item
            }
        }
        if (!(parameters.size == 4 && (parameters[0][0].isDigit() || parameters[0][0] == '-'))) {
            println("Parse error")
            println()
            print("Enter what you want to convert (or exit): ")
            inputLine = readLine()!!.toLowerCase()
        }
        else {
            val oldUnit = getUnit(parameters[1])
            val newUnit = getUnit(parameters[3])
            val oldValue = parse[0].toDouble()
            val oldMeasure = getMeasure(oldUnit)
            val newMeasure = getMeasure(newUnit)

            println(if (oldMeasure == newMeasure && oldMeasure != "???" && newMeasure != "???") {
                if (oldMeasure == "length") {
                    if (oldValue < 0) {
                        "Length shouldn't be negative"
                    }
                    else {
                        val newValue = convertLength(oldValue, oldUnit, newUnit)
                        "$oldValue ${if (oldValue == 1.0) oldUnit.single else oldUnit.plural}" +
                                " is $newValue ${if (newValue == 1.0) newUnit.single else newUnit.plural}"
                    }
                } else if (oldMeasure == "weight") {
                    if (oldValue < 0) {
                        "Weight shouldn't be negative"
                    }
                    else {
                        val newValue = convertWeight(oldValue, oldUnit, newUnit)
                        "$oldValue ${if (oldValue == 1.0) oldUnit.single else oldUnit.plural}" +
                                " is $newValue ${if (newValue == 1.0) newUnit.single else newUnit.plural}"
                    }
                } else {
                    val newValue = convertTemperature(oldValue, oldUnit, newUnit)
                    "$oldValue ${if (oldValue == 1.0) oldUnit.single else oldUnit.plural}" +
                            " is $newValue ${if (newValue == 1.0) newUnit.single else newUnit.plural}"
                }
            } else {
                "Conversion from ${oldUnit.plural} to ${newUnit.plural} is impossible"
            })
            println()
            print("Enter what you want to convert (or exit): ")
            inputLine = readLine()!!.toLowerCase()
        }
    }

}
