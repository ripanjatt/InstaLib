import java.io.DataInputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class InstaSave {

    /*
     * A simple library for downloading instagram photos, videos and reels!
     * Compatible with both java and kotlin!
     * You can freely use this library in your personal project!
     * But we would appreciate a little mention of our work!
     */

    private var photoLinks = ArrayList<String>()
    private var videoLinks = ArrayList<String>()
    private var extract = true

    private var html = ""

    /*
     * Listener for task completion and errors!
     */
    private var processListener: ProcessListener? = null

    fun setProcessListener(processListener: ProcessListener) {
        this.processListener = processListener
    }

    fun start(link: String) {
        thread {
            /*
             * Thread to keep the heavy work away from UI thread (Perfect for Android)!
             */
            try {
                val url = URL(link)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 10000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"
                connection.connect()
                val inStream = DataInputStream(connection.inputStream)
                val data = ByteArray(1024)
                /*
                 * Reading all the html from the link!
                 */
                while (inStream.read(data, 0, data.size) != -1) {
                    html += String(data)
                }
                html = html.replace(" ", "")
                html = html.replace("\n", "")
                getLinks()
            } catch (e : Exception) {
                processListener?.onErrorListener("Error: $e")
            }
        }
    }

    fun startWithHtml(text: String) {
        thread {
            html = text
            html = html.replace(" ", "")
            html = html.replace("\n", "")
            getLinks()
        }
    }

    private fun getLinks() {
        /*
         * Extracts all the links!
         */
        while (extract) {
            if (html.contains("\"display_url\":\"")) {
                /*
                 * Search for photos' links!
                 */
                getPhotos()
                if(photoLinks.size > 10) {
                    val temp = photoLinks[0]
                    if (!temp.contains("\"")) {
                        photoLinks = arrayListOf(temp)
                    }
                    getVideos()
                    extract = false
                }
            } else {
                /*
                 * If no more photos are available on the page, search for videos' links!
                 */
                if (html.contains("og:video:secure_url")) {
                    getVideos()
                } else {
                    /*
                     * When all the links are extracted, stop the process and notify!
                     */
                    extract = false
                }
            }
        }
        processListener?.onCompleteListener(photoLinks.size + videoLinks.size)
    }

    private fun getPhotos() {
        /*
         * This method extracts the first single photo link it encounters and remove it from the rest of the html!
         */
        val temp = html.split("\"display_url\":\"")[1].split("\",\"display_resources")
        var link = temp[0]
        html = html.replace("\"display_url\":\"$link\",\"display_resources", "")
        link = link.replace("\\u0026", "&")
        photoLinks.add(link)
    }

    private fun getVideos() {
        /*
         * This method extracts the first single video link it encounters and remove it from the rest of the html!
         */
        val temp = html.split("og:video:secure_url")[1].split("/>")
        val link = temp[0].split("content=\"")[1].split("\"")[0]
        html = html.replace("og:video:secure_url", "")
        html = html.replace(link, "")
        videoLinks.add(link)
    }

    fun getAllPhotos() : ArrayList<String> {
        /*
         * Utility method for users to get all the photos' links!
         */
        return photoLinks
    }

    fun getAllVideos() : ArrayList<String> {
        /*
         * Utility method for users to get all the videos' links!
         */
        return videoLinks
    }
}