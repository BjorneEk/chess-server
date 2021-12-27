package server

import java.net.Socket

/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    a ClientManager for clients that in the launcer and does not yet host a game
 *
 *    @param socket @see [[ClientManager]]
 *    @param ctx used to acces the server to join a game, get the gameList or create a game
 **/

case class ConnectedClient(socket : Socket)(using ctx : Server) extends ClientManager(socket):
	println(s"Client connected: ${socket.toString}")
	import ClientManager.Actions._
	thread.start()


	/**
	 *   def joinGame
	 *   used to join a game via [[ctx.gameCreators]]
	 *
	 *   @param game string as sent by client in format:
	 *   "{name,isPrivate,pwd}" ex "{newGame,true,password123}"
	 **/
	private def joinGame(game : String) : Unit =
		var gm = game
		val name = game.substring(game.indexOf("{")+1, game.indexOf(","))
		gm = gm.substring(game.indexOf(",")+1, gm.length)
		val pwdProtected = gm.substring(0, 4) == "true"
		var pwd = ""
		if pwdProtected then
			pwd = gm.substring(gm.indexOf(",")+1, gm.indexOf("}"))

		if ctx.gameCreators(name).isPwdProtected then
			if ctx.gameCreators(name).password == pwd then
				ctx.gameCreators(name).join(socket)
			else return
		else ctx.gameCreators(name).join(socket)
		thread.interrupt()

	/**
	 *   def createGame
	 *   used to create a GameCreator and add it to [[ctx.gameCreators]]
	 *
	 *   @param game string as sent by client in format:
	 *   "{name,isPrivate,pwd}" ex "{newGame,true,password123}"
	 **/
	private def createGame(game : String) : Unit =
		var gm = game
		val name = game.substring(game.indexOf("{")+1, game.indexOf(","))
		gm = gm.substring(game.indexOf(",")+1, gm.length)
		val pwdProtected = gm.substring(0, 4) == "true"
		var pwd = ""
		if pwdProtected then //"true,superhemlig}"
			pwd = gm.substring(gm.indexOf(",")+1, gm.indexOf("}"))

		ctx.gameCreators += (name -> GameCreator(name, pwdProtected, socket, pwd))
		connected = false
		ctx.connectedClients -= this
		thread.interrupt()

	/**
	 *   def gameList
	 *   used to generate a list of al games in [[ctx.gameCreators]] in string format
	 *   in order to suply clients with the gameList
	 **/
	private def gameList() : String =
		var res = "gamelist:"
		for (k, v) <- ctx.gameCreators do res += ("|"+v.toString)
		if res.length <= 9 then "empty" else res

	def run() : Unit =
		while connected do
			dIn.readByte() match
				case CREATE  => println("create"); createGame(dIn.readUTF())
				case JOIN    => println("join"); joinGame(dIn.readUTF())
				case REQUEST => send(gameList())
				case EXIT    => println("exit")
