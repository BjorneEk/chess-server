package server

import java.net.Socket
import java.io.{
	BufferedWriter     => Writer,
	BufferedReader     => Reader,
	InputStreamReader  => ISReader,
	OutputStreamWriter => OSWriter
}


/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    a class with two separate sockets for input and output to enable comunication
 *    if a socket is for example read from in a concurrent thread
 *    @param socket The socket of the clients
 *
 **/

case class SocketPair(inSocket : Socket, outSocket : Socket):

	/**
	 *  [[writer]]  BufferedWriter used to write to outputstream of @param outSocket
	 *  [[reader]]  BufferedReader used to read from inputstream of @param inSocket
	 **/

	val writer : Writer  = Writer(OSWriter(outSocket.getOutputStream()))
	val reader : Reader  = Reader(ISReader(inSocket.getInputStream()))

	/**
	 *  send a string to the client at @param outSocket
	 *  @param msg the string to send
	 **/

	def send(msg : String) : Unit =
		writer.write(msg)
		writer.newLine()
		writer.flush()
