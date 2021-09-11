package com.nuggylib.enchantmentsseer.common.lib;

import java.util.Objects;

/**
 * Copied from Mekanism
 *
 * Helper class to create new colors; used when registering various colors
 */
public class Color {

    private final double r, g, b, a;

    private Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int r() {
        return (int) Math.round(r * 255D);
    }

    public int g() {
        return (int) Math.round(g * 255D);
    }

    public int b() {
        return (int) Math.round(b * 255D);
    }

    public int a() {
        return (int) Math.round(a * 255D);
    }

    public int argb() {
        return (a() & 0xFF) << 24 | (r() & 0xFF) << 16 | (g() & 0xFF) << 8 | (b() & 0xFF);
    }

    public static Color rgbai(int r, int g, int b, int a) {
        return new Color(r / 255D, g / 255D, b / 255D, a / 255D);
    }

    public static Color argbi(int a, int r, int g, int b) {
        return rgbai(r, g, b, a);
    }

    public static Color argb(int color) {
        return argbi((color >> 24) & 0xFF, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
    }

    public int rgba() {
        return (r() & 0xFF) << 24 | (g() & 0xFF) << 16 | (b() & 0xFF) << 8 | (a() & 0xFF);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Color)) {
            return false;
        }
        Color other = (Color) obj;
        return r == other.r && g == other.g && b == other.b && a == other.a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }

    @Override
    public String toString() {
        return "[Color: " + r + ", " + g + ", " + b + ", " + a + "]";
    }

}
