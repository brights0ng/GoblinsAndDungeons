package superlord.goblinsanddungeons.common.item;

import net.minecraft.world.item.Item;

public class ScrollItem extends Item {

    public enum SpellType {
        NO_SPELL(0, "no_spell"),
        SOUL_BULLET(1, "soul_bullet"),
        SOUL_JUMP(2, "soul_jump");

        private final int id;
        private final String name;

        SpellType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return this.name;
        }
    }

    private final SpellType type;

    public ScrollItem(SpellType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    public SpellType getSpellType() {
        return this.type;
    }
}
