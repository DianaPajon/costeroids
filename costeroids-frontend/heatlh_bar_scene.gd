extends Node2D


@export var health = 10;
@export var nombre = "jugador 1";
@export var otro = false;


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	$ColorRect.size.y = 20
	$ColorRect.size.x = 15*health
	$ColorRect.color = _getColor()
	$Label.text = nombre
	$Label.add_theme_color_override("font_color",_getColor())
	pass

func _getColor() -> Color:
	if(otro):
		return Color.SALMON;
	else:
		return Color.LIGHT_STEEL_BLUE;
