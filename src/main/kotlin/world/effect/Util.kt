package world.effect

import world.RoomContent

class Util {
    companion object {
        fun getAssociatedWorldEffects(roomContent: RoomContent): ArrayList<WorldEffect> {
            return when(roomContent) {
                RoomContent.BLOCKADE -> arrayListOf(NoEffect())
                RoomContent.BREEZE -> arrayListOf(NoEffect())
                RoomContent.BUMP -> arrayListOf(NoEffect())
                RoomContent.FOOD -> arrayListOf(NoEffect())
                RoomContent.GLITTER -> arrayListOf(NoEffect())
                RoomContent.GOLD -> arrayListOf(AddHereEffect(RoomContent.GLITTER))
                RoomContent.MOO -> arrayListOf(NoEffect())
                RoomContent.PIT -> arrayListOf(AddAdjacentEffect(RoomContent.BREEZE))
                RoomContent.STENCH -> arrayListOf(NoEffect())
                RoomContent.SUPMUW_EVIL -> arrayListOf(AddAdjacentEffect(RoomContent.MOO),
                        AddDiagonalEffect(RoomContent.MOO))
                RoomContent.SUPMUW -> arrayListOf(AddAdjacentEffect(RoomContent.MOO),
                        AddDiagonalEffect(RoomContent.MOO), AddHereEffect(RoomContent.FOOD))
                RoomContent.WUMPUS -> arrayListOf(AddAdjacentEffect(RoomContent.STENCH))
            }
        }
    }
}