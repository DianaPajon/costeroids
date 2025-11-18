extends Node2D

@export var lista = false;

func __fire():
	$navecita.__fire()
	
func __die():
	$navecita.__die()

func __set_id(id:String):
	$navecita.id = id;
func __is_ready():
	lista = $navecita.lista
	return lista;
	
func send_signal(id:String, s:String, para:Dictionary):
	get_parent().recv_signal(id, s, para)
