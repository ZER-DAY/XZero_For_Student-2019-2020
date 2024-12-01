package xzero.model.factory;

import xzero.model.Label;

/**
 * Фабрика, порождающая метку. Реализует самую простую стратегию
 */
public class LabelFactory {
    public Label createLabel() {
        // i make to return to label;
        return new Label();
    }
}
