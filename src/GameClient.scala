package server

import java.net.Socket
import Utils.Color.*
import Utils.Color

/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    a ClientManager for clients that are in a game of chess
 *
 *    @param color the color of the player @see [[Utils.Color]]
 *    @param socket @see [[ClientManager]]
 *    @param ctx used to access the other player
 **/

case class GameClient(
	color  : Color,
	socket : Socket
)(using ctx : GameManager) extends ClientManager(socket):
	var ready = true
	thread.start()

	/**
	 *   send a string to the other player via the GameManager
	 *   @param msg the string to send
	 **/

	def sendToOther(msg : String) : Unit =
		ctx.gameClients(Utils.otherColor(color)).send(msg)

	def run() : Unit =
		while connected do
			var msg = reader.readLine()
			while msg == null do
				msg = reader.readLine()
			if !msg.contains("move") then println("noMove" + msg) else sendToOther(msg)
			println("message recived :: " + msg)
		println("notListening" + color.toString)
