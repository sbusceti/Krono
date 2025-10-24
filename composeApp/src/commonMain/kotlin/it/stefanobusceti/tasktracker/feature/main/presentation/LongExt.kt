package it.stefanobusceti.tasktracker.feature.main.presentation

object LongExt {

    fun formatElapsedTime(elapsedMillis: Long): String {
        val totalSeconds = elapsedMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return "${hours.toTwoDigitString()}:${minutes.toTwoDigitString()}:${seconds.toTwoDigitString()}"
    }

    private fun Long.toTwoDigitString(): String {
        return if (this < 10) "0$this" else this.toString()
    }

}