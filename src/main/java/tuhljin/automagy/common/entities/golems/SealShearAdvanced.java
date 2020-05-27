package tuhljin.automagy.common.entities.golems;

import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.golems.parts.GolemArm;

public class SealShearAdvanced extends SealShear {
    public SealShearAdvanced() {
        super("shear_advanced", new SealToggle[]{new SealToggle(true, "shearCreature", "Automagy.golem.shearCreature"), new SealToggle(false, "shearBlock", "Automagy.golem.shearBlock"), new SealToggle(true, "saveShears", "Automagy.golem.saveShears"), new SealToggle(false, "ppro", "golem.prop.provision.wl")});
    }

    protected boolean canGolemShearWithoutItem(IGolemAPI golem) {
        IGolemProperties prop = golem.getProperties();
        GolemArm arm = prop.getArms();
        if (arm != null && "CLAWS".equals(arm.key)) {
            return !prop.hasTrait(EnumGolemTrait.CLUMSY) || prop.hasTrait(EnumGolemTrait.SMART);
        } else {
            return false;
        }
    }

    public EnumGolemTrait[] getForbiddenTags() {
        return null;
    }

    public boolean shouldShearCreatures() {
        return this.getToggles()[0].getValue();
    }

    public boolean shouldShearBlocks() {
        return this.getToggles()[1].getValue();
    }

    public boolean shouldPreserveShears() {
        return this.getToggles()[2].getValue();
    }

    public boolean shouldRequestProvisioning() {
        return this.getToggles()[3].getValue();
    }
}