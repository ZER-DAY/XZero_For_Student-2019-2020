package xzero.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import xzero.model.events.GameEvent;
import xzero.model.events.GameListener;
import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;
import xzero.model.factory.CellFactory;
import xzero.model.factory.LabelFactory;
import xzero.model.navigation.Direction;

/**
 * Abstraction of the entire game; generates the starting setup;
 * alternates turns between players by assigning them labels to place on the field;
 * monitors players to determine the end of the game.
 *
 * تجريد كامل للعبة؛ يقوم بإنشاء الوضعية الابتدائية؛
 * يقوم بتناوب الأدوار بين اللاعبين من خلال تخصيص علامات لوضعها على اللوحة؛
 * يراقب اللاعبين لتحديد نهاية اللعبة.
 */
public class
GameModel {

// -------------------------------- Field / لوحة -------------------------------------

    // Game field
    private GameField field = new GameField();

    /**
     * Retrieves the game field.
     *
     * استرداد اللوحة.
     */
    public GameField field() {
        return field;
    }

// -------------------------------- Players / اللاعبين -----------------------------------

    private ArrayList<Player> _playerList = new ArrayList<>();
    private int _activePlayer;

    /**
     * Retrieves the active player.
     *
     * استرداد اللاعب النشط.
     */
    public Player activePlayer() {
        return _playerList.get(_activePlayer);
    }

    /**
     * Constructor to initialize the game model.
     *
     * منشئ لتهيئة نموذج اللعبة.
     */
    public GameModel() {
        // Set default field size
        field().setSize(5, 5);

        // Create two players
        Player p;
        PlayerObserver observer = new PlayerObserver();

        p = new Player(field(), "X");
        // Add an observer to monitor the player
        p.addPlayerActionListener(observer);
        _playerList.add(p);
        _activePlayer = 0;

        p = new Player(field(), "O");
        // Add an observer to monitor the player
        p.addPlayerActionListener(observer);
        _playerList.add(p);
    }

// ---------------------- Field Initialization / تهيئة اللوحة ---------------------

    private CellFactory _cellFactory = new CellFactory();

    /**
     * Generates the game field and initializes cells.
     *
     * إنشاء اللوحة وتهيئة الخلايا.
     */
    private void generateField() {
        field().clear();
        field().setSize(5, 5);
        for (int row = 1; row <= field().height(); row++) {
            for (int col = 1; col <= field().width(); col++) {
                field().setCell(new Point(col, row), _cellFactory.createCell());
            }
        }
    }

// ----------------------------- Game Process / سير اللعبة ----------------------------

    /**
     * Starts the game by generating the field and alternating the players' turns.
     *
     * بدء اللعبة من خلال إنشاء اللوحة وتناوب أدوار اللاعبين.
     */
    public void start() {
        generateField();
        _activePlayer = _playerList.size() - 1;
        exchangePlayer();
    }

    private LabelFactory _labelFactory = new LabelFactory();

    /**
     * Alternates turns between players and assigns a new label.
     *
     * تبديل الأدوار بين اللاعبين وتعيين علامة جديدة.
     */
    private void exchangePlayer() {
        _activePlayer++;
        if (_activePlayer >= _playerList.size()) {
            _activePlayer = 0;
        }

        Label newLabel = _labelFactory.createLabel();
        activePlayer().setActiveLabel(newLabel);

        firePlayerExchanged(activePlayer());
    }

    private static int WINNER_LINE_LENGTH = 5;

    /**
     * Determines the winner of the game.
     *
     * تحديد الفائز في اللعبة.
     */
    private Player determineWinner() {
        for (int row = 1; row <= field().height(); row++) {
            for (int col = 1; col <= field().width(); col++) {
                Point pos = new Point(col, row);
                Direction direct = Direction.north();
                for (int i = 1; i <= 8; i++) {
                    direct = direct.rightword();

                    List<Label> line = field().labelLine(pos, direct);

                    if (line.size() >= WINNER_LINE_LENGTH) {
                        return line.get(0).player();
                    }
                }
            }
        }
        return null;
    }

// ------------------------- Player Actions / أفعال اللاعب ------------------

    private class PlayerObserver implements PlayerActionListener {

        @Override
        public void labelIsPlaced(PlayerActionEvent e) {
            if (e.player() == activePlayer()) {
                fireLabelIsPlaced(e);
            }

            Player winner = determineWinner();

            if (winner == null) {
                exchangePlayer();
            } else {
                fireGameFinished(winner);
            }
        }

        @Override
        public void labelIsReceived(PlayerActionEvent e) {
            if (e.player() == activePlayer()) {
                fireLabelIsReceived(e);
            }
        }
    }

// ------------------------ Game Events / أحداث اللعبة ------------------------

    private ArrayList<GameListener> _listenerList = new ArrayList<>();

    public void addGameListener(GameListener l) {
        _listenerList.add(l);
    }

    public void removeGameListener(GameListener l) {
        _listenerList.remove(l);
    }

    protected void fireGameFinished(Player winner) {
        GameEvent event = new GameEvent(this);
        event.setPlayer(winner);
        for (GameListener listener : _listenerList) {
            listener.gameFinished(event);
        }
    }

    protected void firePlayerExchanged(Player p) {
        GameEvent event = new GameEvent(this);
        event.setPlayer(p);
        for (GameListener listener : _listenerList) {
            listener.playerExchanged(event);
        }
    }

// --------------------- Player Events / أحداث اللاعبين ----------------------

    private ArrayList<PlayerActionListener> _playerListenerList = new ArrayList<>();

    public void addPlayerActionListener(PlayerActionListener l) {
        _playerListenerList.add(l);
    }

    public void removePlayerActionListener(PlayerActionListener l) {
        _playerListenerList.remove(l);
    }

    protected void fireLabelIsPlaced(PlayerActionEvent e) {
        for (PlayerActionListener listener : _playerListenerList) {
            listener.labelIsPlaced(e);
        }
    }

    protected void fireLabelIsReceived(PlayerActionEvent e) {
        for (PlayerActionListener listener : _playerListenerList) {
            listener.labelIsReceived(e);
        }
    }
}
