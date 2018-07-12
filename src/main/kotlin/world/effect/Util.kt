package world.effect

import world.RoomContent

class Util {
    companion object {
        fun getAssociatedWorldEffects(roomContent: RoomContent): ArrayList<WorldEffect> {
            val worldEffects: ArrayList<WorldEffect>
            when(roomContent) {
                RoomContent.BLOCKADE -> worldEffects = arrayListOf()
                RoomContent.BREEZE -> worldEffects = arrayListOf()
                RoomContent.BUMP -> worldEffects = arrayListOf()
                RoomContent.FOOD -> worldEffects = arrayListOf()
                RoomContent.GLITTER -> worldEffects = arrayListOf()
                RoomContent.GOLD -> worldEffects = arrayListOf()
                RoomContent.MOO -> worldEffects = arrayListOf()
                RoomContent.PIT -> worldEffects = arrayListOf()
                RoomContent.STENCH -> worldEffects = arrayListOf()
                RoomContent.SUPMUW_EVIL -> worldEffects = arrayListOf()
                RoomContent.SUPMUW -> worldEffects = arrayListOf()
                RoomContent.WUMPUS -> worldEffects = arrayListOf()
            }
            return worldEffects
        }
    }
}