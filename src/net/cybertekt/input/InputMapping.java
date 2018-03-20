package net.cybertekt.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Input Mapping - (C) Cybertekt Software
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class InputMapping {

    private final List<Input> inputs;

    private final List<Input> events;

    private boolean sequence;

    private boolean exclusive;

    private boolean activated;

    public InputMapping(final Input... inputs) {
        this(false, false, inputs);
    }

    public InputMapping(final boolean exclusive, final boolean sequence, final Input... inputs) {
        this.exclusive = exclusive;
        this.sequence = sequence;
        this.inputs = Arrays.asList(inputs);
        this.events = new ArrayList<>();
    }
    
    public void onInput(Input event, final Input.State state) {

        // Convert Event To Modifier If Necessary //
        if (!inputs.contains(event)) {
            Input.Mod mod = getModifier(event);
            if (mod != null && inputs.contains(mod)) {
                event = mod;
            }
        }

        // Update Event List //
        if (state == Input.State.Pressed) {
            events.add(event);
        } else {
            events.remove(event);
        }

        // Determine Activation State //
        if (events.containsAll(inputs)) {
            if (checkExclusive() && checkSequence()) {
                if (state == Input.State.Pressed) {
                    if (!activated) {
                        onAction(activated = true);
                    }
                }
            } else {
                if (activated) {
                    onAction(activated = false);
                }
            }
        } else {
            if (activated) {
                onAction(activated = false);
            }
        }
    }

    public void onAction(final boolean activate) {
        // No Default Implementation //
    }
    
    public boolean isActivated() {
        return activated;
    }

    private boolean checkExclusive() {
        if (exclusive) {
            Iterator<Input> it = events.iterator();
            while (it.hasNext()) {
                if (!inputs.contains(it.next())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkSequence() {
        if (sequence) {
            int start = events.lastIndexOf(inputs.get(0));

            if (events.size() < start + inputs.size()) {
                return false;
            }

            for (int i = 0; i < inputs.size(); i++) {
                if (!events.get(start).equals(inputs.get(i))) {
                    return false;
                }
                start++;
            }
        }
        return true;
    }
    
    private Input.Mod getModifier(final Input input) {
        if (input == Input.Key.ShiftLeft || input == Input.Key.ShiftRight) {
            return Input.Mod.Shift;
        }
        if (input == Input.Key.CtrlLeft || input == Input.Key.CtrlRight) {
            return Input.Mod.Ctrl;
        }
        if (input == Input.Key.AltLeft || input == Input.Key.AltRight) {
            return Input.Mod.Alt;
        }
        if (input == Input.Key.SuperLeft || input == Input.Key.SuperRight) {
            return Input.Mod.Super;
        }
        return null;
    }
}
