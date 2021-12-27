package server

import java.net.Socket
import java.io.{
	BufferedWriter     => Writer,
	OutputStreamWriter => OSWriter
}
import scala.collection.immutable.Map
import Utils.Color.*
import Utils.Color

/**
 *    @author Gustaf Franzén :: https://github.com/BjorneEk;
 *
 *    A game manager class used to manage a running game of chess using two GameClients
 *    @param whiteSocket The socket of the white player
 *    @param blackSocket The socket of the black player
 **/

class GameManager(
	whiteSocket : Socket,
	blackSocket : Socket
) extends Runnable:

	println("a Game has started")
	/**
	 *  [[gameClients]]  HashMap of two [[(Utils.Color -> GameClient)]] (k -> v) pairs
	 *  used by each of the clients to reference the other one using the
	 *  [[Utils.otherColor(color : Utils.Color)]] function
	 **/

	val gameClients = Map[Color, GameClient](
		White -> GameClient(White, whiteSocket)(using this),
		Black -> GameClient(Black, blackSocket)(using this)
	)
	val thread = Thread(this)
	var running = true
	thread.start()

	def run() : Unit =
		while running do ()
			//for (k, v) <- gameClients do ()
				//running = v.connected
