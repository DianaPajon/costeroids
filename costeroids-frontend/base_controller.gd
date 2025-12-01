extends Node2D

#Json
var json = JSON.new()

#User ID:
var userId;

#WebSocket
var rng = RandomNumberGenerator.new()
var username := rng.randi()
var websocket_url :=  dwad"ws://localhost:8080/ws/game/player"
var inputsocket := WebSocketPeer.new();
var inputopen = false;

#Escenas
var nave_scene = preload("res://nave.tscn")
var bullet_scene = preload("res://bullet.tscn")
var healthbarScene = preload("res://scene.tscn")

#Estado:
var objectMap = {}


#Barritas de vida:
var healthBars = {}


#aux
var every200 = 0;
var contador_jugadores = 1;

#Constantes
const SHIP_TYPE = "SHIP_TYPE"
const BULLET_TYPE = "BULLET_TYPE"
func _process(delta:float):
	if(!inputopen):
		connect_input_socket();
	else:
		inputsocket.poll()
		var response;
		if(inputsocket.get_ready_state() == WebSocketPeer.STATE_OPEN):
			while inputsocket.get_available_packet_count():
				response = inputsocket.get_packet().get_string_from_utf8();
				print_every_200(response)
				print_every_200(json.stringify(objectMap))
			if(response != null):
				var parsedState = json.parse(response);
				if(parsedState == OK):
					var updateState = json.data;
					for ship in updateState["ships"]:
						if(!objectMap.has(ship.id)): 
							objectMap[ship.id] = crear_nave(Vector2(ship.center.x, ship.center.y), ship.rotation, ship.id)
							healthBars[ship.id] = crear_health_bar(Vector2(10, 30*contador_jugadores), ship.id, "Tú"  if (ship.id == userId)  else str("enemigo ", contador_jugadores), ship.hp)
							contador_jugadores = contador_jugadores + 1;
						objectMap[ship.id]["position"] = Vector2(ship.center.x, ship.center.y)
						objectMap[ship.id]["rotation"] = ship.rotation
						healthBars[ship.id].health = ship.hp
						healthBars[ship.id].otro = ship.id == userId
					if(updateState.has("bullets")):
						for bullet in updateState["bullets"]:
							if(!objectMap.has(bullet.id)):
								objectMap[bullet.id] = crear_bullet(Vector2(bullet.center.x, bullet.center.y), bullet.rotation, bullet.playerId);
								objectMap[bullet.playerId]["instance"].__fire()
							objectMap[bullet.id].position = Vector2(bullet.center.x, bullet.center.y)
							objectMap[bullet.id].rotation = bullet.rotation
					if(updateState.has("deaths")):
						for ship in updateState["deaths"]:
							if(objectMap.has(ship)):
								objectMap[ship].instance.__die()
					var remove = [];
					for key in objectMap:
						if(objectMap[key].type == BULLET_TYPE):
							var has = false;
							if(updateState.has("bullets")):
								for bullet in updateState["bullets"] :
									if(bullet.id == key):
										has = true;
							if(!has && objectMap[key].creada):
								remove_child(objectMap[key].instance)
								objectMap[key].instance.queue_free()
								remove.push_back(key)
					for key in remove:
						objectMap.erase(key)
	actualizar()
	if(userId):
		if Input.is_action_pressed("acelerar"):
			var message = "{\"type\":\"KEY_TRUST_EVENT\",\"playerId\":\""+ userId +"\"}";
			print(message)
			inputsocket.send_text(message)
		if Input.is_action_pressed("frenar"):
			var message = "{\"type\":\"KEY_BRAKE_EVENT\",\"playerId\":\""+ userId +"\"}";
			print(message)
			inputsocket.send_text(message)
		if Input.is_action_pressed("derecha"):
			var message = "{\"type\":\"KEY_RIGHT_EVENT\",\"playerId\":\""+ userId +"\"}";
			print(message)
			inputsocket.send_text(message)
		if Input.is_action_pressed("izquierda"):
			var message = "{\"type\":\"KEY_LEFT_EVENT\",\"playerId\":\""+ userId +"\"}";
			print(message)
			inputsocket.send_text(message)
		if Input.is_action_pressed("disparar"):
			if(objectMap.has(userId) && objectMap[userId].instance.__is_ready()):
				var message = "{\"type\":\"FIRE_EVENT\",\"playerId\":\""+ userId +"\"}";
				print(message)
				inputsocket.send_text(message)
			
			
func actualizar() -> void:
	for key in objectMap:
		var object = objectMap[key]
		if(object.type == SHIP_TYPE):
			object.instance.position = object.position;
			object.instance.rotation = object.rotation;
		
		if(object.type == BULLET_TYPE):
			if(object.creada):
				object.instance.position = object.position;
			else:
				var shipId = object.playerId;
				if(objectMap[shipId].instance.__is_ready()):
					show_bullet(object);
					
				
func crear_nave(position: Vector2, rotation:float, id:String):
	var nave_instance = nave_scene.instantiate()
	nave_instance.__set_id(id)
	add_child(nave_instance)
	nave_instance.position = position
	nave_instance.rotation = rotation
	return {"instance":nave_instance, "creada":true, "playerId":id, "position":position, "rotation":rotation, "type":SHIP_TYPE}
	
func crear_bullet(position:Vector2, rotation: float, id:String) -> Dictionary:
	var bullet = {}
	bullet["creada"] = false;
	bullet["playerId"] = id;
	bullet["position"] = position;
	bullet["rotation"] = rotation;
	bullet["type"] = BULLET_TYPE
	return bullet;

func crear_health_bar(position:Vector2, id:String, playerName:String, hp:int):
	var bar_instance = healthbarScene.instantiate()
	bar_instance.position = position
	bar_instance.nombre = playerName;
	bar_instance.health = hp
	add_child(bar_instance)
	return bar_instance
	
func show_bullet(bullet:Dictionary):
	bullet.instance = bullet_scene.instantiate()
	bullet.instance.position = bullet.position;
	bullet.instance.rotation = rad_to_deg(bullet.rotation);
	add_child(bullet.instance)
	bullet.creada = true;
	
func connect_input_socket():
	if(!inputopen):
		inputsocket.connect_to_url(websocket_url)
		inputsocket.poll();
		var state = inputsocket.get_ready_state()
		if(state == WebSocketPeer.STATE_OPEN):
			while inputsocket.get_available_packet_count():
				var userIdpacket = inputsocket.get_packet().get_string_from_utf8()
				userId = userIdpacket;
				inputopen = true
			

func print_every_200(string:String):
	if(every200 >= 50):
		print(string)
		every200 = 0;
	else:
		every200 = every200 + 1

func recv_signal(id:String, s:String, para:Dictionary):
	if(s == "dead"):
		var ship = objectMap[id]
		remove_child(ship.instance)
		ship.instance.queue_free()
		objectMap.erase(id)
	if(s=="fire"):
		for key in objectMap:
			var object = objectMap[key]
			if(object.type == BULLET_TYPE && object.creada == false):
				show_bullet(object)
				
			
