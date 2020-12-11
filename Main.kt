class Play {

    companion object {
        private const val dimensionWidth: Int  = 130
        private const val dimensionHeight: Int = 32

        private fun formatOutput(value: Double): String {
            return String.format("%.3f", value)
        }

        fun driver() {
            val world = World(dimensionWidth, dimensionHeight)

            var totalTickTime = 0.0
            var totalRenderTime = 0.0

            while(true)  {
                val tickT0 = System.currentTimeMillis()
                world.checkWorld()
                val tickT1 = System.currentTimeMillis()
                val tickTime = (tickT1 - tickT0) / 1.0
                totalTickTime += tickTime
                val avgTickTime = (totalTickTime / world.refresh)

                val renderT0 = System.currentTimeMillis()
                val rendered = world.render()
                val renderT1 = System.currentTimeMillis()
                val renderTime = (renderT1 - renderT0) / 1.0
                totalRenderTime += renderTime
                val avgRenderTime = (totalRenderTime / world.refresh)

                var output = "Generation #${world.refresh}"
                output += " - World refresh time (${formatOutput(avgTickTime)})"
                output += " - Rendering time (${formatOutput(avgRenderTime)})"
                output += "\n$rendered"
                print("\u001b[H\u001b[2J")
                println(output)
            }
        }
    }
}

fun main() {
    Play.driver()
}