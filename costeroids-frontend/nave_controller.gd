extends AnimatedSprite2D

@export var firing = false
@export var dying = false
@export var lista = true
@export var dead = false
@export var id = ""

func _ready() -> void:
	modulate = Color(1.5, 1.5, 1.5)


func _process(delta:float):
	if(firing):
		print("firing 1")
		if(lista):
			print("firing 2")
			play("fire")
			lista = false
	if(dying):
		if(lista):
			play("die")
			lista = false

func __fire():
	firing = true

func __die():
	dying = true
	
	
	
	
	
func __desanimar():
	if(firing):
		stop();
		lista = true;
		dying = false;
		firing = false;
		get_parent().send_signal(id, "fire", {})
		play("start")
	if(dying):
		stop();
		lista = true;
		dying = false;
		firing = false;
		get_parent().send_signal(id, "dead", {})


func _on_animation_finished() -> void:
	__desanimar()
	pass # Replace with function body.


func _on_animation_looped() -> void:
	__desanimar()
	pass # Replace with function body.
