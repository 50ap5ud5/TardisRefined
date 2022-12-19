package whocraft.tardis_refined.common.dimension;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.common.util.Platform;

import java.util.ArrayList;

public class DimensionData extends SavedData {

    public static DimensionData create() {
        return new DimensionData();
    }

    public static DimensionData getData() {
        return Platform.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(DimensionData::load, DimensionData::create, "tardis_refined");
    }
    public static DimensionData load(CompoundTag tag) {
        DimensionData data = DimensionData.create();
        return data;
    }

    public static ArrayList<ResourceKey<Level>> retrieve(CompoundTag tag) {

        if (!tag.contains("keychain")) {
            return new ArrayList<>();
        }

        ListTag keychain = tag.getList("keychain", Tag.TAG_STRING);

        ArrayList<ResourceKey<Level>> levels = new ArrayList<>();

        for (Tag dim : keychain) {
            String string = dim.getAsString();
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(string));
            levels.add(key);
        }

        // Return the list of ResourceKeys
        return levels;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        for (ResourceKey<Level> level : DimensionHandler.LEVELS) {
            listTag.add(StringTag.valueOf(level.location().toString()));
        }
        compoundTag.put("dimension_data", listTag);
        return compoundTag;
    }
}
