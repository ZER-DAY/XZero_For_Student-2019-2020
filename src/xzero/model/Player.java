package xzero.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;

/**
 *  Игрок, который размещает предложенную ему метку
 */
public class Player {
    
// --------------------------------- Имя игрока -------------------------------    
    private String _name;
    
    public void setName(String name) {
        _name = name;
    }
    
    public String name() {
        return _name;
    }

 // ----------------------- Устанавливаем связь с полем -----------------------   
    GameField _field;
    
    public Player (GameField field, String name) {
        _field = field;
        _name = name;
    }
    
// ---------------------- Метка, которую нужно установить -----------------------    
    Label _label;
    
    public void setActiveLabel(Label l) {
        _label = l;
        _label.setPlayer(this);
        
        // Генерируем событие
        !!!
    }

    public Label activeLabel() {
        return _label;
    }
    
    public void setLabelTo(Point pos){
        
        // TODO породить исключение, если не задана активная метка 
        
        _field.setLabel(pos, _label);
        
        // Генерируем событие
        !!!

        // Повторно использовать метку нельзя
        _label = null;
    }
    
    private ArrayList<Label> _labels = new ArrayList<>();
    
    public List<Label> labels(){
        
        _labels.clear();
        for(Label obj: _field.labels())
        {
            if(obj.player().equals(this))
            { _labels.add(obj); }
        }
        
        return Collections.unmodifiableList(_labels);
    }   
    
    // ---------------------- Порождает события -----------------------------
    
    !!! 
 
    // Присоединяет слушателя
    public void addPlayerActionListener(PlayerActionListener l) { 
        !!!
    }
    
    // Отсоединяет слушателя
    public void removePlayerActionListener(PlayerActionListener l) { 
        !!! 
    } 
    
    // Оповещает слушателей о событии
    protected void fireLabelIsPlaced(Label l) {
        !!!
    }     

    protected void fireLabelIsReceived(Label l) {
        !!!
    }     
}
