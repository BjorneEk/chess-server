package server



object Main:

	def main(args: Array[String]): Unit =
		println("Starting server on port 4000")
		val server = Server()
		println("Server has been started on port 4000")
