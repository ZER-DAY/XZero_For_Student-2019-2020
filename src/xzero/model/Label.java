package xzero.model;

/**
 * Метка, которую можно поместить на поле
 */
public class Label {

    // --------------- Ячейка, которой прнадлежит метка. Задает сама ячейка -------
    private Cell _cell;

    public void setCell(Cell c)
    {
        if(_cell != null)
        {
            throw new RuntimeException("метка уже установленна в ячейку");
        }
        this._cell = c;
    }
    public void unsetCell()
    {
        this._cell = null;
    }
    public Cell cell(){
        return _cell;
    }

// - Игрок, которому прнадлежит метка. Метка может быть нейтральной (не принадлежать никому) -

    private Player _player = null;

    void setPlayer(Player p){
        if(_player != null)
        {
            throw new RuntimeException("метка уже присвоена игроку");
        }
        _player = p;
    }

    void unsetPlayer(){
        _player = null;
    }

    public Player player(){
        return _player;
    }
}
