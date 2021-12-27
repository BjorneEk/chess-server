package server

import java.net.{ServerSocket, Socket}
import scala.collection.mutable.{ArrayBuffer, Map}

/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    This is a server class created for a multiplayer chess game
 *    The server simply listens for clients and connects them
 *    by adding them to [[ConnectedClients]]
 *
 *    @param serverSocket The socket on witch the server listens for clients
 *
 **/

case class Server(serverSocket : ServerSocket) extends Runnable:


/**
 *  [[gameManagers]]      buffer of all running chess games
 *  [[connectedClients]]  buffer of all clientManagers for clients in menu
 *  [[gameCreator]]       hashmap of all clientManagers for clients hosting a game
 **/
	val gameManagers     = ArrayBuffer.empty[GameManager]
	val connectedClients = ArrayBuffer.empty[ConnectedClient]
	val gameCreators     = Map.empty[String, GameCreator]

	var running = true

	val thread = Thread(this)
	thread.start()

	def run() : Unit =
		while running do
			connectedClients += ConnectedClient(serverSocket.accept())(using this)


object Server :

	val portRange = 1023 to 65535

	def apply(port : Int = 4000) : Server = new Server(new ServerSocket(port))
