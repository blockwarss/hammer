package fr.survietiktok.hammeraddon;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public final class HammerHandle {

    private final Plugin hammerPlugin;
    private Method isHammerMethod;

    public HammerHandle(Plugin hammerPlugin) {
        this.hammerPlugin = hammerPlugin;
        try {
            Class<?> main = hammerPlugin.getClass();
            for (Method m : main.getDeclaredMethods()) {
                if (m.getName().equals("isHammer")
                        && m.getParameterCount() == 1
                        && ItemStack.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.setAccessible(true);
                    this.isHammerMethod = m;
                    break;
                }
            }
        } catch (Throwable t) {
        }
    }

    public boolean isReady() {
        return this.isHammerMethod != null;
    }

    public boolean isHammer(ItemStack stack) {
        if (isHammerMethod == null) return false;
        try {
            Object res = isHammerMethod.invoke(hammerPlugin, stack);
            return (res instanceof Boolean) ? (Boolean) res : false;
        } catch (Throwable t) {
            return false;
        }
    }
}