package fr.survietiktok.hammeraddon;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public final class HammerHandle {

    private Plugin hammerPlugin;
    private Method isHammerMethod;

    public boolean tryAttach(Plugin plugin) {
        if (plugin == null) return false;
        try {
            for (Method m : plugin.getClass().getDeclaredMethods()) {
                if (!m.getName().equals("isHammer")) continue;
                if (m.getParameterCount() != 1) continue;
                if (!ItemStack.class.isAssignableFrom(m.getParameterTypes()[0])) continue;
                m.setAccessible(true);
                this.hammerPlugin = plugin;
                this.isHammerMethod = m;
                return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }

    public boolean isReady() {
        return this.hammerPlugin != null && this.isHammerMethod != null;
    }

    public boolean isHammer(ItemStack stack) {
        if (!isReady()) return false;
        try {
            Object res = isHammerMethod.invoke(hammerPlugin, stack);
            return (res instanceof Boolean) ? (Boolean) res : false;
        } catch (Throwable t) {
            return false;
        }
    }
}