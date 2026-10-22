package cn.noryea.fastitems;

/** Wird per Mixin auf ItemRenderState und LayerRenderState gesetzt. */
public interface Flattenable {
    /** Entfernt alle Quads außer der Vorderseite (SOUTH) -> Item wird flach. */
    void fastitems$flatten();
}
