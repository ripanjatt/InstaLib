interface ProcessListener {
    /*
     * Listener!
     */
    fun onCompleteListener(noOfFiles: Int)
    fun onErrorListener(e: String)
}