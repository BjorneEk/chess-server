package server

import java.net.Socket
import java.io.{
	BufferedWriter     => Writer,
	BufferedReader     => Reader,
	InputStreamReader  => ISReader,
	OutputStreamWriter => OSWriter,
	DataInputStream,
	DataOutputStream
}

/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    subclasses ase used by server to manage connected clients
 *    @param socket The socket of the clients
 *
 **/

trait ClientManager(socket : Socket) extends Runnable:
	socket.setKeepAlive(true)

	/**
	 *  [[writer]]  BufferedWriter used to write to outputstream of @param socket
	 *  [[reader]]  BufferedReader used to read from inputstream of @param socket
	 **/

	val writer    : Writer  = Writer(OSWriter(socket.getOutputStream()))
	val reader    : Reader  = Reader(ISReader(socket.getInputStream()))

	val dIn  = DataInputStream(socket.getInputStream())
	val dOut = DataOutputStream(socket.getOutputStream())

	var connected : Boolean = true

	val thread    : Thread  = Thread(this)
	var connectionTimeout : Long = System.currentTimeMillis()

	/**
	 *  send a string to the client at @param socket
	 *  @param msg the string to send
	 **/

	def send(msg : String) : Unit =
		writer.write(msg)
		writer.newLine()
		writer.flush()

	def run() : Unit

object ClientManager:

	/**
	 *  used to send a string to a socket
	 *
	 *  used by [[GameCreator]] to send the color to the joining player
	 *  @param socket the socket to send to
	 *  @param msg the string to send
	 **/

	def send(msg : String, socket : Socket) : Unit =
		val writer : Writer = Writer(OSWriter(socket.getOutputStream()))
		writer.write(msg)
		writer.newLine()
		writer.flush()

	object Actions:

		val CREATE  : Int = 1
		val JOIN    : Int = 2
		val REQUEST : Int = 3
		val EXIT    : Int = 4
