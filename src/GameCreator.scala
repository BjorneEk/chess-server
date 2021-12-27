package server

import java.net.Socket

/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    a ClientManager for clients that are hosting a game on the server
 *
 *    @param name the name of the game
 *    @param isPwdProtected whether or not the game is private
 *    @param socket @see [[ClientManager]]
 *    @param password the password of the game, defaults to empty string
 *    @param ctx used to acces the server to create a new game
 **/

case class GameCreator(
	name           : String,
	isPwdProtected : Boolean,
	socket         : Socket,
	password       : String = ""
)(using ctx : Server) extends ClientManager(socket):
	println(s"game Created: $name, $isPwdProtected, $password")
	thread.start()

	/**
	 *   def join
	 *   used by ClientManagers in [[Server.connectedClients]] to join a gameCreator
	 *   and start a game.
	 *
	 *   randomizes the color of the players and sends that information to
	 *   their respective socket, addw a new GameManager to [[ctx.gameManagers]]
	 *   and removes itself from the [[ctx.gameCreators]] map
	 *   @param _socket socket or the other player
	 **/
	def join(_socket : Socket) : Unit =
		if Math.random() > 0.5 then
			ctx.gameManagers += GameManager(socket, _socket)
			send("color:white")
			ClientManager.send("color:black",_socket)
		else
			ctx.gameManagers += GameManager(_socket, socket)
			send("color:black")
			ClientManager.send("color:white",_socket)
		connected = false
		ctx.gameCreators -= name
		thread.interrupt()

	def run() : Unit =
		while connected do
			var msg = reader.readLine()
			while msg == null do
				msg = reader.readLine()
			if msg == "exit" then connected = false

	override def toString : String = s"{$name,$isPwdProtected}"

object GameCreator:

	def apply(
		name           : String,
		isPwdProtected : Boolean,
		socket         : Socket,
		password       : String = ""
	)(using ctx : Server) : GameCreator =
		new GameCreator(name, isPwdProtected, socket, password)
