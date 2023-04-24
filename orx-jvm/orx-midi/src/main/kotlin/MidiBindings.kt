package org.openrndr.extra.midi

import kotlinx.coroutines.yield
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.parameters.DoubleParameter
import org.openrndr.extra.parameters.Vector2Parameter
import org.openrndr.extra.parameters.Vector3Parameter
import org.openrndr.launch
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.map

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.findAnnotations
@JvmName("bindMidiControlDouble")
fun Program.bindMidiControl(property: KMutableProperty0<Double>, transceiver: MidiTransceiver, channel: Int, control: Int) {
    val anno = property.findAnnotations(DoubleParameter::class).firstOrNull()

    val low = anno?.low ?: 0.0
    val high = anno?.high ?: 1.0
    transceiver.controlChanged.listen {
        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channel && it.control == control) {
            val value = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
            property.set(value)
        }
    }
    launch {
        var propertyValue = property.get()
        while(true) {
            val candidateValue = property.get()
            if (candidateValue != propertyValue) {
                propertyValue = candidateValue
                val value = propertyValue.map(low, high, 0.0, 127.0, clamp = true).toInt()
                transceiver.controlChange(channel, control, value)
            }
            yield()
        }
    }
}

@JvmName("bindMidiControlBoolean")
fun Program.bindMidiControl(property: KMutableProperty0<Boolean>, transceiver: MidiTransceiver, channel: Int, control: Int) {
    transceiver.controlChanged.listen {
        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channel && it.control == control) {
            property.set(it.value >= 64)
        }
    }
    launch {
        var propertyValue = property.get()
        while(true) {
            val candidateValue = property.get()
            if (candidateValue != propertyValue) {
                propertyValue = candidateValue
                transceiver.controlChange(channel, control, if (propertyValue) 127 else 0)
            }
            yield()
        }
    }
}


@JvmName("bindMidiControlVector2")
fun Program.bindMidiControl(property: KMutableProperty0<Vector2>, transceiver: MidiTransceiver,
                            channelX: Int, controlX: Int,
                            channelY: Int = channelX, controlY: Int = controlX + 1) {
    val anno = property.findAnnotations(Vector2Parameter::class).firstOrNull()

    val low = anno?.min ?: 0.0
    val high = anno?.max ?: 1.0
    transceiver.controlChanged.listen {
        val v = property.get()
        var x = v.x
        var y = v.y
        var changed = false

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelX && it.control == controlX) {
            changed = true
            x = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelY && it.control == controlY) {
            changed = true
            y = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (changed) {
            val nv = Vector2(x, y)
            property.set(nv)
        }
    }
    launch {
        var propertyValue = property.get()
        while(true) {
            val candidateValue = property.get()
            if (candidateValue != propertyValue) {
                propertyValue = candidateValue
                val valueX = propertyValue.x.map(low, high, 0.0, 127.0, clamp = true).toInt()
                val valueY = propertyValue.y.map(low, high, 0.0, 127.0, clamp = true).toInt()
                transceiver.controlChange(channelX, controlX, valueX)
                transceiver.controlChange(channelY, controlY, valueY)
            }
            yield()
        }
    }
}

@JvmName("bindMidiControlVector3")
fun Program.bindMidiControl(property: KMutableProperty0<Vector3>, transceiver: MidiTransceiver,
                            channelX: Int, controlX: Int,
                            channelY: Int = channelX, controlY: Int = controlX + 1,
                            channelZ: Int = channelY, controlZ: Int = controlY + 1) {
    val anno = property.findAnnotations(Vector3Parameter::class).firstOrNull()

    val low = anno?.min ?: 0.0
    val high = anno?.max ?: 1.0
    transceiver.controlChanged.listen {
        val v = property.get()
        var x = v.x
        var y = v.y
        var z = v.z
        var changed = false

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelX && it.control == controlX) {
            changed = true
            x = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelY && it.control == controlY) {
            changed = true
            y = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelZ && it.control == controlZ) {
            changed = true
            z = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (changed) {
            val nv = Vector3(x, y, z)
            property.set(nv)
        }
    }
    launch {
        var propertyValue = property.get()
        while(true) {
            val candidateValue = property.get()
            if (candidateValue != propertyValue) {
                propertyValue = candidateValue
                val valueX = propertyValue.x.map(low, high, 0.0, 127.0, clamp = true).toInt()
                val valueY = propertyValue.y.map(low, high, 0.0, 127.0, clamp = true).toInt()
                val valueZ = propertyValue.z.map(low, high, 0.0, 127.0, clamp = true).toInt()
                transceiver.controlChange(channelX, controlX, valueX)
                transceiver.controlChange(channelY, controlY, valueY)
                transceiver.controlChange(channelZ, controlZ, valueZ)
            }
            yield()
        }
    }
}

@JvmName("bindMidiControlColorRGBa")
fun Program.bindMidiControl(property: KMutableProperty0<ColorRGBa>, transceiver: MidiTransceiver,
                            channelR: Int, controlR: Int,
                            channelG: Int = channelR, controlG: Int = controlR + 1,
                            channelB: Int = channelG, controlB: Int = controlG + 1,
                            channelA: Int = channelB, controlA: Int = controlB + 1,
                            ) {
    val low = 0.0
    val high = 1.0
    transceiver.controlChanged.listen {
        val v = property.get()
        var r = v.r
        var g = v.g
        var b = v.b
        var a = v.alpha
        var changed = false

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelR && it.control == controlR) {
            changed = true
            r = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelG && it.control == controlG) {
            changed = true
            g = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelB && it.control == controlB) {
            changed = true
            b = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (it.eventType == MidiEventType.CONTROL_CHANGED && it.channel == channelA && it.control == controlA) {
            changed = true
            a = it.value.toDouble().map(0.0, 127.0, low, high, clamp = true)
        }

        if (changed) {
            val nv = ColorRGBa(r, g, b, a)
            property.set(nv)
        }
    }
    launch {
        var propertyValue = property.get()
        while(true) {
            val candidateValue = property.get()
            if (candidateValue != propertyValue) {
                propertyValue = candidateValue
                val valueR = propertyValue.r.map(low, high, 0.0, 127.0, clamp = true).toInt()
                val valueG = propertyValue.g.map(low, high, 0.0, 127.0, clamp = true).toInt()
                val valueB = propertyValue.b.map(low, high, 0.0, 127.0, clamp = true).toInt()
                val valueA = propertyValue.alpha.map(low, high, 0.0, 127.0, clamp = true).toInt()
                transceiver.controlChange(channelR, controlR, valueR)
                transceiver.controlChange(channelG, controlG, valueG)
                transceiver.controlChange(channelB, controlB, valueB)
                transceiver.controlChange(channelA, controlA, valueA)
            }
            yield()
        }
    }
}