package com.onenigma.pokemonapp

enum class PokemonTypeColor(val colorRes: Int) {
    Grass(R.color.type_grass),
    Fire(R.color.type_fire),
    Water(R.color.type_water),
    Normal(R.color.type_normal),
    Flying(R.color.type_flying),
    Bug(R.color.type_bug),
    Poison(R.color.type_poison),
    Electric(R.color.type_electric),
    Ground(R.color.type_ground),
    Fighting(R.color.type_fighting),
    Psychic(R.color.type_psychic),
    Rock(R.color.type_rock),
    Ice(R.color.type_ice),
    Ghost(R.color.type_ghost),
    Dragon(R.color.type_dragon),
    Dark(R.color.type_dark),
    Steel(R.color.type_steel),
    Fairy(R.color.type_fairy);
}

fun PokemonType.asTypeColor(): PokemonTypeColor? = PokemonTypeColor.values()
    .firstOrNull { it.name.equals(this.name, true) }