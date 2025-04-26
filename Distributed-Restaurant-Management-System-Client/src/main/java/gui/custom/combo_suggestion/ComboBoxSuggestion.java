package gui.custom.combo_suggestion;

import gui.custom.RoundedComboBox;
import javax.swing.JComboBox;

public class ComboBoxSuggestion<E> extends RoundedComboBox {

    public ComboBoxSuggestion() {
        setUI(new ComboSuggestionUI());
    }
}
