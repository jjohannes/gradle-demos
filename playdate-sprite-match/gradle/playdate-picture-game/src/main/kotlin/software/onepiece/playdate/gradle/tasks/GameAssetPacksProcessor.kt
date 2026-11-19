package software.onepiece.playdate.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

abstract class GameAssetPacksProcessor : DefaultTask() {

    @get:InputFiles
    abstract val assetPack: ConfigurableFileCollection

    @get:Input
    abstract val spriteSize: Property<Int>

    @get:Input
    abstract val spriteSpacing: Property<Int>

    @get:OutputDirectory
    abstract val assetFolder: DirectoryProperty

    @TaskAction
    fun runInstall() {
        assetFolder.get().asFile.listFiles()?.forEach { it.deleteRecursively() }

        fixImageTable()
    }

    private fun fixImageTable() {
        val totalSize = spriteSize.get() + spriteSpacing.get()
        val image = ImageIO.read(assetPack.singleFile)
        val columns = image.width / totalSize
        val rows = image.height / totalSize

        val imageWithoutSpacing = BufferedImage(columns * spriteSize.get(), rows * spriteSize.get(), image.type)
        val graphics = imageWithoutSpacing.createGraphics()
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, imageWithoutSpacing.width, imageWithoutSpacing.height) // replace transparency with white

        for (y in 0 until rows) {
            for (x in 0 until columns) {
                val toX = spriteSize.get() * x
                val toY = spriteSize.get() * y
                val toX2 = toX + spriteSize.get()
                val toY2 = toY + spriteSize.get()

                val fromX = totalSize * x
                val fromY = totalSize * y
                val fromX2 = fromX + spriteSize.get()
                val fromY2 = fromY + spriteSize.get()

                graphics.drawImage(image,
                    toX, toY, toX2, toY2,
                    fromX, fromY, fromX2, fromY2, null
                )
            }
        }

        ImageIO.write(imageWithoutSpacing, "png",
            assetFolder.file("pictures-table-${spriteSize.get()}-${spriteSize.get()}.png").get().asFile)
    }
}