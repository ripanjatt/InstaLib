fun main() {
    val instaSave = InstaSave()
    instaSave.start("https://www.instagram.com/p/CMynCWKjuIZ/")
    println("Running...")
    instaSave.setProcessListener(object : ProcessListener {
        override fun onCompleteListener(noOfFiles : Int) {
            println("Completed => Files: $noOfFiles")
            instaSave.getAllPhotos().forEach {
                println(it)
            }
            instaSave.getAllVideos().forEach {
                println(it)
            }
        }

        override fun onErrorListener(e: String) {
            println(e)
        }

    })
}
