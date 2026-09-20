package com.valverdeball.vibe

import java.io.DataOutputStream
import java.io.InputStream
import java.net.Socket

class LspClient {

    fun initializeSpike(): String {
        val socket = Socket("127.0.0.1", 9257)
        socket.soTimeout = 5000
        val out = DataOutputStream(socket.outputStream)
        val input = socket.inputStream

        val body = """{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"processId":null,"rootUri":null,"capabilities":{}}}"""
        val bytes = body.toByteArray(Charsets.UTF_8)

        val header = "Content-Length: ${bytes.size}\r\n\r\n"
        out.write(header.toByteArray(Charsets.UTF_8))
        out.write(bytes)
        out.flush()

        val response = readLspMessage(input)
        socket.close()
        return response
    }

    private fun readLspMessage(input: InputStream): String {
        var contentLength = -1
        while (true) {
            val line = readOneLine(input)
            if (line.isEmpty()) break
            if (line.startsWith("Content-Length:")) {
                contentLength = line.substringAfter(":").trim().toInt()
            }
        }
        require(contentLength >= 0) { "No Content-Length header found" }

        val body = ByteArray(contentLength)
        var read = 0
        while (read < contentLength) {
            val n = input.read(body, read, contentLength - read)
            if (n == -1) break
            read += n
        }
        return String(body, Charsets.UTF_8)
    }

    private fun readOneLine(input: InputStream): String {
        val sb = StringBuilder()
        var prev = -1
        while (true) {
            val c = input.read()
            if (c == -1) break
            if (prev == '\r'.code && c == '\n'.code) {
                sb.setLength(sb.length - 1)
                break
            }
            sb.append(c.toChar())
            prev = c
        }
        return sb.toString()
    }
}
