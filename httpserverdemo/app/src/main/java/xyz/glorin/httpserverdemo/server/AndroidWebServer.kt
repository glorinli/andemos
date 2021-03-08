package xyz.glorin.httpserverdemo.server

import fi.iki.elonen.NanoHTTPD

class AndroidWebServer : NanoHTTPD(8478) {
    override fun serve(session: IHTTPSession?): Response {
        return newFixedLengthResponse(INDEX)
    }

    companion object {
        private const val INDEX = """
            <html><header><title>Good</title></header><body>I am a server!</body></html>
        """
    }
}