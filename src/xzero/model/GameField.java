package xzero.model;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import xzero.model.navigation.Direction;
import xzero.model.navigation.Shift;

/**
 * Rectangular game field consisting of cells.
 * الحقل المستطيل الذي يتكون من خلايا.
 */
public class GameField {

// ----------------- Container of cells and labels -----------------
// Cell and label positions are specified by rows and columns in the range [1, height] and [1, width] respectively.
// ----------------- حاوية الخلايا والملصقات -----------------
// يتم تحديد مواقع الخلايا والملصقات بواسطة الصفوف والأعمدة ضمن النطاق [1, height] و[1, width] على التوالي.

    // ------------------------------ Cells ---------------------------------------
    // List to store all the cells in the game field.
    // قائمة لتخزين جميع الخلايا في الحقل.
    private ArrayList<Cell> _cellPool = new ArrayList<>();

    // Method to retrieve a cell at a specific position.
    // طريقة لاسترجاع خلية في موقع معين.
    Cell cell(Point pos){
        for(Cell obj : _cellPool) {
            if(obj.position().equals(pos)) {
                return obj;
            }
        }
        return null;
    }

    // Method to set a cell at a specific position.
    // طريقة لتعيين خلية في موقع معين.
    void setCell(Point pos, Cell cell){
        // Remove the old cell.
        // إزالة الخلية القديمة.
        removeCell(pos);

        // Link the new cell to the field.
        // ربط الخلية الجديدة بالحقل.
        cell.setField(this);
        cell.setPosition(pos);

        // Add the new cell.
        // إضافة الخلية الجديدة.
        _cellPool.add(cell);
    }

//    public List<Cell> cells(){
//        return Collections.unmodifiableList(_cellPool);
//    }

    // Method to clear all cells.
    // طريقة لمسح جميع الخلايا.
    public void clear(){
        _cellPool.clear();
    }

    // Method to remove a cell at a specific position.
    // طريقة لإزالة خلية في موقع معين.
    private void removeCell(Point pos){
        Cell obj = cell(pos);
        if(obj != null) _cellPool.remove(obj);
    }

    // ------------------------------ Labels ---------------------------------------
    // Method to retrieve a label at a specific position.
    // طريقة لاسترجاع ملصق في موقع معين.
    public Label label(Point pos){
        Cell obj = cell(pos);
        if(obj != null) return obj.label();
        return null;
    }

    // Method to set a label at a specific position.
    // طريقة لتعيين ملصق في موقع معين.
    public void setLabel(Point pos, Label label){
        Cell obj = cell(pos);
        if(obj != null) {
            obj.setLabel(label);
        }
    }

    // List to store all labels in the game field.
    // قائمة لتخزين جميع الملصقات في الحقل.
    private ArrayList<Label> _labelPool = new ArrayList<>();

    // Method to retrieve all labels in the game field.
    // طريقة لاسترجاع جميع الملصقات في الحقل.
    public List<Label> labels(){
        _labelPool.clear();
        for(Cell obj : _cellPool) {
            Label l = obj.label();
            if(l != null) {
                _labelPool.add(obj.label());
            }
        }
        return Collections.unmodifiableList(_labelPool);
    }

    // Method to return a sequence of labels belonging to one player.
    // طريقة لإرجاع سلسلة من الملصقات التي تخص لاعباً واحداً.
    public List<Label> labelLine(Point start, Direction direct){
        ArrayList<Label> line = new ArrayList<>();
        boolean isLineFinished = false;
        Player startPlayer = null;

        // Determine the first label and its corresponding player.
        // تحديد أول ملصق واللاعب المقابل له.
        Point pos = new Point(start);
        Label l = label(pos);

        isLineFinished = (l == null);
        if(!isLineFinished) {
            line.add(l);
            startPlayer = line.get(0).player();
        }

        Shift shift = direct.shift();
        pos.translate(shift.byHorizontal(), shift.byVertical());
        while(!isLineFinished && containsRange(pos)) {
            l = label(pos);
            isLineFinished = (l == null || !l.player().equals(startPlayer));

            if(!isLineFinished) {
                line.add(l);
            }
            pos.translate(shift.byHorizontal(), shift.byVertical());
        }

        return line;
    }

    // ----------------------- Field Width and Height ------------------------------
    // عرض وطول الحقل
    private int _width;
    private int _height;

    // Method to set the size of the game field.
    // طريقة لتعيين حجم الحقل.
    public void setSize(int width, int height) {
        _width = width;
        _height = height;

        // Remove all cells outside the field.
        // إزالة جميع الخلايا خارج الحقل.
        for (Cell obj : _cellPool) {
            if(!containsRange(obj.position())) {
                _cellPool.remove(obj);
            }
        }
    }

    // Method to get the width of the field.
    // طريقة للحصول على عرض الحقل.
    public int width() {
        return _width;
    }

    // Method to get the height of the field.
    // طريقة للحصول على ارتفاع الحقل.
    public int height() {
        return _height;
    }

    // Method to check if a position is within the field range.
    // طريقة للتحقق مما إذا كان الموقع ضمن نطاق الحقل.
    public boolean containsRange(Point p){
        return p.getX() >= 1 && p.getX() <= _width &&
                p.getY() >= 1 && p.getY() <= _height;
    }

    // ----------------------------------------------------------------------------
    // Constructor to initialize the game field with default size 10x10.
    // المُنشئ لتعيين الحقل بحجم افتراضي 10x10.
    public GameField() {
        setSize(10, 10);
    }
}
