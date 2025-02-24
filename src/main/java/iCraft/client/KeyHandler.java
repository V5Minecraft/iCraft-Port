package iCraft.client;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class KeyHandler {
   public KeyBinding[] keyBindings;
   public boolean[] keyDown;
   public boolean[] repeatings;
   public boolean isDummy;

   public KeyHandler(KeyBinding[] bindings, boolean[] rep) {
      assert this.keyBindings.length == this.repeatings.length : "You need to pass two arrays of identical length";

      this.keyBindings = bindings;
      this.repeatings = rep;
      this.keyDown = new boolean[this.keyBindings.length];
   }

   public KeyHandler(KeyBinding[] bindings) {
      this.keyBindings = bindings;
      this.repeatings = new boolean[bindings.length];
      this.keyDown = new boolean[bindings.length];
      this.isDummy = true;
   }

   public static boolean isPressed(KeyBinding keyBinding) {
      int keyCode = keyBinding.getKeyCode();
      return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
   }

   public KeyBinding[] getKeyBindings() {
      return this.keyBindings;
   }

   public void keyTick() {
      for (int i = 0; i < this.keyBindings.length; ++i) {
         KeyBinding keyBinding = this.keyBindings[i];
         boolean state = isPressed(keyBinding);
         if (state != this.keyDown[i] || (state && this.repeatings[i])) {
            if (state) {
               this.keyDown(keyBinding, state != this.keyDown[i]);
            } else {
               this.keyUp(keyBinding);
            }

            this.keyDown[i] = state;
         }
      }
   }

   public abstract void keyDown(KeyBinding keyBinding, boolean pressed);

   public abstract void keyUp(KeyBinding keyBinding);
}
