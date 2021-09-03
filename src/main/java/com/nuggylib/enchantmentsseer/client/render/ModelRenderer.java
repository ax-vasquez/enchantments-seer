package com.nuggylib.enchantmentsseer.client.render;

import com.nuggylib.enchantmentsseer.client.render.data.RenderData;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer.Model3D;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/render/ModelRenderer.java"
 */
public final class ModelRenderer {

    private ModelRenderer() {
    }

    private static final int BLOCK_STAGES = 1_000;

    private static final Map<RenderData, Int2ObjectMap<Model3D>> cachedCenterData = new Object2ObjectOpenHashMap<>();

    public static Model3D getModel(RenderData data, double scale) {
        int maxStages = data.height * BLOCK_STAGES;
        int stage;
        stage = Math.min(maxStages, (int) (scale * maxStages));
        Int2ObjectMap<Model3D> cachedCenter;
        if (cachedCenterData.containsKey(data)) {
            cachedCenter = cachedCenterData.get(data);
            if (cachedCenter.containsKey(stage)) {
                return cachedCenter.get(stage);
            }
        } else {
            cachedCenterData.put(data, cachedCenter = new Int2ObjectOpenHashMap<>());
        }
        if (maxStages == 0) {
            maxStages = stage = 1;
        }

        Model3D model = new Model3D();
        model.setTexture(data.getTexture());

        cachedCenter.put(stage, model);
        model.minX = 0.01F;
        model.minY = 0.01F;
        model.minZ = 0.01F;

        model.maxX = data.length - 0.02F;
        model.maxY = data.height * (stage / (float) maxStages) - 0.02F;
        model.maxZ = data.width - 0.02F;
        return model;
    }

    //Undoes the z-fighting height shift from the model
    public static float getActualHeight(Model3D model) {
        return model.maxY + 0.02F;
    }

    public static void resetCachedModels() {
        cachedCenterData.clear();
    }
}
