package net.cybertekt.input;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFW;

/**
 * Input - (C) Cybertekt Software
 *
 * Defines Cybertekt Engine Input Constants.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public interface Input {

    /**
     * Constants that represent the three possible input states.
     */
    public static enum State {
        /**
         * Indicates that an input event has been activated.
         */
        Pressed,
        /**
         * Indicates that an input event is in an activated state.
         */
        Held,
        /**
         * Indicates that an input event has been deactivated.
         */
        Released;
    }

    /**
     * Constants that represent the four input modifiers.
     */
    public static enum Mod implements Input {
        /**
         * Indicates the left and/or right alt key.
         */
        Alt,
        /**
         * Indicates the left and/or right ctrl key.
         */
        Ctrl,
        /**
         * Indicates the left and/or right shift key.
         */
        Shift,
        /**
         * Indicates the left and/or right super key.
         */
        Super;
    }

    /**
     * Constants that represent mouse input events.
     */
    public static enum Mouse implements Input {
        Left, Right, Middle, Forward, Back, Six, Seven, Eight, ScrollUp, ScrollDown;
    }

    /**
     * Constants that represent keyboard input events.
     */
    public static enum Key implements Input {
        // Input Constant For Unrecognized Key Inputs //
        Unknown,
        // Alphabetic Key Inputs //
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
        // Punctuation Key Inputs //
        Period, Comma, Apostrophe, Semicolon, Accent, Slash, Backslash, BracketLeft, BracketRight,
        // Numeric Key Inputs //
        Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Minus, Equal,
        // Formatting Key Inputs //
        Tab, Space, Backspace,
        // Modifier Key Inputs //
        AltLeft, AltRight, CtrlLeft, CtrlRight, ShiftLeft, ShiftRight, SuperLeft, SuperRight,
        // Named Function Key Inputs //
        Enter, Escape, Insert, Delete, Home, End, Last, Menu, Pause, Print, PageUp, PageDown, CapsLock, ScrollLock,
        // Numbered Function Key Inputs //
        F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14,
        F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25,
        // Arrow Key Inputs //
        Up, Down, Left, Right,
        // Keypad Numeric Inputs //
        KP_Zero, KP_One, KP_Two, KP_Three, KP_Four, KP_Five, KP_Six, KP_Seven, KP_Eight, KP_Nine,
        // Keypad Function Inputs //
        KP_Add, KP_Subtract, KP_Multiply, KP_Divide, KP_Equal, KP_Enter, KP_Decimal, KP_Lock;
    }

    /**
     * Unmodifiable map that associates GLFW input states with engine specific
     * {@link Input.State input states}.
     */
    public static final Map<Integer, Input.State> STATE_MAP = Collections.unmodifiableMap(new HashMap() {
        {
            put(GLFW.GLFW_PRESS, Input.State.Pressed);
            put(GLFW.GLFW_REPEAT, Input.State.Held);
            put(GLFW.GLFW_RELEASE, Input.State.Released);
        }
    });

    /**
     * Unmodifiable map that associates GLFW mouse input constants with engine
     * specific {@link Input.Mouse mouse inputs}.
     */
    public static final Map<Integer, Input.Mouse> MOUSE_MAP = Collections.unmodifiableMap(new HashMap() {
        {
            put(GLFW.GLFW_MOUSE_BUTTON_LEFT, Input.Mouse.Left);
            put(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, Input.Mouse.Middle);
            put(GLFW.GLFW_MOUSE_BUTTON_RIGHT, Input.Mouse.Right);
            put(GLFW.GLFW_MOUSE_BUTTON_4, Input.Mouse.Back);
            put(GLFW.GLFW_MOUSE_BUTTON_5, Input.Mouse.Forward);
            put(GLFW.GLFW_MOUSE_BUTTON_6, Input.Mouse.Six);
            put(GLFW.GLFW_MOUSE_BUTTON_7, Input.Mouse.Seven);
            put(GLFW.GLFW_MOUSE_BUTTON_8, Input.Mouse.Eight);
        }
    });

    /**
     * Unmodifiable map that associates GLFW key input constants with engine
     * specific {@link Input.Mod input modifiers}.
     */
    public static final Map<Integer, Input.Mod> MOD_MAP = Collections.unmodifiableMap(new HashMap() {
        {
            // Shift Input Modifier //
            put(GLFW.GLFW_KEY_LEFT_SHIFT, Input.Mod.Shift);
            put(GLFW.GLFW_KEY_RIGHT_SHIFT, Input.Mod.Shift);

            // Ctrl Input Modifier //
            put(GLFW.GLFW_KEY_LEFT_CONTROL, Input.Mod.Ctrl);
            put(GLFW.GLFW_KEY_RIGHT_CONTROL, Input.Mod.Ctrl);

            // Alt Input Modifier //
            put(GLFW.GLFW_KEY_LEFT_ALT, Input.Mod.Alt);
            put(GLFW.GLFW_KEY_RIGHT_ALT, Input.Mod.Alt);

            // Super Input Modifier //
            put(GLFW.GLFW_KEY_LEFT_SUPER, Input.Mod.Super);
            put(GLFW.GLFW_KEY_RIGHT_SUPER, Input.Mod.Super);
        }
    });

    /**
     * Unmodifiable map that associates GLFW key input constants with engine
     * specific {@link Input.Key input keys}.
     */
    public static final Map<Integer, Input.Key> KEY_MAP = Collections.unmodifiableMap(new HashMap() {
        {
            put(GLFW.GLFW_KEY_UNKNOWN, Input.Key.Unknown);

            put(GLFW.GLFW_KEY_A, Input.Key.A);
            put(GLFW.GLFW_KEY_B, Input.Key.B);
            put(GLFW.GLFW_KEY_C, Input.Key.C);
            put(GLFW.GLFW_KEY_D, Input.Key.D);
            put(GLFW.GLFW_KEY_E, Input.Key.E);
            put(GLFW.GLFW_KEY_F, Input.Key.F);
            put(GLFW.GLFW_KEY_G, Input.Key.G);
            put(GLFW.GLFW_KEY_H, Input.Key.H);
            put(GLFW.GLFW_KEY_I, Input.Key.I);
            put(GLFW.GLFW_KEY_J, Input.Key.J);
            put(GLFW.GLFW_KEY_K, Input.Key.K);
            put(GLFW.GLFW_KEY_L, Input.Key.L);
            put(GLFW.GLFW_KEY_M, Input.Key.M);
            put(GLFW.GLFW_KEY_N, Input.Key.N);
            put(GLFW.GLFW_KEY_O, Input.Key.O);
            put(GLFW.GLFW_KEY_P, Input.Key.P);
            put(GLFW.GLFW_KEY_Q, Input.Key.Q);
            put(GLFW.GLFW_KEY_R, Input.Key.R);
            put(GLFW.GLFW_KEY_S, Input.Key.S);
            put(GLFW.GLFW_KEY_T, Input.Key.T);
            put(GLFW.GLFW_KEY_U, Input.Key.U);
            put(GLFW.GLFW_KEY_V, Input.Key.V);
            put(GLFW.GLFW_KEY_W, Input.Key.W);
            put(GLFW.GLFW_KEY_X, Input.Key.X);
            put(GLFW.GLFW_KEY_Y, Input.Key.Y);
            put(GLFW.GLFW_KEY_Z, Input.Key.Z);

            put(GLFW.GLFW_KEY_PERIOD, Input.Key.Period);
            put(GLFW.GLFW_KEY_COMMA, Input.Key.Comma);
            put(GLFW.GLFW_KEY_APOSTROPHE, Input.Key.Apostrophe);
            put(GLFW.GLFW_KEY_SEMICOLON, Input.Key.Semicolon);
            put(GLFW.GLFW_KEY_GRAVE_ACCENT, Input.Key.Accent);
            put(GLFW.GLFW_KEY_SLASH, Input.Key.Slash);
            put(GLFW.GLFW_KEY_BACKSLASH, Input.Key.Backslash);
            put(GLFW.GLFW_KEY_LEFT_BRACKET, Input.Key.BracketLeft);
            put(GLFW.GLFW_KEY_RIGHT_BRACKET, Input.Key.BracketRight);

            put(GLFW.GLFW_KEY_SPACE, Input.Key.Space);
            put(GLFW.GLFW_KEY_TAB, Input.Key.Tab);
            put(GLFW.GLFW_KEY_BACKSPACE, Input.Key.Backspace);

            put(GLFW.GLFW_KEY_LEFT_ALT, Input.Key.AltLeft);
            put(GLFW.GLFW_KEY_RIGHT_ALT, Input.Key.AltRight);
            put(GLFW.GLFW_KEY_LEFT_CONTROL, Input.Key.CtrlLeft);
            put(GLFW.GLFW_KEY_RIGHT_CONTROL, Input.Key.CtrlRight);
            put(GLFW.GLFW_KEY_LEFT_SHIFT, Input.Key.ShiftLeft);
            put(GLFW.GLFW_KEY_RIGHT_SHIFT, Input.Key.ShiftRight);
            put(GLFW.GLFW_KEY_LEFT_SUPER, Input.Key.SuperLeft);
            put(GLFW.GLFW_KEY_RIGHT_SUPER, Input.Key.SuperRight);

            put(GLFW.GLFW_KEY_ENTER, Input.Key.Enter);
            put(GLFW.GLFW_KEY_ESCAPE, Input.Key.Escape);
            put(GLFW.GLFW_KEY_INSERT, Input.Key.Insert);
            put(GLFW.GLFW_KEY_DELETE, Input.Key.Delete);
            put(GLFW.GLFW_KEY_HOME, Input.Key.Home);
            put(GLFW.GLFW_KEY_END, Input.Key.End);
            put(GLFW.GLFW_KEY_LAST, Input.Key.Last);
            put(GLFW.GLFW_KEY_MENU, Input.Key.Menu);
            put(GLFW.GLFW_KEY_PAUSE, Input.Key.Pause);
            put(GLFW.GLFW_KEY_PRINT_SCREEN, Input.Key.Print);
            put(GLFW.GLFW_KEY_PAGE_UP, Input.Key.PageUp);
            put(GLFW.GLFW_KEY_PAGE_DOWN, Input.Key.PageDown);
            put(GLFW.GLFW_KEY_CAPS_LOCK, Input.Key.CapsLock);
            put(GLFW.GLFW_KEY_SCROLL_LOCK, Input.Key.ScrollLock);

            put(GLFW.GLFW_KEY_UP, Input.Key.Up);
            put(GLFW.GLFW_KEY_DOWN, Input.Key.Down);
            put(GLFW.GLFW_KEY_LEFT, Input.Key.Left);
            put(GLFW.GLFW_KEY_RIGHT, Input.Key.Right);

            put(GLFW.GLFW_KEY_F1, Input.Key.F1);
            put(GLFW.GLFW_KEY_F2, Input.Key.F2);
            put(GLFW.GLFW_KEY_F3, Input.Key.F3);
            put(GLFW.GLFW_KEY_F4, Input.Key.F4);
            put(GLFW.GLFW_KEY_F5, Input.Key.F5);
            put(GLFW.GLFW_KEY_F6, Input.Key.F6);
            put(GLFW.GLFW_KEY_F7, Input.Key.F7);
            put(GLFW.GLFW_KEY_F8, Input.Key.F8);
            put(GLFW.GLFW_KEY_F9, Input.Key.F9);
            put(GLFW.GLFW_KEY_F10, Input.Key.F10);
            put(GLFW.GLFW_KEY_F11, Input.Key.F11);
            put(GLFW.GLFW_KEY_F12, Input.Key.F12);
            put(GLFW.GLFW_KEY_F13, Input.Key.F13);
            put(GLFW.GLFW_KEY_F14, Input.Key.F14);
            put(GLFW.GLFW_KEY_F15, Input.Key.F15);
            put(GLFW.GLFW_KEY_F16, Input.Key.F16);
            put(GLFW.GLFW_KEY_F17, Input.Key.F17);
            put(GLFW.GLFW_KEY_F18, Input.Key.F18);
            put(GLFW.GLFW_KEY_F19, Input.Key.F19);
            put(GLFW.GLFW_KEY_F20, Input.Key.F20);
            put(GLFW.GLFW_KEY_F21, Input.Key.F21);
            put(GLFW.GLFW_KEY_F22, Input.Key.F22);
            put(GLFW.GLFW_KEY_F23, Input.Key.F23);
            put(GLFW.GLFW_KEY_F24, Input.Key.F24);
            put(GLFW.GLFW_KEY_F25, Input.Key.F25);

            put(GLFW.GLFW_KEY_0, Input.Key.Zero);
            put(GLFW.GLFW_KEY_1, Input.Key.One);
            put(GLFW.GLFW_KEY_2, Input.Key.Two);
            put(GLFW.GLFW_KEY_3, Input.Key.Three);
            put(GLFW.GLFW_KEY_4, Input.Key.Four);
            put(GLFW.GLFW_KEY_5, Input.Key.Five);
            put(GLFW.GLFW_KEY_6, Input.Key.Six);
            put(GLFW.GLFW_KEY_7, Input.Key.Seven);
            put(GLFW.GLFW_KEY_8, Input.Key.Eight);
            put(GLFW.GLFW_KEY_9, Input.Key.Nine);
            put(GLFW.GLFW_KEY_MINUS, Input.Key.Minus);
            put(GLFW.GLFW_KEY_EQUAL, Input.Key.Equal);

            put(GLFW.GLFW_KEY_KP_0, Input.Key.KP_Zero);
            put(GLFW.GLFW_KEY_KP_1, Input.Key.KP_One);
            put(GLFW.GLFW_KEY_KP_2, Input.Key.KP_Two);
            put(GLFW.GLFW_KEY_KP_3, Input.Key.KP_Three);
            put(GLFW.GLFW_KEY_KP_4, Input.Key.KP_Four);
            put(GLFW.GLFW_KEY_KP_5, Input.Key.KP_Five);
            put(GLFW.GLFW_KEY_KP_6, Input.Key.KP_Six);
            put(GLFW.GLFW_KEY_KP_7, Input.Key.KP_Seven);
            put(GLFW.GLFW_KEY_KP_8, Input.Key.KP_Eight);
            put(GLFW.GLFW_KEY_KP_9, Input.Key.KP_Nine);

            put(GLFW.GLFW_KEY_KP_ADD, Input.Key.KP_Add);
            put(GLFW.GLFW_KEY_KP_SUBTRACT, Input.Key.KP_Subtract);
            put(GLFW.GLFW_KEY_KP_MULTIPLY, Input.Key.KP_Multiply);
            put(GLFW.GLFW_KEY_KP_DIVIDE, Input.Key.KP_Divide);

            put(GLFW.GLFW_KEY_KP_ENTER, Input.Key.KP_Enter);
            put(GLFW.GLFW_KEY_KP_DECIMAL, Input.Key.KP_Decimal);
            put(GLFW.GLFW_KEY_KP_EQUAL, Input.Key.KP_Equal);
            put(GLFW.GLFW_KEY_NUM_LOCK, Input.Key.KP_Lock);
        }
    });
}
