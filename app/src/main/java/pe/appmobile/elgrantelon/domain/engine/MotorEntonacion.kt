package pe.appmobile.elgrantelon.domain.engine

import kotlin.math.sqrt

object MotorEntonacion {

    const val F0_MINIMO_HZ = 75f
    const val F0_MAXIMO_HZ = 500f
    private const val CORRELACION_MINIMA = 0.3f

    fun detectarF0(muestras: FloatArray, sampleRate: Int): Float? {
        val lagMinimo = (sampleRate / F0_MAXIMO_HZ).toInt().coerceAtLeast(1)
        val lagMaximo = (sampleRate / F0_MINIMO_HZ).toInt()
        if (muestras.size <= lagMaximo + 1) return null

        val correlaciones = FloatArray(lagMaximo - lagMinimo + 1)
        for (lag in lagMinimo..lagMaximo) {
            var productoCruzado = 0f
            var energiaBase = 0f
            var energiaDesplazada = 0f
            val limite = muestras.size - lag
            for (i in 0 until limite) {
                productoCruzado += muestras[i] * muestras[i + lag]
                energiaBase += muestras[i] * muestras[i]
                energiaDesplazada += muestras[i + lag] * muestras[i + lag]
            }
            val energiaCombinada = sqrt(energiaBase * energiaDesplazada)
            correlaciones[lag - lagMinimo] = if (energiaCombinada > 0f) productoCruzado / energiaCombinada else 0f
        }

        // La autocorrelacion es periodica: un multiplo entero del periodo real
        // (un armonico) puede alcanzar una correlacion tan alta como el periodo
        // fundamental, o mas, por redondeo a muestra entera. Por eso se toma el
        // PRIMER pico local que supera el umbral, empezando desde el lag mas chico
        // (la frecuencia mas aguda del rango permitido) — nunca el maximo global,
        // que confunde con frecuencia el primer submultiplo que se cruce.
        for (indice in correlaciones.indices) {
            val correlacion = correlaciones[indice]
            if (correlacion < CORRELACION_MINIMA) continue
            val esMayorOIgualQueAnterior = indice == 0 || correlacion >= correlaciones[indice - 1]
            val esMayorOIgualQueSiguiente = indice == correlaciones.lastIndex || correlacion >= correlaciones[indice + 1]
            if (esMayorOIgualQueAnterior && esMayorOIgualQueSiguiente) {
                return sampleRate.toFloat() / (indice + lagMinimo)
            }
        }
        return null
    }

    fun calcularContorno(ventanas: List<FloatArray>, sampleRate: Int): List<Float?> =
        ventanas.map { detectarF0(it, sampleRate) }
}
