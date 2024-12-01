package xzero.model;

import java.awt.Point;

/*
 * Ячейка, являющаяся составной частью поля и содержащая в себе метку
 */
public class Cell {

    // --------------------- Позиция метки -----------------------
    private Point _position;

    void setPosition(Point pos){
        _position = pos;
    }

    public Point position(){
        return (Point)_position.clone();
    }

    // --------- Метка, принадлежит полю. Принадлежность задает само поле ------
    private GameField _field;

    void setField(GameField f){
        _field = f;
    }

    // --------------------- Метка, принадлежащая ячейке -----------------------
    private Label _label;

    public Label label() {
        return _label;
    }

    public void setLabel(Label l )
    {
        if(_label != null)
        {
            throw new RuntimeException("в этой клетке уже установлена метка");
        }

        _label = l;
        l.setCell(this);
    }

    public void unsetLabel()
    {
        _label.unsetCell();
        _label = null;
    }

    public boolean isEmpty()
    {
        return _label == null;
    }
}
